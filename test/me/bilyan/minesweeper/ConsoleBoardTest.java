package me.bilyan.minesweeper;

import me.bilyan.minesweeper.exceptions.InvalidBoardPositionException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ConsoleBoardTest {
    private static final Difficulty DEFAULT_DIFFICULTY = Difficulty.BEGINNER;
    private static final ByteArrayOutputStream DEFAULT_BYTE_STREAM = new ByteArrayOutputStream();
    private static final PrintStream DEFAULT_RENDER_STREAM = new PrintStream(DEFAULT_BYTE_STREAM);

    private static final String OUTPUT_MINES =
            "     0  1  2  3  4  5  6  7  8 \n" +
            "0    *  –  –  –  –  –  –  –  – \n" +
            "1    –  *  –  –  –  –  –  –  – \n" +
            "2    –  –  *  –  –  –  –  –  – \n" +
            "3    –  –  –  *  –  –  –  –  – \n" +
            "4    –  –  –  –  *  –  –  –  – \n" +
            "5    –  –  –  –  –  *  –  –  – \n" +
            "6    –  –  –  –  –  –  *  –  – \n" +
            "7    1  –  –  –  –  –  –  *  – \n" +
            "8    *  –  –  –  –  –  –  –  * \n";

    private static final String OUTPUT_REVEALED_TILES =
            "     0  1  2  3  4  5  6  7  8 \n" +
            "0    –  –  1  .  .  .  .  .  . \n" +
            "1    –  –  2  1  .  .  .  .  . \n" +
            "2    –  –  –  2  1  .  .  .  . \n" +
            "3    –  –  –  –  2  1  .  .  . \n" +
            "4    –  –  –  –  –  2  1  .  . \n" +
            "5    –  –  –  –  –  –  2  1  . \n" +
            "6    –  –  –  –  –  –  –  2  1 \n" +
            "7    –  –  –  –  –  –  –  –  – \n" +
            "8    –  –  –  –  –  –  –  –  – \n";

    private static final String OUTPUT_NOTHING_REVEALED =
            "     0  1  2  3  4  5  6  7  8 \n" +
            "0    –  –  –  –  –  –  –  –  – \n" +
            "1    –  –  –  –  –  –  –  –  – \n" +
            "2    –  –  –  –  –  –  –  –  – \n" +
            "3    –  –  –  –  –  –  –  –  – \n" +
            "4    –  –  –  –  –  –  –  –  – \n" +
            "5    –  –  –  –  –  –  –  –  – \n" +
            "6    –  –  –  –  –  –  –  –  – \n" +
            "7    –  –  –  –  –  –  –  –  – \n" +
            "8    –  –  –  –  –  –  –  –  – \n";

    private static final String OUTPUT_FIRST_ON_MINE =
            "     0  1  2  3  4  5  6  7  8 \n" +
            "0    *  –  –  –  –  –  –  –  – \n" +
            "1    –  *  –  –  –  –  –  –  – \n" +
            "2    –  –  *  –  –  –  –  –  – \n" +
            "3    –  –  –  *  –  –  –  –  – \n" +
            "4    –  –  –  –  *  –  –  –  – \n" +
            "5    –  –  –  –  –  *  –  –  – \n" +
            "6    –  –  –  –  –  –  *  –  – \n" +
            "7    –  –  –  –  –  –  –  *  – \n" +
            "8    1  *  –  –  –  –  –  –  * \n";

    @Mock
    private Random mockRandom;

    private ConsoleBoard defaultBoard;

    @Before
    public void initializeBoard() {
        setUpMockRandom();
        this.defaultBoard = new ConsoleBoard(DEFAULT_DIFFICULTY, mockRandom, DEFAULT_RENDER_STREAM);
    }

    @Test(expected = InvalidBoardPositionException.class)
    public void testRevealTileThrowsForNegativeTilePosition() throws InvalidBoardPositionException {
        defaultBoard.revealTile(new IntPair(-1, -1));
    }

    @Test(expected = InvalidBoardPositionException.class)
    public void testRevealTileThrowsForOutOfBoundsTilePosition() throws InvalidBoardPositionException {
        defaultBoard.revealTile(new IntPair(Difficulty.BEGINNER.getRowsCount(), 0));
    }

    @Test
    public void testBoardInitializesProperly() {
        resetByteOutputStream(DEFAULT_BYTE_STREAM);

        assertEquals(DEFAULT_DIFFICULTY.getRowsCount() * DEFAULT_DIFFICULTY.getColsCount(),
                defaultBoard.getAllTilesCount());

        assertEquals(0, defaultBoard.getRevealedSafeTilesCount());
        assertFalse(defaultBoard.hasRevealedMine());

        defaultBoard.render();
        assertEquals(OUTPUT_NOTHING_REVEALED, DEFAULT_BYTE_STREAM.toString());
    }

    @Test
    public void testRevealTileRevealsNeighboringEmptyTiles() throws InvalidBoardPositionException {
        resetByteOutputStream(DEFAULT_BYTE_STREAM);

        defaultBoard.revealTile(new IntPair(0, 8));

        assertEquals(34, defaultBoard.getRevealedSafeTilesCount());

        defaultBoard.render();
        assertEquals(OUTPUT_REVEALED_TILES, DEFAULT_BYTE_STREAM.toString());
    }

    @Test
    public void testRevealMineRendersMines() throws InvalidBoardPositionException {
        resetByteOutputStream(DEFAULT_BYTE_STREAM);

        defaultBoard.revealTile(new IntPair(7, 0));
        defaultBoard.revealTile(new IntPair(8, 0));

        defaultBoard.render();
        assertTrue(defaultBoard.hasRevealedMine());
        assertEquals(OUTPUT_MINES, DEFAULT_BYTE_STREAM.toString());
    }

    @Test
    public void testFirstGuessOnMineMovesMine() throws InvalidBoardPositionException {
        resetByteOutputStream(DEFAULT_BYTE_STREAM);

        defaultBoard.revealTile(new IntPair(8, 0));
        defaultBoard.revealTile(new IntPair(8, 1));

        defaultBoard.render();
        assertEquals(OUTPUT_FIRST_ON_MINE, DEFAULT_BYTE_STREAM.toString());
    }

    private void setUpMockRandom() {
        when(mockRandom.nextInt(anyInt()))
                .thenReturn(0).thenReturn(0)
                .thenReturn(1).thenReturn(1)
                .thenReturn(2).thenReturn(2)
                .thenReturn(3).thenReturn(3)
                .thenReturn(4).thenReturn(4)
                .thenReturn(5).thenReturn(5)
                .thenReturn(6).thenReturn(6)
                .thenReturn(7).thenReturn(7)
                .thenReturn(8).thenReturn(8)
                .thenReturn(8).thenReturn(0)
                .thenReturn(8).thenReturn(1);
    }

    private void resetByteOutputStream(ByteArrayOutputStream stream) {
        stream.reset();
    }
}
package com.github.bfallstrom.ominoslide.junit;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.bfallstrom.ominoslide.areastructure.Board;
import com.github.bfallstrom.ominoslide.areastructure.Direction;
import com.github.bfallstrom.ominoslide.areastructure.Omino;
import com.github.bfallstrom.ominoslide.areastructure.Tile;

public class BoardTest {
	public static List<Tile> boardLayout = new ArrayList<Tile>();
	public static List<Tile> ominoStyle1 = new ArrayList<Tile>();
	public static List<Tile> ominoStyle2 = new ArrayList<Tile>();
	
	public Board boardUnderTest;
	public Omino omino1;
	public Omino omino2;
	public Omino omino3;
	
	@BeforeClass
	public static void setUpBeforeClass()
	{
		boardLayout.add(new Tile(0,0));
		boardLayout.add(new Tile(1,0));
		boardLayout.add(new Tile(2,0));
		boardLayout.add(new Tile(0,1));
		boardLayout.add(new Tile(1,1));
		boardLayout.add(new Tile(2,1));
		boardLayout.add(new Tile(3,1));
		ominoStyle1.add(new Tile(0,0));
		ominoStyle1.add(new Tile(1,0));
		ominoStyle2.add(new Tile(0,0));
	}

	@Before
	public void setUp() throws Exception {
		omino1 = new Omino(ominoStyle1);
		omino2 = new Omino(ominoStyle1);
		omino3 = new Omino(ominoStyle2);
		boardUnderTest = new Board(boardLayout);
	}

		@Test
	public void testPlaceOminoOmino() {
		assertTrue(boardUnderTest.placeOmino(omino1));
		assertTrue(Tile.ZERO.equals(boardUnderTest.getOminoPosition(0)));
	}

	@Test
	public void testPlaceOminoOminoTile() {
		assertTrue(boardUnderTest.placeOmino(omino1, Tile.ZERO));
		assertTrue(boardUnderTest.placeOmino(omino2, Tile.UP));
		assertFalse(boardUnderTest.placeOmino(omino3, Tile.ZERO));
		assertTrue(boardUnderTest.placeOmino(omino3, new Tile(2,1)));
	}

	@Test
	public void testGetNumPieces() {
		assertTrue(boardUnderTest.placeOmino(omino1, Tile.ZERO));
		assertTrue(boardUnderTest.placeOmino(omino2, Tile.UP));
		assertTrue(boardUnderTest.placeOmino(omino3, new Tile(2,1)));
		assertTrue(boardUnderTest.getNumPieces() == 3);
	}
	
	@Test
	public void testBoardBoard() {
		assertTrue(boardUnderTest.placeOmino(omino1, Tile.ZERO));
		assertTrue(boardUnderTest.placeOmino(omino2, Tile.UP));
		assertTrue(boardUnderTest.placeOmino(omino3, new Tile(2,1)));
		Board second = new Board(boardUnderTest);
		assertTrue(Tile.ZERO.equals(second.getOminoPosition(omino1)));
		assertTrue(new Tile(2,1).equals(second.getOminoPosition(2)));
	}

	@Test
	public void testShiftOmino() {
		assertTrue(boardUnderTest.placeOmino(omino3, Tile.ZERO));
		assertTrue(boardUnderTest.shiftOmino(0, Direction.UP));
	}
	
	@Test
	public void testGetLastPieceIndex() {
		assertTrue("Weird: last piece index is " + boardUnderTest.getLastPieceIndex()
				+ " before anything is moved!", boardUnderTest.getLastPieceIndex() < 0);
		assertTrue(boardUnderTest.placeOmino(omino3, Tile.ZERO));
		assertTrue(boardUnderTest.shiftOmino(0, Direction.UP));
		assertTrue("Error: last piece index is unexpectedly " + boardUnderTest.getLastPieceIndex(),
				boardUnderTest.getLastPieceIndex() == 0);
	}

	@Test
	public void testGetLastPieceObject() {
		assertTrue(boardUnderTest.placeOmino(omino3, Tile.ZERO));
		assertTrue(boardUnderTest.shiftOmino(0, Direction.UP));
		assertTrue("Error: last omino reference not identical!", boardUnderTest.getLastPieceObject() == omino3);
		// Specifically noting that we want to get reference identicality, not compare .equals() here!
	}

	@Test
	public void testEqualsObject() {
		assertTrue(boardUnderTest.placeOmino(omino1, Tile.ZERO));
		assertTrue(boardUnderTest.placeOmino(omino2, Tile.UP));
		assertTrue(boardUnderTest.placeOmino(omino3, new Tile(2,1)));
		Board second = new Board(boardLayout);
		assertTrue(second.placeOmino(omino1, Tile.UP));
		assertTrue(second.placeOmino(omino2, Tile.ZERO));
		assertTrue(second.placeOmino(omino3, new Tile(2,1)));
		assertTrue("Boards with identical layout should be treated as equal!", boardUnderTest.equals(second));
	}

	@Test
	public void testHashCode() {
		assertTrue(boardUnderTest.placeOmino(omino1, Tile.ZERO));
		assertTrue(boardUnderTest.placeOmino(omino2, Tile.UP));
		assertTrue(boardUnderTest.placeOmino(omino3, new Tile(2,1)));
		Board second = new Board(boardLayout);
		assertTrue(second.placeOmino(omino1, Tile.UP));
		assertTrue(second.placeOmino(omino2, Tile.ZERO));
		assertTrue(second.placeOmino(omino3, new Tile(2,1)));
		assertTrue("Boards with identical layout should have equal hash values!", boardUnderTest.hashCode() == second.hashCode());
	}
}

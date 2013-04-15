package com.github.bfallstrom.ominoslide.junit;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.bfallstrom.ominoslide.areastructure.Board;
import com.github.bfallstrom.ominoslide.areastructure.Omino;
import com.github.bfallstrom.ominoslide.areastructure.Tile;
import com.github.bfallstrom.ominoslide.solver.Moves;

public class MovesTest {
	public static List<Tile> boardLayout = new ArrayList<Tile>();
	public static List<Tile> ominoStyle1 = new ArrayList<Tile>();
	public static List<Tile> ominoStyle2 = new ArrayList<Tile>();
	public static Board masterBoard;
	public static Omino omino1;
	public static Omino omino2;
	public static Omino omino3;
	
	public Board boardUnderTest;
	public Moves movesUnderTest;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
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
		masterBoard = new Board(boardLayout);
		omino1 = new Omino(ominoStyle1);
		omino2 = new Omino(ominoStyle1);
		omino3 = new Omino(ominoStyle2);
		masterBoard.placeOmino(omino1, new Tile(1,0));
		masterBoard.placeOmino(omino2, new Tile(1,1));
		masterBoard.placeOmino(omino3, new Tile(3,1));
	}

	@Before
	public void setUp() throws Exception {
		boardUnderTest = new Board(masterBoard);
	}

	@Test
	public void testMovesBoard() {
		
		fail("Not yet implemented");
	}

	@Test
	public void testResolveMoves() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetNumberOfMoves() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetBoardFromMove() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetMove() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetStatusFromMove() {
		fail("Not yet implemented");
	}

	@Test
	public void testHasWinner() {
		fail("Not yet implemented");
	}

	@Test
	public void testHasUnblocked() {
		fail("Not yet implemented");
	}

	@Test
	public void testTrimBlocked() {
		fail("Not yet implemented");
	}

}

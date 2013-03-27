package com.github.bfallstrom.ominoslide.junit;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.github.bfallstrom.ominoslide.areastructure.Omino;
import com.github.bfallstrom.ominoslide.areastructure.Tile;

public class OminoTest {

	Omino testPiece;
	
	@Before
	public void setUp()
	{
		List<Tile> testList = new ArrayList<Tile>();
		testList.add(new Tile(0,0));
		testList.add(new Tile(0,1));
		testList.add(new Tile(1,0));
		testPiece = new Omino(testList);
	}
	
	@Test
	public void testIsInOmino() {
		assertTrue(testPiece.isInOmino(new Tile(0, 1), Tile.ZERO));
		assertTrue(testPiece.isInOmino(new Tile(1, 0), Tile.ZERO));
		assertFalse(testPiece.isInOmino(new Tile(2, 2), Tile.ZERO));
		assertTrue(testPiece.isInOmino(new Tile(2, 2), new Tile(2, 2)));
	}
	
	@Test
	public void testgetShape() {
		Set<Tile> shape = testPiece.getShape();
		assertTrue(shape.contains(Tile.UP));
		assertTrue(shape.contains(Tile.RIGHT));
		assertTrue(shape.contains(Tile.ZERO));
		assertFalse(shape.contains(Tile.LEFT));
	}
	
	@Test
	public void testBorders() {
		Set<Tile> border = testPiece.getBorderUp();
		assertTrue(border.contains(new Tile(0,2)));
		border = testPiece.getBorderDown();
		assertTrue(border.contains(new Tile(1,-1)));
		border = testPiece.getBorderLeft();
		assertTrue(border.contains(new Tile(-1,0)));
		border = testPiece.getBorderRight();
		assertTrue(border.contains(new Tile(1,1)));
	}
}
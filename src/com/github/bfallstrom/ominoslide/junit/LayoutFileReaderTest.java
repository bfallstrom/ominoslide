package com.github.bfallstrom.ominoslide.junit;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.github.bfallstrom.ominoslide.areastructure.Board;
import com.github.bfallstrom.ominoslide.areastructure.WinningPosition;
import com.github.bfallstrom.ominoslide.solver.Boards;
import com.github.bfallstrom.ominoslide.solver.Move;
import com.github.bfallstrom.ominoslide.tilereader.LayoutFileReader;
import com.github.bfallstrom.ominoslide.view.BoardViewPlaintext;

public class LayoutFileReaderTest {
	private static final String filePath1 = "media/simpleLayoutReaderTest.txt";
	private static final String filePath2 = "media/negativeLayoutTest1_layoutMismatch.txt";
	private static final String filePath3 = "media/negativeLayoutTest2_ominoMismatch.txt";
	
	private LayoutFileReader testIn;

	@Test
	public void testLayoutFileReader() {
		generate(filePath1);
		
		try {
			testIn.readAndParse();
		} catch (IllegalArgumentException i) {
			System.err.println(i.getMessage());
			fail(i.getMessage());
		} catch (IOException io) {
			System.err.println(io.getMessage());
			fail(io.getMessage());
		} catch (RuntimeException r) {
			System.err.println(r.getMessage());
			fail(r.getMessage());
		}
		Board b = testIn.getStartingBoard();
		WinningPosition w = testIn.getWinningBoard();
		assertNotNull(b);
		assertNotNull(w);
		Boards solver = new Boards(b,w);
		try {
			while(!solver.iterate());
			List<Move> solution = solver.getSolution();
			System.out.println(solution);
			System.out.println("Solution found in " + (solution.get(0).getDepth()+1) + " moves!");
		} catch(RuntimeException e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testNegativeResult1() {
		generate(filePath2);
		try {
			testIn.readAndParse();
		} catch (IllegalArgumentException i) {
			System.err.println(i.getMessage());
			assertTrue(i.getMessage() != null && i.getMessage().startsWith("Parse error! Board layouts"));
		} catch (IOException io) {
			System.err.println(io.getMessage());
			fail(io.getMessage());
		} catch (RuntimeException r) {
			System.err.println(r.getMessage());
			fail(r.getMessage());
		}
	}

	@Test
	public void testNegativeResult2() {
		generate(filePath3);
		try {
			testIn.readAndParse();
		} catch (IllegalArgumentException i) {
			System.err.println(i.getMessage());
			assertTrue(i.getMessage() != null && i.getMessage().startsWith("Parse error! Omino "));
		} catch (IOException io) {
			System.err.println(io.getMessage());
			fail(io.getMessage());
		} catch (RuntimeException r) {
			System.err.println(r.getMessage());
			fail(r.getMessage());
		}
	}

	
	private void generate(String path)
	{
		try {
			testIn = new LayoutFileReader(new File(path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail("File not found!");
		}
	}
	
	@Test
	public void testLayton() {
		generate("media/layton_5_90.txt");
		try {
			testIn.readAndParse();
			System.out.println(new BoardViewPlaintext(testIn.getStartingBoard()));
		} catch (IllegalArgumentException i) {
			System.err.println(i.getMessage());
			fail(i.getMessage());
		} catch (IOException io) {
			System.err.println(io.getMessage());
			fail(io.getMessage());
		} catch (RuntimeException r) {
			System.err.println(r.getMessage());
			fail(r.getMessage());
		}
		Board b = testIn.getStartingBoard();
		WinningPosition w = testIn.getWinningBoard();
		assertNotNull(b);
		assertNotNull(w);
		Boards solver = new Boards(b,w);
		try {
			int j = 0;
			while(!solver.iterate()) System.out.println(j++);
			List<Move> solution = solver.getSolution();
			//System.out.println(solution);
			System.out.println("Solution found in " + (solution.get(0).getDepth()+1) + " moves!");
			System.out.println(new BoardViewPlaintext(solution.get(0).getStartingBoard()));
			for(int i = 0; i < solution.size(); i++)
			{
				System.out.println();
				System.out.println(new BoardViewPlaintext(solution.get(i).getNextBoard()));
			}
		} catch(IllegalArgumentException e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}

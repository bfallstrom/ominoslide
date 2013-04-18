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
import com.github.bfallstrom.ominoslide.areastructure.WinningPosition;
import com.github.bfallstrom.ominoslide.solver.Boards;
import com.github.bfallstrom.ominoslide.solver.Move;
import com.github.bfallstrom.ominoslide.view.BoardViewPlaintext;

public class BoardsTest {
	public static List<Tile> boardLayout = new ArrayList<Tile>();
	public static List<Tile> ominoStyle1 = new ArrayList<Tile>();
	public static List<Tile> ominoStyle2 = new ArrayList<Tile>();
	public static Board masterBoard;
	public static Omino omino1;
	public static Omino omino2;
	public static Omino omino3;
	public static WinningPosition solved;
	
	public Boards solver;

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
		assertTrue("ERROR: Master board initialization failed!!", masterBoard.placeOmino(omino1, new Tile(1,0)));
		assertTrue("ERROR: Master board initialization failed!!", masterBoard.placeOmino(omino2, new Tile(1,1)));
		assertTrue("ERROR: Master board initialization failed!!", masterBoard.placeOmino(omino3, new Tile(3,1)));
		solved = new WinningPosition(boardLayout);
		assertTrue("ERROR: Solved position initialization failed!!", solved.placeOmino(omino3, new Tile(2,0)));
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testIterate() {
		solver = new Boards(masterBoard, solved);
		int i = 0;
		try 
		{
			while(!solver.iterate())
			{
				System.out.println("Iteration #: " + ++i);
			}
			System.out.println("Solution found at iteration " + i + "!");
			List<Move> solution = solver.getSolution();
			System.out.println(solution);
			System.out.println(solution.get(0).getDepth());
			
		} catch(RuntimeException e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testOptimize() {
		List<Tile> layout2 = new ArrayList<Tile>();
		layout2.add(new Tile(0,0));
		layout2.add(new Tile(1,0));
		layout2.add(new Tile(2,0));
		layout2.add(new Tile(0,1));
		layout2.add(new Tile(1,1));
		layout2.add(new Tile(2,1));
		layout2.add(new Tile(0,2));
		layout2.add(new Tile(1,2));
		layout2.add(new Tile(2,2));
		layout2.add(new Tile(3,2));
		Omino omino4 = new Omino(ominoStyle1);
		Board board2 = new Board(layout2);
		assertTrue("Initialization error!", board2.placeOmino(omino1, new Tile(1,0)));
		assertTrue("Initialization error!", board2.placeOmino(omino2, new Tile(1,1)));
		assertTrue("Initialization error!", board2.placeOmino(omino3, new Tile(3,2)));
		assertTrue("Initialization error!", board2.placeOmino(omino4, new Tile(1,2)));
		WinningPosition win2 = new WinningPosition(layout2);
		assertTrue("Initialization error!", win2.placeOmino(omino3, new Tile(2,0)));
		solver = new Boards(board2, win2);
		int i = 0;
		try 
		{
			while(!solver.iterate())
			{
				System.out.println("Iteration #: " + ++i);
			}
			System.out.println("Solution found at iteration " + i + "!");
			List<Move> solution = solver.getSolution();
			System.out.println(solution);
			System.out.println(solution.get(0).getDepth());
			
		} catch(RuntimeException e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	// This should be replaced when a suitable solution viewer similar to this is created.
	@Test
	public void testBoardsSolverAndView() {
		solver = new Boards(masterBoard, solved);
		//int i = 0;
		try 
		{
			while(!solver.iterate())
			{
				//System.out.println("Iteration #: " + ++i);
			}
			//System.out.println("Solution found at iteration " + i + "!");
			List<Move> solution = solver.getSolution();
			//System.out.println(solution);
			//System.out.println(solution.get(0).getDepth());
			System.out.println(new BoardViewPlaintext(solution.get(0).getStartingBoard()));
			for(int i = 0; i < solution.size(); i++)
			{
				System.out.println();
				System.out.println(new BoardViewPlaintext(solution.get(i).getNextBoard()));
			}
			
		} catch(RuntimeException e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}

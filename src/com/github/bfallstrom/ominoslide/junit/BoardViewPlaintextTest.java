package com.github.bfallstrom.ominoslide.junit;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.bfallstrom.ominoslide.areastructure.Board;
import com.github.bfallstrom.ominoslide.tilereader.LayoutFileReader;
import com.github.bfallstrom.ominoslide.view.BoardViewPlaintext;

public class BoardViewPlaintextTest {
	private static final String filePath1 = "media/simpleLayoutReaderTest.txt";
	
	private static Board board;
	
	private BoardViewPlaintext view;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		LayoutFileReader testIn;
		board = new Board();
		try {
			testIn = new LayoutFileReader(new File(filePath1));
			testIn.readAndParse();
			board = testIn.getStartingBoard();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail("File not found!");
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
	}

	@Before
	public void setUp() throws Exception {
		view = new BoardViewPlaintext(board);
	}

	@Test
	public void testToString() {
		System.out.println(view.toString());
	}

}

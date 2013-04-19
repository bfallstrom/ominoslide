package com.github.bfallstrom.ominoslide.tilereader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.github.bfallstrom.ominoslide.areastructure.Board;
import com.github.bfallstrom.ominoslide.areastructure.Omino;
import com.github.bfallstrom.ominoslide.areastructure.Tile;
import com.github.bfallstrom.ominoslide.areastructure.WinningPosition;

public class LayoutFileReader {
	/**
	 * Files should be in the following format:
	 * "//" indicates that the remainder of the line should be ignored. At the beginning of a line,
	 *  it indicates that the entire line should be dropped.
	 * "#" denotes a blocked tile, a point not to be included on the Layout.
	 * "." denotes a free tile, a point to be included in the layout but which no Omino covers.
	 * "A" through "Z", "a" through "z", and "0" through "9" denote tiles of an Omino. Ominos
	 *  are not strictly required to be contiguous, but if you make discontiguous ominos, results
	 *  may vary greatly from expectations.
	 * " " space characters will be ignored, but should not be used in the middle of the layout as it
	 *  may get confusing.
	 *  
	 * A solid row of "***" characters should be used to separate the starting layout from the win
	 *  condition. Only one is actually needed; a row containing any "*" characters outside of comments
	 *  is assumed to mean this separation. "*" characters in the middle of any other row will cause
	 *  an exception.
	 * 
	 * The second layout in the file is assumed to be the win condition, being the position of all of
	 *  the ominos needed for the win condition, and all immaterial ominos being replaced by blank
	 *  space. An exception will be thrown if the second layout has any tiles that differ from the
	 *  first, or if any of the labeled ominos have a different shape.
	 */
	
	/**
	 * NOTES: Any ominos parsed as part of the win condition should have a unique ID applied to them.
	 * 
	 * read one line from the file at a time. Initially send lines to clean, then add information to
	 *  wholeLayout1. When a blank line is encountered (i.e. all comment), discard and move on.
	 *  When a "*" line is encountered, flip to adding information to wholeLayout2.
	 * Only afterward parse wholeLayout1 and 2 into the Board and WinningPosition.
	 */
	private static final String			charactersToAccept = ".#A-Za-z0-9";
	
	private Map<Character,Set<Tile>> wholeLayout1 = new HashMap<Character,Set<Tile>>();	// initial layout
	private Map<Character,Set<Tile>> wholeLayout2 = new HashMap<Character,Set<Tile>>();	// winning layout
	private Map<Character,Set<Tile>> startingOminos = new HashMap<Character,Set<Tile>>();
	private Map<Character,Set<Tile>> winningOminos = new HashMap<Character,Set<Tile>>();
	private Map<Character,Omino> uniqueOminos = new HashMap<Character,Omino>();
	private Set<Tile> boardLayout = new HashSet<Tile>();
	private Board startingBoard = null;
	private WinningPosition winningBoard = null;
	private int ominoUniqueId = 0;
	
	private BufferedReader lineReader;
	
	/**
	 * Constructs a new LayoutFileReader to read the given File.
	 * @param inputFile The file to read from.
	 * @throws FileNotFoundException if the file cannot be found.
	 */
	public LayoutFileReader(File inputFile) throws FileNotFoundException
	{
		lineReader = new BufferedReader(new FileReader(inputFile));
	}
	
	/**
	 * Constructs a new LayoutFileReader on the given input stream.
	 * @param in The InputStream to read from.
	 */
	public LayoutFileReader(InputStream in)
	{
		lineReader = new BufferedReader(new InputStreamReader(in));
	}
	
	/**
	 * Constructs a new LayoutFileReader on standard in.
	 */
	public LayoutFileReader()
	{
		this(System.in);
	}
	
	/**
	 * Reads the source given at constructor time and parses it into an initial Board and a WinningPosition.
	 * @throws IOException if the file read throws an IOException
	 */
	public void readAndParse() throws IOException
	{
		String currentLine;
		int i = 0;
		Map<Character,Set<Tile>> layoutToUse = wholeLayout1;
		try{
			while((currentLine = cleanLine(lineReader.readLine())) != null)
			{
				if(currentLine.length() > 0)
				{
					if(currentLine.charAt(0) == '*' && layoutToUse == wholeLayout1)
						layoutToUse = wholeLayout2;
					else if(currentLine.charAt(0) == '*')
						throw new IllegalArgumentException("Parse error: More than one * line!");
					else if(pushLineToSet(currentLine, layoutToUse, i))
						i--;	// Y coordinates must be descending as the file is read top to bottom
				}
			}
		} catch (IllegalArgumentException e)
		{
			throw new IllegalArgumentException(e.getMessage() + " at line #" + (-i));
		}
		// Now parse the wholeLayouts to the Board and WinningPosition.
		parseLayouts();
	}
	
	/**
	 * Gets the initial Board read from the file.
	 * @return The Board read from the file.
	 */
	public Board getStartingBoard()
	{
		if(startingBoard == null)
			throw new RuntimeException("Error: Board not initialized!");
		return startingBoard;
	}
	
	/**
	 * Gets the WinningPosition read from the file.
	 * @return The WinningPosition read from the file.
	 */
	public WinningPosition getWinningBoard()
	{
		if(winningBoard == null)
			throw new RuntimeException("Error: Board not initialized!");
		return winningBoard;
	}
	
	/**
	 * Parses wholeLayout1 into the initial layout, and wholeLayout2 into the winning position.
	 * Throws exceptions if there are errors in the input.
	 */
	private void parseLayouts()
	{
		Set<Character> letters = wholeLayout1.keySet();
		for(Character eachLetter : letters)
		{
			if(eachLetter.charValue() != '#')
			{
				boardLayout.addAll(wholeLayout1.get(eachLetter));
				if(eachLetter.charValue() != '.')
					startingOminos.put(eachLetter, wholeLayout1.get(eachLetter));
			}
		}
		HashSet<Tile> winLayout = new HashSet<Tile>();
		letters = wholeLayout2.keySet();
		for(Character eachLetter : letters)
		{
			if(eachLetter.charValue() != '#')
			{
				winLayout.addAll(wholeLayout2.get(eachLetter));
				if(eachLetter.charValue() != '.')
					winningOminos.put(eachLetter, wholeLayout2.get(eachLetter));
			}
		}
		// Next compare the layouts to each other
		Set<Tile> normalizedLayout = normalizeLayout(boardLayout);
		if(!sameShapes(normalizedLayout, normalizeLayout(winLayout)))
			throw new IllegalArgumentException("Parse error! Board layouts must be the same between starting and winning positions!");
		startingBoard = new Board(normalizedLayout);
		winningBoard = new WinningPosition(normalizedLayout);
		Tile layoutZero = Collections.min(winLayout);
		letters = winningOminos.keySet();
		// Initialize the winning position first, that way the unique ominos are there already
		for(Character eachLetter : letters)
		{
			Set<Tile> ominoShape = winningOminos.get(eachLetter);
			Tile ominoZero = Collections.min(ominoShape);
			if(!startingOminos.containsKey(eachLetter))
				throw new IllegalArgumentException("Parse error! Omino " + eachLetter + " in winning layout is not seen in initial layout!");
			ominoShape = normalizeLayout(ominoShape);
			if(!sameShapes(ominoShape, normalizeLayout(startingOminos.get(eachLetter))))
				throw new IllegalArgumentException("Parse error! Omino " + eachLetter + " in winning layout has different shape from its appearance in initial layout!");
			Omino solutionOmino = new Omino(ominoShape);
			solutionOmino.setUniqueId(ominoUniqueId++);
			if(!winningBoard.placeOmino(solutionOmino, ominoZero.minus(layoutZero)))
				throw new RuntimeException("Placement of omino " + eachLetter + " failed--parse algorithm must need fixing!");
			uniqueOminos.put(eachLetter, solutionOmino);
		}
		layoutZero = Collections.min(boardLayout);
		letters = startingOminos.keySet();
		// Now initialize the starting position
		for(Character eachLetter : letters)
		{
			Set<Tile> ominoShape = startingOminos.get(eachLetter);
			Tile ominoZero = Collections.min(ominoShape);
			Omino initOmino;
			if(uniqueOminos.containsKey(eachLetter))
				initOmino = uniqueOminos.get(eachLetter);	// Use the existing omino if it exists
			else initOmino = new Omino(normalizeLayout(ominoShape));
			if(!startingBoard.placeOmino(initOmino, ominoZero.minus(layoutZero)))
				throw new RuntimeException("Placement of omino " + eachLetter + " failed--parse algorithm must need fixing!");
		}
	}
	
	/**
	 * Parses an input line and pushes the tile data to the indicated map. Lines must be cleaned!
	 * @param inputLine The line to parse.
	 * @param outputMap The map to add data to.
	 * @param yCoord The Y coordinate to use. When reading a file from top to bottom, Y coordinates
	 *  should be strictly descending--recommend stating at zero and proceeding negative. Layouts
	 *  will be normalized before inserting as a Board or Omino, anyway.
	 * @return whether the yCoord should decrement
	 */
	private static boolean pushLineToSet(String inputLine, Map<Character,Set<Tile>> outputMap, int yCoord)
	{
		if(inputLine.length() == 0)
			return false;
		if(!inputLine.matches("[" + charactersToAccept + "]*"))
			throw new IllegalArgumentException("Parse error! Unauthorized characters in line!");
		for(int i = 0; i < inputLine.length(); i++)
		{
			Character snip = new Character(inputLine.charAt(i));
			if(outputMap.containsKey(snip))
				outputMap.get(snip).add(new Tile(i,yCoord));
			else
			{
				Set<Tile> location = new HashSet<Tile>();
				location.add(new Tile(i,yCoord));
				outputMap.put(snip, location);
			}
		}
		return true;
	}
	
	private static String cleanLine(String lineToParse)
	{
		if(lineToParse == null) return null;
		lineToParse = lineToParse.replaceAll("//.*$|\\s", "");
		if(lineToParse.matches(".*\\*.*") && lineToParse.matches(".*[^*].*"))
			throw new IllegalArgumentException("Parse error! '*' found outside of comment or dividing line!");
		lineToParse = lineToParse.replaceFirst("\\*+", "*");
		return lineToParse;
	}
	
	/**
	 * must be normalized first!
	 * @return true iff these are the same shape
	 */
	private static boolean sameShapes(Set<Tile> shape1, Set<Tile> shape2)
	{
		if(shape1.size() != shape2.size())
			return false;
		for(Tile tile : shape1)
		{
			if(!shape2.contains(tile))
				return false;
		}
		return true;
	}
	
	/**
	 * This method "normalizes" a given collection of Tiles such that it is guaranteed to include
	 *  a tile at (0,0). Based on the implementation of
	 *  com.github.bfallstrom.ominoslide.areastructure.Tile's comparator, this places the zero tile
	 *  consistently at the bottommost element in the leftmost column.
	 * To just find the zero tile of a non-normalized collection, use Collections.min(inputLayout).
	 * @param inputLayout The collection of tiles to normalize.
	 * @return A collection with the exact same shape, but guaranteed to contain Tile.ZERO.
	 */
	private static Set<Tile> normalizeLayout(Collection<Tile> inputLayout)
	{
		Tile zeroTile = Collections.min(inputLayout);
		Set<Tile> normalizedLayout = new HashSet<Tile>(inputLayout.size());
		for(Tile inputTile : inputLayout)
		{
			normalizedLayout.add(inputTile.minus(zeroTile));
		}
		return normalizedLayout;
	}
}
package com.github.bfallstrom.ominoslide.tilereader;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import com.github.bfallstrom.ominoslide.areastructure.Tile;

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
	 */
	
	public LayoutFileReader(File inputFile)
	{
		// TODO: NYI
		throw new RuntimeException("NYI!");
	}
	
	private static String cleanLine(String lineToParse)
	{
		// TODO: NYI, strip out comments, reduce lines of * characters to a single *
		throw new RuntimeException("NYI!");
		//return lineToParse;
	}
	
	private static Collection<Tile> normalizeLayout(Collection<Tile> inputLayout)
	{
		Collection<Tile> normalizedLayout = new ArrayList<Tile>();
		// TODO: Ensure that the bottommost element of the leftmost column is Tile.ZERO
		throw new RuntimeException("NYI!");
		//return normalizedLayout;
	}
}

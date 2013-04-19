package com.github.bfallstrom.ominoslide.view.cli;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.github.bfallstrom.ominoslide.areastructure.Board;
import com.github.bfallstrom.ominoslide.areastructure.WinningPosition;
import com.github.bfallstrom.ominoslide.solver.Boards;
import com.github.bfallstrom.ominoslide.solver.Move;
import com.github.bfallstrom.ominoslide.tilereader.LayoutFileReader;
import com.github.bfallstrom.ominoslide.view.BoardViewPlaintext;

public class CliSolverMain {
	private static final String[]		HELP_OPTIONS = {"-h","--help","-?"};
	private static final String[]		VERBOSE_OPTIONS = {"-v","--verbose"};
	private static final String[]		SILENT_OPTIONS = {"-s","--silent"};
	
	private static final String			LF = System.getProperty( "line.separator" );
	private static final String			HELP_MESSAGE =
			  "Tile sliding solver. See example layouts for proper format." + LF
			+ "[OPTIONS] [FILE]" + LF
			+ "-h, -?, or --help displays this help message." + LF
			+ "-v or --verbose gives additional information during a solve." + LF
			+ "-s or --silent suppresses error messages." + LF
			+ "If there is no file, the layout is read from standard input rather than a file." + LF + LF
			+ "Layout file must have two identically shaped boards. The first board represents" + LF
			+ "the initial condition of the board, with all pieces that are ever to be moved." + LF
			+ "The second board represents the win condition--each piece that must be in a" + LF
			+ "specific position, arranged in that position. Only those pieces that are part" + LF
			+ "of the solution condition should be in the second layout. Pieces must maintain" + LF
			+ "the same shape between the two layouts, as must the layout itself." + LF + LF
			+ "Layout should use '#' for unusable tiles, '.' for usable tiles, and unique" + LF
			+ "alphanumeric characters (case sensitive) to mark the positions of pieces." + LF
			+ "" + LF
			+ "" + LF
			+ "" + LF
			+ "" + LF
			;
	private static final String			ERROR_MESSAGE_FILE_NOT_FOUND = "Error; file \"%s\" was not found.";
	private static final String			ERROR_MESSAGE_FILE_ARGS = "Error; more than one file path was input.";
	private static final String			ERROR_MESSAGE_INVALID_OPTION = "Error; invalid option \"%s\".";
	
	private static final String			ITERATION_STRING = "Iteration #%d complete.";
	
	private static boolean				silent = false;
	private static boolean				displayHelp = false;
	private static boolean				verbose = false;
	private static boolean				abort = false;
	private static File					inputFile = null;
	
	private static LayoutFileReader		input;
	
	private enum ArgValue {
		HELP,
		VERBOSE,
		SILENT,
		FILEPATH,
		INVALID
	}
	
	/**
	 * If any argument is "--help", "-?", or "-h" the rest of the arguments will be ignored and the
	 *  help notice will be printed to standard out.
	 *  
	 * Otherwise, arguments starting with dash characters will be parsed first. "-" alone takes an
	 *  input file directly from standard in; any file path will be ignored in that case. "-v" or
	 *  "--verbose" gives additional information to standard out. "-s" or "--silent" suppresses
	 *  error messages.
	 *  
	 * Afterwards, if there is one argument, it will be parsed as a file path to read. Failure to
	 *  read that file will result in an error message to standard error.
	 * 
	 * If there are any invalid arguments, the help notice will be printed to standard out, and such
	 *  invalid arguments will be printed to standard error.
	 * 
	 * @param args command line arguments
	 */
	public static void main(String[] args) {

		for(int i = 0; i < args.length; i++)
		{
			switch (parseArgument(args[i])) {
			case HELP:
				displayHelp = true;
				abort = true;
				break;
			case VERBOSE:
				verbose = true;
				break;
			case SILENT:
				silent = true;
				break;
			case FILEPATH:
				if(inputFile == null)
					inputFile = new File(args[i]);
				else
					outputInvalidArgument(ERROR_MESSAGE_FILE_ARGS);
				break;
			case INVALID:
			default:
				outputInvalidArgument(String.format(ERROR_MESSAGE_INVALID_OPTION, args[i]));
				break;
			}
		}
		
		if(displayHelp)
			displayHelp();
		
		if(inputFile != null && !abort)
		{
			try {
				input = new LayoutFileReader(inputFile);
			} catch (FileNotFoundException e)
			{
				outputInvalidArgument(String.format(ERROR_MESSAGE_FILE_NOT_FOUND, inputFile.getPath()));
			}
		} else input = new LayoutFileReader();
		
		if(!abort)
		{
			try {
				input.readAndParse();
			} catch (IllegalArgumentException i) {
				outputInvalidArgument(i.getMessage());
			} catch (IOException io) {
				outputInvalidArgument(io.getMessage());
			} catch (RuntimeException r) {
				outputInvalidArgument(r.getMessage());
			}
		}
		
		if(!abort)
		{
			try {
				Board initialBoard = input.getStartingBoard();
				WinningPosition finalBoard = input.getWinningBoard();
				Boards solver = new Boards(initialBoard, finalBoard);
				int j = 0;
				while(!solver.iterate())
					if(verbose)
						System.out.println(String.format(ITERATION_STRING, j));
				List<Move> solution = solver.getSolution();
				if(verbose)
					System.out.println("Solution found in " + (solution.get(0).getDepth()+1) + " moves!");
				System.out.println(new BoardViewPlaintext(solution.get(0).getStartingBoard()));
				for(int i = 0; i < solution.size(); i++)
				{
					if(verbose)
						System.out.print(Integer.toString(i));
					System.out.println();
					System.out.println(new BoardViewPlaintext(solution.get(i).getNextBoard()));
				}
			} catch(IllegalArgumentException e)
			{
				outputInvalidArgument(e.getMessage());
			}
		}
	}
	
	private static void displayHelp()
	{
		System.out.println(HELP_MESSAGE);
	}
	
	
	
	private static void outputInvalidArgument(String errorMessage)
	{
		abort = true;
		if(!silent)
			System.err.println(errorMessage);
	}
	
	private static ArgValue parseArgument(String arg)
	{
		for(String opt : HELP_OPTIONS)
		{
			if(opt.equals(arg))
				return ArgValue.HELP;
		}
		for(String opt : VERBOSE_OPTIONS)
		{
			if(opt.equals(arg))
				return ArgValue.VERBOSE;
		}
		for(String opt : SILENT_OPTIONS)
		{
			if(opt.equals(arg))
				return ArgValue.SILENT;
		}
		if(arg == null || arg.length() == 0 || arg.charAt(0) == '-')
			return ArgValue.INVALID;
		return ArgValue.FILEPATH;
	}
}
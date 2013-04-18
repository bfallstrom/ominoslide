package com.github.bfallstrom.ominoslide.view;

import java.util.Set;

import com.github.bfallstrom.ominoslide.areastructure.Board;
import com.github.bfallstrom.ominoslide.areastructure.Omino;
import com.github.bfallstrom.ominoslide.areastructure.Tile;

public class BoardViewPlaintext {
	private static final char WALL = '#';
	private static final char OPEN = '.';
	private static final String PIECES = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	// To allow display of puzzles requiring more than 62 pieces, add additional unique characters to PIECES.
	// This will also require updating LayoutFileReader.charactersToAccept to allow these characters as input!
	
	private Tile lowerLeft;
	private Tile boardSize;
	private String[] boardVisual;
	// initialize to wall, then parse layout, then parse ominos
	
	public BoardViewPlaintext(Board board)
	{
		if(board == null)
			throw new IllegalArgumentException("Error: null board cannot be viewed.");
		lowerLeft = board.getLowerLeftBound();
		boardSize = board.getUpperRightBound().minus(lowerLeft);	// actually size -1 in both directions
		boardVisual = new String[boardSize.getY()+1];
		for(int i = 0; i < boardVisual.length; i++)
		{
			boardVisual[i] = new String(new char[boardSize.getX()+1]).replace('\0', WALL);
		}
		Set<Tile> boardLayout = board.getLayout();
		for(Tile tile : boardLayout)
		{
			Tile tmp = tile.minus(lowerLeft);
			int x = tmp.getX();
			int y = tmp.getY();
			StringBuilder b = new StringBuilder(boardVisual[boardVisual.length-y-1]);
			b.setCharAt(x, OPEN);
			boardVisual[boardVisual.length-y-1] = b.toString(); 
		}
		int numberOfOminos = board.getNumPieces();
		for(int i = 0; i < numberOfOminos; i++)
		{
			Omino omino = board.getOmino(i);
			Tile ominoZero = board.getOminoPosition(i).minus(lowerLeft);
			Set<Tile> ominoLayout = omino.getShape();
			for(Tile tile : ominoLayout)
			{
				Tile tmp = new Tile(tile, ominoZero);
				int x = tmp.getX();
				int y = tmp.getY();
				StringBuilder b = new StringBuilder(boardVisual[boardVisual.length-y-1]);
				b.setCharAt(x, PIECES.charAt(i));	// WARNING: Unchecked StringIndexOutOfBoundsException here if there are more than 62 pieces!
				boardVisual[boardVisual.length-y-1] = b.toString(); 
			}
		}
	}
	
	public String toString()
	{
		StringBuilder fullBoard = new StringBuilder();
		fullBoard.ensureCapacity((boardVisual.length)*(boardVisual[0].length()+1)+1);
		for(int i = 0; i < boardVisual.length; i++)
		{
			fullBoard.append(boardVisual[i]);
			fullBoard.append('\n');
		}
		return fullBoard.toString();
	}
}

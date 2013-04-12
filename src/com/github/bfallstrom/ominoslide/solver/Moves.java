package com.github.bfallstrom.ominoslide.solver;

import java.util.ArrayList;
import java.util.List;
import com.github.bfallstrom.ominoslide.areastructure.Board;
import com.github.bfallstrom.ominoslide.areastructure.Direction;
import com.github.bfallstrom.ominoslide.areastructure.WinningPosition;

public class Moves {
	private List<Move> possibleMoves = new ArrayList<Move>(); // the possible moves from the associated layout, generated at construction time
	private int numberOfMovesIn = 0;	// how many moves have been made since the initial position.
	
	public Moves(Board board, int numberOfMoves)
	{
		this.numberOfMovesIn = numberOfMoves;
		int maxIndex = board.getNumPieces();
		for(int i = 0;i < maxIndex;i++)
		{
			possibleMoves.add(new Move(board, Direction.UP, i));
			possibleMoves.add(new Move(board, Direction.DOWN, i));
			possibleMoves.add(new Move(board, Direction.LEFT, i));
			possibleMoves.add(new Move(board, Direction.RIGHT, i));
		}
	}
	
	public Moves(Board board)
	{
		this(board, 0);
	}
	
	public void resolveMoves(WinningPosition winner)
	{
		for(Move move : possibleMoves)
		{
			move.resolveMove(winner);
		}
	}
	
	public int getNumberOfMoves()
	{
		return this.numberOfMovesIn;
	}
	
	public Board getBoardFromMove(int moveIndex)
	{
		return this.possibleMoves.get(moveIndex).getNextBoard();
	}
	
	public boolean hasWinner()
	{
		for(Move move: possibleMoves)
		{
			if(move.getStatus() == MoveStatus.WINNING)
				return true;
		}
		return false;
	}
	
	public boolean hasUnblocked()
	{
		for(Move move: possibleMoves)
		{
			if(move.getStatus() != MoveStatus.BLOCKED)
				return true;
		}
		return false;
	}
}
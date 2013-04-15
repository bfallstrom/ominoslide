package com.github.bfallstrom.ominoslide.solver;

import java.util.ArrayList;
import java.util.List;
import com.github.bfallstrom.ominoslide.areastructure.Board;
import com.github.bfallstrom.ominoslide.areastructure.Direction;
import com.github.bfallstrom.ominoslide.areastructure.WinningPosition;

public class Moves {
	private List<Move> possibleMoves = new ArrayList<Move>(); // the possible moves from the associated layout, generated at construction time
	private final int numberOfMovesIn;	// how many moves have been made since the initial position.
	
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
	
	/**
	 * Gets the number of moves in this list. This cannot be assumed to be constant, as moves may be
	 *  deleted from the list with the trimBlocked() method! When first initialized, this number should
	 *  be equal to 4 times the number of ominos on the board, assuming four directions.
	 * (For possible future branches dealing with triangular or hexagonal tesselations for boards, the
	 *  multiplier should be assumed to be the number of possible directions it is possible to slide a
	 *  piece.)
	 * @return The number of moves currently in this moves list.
	 */
	public int getNumberOfMoves()
	{
		return this.possibleMoves.size();
	}
	
	/**
	 * Gets the "depth" of this moveset in the solution tree; this value is set when the moveset is first
	 *  generated and should never be changed.
	 * @return
	 */
	public int getMoveDepth()
	{
		return this.numberOfMovesIn;
	}
	
	public Board getBoardFromMove(int moveIndex)
	{
		return this.possibleMoves.get(moveIndex).getNextBoard();
	}
	
	public Move getMove(int moveIndex)
	{
		return this.possibleMoves.get(moveIndex);
	}
	
	public MoveStatus getStatusFromMove(int moveIndex)
	{
		return this.possibleMoves.get(moveIndex).getStatus();
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
	
	/**
	 * Removes all moves from the list that are marked as BLOCKED. Returns true if any were removed.
	 * @return True iff there were any BLOCKED moves removed from the list.
	 */
	public boolean trimBlocked()
	{
		boolean anyRemoved = false;
		for(int i = 0; i < possibleMoves.size(); i++)
		{
			Move move = possibleMoves.get(i);
			if(move.getStatus() == MoveStatus.BLOCKED)
			{
				possibleMoves.remove(i--);	// It's a little more coding to do it with indices than reference
				anyRemoved = true;			// iterators, but we don't have to do a linear search each time!!
			}
		}
		return anyRemoved;
	}
	
	/**
	 * Removes all non-winning moves from the list. If there was not a winner or there were no non-
	 *  winners, returns false; otherwise removes all but the lowest-indexed WINNING move and returns
	 *  true.
	 * @return true iff there is now exactly one Move that is a WINNING move in this Moves object.
	 */
	public boolean trimToWinner()
	{
		if(this.hasWinner() && this.possibleMoves.size() > 1)
		{
			for(int i = 0; i < possibleMoves.size(); i++)
			{
				Move move = possibleMoves.get(i);
				if(move.getStatus() != MoveStatus.WINNING)
					possibleMoves.remove(i--);
			}
			return true;
		}
		return false;
	}
}
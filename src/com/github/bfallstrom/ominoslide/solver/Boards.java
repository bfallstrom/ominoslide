package com.github.bfallstrom.ominoslide.solver;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.github.bfallstrom.ominoslide.areastructure.Board;
import com.github.bfallstrom.ominoslide.areastructure.WinningPosition;

public class Boards {
	private Map<Board,Moves>	states = new HashMap<Board,Moves>(); // maps each known board state to the possible moves that can be made from it
	private WinningPosition		solved = null;	// The position to seek.
	private int					winFoundAt = Integer.MAX_VALUE;
	private Board				rootPosition;
	
	public Boards(Board startingPosition, WinningPosition winningPosition)
	{
		solved = winningPosition;
		Moves moves = new Moves(startingPosition);
		moves.resolveMoves(solved);
		moves.trimBlocked();
		states.put(startingPosition, moves);
		rootPosition = startingPosition;
	}
	
	/**
	 * Makes a single pass through the map and performs all the trimmings and checks.
	 * @return true if a complete solution was found
	 */
	public boolean iterate()
	{
		Set<Board> allBoards = states.keySet();
		for(Board board : allBoards)
		{
			Moves moves = states.get(board);
			if(moves.hasUnblocked())
			{
				moves.trimBlocked();
				for(int i = 0; i < moves.getNumberOfMoves(); i++)
				{
					Move move = moves.getMove(i);
					checkMove(move, moves.getMoveDepth(), solved);
				}
			} else allBoards.remove(board);
		}
		if(states.get(rootPosition).hasWinner())
			states.get(rootPosition).trimToWinner();
		if(!states.get(rootPosition).hasUnblocked())
			throw new RuntimeException("WARNING! This puzzle appears to be unsolvable!");
		return states.get(rootPosition).hasWinner();
	}
	
	
	/**
	 * Checks a resolved move against the mapping. Move MUST already be resolved!
	 * @param move the move to check
	 * @param numOfMovesOut How many moves out this move is said to be.
	 * @param solution the WinningPosition to test against.
	 */
	private void checkMove(Move move, int numOfMovesOut, WinningPosition solution)
	{
		if(numOfMovesOut > winFoundAt)	// If a win was already found with fewer moves, cut this out.
			move.setStatus(MoveStatus.BLOCKED);
		if(move.getStatus() == MoveStatus.UNKNOWN)	// If it's not UNKNOWN, we don't need to check anything!
		{
			Board board = move.getNextBoard();
			if(!states.containsKey(board) || states.get(board).getMoveDepth() > numOfMovesOut)
			{	// Only insert if an equivalent board is not already inserted, or if the equivalent board takes
				Moves theseMoves;	// more moves to reach.
				if(move.getOminoIndex() == board.getLastPieceIndex())
					theseMoves = new Moves(board, numOfMovesOut);
				else theseMoves = new Moves(board, numOfMovesOut+1);
				theseMoves.resolveMoves(solution);
				if(theseMoves.hasUnblocked())
				{
					theseMoves.trimBlocked();
					if(states.containsKey(board))
					{	// convoluted way of getting the move we're replacing!
						Move otherMove = states.get(board).getMove(0).getStartingBoard().getPreviousMove();
						if(otherMove != null)
							otherMove.setStatus(MoveStatus.BLOCKED);
					}
					if(theseMoves.hasWinner())
					{
						theseMoves.trimToWinner();
						move.setStatus(MoveStatus.WINNING);
						if(solution.meetsTheseConditions(theseMoves.getMove(0).getNextBoard()))	// if this is
							winFoundAt = numOfMovesOut;	// actually the solution rather than merely a step.
					}
					states.put(board, theseMoves);
				} else move.setStatus(MoveStatus.BLOCKED);
			} else move.setStatus(MoveStatus.BLOCKED);
		}
	}
}
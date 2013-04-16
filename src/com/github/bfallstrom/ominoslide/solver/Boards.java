package com.github.bfallstrom.ominoslide.solver;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
		List<Board> allBoards;
		Iterator<Board> iter;

		allBoards = new ArrayList<Board>(states.keySet());	// Can't use the keyset directly because of
		iter = allBoards.iterator();						//  ConcurrentModificationException!
		while(iter.hasNext())
		{
			Board board = iter.next();
			if(states.containsKey(board)) // if it doesn't, the list is outdated and we don't need to handle it!
			{
				Moves moves = states.get(board);
				if(moves != null && moves.hasUnblocked())
				{
					moves.trimBlocked();
					if(moves.hasWinner())
					{
						moves.trimToWinner();
						while(board.getPreviousMove() != null)
						{
							board.getPreviousMove().setStatus(MoveStatus.WINNING);
							board = board.getPreviousMove().getStartingBoard();
						}
					} else
					{
						for(int i = 0; i < moves.getNumberOfMoves(); i++)
						{
							Move move = moves.getMove(i);
							checkMove(move, moves.getMoveDepth(), solved);
						}
					}
				} else
				{
					if(board.getPreviousMove() != null)
						board.getPreviousMove().setStatus(MoveStatus.BLOCKED);
					states.remove(board);
				}
			}
		}
		if(states.get(rootPosition).hasWinner())
			states.get(rootPosition).trimToWinner();
		if(!states.get(rootPosition).hasUnblocked())
			throw new RuntimeException("WARNING! This puzzle appears to be unsolvable!");
		return states.get(rootPosition).hasWinner();
	}
	
	/**
	 * Gets the solution and returns it as a List of Move objects. Each in sequence takes you from
	 *  one Board to the next, starting at the first Board.
	 * @return the solution in List<Move> form.
	 */
	public List<Move> getSolution()
	{
		List<Move> solution = new ArrayList<Move>();
		Board board = rootPosition;
		while(states.containsKey(board))
		{
			Moves moves = states.get(board);
			if(!moves.hasWinner())
				throw new RuntimeException("WARNING! You must complete solving before getting the solution!");
			moves.trimToWinner();
			solution.add(moves.getMove(0));
			board = moves.getMove(0).getNextBoard();
		}
		return solution;
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
			if(!states.containsKey(board))
			{	// Only insert if an equivalent board is not already inserted, since we do a breadth-first search.
				Moves theseMoves;
				if(move.getOminoIndex() == move.getStartingBoard().getLastPieceIndex())
					theseMoves = new Moves(board, numOfMovesOut);
				else theseMoves = new Moves(board, numOfMovesOut+1);
				theseMoves.resolveMoves(solution);
				if(theseMoves.hasUnblocked())
				{
					theseMoves.trimBlocked();
					/*if(states.containsKey(board))
					{	// convoluted way of getting the move we're replacing!
						Move otherMove = states.get(board).getMove(0).getStartingBoard().getPreviousMove();
						if(otherMove != null)
							otherMove.setStatus(MoveStatus.BLOCKED);
					}*/
					if(theseMoves.hasWinner())
					{
						theseMoves.trimToWinner();
						move.setStatus(MoveStatus.WINNING);
						if(solution.meetsTheseConditions(theseMoves.getMove(0).getNextBoard()))	// if this is
							winFoundAt = numOfMovesOut;	// actually the solution rather than merely a step.
					}
					move.setStatus(MoveStatus.GENERATED);	// so we don't redundantly check except to prune.
					states.put(board, theseMoves);
				} else move.setStatus(MoveStatus.BLOCKED);
			} else move.setStatus(MoveStatus.BLOCKED);
		}
	}
}
package com.github.bfallstrom.ominoslide.solver;

import com.github.bfallstrom.ominoslide.areastructure.Board;
import com.github.bfallstrom.ominoslide.areastructure.Direction;
import com.github.bfallstrom.ominoslide.areastructure.Omino;

public class Move {
	private Board startsAt = null;
	private Board leadsTo = null;
	private MoveStatus status = MoveStatus.UNKNOWN;
	private Direction direction = null;
	private Omino piece = null;
	

	/**
	 * Constructs a new Move, but does not immediately resolve it. Move status and the Board it leads to
	 *  are not initialized: status remains UNKNOWN.
	 * @param startingPosition The Board this move is starting from.
	 * @param moveDirection The direction to attempt to slide an omino.
	 * @param ominoIndex The index of the omino to slide in the board's index.
	 */
	public Move(Board startingPosition, Direction moveDirection, int ominoIndex)
	{
		// TODO: NYI
	}
	
	/**
	 * Generates the new Board that this move leads to, and tests it. If the move cannot be made, status
	 *  is set to BLOCKED and the method returns BLOCKED. If the board is a winning position, status is
	 *  set to WINNING and the method returns WINNING. Otherwise the method returns UNKNOWN and status
	 *  remains set to that.
	 * @return MoveStatus denoting the resolved status of this Move.
	 */
	public MoveStatus resolveMove()
	{
		// TODO: NYI
		return null;
	}
}

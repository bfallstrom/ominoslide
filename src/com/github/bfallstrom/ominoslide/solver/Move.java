package com.github.bfallstrom.ominoslide.solver;

import com.github.bfallstrom.ominoslide.areastructure.Board;
import com.github.bfallstrom.ominoslide.areastructure.Direction;
import com.github.bfallstrom.ominoslide.areastructure.WinningPosition;

public class Move {
	private Board startsAt = null;
	private Board leadsTo = null;
	private MoveStatus status = MoveStatus.UNKNOWN;
	private Direction direction = null;
	private int piece;
	

	/**
	 * Constructs a new Move, but does not immediately resolve it. Move status and the Board it leads to
	 *  are not initialized: status remains UNKNOWN.
	 * @param startingPosition The Board this move is starting from.
	 * @param moveDirection The direction to attempt to slide an omino.
	 * @param ominoIndex The index of the omino to slide in the board's index.
	 */
	public Move(Board startingPosition, Direction moveDirection, int ominoIndex)
	{
		this.startsAt = startingPosition;
		this.direction = moveDirection;
		this.piece = ominoIndex;
	}
	
	/**
	 * Generates the new Board that this move leads to, and tests it. If the move cannot be made, status
	 *  is set to BLOCKED and the method returns BLOCKED. If the board is a winning position, status is
	 *  set to WINNING and the method returns WINNING. Otherwise the method returns UNKNOWN and status
	 *  remains set to that.
	 * @return MoveStatus denoting the resolved status of this Move.
	 */
	public MoveStatus resolveMove(WinningPosition winner)
	{
		leadsTo = new Board(this.startsAt);
		leadsTo.setPreviousMove(this);
		if(!leadsTo.shiftOmino(piece, direction)) // If the shift fails, then this move is BLOCKED!
			this.setStatus(MoveStatus.BLOCKED);
		else if(winner.meetsTheseConditions(leadsTo))
			this.setStatus(MoveStatus.WINNING);
		return status;
	}
	
	/**
	 * Gets the current status of this Move without resolving it.
	 * @return The current status.
	 */
	public MoveStatus getStatus()
	{
		return status;
	}
	
	/**
	 * Sets the current status of this Move. Should only be used based on a known resolution:
	 *  eg. marking a move as WINNING when it is known to lead to a winning position later, or
	 *  marking a move as BLOCKED when all possible moves after it are marked as BLOCKED.
	 * A move should be marked as BLOCKED when it is invalid, or it leads to a repetitive state,
	 *  or it is being trimmed due to a winning move set taking fewer moves having been discovered,
	 *  or every possible move in the configuration it leads to being already marked as BLOCKED.
	 * @param newStatus
	 */
	public void setStatus(MoveStatus newStatus)
	{
		this.status = newStatus;
		if(newStatus == MoveStatus.BLOCKED)
			this.leadsTo = null;	// If we are rejecting this move, drop the Board reference it leads to.
	}
	
	public Board getNextBoard()
	{
		return this.leadsTo;
	}
	
	public int getOminoIndex()
	{
		return piece;
	}
	
	public Direction getMoveDirection()
	{
		return this.direction;
	}
	
	public Board getStartingBoard()
	{
		return this.startsAt;
	}
}
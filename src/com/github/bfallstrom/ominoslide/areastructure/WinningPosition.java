package com.github.bfallstrom.ominoslide.areastructure;

/**
 * A special type of board to store the locations of ominos in a winning position. This
 *  should store ONLY the positions to test, and other ominos should be left out.
 * This class represents the conditions required for a solution: just the particular pieces that
 *  must be in given positions.
 * @author bfallstrom
 *
 */
public final class WinningPosition extends Board {
	/**
	 * Compares with regular Boards to determine if they are winning
	 *  positions. equals() is not overridden since it would not remain commutative.
	 * @param board The Board to test. Since WinningPosition is a subclass, this method will
	 *  throw an IllegalArgumentException if passed another WinningPosition.
	 * @return true iff all the pieces in this WinningPosition are arranged in the same locations
	 *  on the given Board, regardless of the arrangement of other pieces. 
	 */
	public boolean meetsTheseConditions(Board board)
	{
		if(board instanceof WinningPosition)	// We shouldn't be comparing two win conditions!
			throw new IllegalArgumentException("ERROR: Tried to test a win condition on another win condition!");
		for (int i = 0; i < this.pieceOrder.size(); i++) {
			Omino o = this.pieceOrder.get(i);
			if(!(board).allPieces.get(o).equals(this.allPieces.get(o)))
				return false;
		}
		return true;
	}
}
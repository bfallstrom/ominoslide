package com.github.bfallstrom.ominoslide.areastructure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class Board {
	private Map<Omino, Tile> allPieces = new HashMap<Omino, Tile>(); // Every piece inserted into the map should
	private List<Omino> pieceOrder = new ArrayList<Omino>(); // immediately be inserted into the list as well.
	
	private Set<Tile> layout = new HashSet<Tile>();
	int lastPieceMoved = -1;

	public Board(Board oldBoard)
	{
		this.layout = oldBoard.layout; // we expect the board itself to be stable
		this.lastPieceMoved = oldBoard.lastPieceMoved;
		this.allPieces = new HashMap<Omino, Tile>(oldBoard.allPieces);
		this.pieceOrder = oldBoard.pieceOrder; // we expect the pieces to be stable
	}
	
	public Board() {
	}
	
	public Board(Collection<Tile> tiles)
	{
		layout.addAll(tiles);
	}
	
	/**
	 * Adds a tile to the layout. Should only be used in initialization--once you start placing ominos,
	 *  you should stop altering the layout.
	 * @param tile the tile to add to the layout
	 * @return true if the tile was added; false if the tile already existed in the layout and therefore
	 *  nothing was changed.
	 */
	public boolean addTile(Tile tile)
	{
		return layout.add(tile);
	}
	
	/**
	 * Adds tiles to the layout. Should only be used in initialization--once you start placing ominos,
	 *  you should stop altering the layout.
	 * @param tiles The collection of tiles to add.
	 * @return true if the collection contained any tiles that successfully added.
	 */
	public boolean addTiles(Collection<Tile> tiles)
	{
		return layout.addAll(tiles);
	}
	
	/**
	 * Removes a tile from the layout. Must only be used in initialization--once you start placing ominos,
	 *  you must stop altering the layout, especially regarding removal of tiles.
	 * @param tile the tile to remove from the layout
	 * @return true if the tile was removed; false if the tile did not exist in the layout and therefore
	 *  nothing was changed.
	 */
	public boolean removeTile(Tile tile)
	{
		return layout.remove(tile);
	}
	
	/**
	 * Removes tiles from the layout. Must only be used in initialization--once you start placing ominos,
	 *  you must stop altering the layout, especially regarding removal of tiles.
	 * @param tiles The collection of tiles to remove.
	 * @return true if the collection contained any tiles that were successfully removed.
	 */
	public boolean removeTiles(Collection<Tile> tiles)
	{
		return layout.removeAll(tiles);
	}

	/**
	 * Places an omino with its origin at 0,0. 
	 * @param newOmino The omino to be placed
	 * @return true if the placement succeeded, false if it failed.
	 */
	public boolean placeOmino(Omino newOmino)
	{
		return this.placeOmino(newOmino, Tile.ZERO);
	}
	
	
	
	/**
	 * Places an omino at the specified location.
	 * @param newOmino The omino to be placed
	 * @param origin The position it is to be placed in.
	 * @return true if the placement succeeded, false if it failed.
	 */
	public boolean placeOmino(Omino newOmino, Tile origin)
	{
		Set<Tile> shape = newOmino.getShape();
		for(Tile tile : shape)
		{
			if(!isAvailable(new Tile(tile,origin)))
				return false;
		}
		allPieces.put(newOmino, origin);
		pieceOrder.add(newOmino);
		return true;
	}
	
	/**
	 * 
	 * @return The index in the internal list of the last piece that was moved.
	 */
	public int getLastPieceIndex()
	{
		return lastPieceMoved;
	}
	
	/**
	 * 
	 * @return The last Omino to be moved; null if the referring index is invalid.
	 */
	public Omino getLastPieceObject()
	{
		if(this.lastPieceMoved < 0 || this.lastPieceMoved >= pieceOrder.size())
			return null;
		return pieceOrder.get(this.lastPieceMoved);
	}
	
	/**
	 * 
	 * @return The total number of ominos that can slide around on this board. 
	 */
	public int getNumPieces()
	{
		return pieceOrder.size();
	}
	
	/**
	 * Gets the Tile position of the specified omino, or null if the index is invalid.
	 * @param index The index in the internal list of the omino to search for.
	 * @return A Tile representing the origin position of the omino in the board's current state,
	 *  or null if the index is invalid.
	 */
	public Tile getOminoPosition(int index)
	{
		if(index < 0 || index >= pieceOrder.size())
			return null;
		return allPieces.get(pieceOrder.get(index));
	}
	
	/**
	 * Gets the Tile position of the specified omino, or null if the piece is not on the board.
	 * @param omino The omino to search for.
	 * @return A Tile representing the origin position of the omino in the board's current state,
	 *  or null if the omino is not on the board.
	 */
	public Tile getOminoPosition(Omino omino)
	{
		return allPieces.get(omino);
	}
	
	
	/**
	 * Attempts to shift the omino with the given index in this board in the given direction.
	 *  Fails if the shift is blocked, or if the index does not retrieve an actual omino.
	 *  If the shift is successful, updates the state of the board by changing lastPieceMoved to
	 *  match the given index. The new entry in the Boards map should increment its number of moves
	 *  value only if the last piece to move differed from this piece.
	 * @param ominoIndex The index in the internal List of ominos for this board.
	 * @param direction
	 * @return true iff the shift was successful, false if unsuccessful and nothing was moved.
	 */
	public boolean shiftOmino(int ominoIndex, Direction direction)
	{
		if(ominoIndex < 0 || ominoIndex >= pieceOrder.size())
			return false;
		
		return false; // TODO: NYI
	}
	
	
	/**
	 * Tests if a tile can be used for moving or placing a piece. If the tile is not in the layout
	 * or if an omino is already on the tile, it returns false; otherwise true.
	 * @param tile The absolute tile to test
	 * @return true if the space is open and usable, false if used or outside the board
	 */
	private boolean isAvailable(Tile tile)
	{
		if(!layout.contains(tile))
			return false;
		for(Omino existing : pieceOrder)
		{
			if(existing.isInOmino(tile, allPieces.get(existing)))
				return false;
		}
		return true;
	}
	
	/**
	 * Overrides Object.equals to return true if the layout is exactly the same, with identically-shaped
	 *  ominos placed in the same location being treated as identical for this purpose.
	 */
	public boolean equals(Object otherBoard)
	{
		if(!(otherBoard instanceof Board) || ((Board)otherBoard).pieceOrder.size() != this.pieceOrder.size())
			return false;
		for (int i = 0; i < this.pieceOrder.size(); i++) {
			Omino o = this.pieceOrder.get(i);
			if(((Board)otherBoard).pieceOrder.get(i) != o)	// must be exact same references!
				return false;
			if(!((Board)otherBoard).allPieces.get(o).equals(this.allPieces.get(o)))
			{
				boolean matchFound = false;
				for(Omino possibleShape : this.pieceOrder)
				{
					Tile pos = ((Board)otherBoard).allPieces.get(possibleShape);
					if(pos != null && pos.equals(this.allPieces.get(o)) && o.isSameShape(possibleShape))
					{
						matchFound = true;
						break;
					}
				}
				if(!matchFound)
					return false;
			}
		}
		return true;
	}
}

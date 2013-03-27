package com.github.bfallstrom.ominoslide.areastructure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class Omino {
	Set<Tile> shape = new HashSet<Tile>();
	
	Set<Tile> borderUp = new HashSet<Tile>();
	Set<Tile> borderDown = new HashSet<Tile>();
	Set<Tile> borderLeft = new HashSet<Tile>();
	Set<Tile> borderRight = new HashSet<Tile>();
	
	Integer uniqueId = null;
	
	//Tile pieceOrigin; // now origin always assumed to be zero, no reason why not
	
	/*
	public Omino(List<Tile> existingShape, Tile originPoint)
	{
		this.currentPosition = new Tile(originPoint.getX(), originPoint.getY(), TilePart.VIRTUAL);
		for (Tile tile : existingShape) {
			if(!shape.contains(tile))
				shape.add(tile);
		}
	}
	*/
	private Omino()
	{
		//pieceOrigin = null;
	}
	
	/*
	public Omino(int[] xCoords, int[] yCoords)
	{
		// Silently discards coordinate data from past the matching portions of the arrays if one is longer
		int lastIndex = Math.min(xCoords.length, yCoords.length);

		for(int i = 0; i < lastIndex; i++)
		{
			//Tile tile = new Tile(xCoords[i]-originX, yCoords[i]-originY, TilePart.OMINO, this);
			//Tile tile = new Tile(xCoords[i]-originX, yCoords[i]-originY, this);
			Tile tile = new Tile(xCoords[i], yCoords[i]);
			if(!shape.contains(tile))
				shape.add(tile);
		}
		//this.currentPosition = new Tile(originX, originY, TilePart.VIRTUAL);
		//this.pieceOrigin = new Tile(originX, originY);
		generateBorders();
	}*/
	
	/**
	 * The constructor for a new omino from a collection of tiles. It is required that you
	 * create all ominos arranged such that they include the relative tile {0,0}. This will force
	 * all omino coordinates in the board to be unique!
	 * @param tiles The Collection of tiles to include in the new omino.
	 */
	public Omino(Collection<Tile> tiles)
	{
		if(!shape.contains(Tile.ZERO))
			throw new IllegalArgumentException("New Omino must contain Tile at (0, 0)!");
		for(Tile tile : tiles)
		{
			if(!shape.contains(tile))
				shape.add(tile);
		}
		generateBorders();
	}
	
	/**
	 * Create a new omino with the same tiles as the old one. This should be used to create
	 *  many identically-shaped pieces. The solving algorithm will be much less efficient
	 *  if you create ominos with the same shape but different relative points of origin,
	 *  since it will not recognize them as the same shape when comparing board equivalence.
	 * @param oldOmino
	 */
	public Omino(Omino oldOmino)
	{
		for (Tile tile : oldOmino.shape) {
			this.shape.add(new Tile(tile));
		}
		this.generateBorders();
	}
	
	/**
	 * Tests if a tile is part of this omino. Should only be used internally.
	 * @param testTile The omino-relative tile to be tested
	 * @return true if the tile is part of this omino
	 */
	private boolean isInOmino(Tile testTile)
	{
		/*if(testTile.getPiece() != null && testTile.getPiece() != this)
		{
			return this.isInOmino(new Tile(testTile.getX() + testTile.getPiece().pieceOrigin.getX(),
					testTile.getY() + testTile.getPiece().pieceOrigin.getY()));
		} else if(testTile.getPiece() == null)
		{
			return this.isInOmino(new Tile(testTile.getX() - this.pieceOrigin.getX(),
					testTile.getY() - this.pieceOrigin.getY(),
					this));
		} else*/			
			return shape.contains(testTile);
	}
	
	/**
	 * Tests if a tile is part of this omino, assuming this omino's origin is at a given point.
	 * @param testTile The tile to be tested. Relative tile.
	 * @param originPoint The place where this omino's origin should be assumed to be. Absolute tile.
	 * @return true if the tile is within this omino's bounds.
	 */
	public boolean isInOmino(Tile testTile, Tile originPoint)
	{
		Tile relativeTile = testTile.minus(originPoint);
		return isInOmino(relativeTile);
	}
	
	/**
	 * WARNING: Do not mutate the shape set after the reference is returned; doing so could lead to unstable
	 *   behavior. Letting the reference leave the class at all is only allowed for memory efficiency.
	 * @return The tiles contained in this omino.
	 */
	public Set<Tile> getShape()
	{
		return this.shape;
	}
	
	/**
	 * Returns the number of tiles in this omino. For comparing identically-shaped pieces.
	 * @return the number of tiles in this omino
	 */
	public int getNumTiles()
	{
		return this.shape.size();
	}
	
	/**
	 * WARNING: Do not mutate the border set after the reference is returned; doing so could lead to unstable
	 *   behavior. Letting the reference leave the class at all is only allowed for memory efficiency.
	 * @return The tiles contained in the upper border to this omino.
	 */
	public Set<Tile> getBorderUp()
	{
		return this.borderUp;
	}
	
	/**
	 * WARNING: Do not mutate the border set after the reference is returned; doing so could lead to unstable
	 *   behavior. Letting the reference leave the class at all is only allowed for memory efficiency.
	 * @return The tiles contained in the lower border to this omino.
	 */
	public Set<Tile> getBorderDown()
	{
		return this.borderDown;
	}
	
	/**
	 * WARNING: Do not mutate the border set after the reference is returned; doing so could lead to unstable
	 *   behavior. Letting the reference leave the class at all is only allowed for memory efficiency.
	 * @return The tiles contained in the left border to this omino.
	 */
	public Set<Tile> getBorderLeft()
	{
		return this.borderLeft;
	}
	
	/**
	 * WARNING: Do not mutate the border set after the reference is returned; doing so could lead to unstable
	 *   behavior. Letting the reference leave the class at all is only allowed for memory efficiency.
	 * @return The tiles contained in the right border to this omino.
	 */
	public Set<Tile> getBorderRight()
	{
		return this.borderRight;
	}
	
	/**
	 * Sets a unique ID for tracking win conditions
	 * @param id the id to set
	 */
	public void setUniqueId(int id)
	{
		this.setUniqueId(new Integer(id));
	}
	
	/**
	 * Sets a unique ID for tracking win conditions
	 * @param id the id to set
	 */
	public void setUniqueId(Integer id)
	{
		this.uniqueId = id;
	}
	
	/**
	 * Gets the unique ID for tracking win conditions
	 * @return the unique ID, or null if there is none on this omino.
	 */
	public Integer getUniqueId()
	{
		return this.uniqueId;
	}
	
	/**
	 * Should only be called at the end of the constructor; creates sets for determining safe shift locations.
	 */
	private void generateBorders()
	{
		for(Tile currTile : shape) {
			// test UP
			Tile borderTile = new Tile(currTile, Tile.UP);
			if(!isInOmino(borderTile))
				borderUp.add(borderTile);
			// test DOWN
			borderTile = new Tile(currTile, Tile.DOWN);
			if(!isInOmino(borderTile))
				borderDown.add(borderTile);
			// test LEFT
			borderTile = new Tile(currTile, Tile.LEFT);
			if(!isInOmino(borderTile))
				borderLeft.add(borderTile);
			// test RIGHT
			borderTile = new Tile(currTile, Tile.RIGHT);
			if(!isInOmino(borderTile))
				borderRight.add(borderTile);
		}
	}
	
	/**
	 * Compares two ominos by shape. The .equals method is not used because it would wreak havoc with
	 *  the Map used in implementing the Board and must remain the default Object implementation of
	 *  "same object reference".
	 * @param other The omino to compare to
	 * @return true if both ominos have the same tile coordinates and are both null for uniqueId.
	 */
	public boolean isSameShape(Omino other)
	{
		if(this.uniqueId != null || other.uniqueId != null)
		{
			if(this.uniqueId == other.uniqueId)
				return true;	// Ids MUST be made unique or results will be unpredictable!
			return false;
		}
		if(this.shape.size() != other.shape.size())
			return false;
		for(Tile tile : this.shape)
		{
			if(!other.isInOmino(tile))
				return false;
		}
		return true;
	}
}

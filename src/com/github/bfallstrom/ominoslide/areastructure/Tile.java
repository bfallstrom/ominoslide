package com.github.bfallstrom.ominoslide.areastructure;

// Represents a single tile on the board or as a piece of a sliding part
public final class Tile implements Cloneable {
	private final int xCoord;
	private final int yCoord;
	//private final TilePart tileType;
	//private Omino piece;	// Removed since relative/absolute tiles are now determined from context
	
	public static final Tile UP		= new Tile( 0, 1);
	public static final Tile DOWN	= new Tile( 0,-1);
	public static final Tile LEFT	= new Tile(-1, 0);
	public static final Tile RIGHT	= new Tile( 1, 0);
	public static final Tile ZERO	= new Tile( 0, 0);
	
	//public Tile(int x, int y, TilePart type)
	public Tile(int x, int y)
	{
		this.xCoord = x;
		this.yCoord = y;
	//	this.tileType = type;
		//this.piece = null;
	}
	
	//public Tile(int x, int y, TilePart type, Omino piecePart)
	/*public Tile(int x, int y, Omino piecePart)
	{
	//	this(x, y, type);
		this(x, y);
	//	if(type == TilePart.OMINO)
			this.piece = piecePart;	// otherwise defaults to null for the piece link
	}*/
	
	// replicates a tile without cloning the Omino reference
	public Tile(Tile sameTile)
	{
		this(sameTile, ZERO);
	}
	
	// takes a relative tile and its origin tile and returns an absolute tile!
	// order is unimportant since we are simply adding the coordinates.
	public Tile(Tile relativeTile, Tile originCoords)
	{
		this(relativeTile.xCoord + originCoords.xCoord, relativeTile.yCoord + originCoords.yCoord);
	}
	
	/**
	 * "Subtracts" two tiles; in effect this gives this tile relative to the parameter tile as origin.
	 * @param diffThisTile the tile to be subtracted
	 * @return The Tile representing the current tile relative to the parameter tile
	 */
	public Tile minus(Tile diffThisTile)
	{
		return new Tile(this.xCoord - diffThisTile.xCoord, this.yCoord - diffThisTile.yCoord);
	}
	
	/*
	public TilePart getType()
	{
		return this.tileType;
	}
	*/
	
	public int getX()
	{
		return this.xCoord;
	}
	
	public int getY()
	{
		return this.yCoord;
	}
	
	/*
	public Omino getPiece() // gets the associated Omino piece this is a part of; returns null if it's not an Omino piece
	{
		return this.piece;
	}
	
	public void setPiece(Omino newPiece) // Sets the associated Omino piece... USE WITH CAUTION
	{
		this.piece = newPiece; // If used any time other than cloning an Omino, this could cause unstable behavior.
	}*/
	
	public boolean equals(Object o)
	{
		if(o instanceof Tile)
		{
			return (this.xCoord == ((Tile)o).getX()) && (this.yCoord == ((Tile)o).getY());
		} else return false;
	}
	
	public String toString()
	{
		//return "{x:" + this.xCoord + ",y:" + this.yCoord + ",piece:\"" + ((this.piece==null)?"null":"omino") + "\"}";
		return "{x:" + this.xCoord + ",y:" + this.yCoord + "}";
	}
	
	public int hashCode()
	{
		return (this.yCoord << 16) | this.xCoord; // guarantees that for coordinate values small enough to be represented
		// in 16 bits, the hash code will return unique values where .equals implies uniqueness. If we need coordinate values
		// larger than that, the speed is likely to already be ridiculously slow and worrying about hash collisions will
		// be the least of our problems.
	}
}

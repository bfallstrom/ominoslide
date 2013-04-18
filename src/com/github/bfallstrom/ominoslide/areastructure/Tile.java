package com.github.bfallstrom.ominoslide.areastructure;

// Represents a single tile on the board or as a piece of a sliding part
public final class Tile implements Cloneable, Comparable<Tile> {
	private final int xCoord;
	private final int yCoord;
	
	public static final Tile UP		= new Tile( 0, 1);
	public static final Tile DOWN	= new Tile( 0,-1);
	public static final Tile LEFT	= new Tile(-1, 0);
	public static final Tile RIGHT	= new Tile( 1, 0);
	public static final Tile ZERO	= new Tile( 0, 0);
	
	public Tile(int x, int y)
	{
		this.xCoord = x;
		this.yCoord = y;
	}
	
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

	public int getX()
	{
		return this.xCoord;
	}
	
	public int getY()
	{
		return this.yCoord;
	}

	public boolean equals(Object o)
	{
		if(o instanceof Tile)
		{
			return (this.xCoord == ((Tile)o).getX()) && (this.yCoord == ((Tile)o).getY());
		} else return false;
	}
	
	public int compareTo(Tile o)
	{
		if(this.xCoord != o.xCoord)
			return this.xCoord - o.xCoord;	// Returns the difference in x coordinate if there is a difference
		return this.yCoord - o.yCoord;		// Otherwise returns the difference in y coordinate.
	}
	
	public String toString()
	{
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

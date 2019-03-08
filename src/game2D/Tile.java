package game2D;

/**
 * A Tile in the TileMap.
 * 
 * @author David Cairns
 *
 */
public class Tile {

	private char character=' ';	// The character associated with this tile
	private int xc=0;			// The tile's x coordinate in pixels
	private int yc=0;			// The tile's y coordinate in pixels
 	
	/**
	 * Create an instance of a tile
	 * @param c	The character associated with this tile
	 * @param x The x tile coordinate in pixels
	 * @param y The y tile coordinate in pixels
	 */
	public Tile(char c, int x, int y)
	{
		character = c;
		xc = x;
		yc = y;
	}

	/**
	 * @return The character for this tile
	 */
	public char getCharacter() {
		return character;
	}

	/**
	 * @param character The character to set the tile to
	 */
	public void setCharacter(char character) {
		this.character = character;
	}

	/**
	 * @return The x coordinate (in pixels)
	 */
	public int getXC() {
		return xc;
	}

	/**
	 * @return The y coordinate (in pixels)
	 */
	public int getYC() {
		return yc;
	}
}

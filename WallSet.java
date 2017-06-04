
public class WallSet {

	//a set of walls - used to break a room into walls for convenience
	Room genesis;
	Wall north;
	Wall east;
	Wall south;
	Wall west;
	int height;
	int width;
	
	public WallSet(Room genesis) {
		this.genesis = genesis;
		this.north = getSide(0);
		this.east = getSide(1);
		this.south = getSide(2);
		this.west = getSide(3);
		this.height = genesis.getHeight();
		this.width = genesis.getWidth();
	}
	
	public Wall getSide(int side)
	{
		//returns a specific side
		
		/*
		 * 0 = north
		 * 1 = east
		 * 2 = south
		 * 3 = west
		*/
		
		switch(side)
		{
		case (0):
		{
			coord start = new coord(genesis.getTopLeftX(), genesis.getTopLeftY());
			coord end = new coord(start.getX() + width, start.getY());
			return new Wall(start, end, false);
		}
		
		case (1):
		{
			coord start = new coord(genesis.getRightX(), genesis.getTopLeftY());
			coord end = new coord(start.getX(), start.getY() + height);
			return new Wall(start, end, true);
		}
		case (2):
		{
			coord start = new coord(genesis.getTopLeftX(), genesis.getTopLeftY() + height);
			coord end = new coord(start.getX() + width, start.getY() + width);
			return new Wall(start, end, false);
		}
		case (3):
		{
			coord start = new coord(genesis.getTopLeftX(), genesis.getTopLeftY());
			coord end = new coord(start.getX(), start.getY() + height);
			return new Wall(start, end, true);
		}
		}
		
		System.out.println("getSide machine broke");
		return null;
	}
}

import java.util.ArrayList;
import java.util.Random;

public class Room {
	
	int width;
	int height;
	int topLeftX;
	int topLeftY;
	ArrayList<Room> connectedRooms;
	
	public Room(int width, int height, int topLeftX, int topLeftY) {
		this.width = width;
		this.height = height;
		this.topLeftX = topLeftX;
		this.topLeftY = topLeftY;
		this.connectedRooms = new ArrayList<Room>();
	}
	
	public static int randInt(int min, int max)
	{
	    Random rand = new Random();
	    int randomNum = rand.nextInt((max - min) + 1) + min;
	    return randomNum;
	}
	
	public int getTopLeftX()
	{
		return topLeftX;
	}
	
	public int getTopLeftY()
	{
		return topLeftY;
	}
	
	public int getBotY()
	{
		return topLeftY + height;
	}
	
	public int getRightX()
	{
		return topLeftX + width;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public boolean hasInside(int x, int y, int buf)
	{
		return(x > topLeftX-buf && x < topLeftX+width+buf && y > topLeftY-buf && y < topLeftY + height + buf);
	}
	
	public boolean conflictsWith(Room b, int buf)
	{
		int aLX = getTopLeftX();
		int aTY = getTopLeftY();
		int aRX = getTopLeftX() + getWidth();
		int aBY = getTopLeftY() + getHeight(); 
		
		int bLX = b.getTopLeftX();
		int bTY = b.getTopLeftY();
		int bRX = b.getTopLeftX() + b.getWidth();
		int bBY = b.getTopLeftY() + b.getHeight(); 
		
		if(b.hasInside(aLX, aTY, buf))
			return true;
		if(b.hasInside(aLX, aBY, buf))
			return true;
		if(b.hasInside(aRX, aTY, buf))
			return true;
		if(b.hasInside(aRX, aBY, buf))
			return true;
		
		if(hasInside(bLX, bTY, buf))
			return true;
		if(hasInside(bLX, bBY, buf))
			return true;
		if(hasInside(bRX, bTY, buf))
			return true;
		if(hasInside(bRX, bBY, buf))
			return true;
		
		return false;
	}
	
	public WallSet toWallSet()
	{
		return new WallSet(this);
	}
	
	public Tendon getTendon(Room b)
	{
		WallPair toMake = this.getImportantWalls(b);
		Wall start = toMake.getA();
		Wall end = toMake.getB();
		int pattern = toMake.getPattern();
		
		switch(pattern)
		{
		case 1:
		{
			System.out.println("Stright a: " + start.toString());
			System.out.println("Straight b: "+ start.toString());
			coordPair a = start.getStraightPair(end);
			return new Tendon(a.getA(),a.getB(), 1);
		}
		case 2:
		{
			System.out.println("     "+start.getValidSpots().length);
			System.out.println("     "+end.getValidSpots().length);
			System.out.println("//start" + start.toString());
			System.out.println("End " + end.toString());
			
			return new Tendon(start.getValidSpots()[Room.randInt(0, start.getValidSpots().length-1)], 
					end.getValidSpots()[Room.randInt(0, end.getValidSpots().length-1)], 2);
		}
		case 3:
		{
			return new Tendon(start.getValidSpots()[Room.randInt(0, start.getValidSpots().length-1)], 
					end.getValidSpots()[Room.randInt(0, end.getValidSpots().length-1)], 3);
		}
		case 4:
		{
			coordPair a = start.getCornerSpot(end);
			return new Tendon(a.getA(), a.getB(), 4);
		}
		case 5:
		{
			coordPair a = start.getCornerSpot(end);
			return new Tendon(a.getA(), a.getB(), 5);
		}
		}
		
		return null;
	}
	
	public WallPair getImportantWalls(Room toTest)
	{
		WallSet a = new WallSet(this);
		WallSet b = new WallSet(toTest);
		
		ArrayList<WallPair> viable = new ArrayList<WallPair>();
		
		//check for direct overlap, return a good wallPair
		
		if(a.getSide(1).overlaps(b.getSide(3)))
		{
			if(this.getTopLeftX() < toTest.getTopLeftX())
			{
				//viable.add
				return(new WallPair(a.getSide(1), b.getSide(3), 1));
			}
			if(this.getTopLeftX() > toTest.getTopLeftX())
			{
				//viable.add
				return(new WallPair(a.getSide(3), b.getSide(1), 1));
			}
		}
		
		if(a.getSide(0).overlaps(b.getSide(2)))
		{
			if(this.getTopLeftY() < toTest.getTopLeftY())
			{
				//viable.add
				return(new WallPair(a.getSide(2), b.getSide(0), 1));
			}
			if(this.getTopLeftY() > toTest.getTopLeftY())
			{
				//viable.add
				return(new WallPair(a.getSide(0), b.getSide(2), 1));
			}
		}
		
		//no overlap, now check for other patterns
		
		//check heights and see if there's a margin
		
		if(this.getBotY() < toTest.getTopLeftY() - 3 )
		{
			viable.add(new WallPair(a.getSide(2),b.getSide(0),3));
		}
		if(this.getTopLeftY() > toTest.getBotY() + 3)
		{
			viable.add(new WallPair(a.getSide(0),b.getSide(2),3));
		}
		
		//check the lateral and see if there's a gap
		
		if(this.getRightX() + 3 < toTest.getTopLeftX())
		{
			viable.add(new WallPair(a.getSide(1), b.getSide(3), 2));
		}
		if(this.getTopLeftX() - 3 > toTest.getRightX())
		{
			viable.add(new WallPair(a.getSide(3), b.getSide(1), 2));
		}
		
		//check if there's enough space for a vertical ending corner 
		//also works for horizontal?
		//I don't remember writing this code
		
		if(this.getRightX() + 2 < toTest.getRightX())
		{
			if(this.getTopLeftY() < toTest.getTopLeftY() - 5)
			{
				//-> v
				viable.add(new WallPair(a.getSide(1), b.getSide(0), 5));
			}
			
			if(this.getTopLeftY() - 5 > toTest.getTopLeftY())
			{
				//-> ^
				viable.add(new WallPair(a.getSide(1), b.getSide(2), 5));
			}
			
		}
		if(this.getTopLeftX() - 2 > toTest.getTopLeftX())
		{
			if(this.getTopLeftY() < toTest.getTopLeftY() - 5)
			{
				//-> v
				viable.add(new WallPair(a.getSide(3), b.getSide(0), 5));
			}
			
			if(this.getTopLeftY() - 5 > toTest.getTopLeftY())
			{
				//-> ^
				viable.add(new WallPair(a.getSide(3), b.getSide(2), 5));
			}
		}
		
		
		System.out.println(viable.size());
		//try{
		return viable.get(Room.randInt(0, viable.size()-1));
		//}catch()
	}
	
	public Object pick(Object a, Object b)
	{
		//picks a random object from two
		///never used in the entire program but I might need it someday
		if(Room.randInt(0, 1) == 1)
		{
			return a;
		}
		else return b;
	}

	public double getDistance(Room toTest)
	{
		return(Math.sqrt(
					Math.pow(Math.abs(this.getTopLeftX() - toTest.getTopLeftX()), 2) +
					Math.pow(Math.abs(this.getTopLeftY() - toTest.getTopLeftY()), 2)
					)
				);
	}
	
	public void addConnected(Room g)
	{
		System.out.println(connectedRooms.isEmpty());
		connectedRooms.add(g);
	}
	
	public boolean isConnected(Room a)
	{
		if(connectedRooms == null)
			return false;
		for(int i = 0; i < connectedRooms.size(); i++)
		{
			if(a.topLeftX == connectedRooms.get(i).topLeftX && a.topLeftY == connectedRooms.get(i).topLeftY)
			{
				return true;
			}
		}
		
		return false;
	}
	
	public ArrayList<Room> getConnectList()
	{
		return connectedRooms;
	}
}














































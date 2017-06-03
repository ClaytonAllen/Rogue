import java.util.ArrayList;

public class Wall {

	coord start;
	int length;
	boolean vertical;
	coord end;
	coord[] line;
	
	public Wall(coord start, coord end, boolean vertical) {
		this.start = start;
		this.end = end;
		this.vertical = vertical;
		if(vertical)
		{
			
			this.length = end.getY() - start.getY(); 
			this.line = new coord[length];
			for(int i = 0; i < length; i++)
			{
				this.line[i] = new coord(start.getX(),start.getY()+i);
			}
		}
		else
		{
			this.length = end.getX() - start.getX();
			this.line = new coord[length];
			for(int i = 0; i < length; i++)
			{
				this.line[i] = new coord(start.getX()+i,start.getY());
			}
		}
	}
	
	public coord[] getLine()
	{
		return line;
	}

	public coord[] getValidSpots()
	{
		coord[] valid = new coord[length-2];
		for(int i = 2; i<length; i++)
		{
			valid[i-2] = 
					line[i];
		}
		
		return valid;
	}
	
	public boolean getVert()
	{
		return vertical;
	}

	public boolean overlaps(Wall b)
	{
		//test parity 
		if(this.getVert() == b.getVert())
		{
			coord[] testA = this.getValidSpots();
			coord[] testB = b.getValidSpots();
			if(this.getVert() == true)
			{
				for(int i = 0; i < testA.length; i++)
				{
					for(int j = 0; j < testB.length; j++)
					{
						if(testA[i].getY() == testB[j].getY())
						{
							return true;
						}
					}
				}
			}
			else //Horizontal lines
			{
				for(int i = 0; i < testA.length; i++)
				{
					for(int j = 0; j < testB.length; j++)
					{
						if(testA[i].getX() == testB[j].getX())
						{
							return true;
						}
					}
				}
			}
			
			return false;
		}
		
		return false;
	}
	

	public coordPair getStraightPair(Wall b) 
	{
		if(this.overlaps(b))
		{
			if(this.getVert())
			{
				for(int i = 0; i<this.getValidSpots().length; i++)
				{
					for(int j = 0; j < b.getValidSpots().length; j++)
					{
						if(this.getValidSpots()[i].getY() == b.getValidSpots()[j].getY()
								&& b.getValidSpots()[j].getX() != this.getValidSpots()[i].getX())
						{
							return new coordPair(this.getValidSpots()[i],b.getValidSpots()[j]);
						}
					}
				}
			}
			else
			{
				for(int i = 0; i<this.getValidSpots().length; i++)
				{
					for(int j = 0; j < b.getValidSpots().length; j++)
					{
						if(this.getValidSpots()[i].getX() == b.getValidSpots()[j].getX() 
								&& b.getValidSpots()[j].getY() != this.getValidSpots()[i].getY())
						{
							return new coordPair(this.getValidSpots()[i],b.getValidSpots()[j]);
						}
					}
				}
			}
		}
		return null;
	}
	
	public coordPair getCornerSpot(Wall b)
	{
		coord[] aSpots = this.getValidSpots();
		coord[] bSpots = b.getValidSpots();
		ArrayList<coordPair> viable = new ArrayList<coordPair>();
		
		//find vertical and horizontal
		//if(this.getVert())
		{
			for(int i = 0; i< aSpots.length; i++)
			{
				for(int j = 0; j < bSpots.length; j++)
				{
					if(Math.abs(aSpots[i].getX() - bSpots[j].getX()) >= 1 && Math.abs(aSpots[i].getY() - bSpots[j].getY()) >= 1)
					{
						viable.add(new coordPair(aSpots[i], bSpots[j]));
					}
				}
			}
		}
		System.out.println("//viablesize: "+viable.size());
		return viable.get(Room.randInt(0, viable.size()-1));
		
	}

	public String toString()
	{
		return "Starts at " + start.getX() + "," +start.getY() + "  Ends at " + end.getX()+","+end.getY();
	}
}





































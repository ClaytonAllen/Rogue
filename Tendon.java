
public class Tendon {
	coord start;
	coord end;
	int pattern;
	coord[] coordSet;
	public Tendon(coord s, coord e, int p) {
		this.start = s;
		this.end = e;
		this.pattern = p;
		this.coordSet = getCoords();
		// TODO Auto-generated constructor stub
	}
	
	public coord[] getCoords()
	{
		switch (pattern)
		{
			
			case 1:
			{
				//straight across
				Line a = new Line(start, end);
				return a.getProjection();
			}
			/*
			case 2:
			{
				//straight down
				Line a = new Line(start, end);
				return a.getProjection();
			}
			*/
			case 2:
			{
				//over, then vertical, then over
				coord first = new coord((end.getX() + start.getX())/2, start.getY());
				coord second = new coord(first.getX(), end.getY());
				
				Line a = new Line(start, first);
				Line b = new Line(first, second);
				Line c = new Line(second, end);
				return getSetList(a,b,c);
			}
			case 3:
			{
				//vertical, then over, then vertical
				coord first = new coord(start.getX(), (end.getY() + start.getY())/2 );
				coord second = new coord(end.getX(), first.getY());
				
				Line a = new Line(start, first);
				Line b = new Line(first, second);
				Line c = new Line(second, end);
				return getSetList(a,b,c);
			}
			case 4:
			{
				//vertical, then over
				coord elbow = new coord(start.getX(), end.getY());
				
				Line a = new Line(start, elbow);
				Line b = new Line(elbow, end);
				return getSetList(a,b);
			}
			case 5:
			{
				//over, then vertical
				coord elbow = new coord(end.getX(), start.getY());
				System.out.println(start.getX() + " " + elbow.getX());
				Line a = new Line(start, elbow);
				Line b = new Line(elbow, end);
				return getSetList(a,b);
			}
			/*
			case 9: pattern = 9;
			{
				//down, then over
				coord elbow = new coord(start.getX(), end.getY());
				
				Line a = new Line(start, elbow);
				Line b = new Line(elbow, end);
				return getSetList(a,b);
			}
			case 10: pattern = 10;
			{
				//over, then up
				coord elbow = new coord(start.getY(), end.getX());
				Line a = new Line(start, elbow);
				Line b = new Line(elbow, end);
				return getSetList(a,b);
			}			
			*/
			
			/*of the ten patterns the copies work like this
			 * 1 2  (1)
			 * 3 5  (2)
			 * 4 6  (3)
			 * 7 9  (4)
			 * 8 10 (5)
			 * So I've deleted 2,5,6,9, and 10
			 * Just remember to call the right ones please!
			*/
		}
		return null;
	}
	
	private coord[] getSetList(Line a, Line b, Line c)
	{
		int holdNum = 0;
		coord[] hold = new coord[a.getProjection().length + b.getProjection().length + c.getProjection().length];
		
		for(int i = 0; i < a.getProjection().length; i++)
		{
			hold[holdNum] = a.getProjection()[i];
			holdNum++;
		}
		
		for(int i = 0; i < b.getProjection().length; i++)
		{
			hold[holdNum] = b.getProjection()[i];
			holdNum ++;
		}
		
		for(int i = 0; i < c.getProjection().length; i++)
		{
			hold[holdNum] = c.getProjection()[i];
			holdNum ++;
		}
		
		return hold;
	}
	
	private coord[] getSetList(Line a, Line b)
	{
		int holdNum = 0;
		System.out.println(a.getProjection() == null);
		coord[] hold = new coord[
		                         a.getProjection().length + 
		                         b.getProjection().length];
		
		
		for(int i = 0; i < a.getProjection().length; i++)
		{
			hold[holdNum] = a.getProjection()[i];
			holdNum++;
		}
		
		for(int i = 0; i < b.getProjection().length; i++)
		{
			hold[holdNum] = b.getProjection()[i];
			holdNum ++;
		}
		
		return hold;
	}
	
	public String toString()
	{
		return("Starts at "+start.getX()+","+start.getY()+" ends at "+end.getX()+","+end.getY()+" Pattern #" + pattern);
	}

}




























public class Line {

	coord start;
	coord end;
	coord[] projection;
	
	public Line(coord e, coord s) {
		this.start = s;
		this.end = e;
		this.projection = getProjection();
	}
	
	public coord[] getProjection() {
		
		//determine if line is vertical or horizontal
		
		if(start.getX() == end.getX())
		{
			//vertical down
			if(start.getY() < end.getY())
			{
				coord[] project = new coord[Math.abs(start.getY() - end.getY()) + 1];
				project[0] = start;
				for(int i = 1; i < project.length; i++)
					project[i] = new coord(start.getX(),start.getY() + i);
				return project;
			}
			
			//vertical up
			if(start.getY() > end.getY())
			{
				coord[] project = new coord[Math.abs(start.getY() - end.getY()) + 1];
				project[0] = start;
				for(int i = 1; i < project.length; i++)
					project[i] = new coord(start.getX(),start.getY() - i);
				return project;
			}
		}
		if(start.getY() == end.getY())
		{
			//horizontal to the right
			if(start.getX() < end.getX())
			{
				coord[] project = new coord[Math.abs(start.getX() - end.getX()) + 1];
				project[0] = start;
				for(int i = 1; i < project.length; i++)
					project[i] = new coord(start.getX() + i,start.getY());
				return project;
			}
			
			//horizontal to the left
			if(start.getX() > end.getX())
			{
				coord[] project = new coord[Math.abs(start.getX() - end.getX()) + 1];
				project[0] = start;
				for(int i = 1; i < project.length; i++)
					project[i] = new coord(start.getX() - i,start.getY());
				return project;
			}
		}
		
		System.out.println("That line isn't straight. (Start " + start.toString() + " // End " + end.toString() + ")");
		return null;
	}
}

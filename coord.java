
public class coord {

	int xVal;
	int yVal;
	
	public coord(int x, int y) {
		this.xVal = x;
		this.yVal = y;
	}

	public int getX()
	{
		return xVal;
	}
	
	public int getY()
	{
		return yVal;
	}
	
	public String toString()
	{
		return(""+xVal+","+yVal);
	}
}

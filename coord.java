
public class coord {

	//just holds an x and y value so I can move both values at once - very convenient
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


public class WallPair {
	Wall a;
	Wall b;
	int pattern;
	
	public WallPair(Wall a, Wall b, int pattern) {
		this.a = a;
		this.b = b;
		this.pattern = pattern;
	}
	
	public Wall getA()
	{
		return a;
	}
	
	public Wall getB()
	{
		return b;
	}
	
	public int getPattern()
	{
		return pattern;
	}
}


public class WallPair {
	//pair of walls and a pattern for the tendon between them
	//keeps the pattern because it's wayyyy easier to just hold the value than redetermine it
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

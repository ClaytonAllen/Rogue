import java.util.*;

public class Floor 
{
	char[][] level;
	ArrayList<Room> roomList;
	int height;
	int width;
	int buffer;
	int numRooms;
	
	public Floor(int width, int height, int numRooms, int buffer)
	{
		this.level = new char[width][height];
		this.roomList = new ArrayList<Room>();
		this.height = height;
		this.width = width;
		this.buffer = buffer;
		this.numRooms = numRooms;
		this.buffer = buffer;
	}
	
	public void initiate()
	{	
		//empty out the inside
		for(int i = 1; i < level.length-1; i++)
		{
			for(int j = 0; j < level[i].length; j++)
			{
				level[i][j] = ' ';
			}
		}
		
		//the edges
		char edgeChar = 'E';
		for(int i = 0; i < level[0].length; i++)
			level[0][i] = edgeChar;
		for(int i = 0; i < level[0].length; i++)
			level[level.length-1][i] = edgeChar;
		for(int i = 1; i<level.length-1;i++)
		{
			level[i][0] = edgeChar;
			level[i][level[i].length-2] = edgeChar;
		}
		
		//anchor room
		Room first = genFirstRoom();
		applyRoom(first);
		roomList.add(first);
	}
	
	public void renderFloor()
	{
		//clears the console kind of
		for (int i = 0; i<100; i++){
			System.out.println();
		}
		
		//empties out all the rooms
		for(Room a:roomList)
		{
			empty(a);
		}
		
		//prints the whole floor
		for(int j = 0; j < level[0].length-1; j++)
		{
			for(int i = 0; i <= level.length-1; i++)
			{
				System.out.print(level[i][j]);
			}
			System.out.println();
		}
	}
	
	public Room genFirstRoom()
	{
		return new Room(Room.randInt(20, 30),8,2,2);
	}
	
	public void applyRoom(Room room)
	{
		char roomChar = 'X';
		//^^^ defines the character used for room walls
		//draw top and bottom walls
		
		for(int i = room.getTopLeftX(); i < room.getWidth() + room.getTopLeftX(); i++)
		{
			level[i][room.getTopLeftY()] = roomChar;
		}
		for(int i = room.getTopLeftX(); i < room.getWidth() + room.getTopLeftX(); i++)
		{
			level[i][room.getTopLeftY()+room.getHeight()] = roomChar;
		}
		
		//draw left and right walls
		for(int i = room.getTopLeftY(); i < room.getTopLeftY() + room.getHeight(); i++)
		{
			level[room.getTopLeftX()][i] = roomChar;
		}
		for(int i = room.getTopLeftY(); i < room.getTopLeftY() + room.getHeight()+1; i++)
		{
			level[room.getTopLeftX()+room.getWidth()][i] = roomChar;
		}
		
		//roomList.add(room);
	}
	
	public Room genPosRoom(int size)
	{
		switch(size)
		{
		case 1:
			return new Room(5*Room.randInt(4,6),2*Room.randInt(4,6),0,0);
		case 2:
			return new Room(5*Room.randInt(2,3),2*Room.randInt(2,3),0,0);
		case 3:
			return new Room(5*Room.randInt(1,2),2*Room.randInt(1,2),0,0);
		}
		
		System.out.println("Unrecognized size argument at genPosRoom");
		return null;
	}
	
	public boolean conflictsWithEdges(Room room)
	{
		int rX = room.getTopLeftX() + room.getWidth();
		int bY = room.getTopLeftY() + room.getHeight();
		
		//check screen edges
		if (rX > width - buffer - 2)
			return true;
		if (bY > height - buffer - 2)
			return true;
		if (room.getTopLeftX() < 0 + buffer + 2)
			return true;
		if (room.getTopLeftY() < 0 + buffer + 2)
			return true;

		return false;
	}
	
	public ArrayList<Room> getRoomList()
	{
		return roomList;
	}
	
	public boolean conflictsWithList(Room a)
	{
		for(int i = roomList.size()-1; i>=0; i--)
		{
			if(a.conflictsWith(roomList.get(i), buffer))
				return true;
		}
		return false;
	}
	
	public coord findGoodSpot(Room room, int buffer)
	{
		for(int tries = 0; tries < 100; tries++)
		{
			int x = Room.randInt(buffer, width-room.getWidth()-buffer);
			int y = Room.randInt(buffer, height-room.getHeight()-buffer);
			
			//try that spot with the room
			
			Room toTest = new Room(room.getWidth(), room.getHeight(), x, y);
			if(!conflictsWithList(toTest)&&!conflictsWithEdges(toTest))
				return new coord(x,y);
		}
		return null;
	}
	
	public void generateRooms(int a, int numToGen)
	{
		for(int i = numToGen; i > 0; i--)
		{
			Room toTest = genPosRoom(a);
			
			coord spot = findGoodSpot(toTest, buffer);
			
			if(spot != null)
			{
				roomList.add(new Room(toTest.getWidth(), toTest.getHeight(), spot.getX(), spot.getY()));
			}
			
			if(spot == null)
			{
				break;
			}
		}
		
		for(int i = 0; i < roomList.size(); i++)
		{
			applyRoom(roomList.get(i));
		}
	}
	
	public void roomGenThree()
	{
		//1 is large, 3 is small
		generateRooms(1, numRooms);
		//generateRooms(2, numRooms/2);
		//generateRooms(3, numRooms/2);
	}
	
	public void applyTendon(Tendon t)
	{
		//hWC is hallWayCharacter
		char hWC = 'O';
		coord[] set = t.getCoords();
			for(coord c: set)
			{
				level[c.getX()][c.getY()] = hWC;
			}
	}
	
	public Room findClosest(Room toTest)
	{
		Room closest = null;
		int index = -1; //this breaks it if it doesn't find what it wants
		double distance = 0;
		for(int i = 0; i < roomList.size(); i++)
		{
			if((toTest.getTopLeftY() != roomList.get(i).getTopLeftY() || toTest.getTopLeftX() !=roomList.get(i).getTopLeftX()) 
					&& !toTest.isConnected(roomList.get(i)))
			{
				if(closest == null)
				{
					closest = roomList.get(i);
					distance = toTest.getDistance(roomList.get(i));
					index = i;
				}
				
				if(toTest.getDistance(roomList.get(i)) < distance)
				{
					closest = roomList.get(i);
					distance = toTest.getDistance(roomList.get(i));
					index = i;
				}
						
			}	
		}
		System.out.println(index);
		roomList.get(index).addConnected(toTest);
		toTest.addConnected(roomList.get(index));
		return closest;
	}
	
	public void empty(Room a)
	{
		for(int i = 1; i < a.width; i++)
		{
			for(int j = 1; j < a.height; j++)
			{
				level[a.getTopLeftX() + i][a.getTopLeftY() + j] = ' ';
			}
		}
	}
	
	public void generateTendons()
	{
		for(int i = 0; i < roomList.size(); i++)
		{
			Tendon k = (roomList.get(i).getTendon(findClosest(roomList.get(i))));
			System.out.println(k.toString());
			applyTendon(k);
		}	
		
	}
	
	public void text(String input)
	{
		//I'm going to add more arguments as they become meaningful
		switch(input)
		{
		case("g"):
		{
			kill();
			doEverything();
			ask();
		}
		case("generate"):
		{
			kill();
			doEverything();
			ask();
		}
		case("no tendons"):
		{
			kill();
			this.initiate();
			this.generateRooms(1, 8);
			this.renderFloor();
			ask();
		}
		case("size"):
		{
			Scanner arg = new Scanner(System.in);
			System.out.println("Please input your intended x and y values - in that order");
			int x = Integer.parseInt(arg.nextLine());
			int y = Integer.parseInt(arg.nextLine());
			
			changeWorldSize(x,y);
			System.out.println("The level is now generated at " + x + " units by " + y + " units");
			ask();
		}
		case("help"):
		{
			System.out.println("The following commands are avalible:");
					System.out.println("generate    - creates a new level");
					System.out.println("no tendons  - creates a new level sans tendons");
					System.out.println("size        - changes world size");
					System.out.println("num rooms   - changes number of rooms (note that this tops out around 12)");
					System.out.println("help        - this menu");
			ask();
		}
		case("num rooms"):
		{
			Scanner arg = new Scanner(System.in);
			System.out.println("Please input your intended value.");
			int x = Integer.parseInt(arg.nextLine());
			
			this.changeNumRooms(x);
			
			ask();
		}
		default:
		{
			System.out.println("Unrecognized argument.");
			ask();
		}
		}
	}
	
	public void doEverything()
	{
		this.initiate();
		this.generateRooms(1, 8);
		//this.generateRooms(3, 3);
		this.generateTendons();
		//this.reapplyRooms();
		this.renderFloor();
	}

	private void reapplyRooms() {
		ArrayList<Room> hold =roomList;
		roomList.clear();
		for(Room a:hold)
		{
			applyRoom(a);
			roomList.add(a);
		}
	}
	
	public void ask()
	{
		System.out.print(">");
		String input = null;
		Scanner arg = new Scanner(System.in);
		
		input = arg.nextLine();
		text(input);
	}
	
	private void kill()
	{
		roomList = new ArrayList<Room>();
	}
	
	private void changeWorldSize(int x, int y)
	{
		this.width = x;
		this.height = y;
	}
	
	private void changeNumRooms(int a)
	{
		this.numRooms = a;
	}
}


























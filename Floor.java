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
		//generates a room of a specific size
		//note that size 3 will break the game because it doesn't have space for a viable hallway
		
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
		//checks if a room hits the edges of the screen
		
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
		//checks if a given room conflicts with any other room in the list
		for(int i = roomList.size()-1; i>=0; i--)
		{
			if(a.conflictsWith(roomList.get(i), buffer))
				return true;
		}
		return false;
	}
	
	public coord findGoodSpot(Room room, int buffer)
	{
		//checks 100 different spots for a given room size
		//a good spot isn't within buffer of another room at all
		
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
	
	public void generateRooms(int a)
	{
		//generates a set of room of a given
		for(int i = numRooms; i > 0; i--)
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
		generateRooms(1);
	}
	
	public void applyTendon(Tendon t)
	{
		//takes a tendon and applies the coords in the projection
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
		//finds the closest room when given a room
		
		Room closest = null;
		int index = -1; //this breaks it if it doesn't find what it wants
		double distance = 0;
		for(int i = 0; i < roomList.size(); i++)
		{
			Room a = roomList.get(i);
			if((toTest.getTopLeftY() != a.getTopLeftY() || toTest.getTopLeftX() !=a.getTopLeftX()) 
					&& !toTest.isConnected(a))
			{
				if(closest == null && !a.isConnected(toTest))
				{
					closest = a;
					distance = toTest.getDistance(a);
					index = i;
				}
				
				if(toTest.getDistance(a) < distance && !a.isConnected(toTest))
				{
					closest = roomList.get(i);
					distance = toTest.getDistance(a);
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
		//empties out a given room
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
		//generates a set of tendons for every room
		for(int i = 0; i < roomList.size(); i++)
		{
			Tendon k = (roomList.get(i).getTendon(findClosest(roomList.get(i))));
			System.out.println(k.toString());
			applyTendon(k);
		}	
		
	}
	
	public void text(String input)
	{
		//takes a user input and does whatever with it - used through ask()
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
			this.generateRooms(1);
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
		//generates everything - rooms and tendons
		this.initiate();
		this.generateRooms(1);
		this.generateTendons();
		//this.generateTendons();
		this.renderFloor();
	}

	public void ask()
	{
		//asks the user for an argument - takes in text and sends it to the text() method
		
		System.out.print(">");
		String input = null;
		Scanner arg = new Scanner(System.in);
		
		input = arg.nextLine();
		text(input);
	}
	
	private void kill()
	{
		//empties out the room list to add new ones
		roomList = new ArrayList<Room>();
	}
	
	private void changeWorldSize(int x, int y)
	{
		//changes world size
		this.width = x;
		this.height = y;
	}
	
	private void changeNumRooms(int a)
	{
		//changes number of rooms generated
		this.numRooms = a;
	}
}


























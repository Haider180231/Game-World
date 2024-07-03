# Design Document

## Overview

This document outlines the design and implementation details of the GameWorld simulation project. The project simulates a game world with rooms, items, players, and a target character. The goal is to provide a graphical representation of the game world, determine neighboring rooms, display information about rooms, and move the target character through the rooms.

## Class Descriptions

### IRoom

**Interface** for representing a room in the game world.

- **Methods:**
  - `String getName()`: Returns the name of the room.
  - `Tuple<Integer, Integer> getCoordinates()`: Returns the starting coordinates of the room.
  - `Tuple<Integer, Integer> getEndingCoordinates()`: Returns the ending coordinates of the room.
  - `void addItem(IItem item)`: Adds an item to the room.
  - `List<IItem> getItems()`: Returns the list of items in the room.
  - `boolean isNeighbor(Room room)`: Determines if the given room is a neighbor of this room.

### Room

**Class** that implements the IRoom interface.

- **Attributes:**
  - `String name`
  - `Tuple<Integer, Integer> coordinates`
  - `Tuple<Integer, Integer> endingCoordinates`
  - `List<IItem> items`

- **Methods:**
  - `Room(String name, Tuple<Integer, Integer> coordinates, Tuple<Integer, Integer> endingCoordinates)`: Constructor to initialize the room.
  - Implementations of all methods defined in `IRoom`:
    - `String getName()`: Returns the name of the room.
    - `Tuple<Integer, Integer> getCoordinates()`: Returns the starting coordinates of the room.
    - `Tuple<Integer, Integer> getEndingCoordinates()`: Returns the ending coordinates of the room.
    - `void addItem(IItem item)`: Adds an item to the room.
    - `List<IItem> getItems()`: Returns the list of items in the room.
    - `boolean isNeighbor(Room other)`: Determines if the specified room is a neighbor of this room.


### ITarget

**Interface** for representing the target character in the game world.

- **Methods:**
  - `int getHealth()`: Returns the health of the target.
  - `String getName()`: Returns the name of the target.
  - `void moveTarget(Tuple<Integer, Integer> newCoordinates)`: Moves the target to new coordinates.
  - `Tuple<Integer, Integer> getCoordinates()`: Returns the current coordinates of the target.

### Target

**Class** that implements the ITarget interface.

- **Attributes:**
  - `int health`
  - `String name`
  - `Tuple<Integer, Integer> coordinates`

- **Methods:**
  - `Target(int health, String name, Tuple<Integer, Integer> coordinates)`: Constructor to initialize the target.
  - Implementations of all methods defined in `ITarget`:
    - `int getHealth()`: Returns the health of the target.
    - `String getName()`: Returns the name of the target.
    - `void moveTarget(Tuple<Integer, Integer> newCoordinates)`: Moves the target to new coordinates.
    - `Tuple<Integer, Integer> getCoordinates()`: Returns the current coordinates of the target.

### IItem

**Interface** for representing an item in the game world.

- **Methods:**
  - `String getName()`: Returns the name of the item.
  - `int getDamage()`: Returns the damage value of the item.
  - `int getRoomIndex()`: Returns the index of the room the item belongs to.

### Item

**Class** that implements the IItem interface.

- **Attributes:**
  - `String name`
  - `int damage`
  - `int roomIndex`

- **Methods:**
  - `Item(String name, int damage, int roomIndex)`: Constructor to initialize the item.
  - Implementations of all methods defined in `IItem`:
    - `String getName()`: Returns the name of the item.
    - `int getDamage()`: Returns the damage value of the item.
    - `int getRoomIndex()`: Returns the index of the room the item belongs to.


### GameWorld

**Class** representing the game world containing rooms and a target.

- **Attributes:**
  - `int rows`
  - `int columns`
  - `String name`
  - `ITarget target`
  - `List<IRoom> rooms`

- **Methods:**
  - `GameWorld(int rows, int columns, String name, ITarget target, List<IRoom> rooms)`: Constructor to initialize the game world.
  - `void addRoom(IRoom room)`: Adds a room to the game world.
  - `IRoom getRoomByIndex(int index)`: Retrieves a room by its index.
  - `List<IRoom> getRooms()`: Retrieves the list of rooms in the game world.
  - `ITarget getTarget()`: Retrieves the target in the game world.
  - `List<IRoom> getNeighbors(IRoom room)`: Retrieves the neighboring rooms of a specified room.
  - `void displayRoomInfo(int index)`: Displays information about a room specified by its index.
  - `void moveTarget()`: Moves the target to each room in the game world sequentially.
  - `void displayMap(String imageOutputPath)`: Displays a map of the game world and saves it as an image.

### IPlayer

**Interface** for representing a player in the game world.

- **Methods:**
  - `String getName()`: Returns the name of the player.
  - `Tuple<Integer, Integer> getCoordinates()`: Returns the current coordinates of the player.
  - `void move(Tuple<Integer, Integer> newCoordinates)`: Moves the player to new coordinates.
  - `List<IItem> getItems()`: Returns the list of items the player is carrying.
  - `void addItem(IItem item)`: Adds an item to the player's inventory.
  - `int getMaxItems()`: Returns the maximum number of items the player can carry.

### Player

**Class** that implements the IPlayer interface.

- **Attributes:**
  - `String name`
  - `Tuple<Integer, Integer> coordinates`
  - `List<IItem> items`
  - `int maxItems`

- **Methods:**
  - `Player(String name, Tuple<Integer, Integer> coordinates, int maxItems)`: Constructor to initialize the player.
  - Implementations of all methods defined in `IPlayer`:
    - `String getName()`: Returns the name of the player.
    - `Tuple<Integer, Integer> getCoordinates()`: Returns the current coordinates of the player.
    - `void move(Tuple<Integer, Integer> newCoordinates)`: Moves the player to new coordinates.
    - `List<IItem> getItems()`: Returns the list of items the player is carrying.
    - `void addItem(IItem item)`: Adds an item to the player's inventory.
    - `int getMaxItems()`: Returns the maximum number of items the player can carry.

### ComputerPlayer

**Class** that implements the IPlayer interface and represents a computer-controlled player.

- **Attributes:**
  - `String name`
  - `Tuple<Integer, Integer> coordinates`
  - `List<IItem> items`
  - `int maxItems`

- **Methods:**
  - `ComputerPlayer(String name, Tuple<Integer, Integer> coordinates, int maxItems)`: Constructor to initialize the computer player.
  - `void takeTurn(Igameworld world)`: Executes a turn for the computer player, choosing randomly between moving, picking up an item, or looking around.
  - Implementations of all methods defined in `IPlayer`:
    - `String getName()`: Returns the name of the player.
    - `Tuple<Integer, Integer> getCoordinates()`: Returns the current coordinates of the player.
    - `void move(Tuple<Integer, Integer> newCoordinates)`: Moves the player to new coordinates.
    - `List<IItem> getItems()`: Returns the list of items the player is carrying.
    - `void addItem(IItem item)`: Adds an item to the player's inventory.
    - `int getMaxItems()`: Returns the maximum number of items the player can carry.

### GameWorld

**Class** representing the game world containing rooms, players, and a target.

- **Attributes:**
  - `int rows`
  - `int columns`
  - `String name`
  - `ITarget target`
  - `List<IRoom> rooms`
  - `List<IPlayer> players`

- **Methods:**
  - `GameWorld(int rows, int columns, String name, ITarget target, List<IRoom> rooms)`: Constructor to initialize the game world.
  - `void addRoom(IRoom room)`: Adds a room to the game world.
  - `IRoom getRoomByIndex(int index)`: Retrieves a room by its index.
  - `List<IRoom> getRooms()`: Retrieves the list of rooms in the game world.
  - `ITarget getTarget()`: Retrieves the target in the game world.
  - `List<IRoom> getNeighbors(IRoom room)`: Retrieves the neighboring rooms of a specified room.
  - `void displayRoomInfo(int index)`: Displays information about a room specified by its index.
  - `void moveTarget()`: Moves the target to each room in the game world sequentially.
  - `void addPlayer(IPlayer player)`: Adds a player to the game world.
  - `List<IPlayer> getPlayers()`: Retrieves the list of players in the game world.
  - `void movePlayer(IPlayer player, Tuple<Integer, Integer> newCoordinates)`: Moves a player to new coordinates.
  - `IRoom getRoomByCoordinates(Tuple<Integer, Integer> coordinates)`: Retrieves a room by its coordinates.

### GameController

**Class** that controls the game logic and flow.

- **Attributes:**
  - `Igameworld world`
  - `GameView view`
  - `int maxTurns`
  - `int currentTurn`
  - `FileWriter logWriter`
  - `List<IPlayer> players`
  - `int currentPlayerIndex`

- **Methods:**
  - `GameController(Igameworld world, GameView view, int maxTurns, String logFilePath)`: Constructor to initialize the game controller.
  - `void startGame()`: Starts the game and handles the game loop.
  - `void movePlayer(IPlayer player, Scanner scanner)`: Moves the player to a new room based on user input.
  - `void pickItem(IPlayer player)`: Allows the player to pick up an item in the current room.
  - `void lookAround(IPlayer player)`: Allows the player to look around the current room and see items, neighboring rooms, other players, and the target.
  - `void displayMap()`: Displays the game map and saves it to a file.
  - `void displayPlayer(IPlayer player)`: Displays the information of the player.
  - `void displayRoom(Scanner scanner)`: Displays information about a room specified by its index.
  - `IRoom findRoomByCoordinates(Tuple<Integer, Integer> coordinates)`: Finds a room by its coordinates.
  - `void log(String message)`: Logs messages to the log file.
  - `void closeResources(Scanner scanner)`: Closes the log writer and scanner resources.

### GameView

**Class** providing a graphical representation of the game world.

- **Methods:**
  - `void displayMap(Igameworld world, String imageOutputPath)`: Displays the game world map and saves it to an image file.

### Main

**Class** to run the game world simulation.

- **Methods:**
  - `void main(String[] args)`: The main method to run the game world simulation.

### MansionParser

**Class** for parsing a mansion description file and creating a game world.

- **Methods:**
  - `static Igameworld parseMansion(String filePath)`: Parses the mansion description file and creates a game world.
  - `static int parseInteger(String value, String errorMessage)`: Parses a string value as an integer, throwing an IOException with a custom error message if parsing fails.

### Tuple<X, Y>

**Class** representing a generic tuple to hold a pair of values.

- **Attributes:**
  - `X first`
  - `Y second`

- **Methods:**
  - `Tuple(X first, Y second)`: Constructor to initialize the tuple.
  - `X getFirst()`: Retrieves the first value of the tuple.
  - `Y getSecond()`: Retrieves the second value of the tuple.
  - `boolean equals(Object o)`: Checks if two tuples are equal.
  - `int hashCode()`: Generates the hash code for the tuple.
  - `String toString()`: Converts the tuple to a string representation.


## Assumptions

1. Each room is defined by its starting and ending coordinates.
2. The target starts at the first room and moves sequentially through the rooms.
3. Items can be added to rooms, and each item has a name and a damage value.
4. Players can be either human or computer-controlled, and can perform various actions within the game.
5. The game world is represented as a grid of rows and columns.
6. Neighboring rooms are determined based on their coordinates.
7. The game has a fixed number of turns, after which it ends.
8. The log file records important game events and actions for debugging and auditing purposes.
9. The game view is responsible for providing a graphical representation of the game world.
10. The game controller manages the game logic, including player actions, item interactions, and target movements.




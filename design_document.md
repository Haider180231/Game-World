# Design Document

## Overview

This document outlines the design and implementation details of the GameWorld simulation project. The project simulates a game world with rooms, items, and a target character. The goal is to provide a graphical representation of the game world, determine neighboring rooms, display information about rooms, and move the target character through the rooms.

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
  - Implementations of all methods defined in `IRoom`.

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
  - Implementations of all methods defined in `ITarget`.

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
  - Implementations of all methods defined in `IItem`.

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

### Main

**Class** to run the game world simulation.

- **Methods:**
  - `void main(String[] args)`: The main method to run the game world simulation.

## Assumptions

1. Each room is defined by its starting and ending coordinates.
2. The target starts at the first room and moves sequentially through the rooms.
3. Items can be added to rooms, and each item has a name and a damage value.

## Limitations

1. The current design assumes a fixed grid size for the game world.
2. The target movement is sequential and does not consider any pathfinding logic.

## Future Enhancements

1. Implement pathfinding algorithms for target movement.
2. Add more complex interactions between items and the target.
3. Introduce different types of rooms with unique properties.



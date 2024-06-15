# GameWorld Project

## Overview

The GameWorld Project is a Java application that simulates a game world consisting of multiple rooms and a target character. The project includes functionalities to parse a world description from a text file, visualize the world as a graphical map, and move the target character through the rooms.

## Features

- Parse a game world from a specified text file.
- Display detailed information about rooms and items within them.
- Determine and display neighboring rooms.
- Move a target character through the game world in a specified order.
- Generate and save a graphical representation of the game world.

## Requirements

- Java 8 or later
- Eclipse IDE (optional but recommended)

## Installation

### 1. Clone the Repository

Clone the repository to your local machine using the following command:


git clone https://github.com/Haider180231/Game-World.git

### 2. Set Up the Project in Eclipse

1. Open Eclipse IDE.
2. Select `File` > `Import`.
3. Choose `General` > `Existing Projects into Workspace` and click `Next`.
4. Browse to the location of the cloned repository and select it.
5. Click `Finish`.

## Usage

### 1. Running the Application

You can run the application by executing the `Main` class. This will parse the `mansion.txt` file, display room information, generate a graphical map, and move the target character.


### 2. Generating the World Map

The `displayMap` method in the `GameWorld` class generates a graphical map of the game world and saves it to the specified path.

## Testing

### 1. Running Tests

JUnit tests are provided to ensure the correctness of the implementation. You can run the tests using Eclipse's built-in JUnit runner or any other JUnit-compatible test runner.

### 2. Test Classes

Test classes are located in the `test/gameworld` directory and include:

- `GameWorldTest.java`
- `RoomTest.java`
- `TargetTest.java`
- `ItemTest.java`


## Example Run

The following section describes the content of an example run of the program. The output is saved in a `run_log.txt` file in the `res/` directory. This file demonstrates various functionalities of the program, including displaying room information, generating the world map, and moving the target character.

### Run Log

```plaintext
Displaying information of the first room:
=======================================
Room Name: Carriage House
Items in the Room:
 - Chain Saw (Damage: 4)
 - Big Red Hammer (Damage: 4)
Visible Rooms:
 - Winter Garden

Testing getNeighbors method:
============================
Neighbors of Carriage House:
Winter Garden

Moving the target character:
============================
Target moved to: Armory
Target moved to: Billiard Room
Target moved to: Carriage House
Target moved to: Dining Hall
Target moved to: Drawing Room
Target moved to: Foyer
Target moved to: Green House
Target moved to: Hedge Maze
Target moved to: Kitchen
Target moved to: Lancaster Room
Target moved to: Library
Target moved to: Lilac Room
Target moved to: Master Suite
Target moved to: Nursery
Target moved to: Parlor
Target moved to: Piazza
Target moved to: Servants' Quarters
Target moved to: Tennessee Room
Target moved to: Trophy Room
Target moved to: Wine Cellar
Target moved to: Winter Garden
```
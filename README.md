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
Player John added at room Hedge Maze.
Player computer1 added at room Green House.
Player Haidong added at room Trophy Room.
Turn 0: John's turn.
Action: move
Player John moved to room Piazza.
Action completed

Target moved.

Turn 1: computer1's turn.
Computer player took its turn.

Target moved.

Turn 2: Haidong's turn.
Action: pick item
Player Haidong picked up item Duck Decoy.
Action completed

Target moved.

Turn 3: John's turn.
Action: look around
Player John is in: Piazza.
Items in the room:
 - Civil War Cannon
Visible rooms:
 - Foyer
 - Hedge Maze
 - Winter Garden
Players in the room:
 - No other players
Target in the room:
 - No target
Action completed

Target moved.

Turn 4: computer1's turn.
Computer player took its turn.

Target moved.

Turn 5: Haidong's turn.
Action: display player
Player Name: Haidong
Coordinates: 10, 21
Items:
 - Duck Decoy (Damage: 3)
Action completed

Target moved.

Turn 6: John's turn.
Action: display room
Action completed

Target moved.

Turn 7: computer1's turn.
Computer player took its turn.

Target moved.

Turn 8: Haidong's turn.
Action: display map
Map displayed and saved to res/world20240702212300.png.
Action completed

Target moved.

Turn 9: John's turn.
Action: look around
Player John is in: Piazza.
Items in the room:
 - Civil War Cannon
Visible rooms:
 - Foyer
 - Hedge Maze
 - Winter Garden
Players in the room:
 - No other players
Target in the room:
 - No target
Action completed

Target moved.

Game over. Maximum turns reached.
```

## Running the JAR File

### 1. Run the JAR File

To run the JAR file, use the following command:

```
java -jar game-world.jar
```

Make sure that the res directory is in the same directory as the JAR file so that the program can find the necessary resources (e.g., mansion.txt).

## Using the JAR File

The JAR file provides several functionalities that you can use to interact with the game world. Here is a list of commands you can use:

**Add a Human-Controlled Player**
   - **Command**: `add player`
   - **Description**: Adds a human-controlled player to the game world. You will be prompted to enter the player's name and initial coordinates.
   
**Move a Player**
   - **Command**: `move player`
   - **Description**: Moves a player to a neighboring room. You will be prompted to enter the player's name and the new coordinates.
   
**Pick Up an Item**
   - **Command**: `pick item`
   - **Description**: Allows a player to pick up an item in the current room. You will be prompted to enter the player's name.
   
**Look Around**
   - **Command**: `look around`
   - **Description**: Displays information about the current room and its neighboring rooms. You will be prompted to enter the player's name.
   
**Display the Map**
   - **Command**: `display map`
   - **Description**: Generates and saves a graphical representation of the game world map to a file. You will be prompted to enter the output file path.
   
**Display Player Information**
   - **Command**: `display player`
   - **Description**: Displays the information of a specific player, including their current coordinates and the items they are carrying.
   
**Exit the Game**
   - **Command**: `exit`
   - **Description**: Exits the game.


##The example run demonstrates the following functionalities of the game:

1. Adding a human-controlled player to the game world.
2. Adding a computer-controlled player to the game world.
3. The player moving around the world.
4. The player picking up an item.
5. The player looking around to see neighboring rooms and items.
6. Taking turns between multiple players.
7. Displaying the description of a specific player, including their items and current location.
8. Displaying information about a specific space in the world, including items and neighboring rooms.
9. Creating and saving a graphical representation of the world map to the current directory.
10. Automatically moving the target character around the world after each turn.
11. Demonstrating the game ending after reaching the maximum number of turns.





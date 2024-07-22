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
- `ComputerPlayerTest.java`
- `GameControllerTest.java`
- `PlaerTest.java`
- `PetTest.java`


## Example Run

The following section describes the content of an example run of the program. The output is saved in a `run_log.txt` file in the `res/` directory. This file demonstrates various functionalities of the program, including displaying room information, generating the world map, and moving the target character.

### Run Log

```
Player haidong added at room 0.
Pet placed in room 0.
Turn 0: haidong's turn.
Player haidong is at coordinates: 22, 19
Action: look around
Player looked around
Target moved.

Turn 1: haidong's turn.
Player haidong is at coordinates: 22, 19
Action: move
Player haidong moved to room Billiard Room.
Action completed

Target moved.

Turn 2: haidong's turn.
Player haidong is at coordinates: 16, 21
Action: look around
Player looked around
Target moved.

Turn 3: haidong's turn.
Player haidong is at coordinates: 16, 21
Action: move pet
Pet moved to room Carriage House.
Action completed

Target moved.

Turn 4: haidong's turn.
Player haidong is at coordinates: 16, 21
Action: move
Player haidong moved to room Armory.
Action completed

Target moved.

Turn 5: haidong's turn.
Player haidong is at coordinates: 22, 19
Action: pick item
Player haidong picked up item Revolver.
Action completed

Target moved.

Turn 6: haidong's turn.
Player haidong is at coordinates: 22, 19
Action: attack
Player haidong attacked Static Target with 3 damage.
Action completed

Target moved.

Turn 7: haidong's turn.
Player haidong is at coordinates: 22, 19
Action: attack
Player haidong attacked Static Target with 1 damage.
Action completed

Target moved.

Turn 8: haidong's turn.
Player haidong is at coordinates: 22, 19
Action: attack
Player haidong attacked Static Target with 1 damage.
Target Static Target has been defeated!
Action completed

Target moved.

Target Static Target has been defeated!
Player c1 added at room 0.
Pet placed in room 0.
Turn 0: c1's turn.
Player c1 is at coordinates: 22, 19
Computer player took its turn.

Target moved.

Turn 1: c1's turn.
Player c1 is at coordinates: 22, 19
Computer player took its turn.

Target moved.

Turn 2: c1's turn.
Player c1 is at coordinates: 22, 19
Computer player took its turn.

Target moved.

Target Static Target has been defeated!
Player haidong added at room 0.
Pet placed in room 0.
Turn 0: haidong's turn.
Player haidong is at coordinates: 22, 19
Action: look around
Player looked around
Target moved.

Turn 1: haidong's turn.
Player haidong is at coordinates: 22, 19
Action: look around
Player looked around
Target moved.

Turn 2: haidong's turn.
Player haidong is at coordinates: 22, 19
Action: look around
Player looked around
Target moved.

Turn 3: haidong's turn.
Player haidong is at coordinates: 22, 19
Action: look around
Player looked around
Target moved.

Turn 4: haidong's turn.
Player haidong is at coordinates: 22, 19
Action: look around
Player looked around
Target moved.

Turn 5: haidong's turn.
Player haidong is at coordinates: 22, 19
Action: look around
Player looked around
Target moved.

Turn 6: haidong's turn.
Player haidong is at coordinates: 22, 19
Action: look around
Player looked around
Target moved.

Turn 7: haidong's turn.
Player haidong is at coordinates: 22, 19
Action: look around
Player looked around
Target moved.

Turn 8: haidong's turn.
Player haidong is at coordinates: 22, 19
Action: look around
Player looked around
Target moved.

Turn 9: haidong's turn.
Player haidong is at coordinates: 22, 19
Action: look around
Player looked around
Target moved.

Game over. Maximum turns reached.

```

## Limitation and Assuming

For testing purposes, I set up a Target that doesn't move to be born in room 0, and then the user and the pet can choose their own birth location when they are born

AI actions are relatively homogenous.


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
   
**Attack the Target**
   - **Command**: `attack`
   - **Description**: Allows a player to attack the target character if they are in the same room. You will be prompted to enter the player's name.

**Display Player Information**
   - **Command**: `display player`
   - **Description**: Displays the information of a specific player, including their current coordinates and the items they are carrying.

**Move the Pet**
   - **Command**: `attack`
   - **Description**: Moves the pet to a new room. You will be prompted to enter the room index.

**Exit the Game**
   - **Command**: `exit`
   - **Description**: Exits the game.








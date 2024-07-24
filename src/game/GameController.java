package game;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Controls the game logic and flow.
 */
public class GameController {
  private Igameworld world;
  private GameView view;
  private int maxTurns;
  private int currentTurn;
  private FileWriter logWriter;
  private List<Iplayer> players;
  private int currentPlayerIndex;
  private boolean gameRunning;
  private Scanner scanner; 

  /**
   * Constructs a new GameController with the specified parameters.
   *
   * @param world      the game world
   * @param view       the game view
   * @param maxTurns   the maximum number of turns
   * @param logFilePath the path to the log file
   */
  public GameController(Igameworld world, GameView view, int maxTurns, String logFilePath) {
    this.world = world;
    this.view = view;
    this.maxTurns = maxTurns;
    this.currentTurn = 0;
    this.players = new ArrayList<>();
    this.currentPlayerIndex = 0;
    this.gameRunning = true;
    this.scanner = new Scanner(System.in); // Initialize the Scanner instance
    try {
      this.logWriter = new FileWriter(logFilePath, true);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Starts the game loop, setting up the game, running it, and ending it.
   */
  public void startGame() {
    while (gameRunning) {
      setupGame();
      runGame();
      endGame();
    }
    scanner.close(); // Close the Scanner instance when the game ends
  }

  /**
   * Sets up the game by clearing previous state and adding players.
   */
  private void setupGame() {
    players.clear();
    currentTurn = 0;
    currentPlayerIndex = 0;

    while (true) {
      System.out.println("Do you want to add a player? (yes/no)");
      String response = scanner.nextLine().trim().toLowerCase();
      if (!"yes".equals(response)) {
        break;
      }
      System.out.println("Enter player type (human/computer):");
      String playerType;
      while (true) {
        playerType = scanner.nextLine().trim().toLowerCase();
        if ("human".equals(playerType) || "computer".equals(playerType)) {
          break;
        } else {
          System.out.println("Invalid player type. Please enter 'human' or 'computer':");
        }
      }
      System.out.println("Enter player name:");
      final String playerName = scanner.nextLine().trim();

      System.out.println("Choose a room number for the player ("
          + "0 to " + (world.getRooms().size() - 1) + "):");
      int roomIndex = scanner.nextInt();
      scanner.nextLine(); // consume the newline

      if (roomIndex < 0 || roomIndex >= world.getRooms().size()) {
        System.out.println("Invalid room index. Assigning to a random room.");
        roomIndex = new Random().nextInt(world.getRooms().size());
      }

      Iroom initialRoom = world.getRoomByIndex(roomIndex);
      Iplayer player;
      if ("computer".equals(playerType)) {
        player = new ComputerPlayer(playerName, initialRoom.getCoordinates(), 5);
      } else {
        player = new Player(playerName, initialRoom.getCoordinates(), 5);
      }
      world.addPlayer(player);
      players.add(player);
      log("Player " + playerName + " added at room " + roomIndex + ".\n");
    }

    System.out.println("Choose a room number to place the pet ("
        + "0 to " + (world.getRooms().size() - 1) + "):");
    int petRoomIndex = scanner.nextInt();
    scanner.nextLine(); // consume the newline

    if (petRoomIndex < 0 || petRoomIndex >= world.getRooms().size()) {
      System.out.println("Invalid room index. Assigning to a random room.");
      petRoomIndex = new Random().nextInt(world.getRooms().size());
    }

    Iroom petRoom = world.getRoomByIndex(petRoomIndex);
    world.movePet(petRoom.getCoordinates());
    log("Pet placed in room " + petRoomIndex + ".\n");
  }

  /**
   * Runs the game loop, executing player turns and updating the game state.
   */
  public void runGame() {
    while (currentTurn < maxTurns && gameRunning) {
      Iplayer currentPlayer = players.get(currentPlayerIndex);
      System.out.println("It's " + currentPlayer.getName() + "'s turn.");
      log("Turn " + currentTurn + ": " + currentPlayer.getName() + "'s turn.\n");

      // Display limited information about the player's position in the world
      Tuple<Integer, Integer> playerCoordinates = currentPlayer.getCoordinates();
      System.out.println("You are at coordinates: " + playerCoordinates.getFirst() + ""
          + ", " + playerCoordinates.getSecond());
      log("Player " + currentPlayer.getName() + ""
          + " is at coordinates: " + playerCoordinates.getFirst() + ""
          + ", " + playerCoordinates.getSecond() + "\n");

      if (currentPlayer instanceof ComputerPlayer) {
        System.out.println("Computer player is taking its turn...");
        ((ComputerPlayer) currentPlayer).takeTurn(world);
        log("Computer player took its turn.\n\n");
        System.out.println("Action completed\n");
      } else {
        System.out.println("Choose an action: move, pick item, look around, "
            + "attack, display map, display player, display room, move pet, exit");
        String action = scanner.nextLine().trim().toLowerCase();
        log("Action: " + action + "\n");

        switch (action) {
          case "move":
            movePlayer(currentPlayer, scanner);
            System.out.println("Action completed\n");
            log("Action completed\n\n");
            break;
          case "pick item":
            pickItem(currentPlayer);
            System.out.println("Action completed\n");
            log("Action completed\n\n");
            break;
          case "look around":
            System.out.println(currentPlayer.lookAround(world));
            log("Player looked around\n");
            break;
          case "attack":
            attackTarget(currentPlayer);
            System.out.println("Action completed\n");
            log("Action completed\n\n");
            break;
          case "display map":
            displayMap();
            System.out.println("Action completed\n");
            log("Action completed\n\n");
            break;
          case "display player":
            displayPlayer(currentPlayer);
            System.out.println("Action completed\n");
            log("Action completed\n\n");
            break;
          case "display room":
            displayRoom(scanner);
            System.out.println("Action completed\n");
            log("Action completed\n\n");
            break;
          case "move pet":
            movePet(scanner);
            System.out.println("Action completed\n");
            log("Action completed\n\n");
            break;
          case "exit":
            gameRunning = false;
            return;
          default:
            System.out.println("Invalid action.");
            log("Invalid action.\n\n");
            break;
        }
      }

      world.moveTarget();
      log("Target moved.\n\n");

      if (world.getTarget().getHealth() <= 0) {
        System.out.println("Target " + world.getTarget().getName() + " has been defeated!");
        log("Target " + world.getTarget().getName() + " has been defeated!\n");
        gameRunning = false;
        return;
      }

      currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
      world.nextTurn();
      currentTurn++;
    }

    if (currentTurn >= maxTurns) {
      System.out.println("Game over! Maximum number of turns reached.");
      log("Game over. Maximum turns reached.\n");
      gameRunning = false;
    }
  }

  /**
   * Ends the game, providing an option to save the game log.
   */
  private void endGame() {
    System.out.println("Do you want to save the game log? (yes/no)");
    if (scanner.hasNextLine()) {
      String response = scanner.nextLine().trim().toLowerCase();
      if ("yes".equals(response)) {
        try {
          logWriter.close();
          System.out.println("Game log saved successfully.");
        } catch (IOException e) {
          e.printStackTrace();
        }
      } else {
        System.out.println("Game log not saved.");
      }
    } else {
      System.out.println("No input available to save the game log.");
    }
  }

  /**
   * Logs a message to the log file.
   *
   * @param message the message to log
   */
  private void log(String message) {
    try {
      logWriter.write(message);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Closes the resources used by the game controller.
   */
  private void closeResources() {
    try {
      logWriter.close();
      scanner.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Moves the player to a new room.
   *
   * @param player   the player to move
   * @param scanner  the scanner for user input
   */
  public void movePlayer(Iplayer player, Scanner scanner) {
    Iroom currentRoom = findRoomByCoordinates(player.getCoordinates());
    List<Iroom> neighbors = world.getNeighbors(currentRoom);
    System.out.println("Enter the room number to move to:");
    for (int i = 0; i < neighbors.size(); i++) {
      System.out.println(i + ": " + neighbors.get(i).getName());
    }
    int roomIndex = scanner.nextInt();
    scanner.nextLine(); // consume the newline
    if (roomIndex >= 0 && roomIndex < neighbors.size()) {
      Iroom newRoom = neighbors.get(roomIndex);
      world.movePlayer(player, newRoom.getCoordinates());
      log("Player " + player.getName() + " moved to room " + newRoom.getName() + ".\n");
    } else {
      System.out.println("Invalid room index.");
      log("Invalid room index for player " + player.getName() + ".\n");
    }
  }

  /**
   * Allows the player to pick an item from the current room.
   *
   * @param player the player picking the item
   */
  public void pickItem(Iplayer player) {
    Iroom room = findRoomByCoordinates(player.getCoordinates());
    if (room != null && !room.getItems().isEmpty()) {
      Iitem item = room.getItems().get(0);
      player.addItem(item);
      room.getItems().remove(item);
      log("Player " + player.getName() + " picked up item " + item.getName() + ".\n");
      System.out.println("Player " + player.getName()
          + " picked up item " + item.getName() + ".\n");
    } else {
      log("No items in the room for player " + player.getName() + " to pick up.\n");
      System.out.println("No items in the room for player " + player.getName() + " to pick up.\n");
    }
  }

  /**
   * Allows the player to attack the target if it is in the same room.
   *
   * @param player the player attacking the target
   */
  private void attackTarget(Iplayer player) {
    Itarget target = world.getTarget();
    if (target != null && target.getCoordinates().equals(player.getCoordinates())) {
      String result = player.attack(target, world);
      log(result + "\n");
      System.out.println(result + "\n");
      if (target.getHealth() <= 0) {
        System.out.println("Target " + target.getName() + " has been defeated!");
        log("Target " + target.getName() + " has been defeated!\n");
        gameRunning = false;
      }
    } else {
      System.out.println("There is no target to attack in this room.");
      log("No target to attack in this room.\n");
    }
  }

  /**
   * Moves the pet to a new room.
   *
   * @param scanner the scanner for user input
   */
  public void movePet(Scanner scanner) {
    Ipet pet = world.getPet();
    if (pet == null) {
      System.out.println("No pet in the game world.");
      log("No pet in the game world.\n");
      return;
    }

    System.out.println("Enter the room number to move the pet to:");
    int roomIndex = scanner.nextInt();
    scanner.nextLine(); // consume the newline
    Iroom room = world.getRoomByIndex(roomIndex);
    if (room != null) {
      world.movePet(room.getCoordinates());
      log("Pet moved to room " + room.getName() + ".\n");
      System.out.println("Pet moved to room " + room.getName() + ".\n");
    } else {
      System.out.println("Invalid room index.");
      log("Invalid room index for moving pet.\n");
    }
  }

  /**
   * Displays the game map and saves it to a file.
   */
  public void displayMap() {
    String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    String path = "res/world" + timeStamp + ".png";
    try {
      view.displayMap(world, path);
      log("Map displayed and saved to " + path + ".\n");
      System.out.println("Map displayed and saved to " + path + ".\n");
    } catch (IOException e) {
      log("Failed to display map: " + e.getMessage() + "\n");
      System.out.println("Failed to display map: " + e.getMessage() + "\n");
    }
  }

  /**
   * Displays information about the player.
   *
   * @param player the player whose information is to be displayed
   */
  public void displayPlayer(Iplayer player) {
    StringBuilder output = new StringBuilder();
    output.append("Player Name: " + player.getName() + "\n");
    output.append("Coordinates: " + player.getCoordinates().getFirst()
        + ", " + player.getCoordinates().getSecond() + "\n");
    output.append("Items:\n");
    for (Iitem item : player.getItems()) {
      output.append(" - " + item.getName() + " (Damage: " + item.getDamage() + ")\n");
    }
    String outputStr = output.toString();
    System.out.print(outputStr);
    log(outputStr);
  }

  /**
   * Displays information about a room.
   *
   * @param scanner the scanner for user input
   */
  public void displayRoom(Scanner scanner) {
    System.out.println("Enter the room index:");
    int roomIndex = scanner.nextInt();
    scanner.nextLine(); // consume the newline
    world.displayRoomInfo(roomIndex);
  }

  /**
   * Finds a room by its coordinates.
   *
   * @param coordinates the coordinates of the room
   * @return the room at the specified coordinates, or null if not found
   */
  private Iroom findRoomByCoordinates(Tuple<Integer, Integer> coordinates) {
    for (Iroom room : world.getRooms()) {
      if (room.getCoordinates().equals(coordinates)) {
        return room;
      }
    }
    return null;
  }
}

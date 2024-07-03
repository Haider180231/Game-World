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

  /**
   * Constructs a new GameController.
   *
   * @param world the game world
   * @param view the game view
   * @param maxTurns the maximum number of turns
   * @param logFilePath the path to the log file
   */
  public GameController(Igameworld world, GameView view, int maxTurns, String logFilePath) {
    this.world = world;
    this.view = view;
    this.maxTurns = maxTurns;
    this.currentTurn = 0;
    this.players = new ArrayList<>();
    this.currentPlayerIndex = 0;
    try {
      this.logWriter = new FileWriter(logFilePath, true);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Starts the game.
   */
  public void startGame() {
    final Scanner scanner = new Scanner(System.in);
    Random random = new Random();

    // Add players
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
      String playerName = scanner.nextLine().trim();
      Iroom initialRoom = world.getRoomByIndex(random.nextInt(world.getRooms().size()));
      Iplayer player;
      if ("computer".equals(playerType)) {
        player = new ComputerPlayer(playerName, initialRoom.getCoordinates(), 5);
      } else {
        player = new Player(playerName, initialRoom.getCoordinates(), 5);
      }
      world.addPlayer(player);
      players.add(player);
      log("Player " + playerName + " added at room " + initialRoom.getName() + ".\n");
    }

    while (currentTurn < maxTurns) {
      Iplayer currentPlayer = players.get(currentPlayerIndex);
      System.out.println("It's " + currentPlayer.getName() + "'s turn.");
      log("Turn " + currentTurn + ": " + currentPlayer.getName() + "'s turn.\n");

      if (currentPlayer instanceof ComputerPlayer) {
        System.out.println("Computer player is taking its turn...");
        ((ComputerPlayer) currentPlayer).takeTurn(world);
        log("Computer player took its turn.\n\n");
        System.out.println("Action completed\n");
      } else {
        System.out.println("Choose an action: move, pick item, look around, "
            + "display map, display player, display room, exit");
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
            lookAround(currentPlayer);
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
          case "exit":
            closeResources(scanner);
            return;
          default:
            System.out.println("Invalid action.");
            log("Invalid action.\n\n");
            break;
        }
      }

      world.moveTarget();
      log("Target moved.\n\n");

      currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
      currentTurn++;
    }
    System.out.println("Game over! Maximum number of turns reached.");
    log("Game over. Maximum turns reached.\n");
    closeResources(scanner);
  }

  private void log(String message) {
    try {
      logWriter.write(message);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void closeResources(Scanner scanner) {
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
   * @param player the player to move
   * @param scanner the scanner to read input
   */
  private void movePlayer(Iplayer player, Scanner scanner) {
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
   * Allows the player to pick up an item in the current room.
   *
   * @param player the player picking up the item
   */
  private void pickItem(Iplayer player) {
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
   * Allows the player to look around the current room and see items, neighboring rooms,
   * other players, and the target.
   *
   * @param player the player looking around
   */
  private void lookAround(Iplayer player) {
    Iroom room = findRoomByCoordinates(player.getCoordinates());
    if (room != null) {
      StringBuilder output = new StringBuilder();
      output.append("Player ").append(player.getName())
      .append(" is in: ").append(room.getName()).append(".\n");

      output.append("Items in the room:\n");
      if (room.getItems().isEmpty()) {
        output.append(" - No items\n");
      } else {
        for (Iitem item : room.getItems()) {
          output.append(" - ").append(item.getName()).append("\n");
        }
      }

      output.append("Visible rooms:\n");
      List<Iroom> neighbors = world.getNeighbors(room);
      if (neighbors.isEmpty()) {
        output.append(" - No visible rooms\n");
      } else {
        for (Iroom neighbor : neighbors) {
          output.append(" - ").append(neighbor.getName()).append("\n");
        }
      }

      output.append("Players in the room:\n");
      boolean playersFound = false;
      for (Iplayer p : world.getPlayers()) {
        if (!p.equals(player) && p.getCoordinates().equals(room.getCoordinates())) {
          output.append(" - ").append(p.getName()).append("\n");
          playersFound = true;
        }
      }
      if (!playersFound) {
        output.append(" - No other players\n");
      }

      output.append("Target in the room:\n");
      if (world.getTarget().getCoordinates().equals(room.getCoordinates())) {
        output.append(" - ").append(world.getTarget().getName()).append("\n");
      } else {
        output.append(" - No target\n");
      }

      String outputStr = output.toString();
      System.out.print(outputStr);
      log(outputStr);
    }
  }

  /**
   * Displays the game map and saves it to a file.
   */
  private void displayMap() {
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
   * Displays the information of the player.
   *
   * @param player the player whose information is to be displayed
   */
  private void displayPlayer(Iplayer player) {
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
   * Displays information about a room by its index.
   *
   * @param scanner the scanner to read input
   */
  private void displayRoom(Scanner scanner) {
    System.out.println("Enter the room index:");
    int roomIndex = scanner.nextInt();
    scanner.nextLine(); // consume the newline
    world.displayRoomInfo(roomIndex);
  }

  /**
   * Finds a room by its coordinates.
   *
   * @param coordinates the coordinates of the room
   * @return the room if found, null otherwise
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

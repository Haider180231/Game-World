package game;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

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
  private boolean testMode = false;

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
    try {
      this.logWriter = new FileWriter(logFilePath, true);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Sets the controller to test mode.
   *
   * @param testMode whether to enable test mode
   */
  public void setTestMode(boolean testMode) {
    this.testMode = testMode;
  }

  /**
   * Starts the game loop, setting up the game, running it, and ending it.
   */
  public void startGame() {
    setupGame();
    runTurn();
  }

  /**
   * Sets up the game by clearing previous state and adding players.
   */
  public void setupGame() {
    if (testMode) {
      Iplayer player1 = new Player("TestPlayer1", world.getRoomByIndex(0).getCoordinates(), 5);
      Iplayer player2 = new Player("TestPlayer2", world.getRoomByIndex(1).getCoordinates(), 5);
      Iplayer computerPlayer = new ComputerPlayer(
          "AI_Player", world.getRoomByIndex(2).getCoordinates(), 5);

      world.addPlayer(player1);
      world.addPlayer(player2);
      world.addPlayer(computerPlayer);

      players.add(player1);
      players.add(player2);
      players.add(computerPlayer);

      world.movePet(world.getRoomByIndex(3).getCoordinates());

      log("Test mode: Two human players and one computer player set without GUI interaction.");
      view.updatePlayerList();
      view.displayGameWorld();
      return;
    }

    players.clear();
    currentTurn = 0;
    currentPlayerIndex = 0;

    while (true) {
      int response = JOptionPane.showConfirmDialog(
          view, "Do you want to add a player?", 
          "Add Player", JOptionPane.YES_NO_OPTION);
      if (response != JOptionPane.YES_OPTION) {
        break;
      }

      String[] playerTypes = {"human", "computer"};
      String playerType = (String) JOptionPane.showInputDialog(
          view, "Enter player type (human/computer):", 
          "Player Type", JOptionPane.QUESTION_MESSAGE, null, playerTypes, playerTypes[0]);
      if (playerType == null || (!"human".equals(playerType) && !"computer".equals(playerType))) {
        JOptionPane.showMessageDialog(
            view, "Invalid player type. Please enter 'human' or 'computer'.");
        continue;
      }

      String playerName = JOptionPane.showInputDialog(view, "Enter player name:");
      if (playerName == null || playerName.trim().isEmpty()) {
        JOptionPane.showMessageDialog(view, "Invalid player name. Please enter a valid name.");
        continue;
      }

      int roomIndex = -1;
      while (roomIndex < 0 || roomIndex >= world.getRooms().size()) {
        String roomIndexStr = JOptionPane.showInputDialog(
            view, "Choose a room number for the player "
                + "(0 to " + (world.getRooms().size() - 1) + "):");
        try {
          roomIndex = Integer.parseInt(roomIndexStr);
        } catch (NumberFormatException e) {
          JOptionPane.showMessageDialog(view, "Invalid room index. Please enter a number.");
        }
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
      view.updatePlayerList();
    }

    int petRoomIndex = -1;
    while (petRoomIndex < 0 || petRoomIndex >= world.getRooms().size()) {
      String petRoomIndexStr = JOptionPane.showInputDialog(
          view, "Choose a room number to place the pet "
              + "(0 to " + (world.getRooms().size() - 1) + "):");
      try {
        petRoomIndex = Integer.parseInt(petRoomIndexStr);
      } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(view, "Invalid room index. Please enter a number.");
      }
    }

    Iroom petRoom = world.getRoomByIndex(petRoomIndex);
    world.movePet(petRoom.getCoordinates());
    log("Pet placed in room " + petRoomIndex + ".\n");

    view.displayGameWorld();
  }

  /**
   * Runs a single turn of the game.
   */
  public void runTurn() {
    if (!gameRunning) {
      endGame();
      return;
    }

    if (currentTurn < maxTurns) {
      Iplayer currentPlayer = players.get(currentPlayerIndex);
      view.appendToConsole("It's " + currentPlayer.getName() + "'s turn.");
      log("Turn " + currentTurn + ": " + currentPlayer.getName() + "'s turn.\n");

      Tuple<Integer, Integer> playerCoordinates = currentPlayer.getCoordinates();
      view.appendToConsole(
          "You are at coordinates: " 
          + playerCoordinates.getFirst() + ", " + playerCoordinates.getSecond());
      log("Player " + currentPlayer.getName() + " is at coordinates: " 
          + playerCoordinates.getFirst() + ", " + playerCoordinates.getSecond() + "\n");

      if (currentPlayer instanceof ComputerPlayer) {
        view.appendToConsole("Computer player is taking its turn...");
        ((ComputerPlayer) currentPlayer).takeTurn(world);
        log("Computer player took its turn.\n\n");
        view.appendToConsole("Action completed\n");
      }
    } else {
      view.appendToConsole("Game over! Maximum number of turns reached.");
      log("Game over. Maximum turns reached.\n");
      gameRunning = false;
      endGame();
    }
  }

  /**
   * Proceeds to the next turn.
   */
  public void nextTurn() {
    world.moveTarget();
    view.appendToConsole("Target moved");
    world.moveDfsPet();
    currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    currentTurn++;
    runTurn();
  }

  /**
   * Ends the game, providing an option to save the game log.
   */
  private void endGame() {
    int response = JOptionPane.showConfirmDialog(
        view, "Do you want to save the game log?", "Save Log", JOptionPane.YES_NO_OPTION);
    if (response == JOptionPane.YES_OPTION) {
      try {
        logWriter.close();
        JOptionPane.showMessageDialog(view, "Game log saved successfully.");
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      JOptionPane.showMessageDialog(view, "Game log not saved.");
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
   * Moves the player to a new room.
   *
   * @param player the player to move
   */
  public void movePlayer(Iplayer player) {
    Iroom currentRoom = findRoomByCoordinates(player.getCoordinates());
    List<Iroom> neighbors = world.getNeighbors(currentRoom);
    String[] neighborNames = new String[neighbors.size()];
    for (int i = 0; i < neighbors.size(); i++) {
      neighborNames[i] = neighbors.get(i).getName();
    }
    String selectedRoomName = (String) JOptionPane.showInputDialog(
        view, "Enter the room number to move to:", "Move Player", 
        JOptionPane.QUESTION_MESSAGE, null, neighborNames, neighborNames[0]);
    for (Iroom neighbor : neighbors) {
      if (neighbor.getName().equals(selectedRoomName)) {
        world.movePlayer(player, neighbor.getCoordinates());
        log("Player " + player.getName() + " moved to room " + neighbor.getName() + ".\n");
        view.appendToConsole(
            "Player " + player.getName() + " moved to room " + neighbor.getName() + ".\n");
        view.displayGameWorld();
        return;
      }
    }
    view.appendToConsole("Invalid room index.");
    log("Invalid room index for player " + player.getName() + ".\n");
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
      view.appendToConsole(
          "Player " + player.getName() + " picked up item " + item.getName() + ".\n");
    } else {
      log("No items in the room for player " + player.getName() + " to pick up.\n");
      view.appendToConsole(
          "No items in the room for player " + player.getName() + " to pick up.\n");
    }
  }

  /**
   * Allows the player to attack the target if it is in the same room.
   *
   * @param player the player attacking the target
   */
  public void attackTarget(Iplayer player) {
    Itarget target = world.getTarget();
    if (target != null && target.getCoordinates().equals(player.getCoordinates())) {
      String result = player.attack(target, world);
      log(result + "\n");
      view.appendToConsole(result + "\n");
      if (target.getHealth() <= 0) {
        view.appendToConsole("Target " + target.getName() + " has been defeated!");
        log("Target " + target.getName() + " has been defeated!\n");
        gameRunning = false;
      }
    } else {
      view.appendToConsole("There is no target to attack in this room.");
      log("No target to attack in this room.\n");
    }
  }

  /**
   * Moves the pet to a new room.
   */
  public void movePet() {
    Ipet pet = world.getPet();
    if (pet == null) {
      view.appendToConsole("No pet in the game world.");
      log("No pet in the game world.\n");
      return;
    }

    String[] roomNames = new String[world.getRooms().size()];
    for (int i = 0; i < world.getRooms().size(); i++) {
      roomNames[i] = world.getRooms().get(i).getName();
    }

    String selectedRoomName = (String) JOptionPane.showInputDialog(
        view, "Enter the room number to move the pet to:", "Move Pet", 
        JOptionPane.QUESTION_MESSAGE, null, roomNames, roomNames[0]);
    for (Iroom room : world.getRooms()) {
      if (room.getName().equals(selectedRoomName)) {
        world.movePet(room.getCoordinates());
        log("Pet moved to room " + room.getName() + ".\n");
        view.appendToConsole("Pet moved to room " + room.getName() + ".\n");
        view.displayGameWorld();
        return;
      }
    }
    view.appendToConsole("Invalid room index.");
    log("Invalid room index for moving pet.\n");
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
      view.appendToConsole("Map displayed and saved to " + path + ".\n");
      view.displayGameWorld();
    } catch (IOException e) {
      log("Failed to display map: " + e.getMessage() + "\n");
      view.appendToConsole("Failed to display map: " + e.getMessage() + "\n");
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
    output.append("Coordinates: " 
        + player.getCoordinates().getFirst() + ", " + player.getCoordinates().getSecond() + "\n");
    output.append("Items:\n");
    for (Iitem item : player.getItems()) {
      output.append(" - " + item.getName() + " (Damage: " + item.getDamage() + ")\n");
    }
    String outputStr = output.toString();
    view.appendToConsole(outputStr);
    log(outputStr);
  }

  /**
   * Displays information about a room.
   */
  public void displayRoom() {
    String[] roomNames = new String[world.getRooms().size()];
    for (int i = 0; i < world.getRooms().size(); i++) {
      roomNames[i] = world.getRooms().get(i).getName();
    }

    String selectedRoomName = (String) JOptionPane.showInputDialog(
        view, "Enter the room index:", "Display Room", 
        JOptionPane.QUESTION_MESSAGE, null, roomNames, roomNames[0]);
    for (Iroom room : world.getRooms()) {
      if (room.getName().equals(selectedRoomName)) {
        StringBuilder output = new StringBuilder();
        output.append("Room Name: ").append(room.getName()).append("\n");
        output.append("Items in the Room:\n");
        for (Iitem item : room.getItems()) {
          output.append(" - ").append(item.getName()).append(
              " (Damage: ").append(item.getDamage()).append(")\n");
        }
        output.append("Visible Rooms:\n");
        for (Iroom visibleRoom : world.getNeighbors(room)) {
          output.append(" - ").append(visibleRoom.getName()).append("\n");
        }
        output.append("Players in the Room:\n");
        for (Iplayer player : world.getPlayers()) {
          if (player.getCoordinates().equals(room.getCoordinates())) {
            output.append(" - ").append(player.getName()).append("\n");
          }
        }
        if (world.getPet() != null 
            && world.getPet().getCoordinates().equals(room.getCoordinates())) {
          output.append("Pet: ").append(world.getPet().getName()).append(" is here.\n");
        }

        view.appendToConsole(output.toString());
        log(output.toString());
        return;
      }
    }
    view.appendToConsole("Invalid room index.\n");
    log("Invalid room index.\n");
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

  /**
   * Retrieves the current player based on the current player index.
   *
   * @return the current player
   */
  public Iplayer getCurrentPlayer() {
    return players.get(currentPlayerIndex);
  }

  /**
   * Adds a player to the controller's list of players.
   *
   * @param player the player to add
   */
  public void addPlayerToController(Iplayer player) {
    players.add(player);
  }
}

package game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;


/**
 * This class represents the view component of the game, responsible for 
 * displaying the game world and handling user interactions.
 */
public class GameView extends JFrame {
  private Igameworld world;
  private JTextArea consoleArea;
  private JPanel playerPanel;
  private JPanel gamePanel;
  private GameController controller;
  private Map<Rectangle, Iplayer> playerIconMap = new HashMap<>();
  private Map<Rectangle, Iroom> roomMap = new HashMap<>();
  private boolean testMode = false;
  private Queue<String> testInputs = new LinkedList<>();

  /**
   * Constructs a new GameView.
   * 
   * @param world          the game world.
   * @param imageOutputPath the path to output images.
   * @param controller      the game controller.
   */
  public GameView(Igameworld world, String imageOutputPath, GameController controller) {
    this.world = world;
    this.controller = controller;

    setTitle("Game View");
    setSize(800, 600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    JMenuBar menuBar = new JMenuBar();
    JMenu gameMenu = new JMenu("Game");
    JMenuItem newGameItem = new JMenuItem("New Game");
    JMenuItem quitGameItem = new JMenuItem("Quit Game");

    gameMenu.add(newGameItem);
    gameMenu.add(quitGameItem);
    menuBar.add(gameMenu);

    setJMenuBar(menuBar);

    newGameItem.addActionListener(e -> startNewGame());

    quitGameItem.addActionListener(e -> System.exit(0));

    consoleArea = new JTextArea(10, 20);
    consoleArea.setEditable(false);
    final JScrollPane consoleScrollPane = new JScrollPane(consoleArea);
    consoleScrollPane.setBorder(BorderFactory.createTitledBorder("Console"));

    playerPanel = new JPanel();
    playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.Y_AXIS));
    playerPanel.setBorder(BorderFactory.createTitledBorder("Players"));
    final JScrollPane playerScrollPane = new JScrollPane(playerPanel);

    JPanel controlPanel = new JPanel();
    controlPanel.setLayout(new GridLayout(2, 5));
    String[] buttonLabels = { "Move", "Pick Item", 
        "Look Around", "Attack", "Display Map", "Display Player",
        "Display Room", "Move Pet", "Exit" };

    for (String label : buttonLabels) {
      JButton button = new JButton(label);
      button.addActionListener(e -> handleButtonAction(label));
      controlPanel.add(button);
    }

    addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        switch (e.getKeyChar()) {
          case 'a':
            handleButtonAction("Attack");
            break;
          case 'p':
            handleButtonAction("Pick Item");
            break;
          case 'l':
            handleButtonAction("Look Around");
            break;
          default:
            break;
        }
      }
    });

    setFocusable(true);
    requestFocusInWindow();

    JPanel leftPanel = new JPanel();
    leftPanel.setLayout(new BorderLayout());
    leftPanel.add(playerScrollPane, BorderLayout.CENTER);
    leftPanel.add(consoleScrollPane, BorderLayout.SOUTH);

    add(leftPanel, BorderLayout.WEST);
    add(controlPanel, BorderLayout.SOUTH);

    showWelcomeScreen();
  }

  /**
   * Handles button actions based on the action name.
   * 
   * @param action the action name.
   */
  void handleButtonAction(String action) {
    switch (action) {
      case "Move":
        controller.movePlayer(controller.getCurrentPlayer());
        break;
      case "Pick Item":
        controller.pickItem(controller.getCurrentPlayer());
        break;
      case "Look Around":
        appendToConsole(controller.getCurrentPlayer().lookAround(world));
        break;
      case "Attack":
        controller.attackTarget(controller.getCurrentPlayer());
        break;
      case "Display Map":
        controller.displayMap();
        break;
      case "Display Player":
        controller.displayPlayer(controller.getCurrentPlayer());
        break;
      case "Display Room":
        controller.displayRoom();
        break;
      case "Move Pet":
        controller.movePet();
        break;
      case "Exit":
        System.exit(0);
        break;
      default:
        appendToConsole("Unknown action: " + action);
    }
    controller.nextTurn();
    refreshView();
  }

  /**
   * Refreshes the game view.
   */
  private void refreshView() {
    revalidate();
    repaint();
    requestFocusInWindow();
  }

  /**
   * Sets the controller for the game view.
   * 
   * @param controller the game controller.
   */
  public void setController(GameController controller) {
    this.controller = controller;
  }

  /**
   * Displays the welcome screen.
   */
  private void showWelcomeScreen() {
    gamePanel = new JPanel(new BorderLayout());
    JLabel welcomeLabel = 
        new JLabel("<html><h1>Welcome to the Game!</h1><p>Created by Haidong Xu</p></html>",
        SwingConstants.CENTER);
    gamePanel.add(welcomeLabel, BorderLayout.CENTER);
    add(gamePanel, BorderLayout.CENTER);
    setVisible(true);
    requestFocusInWindow();
  }

  /**
   * Starts a new game.
   */
  private void startNewGame() {
    setupPlayersAndPet();
  }

  /**
   * Sets up the players and pet for the game.
   */
  void setupPlayersAndPet() {
    int numPlayers = Integer.parseInt(getInput("Enter the number of players:"));
    int currentPlayerCount = 0;

    for (int i = 0; i < numPlayers; i++) {
      if (currentPlayerCount >= 10) {
        showMessage(
            "Cannot add more players. The maximum number of players (10) has been reached.");
        break;
      }

      String playerName = getInput("Enter player " + (i + 1) + " name:");
      String[] playerTypes = { "Human", "Computer" };
      String playerType = getInputFromOptions("Select player type:", playerTypes);
      int roomIndex = Integer.parseInt(getInput(
          "Enter room index for player " + playerName + " (0 to "
          + (world.getRooms().size() - 1) + "):"));
      if (roomIndex < 0 || roomIndex >= world.getRooms().size()) {
        showMessage("Invalid room index. Assigning to a random room.");
        roomIndex = new Random().nextInt(world.getRooms().size());
      }
      Iroom initialRoom = world.getRoomByIndex(roomIndex);
      Iplayer player;
      if ("Computer".equalsIgnoreCase(playerType)) {
        player = new ComputerPlayer(playerName, initialRoom.getCoordinates(), 5);
      } else {
        player = new Player(playerName, initialRoom.getCoordinates(), 5);
      }

      world.addPlayer(player);
      currentPlayerCount++;
      updatePlayerList();
    }

    int petRoomIndex = Integer.parseInt(getInput("Enter room index for pet (0 to "
        + (world.getRooms().size() - 1) + "):"));
    if (petRoomIndex < 0 || petRoomIndex >= world.getRooms().size()) {
      showMessage("Invalid room index. Assigning to a random room.");
      petRoomIndex = new Random().nextInt(world.getRooms().size());
    }
    Iroom petRoom = world.getRoomByIndex(petRoomIndex);
    world.movePet(petRoom.getCoordinates());

    displayGameWorld();
  }

  /**
   * Displays the game world.
   */
  public void displayGameWorld() {
    getContentPane().remove(gamePanel);

    playerIconMap.clear();
    roomMap.clear();

    gamePanel = new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
          displayMap(world, g, true);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

      @Override
      public Dimension getPreferredSize() {
        int cellSize = 50;
        int padding = 10;
        int width = world.getColumns() * cellSize + 2 * padding;
        int height = world.getRows() * cellSize + 2 * padding;
        return new Dimension(width, height);
      }
    };

    gamePanel.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        Point clickPoint = e.getPoint();

        if (SwingUtilities.isRightMouseButton(e)) {
          for (Map.Entry<Rectangle, Iroom> entry : roomMap.entrySet()) {
            if (entry.getKey().contains(clickPoint)) {
              Iroom clickedRoom = entry.getValue();
              movePlayerToRoom(clickedRoom);
              return;
            }
          }
        } else {
          for (Map.Entry<Rectangle, Iplayer> entry : playerIconMap.entrySet()) {
            if (entry.getKey().contains(clickPoint)) {
              Iplayer clickedPlayer = entry.getValue();
              showPlayerInfo(clickedPlayer);
              break;
            }
          }
        }
      }
    });

    JScrollPane scrollPane = new JScrollPane(gamePanel);
    add(scrollPane, BorderLayout.CENTER);
    revalidate();
    repaint();
    requestFocusInWindow();
  }

  /**
   * Moves the player to a new room.
   * 
   * @param targetRoom the room to move the player to.
   */
  private void movePlayerToRoom(Iroom targetRoom) {
    Iplayer currentPlayer = controller.getCurrentPlayer();
    Iroom currentRoom = world.getRoomByCoordinates(currentPlayer.getCoordinates());
    List<Iroom> neighbors = world.getNeighbors(currentRoom);

    if (neighbors.contains(targetRoom)) {
      world.movePlayer(currentPlayer, targetRoom.getCoordinates());
      appendToConsole("Player " + currentPlayer.getName() + " moved to " + targetRoom.getName());
      controller.nextTurn();
      refreshView();
    } else {
      appendToConsole("Cannot move to " + targetRoom.getName(
          ) + " as it is not a neighboring room.");
    }
  }

  /**
   * Displays information about the player.
   * 
   * @param player the player whose information is to be displayed.
   */
  private void showPlayerInfo(Iplayer player) {
    StringBuilder playerInfo = new StringBuilder();
    playerInfo.append("Player Name: ").append(player.getName()).append("\n")
        .append("Coordinates: ").append(player.getCoordinates().getFirst()).append(", ")
        .append(player.getCoordinates().getSecond()).append("\n")
        .append("Items:\n");
    for (Iitem item : player.getItems()) {
      playerInfo.append(" - ").append(item.getName(
          )).append(" (Damage: ").append(item.getDamage()).append(")\n");
    }
    showMessage(playerInfo.toString());

    controller.nextTurn();
    refreshView();
  }

  /**
   * Updates the player list display.
   */
  public void updatePlayerList() {
    playerPanel.removeAll();
    for (Iplayer player : world.getPlayers()) {
      JLabel playerLabel = new JLabel(player.getName());
      playerPanel.add(playerLabel);
    }
    playerPanel.revalidate();
    playerPanel.repaint();
  }

  /**
   * Appends a message to the console.
   * 
   * @param message the message to append.
   */
  public void appendToConsole(String message) {
    consoleArea.append(message + "\n");
  }

  /**
   * Displays the map.
   * 
   * @param world       the game world.
   * @param g           the graphics context.
   * @param hideDetails whether to hide details on the map.
   * @throws IOException if an I/O error occurs.
   */
  public void displayMap(Igameworld world, Graphics g, boolean hideDetails) throws IOException {
    int cellSize = 50;
    int padding = 10;

    int width = world.getColumns() * cellSize + 2 * padding;
    int height = world.getRows() * cellSize + 2 * padding;

    g.setColor(Color.WHITE);
    g.fillRect(0, 0, width, height);

    g.setColor(Color.BLACK);
    Iplayer currentPlayer = controller.getCurrentPlayer();

    for (Iroom room : world.getRooms()) {
      Tuple<Integer, Integer> coords = room.getCoordinates();
      int rowStart = coords.getFirst();
      int colStart = coords.getSecond();
      int rowEnd = room.getEndingCoordinates().getFirst();
      int colEnd = room.getEndingCoordinates().getSecond();
      String roomName = room.getName();

      int x = colStart * cellSize + padding;
      int y = rowStart * cellSize + padding;
      int roomWidth = (colEnd - colStart + 1) * cellSize;
      int roomHeight = (rowEnd - rowStart + 1) * cellSize;

      g.drawRect(x, y, roomWidth, roomHeight);
      g.drawString(roomName, x + 2, y + 12);

      if (room.getCoordinates().equals(currentPlayer.getCoordinates())) {
        int itemOffsetY = 12;
        g.setColor(Color.ORANGE);
        for (Iitem item : room.getItems()) {
          g.drawString(item.getName(), x + roomWidth - g.getFontMetrics(
              ).stringWidth(item.getName()) - 2, y + itemOffsetY);
          itemOffsetY += 15;
        }
        g.setColor(Color.BLACK);
      }

      Rectangle roomRect = new Rectangle(x, y, roomWidth, roomHeight);
      roomMap.put(roomRect, room);
    }

    int playerSize = 15;
    int[][] positions = {
        { 0, 0 }, { 2, 0 }, { 2, 2 }, { 0, 2 }, { -2, 2 }, {
          -2, 0 }, { -2, -2 }, { 0, -2 }, { 2, -2 }, { 4, 0 }
    };

    Map<Iroom, Integer> roomPlayerCount = new HashMap<>();

    for (Iplayer player : world.getPlayers()) {
      Tuple<Integer, Integer> playerCoords = player.getCoordinates();
      Iroom playerRoom = world.getRoomByCoordinates(playerCoords);
      int index = roomPlayerCount.getOrDefault(playerRoom, 0);

      g.setColor(Color.BLUE);
      int playerX = playerCoords.getSecond() * cellSize + padding + cellSize / 2
          + positions[index % positions.length][0] * playerSize;
      int playerY = playerCoords.getFirst() * cellSize + padding + cellSize / 2
          + positions[index % positions.length][1] * playerSize;
      g.fillRect(playerX, playerY, playerSize, playerSize);
      g.drawString(player.getName(), playerX + playerSize + 2, playerY + playerSize / 2);

      Rectangle playerRect = new Rectangle(playerX, playerY, playerSize, playerSize);
      playerIconMap.put(playerRect, player);

      roomPlayerCount.put(playerRoom, index + 1);
    }

    Iroom currentRoom = world.getRoomByCoordinates(currentPlayer.getCoordinates());

    int targetSize = 15;
    Tuple<Integer, Integer> targetCoords = world.getTarget().getCoordinates();
    if (targetCoords != null && currentRoom.getCoordinates().equals(targetCoords)) {
      g.setColor(Color.RED);
      int targetX = targetCoords.getSecond() * cellSize + padding + 2;
      int targetY = targetCoords.getFirst() * cellSize + padding + cellSize - targetSize - 2;
      g.fillOval(targetX, targetY, targetSize, targetSize);
    }

    if (world.getPet() != null && currentRoom.getCoordinates(
        ).equals(world.getPet().getCoordinates())) {
      Ipet pet = world.getPet();
      g.setColor(Color.GREEN);
      int petSize = 15;
      int petX = currentRoom.getCoordinates(
          ).getSecond() * cellSize + padding + (cellSize - petSize) / 2;
      int petY = currentRoom.getCoordinates(
          ).getFirst() * cellSize + padding + (cellSize - petSize) / 2;
      g.fillRect(petX, petY, petSize, petSize);
      g.drawString(pet.getName(), petX + petSize + 2, petY + petSize / 2);
    }
  }

  /**
   * Saves and displays the map as an image.
   * 
   * @param world the game world.
   * @param path  the path to save the image.
   * @throws IOException if an I/O error occurs.
   */
  public void displayMap(Igameworld world, String path) throws IOException {
    int cellSize = 50;
    int padding = 10;

    int width = world.getColumns() * cellSize + 2 * padding;
    int height = world.getRows() * cellSize + 2 * padding;

    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = image.createGraphics();

    displayMap(world, g2d, false);

    g2d.dispose();
    ImageIO.write(image, "png", new File(path));
  }

  /**
   * Checks if a player is in the given room.
   * 
   * @param room the room to check.
   * @return true if a player is in the room, false otherwise.
   */
  private boolean isPlayerInRoom(Iroom room) {
    for (Iplayer player : world.getPlayers()) {
      if (player.getCoordinates().equals(room.getCoordinates())) {
        return true;
      }
    }
    return false;
  }

  /**
   * Gets the text from the console area.
   * 
   * @return the console text.
   */
  public String getConsoleText() {
    return consoleArea.getText();
  }

  /**
   * Sets the test mode and initializes the input queue.
   * 
   * @param testMode whether the test mode is enabled.
   * @param inputs   the inputs for the test mode.
   */
  public void setTestMode(boolean testMode, List<String> inputs) {
    this.testMode = testMode;
    this.testInputs.clear();
    this.testInputs.addAll(inputs);
  }

  /**
   * Gets input from the user or from the test queue.
   * 
   * @param message the message to display.
   * @return the user input.
   */
  private String getInput(String message) {
    if (testMode && !testInputs.isEmpty()) {
      return testInputs.poll();
    }
    return JOptionPane.showInputDialog(this, message);
  }

  /**
   * Gets input from the user or from the test queue with options.
   * 
   * @param message the message to display.
   * @param options the options for the user to choose from.
   * @return the user input.
   */
  private String getInputFromOptions(String message, String[] options) {
    if (testMode && !testInputs.isEmpty()) {
      return testInputs.poll();
    }
    return (String) JOptionPane.showInputDialog(
        this, message, "Player Type", JOptionPane.QUESTION_MESSAGE, null,
        options, options[0]);
  }

  /**
   * Displays a message to the user or appends it to the console in test mode.
   * 
   * @param message the message to display.
   */
  private void showMessage(String message) {
    if (testMode) {
      appendToConsole(message);
    } else {
      JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }
  }
}

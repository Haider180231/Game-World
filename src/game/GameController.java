package game;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class GameController {
    private Igameworld world;
    private GameView view;
    private int maxTurns;
    private int currentTurn;
    private FileWriter logWriter;
    private List<Iplayer> players;
    private int currentPlayerIndex;

    public GameController(Igameworld world, GameView view, int maxTurns, String logFilePath) throws IOException {
        this.world = world;
        this.view = view;
        this.maxTurns = maxTurns;
        this.currentTurn = 0;
        this.logWriter = new FileWriter(logFilePath, true); 
        this.players = new ArrayList<>();
        this.currentPlayerIndex = 0;
    }

    public void startGame() throws IOException {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();


        Iroom initialRoom = world.getRoomByIndex(random.nextInt(world.getRooms().size()));
        Iplayer computerPlayer = new ComputerPlayer("Computer", initialRoom.getCoordinates(), 5); // Example max items
        world.addPlayer(computerPlayer);
        players.add(computerPlayer);
        logWriter.write("Computer player added at room " + initialRoom.getName() + ".\n");

        // add player
        while (true) {
            System.out.println("Do you want to add a human player? (yes/no)");
            String response = scanner.nextLine();
            if (!response.equalsIgnoreCase("yes")) {
                break;
            }
            System.out.println("Enter player name:");
            String playerName = scanner.nextLine();
            initialRoom = world.getRoomByIndex(random.nextInt(world.getRooms().size()));
            Iplayer player = new Player(playerName, initialRoom.getCoordinates(), 5);  // Example max items
            world.addPlayer(player);
            players.add(player);
            logWriter.write("Player " + playerName + " added at room " + initialRoom.getName() + ".\n");
        }

        while (currentTurn < maxTurns) {
            Iplayer currentPlayer = players.get(currentPlayerIndex);
            System.out.println("It's " + currentPlayer.getName() + "'s turn.");
            logWriter.write("Turn " + currentTurn + ": " + currentPlayer.getName() + "'s turn.\n");

            if (currentPlayer instanceof ComputerPlayer) {
                System.out.println("Computer player is taking its turn...");
                ((ComputerPlayer) currentPlayer).takeTurn(world);
                logWriter.write("Computer player took its turn.\n\n");  
            } else {
                System.out.println("Choose an action: move, pick item, look around, display map, display player, exit");
                String action = scanner.nextLine();
                logWriter.write("Action: " + action + "\n");

                switch (action) {
                    case "move":
                        movePlayer(currentPlayer, scanner, logWriter);
                        logWriter.write("\n");  
                        break;

                    case "pick item":
                        pickItem(currentPlayer, logWriter);
                        logWriter.write("\n");  
                        break;

                    case "look around":
                        lookAround(currentPlayer, logWriter);
                        logWriter.write("\n");  
                        break;

                    case "display map":
                        displayMap(logWriter);
                        logWriter.write("\n");  
                        break;

                    case "display player":
                        displayPlayer(currentPlayer, logWriter);
                        logWriter.write("\n");  
                        break;

                    case "exit":
                        logWriter.close();
                        scanner.close();
                        return;

                    default:
                        System.out.println("Invalid action.");
                        logWriter.write("Invalid action.\n\n");  
                        break;
                }
            }


            world.moveTarget();
            logWriter.write("Target moved.\n\n");  

            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
            currentTurn++;
        }

        logWriter.write("Game over. Maximum turns reached.\n");
        logWriter.close();
        scanner.close();
    }

    private void movePlayer(Iplayer player, Scanner scanner, FileWriter logWriter) throws IOException {
        Iroom currentRoom = findRoomByCoordinates(player.getCoordinates());
        List<Iroom> neighbors = world.getNeighbors(currentRoom);
        System.out.println("Enter the room number to move to:");
        for (int i = 0; i < neighbors.size(); i++) {
            System.out.println(i + ": " + neighbors.get(i).getName());
        }
        int roomIndex = scanner.nextInt();
        scanner.nextLine();  
        if (roomIndex >= 0 && roomIndex < neighbors.size()) {
            Iroom newRoom = neighbors.get(roomIndex);
            world.movePlayer(player, newRoom.getCoordinates());
            logWriter.write("Player " + player.getName() + " moved to room " + newRoom.getName() + ".\n");
        } else {
            logWriter.write("Invalid room index for player " + player.getName() + ".\n");
        }
    }

    private void pickItem(Iplayer player, FileWriter logWriter) throws IOException {
        Iroom room = findRoomByCoordinates(player.getCoordinates());
        if (room != null && !room.getItems().isEmpty()) {
            Iitem item = room.getItems().get(0);  
            player.addItem(item);
            room.getItems().remove(item);
            logWriter.write("Player " + player.getName() + " picked up item " + item.getName() + ".\n");
            System.out.println("Player " + player.getName() + " picked up item " + item.getName() + ".\n");
        } else {
            logWriter.write("No items in the room for player " + player.getName() + " to pick up.\n");
            System.out.println("No items in the room for player " + player.getName() + " to pick up.\n");
        }
    }

    private void lookAround(Iplayer player, FileWriter logWriter) throws IOException {
        Iroom room = findRoomByCoordinates(player.getCoordinates());
        if (room != null) {
            StringBuilder output = new StringBuilder();
            output.append("Player " + player.getName() + " is in: " + room.getName() + ".\n");
            output.append("Items in the room:\n");
            for (Iitem item : room.getItems()) {
                output.append(" - " + item.getName() + "\n");
            }
            output.append("Visible rooms:\n");
            for (Iroom neighbor : world.getNeighbors(room)) {
                output.append(" - " + neighbor.getName() + "\n");
            }
            String outputStr = output.toString();
            System.out.print(outputStr);  
            logWriter.write(outputStr);  
        }
    }

    private void displayMap(FileWriter logWriter) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String path = "res/world" + timeStamp + ".png";
        try {
            view.displayMap(world, path);
            logWriter.write("Map displayed and saved to " + path + ".\n");
            System.out.println("Map displayed and saved to " + path + ".\n");
        } catch (IOException e) {
            logWriter.write("Failed to display map: " + e.getMessage() + "\n");
            System.out.println("Failed to display map: " + e.getMessage() + "\n");
        }
    }

    private void displayPlayer(Iplayer player, FileWriter logWriter) throws IOException {
        StringBuilder output = new StringBuilder();
        output.append("Player Name: " + player.getName() + "\n");
        output.append("Coordinates: " + player.getCoordinates().getFirst() + ", " + player.getCoordinates().getSecond() + "\n");
        output.append("Items:\n");
        for (Iitem item : player.getItems()) {
            output.append(" - " + item.getName() + " (Damage: " + item.getDamage() + ")\n");
        }
        String outputStr = output.toString();
        System.out.print(outputStr);  
        logWriter.write(outputStr);  
    }

    private Iroom findRoomByCoordinates(Tuple<Integer, Integer> coordinates) {
        for (Iroom room : world.getRooms()) {
            if (room.getCoordinates().equals(coordinates)) {
                return room;
            }
        }
        return null;
    }
}

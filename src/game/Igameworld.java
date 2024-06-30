package game;

import java.io.IOException;
import java.util.List;

public interface Igameworld {
    void addRoom(Iroom room);
    Iroom getRoomByIndex(int index);
    List<Iroom> getRooms();
    Itarget getTarget();
    List<Iroom> getNeighbors(Iroom room);
    void displayRoomInfo(int index);
    void moveTarget();
    void addPlayer(Iplayer player);
    List<Iplayer> getPlayers();
    void movePlayer(Iplayer player, Tuple<Integer, Integer> newCoordinates);
    int getColumns();
    int getRows();
    Iroom getRoomByCoordinates(Tuple<Integer, Integer> coordinates);
}

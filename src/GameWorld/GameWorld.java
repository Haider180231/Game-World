package GameWorld;

import java.util.List;

public class GameWorld {
    private int rows;
    private int columns;
    private String name;
    private ITarget target;
    private List<IRoom> rooms;

    public GameWorld(int rows, int columns, String name, ITarget target, List<IRoom> rooms) {
        this.rows = rows;
        this.columns = columns;
        this.name = name;
        this.target = target;
        this.rooms = rooms;
    }

    public void addRoom(IRoom room) {
        rooms.add(room);
    }

    public IRoom getRoomByIndex(int index) {
        return rooms.get(index);
    }

    public int getTargetHealth() {
        return target.getHealth();
    }

    public String getTargetName() {
        return target.getName();
    }

    public void displayMap() {
        // 实现显示地图的逻辑
    }

    public void moveTarget() {
        // 实现移动目标的逻辑
    }

    public List<IRoom> getRooms() {
        return rooms;
    }
}

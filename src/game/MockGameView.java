package game;

import java.util.ArrayList;
import java.util.List;

public class MockGameView extends GameView {
    private List<String> consoleOutput;

    public MockGameView(Igameworld world) {
        super(world, "res/run_log.txt", null); // 传递模拟数据以满足GameView的构造函数
        consoleOutput = new ArrayList<>();
    }

    @Override
    public void appendToConsole(String message) {
        consoleOutput.add(message);
    }

    @Override
    public void updatePlayerList() {
        // 可以不做任何操作，或者记录更新
    }

    @Override
    public void displayGameWorld() {
        // 可以不做任何操作，或者记录更新
    }

    @Override
    public void displayMap(Igameworld world, String path) {
        // 可以不做任何操作，或者记录地图显示
    }

    public List<String> getConsoleOutput() {
        return consoleOutput;
    }
}

package sudoku;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class App {

    public static void main(String[] args) throws Exception {
        App app = new App();
        app.run();
    }

    public void run() throws Exception {
        Map<NodeKey, Node> nodeMap = initializeBoard();
        NodeObserver observer = new NodeObserver(nodeMap);
        loadBoard(readFile("board3.txt"), nodeMap);
        printBoard(nodeMap);
        System.out.println();
        while (isBoardComplete(nodeMap)) {
            List<Node> nodeList = getEmptyNodes(nodeMap);
            for (int i = 0; i < 3; i++) {
                final int index = i;
                for (Node n : nodeList) {
                    List<Node> emptyColleagues = NodeUtil.getCluster(nodeMap, NodeUtil.NodeType.getNodeType(index), n);
                    List<Long> longList = new ArrayList<>(n.getPossibleValuesList());
                    for (Long x : longList) {
                        boolean cantPlace = false;
                        for (Node emptyNode : emptyColleagues) {
                            if (emptyNode.getPossibleValuesList().contains(x)) {
                                cantPlace = true;
                                break;
                            }
                        }
                        if (!cantPlace) {
                            n.setValue(x);
                        }
                    }
                }
            }
        }
        printBoard(nodeMap);
    }

    public boolean isBoardComplete(Map<NodeKey, Node> nodeMap) {
        return nodeMap.values().stream().filter(c -> c.getValue() == -1).count() > 1;
    }

    public List<Node> getEmptyNodes(Map<NodeKey, Node> nodeMap) {
        return nodeMap.values().stream().filter(c -> c.getValue() == -1).collect(Collectors.toList());
    }

    private Map<NodeKey, Node> initializeBoard() {
        Map<NodeKey, Node> nodeMap = new HashMap<>();
        int grid = 0;
        int tmp = 0;
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                Node node = new Node.Builder().x(x).y(y).grid(grid).build();
                nodeMap.put(new NodeKey(x, y), node);
                if (nodeMap.size() == 26 || nodeMap.size() == 53 || nodeMap.size() == 80) {
                    tmp = tmp + 3;
                }
                if (y == 2 || y == 5 || y == 8) {
                    grid++;
                }
            }
            grid = tmp;
        }
        return nodeMap;
    }

    public Node getNode(final int x,
                        final int y,
                        final Map<NodeKey, Node> nodeMap) {
        return nodeMap.get(new NodeKey(x, y));
    }

    public void printBoard(Map<NodeKey, Node> nodeMap) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                Node node = getNode(i, j, nodeMap);
                if (node.getValue() == -1) {
                    System.out.print("X");
                } else {
                    System.out.print(node.getValue());
                }
            }
            System.out.println();
        }
    }

    public void loadBoard(final List<String> list,
                          final Map<NodeKey, Node> nodeMap) throws Exception {
        int row = 0;
        for (String line : list) {
            for (int i = 0; i < 9; i++) {
                char c = line.trim().charAt(i);
                if (c != 'x') {
                    getNode(row, i, nodeMap).setValue(Integer.parseInt("" + c));
                }
            }
            row++;
        }
    }

    public List<String> readFile(final String filename) throws Exception {
        Path filePath = new File(filename).toPath();
        Charset charset = Charset.defaultCharset();
        List<String> stringList = Files.readAllLines(filePath, charset);
        return stringList;
    }
}

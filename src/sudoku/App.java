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

    private String filename;

    public static void main(String[] args) throws Exception {
        App app = new App();
        app.run("board5.txt");
    }

    public void run(final String filename) throws Exception {
        this.filename = filename;
        Map<NodeKey, Node> nodeMap = initializeBoard();
        NodeObserver observer = new NodeObserver(nodeMap);
        System.out.println("start\n");
        loadBoard(readFile(this.filename), nodeMap);
        printBoard(nodeMap);
        placeValues(nodeMap);

        if (!isBoardComplete(nodeMap)) {
            Map<NodeKey, Node> cloneMap = cloneMap(nodeMap);


            final Node guessNode = cloneMap.values().stream().filter(c -> c.getValue() == -1
                    && !c.getPossibleValuesList().isEmpty()
                    && c.getPossibleValuesList().size() == 2).findAny().get();
            long val = guessNode.getPossibleValuesList().get(0);
            long secondVal = guessNode.getPossibleValuesList().get(1);

            cloneMap.get(new NodeKey(guessNode.getX(), guessNode.getY())).setValue(val);
            if (!isBoardComplete(nodeMap)) {
                nodeMap.get(new NodeKey(guessNode.getX(), guessNode.getY())).setValue(secondVal);
            } else {
                nodeMap = cloneMap;
            }
        }
        printBoard(nodeMap);
        System.out.println("end\n");
    }

    private Map<NodeKey, Node> cloneMap(final Map<NodeKey, Node> originalMap) throws Exception {
        Map<NodeKey, Node> nodeMap = initializeBoard();
        NodeObserver observer = new NodeObserver(nodeMap);
        loadBoard(readFile(this.filename), nodeMap);
        printBoard(nodeMap);
        placeValues(nodeMap);
        return nodeMap;
    }

    private void placeValues(Map<NodeKey, Node> nodeMap) {
        final int maxIterations = 100;
        int cnt = 0;
        while (!isBoardComplete(nodeMap) && cnt < maxIterations) {
            List<Node> nodeList = getEmptyNodes(nodeMap);
            for (int i = 0; i < 3; i++) {
                for (Node n : nodeList) {
                    List<Node> emptyColleagues = NodeUtil.getCluster(nodeMap, NodeUtil.NodeType.getNodeType(i), n);
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
                            break;
                        }
                    }
                }
            }
            cnt++;
        }
    }

    public boolean isBoardComplete(Map<NodeKey, Node> nodeMap) {
        return nodeMap.values().stream().filter(c -> c.getValue() == -1).count() == 0;
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
                    System.out.print("x");
                } else {
                    System.out.print(node.getValue());
                }

                if (j == 2 || j == 5) {
                    System.out.print("|");
                }
            }
            System.out.println();
            if (i == 2 || i == 5) {
                System.out.println("-----------");
            }
        }
        System.out.println("\n###########\n");
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

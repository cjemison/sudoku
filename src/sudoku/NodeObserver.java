package sudoku;

import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.stream.Collectors;

/**
 * Created by cjemison on 3/9/16.
 */
public class NodeObserver implements Observer {
    private final Map<NodeKey, Node> nodeMap;

    public NodeObserver(Map<NodeKey, Node> nodeMap) {
        this.nodeMap = nodeMap;
        nodeMap.values().forEach(c -> {
            c.addObserver(this);
        });
    }

    @Override
    public void update(Observable o, Object arg) {

        Node updatedNode = (Node) arg;
        nodeMap.values().stream().filter(node -> !node.equals(updatedNode)).filter(node -> updatedNode.getX() == node.getX()).forEach(node -> {
            node.removePossiblilty(updatedNode.getValue());
        });

        nodeMap.values().stream().filter(node -> !node.equals(updatedNode)).filter(node -> updatedNode.getY() == node.getY()).forEach(node -> {
            node.removePossiblilty(updatedNode.getValue());
        });

        for (int i = 0; i < 9; i++) {
            final int val = i;
            List<Node> list = nodeMap.values().stream().filter(c -> c.getGrid() == val).collect(Collectors.toList());

            List<Long> longList = list.stream().filter(c -> c.getValue() != -1).map(Node::getValue).collect(Collectors.toList());

            for (Node node : list) {
                if (node.getValue() == -1) {
                    for (Long l : longList) {
                        nodeMap.get(new NodeKey(node.getX(), node.getY())).removePossiblilty(l);
                    }
                }
            }
        }
    }
}

package sudoku;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by cjemison on 3/12/16.
 */
public class NodeUtil {

    public static List<Node> getCluster(final Map<NodeKey, Node> nodeMap,
                                        final NodeType nodeType,
                                        final Node value) {
        List<Node> list = new ArrayList<>();
        if (nodeType == NodeType.X) {
            list.addAll(nodeMap.values().stream().filter(c -> !c.equals(value)
                    && c.getX() == value.getX()
                    && c.getValue() == -1).collect(Collectors.toList()));
        } else if (nodeType == NodeType.Y) {
            list.addAll(nodeMap.values().stream().filter(c -> !c.equals(value)
                    && c.getY() == value.getY()
                    && c.getValue() == -1).collect(Collectors.toList()));
        } else {
            list.addAll(nodeMap.values().stream().filter(c -> !c.equals(value)
                    && c.getGrid() == value.getGrid()
                    && c.getValue() == -1).collect(Collectors.toList()));
        }
        return list;
    }

    enum NodeType {
        X(0), Y(1), G(2);

        private static final Map<Integer, NodeType> map;

        static {
            map = new HashMap<>();
            for (NodeType n : NodeType.values()) {
                map.put(n.getValue(), n);
            }
        }

        private final int value;

        NodeType(int value) {
            this.value = value;
        }

        public static NodeType getNodeType(int i) {
            return map.get(i);
        }

        public int getValue() {
            return value;
        }
    }

}

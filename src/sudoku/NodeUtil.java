package sudoku;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by cjemison on 3/12/16.
 */
public class NodeUtil {


    public static List<Node> getCluster(final Map<NodeKey, Node> nodeMap,
                                        final NodeType nodeType,
                                        final Node value) {

        Objects.requireNonNull(nodeMap);
        Objects.requireNonNull(nodeType);
        Objects.requireNonNull(value);

        return nodeType.getFriends(nodeMap, nodeType.getNodeAxisValues().apply(value)).parallelStream().filter((c) ->
                !c.equals(value) && c.getValue() == -1).collect(Collectors.toList());
    }

    public static Map<NodeType, List<Long>> getValues(final Map<NodeKey, Node> nodeMap,
                                                      final Node value) {
        Map<NodeType, List<Long>> map = new HashMap<>();

        for (NodeType nodeType : NodeType.values()) {
            map.put(nodeType, nodeType.getFriends(nodeMap, nodeType.getNodeAxisValues().apply(value)).stream().map(Node::getValue).collect(Collectors.toList()));
        }
        return map;
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

        public final int value;

        NodeType(int value) {
            this.value = value;
        }

        public static NodeType getNodeType(int i) {
            return map.get(i);
        }

        public int getValue() {
            return value;
        }

        public Function<Node, Long> getNodeAxisValues() {
            if (this.value == NodeType.X.value) {
                return Node::getX;
            } else if (this.value == NodeType.Y.value) {
                return Node::getY;
            } else {
                return Node::getGrid;
            }
        }

        public Predicate<Node> getPredicate(final Node node,
                                            final long index) {
            return (c) -> getNodeAxisValues().apply(node) == index;
        }

        public List<Node> getFriends(final Map<NodeKey, Node> nodeMap,
                                     final long index) {
            return nodeMap.values().stream().filter(c -> getPredicate(c, index).test(c)).collect(Collectors.toList());
        }
    }

}

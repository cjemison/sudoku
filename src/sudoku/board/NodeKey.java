package sudoku.board;

/**
 * Created by cjemison on 3/9/16.
 */
public class NodeKey {
    final long x;
    final long y;

    public NodeKey(long x, long y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NodeKey)) return false;

        NodeKey nodeKey = (NodeKey) o;

        if (x != nodeKey.x) return false;
        return y == nodeKey.y;

    }

    @Override
    public int hashCode() {
        int result = (int) (x ^ (x >>> 32));
        result = 31 * result + (int) (y ^ (y >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "NodeKey{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}

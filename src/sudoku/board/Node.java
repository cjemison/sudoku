package sudoku.board;


import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Node extends Observable {
    private final long x;
    private final long y;
    private final long grid;
    private final List<Long> possibleValuesList = new ArrayList<>();
    private long value;

    private Node(final long x,
                 final long y,
                 final long grid,
                 final long value) {
        this.x = x;
        this.y = y;
        this.grid = grid;
        this.value = value;
        if (value == -1) {
            this.clear();
            possibleValuesList.addAll(IntStream.range(1, 10).asLongStream().boxed().collect(Collectors.toList()));
        } else {
            this.clear();
        }
    }

    public long getX() {
        return x;
    }

    public long getY() {
        return y;
    }

    public long getGrid() {
        return grid;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.clear();
        this.value = value;
        setChanged();
        notifyObservers(this);
    }

    public void clear() {
        this.possibleValuesList.clear();
    }

    public List<Long> getPossibleValuesList() {
        return possibleValuesList;
    }

    public void removePossiblilty(long value) {
        this.possibleValuesList.remove(value);
        if (this.possibleValuesList.size() == 1) {
            this.setValue(this.possibleValuesList.get(0));
            setChanged();
            notifyObservers(this);
        } else {
            this.possibleValuesList.remove(value);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;

        Node node = (Node) o;

        if (x != node.x) return false;
        if (y != node.y) return false;
        return grid == node.grid;

    }

    @Override
    public int hashCode() {
        int result = (int) (x ^ (x >>> 32));
        result = 31 * result + (int) (y ^ (y >>> 32));
        result = 31 * result + (int) (grid ^ (grid >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Node{" +
                "value=" + value +
                ", x=" + x +
                ", y=" + y +
                ", grid=" + grid +
                '}';
    }

    public static class Builder {
        private long value = -1;
        private long x = -1;
        private long y = -1;
        private long grid = -1;

        public Builder x(long x) {
            this.x = x;
            return this;
        }

        public Builder y(long y) {
            this.y = y;
            return this;
        }

        public Builder grid(long z) {
            this.grid = z;
            return this;
        }

        public Builder value(final long value) {
            this.value = value;
            return this;
        }

        public Node build() {
            return new Node(this.x, this.y, this.grid, value);
        }
    }
}

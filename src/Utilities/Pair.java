package Utilities;

public class Pair<K, V> {
    private final K first;
    private final V second;

    public Pair(K first, V second) {
        this.first = first;
        this.second = second;
    }

    public K getFirst() {
        return first;
    }

    public V getSecond() {
        return second;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Pair<?, ?>) {
            Pair<?, ?> objPair = (Pair<?, ?>) obj;
            return objPair.getFirst().equals(first) && objPair.getSecond().equals(second);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return first.hashCode() + second.hashCode();
    }
}

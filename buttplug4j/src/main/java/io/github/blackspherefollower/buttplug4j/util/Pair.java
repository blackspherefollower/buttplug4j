package io.github.blackspherefollower.buttplug4j.util;

public final class Pair<A, B> {

    private A a;
    private B b;

    public Pair(final A a, final B b) {
        this.a = a;
        this.b = b;
    }

    public A getLeft() {
        return a;
    }

    public B getRight() {
        return b;
    }

    public A setLeft(final A value) {
        a = value;
        return a;
    }

    public B setRight(final B value) {
        b = value;
        return b;
    }

    @Override
    public String toString() {
        return String.format("(%s, %s)", a, b);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Pair<?, ?> pair = (Pair<?, ?>) o;
        if (a != null ? !a.equals(pair.a) : pair.a != null) return false;
        return b != null ? b.equals(pair.b) : pair.b == null;
    }

    @Override
    public int hashCode() {
        int result = a != null ? a.hashCode() : 0;
        result = 31 * result + (b != null ? b.hashCode() : 0);
        return result;
    }
}

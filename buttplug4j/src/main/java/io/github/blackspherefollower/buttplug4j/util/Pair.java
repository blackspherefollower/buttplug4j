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
}

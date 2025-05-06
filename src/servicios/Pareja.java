package servicios;

import java.util.Iterator;

public class Pareja<X, Y> implements Iterable<Object> {
    private X x;
    private Y y;

    public Pareja(X x, Y y) {
        this.x = x;
        this.y = y;
    }

    public X getX() { return x; }
    public void setX(X x) { this.x = x; }

    public Y getY() { return y; }
    public void setY(Y y) { this.y = y; }

    @Override
    public Iterator<Object> iterator() {
        return new Iterator<>() {
            private int indice = 0;

            @Override
            public boolean hasNext() { return indice < 2; }
            @Override
            public Object next() { return indice++ == 0 ? x : y; }
        };
    }
}


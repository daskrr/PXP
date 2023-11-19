package pxp.util;

/**
 * A pair of two values.<br/>
 * Checking equality with {@link Pair#equals(Object)} will check the values and order of this pair against another, instead of the hash code.
 */
public class Pair<T1,T2>
{
    public T1 left;
    public T2 right;

    public Pair(T1 left, T2 right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Pair pair))
            return false;

        return this.left.equals(pair.left) && this.right.equals(pair.right);
    }
}

package pxp.util;

/**
 * Has the same functionality as a Pair, but when using {@link SwitchPair#equals(Object)}, it checks each value against each of the other values.
 */
public class SwitchPair<T1, T2> extends Pair<T1, T2>
{
    public SwitchPair(T1 left, T2 right) {
        super(left, right);
    }

    public boolean equals(Object other) {
        if (!(other instanceof SwitchPair pair))
            return false;

        return
            (this.left.equals(pair.left) && this.right.equals(pair.right)) ||
            (this.left.equals(pair.right) && this.right.equals(pair.left));
    }
}

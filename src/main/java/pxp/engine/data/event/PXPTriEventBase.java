package pxp.engine.data.event;

public abstract class PXPTriEventBase<T0, T1, T2> extends PXPEventBase {
    public abstract void invoke(T0 t0, T1 t1, T2 t2);
}

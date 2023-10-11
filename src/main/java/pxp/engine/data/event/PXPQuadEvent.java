package pxp.engine.data.event;

public abstract class PXPQuadEvent<T0, T1, T2, T3> extends PXPEventBase {
    public abstract void invoke(T0 t0, T1 t1, T2 t2, T3 t3);
}

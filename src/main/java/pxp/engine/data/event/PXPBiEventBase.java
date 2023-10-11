package pxp.engine.data.event;

public abstract class PXPBiEventBase<T0, T1> extends PXPEventBase {
    public abstract void invoke(T0 t0, T1 t1);
}

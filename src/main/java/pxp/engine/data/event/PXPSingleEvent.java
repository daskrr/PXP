package pxp.engine.data.event;

public abstract class PXPSingleEvent<T> extends PXPEventBase {
    public abstract void invoke(T t);
}


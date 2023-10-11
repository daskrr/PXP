package pxp.engine.data;

import pxp.engine.core.GameObject;

import java.util.function.Supplier;

/**
 * GameObject Supplier implementation for ease of use in case it isn't used as a lambda
 */
public interface GameObjectSupplier extends Supplier<GameObject> { }

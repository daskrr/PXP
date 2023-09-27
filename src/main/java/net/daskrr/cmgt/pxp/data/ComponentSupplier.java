package net.daskrr.cmgt.pxp.data;

import net.daskrr.cmgt.pxp.core.component.Component;

import java.util.function.Supplier;

/**
 * Component Supplier implementation for ease of use in case it isn't used as a lambda
 */
public interface ComponentSupplier extends Supplier<Component> { }

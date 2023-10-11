package pxp.engine.data;

import pxp.engine.core.GameProcess;
import processing.core.PConstants;

import java.util.function.Consumer;

/**
 * DrawEnvironment for easy anonymous (or otherwise) implementation of the interface
 */
public interface DrawEnvironment extends Consumer<GameProcess>, PConstants { }

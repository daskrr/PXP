package pxp.annotations;

import pxp.engine.core.GameObject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Lets the Inspector know that a String field represents a {@link GameObject}'s name.<br/>
 * Use this in reverse (applied to a GameObject field) to let the Inspector know to automatically assign a GameObject
 * instance to the field, instead of just showing it, and the actual instance of the GameObject having to be manually grabbed.<br/><br/>
 * <i>Note: This isn't used in reverse in PXP, since the engine is supposed to be able to run standalone, without the Editor.</i>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SyncGameObject { }

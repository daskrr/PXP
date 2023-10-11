package pxp.annotations;

import pxp.engine.core.GameObject;
import pxp.engine.core.RectTransform;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The component requires a {@link RectTransform} to be present in the {@link GameObject}.<br/><br/>
 * The annotation can only be used with components!
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RequiresRectTransform { }

package pxp.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * NOT SURE YET IF THIS IS NEEDED - would have to modify a few components to take strings as components and later in start() get those components from the GOs
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SyncGameObjectComponent { }

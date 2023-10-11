package pxp.annotations;

/**
 * Used with {@link LinkFieldInInspector} to specify whether a method should get or set a value
 */
public enum LinkType {
    /**
     * The method is of type GETTER (gets the value of the field and returns it)<br/>
     * Getters must return the same type as the field and must not take any parameters
     */
    GETTER,
    /**
     * The method is of type SETTER (sets the value of the field)<br/>
     * Setters must return void and take 1 parameter of the same type as the field's
     */
    SETTER
}

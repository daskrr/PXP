package pxp.annotations;

import java.lang.annotation.*;

/**
 * Whether to show the field in the Editor's Inspector<br/>
 * Also has the capacity to limit showing of the field based on meeting a condition
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Repeatable(RepeatableShowInInspector.class)
public @interface ShowInInspector
{
    /**
     * Field name<br/>
     * Checks a field by name from the class where the annotation is
     */
    String key() default "";

    /**
     * Checks a method's return by name from the class where the annotation is
     */
    String conditionMethod() default "";

    /**
     * Checks the condition against a boolean, defaulted to true
     */
    boolean value() default true;
    /**
     * Condition value<br/>
     * Checks the condition against a String value
     */
    String stringValue() default "";

    /**
     * Checks the condition against an enum string value (enum#toString)
     */
    String enumValue() default "";


    /**
     * Checks if the object is an instance of the specified Class<br/>
     * <i>Due to Java limitations, this is defaulted to {@link Object}, ergo Object.class will be ignored!</i>
     */
    Class<?> isInstance() default Object.class;
}

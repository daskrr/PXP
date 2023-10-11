package pxp.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Turns an int field into a slider in the Inspector
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ValueSliderInt
{
    /**
     * The minimum value of the slider
     */
    int min() default 0;
    /**
     * The maximum value of the slider
     */
    int max() default 1;

    /**
     * A field in the class that contains the <b>minimum</b> value of the slider<br/>
     * The field must be of type <code>int</code>.
     */
    String minField() default "";
    /**
     * A field in the class that contains the <b>maximum</b> value of the slider<br/>
     * The field must be of type <code>int</code>.
     */
    String maxField() default "";
}

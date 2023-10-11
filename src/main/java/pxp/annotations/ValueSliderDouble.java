package pxp.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Turns a double field into a slider in the Inspector
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ValueSliderDouble
{
    /**
     * The minimum value of the slider
     */
    double min() default 0D;
    /**
     * The maximum value of the slider
     */
    double max() default 1D;

    /**
     * A field in the class that contains the <b>minimum</b> value of the slider<br/>
     * The field must be of type <code>double</code>.
     */
    String minField() default "";
    /**
     * A field in the class that contains the <b>maximum</b> value of the slider<br/>
     * The field must be of type <code>double</code>.
     */
    String maxField() default "";
}

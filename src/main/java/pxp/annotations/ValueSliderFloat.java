package pxp.annotations;

import java.lang.annotation.*;

/**
 * Turns a float field into a slider in the Inspector
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ValueSliderFloat
{
    /**
     * The minimum value of the slider
     */
    float min() default 0f;
    /**
     * The maximum value of the slider
     */
    float max() default 1f;

    /**
     * A field in the class that contains the <b>minimum</b> value of the slider<br/>
     * The field must be of type <code>float</code>.
     */
    String minField() default "";
    /**
     * A field in the class that contains the <b>maximum</b> value of the slider<br/>
     * The field must be of type <code>float</code>.
     */
    String maxField() default "";
}

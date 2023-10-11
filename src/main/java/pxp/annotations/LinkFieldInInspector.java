package pxp.annotations;

import pxp.engine.core.component.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Links a field to this method, specifying this method's intention: Getting the value of the field or setting it.<br/><br/>
 * This allows fields that are private or not supposed to be directly retrieved or set to show up in the Inspector and be modifiable.<br/><br/>
 * By default, a link applies both when the game is not running and at runtime. To control this behaviour, use {@link LinkFieldInInspector#state()}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LinkFieldInInspector
{
    /**
     * The field name to link as a getter or a setter
     */
    String name();

    /**
     * The type of the method, either GETTER or SETTER<br/><br/>
     * Getters must return the same type as the field and must not take any parameters<br/>
     * Setters must return void and take 1 parameter of the same type as the field's
     */
    LinkType type();

    /**
     * When should the Link apply?<br/>
     * <blockquote>
     *    - <code>INSPECTOR</code>: the link only applies when in inspector with the game <b>not running</b>.<br/>
     *    - <code>RUNTIME</code>: the link only applies when the <b>game is running</b><br/>
     *    - <code>BOTH</code>: the link applies for both of the aforementioned cases.
     * </blockquote><br/>
     * Be careful when using this, as if the getter/setter method performs actions that involve the running game <i>(like
     * communicating with another GameObject)</i> and this is set to <code>INSPECTOR</code> (or <code>BOTH</code>), the application will crash.<br/>
     * Or if it is set to <code>BOTH</code>, but there is no check if the Component has been started <i>({@link Component#started})</i>
     * and an action is performed that requires the context of the running game, the application will <b>crash</b>.<br/><br/>
     * To avoid confusion, this is defaulted to <code>RUNTIME</code> to avoid crashes. This will, however severely limit
     * the number of editable properties a component has, if the component has a lot of getters/setters.<br/><br/>
     * <h3>Practical examples:</h3>
     * <blockquote>
     *     <h4>Simple calculation:</h4>
     *     You have a field that needs, when being set, to be converted from a 0-100 percentage to a 0-1 floating point
     *     and vice versa when being retrieved.<br/>
     *     You make a getter and a setter and you want this behaviour to work in the Inspector, ergo you use @{@link LinkFieldInInspector}.<br/>
     *     In the above example, your best choice is using {@link LinkState#BOTH}, since the calculation is independent of
     *     the game state (running or not).
     * </blockquote><br/>
     * <blockquote>
     *     <h4>Game Object communication:</h4>
     *     You have a field that sets another Game Object's RectTransform's size.<br/>
     *     You make a getter and a setter. (getter too, since you don't want the field to be public and exposed to changing
     *     without applying the change) The setter's LinkFieldInInspector annotation needs to have <code>state = LinkState.RUNTIME</code>
     *     because setting this value while the game is not running will fail, since there is no connection to the other Game Object.<br/>
     *     You might think that this also causes you to need another setter for the <code>INSPECTOR</code> state, however, if no other
     *     code is ran using the value, just using a @{@link ShowInInspector} will work in this case, since the Inspector
     *     will only use the link when the state matches.
     * </blockquote><br/>
     * Other cases should be self-explanatory after the examples above.<br/><br/>
     * <i>Note: If the existing components seem to be using this annotation contrary to the examples (specifically, not
     * using a @{@link ShowInInspector} annotation, where it would be easier to, this is intended, as PXP should be usable
     * as a standalone without an Editor too and to accommodate for that, there were some workarounds done.<br/>
     * The examples and all of the annotations only exist for the Inspector to read. <b>They do nothing outside of the Inspector!</b></i>
     */
    // all the above "I ain't reading alldat" inducing text had to be written as this is pretty confusing aaaand java
    // setters and getters for fields DON'T EXIST, so... yeah bring some coffee and read up!
    LinkState state() default LinkState.RUNTIME;
}

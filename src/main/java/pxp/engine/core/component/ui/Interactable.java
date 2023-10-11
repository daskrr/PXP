package pxp.engine.core.component.ui;

import processing.event.MouseEvent;
import pxp.annotations.*;
import pxp.engine.core.Routine;
import pxp.engine.core.component.Animator;
import pxp.engine.core.component.pointer.PointerHandlerHover;
import pxp.engine.core.component.pointer.PointerHandlerMouse;
import pxp.engine.data.Color;
import pxp.engine.data.Vector2;
import pxp.engine.data.assets.SpriteAsset;
import pxp.engine.data.ui.InteractableTransition;
import pxp.engine.data.ui.Cursor;

/**
 * Interactable is a component base used for interactable components, such as {@link Button} and {@link Slider}.<br/>
 * <i>This is NOT a component and cannot be instantiated by itself</i>
 */
@RequiresRectTransform
public class Interactable extends UIRenderer implements PointerHandlerHover, PointerHandlerMouse
{
    /**
     * Whether this interactable can be interacted with<br/>
     * This includes: hovering and clicking
     */
    public boolean interactable = true;
    /**
     * Whether this interactable is disabled<br/>
     * Disabled interactables cannot be hovered or clicked
     */
    protected boolean disabled = false;

    /**
     * Which transition type to use
     * @see InteractableTransition
     */
    public InteractableTransition transition;


    // ============== transition color ==============
    /**
     * Which graphic to change (must be a UI component that renders. i.e.: Text or Image)
     */
    @ShowInInspector(key = "transition", enumValue = "COLOR")
    public UIRenderer targetGraphic;
    /**
     * The default color
     */
    @ShowInInspector(key = "transition", enumValue = "COLOR")
    public Color normalColor = new Color(255,255,255);
    /**
     * The hovering (mouse over) color
     */
    @ShowInInspector(key = "transition", enumValue = "COLOR")
    public Color hoverColor = new Color(179, 179, 179);
    /**
     * The click color
     */
    @ShowInInspector(key = "transition", enumValue = "COLOR")
    public Color pressedColor = new Color(107, 107, 107);
    /**
     * The disabled color
     */
    @ShowInInspector(key = "transition", enumValue = "COLOR")
    public Color disabledColor = new Color(48, 48, 48);


    // ============== transition sprite swap & animation ==============
    /**
     * The Image component to target when using {@link InteractableTransition#SPRITE_SWAP} or {@link InteractableTransition#ANIMATION}
     */
    @ShowInInspector(key = "transition", enumValue = "SPRITE_SWAP")
    @ShowInInspector(key = "transition", enumValue = "ANIMATION")
    public Image targetImage;


    // ============== transition sprite swap ==============
    /**
     * The default sprite
     */
    @ShowInInspector(key = "transition", enumValue = "SPRITE_SWAP")
    public SpriteAsset normalSprite;
    /**
     * The hover (mouse over) sprite
     */
    @ShowInInspector(key = "transition", enumValue = "SPRITE_SWAP")
    public SpriteAsset hoverSprite;
    /**
     * The click sprite
     */
    @ShowInInspector(key = "transition", enumValue = "SPRITE_SWAP")
    public SpriteAsset pressedSprite;
    /**
     * The disabled sprite
     */
    @ShowInInspector(key = "transition", enumValue = "SPRITE_SWAP")
    public SpriteAsset disabledSprite;


    // ============== transition animation ==============
    /**
     * The default animation
     */
    @ShowInInspector(key = "transition", enumValue = "ANIMATION")
    public String normalAnimation = Animator.DEFAULT_ID;
    /**
     * The hover (mouse over) animation
     */
    @ShowInInspector(key = "transition", enumValue = "ANIMATION")
    public String hoverAnimation = Animator.DEFAULT_ID;
    /**
     * The click animation
     */
    @ShowInInspector(key = "transition", enumValue = "ANIMATION")
    public String pressedAnimation = Animator.DEFAULT_ID;
    /**
     * The disabled animation
     */
    @ShowInInspector(key = "transition", enumValue = "ANIMATION")
    public String disabledAnimation = Animator.DEFAULT_ID;


    /**
     * The cursor used when hovering over the interactable
     */
    public Cursor hoverCursor = Cursor.HAND;
    /**
     * The cursor to use when the interactable is pressed
     */
    public Cursor pressCursor = Cursor.HAND;

    /**
     * The cursor to use when hovering over the interactable, but it's set to disabled
     */
    public Cursor disabledHoverCursor = Cursor.ARROW;

    /**
     * How long should the colour change or sprite stay changed to the <i>pressed</i> status<br/>
     * <i>Defaulted to 0.1; expressed in seconds</i>
     */
    public float clickDuration = .1f;

    /**
     * Stores the routine of transitioning back to normal/hover after clicking
     */
    private Routine resetClick = null;

    /**
     * The animator component that is either provided with the game object or is dynamically created by the interactable component,
     * if the transition is of type Animation.<br/>
     * <i>Note: the animator is retrieved once at the start of this component. Removing it and adding another one will not
     * work!</i>
     */
    private Animator animator = null;


    /**
     * Creates a blank Interactable base
     */
    protected Interactable() { }

    /**
     * Creates an interactable given the transition type
     * @param transition the transition type to use
     */
    protected Interactable(InteractableTransition transition) {
        this.transition = transition;
    }

    @Override
    public void start() {
        // in case the default sprite was left out
        if (transition == InteractableTransition.SPRITE_SWAP && normalSprite == null && targetImage != null)
            normalSprite = (SpriteAsset) targetImage.sprite.clone(); // also clone it in case we change it, we don't want to change the ref

        // for animation transition type, check if we have a target image
        if (transition == InteractableTransition.ANIMATION && targetImage != null) {
            this.animator = targetImage.getComponentOfType(Animator.class);

            // if we have at least one animation on the target image, but no animator, make one
            if (animator == null) {
                this.animator = new Animator(normalAnimation);
                targetImage.gameObject.addComponent(animator);
            }
            else {
                // set default animation to normalAnimation in the animator
                animator.defaultAnimation = normalAnimation;
                // in order to play immediately, we can't know if the animator already started playing the default animation,
                // so we restart it
                animator.play();
            }
        }
    }

    /**
     * Whether this interactable is disabled
     */
    @LinkFieldInInspector(name = "disabled", type = LinkType.GETTER, state = LinkState.BOTH)
    public boolean isDisabled() {
        return this.disabled;
    }

    /**
     * Sets this interactable to disabled, transitioning accordingly
     * @param disabled whether this is disabled or not
     */
    @LinkFieldInInspector(name = "disabled", type = LinkType.SETTER, state = LinkState.BOTH)
    public void setDisabled(boolean disabled) {
        if (this.resetClick != null)
            // stop the routine so that it doesn't interrupt the transition to/from disabled
            this.resetClick.stop();

        this.disabled = disabled;

        if (!started) return;

        if (disabled) {
            if (isHovering())
                ctx().setCursor(disabledHoverCursor);

            transition(disabledColor, disabledSprite, disabledAnimation);
        }
        else {
            if (isHovering())
                ctx().setCursor(hoverCursor);

            transitionReset();
        }
    }

    @Override
    public boolean checkOverlap(Vector2 mousePos) {
        Vector2 worldPos = this.gameObject.scene.getCamera().screenToWorldPosition(mousePos);
        return rectTransform().checkOverlap(worldPos, this.pivot);
    }

    /**
     * Whether the mouse is hovering over the component
     */
    private boolean isHovering = false;

    @Override
    public void setHovering(boolean hovering) {
        this.isHovering = hovering;
    }

    @Override
    public boolean isHovering() {
        return isHovering;
    }

    @Override
    public void mouseHoverEnter(MouseEvent event) {
        if (disabled && interactable)
            ctx().setCursor(disabledHoverCursor);

        if (disabled || !interactable) return;

        transition(hoverColor, hoverSprite, hoverAnimation);
        ctx().setCursor(hoverCursor);
    }

    @Override
    public void mouseHoverExit(MouseEvent event) {
        if (interactable)
            ctx().resetCursor();

        if (disabled || !interactable) return;

        transition(normalColor, normalSprite, normalAnimation);
    }

    @Override
    public void mouseClick(MouseEvent event) {
        if (disabled || !interactable) return;

        // change graphics to clicked
        transition(pressedColor, pressedSprite, pressedAnimation);
        ctx().setCursor(pressCursor);

        // set up a routine to change them back after clickDuration seconds
        resetClick = gameObject.runLater(this.clickDuration, () -> {
            transitionReset();
            if (isHovering())
                ctx().setCursor(hoverCursor);
            else
                ctx().resetCursor();
        });
    }

    private void transition(Color color, SpriteAsset sprite, String animation) {
        switch (transition) {
            case COLOR -> {
                if (targetGraphic == null) return;
                targetGraphic.color = color;
            }
            case SPRITE_SWAP -> {
                if (targetImage == null) return;
                targetImage.sprite = sprite;
            }
            case ANIMATION -> {
                if (targetImage == null || animator == null) return;
                animator.play(animation);
            }
        }
    }

    /**
     * Resets the interactable according to the transition, keeping in mind if this is being hovered
     */
    private void transitionReset() {
        this.transition(
                isHovering() ? hoverColor : normalColor,
                isHovering() ? hoverSprite : normalSprite,
                isHovering() ? hoverAnimation : normalAnimation
        );
    }

    // unused
    @Override
    public void mouseHover(MouseEvent event) { }
    @Override
    public void mouseScroll(MouseEvent event) { }
    @Override
    public void mouseDown(MouseEvent event) { }
    @Override
    public void mouseUp(MouseEvent event) { }
}

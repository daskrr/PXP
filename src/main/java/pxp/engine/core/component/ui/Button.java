package pxp.engine.core.component.ui;

import pxp.annotations.RequiresComponent;
import pxp.annotations.RequiresRectTransform;
import pxp.engine.data.event.PXPSingleEvent;
import pxp.engine.data.ui.InteractableTransition;
import processing.event.MouseEvent;

/**
 * The Button Component is used to interact with UI elements and provides a plethora of possibilities for creating GUIs
 */
@RequiresRectTransform
@RequiresComponent(UIRenderer.class)
public class Button extends Interactable
{
    /**
     * The callback event when there is a click on this button
     */
    public PXPSingleEvent<MouseEvent> onClick = new PXPSingleEvent<>() { @Override public void invoke(MouseEvent event) { } };

    /**
     * Creates a blank Button
     */
    public Button() { }

    /**
     * Creates a Button given the transition type
     * @param transition the transition type to use
     */
    public Button(InteractableTransition transition) {
        super(transition);
    }

    @Override
    public void mouseClick(MouseEvent event) {
        super.mouseClick(event);

        // propagate click
        onClick.invoke(event);
    }

}

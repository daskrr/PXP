package pxp.engine.core.component.ui;

import pxp.annotations.LinkFieldInInspector;
import pxp.annotations.LinkState;
import pxp.annotations.LinkType;
import pxp.engine.core.RectTransform;
import pxp.engine.data.Pivot;
import pxp.engine.data.Vector2;
import pxp.engine.data.assets.AssetManager;
import pxp.engine.data.assets.FontAsset;
import pxp.engine.data.assets.FontStyle;

/**
 * Text is a UI element Component which renders text in a Canvas.
 */
public class Text extends UIRenderer
{
    /**
     * The text to display
     */
    private String text = "";
    /**
     * The text font asset
     */
    public FontAsset font = AssetManager.getDefaultFont();
    /**
     * The font style
     */
    public FontStyle fontStyle = FontStyle.NORMAL;
    /**
     * The custom font style (in case the font has added styles outside of {@link FontStyle})
     */
    public int customStyle = -1;
    /**
     * The font size (in pixels)
     */
    public float fontSize = 14f;
    /**
     * The space between lines (1 = auto)<br/>
     * <i>Note: This is calculated based on the font size:</i><br/>
     * <code>(fontSize &times; 1.2) &times; lineSpacing</code>
     */
    public float lineSpacing = 1f;

    /**
     * The calculated size (width & height) of the text
     */
    private Vector2 textSize = new Vector2(-1f, -1f);


    /**
     * Creates a blank Text component
     */
    public Text() { }
    /**
     * Creates a Text component given the text to display
     * @param text the text to display
     */
    public Text(String text) {
        this.text = text;
    }
    /**
     * Creates a Text component given the text to display and a pivot
     * @param text the text to display
     * @param pivot the pivot of the text
     */
    public Text(String text, Pivot pivot) {
        this.text = text;
        this.pivot = pivot;
    }

    @Override
    public void render() {
        super.render();

        // check if game object has a RectTransform & the size changed
        if (gameObject.transform instanceof RectTransform && rectTransform().size.x != -1f && rectTransform().size.x != textSize.x) {
            // size changed, change text width to that of the rect transform
            textSize.x = rectTransform().size.x;
            // re-calculate the height
            calcTextHeight();

            if (textSize.y > rectTransform().size.y)
                textSize.y = rectTransform().size.y;
        }

        // configure everything
        setTextProperties();

        // calculate pivot
        if (textSize.x == -1f)
            calcTextWidth();
        // expensive calculation, that's why we have 2 methods
        if (textSize.y == -1f)
            calcTextHeight();

        // this is defaulted to the auto sizes, of which width will be automatically set to rect size if there is a rect size
        Vector2 sizeForPivot = new Vector2(textSize.x, textSize.y);
        // set the rect size (if any) to give to the pivot
        if (gameObject.transform instanceof RectTransform && rectTransform().size.y != -1f)
            sizeForPivot.y = rectTransform().size.y;

        // calculate pivot
        Vector2 pivot = this.pivot.calc.apply(sizeForPivot);

        // render text
        // push translation for pivot
        // add width and height, whether they are calculated automatically based on text size or the game object has a
        // rect transform which will limit the text
        // the +1 is for the automatic version, as the size is calculated and the text is cut off due to it being the same width/height
        ctx().text(text, pivot.x, pivot.y, textSize.x + 1, textSize.y + 1);
    }

    @LinkFieldInInspector(name = "text", type = LinkType.SETTER, state = LinkState.BOTH)
    public void setText(String text) {
        this.text = text;
        if (started)
            calcTextHeight();
    }
    @LinkFieldInInspector(name = "text", type = LinkType.GETTER, state = LinkState.BOTH)
    public String getText() {
        return this.text;
    }

    /**
     * Calculates the line spacing based on the font size with the size included
     * @return the calculated leading (with the line spacing)
     */
    private float calcLeading() {
        return lineSpacing * Math.round(this.fontSize * 1.2f);
    }

    /**
     * Through the current processing render context, set all properties of this Text component
     */
    private void setTextProperties() {
        // set font
        if (customStyle >= 0)
            ctx().textFont(this.font.getPFont(customStyle));
        else
            ctx().textFont(this.font.getPFont(fontStyle));

        // set size & leading (line spacing)
        ctx().textSize(this.fontSize);
        ctx().textLeading(calcLeading());
    }

    /**
     * This is merely a fallback and shouldn't really be used as we take the size from the rect transform
     */
    private void calcTextWidth() {
        if (ctx() == null) return;

        setTextProperties();

        // calculate the text size
        this.textSize.x = ctx().textWidth(this.text);
    }

    private void calcTextHeight() {
        if (ctx() == null) return;

        if (textSize.x == -1f)
            calcTextWidth();
        else
            // set them in an else, as they are already set if the statement was true
            setTextProperties();

        // split by new lines first
        String[] paragraphs = this.text.split("\n");
        int numberEmptyLines = 0;
        int numTextLines = 0;

        for (String paragraph : paragraphs)
            // anything with length 0 ignore and increment empty line count
            if (paragraph.length() == 0)
                numberEmptyLines++;
            else {
                numTextLines++;

                // word wrap
                String[] wordsArray = paragraph.split(" ");
                StringBuilder tempString = new StringBuilder();

                for (String s : wordsArray)
                    if (ctx().textWidth(tempString + s) <= this.textSize.x)
                        tempString.append(s).append(" ");
                    else {
                        tempString = new StringBuilder(s + " ");
                        numTextLines++;
                    }
            }

        float totalLines = numTextLines + numberEmptyLines;
        this.textSize.y = Math.round(totalLines * calcLeading());
    }
}

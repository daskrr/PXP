package pxp.engine.core.manager;

import pxp.engine.core.component.ui.Canvas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The Layer Manager stores collision and sorting layers and provides helper methods
 */
public class LayerManager
{
    public static List<String> layers;
    public static List<String> sortingLayers;

    /**
     * [Internal] initializes the LayerManager given the layers from settings<br/>
     * It makes sure that "Default" is present in both collision and sorting layers
     */
    public static void initialize(String[] layersSet, String[] sortingLayersSet) {
        // check if layers' size is bigger than 32 (meaning that it exceeds the bitmask)
        if (layersSet.length > 32)
            throw new IndexOutOfBoundsException("Maximum amount of layers exceeded. Up to 32 layers are possible.");

        layers = new ArrayList<>(List.of(layersSet));
        sortingLayers = new ArrayList<>(List.of(sortingLayersSet));

        // check if "Default" exists for both sets of layers
        if (!layers.contains("Default"))
            if (layers.size() == 0)
                layers.add("Default");
            else
                layers.add(0, "Default");

        if (!sortingLayers.contains("Default"))
            if (sortingLayers.size() == 0)
                sortingLayers.add("Default");
            else
                sortingLayers.add(0, "Default");

        // also add the canvas layer
        sortingLayers.add(Canvas.CANVAS_LAYER);

        layers = Collections.unmodifiableList(layers);
        sortingLayers = Collections.unmodifiableList(sortingLayers);
    }

    public static int getLayerId(String name) {
        return layers.indexOf(name);
    }

    public static String getLayerName(int id) {
        if (id < layers.size())
            return layers.get(id);

        throw new IndexOutOfBoundsException("The id is out of bounds!");
    }

    public static int getSortingLayerId(String name) {
        return sortingLayers.indexOf(name);
    }

    public static String getSortingLayerName(int id) {
        if (id < sortingLayers.size())
            return sortingLayers.get(id);

        throw new IndexOutOfBoundsException("The id is out of bounds!");
    }
}

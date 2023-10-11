package pxp.engine.data;

import pxp.engine.core.manager.LayerManager;

import java.util.ArrayList;
import java.util.List;

/**
 * The layer mask is used to combine up to 32 layers into one "mask".
 */
public class LayerMask
{
    public int mask;

    public boolean hasLayer(int layer) {
        return this.mask == (this.mask | 1 << layer);
    }

    public boolean hasLayer(String layer) {
        return this.mask == (this.mask | 1 << nameToId(layer));
    }

    public Integer[] getLayerIds() {
        List<Integer> ids = new ArrayList<>();

        for (int i = 0; i < 32; i++)
            if (this.mask == (this.mask | (1 << i)))
                ids.add(i);

        return ids.toArray(new Integer[0]);
    }

    public String[] getLayerNames() {
        List<String> layers = new ArrayList<>();

        for (int i = 0; i < 32; i++)
            if (this.mask == (this.mask | (1 << i)))
                layers.add(idToName(i));

        return layers.toArray(new String[0]);
    }

    public static String idToName(int layer) {
        return LayerManager.getLayerName(layer);
    }

    public static int nameToId(String name) {
        return LayerManager.getLayerId(name);
    }

    public static int create(String... names) {
        if (names == null) throw new IllegalArgumentException("names is null");

        int mask = 0;
        for (String name : names) {
            int layerId = nameToId(name);

            if (layerId != -1)
                mask |= 1 << layerId;
        }

        return mask;
    }
}

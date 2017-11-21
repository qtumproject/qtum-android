package org.qtum.wallet.utils;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

public class FontManager {
    private static FontManager instance;
    private AssetManager mgr;
    private Map<String, Typeface> fonts;

    private FontManager(AssetManager _mgr) {
        mgr = _mgr;
        fonts = new HashMap<>();
    }

    public static void init(AssetManager mgr) {
        instance = new FontManager(mgr);
    }

    public static FontManager getInstance() {
        return instance;
    }

    public Typeface getFont(String asset) {
        if (fonts.containsKey(asset))
            return fonts.get(asset);

        Typeface font = null;

        try {
            font = Typeface.createFromAsset(mgr, asset);
            fonts.put(asset, font);
        } catch (Exception ignored) {
        }
        if (font == null) {
            try {
                String fixedAsset = fixAssetFilename(asset);
                font = Typeface.createFromAsset(mgr, fixedAsset);
                fonts.put(asset, font);
                fonts.put(fixedAsset, font);
            } catch (Exception e) {

            }
        }
        return font;
    }

    private String fixAssetFilename(String asset) {
        if (TextUtils.isEmpty(asset))
            return asset;
        if (!asset.endsWith(".ttf") && !asset.endsWith(".ttc") && !asset.endsWith(".otf"))
            asset = String.format("%s.ttf", asset);
        return asset;
    }
}

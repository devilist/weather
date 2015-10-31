package com.weather.app.util;

import android.content.Context;
import android.graphics.Typeface;

import java.lang.reflect.Field;

/**
 * Created by zengpu on 15/11/12.
 */
public final class OverrideFonts {

    protected static void replaceFont(Context context,String staticTypefaceFieldName,
                                      String fontAssetName) {

         final Typeface regular = Typeface.createFromAsset(context.getAssets(),
                fontAssetName);
        try {
            final Field staticField = Typeface.class
                    .getDeclaredField(staticTypefaceFieldName);
            staticField.setAccessible(true);
            staticField.set(null, regular);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}

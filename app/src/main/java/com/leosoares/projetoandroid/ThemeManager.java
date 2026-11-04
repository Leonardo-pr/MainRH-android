package com.leosoares.projetoandroid;
import android.content.Context;
import android.content.SharedPreferences;

public class ThemeManager {
    private static final String PREFS_NAME = "theme_prefs";
    private static final String KEY_THEME = "selected_theme";

    // Constantes para os tipos de tema
    public static final int THEME_LIGHT = 0;
    public static final int THEME_DARK = 1;
    public static final int THEME_HIGH_CONTRAST = 2;
    public static final int THEME_COLORBLIND = 3;

    public static void saveTheme(Context context, int themeType) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putInt(KEY_THEME, themeType).apply();
    }

    public static int getSavedTheme(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(KEY_THEME, THEME_LIGHT); // Padrão como Claro
    }
}
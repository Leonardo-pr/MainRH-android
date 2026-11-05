package com.leosoares.projetoandroid;
import android.content.Context;
import android.content.SharedPreferences;

import android.content.Context;
import android.content.res.Configuration;
import android.content.SharedPreferences;

public class ThemeManager {
    public static final int THEME_LIGHT = 0;
    public static final int THEME_DARK = 1;
    public static final int THEME_HIGH_CONTRAST = 2;
    public static final int THEME_COLORBLIND = 3;

    private static final String PREF_NAME = "prefs_tema";
    private static final String KEY_TEMA = "tema_atual";

    public static int getSavedTheme(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        // Se o usuário nunca salvou um tema manualmente, verifica o modo do sistema
        if (!prefs.contains(KEY_TEMA)) {
            int nightModeFlags = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
                return THEME_DARK; // Sistema modo escuro = App inicia escuro
            } else {
                return THEME_LIGHT; // Sistema modo claro = App inicia claro
            }
        }

        return prefs.getInt(KEY_TEMA, THEME_LIGHT);
    }

    public static void saveTheme(Context context, int theme) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putInt(KEY_TEMA, theme).apply();
    }
}
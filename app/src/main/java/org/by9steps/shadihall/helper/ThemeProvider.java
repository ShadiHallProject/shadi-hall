package org.by9steps.shadihall.helper;

import android.app.Activity;

import org.by9steps.shadihall.R;

import static android.content.Context.MODE_PRIVATE;

public class ThemeProvider {
    public static final String THEME_lOC_KEY = "AppTheme";

    public static void setThemeOfApp(Activity activity) {
        //context.setTheme();
        int theme = activity.getPreferences(MODE_PRIVATE).getInt(THEME_lOC_KEY, 0);
        switch (theme) {
            case 0:
                activity.setTheme(R.style.AppTheme2);
                break;
            case 1:
                activity.setTheme(R.style.AppTheme2);
                break;
            case 2:
                activity.setTheme(R.style.AppTheme2);
                break;
        }
       // activity.recreate();
    }
}

package org.by9steps.shadihall.helper;

import android.content.Context;

import org.by9steps.shadihall.R;

public class GenericGridTheme {
    static class Theme1Blue implements GenericGridColorSchema {
        @Override
        public int getGridHeadRowColor(Context context) {
            return context.getResources().getColor(R.color.g_gridheadcolor);
        }

        @Override
        public int getGridRow1Color(Context context) {
            return context.getResources().getColor(R.color.g_gridrow1);
        }

        @Override
        public int getGridRow2Color(Context context) {
            return context.getResources().getColor(R.color.g_gridrow2);
        }

        @Override
        public int getGridContentColor(Context context) {
            return context.getResources().getColor(R.color.contenttextcolor);
        }

        @Override
        public int getGridBottomRowColor(Context context) {
            return context.getResources().getColor(R.color.g_gridbottomcolor);
        }

        @Override
        public int getGridBottomSecOfsecRowColor(Context context) {
            return context.getResources().getColor(R.color.g_gridbottomsec_of_sec_color);
        }

        @Override
        public int getGridContentHeaderColor(Context context) {
            return context.getResources().getColor(R.color.contentHeadertextcolor);
        }

    }


}


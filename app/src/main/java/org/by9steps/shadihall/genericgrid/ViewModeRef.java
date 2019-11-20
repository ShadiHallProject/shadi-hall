package org.by9steps.shadihall.genericgrid;

import android.util.Log;

public class ViewModeRef {
///////////////////////////////////Providing The width of each Column Width
    public static boolean isNumberCoumn[];
     public static int eachColWidth[];
    // public static boolean isColWidthCustom=false;
     public static final int MaxLimitForEachColWidth=400;
     public static final int MaxPxlEachCharTake=32;
    public static int MaxPxlEachCharTakeINSectionOFSec=400;
    // public static final int MaxPxlEachCharTake=41;
    ///////////////////////////////variable For Section Of Grid
    static String Ref_Column_Name_For_Sorting = "";
    ////////////////Index Position To sort the list
    public int ColumnIndexToSort = -1;
    boolean isSectionRow = false;
    String SectionName = "";
    //static int indexNoToSortItem=0;
    ///////////////////////////////
    String columns[];
    /////////////////ary To validate each row visibility on the grid
    boolean checkvisibility[];
    char sortingtype = 'A';
    /////////////////////////////
    boolean isviewed = false;
    boolean isheaderview = false;
    /////////////////////////////
    boolean isbottomrow = false;
    ////////////////////////
    boolean isSectionOfSectionView=false;


    public ViewModeRef(int rowsize) {
        this.columns = new String[rowsize];
        this.checkvisibility = new boolean[rowsize];
        eachColWidth=new int[rowsize];
        isNumberCoumn=new boolean[rowsize];
        for (int i = 0; i < columns.length; i++) {
            this.columns[i] = "";
            this.checkvisibility[i] = true;
            isNumberCoumn[i]=true;
            //////////////////////////////Characher Length
            eachColWidth[i]=0;
        }
    }

    public void showdata() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        for (int i = 0; i < columns.length; i++) {
            builder.append(columns[i] + "-");
        }
        builder.append("}");

        Log.e("object", "comobj:" + builder.toString());
    }


}

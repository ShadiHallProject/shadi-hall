package org.by9steps.shadihall.model;

import java.io.Serializable;

public class ItemLedger implements Serializable {



    //////////////////////////Change The list in to columns Names
    public String ClientIDcol,Datecol,ItemIDcol,EntryTypecol,BillNocol,AccountNameCol,RemarksCol,AddCol,LessCol,PriceCol,BalCol,UserCol,LocCol;




//////////////////////////////////////////////////////
    public static String columnNames[];
  public String columnsData[];
    public int isRow=0;
//////////////////////////////////For Total Row ////////////////////
    //////////////////Row No 1

    public void rowForSum(String lescol,String balcol,int indexof1,int indexof2){
        for (int i = 0; i <columnNames.length ; i++) {
            if(i==indexof1){
                columnsData[i]=lescol;
            }else if(i==indexof2){
                columnsData[i]=balcol;
            }else
            columnsData[i]="";
        }
        isRow=1;
    }
    public void rowForSumTransaction(String lescol,String balcol,int indexof1,int indexof2){
        for (int i = 0; i <columnNames.length ; i++) {
            if(i==indexof1){
                columnsData[i]=lescol;
            }else if(i==indexof2){
                columnsData[i]=balcol;
            }else
                columnsData[i]="";
        }
        isRow=2;
    }


    public ItemLedger(int colSze) {
        this.columnsData = new String[colSze];
    }

}

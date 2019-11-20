package org.by9steps.shadihall.genericgrid;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import org.by9steps.shadihall.chartofaccountdialog.CustomDialogOnDismisListener;
import org.by9steps.shadihall.helper.MNotificationClass;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

///////////////////////////30.7 Max Width of Each Character
//////////////////////////(400/13=30.7 aprox=31)
public class MediatorClass {

    ////////////////////////////////Listener For Search Dialog Click Listener
    DialogForSearchGrid.DialogClickListener dialogClickListener;

    private Cursor cc,cursorCloneForBackup;
    private RecyclerView recyclerView;
    private GenericGridAdapter adapterobj;
    private List<ViewModeRef> list;
    private boolean isSortingAllowed = false;

    public MediatorClass(Cursor cc, RecyclerView recyclerView) {
        this.cc = cc;
        cursorCloneForBackup=cc;
        this.recyclerView = recyclerView;
        this.recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        list = new ArrayList<>();
        ///////////////////////////Setting Column To Index Grouping
        if (cc.getColumnCount() > 2) {
            ViewModeRef.Ref_Column_Name_For_Sorting = cc.getColumnName(2);
            String columnsnames[] = cc.getColumnNames();
            for (int i = 0; i < columnsnames.length; i++) {
                //////////////////getting Coumns For Default Sorting Haviing _G in that
                if (columnsnames[i].contains("_G"))
                    ViewModeRef.Ref_Column_Name_For_Sorting = columnsnames[i];
            }
        } else {
            ViewModeRef.Ref_Column_Name_For_Sorting = cc.getColumnName(0);
        }
    }

    public void ShowGrid() {
        ViewModeRef helperclass;
        cc.moveToFirst();
        if (isSortingAllowed)
            setHeaderRowColumnNames(0, 'A');
        else
            setHeaderRowColumnNames(-1, 'A');

        for (int i = 0; i < cc.getCount(); i++) {
            helperclass = new ViewModeRef(cc.getColumnCount());
            for (int j = 0; j < cc.getColumnCount(); j++) {

                helperclass.columns[j] = cc.getString(j);

            }
            cc.moveToNext();
            helperclass.showdata();
            list.add(helperclass);
        }
        setBottomRowColumnNames();
        hideColumVisibility();
        if (!isSortingAllowed) {
//            try {
            addSectionView();

        }
        settingEachColumnWidth();
        adapterobj = new GenericGridAdapter(list, recyclerView.getContext());
        adapterobj.headrowClickListner=menuItemHeaderRowClickListner;
        if (!isSortingAllowed) {
            adapterobj.listenerForChange = listenGrouping;
        }
        recyclerView.setAdapter(adapterobj);
    }

    private void setHeaderRowColumnNames(int indexToSort, char sortyType) {
        Log.e("headrowcol", "colIndex" + indexToSort);
        ViewModeRef helperclass = new ViewModeRef(cc.getColumnCount());
        for (int i = 0; i < cc.getColumnCount(); i++) {

            helperclass.columns[i] = cc.getColumnName(i);
        }
        helperclass.ColumnIndexToSort = indexToSort;
        helperclass.sortingtype = sortyType;
        helperclass.isheaderview = true;
        list.add(helperclass);

    }

    public void listenForSortClick(final GenericGridAdapter.ListenerForChange listener) {
        if (isSortingAllowed)
            adapterobj.listenerForChange = listener;
    }

    ///////////////////////////Send All Parament
    public void FilterList(Cursor ccobj, int ColumnIndexToSort, char sorttype) {
        if (adapterobj != null && recyclerView != null) {
            list.clear();

            ViewModeRef helperclass;

            ccobj.moveToFirst();
            setHeaderRowColumnNames(ColumnIndexToSort, sorttype);
            for (int i = 0; i < ccobj.getCount(); i++) {
                helperclass = new ViewModeRef(ccobj.getColumnCount());

                for (int j = 0; j < ccobj.getColumnCount(); j++) {
//                    if(j==1)
//                        ViewModeRef.columns[j]="pos:"+i;
//                    else
                    helperclass.columns[j] = ccobj.getString(j);

                }
                ccobj.moveToNext();
                helperclass.showdata();
                list.add(helperclass);

            }
            setBottomRowColumnNames();
            settingEachColumnWidth();
//            if (!isSortingAllowed)
//                addSectionView();
            recyclerView.getAdapter().notifyDataSetChanged();
//            for (int i = 0; i < list.size(); i++) {
//                ViewModeRef obj = list.get(i);
//                //Log.e("observe","itemNo:("+i+")"+obj.columns[0]);
//            }


        } else {
            Toast.makeText(recyclerView.getContext(), "Show Grid First", Toast.LENGTH_SHORT).show();
        }
        hideColumVisibility();
    }

    private void setBottomRowColumnNames() {

        ViewModeRef helperclass = new ViewModeRef(cc.getColumnCount());
        cc.moveToFirst();
        helperclass = new ViewModeRef(cc.getColumnCount());
        for (int i = 0; i < cc.getCount(); i++) {
            for (int j = 0; j < cc.getColumnCount(); j++) {
                String cname = cc.getColumnName(j);
                if (cname.contains("_")) {
                    String ary[] = cname.split("_");
                    if (ary[1].equals("S")) {
                        try {
                            int s1 = Integer.parseInt(cc.getString(j));
                            int st = Integer.parseInt("0" + helperclass.columns[j]);
                            st += s1;
                            //   Log.e("obstotala",j+")"+st+"--"+s1);
                            helperclass.columns[j] = st + "";
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (ary[1].equals("C")) {
                        helperclass.columns[j] = (i + 1) + "";
                    }
                }
            }
            cc.moveToNext();
            // ViewModeRef.showdata();
            //  list.add(ViewModeRef);
        }
        helperclass.isbottomrow = true;
        list.add(helperclass);
    }

    public void hideColumVisibility() {
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < cc.getColumnCount(); j++) {
                String cname = cc.getColumnName(j);
                if (cname.contains("_")) {
                    String ary[] = cname.split("_");
                    if (ary[1].equals("H")) {
                        list.get(i).checkvisibility[j] = false;
                    } else if (ary[1].equals("G")) {
                        list.get(i).checkvisibility[j] = false;
                    }
                }
            }
        }

    }

    public List<ViewModeRef> addSectionView() {
        Log.e("addSectView","addSectionView()  CAlls");
        //Log.e("helloaa","CAlled");
        String sorttype = ViewModeRef.Ref_Column_Name_For_Sorting;
        int index = cc.getColumnIndex(sorttype);
        List<ViewModeRef> reflist = list.subList(1, list.size() - 1);
//        cc.moveToFirst();
        ViewModeRef firstRow = list.get(0);
        ViewModeRef endrow = list.get(list.size() - 1);
        list = new ArrayList<>();

        HashSet<String> hashSet = new HashSet<>();
        ////////////////////////Finding Unique Item in The List For Grouping
        for (int i = 0; i < reflist.size(); i++) {
            if (reflist.get(i).columns[index] != null && reflist.get(i).columns[index] != "")
                hashSet.add(reflist.get(i).columns[index]);
        }
        /////////////////////////////////////////////////
        ViewModeRef sectionref;
        int counter = 0;
        ///////////////////////////Adding Frist Row To New List
        list.add(firstRow);
        ////////////////Creating uper and lower bound for section
        int star = 0, end = 0;
        //////////////////////Adding Other Items
        for (String ss : hashSet) {
            sectionref = new ViewModeRef(cc.getColumnCount());
            Log.e("setitem", counter + "=>" + ss);
            counter++;
            sectionref.isSectionRow = true;
            sectionref.SectionName = ss + "";
            list.add(sectionref);
            star = list.size();
            end = 0;
            List<ViewModeRef> minilist = new ArrayList<>();
            for (int i = 0; i < reflist.size(); i++) {
                if (reflist.get(i).columns[index].equals(ss)) {
                    list.add(reflist.get(i));
                    minilist.add(reflist.get(i));
                    end++;
                }
            }
            if (minilist.size() > 0) {
                Log.e("mimilistsize", "TotalSize:" + minilist.size());
                AddSectionViseTotalRow(minilist);
            }
            // setBottomRowColumnNames();
        }
        ////////////////////////Addding Lat Row to New List
        list.add(endrow);
        hideColumVisibility();
        return list;
    }

    public void reisterItemMenuClickListner(GenericGridAdapter.MenuItemClickListner listner) {
        adapterobj.menuItemClickListner = listner;
    }

    public void registerHeaderRowMenuClickListner(DialogForSearchGrid.DialogClickListener dialogClickListener1) {
        dialogClickListener=dialogClickListener1;
    }

    public void setSortingAllowed(boolean sortingAllowed) {
        isSortingAllowed = sortingAllowed;
    }

    public boolean isSortingAllowed() {
        return isSortingAllowed;
    }

    /////////////////////////////Custom Listener Object For Group Listener
    GenericGridAdapter.ListenerForChange listenGrouping = new GenericGridAdapter.ListenerForChange() {
        @Override
        public void listenForSortClick(String columnName, int index, char sorttype) {
            //Toast.makeText(recyclerView.getContext(), "=>" + columnName, Toast.LENGTH_SHORT).show();
//            if (!isSortingAllowed) {
//                ViewModeRef.Ref_Column_Name_For_Sorting = columnName;
//
//                addSectionView();
//
//                adapterobj.setList(list);
//                adapterobj.notifyDataSetChanged();
//            }
        }

    };

    public void AddSectionViseTotalRow(List<ViewModeRef> list1) {

        Log.e("litmetiofeach", "--Tsize--" + list1.size());
        cc.moveToFirst();
        ViewModeRef helperclass = new ViewModeRef(cc.getColumnCount());

        //helperclass = new ViewModeRef(cc.getColumnCount());
        //Log.e("obstotala","pass----------------");
        boolean isContentQuailfied = false;
        for (int i = 0; i < list1.size(); i++) {
            for (int j = 0; j < cc.getColumnCount(); j++) {
                String cname = cc.getColumnName(j);
                if (cname.contains("_") && !list1.get(i).isSectionRow && !list1.get(i).isbottomrow
                        && !list1.get(i).isheaderview) {
                    String ary[] = cname.split("_");
                    isContentQuailfied = true;
                    if (ary[1].equals("S")) {
                        try {
                            int s1 = Integer.parseInt(list1.get(i).columns[j]);
                            int st = Integer.parseInt("0" + helperclass.columns[j]);
                            st += s1;
                            Log.e("obstotala", j + ")" + st + "--" + s1 + " ColName" + cc.getColumnName(j));
                            helperclass.columns[j] = st + "";
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (ary[1].equals("C")) {
                        helperclass.columns[j] = (i + 1) + "";
                    }
                }
            }
            //isContentQuailfied=true;
            cc.moveToNext();
            // ViewModeRef.showdata();
            //  list.add(ViewModeRef);
        }
        // helperclass.isbottomrow = true;
        helperclass.isSectionOfSectionView = true;
        if (isContentQuailfied)
            list.add(helperclass);
    }


    public void settingEachColumnWidth() {


        Log.e("TotalCount", cc.getCount() + "-----" + cc.getColumnCount());
        cc.moveToFirst();
        do {
            for (int i = 0; i < cc.getColumnCount() && cc.getCount() > 0; i++) {
                int len = (cc.getString(i) + "").length();
                if (len > ViewModeRef.eachColWidth[i]) {
                    ViewModeRef.eachColWidth[i] = len;
                }
                ///////////////////////////Cehcking For number
                if (!TextUtils.isDigitsOnly(cc.getString(i) + "")) {
                    ViewModeRef.isNumberCoumn[i] = false;
                }
            }

        } while (cc.moveToNext());
        for (int i = 0; i < cc.getColumnCount(); i++) {
            int len = (cc.getColumnName(i)).length();
            if (len > ViewModeRef.eachColWidth[i]) {
                ViewModeRef.eachColWidth[i] = len;
            }
        }
        ///////////////////Adding Extra Width Fro Firest Column Due To Three Dot Menu
        if (ViewModeRef.eachColWidth.length > 0) {
            ViewModeRef.eachColWidth[0] += 1;
        }
        for (int i = 0; i < ViewModeRef.eachColWidth.length; i++) {
            Log.e("widthOFCol", "isColNum(" + ViewModeRef.isNumberCoumn[i] + ")ColNo" + i + ") Width :" + ViewModeRef.eachColWidth[i] * ViewModeRef.MaxPxlEachCharTake);
        }
    }

    private GenericGridAdapter.MenuItemHeaderRowClickListner menuItemHeaderRowClickListner=new GenericGridAdapter.MenuItemHeaderRowClickListner() {
        @Override
        public void ListenForDotMenuItemClick(MenuItem menuItem) {
            if(menuItem.getTitle().toString().equals("ClearFilter")){
                cc=cursorCloneForBackup;
                list.clear();
                ShowGridAgaiFromBackup();
                Log.e("checck23",isSortingAllowed+" <->");
            }else{
                DialogForSearchGrid dialogForSearchGrid=new DialogForSearchGrid();
                try {
                    FragmentActivity activity=(FragmentActivity) recyclerView.getContext();
                    ArrayList<String> arrayList=new ArrayList<>();
                    for (int i = 0; i <cc.getColumnCount() ; i++) {
                        arrayList.add(cc.getColumnName(i));
                    }
                    Bundle bb=new Bundle();
                    bb.putStringArrayList("columnarray",arrayList);
                    dialogForSearchGrid.setArguments(bb);
                    dialogForSearchGrid.show(activity.getSupportFragmentManager(),"Filter Grid");
                    if(dialogClickListener!=null){
                        dialogForSearchGrid.listener=dialogClickListener;
                    }
//                dialogForSearchGrid.listener=new DialogForSearchGrid.DialogClickListener() {
//                    @Override
//                    public void HandleClickLisnterOfDialog(String colName, String SearchText) {
//
//                    }
//                };
                }catch (Exception e){
                    e.printStackTrace();
                }
            }


        }
    };

    private void ShowGridAgaiFromBackup() {
        ViewModeRef helperclass;
        cc.moveToFirst();
        if (isSortingAllowed)
            setHeaderRowColumnNames(0, 'A');
        else
            setHeaderRowColumnNames(-1, 'A');

        for (int i = 0; i < cc.getCount(); i++) {
            helperclass = new ViewModeRef(cc.getColumnCount());
            for (int j = 0; j < cc.getColumnCount(); j++) {

                helperclass.columns[j] = cc.getString(j);

            }
            cc.moveToNext();
         //   helperclass.showdata();
            list.add(helperclass);
        }
        setBottomRowColumnNames();
        hideColumVisibility();
        if (!isSortingAllowed) {
//            try {
        adapterobj.list=addSectionView();

        }
        settingEachColumnWidth();
        //adapterobj = new GenericGridAdapter(list, recyclerView.getContext());
       // adapterobj.headrowClickListner=menuItemHeaderRowClickListner;
//        if (!isSortingAllowed) {
//            adapterobj.listenerForChange = listenGrouping;
//        }
        adapterobj.notifyDataSetChanged();
        //recyclerView.setAdapter(adapterobj);
    }

    ///////////////////////////Send All Parament
    public void FilterListByEnterText(Cursor cco) {
        ViewModeRef helperclass;
        cc=cco;
        list.clear();
        if (adapterobj != null && recyclerView != null) {
            cc.moveToFirst();
            if (isSortingAllowed)
                setHeaderRowColumnNames(0, 'A');
            else
                setHeaderRowColumnNames(-1, 'A');

            for (int i = 0; i < cc.getCount(); i++) {
                helperclass = new ViewModeRef(cc.getColumnCount());
                for (int j = 0; j < cc.getColumnCount(); j++) {
                    helperclass.columns[j] = cc.getString(j);
                }
                cc.moveToNext();
               // helperclass.showdata();
                list.add(helperclass);
            }
            setBottomRowColumnNames();
            hideColumVisibility();
            if (!isSortingAllowed) {

                addSectionView();
            }
            settingEachColumnWidth();
        } else {
            Toast.makeText(recyclerView.getContext(), "Show Grid First", Toast.LENGTH_SHORT).show();
        }
        hideColumVisibility();
        adapterobj.list=list;
        adapterobj.notifyDataSetChanged();
    }
}

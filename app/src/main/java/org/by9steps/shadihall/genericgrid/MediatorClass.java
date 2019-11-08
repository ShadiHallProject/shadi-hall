package org.by9steps.shadihall.genericgrid;

import android.database.Cursor;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MediatorClass {

    private Cursor cc;
    private RecyclerView recyclerView;
    private GenericGridAdapter adapterobj;
    private List<ViewModeRef> list;
    private boolean isSortingAllowed = false;

    public MediatorClass(Cursor cc, RecyclerView recyclerView) {
        this.cc = cc;
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
        setHeaderRowColumnNames(0, 'A');

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

        adapterobj = new GenericGridAdapter(list, recyclerView.getContext());
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
        for (String ss : hashSet) {
            sectionref = new ViewModeRef(cc.getColumnCount());
            Log.e("setitem", counter + "=>" + ss);
            counter++;
            sectionref.isSectionRow = true;
            sectionref.SectionName = ss + "";
            list.add(sectionref);
            for (int i = 0; i < reflist.size(); i++) {
                if (reflist.get(i).columns[index].equals(ss)) {
                    list.add(reflist.get(i));
                }
            }
            // setBottomRowColumnNames();
        }
        ////////////////////////Addding Lat Row to New List
        list.add(endrow);
        return list;
    }

    public void reisterItemMenuClickListner(GenericGridAdapter.MenuItemClickListner listner) {
        adapterobj.menuItemClickListner = listner;
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
            if (!isSortingAllowed) {
                ViewModeRef.Ref_Column_Name_For_Sorting = columnName;
//                try {
                addSectionView();
//                }catch (Exception e)
//                {
//                    e.printStackTrace();
//                }
                adapterobj.setList(list);
                adapterobj.notifyDataSetChanged();
            }
        }

    };

}

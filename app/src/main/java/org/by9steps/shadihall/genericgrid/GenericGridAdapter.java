package org.by9steps.shadihall.genericgrid;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import org.by9steps.shadihall.R;

import java.util.List;

public class GenericGridAdapter extends RecyclerView.Adapter {

    ///////////////////////////Defining Width Of Each Column TextView

    /////////////////////////////////////

    //////////////////////Each Text View Padding
    public static int txtpadl = 0, txtpadt = 0, txtpadr = 40, txtpadb = 0;
    List<ViewModeRef> list;
    //List<ViewModeRef> filterlist;
    Context context;
    ListenerForChange listenerForChange;
    ////////////////Text View Width Pixel
    int textviewdimWidth = 300;
    ////////////////////////////For item or menu click listner
    MenuItemClickListner menuItemClickListner;
////////////////////////////////For header Row Click Listern
    public MenuItemHeaderRowClickListner headrowClickListner;
    public void setList(List<ViewModeRef> list) {
        this.list = list;
    }

    ////////////////////////////interfaceForListning Changes
    public interface ListenerForChange {
        void listenForSortClick(String columnName, int index, char sorttype);
    }

    //////////////////////For Menu and Item Click listner
    public interface MenuItemClickListner {

        void ListenForGridItemClick(ViewModeRef refobj, int index);

        void ListenForDotMenuItemClick(ViewModeRef refobj, int index, MenuItem menuItem);
    }

    ////////////////////////////////////////////////////////////Header Row Thre Dot menu Click
    public interface MenuItemHeaderRowClickListner {

        void ListenForDotMenuItemClick(MenuItem menuItem);
    }

    public GenericGridAdapter(List<ViewModeRef> list, Context context) {
        this.list = list;
        this.context = context;
        // textviewdimWidth=  context.getResources().getDimension(R.dimen.gridtextviewdim);
        //filterlist = list.subList(1, list.size());
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        if (viewType == 0) {
            View vv = LayoutInflater.from(parent.getContext()).inflate(R.layout.headrow, parent, false);

            return new HeaderRowView(vv);
        } else if (viewType == -1) {
            View vv = LayoutInflater.from(parent.getContext()).inflate(R.layout.gridbottomrow, parent, false);

            return new BottomRowView(vv);
        } else if (viewType == -2) {
            View vv = LayoutInflater.from(parent.getContext()).inflate(R.layout.gridsectionview, parent, false);

            return new SectionRowView(vv);
        } else if (viewType == -3) {
            View vv = LayoutInflater.from(parent.getContext()).inflate(R.layout.gridbottomsec_of_sec_row, parent, false);

            return new BottomRowSectionOfSectionView(vv);
        } else {
            View vv = LayoutInflater.from(parent.getContext()).inflate(R.layout.contentrow, parent, false);

            return new ContentHolder(vv);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        TextView textView;

        if (list.get(position).isheaderview) {
            HeaderRowView headrow = (HeaderRowView) holder;
            headrow.linearLayout.removeAllViews();
            headrow.textviewtext = new String[list.get(position).columns.length];
            ImageView imageView = new ImageView(headrow.rootview.getContext());
            imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_more_vert_black_24dp));
            imageView.setOnClickListener(headrow.dotmenulistner);
            headrow.linearLayout.addView(imageView);
            for (int i = 0; i < list.get(position).columns.length; i++) {
                textView = new TextView(headrow.rootview.getContext());
                textView.setId(i);
                if (list.get(position).columns[i].contains("_")) {
                    String cnameref[] = list.get(position).columns[i].split("_");
                    textView.setText(cnameref[0]);
                } else
                    textView.setText(list.get(position).columns[i]);
                //textView.setText(list.get(position).columns[i]);
                headrow.textviewtext[i] = list.get(position).columns[i];
                textView.setPadding(txtpadl, txtpadt, txtpadr, txtpadb);
                int txtwidth = ViewModeRef.eachColWidth[i] * ViewModeRef.MaxPxlEachCharTake;
                if (txtwidth > ViewModeRef.MaxLimitForEachColWidth)
                    textView.setWidth(ViewModeRef.MaxLimitForEachColWidth);
                else
                    textView.setWidth(txtwidth);
                //textView.setWidth(ViewModeRef.eachColWidth[i] * ViewModeRef.MaxPxlEachCharTake);

                if (i == list.get(position).ColumnIndexToSort) {
                    if (list.get(position).sortingtype == 'A') {
                        headrow.sortingtype = 'A';
                        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_drop_up_black_24dp, 0, 0, 0);
                    } else {
                        headrow.sortingtype = 'D';
                        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_drop_down_black_24dp, 0, 0, 0);
                    }
                    Log.e("flage22", "ColumSigned:" + i);
                } else {
                    Log.e("flage22", "ColumNotSigned:" + i);
                }
                //headrow.indexOfclickItem=i;
                textView.setOnClickListener(headrow.listenerheaderrow);
                textView.setTypeface(Typeface.DEFAULT_BOLD);
                if (list.get(position).checkvisibility[i]) {
                    headrow.linearLayout.addView(textView,
                            new ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT));
                }


            }
            list.get(position).isviewed = true;
            headrow.refobjectfortoprow = list.get(position);
        } else if (list.get(position).isbottomrow) {
            ////////////////////////////////For Bottom Row

            BottomRowView holder1 = (BottomRowView) holder;
            holder1.linearLayout.removeAllViews();
            for (int i = 0; i < list.get(position).columns.length; i++) {
                textView = new TextView(holder1.rootview.getContext());
                textView.setId(i);
                textView.setText(list.get(position).columns[i]);
                //   holder1.textviewtext[i] = textView.getText().toString();
                textView.setPadding(txtpadl, txtpadt, txtpadr, txtpadb);
                int txtwidth = ViewModeRef.eachColWidth[i] * ViewModeRef.MaxPxlEachCharTake;
                if (txtwidth > ViewModeRef.MaxLimitForEachColWidth)
                    textView.setWidth(ViewModeRef.MaxLimitForEachColWidth);
                else
                    textView.setWidth(txtwidth);
                textView.setTypeface(Typeface.DEFAULT_BOLD);
                if (list.get(position).checkvisibility[i]) {
                    holder1.linearLayout.addView(textView,
                            new ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT));
                }
            }
            Log.e("object", "StartShow--");
            list.get(position).showdata();
            //holder1.linearLayout.removeAllViews();
            //holder1.linearLayout.setBackgroundColor(Color.parseColor("#000"));
        } else if (list.get(position).isSectionOfSectionView) {
            ////////////////////////////////For Bottom Row

            BottomRowSectionOfSectionView holder1 = (BottomRowSectionOfSectionView) holder;
            holder1.linearLayout.removeAllViews();
            for (int i = 0; i < list.get(position).columns.length; i++) {
                textView = new TextView(holder1.rootview.getContext());
                textView.setId(i);
                textView.setText(list.get(position).columns[i]);
                //   holder1.textviewtext[i] = textView.getText().toString();
                textView.setPadding(txtpadl, txtpadt, txtpadr, txtpadb);

                textView.setGravity(Gravity.START);
                int txtwidth = ViewModeRef.eachColWidth[i] * ViewModeRef.MaxPxlEachCharTake;
                if (txtwidth > ViewModeRef.MaxLimitForEachColWidth)
                    textView.setWidth(ViewModeRef.MaxLimitForEachColWidth);
                else
                    textView.setWidth(txtwidth);
                //textView.setWidth(ViewModeRef.MaxPxlEachCharTakeINSectionOFSec);
                textView.setTypeface(Typeface.DEFAULT_BOLD);
                if (list.get(position).checkvisibility[i]) {
                    holder1.linearLayout.addView(textView,
                            new ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT));
                }
            }
            Log.e("object", "StartShow--");
            list.get(position).showdata();
            //holder1.linearLayout.removeAllViews();
            //holder1.linearLayout.setBackgroundColor(Color.parseColor("#000"));
        } else if (list.get(position).isSectionRow) {
            ///////////////////////////////////For Section View
            SectionRowView sectionRowView = (SectionRowView) holder;
            if (sectionRowView.linearLayout.getChildCount() > 0)
                sectionRowView.linearLayout.removeAllViews();
            textView = new TextView(sectionRowView.rootview.getContext());
            textView.setTextColor(context.getResources().getColor(R.color.contenttextcolor));
            textView.setText(list.get(position).SectionName);
            textView.setPadding(txtpadl, txtpadt, txtpadr, txtpadb);
            textView.setWidth(ViewModeRef.MaxPxlEachCharTakeINSectionOFSec);
            sectionRowView.linearLayout.addView(textView,
                    new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
        } else {
            /////////////For Row Contenet
            ContentHolder holder1 = (ContentHolder) holder;
            holder1.pos = position;
            holder1.object = list.get(position);
            holder1.linearLayout.removeAllViews();
            ImageView imageView = new ImageView(holder1.rootview.getContext());
            imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_more_vert_black_24dp));
            imageView.setOnClickListener(holder1.dotmenulistner);
            holder1.linearLayout.addView(imageView,
                    new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
            for (int i = 0; i < list.get(position).columns.length; i++) {
//                textView = getMyTextViewForRContent(holder1.linearLayout);
                textView = new TextView(holder1.linearLayout.getContext());
                textView.setTextColor(context.getResources().getColor(R.color.contenttextcolor));
                textView.setPadding(txtpadl, txtpadt, txtpadr, txtpadb);
                int txtwidth = ViewModeRef.eachColWidth[i] * ViewModeRef.MaxPxlEachCharTake;
                if (txtwidth > ViewModeRef.MaxLimitForEachColWidth)
                    textView.setWidth(ViewModeRef.MaxLimitForEachColWidth);
                else
                    textView.setWidth(txtwidth);

                if (ViewModeRef.isNumberCoumn[i])
                    textView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                //textView.setWidth(ViewModeRef.eachColWidth[i] * ViewModeRef.MaxPxlEachCharTake);
                textView.setText(list.get(position).columns[i]);
                Log.e("variable", "Objecct" + position + " Colum:" + i + " " + list.get(position).checkvisibility[i]);
                if (list.get(position).checkvisibility[i]) {
                    holder1.linearLayout.addView(textView,
                            new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT));
                }

            }
            if (position % 2 == 0)
                holder1.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.g_gridrow1));
            else
                holder1.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.g_gridrow2));


            list.get(position).isviewed = true;
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        ////////////////////// 0 for header 1 for contenet -1 for last row
        if (list.get(position).isheaderview)
            return 0;
        else if (list.get(position).isbottomrow)
            return -1;
        else if (list.get(position).isSectionRow)
            return -2;
        else if (list.get(position).isSectionOfSectionView)
            return -3;
        else return 1;
    }


    class ContentHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;
        View rootview;
        int pos;
        ViewModeRef object;

        public ContentHolder(@NonNull View itemView) {
            super(itemView);
            rootview = itemView;
            linearLayout = itemView.findViewById(R.id.reflayoutbase);
            if (menuItemClickListner != null)
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        menuItemClickListner.ListenForGridItemClick(object, pos);
                    }
                });
        }

        View.OnClickListener dotmenulistner = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(context, "Clicked ", Toast.LENGTH_SHORT).show();
                PopupMenu popup = new PopupMenu(context, view);
                popup.inflate(R.menu.contextmenu);
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        Toast.makeText(context, "" + menuItem, Toast.LENGTH_SHORT).show();
                        if (menuItemClickListner != null) {
                            menuItemClickListner.ListenForDotMenuItemClick(object, pos, menuItem);
                        }
                        return false;
                    }
                });
            }
        };
    }

    class HeaderRowView extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;
        View rootview;
        View.OnClickListener listenerheaderrow;
        //   int indexOfclickItem=0;
        ViewModeRef refobjectfortoprow;
        char sortingtype;
        String textviewtext[];

        public HeaderRowView(@NonNull View itemView) {
            super(itemView);
            rootview = itemView;
            linearLayout = itemView.findViewById(R.id.headerlayout);
            listenerheaderrow = new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    TextView textView = (TextView) view;
                    if (listenerForChange != null) {
                        Log.e("customvar", textviewtext[textView.getId()]);
                        listenerForChange.listenForSortClick(textviewtext[textView.getId()],
                                textView.getId(), sortingtype);
                    }
                }
            };
        }

        View.OnClickListener dotmenulistner = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(context, "Clicked ", Toast.LENGTH_SHORT).show();
                PopupMenu popup = new PopupMenu(context, view);
                popup.inflate(R.menu.generic_grid_header_menu);
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                      //  Toast.makeText(context, "" + menuItem, Toast.LENGTH_SHORT).show();
                        if(headrowClickListner!=null){
                            headrowClickListner.ListenForDotMenuItemClick(menuItem);
                        }
                        return false;
                    }
                });
            }
        };
    }

    class BottomRowView extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;
        View rootview;
        View.OnClickListener listenerheaderrow;
        //   int indexOfclickItem=0;
        ViewModeRef refobjectfortoprow;
        char sortingtype;
        String textviewtext[];

        public BottomRowView(@NonNull View itemView) {
            super(itemView);
            rootview = itemView;
            linearLayout = itemView.findViewById(R.id.headerlayout);
//            listenerheaderrow = new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    TextView textView = (TextView) view;
//                    if(listenerForChange!=null){
//                        Log.e("customvar",textviewtext[textView.getId()]);
//                        listenerForChange.listenForSortClick(textviewtext[textView.getId()],
//                                textView.getId(),sortingtype);
//                    }
//                }
//            };
        }
    }

    class BottomRowSectionOfSectionView extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;
        View rootview;
        View.OnClickListener listenerheaderrow;
        //   int indexOfclickItem=0;
        ViewModeRef refobjectfortoprow;
        char sortingtype;
        String textviewtext[];

        public BottomRowSectionOfSectionView(@NonNull View itemView) {
            super(itemView);
            rootview = itemView;
            linearLayout = itemView.findViewById(R.id.headerlayout);
//            listenerheaderrow = new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    TextView textView = (TextView) view;
//                    if(listenerForChange!=null){
//                        Log.e("customvar",textviewtext[textView.getId()]);
//                        listenerForChange.listenForSortClick(textviewtext[textView.getId()],
//                                textView.getId(),sortingtype);
//                    }
//                }
//            };
        }
    }

    class SectionRowView extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;
        View rootview;
        View.OnClickListener listenerheaderrow;
        //   int indexOfclickItem=0;
        ViewModeRef refobjectfortoprow;
        char sortingtype;
        String textviewtext[];

        public SectionRowView(@NonNull View itemView) {
            super(itemView);
            rootview = itemView;
            linearLayout = itemView.findViewById(R.id.refseclayout);
//            listenerheaderrow = new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    TextView textView = (TextView) view;
//                    if(listenerForChange!=null){
//                        Log.e("customvar",textviewtext[textView.getId()]);
//                        listenerForChange.listenForSortClick(textviewtext[textView.getId()],
//                                textView.getId(),sortingtype);
//                    }
//                }
//            };
        }
    }

    //////////////////////////////////Genrinc TextViewClass


}

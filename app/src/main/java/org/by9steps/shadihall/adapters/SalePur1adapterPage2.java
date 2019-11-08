package org.by9steps.shadihall.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.activities.Salepur1AddNewActivity;
import org.by9steps.shadihall.helper.MNotificationClass;
import org.by9steps.shadihall.model.ModelForSalePur1page2;

import java.text.DecimalFormat;
import java.util.List;

public class SalePur1adapterPage2 extends RecyclerView.Adapter<SalePur1adapterPage2.viewholdercustom> {

    List<ModelForSalePur1page2> list;
    Context mcontext;
    //public String salepur1id;
    //////////////////interface for Menuitem Click Listner
    popUpMenuItemClickListener listenerpopup;

    public void setListenerpopup(popUpMenuItemClickListener listenerpopup) {
        this.listenerpopup = listenerpopup;
    }

    public interface popUpMenuItemClickListener {
        void popUpMyClickListener(MenuItem item, String ID, ModelForSalePur1page2 model);
    }

    public SalePur1adapterPage2(List<ModelForSalePur1page2> list, Context mcontext) {
        this.list = list;
        this.mcontext = mcontext;
    }


    @NonNull
    @Override
    public viewholdercustom onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        View vv = inflater.inflate(R.layout.salepurfragrowpage2, viewGroup, false);

        return new viewholdercustom(vv);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholdercustom viewholdercustom, int i) {
        Log.e("stss", "onBindViewHolder: " + i);
        Log.e("stss", "SalePur1IDEAch: " + list.get(i).Columns[0]);
        /// 0-3-1-4-5-6
        viewholdercustom.c1.setText(list.get(i).Columns[1]);
        viewholdercustom.c2.setText(list.get(i).Columns[2]);
        viewholdercustom.c3.setText(list.get(i).Columns[3]);
        viewholdercustom.c4.setText(list.get(i).Columns[4]);
        //////////////////////////////////Changin Format of Total Column
        try{
            double dd=Double.parseDouble(list.get(i).Columns[5]);
//            String strform=String.format("%.2f",dd);
//            Log.e("strform",strform);
            DecimalFormat formatter = new DecimalFormat("0.00");
            String ss= formatter.format(dd);
            viewholdercustom.c5.setText(ss);
        }catch (Exception e){
            e.printStackTrace();
        }

        viewholdercustom.model = list.get(i);
        if (list.get(i).setLastRowfun) {

//            DecimalFormat formatter = new DecimalFormat("#,###,###");
//            temobj.Columns[3]= formatter.format(temobj.Columns[3]);
//            temobj.Columns[5]= formatter.format(temobj.Columns[5]);

            viewholdercustom.grandtotal.setVisibility(View.VISIBLE);
//            viewholdercustom.pricetotal.setText("" + list.get(i).GrandTotal);
            try{
                double dd=Double.parseDouble(list.get(i).GrandTotal);
                DecimalFormat formatter = new DecimalFormat("#,###,###");
                String ss1= formatter.format(dd);
                viewholdercustom.pricetotal.setText(ss1);
            }catch (Exception e){e.printStackTrace();}
            viewholdercustom.addnewitem.setVisibility(View.VISIBLE);
            viewholdercustom.quanttotal.setText("" + list.get(i).totalqty);
            viewholdercustom.totalitemcount.setText("" + list.get(i).ColumnCount);

        } else {
            viewholdercustom.grandtotal.setVisibility(View.GONE);
        }


        if (i % 2 == 0) {
            viewholdercustom.view.setBackgroundColor(mcontext.getResources().getColor(android.R.color.background_light));
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class viewholdercustom extends RecyclerView.ViewHolder {
        private TextView c1, c2, c3, c4, c5, quanttotal, pricetotal, addnewinvoice, totalitemcount;
        private ImageView threoodot;
        private View view;
        //private LinearLayout grandtotal,adnewitem,topdatarowlayout;
        private LinearLayout grandtotal,addnewitem;
        private ModelForSalePur1page2 model;
        private View lastlineview, lineseparator;

        public viewholdercustom(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            addnewitem=itemView.findViewById(R.id.grandtotalview2);
             addnewinvoice=itemView.findViewById(R.id.textaddnewitem);
            totalitemcount = itemView.findViewById(R.id.totalcunt);
            // topdatarowlayout=itemView.findViewById(R.id.topdatarowlayout);
            c1 = itemView.findViewById(R.id.col1);
            c2 = itemView.findViewById(R.id.col2);
            c3 = itemView.findViewById(R.id.col3);
            c4 = itemView.findViewById(R.id.col4);
            c5 = itemView.findViewById(R.id.col5);
            // lineseparator=itemView.findViewById(R.id.lineseparator);
            lastlineview = itemView.findViewById(R.id.lastlineview);
             setSapnnableString();
            quanttotal = itemView.findViewById(R.id.grandtotalquan);
            pricetotal = itemView.findViewById(R.id.grandtotalprice);
            grandtotal = itemView.findViewById(R.id.grandtotalview);
            grandtotal.setVisibility(View.GONE);
            threoodot = itemView.findViewById(R.id.threedotpopupsaleedit);
            threoodot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(mcontext, v);

                    popup.inflate(R.menu.contextmenu);
                    popup.show();
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            if (listenerpopup != null)
                                listenerpopup.popUpMyClickListener(menuItem, c1.getText().toString(), model);

                            return false;
                        }
                    });
                }
            });

        }

        public void setSapnnableString() {
            ForegroundColorSpan foregroundSpan = new ForegroundColorSpan(Color.BLUE);
            SpannableString spannableString = new SpannableString("Add new item");
            spannableString.setSpan(clickableSpan, 0, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(foregroundSpan, 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


            addnewinvoice.setText(spannableString);
            addnewinvoice.setMovementMethod(LinkMovementMethod.getInstance());
        }

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                MNotificationClass.ShowToastTem(mcontext, "Click");
                Salepur1AddNewActivity activity = (Salepur1AddNewActivity) mcontext;

                if(activity.locksaleitemedit)
                activity.AddNewDialog();
                else
                    MNotificationClass.ShowToast(mcontext,"Enable Editing");
            }
        };
    }
}

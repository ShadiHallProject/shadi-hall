package org.by9steps.shadihall.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.helper.MNotificationClass;
import org.by9steps.shadihall.model.ModelForSalePur1page2;

import java.util.List;

public class SalePur1adapterPage2 extends RecyclerView.Adapter<SalePur1adapterPage2.viewholdercustom> {

    List<ModelForSalePur1page2> list;
    Context mcontext;

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
        /// 0-3-1-4-5-6
        viewholdercustom.c1.setText(list.get(i).Columns[1]);
        viewholdercustom.c2.setText(list.get(i).Columns[2]);
        viewholdercustom.c3.setText(list.get(i).Columns[3]);
        viewholdercustom.c4.setText(list.get(i).Columns[4]);
        viewholdercustom.c5.setText(list.get(i).Columns[5]);
        //viewholdercustom.c6.setText(list.get(i).Columns[6]);
       if(i%2==0)
       {
           viewholdercustom.view.setBackgroundColor(mcontext.getResources().getColor(android.R.color.background_light));
       }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class viewholdercustom extends RecyclerView.ViewHolder {
        private TextView c1, c2, c3, c4, c5;
        private ImageView threoodot;
        private View view;
        public viewholdercustom(@NonNull View itemView) {
            super(itemView);
            view=itemView;
            c1 = itemView.findViewById(R.id.col1);
            c2 = itemView.findViewById(R.id.col2);
            c3 = itemView.findViewById(R.id.col3);
            c4 = itemView.findViewById(R.id.col4);
            c5 = itemView.findViewById(R.id.col5);
            threoodot=itemView.findViewById(R.id.threedotpopupsaleedit);
            threoodot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(mcontext, v);

                    popup.inflate(R.menu.contextmenu);
                    popup.show();
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {

MNotificationClass.ShowToastTem(mcontext,"CLick"+c1.getText());
//                            if(callBackMethod!=null)
//                                callBackMethod.getPoupCallBackInfo(AcNameID,sqliteDbEdit,menuItem.getTitle().toString());
                            return false;
                        }
                    });
                }
            });

        }
    }
}

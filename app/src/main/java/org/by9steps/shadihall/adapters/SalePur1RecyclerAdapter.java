package org.by9steps.shadihall.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.fragments.SalePur1Fragment;
import org.by9steps.shadihall.model.ProjectMenu;
import org.by9steps.shadihall.model.SalePur1Fragmentmodel;

import java.util.List;

public class SalePur1RecyclerAdapter extends RecyclerView.Adapter<SalePur1RecyclerAdapter.mRecyclerView> {

    private Context mcontext;
    private List<SalePur1Fragmentmodel> list;

    public SalePur1RecyclerAdapter(Context mcontext, List<SalePur1Fragmentmodel> list) {
        this.mcontext = mcontext;
        this.list = list;
    }


    @NonNull
    @Override
    public mRecyclerView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        View vv = inflater.inflate(R.layout.salepurfragrow, viewGroup, false);

        return new mRecyclerView(vv);
    }

    @Override
    public void onBindViewHolder(@NonNull mRecyclerView mRecyclerView, int i) {
        mRecyclerView.c1num.setText(list.get(i).getC1num());
        mRecyclerView.c2date.setText(list.get(i).getC2date());
        mRecyclerView.c3acname.setText(list.get(i).getC3acname());
        mRecyclerView.c4remarks.setText(list.get(i).getC4remarks());
        mRecyclerView.c5billamount.setText(list.get(i).getC5billamount());
        if(i%2==0)
        mRecyclerView.itemView.setBackgroundColor(mcontext.getResources().getColor(R.color.common_google_signin_btn_text_light_disabled));



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class mRecyclerView extends RecyclerView.ViewHolder {
        private TextView c1num, c2date, c3acname, c4remarks, c5billamount;

        public mRecyclerView(@NonNull View itemView) {
            super(itemView);
            c1num = itemView.findViewById(R.id.col1no);
            c2date = itemView.findViewById(R.id.col2date);
            c3acname = itemView.findViewById(R.id.col3acname);
            c4remarks = itemView.findViewById(R.id.col4remark);
            c5billamount = itemView.findViewById(R.id.col5billamnt);

        }
    }
}

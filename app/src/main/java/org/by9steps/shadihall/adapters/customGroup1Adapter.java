package org.by9steps.shadihall.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.helper.Prefrence;
import org.by9steps.shadihall.model.JoinQueryAccount3Name;

import java.util.ArrayList;
import java.util.List;

public class customGroup1Adapter extends RecyclerView.Adapter<customGroup1Adapter.mRecycleView> {

    Prefrence prefrence;
    DatabaseHelper databaseHelper;

    private List<JoinQueryAccount3Name> NewList;
    private List<JoinQueryAccount3Name> list2;
    private Context mContext;
    boolean[] array;

    public customGroup1Adapter(Context mContext, List<JoinQueryAccount3Name> list) {
        this.mContext = mContext;
        this.NewList = list;

        prefrence=new Prefrence(mContext);
        databaseHelper=new DatabaseHelper(mContext);
        array=new boolean[list.size()];
        for (int i = 0; i < list.size(); i++) {
                    array[i]=false;
        }

    }

    @NonNull
    @Override
    public mRecycleView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater=LayoutInflater.from(mContext);
        View view=inflater.inflate(R.layout.customgroup1_gv_row,viewGroup,false);
        return new mRecycleView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final mRecycleView mRecycleView, int i) {

        mRecycleView.checkBox.setOnCheckedChangeListener(null);
        final int position = i;
        mRecycleView.col1.setText(NewList.get(i).getAcNameID());
        mRecycleView.col2.setText(NewList.get(i).getAcName());
        mRecycleView.checkBox.setChecked(array[i]);


        mRecycleView.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                array[position]=isChecked;
                mRecycleView.checkBox.setChecked(isChecked);
            }
        });

      if(i%2==0)
          mRecycleView.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.common_google_signin_btn_text_light_disabled));
    }

    @Override
    public int getItemCount() {
        return NewList.size();
    }

    class mRecycleView extends RecyclerView.ViewHolder{

        TextView col1,col2;
        CheckBox checkBox;
        ImageView imageView;
       // Account3Name dataclicked;

        public mRecycleView(@NonNull View itemView) {
            super(itemView);

            col1=itemView.findViewById(R.id.col1rowAcID1);
            col2=itemView.findViewById(R.id.col2rowAccountName2);
            checkBox=itemView.findViewById(R.id.mCheckbox);

        }

    }


    public List<JoinQueryAccount3Name> getdata(){
        int size=NewList.size();
        list2=new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (array[i]==true)
            {
                list2.add(NewList.get(i));
            }
        }
        return list2;
    }


}

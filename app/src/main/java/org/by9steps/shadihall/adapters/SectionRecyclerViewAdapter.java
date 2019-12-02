package org.by9steps.shadihall.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.helper.MNotificationClass;
import org.by9steps.shadihall.model.ResturentSectionModel1;

import java.util.ArrayList;
import java.util.Locale;

public class SectionRecyclerViewAdapter extends RecyclerView.Adapter<SectionRecyclerViewAdapter.SectionViewHolder>{

    private Context context;
    int NumCol;
    private ArrayList<ResturentSectionModel1> sectionModelArrayList;
    private ItemRecyclerViewAdapter adapter;
    private ArrayList<ResturentSectionModel1> arraylistSearchView;

    public SectionRecyclerViewAdapter(Context context, int numCol, ArrayList<ResturentSectionModel1> sectionModelArrayList) {
        this.context = context;
        NumCol = numCol;
        this.sectionModelArrayList = sectionModelArrayList;

        this.arraylistSearchView = new ArrayList<ResturentSectionModel1>();
        this.arraylistSearchView.addAll(sectionModelArrayList);

    }

    @NonNull
    @Override
    public SectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.section_custom_row_layout, parent, false);
        return new SectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SectionViewHolder holder, int i) {

        final ResturentSectionModel1 sectionModel = sectionModelArrayList.get(i);
        holder.sectionLabel.setText(sectionModel.getSectionLabel());

        //recycler view for items
        holder.itemRecyclerView.setHasFixedSize(true);
        holder.itemRecyclerView.setNestedScrollingEnabled(false);

       // LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
       // holder.itemRecyclerView.setLayoutManager(linearLayoutManager);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, NumCol);
        holder.itemRecyclerView.setLayoutManager(gridLayoutManager);

        adapter = new ItemRecyclerViewAdapter(context, sectionModel.getItemArrayListJoinQuery(),sectionModelArrayList);
        holder.itemRecyclerView.setAdapter(adapter);

        //show toast on click of show all button
        holder.sectionLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MNotificationClass.ShowToast(context,""+sectionModel.getSectionLabel());
            }
        });
    }

    @Override
    public int getItemCount() {
        return sectionModelArrayList.size();
    }

    class SectionViewHolder extends RecyclerView.ViewHolder {
        private TextView sectionLabel;
        private RecyclerView itemRecyclerView;

        public SectionViewHolder(View itemView) {
            super(itemView);
            sectionLabel = (TextView) itemView.findViewById(R.id.section_label);
            itemRecyclerView = (RecyclerView) itemView.findViewById(R.id.item_recycler_view);
        }
    }

    // Filter Class
    public void filter(String s) {


        s = s.toLowerCase(Locale.getDefault());
        sectionModelArrayList.clear();
        if (s.length() == 0) {
            sectionModelArrayList.addAll(arraylistSearchView);
        } else {

            for (int i = 0; i < arraylistSearchView.size(); i++) {
                for (int j = 0; j < arraylistSearchView.get(i).getItemArrayListJoinQuery().size(); j++) {
                    if(arraylistSearchView.get(i).getItemArrayListJoinQuery().get(j).getTableName().toLowerCase(Locale.getDefault()).contains(s))
                        sectionModelArrayList.add(arraylistSearchView.get(i));

                }
            }


           // adapter.filter1(s);
        }
        notifyDataSetChanged();

    }

}

package org.by9steps.shadihall.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.model.ProfitLoss;
import org.by9steps.shadihall.model.ProjectMenu;

import java.util.List;

public class ProjectMenuAdapter extends RecyclerView.Adapter{

    private Context mCtx;
    List<ProjectMenu> mList;

    public ProjectMenuAdapter(Context mCtx, List<ProjectMenu> mList) {
        this.mCtx = mCtx;
        this.mList = mList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_menu_item, parent, false);
            return new ProjectMenuAdapter.SectionViewHolder(v);
        }else {
            LayoutInflater inflater = LayoutInflater.from(mCtx);
            View view = inflater.inflate(R.layout.fragment_section_list, null);
            return new ProjectMenuAdapter.ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {

        final ProjectMenu projectMenu = mList.get(position);

        if (projectMenu.isRow() == 1) {
            ProjectMenuAdapter.ItemViewHolder h = (ProjectMenuAdapter.ItemViewHolder) viewHolder;
            h.name.setText(projectMenu.getMenuName());


        }else {
            ProjectMenuAdapter.SectionViewHolder h = (ProjectMenuAdapter.SectionViewHolder) viewHolder;
            h.textView.setText(projectMenu.getMenuGroup());
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);
        ProjectMenu item = mList.get(position);
        if(item.isRow() == 0) {
            return 0;
        }else {
            return 1;
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        ImageView image;


        public ItemViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.title);
            image = itemView.findViewById(R.id.image);

        }
    }

    class SectionViewHolder extends RecyclerView.ViewHolder{

        private TextView textView;

        public SectionViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.section_name);
        }
    }

//    public void filterList(List<ProfitLoss> filterdNames) {
//        this.mList = filterdNames;
//        notifyDataSetChanged();
//    }
}

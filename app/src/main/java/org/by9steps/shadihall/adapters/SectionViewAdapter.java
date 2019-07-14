package org.by9steps.shadihall.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.model.ProjectMenu;
import org.by9steps.shadihall.model.SectionModel;

import java.util.List;

public class SectionViewAdapter extends RecyclerView.Adapter {


    private Context mCtx;
    List<SectionModel> mList;
    List<ProjectMenu> mList1;

    DatabaseHelper databaseHelper;

    public SectionViewAdapter(Context mCtx, List<SectionModel> mList) {
        this.mCtx = mCtx;
        this.mList = mList;
        databaseHelper = new DatabaseHelper(mCtx);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.fragment_menu_item, null);
        return new SectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        final SectionModel sectionModel = mList.get(i);
        ((SectionViewHolder)viewHolder).sectionname.setText(sectionModel.getLabel());

        mList1 = databaseHelper.getProjectMenu("SELECT * FROM ProjectMenu WHERE MenuGroup = '"+ sectionModel.getLabel()+"' ORDER BY SortBy");

        ((SectionViewHolder)viewHolder).adapter = new RecyclerViewAdapter(((SectionViewHolder)viewHolder).itemView.getContext(),mList1);
        ((SectionViewHolder)viewHolder).recyclerView.setLayoutManager(new GridLayoutManager(((SectionViewHolder)viewHolder).itemView.getContext(),2));
        ((SectionViewHolder)viewHolder).recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(((SectionViewHolder)viewHolder).itemView.getContext(),10), true));
        ((SectionViewHolder)viewHolder).recyclerView.setItemAnimator(new DefaultItemAnimator());
        ((SectionViewHolder)viewHolder).recyclerView.setAdapter(((SectionViewHolder)viewHolder).adapter);

    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(Context context, int dp) {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class SectionViewHolder extends RecyclerView.ViewHolder {

        TextView sectionname;
        RecyclerView recyclerView;
        RecyclerViewAdapter adapter;


        public SectionViewHolder(View itemView) {
            super(itemView);

            sectionname = itemView.findViewById(R.id.section_name);
            recyclerView = itemView.findViewById(R.id.menu_list);

        }
    }
}


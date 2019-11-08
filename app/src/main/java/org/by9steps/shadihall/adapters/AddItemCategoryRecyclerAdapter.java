package org.by9steps.shadihall.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.activities.MenuClickActivity;
import org.by9steps.shadihall.model.Item2Group;

import java.util.List;

public class AddItemCategoryRecyclerAdapter extends RecyclerView.Adapter<AddItemCategoryRecyclerAdapter.mRecyclerView> {

    private Context mContext;
    private List<Item2Group> list;


    public AddItemCategoryRecyclerAdapter(Context mContext, List<Item2Group> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public mRecyclerView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.additemcategory_recyclerview_row, viewGroup, false);
        return new mRecyclerView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull mRecyclerView mRecyclerView, int i) {

        mRecyclerView.col1.setText(list.get(i).getItem2GroupID());
        mRecyclerView.col2.setText(list.get(i).getItem1TypeID());
        mRecyclerView.col3.setText(list.get(i).getItem2GroupName());
mRecyclerView.clickdata=list.get(i);
        if (i % 2 == 0)
            mRecyclerView.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.common_google_signin_btn_text_light_disabled));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class mRecyclerView extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView col1, col2, col3;
        private ImageView imageView;
        private Item2Group clickdata;

        public mRecyclerView(@NonNull View itemView) {
            super(itemView);
            col1 = itemView.findViewById(R.id.rowItem2GroupId);
            col2 = itemView.findViewById(R.id.rowItem1TypeId);
            col3 = itemView.findViewById(R.id.rowItem2GroupName);
            imageView = itemView.findViewById(R.id.Imagethreedotpopup);
            imageView.setOnClickListener(this);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   // MNotificationClass.ShowToastTem(mContext, "item click");
                }
            });


        }

        @Override
        public void onClick(View view) {

            PopupMenu popup = new PopupMenu(mContext, view);

            popup.inflate(R.menu.additemcategorymenu);
            popup.show();
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {

                 //   DialogForAddItemCategory dialog = new DialogForAddItemCategory();

                    int Id= clickdata.getID();
                    String s1="Edit";
                    String s2=clickdata.getItem1TypeID();
                    String s3=clickdata.getItem2GroupName();
                    MenuClickActivity acitviyt = (MenuClickActivity) mContext;
                    acitviyt.MenuItemClickListener(s1,s2,s3,Id);
                    return false;
                }
            });
        }

    }
}

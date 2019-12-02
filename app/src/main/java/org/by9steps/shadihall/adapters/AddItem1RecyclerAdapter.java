package org.by9steps.shadihall.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.activities.ItemLedgerActivity;
import org.by9steps.shadihall.activities.MenuClickActivity;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.model.item3name.Item3Name_;

import java.util.List;

public class AddItem1RecyclerAdapter extends RecyclerView.Adapter<AddItem1RecyclerAdapter.mRecyclerView> {

    private AddItem1RecyclerAdapter recyclerAdapter;
    private DatabaseHelper databaseHelper;
    private RecyclerView mRecyclerView;
    private Context mcontext;
    private List<Item3Name_> list;


    public AddItem1RecyclerAdapter(Context context, List<Item3Name_>list) {
        this.mcontext=context;
        this.list=list;
    }

    @NonNull
    @Override
    public mRecyclerView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater=LayoutInflater.from(mcontext);
        View view=inflater.inflate(R.layout.additemfragrow,viewGroup,false);
        return new mRecyclerView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull mRecyclerView mRecyclerView, int i) {


        Log.e("dsfasfasdfasdf",list.get(i).getItem3NameID()+"");
        mRecyclerView.col1.setText(list.get(i).getItemCode());
        mRecyclerView.col2.setText(list.get(i).getItemName());
        mRecyclerView.col3.setText(list.get(i).getSalePrice());
        mRecyclerView.col4.setText(list.get(i).getStock());
        mRecyclerView.clickData =list.get(i);

        if(i%2==0)
            mRecyclerView.itemView.setBackgroundColor(mcontext.getResources().getColor(R.color.common_google_signin_btn_text_light_disabled));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class mRecyclerView extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView col1, col2, col3, col4;
        private ImageView imageView;
        private Item3Name_ clickData;

        public mRecyclerView(@NonNull final View itemView) {
            super(itemView);
            col1=itemView.findViewById(R.id.col1ItemGroup);
            col2=itemView.findViewById(R.id.col2ItemName);
            col3=itemView.findViewById(R.id.col3Price);
            col4=itemView.findViewById(R.id.col4Stock);
            imageView=itemView.findViewById(R.id.Image3Dot);
            imageView.setOnClickListener(this);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent ii=new Intent(v.getContext(), ItemLedgerActivity.class);

                    Log.e("asdfasdf",clickData.getItem3NameID()+"");
                    ii.putExtra("Item3nameid",clickData.getItem3NameID()+"");

                    itemView.getContext().startActivity(ii);
                }
            });


        }

        @Override
        public void onClick(View view) {

            PopupMenu popup = new PopupMenu(mcontext, view);

            popup.inflate(R.menu.additemcategorymenu);
            popup.show();
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {

                    int Id=clickData.getID();
                    String type="Edit";
                    String name=clickData.getItemName();
                    String price=clickData.getSalePrice();
                    String code=clickData.getStock();

                    MenuClickActivity acitviyt = (MenuClickActivity) mcontext;
                    acitviyt.myFunction(Id,type,name,price,code);

                    return false;


                }
            });

        }
    }
}

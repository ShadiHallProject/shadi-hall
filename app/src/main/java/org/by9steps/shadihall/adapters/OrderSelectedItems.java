package org.by9steps.shadihall.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.model.SelectedItems;

import java.util.List;

public class OrderSelectedItems extends RecyclerView.Adapter<OrderSelectedItems.itemViewHold>{

    private Context context;
    private List<SelectedItems> selectedItems;

    public OrderSelectedItems(Context context, List<SelectedItems> selectedItems) {
        this.context = context;
        this.selectedItems = selectedItems;
    }

    @NonNull
    @Override
    public itemViewHold onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.oreder_item_selected,viewGroup,false);

        return new itemViewHold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull itemViewHold holder, int i) {
        holder.itemName.setText(selectedItems.get(i).getItemName());
        holder.itemPrice.setText(selectedItems.get(i).getPrice());
        holder.itemQty.setText(""+selectedItems.get(i).getQty());
        holder.totalPrice.setText(""+selectedItems.get(i).getTotalPrice());

        if (i % 2 == 0)
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.common_google_signin_btn_text_light_disabled));


    }

    @Override
    public int getItemCount() {
        return selectedItems.size();
    }

    class itemViewHold extends RecyclerView.ViewHolder{
        private TextView itemName,itemPrice,itemQty,totalPrice;

        public itemViewHold(@NonNull View itemView) {
            super(itemView);
            itemName=itemView.findViewById(R.id.textview_title_itemName);
            itemPrice=itemView.findViewById(R.id.textview_title_itemPrice);
            itemQty=itemView.findViewById(R.id.textview_title_itemQuantity);
            totalPrice=itemView.findViewById(R.id.textview_title_totalPrice);
        }
    }
}

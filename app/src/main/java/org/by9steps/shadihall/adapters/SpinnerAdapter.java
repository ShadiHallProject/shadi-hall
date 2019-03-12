package org.by9steps.shadihall.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.model.Account2Group;
import org.by9steps.shadihall.model.AreaName;

public class SpinnerAdapter extends ArrayAdapter<String>{

    private final LayoutInflater mInflater;
    private final Context mContext;
    private List<AreaName> items = null;
    private List<Account2Group> itemsAcGroup = null;

    public SpinnerAdapter(@NonNull Context context, @NonNull List objects) {
        super(context, 0, objects);

        mContext = context;
        mInflater = LayoutInflater.from(context);
        items = objects;
    }
    public SpinnerAdapter(@NonNull Context context, @NonNull List objects, String s) {
        super(context, 0, objects);

        mContext = context;
        mInflater = LayoutInflater.from(context);
        itemsAcGroup = objects;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView,
                                @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent){
        final View view = mInflater.inflate(R.layout.register_spinner_item, parent, false);

        TextView name = view.findViewById(R.id.name);

        if (items != null) {
            AreaName areaName = items.get(position);

            name.setText(areaName.getAreaName());
        }else if(itemsAcGroup != null){
            Account2Group account3Name = itemsAcGroup.get(position);

            name.setText(account3Name.getAcGruopName());
        }

        return view;
    }
}

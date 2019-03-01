package org.by9steps.shadihall.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.by9steps.shadihall.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {

    //shared prefrences
    SharedPreferences sharedPreferences;
    public static final String mypreference = "mypref";
    public static final String login = "loginKey";


    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        //shared prefrences
        sharedPreferences = getActivity().getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        String s = "No";
        if(sharedPreferences.contains(login)){
            s = sharedPreferences.getString(login,"");
        }

        if (s.equals("Yes")) {
            getChildFragmentManager().beginTransaction()
                    .add(R.id.mContainer, new MenuItemsFragment())
                    .addToBackStack(null)
                    .commit();
        }else{
            getChildFragmentManager().beginTransaction()
                    .add(R.id.mContainer, new LoginFragment())
                    .addToBackStack(null)
                    .commit();
        }



        return view;
    }

}

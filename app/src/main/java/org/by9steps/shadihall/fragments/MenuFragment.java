package org.by9steps.shadihall.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.helper.Prefrence;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {

    private static final String ARG_MENU = "message";

    //shared prefrences
    SharedPreferences sharedPreferences;
    public static final String mypreference = "mypref";
    public static final String login = "loginKey";

    Prefrence prefrence;

    // TODO: Rename and change types of parameters
    private String men;

    public static MenuFragment newInstance(String message) {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MENU, message);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            men = getArguments().getString(ARG_MENU);
        }
    }


    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        setHasOptionsMenu(true);
        //shared prefrences
        sharedPreferences = getActivity().getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        prefrence = new Prefrence(getContext());

        String s = "No";
        if(sharedPreferences.contains(login)){
            s = sharedPreferences.getString(login,"");
        }

//        if (s.equals("Yes")) {
//            getChildFragmentManager().beginTransaction()
//                    .add(R.id.mContainer, new MenuItemsFragment())
//                    .addToBackStack(null)
//                    .commit();
//        }else{
//            getChildFragmentManager().beginTransaction()
//                    .add(R.id.mContainer, new LoginFragment())
//                    .addToBackStack(null)
//                    .commit();
//        }

        if (prefrence.getClientIDSession().equals("0")
                && prefrence.getClientUserIDSession().equals("0")
                && prefrence.getUserRighhtsSession().equals("0")){

            getChildFragmentManager().beginTransaction()
                    .add(R.id.mContainer, new LoginFragment())
                    .addToBackStack(null)
                    .commit();
        }else {

            getChildFragmentManager().beginTransaction()
                    .add(R.id.mContainer, new MenuItemsFragment())
                    .addToBackStack(null)
                    .commit();
        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//         Inflate the menu; this adds items to the action bar if it is present.
//            inflater.inflate(R.menu.setting_menu, menu);
    }

}

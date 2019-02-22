package org.by9steps.shadihall.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.by9steps.shadihall.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);

        getChildFragmentManager().beginTransaction()
                .add(R.id.container, new TreeFragment())
                .commit();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("CLICK","Click");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        Fragment fragment = ((MainActivity.ViewPagerAdapter)viewPager.getAdapter()).getFragment(0);

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_list){

                getChildFragmentManager().beginTransaction()
                        .replace(R.id.container, new ListFragment())
                        .commit();

        }else if(id == R.id.action_tree){

                getChildFragmentManager().beginTransaction()
                        .replace(R.id.container, new TreeFragment())
                        .commit();

        }else if (id == R.id.action_map){
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.container, new ListFragment())
                        .commit();
        }

        return super.onOptionsItemSelected(item);
    }
}

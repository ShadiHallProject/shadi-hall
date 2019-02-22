package org.by9steps.shadihall.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.fragments.HomeFragment;
import org.by9steps.shadihall.fragments.ListFragment;
import org.by9steps.shadihall.fragments.LoginFragment;
import org.by9steps.shadihall.fragments.MenuFragment;
import org.by9steps.shadihall.fragments.TreeFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    int status = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar()!=null)
            getSupportActionBar().setElevation(0.0f);


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(2);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

                if ( i != 0){

                }

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(), "Home");
        adapter.addFragment(new MenuFragment(), "Menu");
        adapter.addFragment(new ListFragment(), "Tab 3");
        viewPager.setAdapter(adapter);
    }


    //View Pager Adapter Class
    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final Map<Integer,String> mFragmentTags;
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        private FragmentManager fragmentManager;

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
            mFragmentTags = new HashMap<Integer, String>();
            fragmentManager = manager;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            Object obj = super.instantiateItem(container,position);

            if (obj instanceof Fragment){
                Fragment f = (Fragment) obj;
                String tag = f.getTag();
                mFragmentTags.put(position, tag);
            }
            return obj;
        }

        public Fragment getFragment(int position){
            String tag = mFragmentTags.get(position);
            if (tag == null)
                return null;

            return fragmentManager.findFragmentByTag(tag);

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Fragment fragment = ((ViewPagerAdapter)viewPager.getAdapter()).getFragment(0);

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_list){
            if (fragment != null){
//                fragment.onResume();
                (fragment).getChildFragmentManager().beginTransaction()
                        .replace(R.id.container, new ListFragment())
                        .commit();
            }

        }else if(id == R.id.action_tree){

            if (fragment != null){
                (fragment).getChildFragmentManager().beginTransaction()
                        .replace(R.id.container, new TreeFragment())
                        .commit();
            }

        }else if (id == R.id.action_map){
            if (fragment != null){
                (fragment).getChildFragmentManager().beginTransaction()
                        .replace(R.id.container, new ListFragment())
                        .commit();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem action_map = menu.findItem(R.id.action_map);
        MenuItem action_list = menu.findItem(R.id.action_list);
        MenuItem action_tree = menu.findItem(R.id.action_tree);
        if (status == 0){
            action_list.setVisible(false);
            action_map.setVisible(false);
            action_tree.setVisible(false);
        }else {
            action_list.setVisible(true);
            action_map.setVisible(true);
            action_tree.setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }
}

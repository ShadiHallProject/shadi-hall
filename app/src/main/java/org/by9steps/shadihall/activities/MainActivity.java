package org.by9steps.shadihall.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.orm.SugarContext;

import org.by9steps.shadihall.AppController;
import org.by9steps.shadihall.R;
import org.by9steps.shadihall.fragments.HomeFragment;
import org.by9steps.shadihall.fragments.ListFragment;
import org.by9steps.shadihall.fragments.LoginFragment;
import org.by9steps.shadihall.fragments.MenuFragment;
import org.by9steps.shadihall.fragments.TreeFragment;
import org.by9steps.shadihall.helper.Prefrence;
import org.by9steps.shadihall.model.AreaName;
import org.by9steps.shadihall.model.Bookings;
import org.by9steps.shadihall.model.Tree;
import org.by9steps.shadihall.model.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    //shared prefrences
    SharedPreferences sharedPreferences;
    public static final String mypreference = "mypref";
    public static final String login = "loginKey";

    Prefrence prefrence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SugarContext.init(this);

        if (getSupportActionBar()!=null)
            getSupportActionBar().setElevation(0.0f);

        //shared prefrences
        sharedPreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        prefrence = new Prefrence(this);


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
        String s = "No";
        if(sharedPreferences.contains(login)){
            s = sharedPreferences.getString(login,"");
        }
        if (s.equals("Yes")) {
            getMenuInflater().inflate(R.menu.setting_menu, menu);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        noinspection SimplifiableIfStatement
        if (id == R.id.action_signout){
            LogOut();
        }else if(id == R.id.action_profite){
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            intent.putExtra("TYPE", AppController.profileType);
            startActivity(intent);
        }else if(id == R.id.action_location){
            Toast.makeText(MainActivity.this, "coming soon", Toast.LENGTH_SHORT).show();
        }else if (id == R.id.action_password){
            Toast.makeText(MainActivity.this, "coming soon", Toast.LENGTH_SHORT).show();
        }else if (id == R.id.action_sms_settings){
            Toast.makeText(MainActivity.this, "coming soon", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void LogOut(){
        prefrence.setUserRighhtsSession("0");
        prefrence.setClientUserIDSession("0");
        prefrence.setClientIDSession("0");
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(login, "No");
        editor.apply();
        User.delete(User.class);
//        viewPager.setCurrentItem(1);
        startActivity(new Intent(MainActivity.this, MainActivity.class));
        finish();
    }
}

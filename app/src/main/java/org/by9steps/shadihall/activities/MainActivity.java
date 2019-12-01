package org.by9steps.shadihall.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.orm.SugarContext;

import org.by9steps.shadihall.AppController;
import org.by9steps.shadihall.R;
import org.by9steps.shadihall.fragments.HomeFragment;
import org.by9steps.shadihall.fragments.ListFragment;
import org.by9steps.shadihall.fragments.MenuFragment;
import org.by9steps.shadihall.helper.GenericConstants;
import org.by9steps.shadihall.helper.Prefrence;
import org.by9steps.shadihall.helper.ThemeProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    public DrawerLayout dl;
    public ActionBarDrawerToggle t;
    public NavigationView nv;
    //shared prefrences
    SharedPreferences sharedPreferences;
    public static final String mypreference = "mypref";
    public static final String login = "loginKey";
   public static int indexOfSelectdFrag = 0;
    Prefrence prefrence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        indexOfSelectdFrag=0;
        ThemeProvider.setThemeOfApp(this);
        setContentView(R.layout.activity_main);
        settingnavdrawerComponents();
        SugarContext.init(this);

        if (getSupportActionBar() != null)
            getSupportActionBar().setElevation(0.0f);

        //shared prefrences
        sharedPreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        prefrence = new Prefrence(this);


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(3);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

                Log.e("flagdrawerforlist", "Inside MainACtivity onPageSelected:" + i);
                indexOfSelectdFrag = i;
                if (i == 1 && prefrence.getDrawerVisibiltiy()) {
                    t.setDrawerIndicatorEnabled(true);
                    dl.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                } else {
                    t.setDrawerIndicatorEnabled(false);
                    dl.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        Log.e(GenericConstants.MYEdittion, "Editing");

        Log.e(this.getClass().getName(), "Client ID::" + new Prefrence(this).getClientIDSession());
        Log.e(this.getClass().getName(), "ClientUserID::" + new Prefrence(this).getClientUserIDSession());
        Log.e(this.getClass().getName(), "ProjectIDSerssion::" + new Prefrence(this).getProjectIDSession());
        Log.e(this.getClass().getName(), "UserRightSession::" + new Prefrence(this).getUserRighhtsSession());

    }

    private void settingnavdrawerComponents() {
        dl = (DrawerLayout) findViewById(R.id.activity_main);
        t = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dl.addDrawerListener(t);
        dl.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        t.syncState();

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            //  actionBar.setDisplayHomeAsUpEnabled(false);
            t = new ActionBarDrawerToggle(this, dl, mToolbar, R.string.Open, R.string.Close) {

                public void onDrawerClosed(View view) {
                    supportInvalidateOptionsMenu();
                    //drawerOpened = false;
                }

                public void onDrawerOpened(View drawerView) {
                    supportInvalidateOptionsMenu();
                    //drawerOpened = true;
                }
            };

            //  dl.setDrawerIndicatorEnabled(t);
            dl.setDrawerListener(t);
            t.setDrawerIndicatorEnabled(false);

            t.syncState();
        }

        nv = (NavigationView) findViewById(R.id.nv);
        nv.setVisibility(View.GONE);
//
//        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                int id = item.getItemId();
//                switch (id) {
//                    case R.id.account:
//                        Toast.makeText(MainActivity.this, "My Account", Toast.LENGTH_SHORT).show();
//                        break;
//                    case R.id.settings:
//                        Toast.makeText(MainActivity.this, "Settings", Toast.LENGTH_SHORT).show();
//                        break;
//                    case R.id.mycart:
//                        Toast.makeText(MainActivity.this, "My Cart", Toast.LENGTH_SHORT).show();
//                        break;
//                    default:
//                        return true;
//                }
//
//
//                return true;
//
//            }
//        });


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

        private final Map<Integer, String> mFragmentTags;
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
            Object obj = super.instantiateItem(container, position);

            if (obj instanceof Fragment) {
                Fragment f = (Fragment) obj;
                String tag = f.getTag();
                mFragmentTags.put(position, tag);
            }
            return obj;
        }

        public Fragment getFragment(int position) {
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
        if (sharedPreferences.contains(login)) {
            s = sharedPreferences.getString(login, "");
        }
        if (s.equals("Yes")) {
            getMenuInflater().inflate(R.menu.setting_menu, menu);
            return true;
        } else {
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
        if (id == R.id.action_signout) {
            LogOut();
        } else if (id == R.id.action_profite) {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            intent.putExtra("TYPE", AppController.profileType);
            startActivity(intent);
        } else if (id == R.id.action_location) {
            Toast.makeText(MainActivity.this, "coming soon", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.action_password) {
            Toast.makeText(MainActivity.this, "coming soon", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.action_sms_settings) {
            Toast.makeText(MainActivity.this, "coming soon", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void LogOut() {
        prefrence.setUserRighhtsSession("0");
        prefrence.setClientUserIDSession("0");
        prefrence.setMYClientUserIDSession("0");
        prefrence.setClientIDSession("0");
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(login, "No");
        editor.apply();
//        viewPager.setCurrentItem(1);
        startActivity(new Intent(MainActivity.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (indexOfSelectdFrag == 1 && prefrence.getDrawerVisibiltiy()) {
            Log.e("falgtesting","qualifiednacdrawer");
            t.setDrawerIndicatorEnabled(true);
            dl.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else {
            Log.e("falgtesting","Unqualifiednacdrawer");

            t.setDrawerIndicatorEnabled(false);
            dl.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }
}

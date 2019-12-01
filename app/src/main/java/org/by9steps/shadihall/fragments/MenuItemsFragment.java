package org.by9steps.shadihall.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.by9steps.shadihall.AppController;
import org.by9steps.shadihall.R;
import org.by9steps.shadihall.activities.MainActivity;
import org.by9steps.shadihall.activities.RegisterActivity;
import org.by9steps.shadihall.adapters.SectionViewAdapter;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.helper.GenericConstants;
import org.by9steps.shadihall.helper.Prefrence;
import org.by9steps.shadihall.model.Client;
import org.by9steps.shadihall.model.Menu;
import org.by9steps.shadihall.model.ProjectMenu;
import org.by9steps.shadihall.model.SectionModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuItemsFragment extends Fragment {

    List<Menu> mEntries, mReports;

    List<SectionModel> modelList;
    DatabaseHelper databaseHelper;
    Prefrence prefrence;

    List<ProjectMenu> mList;
    List<ProjectMenu> projectMenus;

    String m = "First";


    public MenuItemsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
       if(true){
           prefrence.setDrawerVisibiltiy(true);
           Log.e("falgtesting", "onStartFramgMeuItemcalls");


           ((MainActivity) getActivity()).t.setDrawerIndicatorEnabled(true);
           ((MainActivity) getActivity()).t.syncState();
           ((MainActivity) getActivity()).dl.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
//      int ocutn=  ((MainActivity)getActivity()).nv.getHeaderCount();
//        Log.e("asdfasdfasdfsadf","Header Count:"+ocutn);
           View headerview= ((MainActivity) getActivity()).nv.getHeaderView(0);
           ImageView headerimage=headerview.findViewById(R.id.navheaderimageview);
           TextView headertext=headerview.findViewById(R.id.headertextview);
           try {
               List<Client>  list= databaseHelper.getClient("Select * from Client where ClientID="+prefrence.getClientIDSession());
               headertext.setText(list.get(0).getCompanyName());
           }catch (Exception e){
               e.printStackTrace();
           }


         String uriForLogo= AppController.imageUrl + prefrence.getClientIDSession() + "/logo.png";
           Log.e("asdfasddfasdf",uriForLogo);

           Picasso.get().load(AppController.imageUrl + prefrence.getClientIDSession() + "/logo.png")
                   .placeholder(R.drawable.default_avatar).networkPolicy(NetworkPolicy.NO_CACHE)
                   .memoryPolicy(MemoryPolicy.NO_CACHE)
                   .into(headerimage);
           ((MainActivity) getActivity()).nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
               @Override
               public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                   int id = menuItem.getItemId();
                   switch (id) {
                       case R.id.edit_profile:
                           ((MainActivity) getActivity()).dl.closeDrawer(Gravity.LEFT);
                           Intent i=new Intent(getContext(), RegisterActivity.class);
                           i.putExtra("TYPE","Edit");
                           startActivity(i);

                           break;
                       case R.id.recharg:
                           ((MainActivity) getActivity()).dl.closeDrawer(Gravity.LEFT);
                           //  Toast.makeText(getContext(), "Re", Toast.LENGTH_SHORT).show();
                           break;
                       case R.id.baltrans:
                           ((MainActivity) getActivity()).dl.closeDrawer(Gravity.LEFT);
                           //    Toast.makeText(getContext(), "transfer", Toast.LENGTH_SHORT).show();
                           break;
                       default:
                           return true;
                   }
                   return true;
               }
           });
       }
    }

//    @Override
//    public void onPause() {
//        Log.e("flagdrawerforlist", "onPauseFramgMeuItemcalls");
//        prefrence.setDrawerVisibiltiy(false);
//        super.onPause();
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        prefrence.setDrawerVisibiltiy(false);
//    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.e("flagdrawerforlist", "setUserVisibleHint");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu_item_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);

        databaseHelper = new DatabaseHelper(getContext());
        prefrence = new Prefrence(getContext());
        projectMenus = new ArrayList<>();

        mList = databaseHelper.getProjectMenu("SELECT * FROM ProjectMenu WHERE ProjectID = " + prefrence.getProjectIDSession() + " ORDER BY GroupSortBy");

//        for (ProjectMenu p : mList){
//            if (m.equals("First")){
//                projectMenus.add(ProjectMenu.createSection(p.getMenuGroup()));
//                projectMenus.add(ProjectMenu.createRow(p.getMenuID(), p.getProjectID(), p.getMenuGroup(), p.getMenuName(), p.getPageOpen(), p.getValuePass(), p.getImageName(), p.getSortBy()));
//                m = p.getMenuGroup();
//            }else if (m.equals(p.getMenuGroup())){
//                projectMenus.add(ProjectMenu.createRow(p.getMenuID(), p.getProjectID(), p.getMenuGroup(), p.getMenuName(), p.getPageOpen(), p.getValuePass(), p.getImageName(), p.getSortBy()));
//
//            }else {
//                projectMenus.add(ProjectMenu.createSection(p.getMenuGroup()));
//                projectMenus.add(ProjectMenu.createRow(p.getMenuID(), p.getProjectID(), p.getMenuGroup(), p.getMenuName(), p.getPageOpen(), p.getValuePass(), p.getImageName(), p.getSortBy()));
//                m = p.getMenuGroup();
//            }
//        }
//
//        ProjectMenuAdapter adapter = new ProjectMenuAdapter(getContext(), projectMenus);
//        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(adapter);

//        modelList = new ArrayList<>();
//
//        mEntries = new ArrayList<>();
//        mEntries.add(new Menu("Booking",R.drawable.goldenbook));
//        mEntries.add(new Menu("Recovery",R.drawable.recovercash));
//        mEntries.add(new Menu("Web Editing",R.drawable.goldenwebedit));
//        mEntries.add(new Menu("Cash Book",R.drawable.cash));
//        mEntries.add(new Menu("ChartOfAcc",R.drawable.chartofaccount));
//
//        mReports = new ArrayList<>();
//        mReports.add(new Menu("Cash Book",R.drawable.cash));
//        mReports.add(new Menu("Booking",R.drawable.booking));
//        mReports.add(new Menu("Cash And Bank",R.drawable.cash));
//        mReports.add(new Menu("Employee",R.drawable.employee));
//        mReports.add(new Menu("General Expense",R.drawable.goldenexpense));
//        mReports.add(new Menu("Fixed Asset",R.drawable.goldenfixedassets));
//        mReports.add(new Menu("Supplier",R.drawable.supplier));
//        mReports.add(new Menu("Client",R.drawable.client));
//        mReports.add(new Menu("Revenue",R.drawable.revenue));
//        mReports.add(new Menu("Capital",R.drawable.goldencapital));
//        mReports.add(new Menu("Website",R.drawable.goldenwebedit));
//        mReports.add(new Menu("Trail Balance",R.drawable.trailbalnce));
//        mReports.add(new Menu("Profit/Loss",R.drawable.profitloss));
//        mReports.add(new Menu("Bal Sheet",R.drawable.balancesheet));
//
//        modelList.add(new SectionModel("Entries",mEntries));
//        modelList.add(new SectionModel("Reports",mReports));
//
//        SectionViewAdapter adapter = new SectionViewAdapter(getContext(),modelList);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setAdapter(adapter);
        Log.e(GenericConstants.MYEdittion, "Editing");
        Log.e(this.getClass().getName(), "Client ID::" + new Prefrence(this.getContext()).getClientIDSession());
        Log.e(this.getClass().getName(), "ClientUserID::" + new Prefrence(this.getContext()).getClientUserIDSession());
        Log.e(this.getClass().getName(), "ProjectIDSerssion::" + new Prefrence(this.getContext()).getProjectIDSession());
        Log.e(this.getClass().getName(), "UserRightSession::" + new Prefrence(this.getContext()).getUserRighhtsSession());

        mEntries = new ArrayList<>();
        modelList = new ArrayList<>();
        for (int i = 0; i < mList.size(); i++) {
            if (m.equals("First")) {
//                mEntries.add(new Menu(mList.get(i).getMenuName(),R.drawable.cash));
                modelList.add(new SectionModel(mList.get(i).getMenuGroup(), mList));
                m = mList.get(i).getMenuGroup();
            } else if (m.equals(mList.get(i).getMenuGroup())) {
                mEntries.add(new Menu(mList.get(i).getMenuName(), R.drawable.cash));
            } else {
                modelList.add(new SectionModel(mList.get(i).getMenuGroup(), mList));
                Log.e("LISTSIZE", String.valueOf(mEntries.size()));
//                mEntries.clear();
//                mEntries.add(new Menu(mList.get(i).getMenuName(),R.drawable.cash));
                m = mList.get(i).getMenuGroup();
            }
        }
//        modelList.add(new SectionModel(m, mEntries));


        SectionViewAdapter adapter = new SectionViewAdapter(getContext(), modelList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        return view;
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(Context context, int dp) {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

}

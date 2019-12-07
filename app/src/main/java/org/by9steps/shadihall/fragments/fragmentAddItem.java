package org.by9steps.shadihall.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.callingapi.Item2GroupApis;
import org.by9steps.shadihall.callingapi.Item3NameApis;
import org.by9steps.shadihall.chartofaccountdialog.DialogForAddNewItemEntry;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.helper.GenericConstants;
import org.by9steps.shadihall.helper.MNotificationClass;
import org.by9steps.shadihall.helper.Prefrence;
import org.by9steps.shadihall.model.item3name.Item3Name_;

public class fragmentAddItem extends Fragment implements  AdapterView.OnItemSelectedListener, View.OnClickListener {

    private DatabaseHelper databaseHelper;
    private ImageView addnew;
    private String EntryType = null;
    private String message = null;
    private String ValuePass = null;

    Button viewgrid, viewtree, other;
    FragmentAddItemCompleteStockGridView fragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_item, container, false);
        AssignIdsToViewWidget(view);

        EntryType = getArguments().getString("AddItemTitle");
        message = getArguments().getString("AddItemMessage");
        ValuePass = getArguments().getString("AddItemPossition");

        fillGridView();
        return view;
    }


    private void AssignIdsToViewWidget(View vv) {

        databaseHelper = new DatabaseHelper(getContext());
        addnew = vv.findViewById(R.id.add_item);
        addnew.setOnClickListener(this);
        viewgrid = vv.findViewById(R.id.btnhorizontal1);
        viewgrid.setOnClickListener(this);
        viewtree = vv.findViewById(R.id.btnhorizontal2);
        viewtree.setOnClickListener(this);
        other = vv.findViewById(R.id.btnhorizontal3);
        other.setOnClickListener(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.cb_menu, menu);
        MenuItem settings = menu.findItem(R.id.action_settings);
        settings.setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();
        } else if (item.getItemId() == R.id.action_print) {
            //do something
        } else if (item.getItemId() == R.id.action_refresh) {
            //do something
            MNotificationClass.ShowToastTem(getContext(), "RefreshClick");
            updateItemFromSqlite();
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateItemFromSqlite() {
        final Prefrence prefrence = new Prefrence(getContext());
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Updating item2");
        progressDialog.setCancelable(false);
        progressDialog.show();
        // progressDialog.setMessage("Updating new Item From Sqlite to Cloud");
        final Item2GroupApis item2GroupApis = new Item2GroupApis(getContext(), progressDialog, databaseHelper);
       // final Item3NameApis item3NameApis = new Item3NameApis(getContext(), progressDialog, databaseHelper);

        //////////////////First Mehtod
        if (GenericConstants.isConnected(getContext())) {
            progressDialog.show();
            item2GroupApis.trigerAllMethodinSequence(prefrence.getClientIDSession(), new Item2GroupApis.listentodatafinish() {
                @Override
                public void method(String name) {
                    methodItem3Name(name);
                    Log.e("item2end",name);
                }
            });
//            Log.e("status", "Flag1");
//            item2GroupApis.getEditedDataFromServerItem2Group(prefrence.getClientIDSession(), new Item2GroupApis.listentodatafinish() {
//                @Override
//                public void method(String name) {
//                    progressDialog.show();
//                    Log.e("status", "Flag2");
//                    item2GroupApis.AddEditedDataFromSqliteToItem2GroupServer(prefrence.getClientIDSession(), new Item2GroupApis.listentodatafinish() {
//                        @Override
//                        public void method(String name) {
//                            Log.e("status", "Flag3");
//                            item2GroupApis.getNewInsertedDataFromServerItem2Group(prefrence.getClientIDSession(), new Item2GroupApis.listentodatafinish() {
//                                @Override
//                                public void method(String name) {
//                                    Log.e("infint",""+name);
//                                    progressDialog.show();
//                                    Log.e("status", "Flag4");
//                                    item2GroupApis.AddNewDatafromSqliteToServer(prefrence.getClientIDSession(), new Item2GroupApis.listentodatafinish() {
//                                        @Override
//                                        public void method(String name) {
//                                            methodItem3Name(name);
//                                        }
//                                    });
//
//                                }
//                            });
//                        }
//                    });
//                }
//            });
        }else{
            MNotificationClass.ShowToast(getContext(),"Enable Internet Connection");
        }

    }

    @Override
    public void onClick(View view) {
        if (R.id.add_item == view.getId()) {

            DialogForAddNewItemEntry AddItemEntry = new DialogForAddNewItemEntry();
            Bundle bundle = new Bundle();
            bundle.putInt("keyid", 0);
            bundle.putString("type", "new");
            bundle.putString("name", null);
            bundle.putString("price", null);
            bundle.putString("code", null);

            AddItemEntry.setArguments(bundle);
            AddItemEntry.show(getFragmentManager(), "DialogAddItem");


        }
        switch (view.getId()) {
            case R.id.btnhorizontal1:
                FragmentAddItemCompleteStockGridView fragment = new FragmentAddItemCompleteStockGridView();
                getChildFragmentManager().beginTransaction()
                        .add(R.id.GridViewcontainer, fragment)
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.btnhorizontal2:

                FragmentAddItemTreeView frag = new FragmentAddItemTreeView();
                getChildFragmentManager().beginTransaction()
                        .add(R.id.GridViewcontainer, frag)
                        .addToBackStack(null)
                        .commit();

                MNotificationClass.ShowToastTem(getContext(), "Tree View  ");

                break;
            case R.id.btnhorizontal3:

                MNotificationClass.ShowToastTem(getContext(), "other View  ");

                break;
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onStop() {
        super.onStop();
        databaseHelper.close();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        databaseHelper.close();
    }

    public void fillGridView() {
        fragment = new FragmentAddItemCompleteStockGridView();
        getChildFragmentManager().beginTransaction()
                .add(R.id.GridViewcontainer, fragment)
                .addToBackStack(null)
                .commit();
    }


    public void methodItem3Name(String name) {
        final Prefrence prefrence = new Prefrence(getContext());
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Updating item3");
        progressDialog.show();
        final Item3NameApis item3NameApis = new Item3NameApis(getContext(), progressDialog, databaseHelper);
        Log.e("status", "Flag5");
        item3NameApis.trigerAllMethod(prefrence.getClientIDSession(), new Item3NameApis.item3namefunListener() {
            @Override
            public void method(String success, String funType) {
                MNotificationClass.ShowToast(getContext(),"All Done");
            }
        });
//        item3NameApis.getNewEditedDataFromServerItem3Name(prefrence.getClientIDSession(), new Item3NameApis.item3namefunListener() {
//            @Override
//            public void method(String success, String funType) {
//                Log.e("status", "Flag6");
//                item3NameApis.SendEditedDataFromSqliteToItem3NameServer(prefrence.getClientIDSession(), new Item3NameApis.item3namefunListener() {
//                    @Override
//                    public void method(String success, String funType) {
//                        progressDialog.show();
//                        Log.e("status", "Flag7");
//                        item3NameApis.getNewInsertedDataFromServerItem3Name(prefrence.getClientIDSession(), new Item3NameApis.item3namefunListener() {
//                            @Override
//                            public void method(String success, String funType) {
//                                progressDialog.show();
//                                Log.e("status", "Flag8");
//                                item3NameApis.SendNewDatafromSqliteToServer(prefrence.getClientIDSession());
//                            }
//                        });
//                    }
//                });
//            }
//        });
    }

    public void updateGridView(){
        fragment.FillRecyclerViewAdapter();
    }
}

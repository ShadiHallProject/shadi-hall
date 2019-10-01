package org.by9steps.shadihall.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import org.by9steps.shadihall.R;

public class Salepur1AddNewActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salepur1_add_new);





    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=new MenuInflater(this);
        inflater.inflate(R.menu.cb_menu, menu);
        MenuItem settings = menu.findItem(R.id.action_settings);
        settings.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
           onBackPressed();
        } else if (item.getItemId() == R.id.action_print) {
//            try {
//                createPdf();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (DocumentException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            return true;
        } else if (item.getItemId() == R.id.action_refresh) {
//            if (isConnected()) {
//                refereshTables(getContext());
//            } else {
//                Toast.makeText(getContext(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
//            }
        }
        return super.onOptionsItemSelected(item);
    }
}

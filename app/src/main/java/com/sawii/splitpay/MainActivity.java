package com.sawii.splitpay;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.sawii.splitpay.Database.Account;
import com.sawii.splitpay.Database.DatabaseHandler;
import com.sawii.splitpay.Utils.Utils;

public class MainActivity extends FragmentActivity implements NewAccountDialogFragment.NewAccountDialogListener {

    private SimpleCursorAdapter dataAdapter;
    private DatabaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         db = new DatabaseHandler(this);
        Button button = findViewById(R.id.add_account);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNewAccountDialog();
            }
        });

        displayListView();
    }

    private void displayListView(){

        Cursor cursor = db.fetchAllAccounts();


        String[] columns = new String[] {
                DatabaseHandler.KEY_ID,
                DatabaseHandler.KEY_NAME,
                DatabaseHandler.KEY_TOTAL_PAID
        };

        int[] to = new int[] {
                R.id.id_wasted,
                R.id.textview_name_result,
                R.id.textview_total_paid_result
        };
        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.account_list_layout,
                cursor,
                columns,
                to,
                0);

        ListView listView = findViewById(R.id.account_list);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Cursor cur = (Cursor)dataAdapter.getItem(position);
                cur.moveToPosition(position);
                int id_ = cur.getInt(cur.getColumnIndexOrThrow("_id"));




                Intent intent = new Intent(MainActivity.this, AccountActivity.class);
                intent.putExtra("account_id",id_);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        EditText name = dialog.getDialog().findViewById(R.id.account_dialog_name);
        EditText participants = dialog.getDialog().findViewById(R.id.account_dialog_participants);
        Account account = new Account(name.getText().toString(), Utils.splitMembers(participants.getText().toString()));
        db.addAccount(account);
        displayListView();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    public void showNewAccountDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new NewAccountDialogFragment();
        dialog.show(getFragmentManager(), "NewAccountDialogFragment");
    }


}

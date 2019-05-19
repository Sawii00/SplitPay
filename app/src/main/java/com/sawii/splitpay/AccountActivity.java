package com.sawii.splitpay;

import android.app.DialogFragment;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.sawii.splitpay.Database.DatabaseHandler;

public class AccountActivity extends FragmentActivity implements NewExpenseDialogFragment.NewExpenseDialogListener{
    private DatabaseHandler databaseHandler;
    private SQLiteDatabase db;
    int account_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_account);
        databaseHandler = new DatabaseHandler(this);
        Button button = findViewById(R.id.add_expense);
        button. setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNewExpenseDialog();
            }
        });
        Bundle data = getIntent().getExtras();
        account_id = data.getInt("account_id");

        db = databaseHandler.getReadableDatabase();
    }

    public void showNewExpenseDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new NewExpenseDialogFragment();
        dialog.show(getFragmentManager(), "NewExpenseDialogFragment");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }
}

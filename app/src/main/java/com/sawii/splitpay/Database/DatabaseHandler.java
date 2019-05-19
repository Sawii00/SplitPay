package com.sawii.splitpay.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import java.util.Random;
import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {

    private SQLiteDatabase db = this.getWritableDatabase();

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "SplitPayDB";

    public static final String TABLE_ACCOUNTS = "Accounts";
    public static final String TABLE_EXPENSES = "Expenses";

    //Accounts table
    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "_name";
    public static final String KEY_PARTECIPANTS = "_partecipants";
    public static final String KEY_TOTAL_PAID = "_total_paid";

    //Expenses table
    public static final String KEY_TRANSACTION_ID = "_id";
    public static final String KEY_GROUP_ID = "_groupId";
    public static final String KEY_AMOUNT = "_amount";
    public static final String KEY_PAID_BY = "_paidby";
    public static final String KEY_DESCRIPTION = "_description";


    public DatabaseHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ACCOUNTS_TABLE = "CREATE TABLE " + TABLE_ACCOUNTS +
                "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_NAME + " TEXT," +
                KEY_PARTECIPANTS + " INTEGER," +
                KEY_TOTAL_PAID+" INTEGER"+ ")";
        db.execSQL(CREATE_ACCOUNTS_TABLE);
        String CREATE_EXPENSES_TABLE = "CREATE TABLE " + TABLE_EXPENSES +
                "(" +KEY_TRANSACTION_ID+" INTEGER," +
                KEY_GROUP_ID + " INTEGER," +
                KEY_AMOUNT + " REAL," +
                KEY_PAID_BY + " TEXT," +
                KEY_DESCRIPTION + " TEXT"+ ")";
        db.execSQL(CREATE_EXPENSES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);

        // Create tables again
        onCreate(db);
    }
//LOOK UP INNER JOIN AND SQLITE QUERIES http://www.sqlitetutorial.net/


    public boolean addAccount(Account account){
        Cursor cursor = null;
        try{
        cursor = db.rawQuery("SELECT * FROM "+TABLE_ACCOUNTS+" WHERE "+KEY_NAME+" = "+account.getName(), null);
        }catch(SQLException e){
            e.printStackTrace();
        }
        if(isCursorEmpty(cursor)){
            ContentValues cv = new ContentValues();
            cv.put(KEY_NAME, account.getName());
            cv.put(KEY_PARTECIPANTS, account.membersToString());
            cv.put(KEY_TOTAL_PAID, 0);
            db.insert(TABLE_ACCOUNTS, null, cv);
            return true;
        }else{
            return false;
        }
    }
    public boolean addExpense(Account account, Expense expense){
        Cursor cursor = null;
        try{
            cursor = db.rawQuery("Select * from "+TABLE_ACCOUNTS+" where "+KEY_NAME+" = "+account.getName(), null);
        }catch(SQLException e){
            e.printStackTrace();
        }
        if(isCursorEmpty(cursor))return false;
        cursor.moveToFirst();
        int id = cursor.getInt(cursor.getColumnIndex(KEY_ID));

        long transaction = validateID(expense.getId());

        ContentValues cv = new ContentValues();
        cv.put(KEY_TRANSACTION_ID, transaction);
        cv.put(KEY_GROUP_ID, id);
        cv.put(KEY_AMOUNT, expense.getAmount());
        cv.put(KEY_PAID_BY, expense.getPaid_by().getName());
        cv.put(KEY_DESCRIPTION, expense.getDescription());
        db.insert(TABLE_EXPENSES, null, cv);

        updateAccountBalance(account, expense);


        return true;
    }

    //@TODO check if this works
    private long validateID(long id){
        Cursor check_id = db.rawQuery("Select * from "+TABLE_EXPENSES+" where "+KEY_TRANSACTION_ID+" = "+ id, null);
        if (isCursorEmpty(check_id)){
            return id;
        }
        else{
            Random rand = new Random();
            long new_id = rand.nextLong();
            return validateID(new_id);
        }

    }

    private void updateAccountBalance(Account account, Expense expense){
        Cursor cursor = db.rawQuery("Select * from "+TABLE_ACCOUNTS+" where "+KEY_NAME+" = "+account.getName(), null);

        if(!isCursorEmpty(cursor)){
            cursor.moveToFirst();
            double current_balance = cursor.getDouble(cursor.getColumnIndex(KEY_TOTAL_PAID));
            current_balance += expense.getAmount();
            ContentValues contentValues = new ContentValues();

            contentValues.put(KEY_NAME, account.getName());
            contentValues.put(KEY_PARTECIPANTS, account.membersToString());
            contentValues.put(KEY_TOTAL_PAID, current_balance);

            db.update(TABLE_ACCOUNTS, contentValues,KEY_NAME+account.getName(), null);

        }


    }

    public boolean removeExpense(Expense expense){

        return db.delete(TABLE_EXPENSES, KEY_TRANSACTION_ID + "=" + expense.getId(), null) > 0;
    }

    public boolean removeAccount(Account account){
        Cursor cursor = db.rawQuery("Select * from "+TABLE_ACCOUNTS+" where "+KEY_NAME+" = "+account.getName(), null);
        if (isCursorEmpty(cursor))return false;
        cursor.moveToFirst();
        int group_id = cursor.getInt(cursor.getColumnIndex(KEY_ID));
        return db.delete(TABLE_ACCOUNTS, KEY_NAME + "=" + account.getName(), null) > 0 && db.delete(TABLE_EXPENSES, KEY_GROUP_ID + " = "+group_id, null) > 0;
    }

    private boolean isCursorEmpty(Cursor cursor){

        if (cursor == null || !cursor.moveToFirst() || cursor.getCount() == 0) return true;
        return false;
    }
    public Cursor fetchAllAccounts(){
        Cursor cursor = db.rawQuery("Select "+KEY_ID+ ", "+KEY_NAME+", "+ KEY_TOTAL_PAID+" from " + TABLE_ACCOUNTS, null);
        if(cursor != null)cursor.moveToFirst();
        return cursor;
    }

}

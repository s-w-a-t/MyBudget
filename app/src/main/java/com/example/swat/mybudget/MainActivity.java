package com.example.swat.mybudget;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.swat.mybudget.data.BudgetContract.ReservEntry;
import com.example.swat.mybudget.data.BudgetContract.NegativeEntry;
import com.example.swat.mybudget.data.BudgetContract.PositiveEntry;
import com.example.swat.mybudget.data.BudgetContract.ShoppingEntry;
import com.example.swat.mybudget.data.BudgetDbBalance;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    //add id item context menu
    private static final int DELETE_ID = 1;

    TextView tvPosBalMon;
    TextView tvNegBalMon;
    TextView tvPosBalMidDay;
    TextView tvNegBalMidDay;
    TextView tvResBal;
    TextView tvBalShoppList;
    TextView tvDateShoppList;

    EditText etValueNameNegBal;
    EditText etValueCostNegBal;

    Button btnPosBal;
    Button btnResBal;
    Button btnAddBuying;
    Button btnClearShoppList;

    ListView lvShoppingList;

    Intent intentNextActivity;

    BudgetDbBalance budgetDbBalance;
    SQLiteDatabase dbBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //add view
        findView();

        //add onClickListener to view
        addOnClickListener();

        //connect to all data bases
        connectToDB();

        //add data to ListView
        fillShoppingListFromDb();

        //add context menu to ListView
        registerForContextMenu(lvShoppingList);
    }

    private void findView() {
        tvPosBalMon = (TextView) findViewById(R.id.tvPosBalMonth);
        tvNegBalMon = (TextView) findViewById(R.id.tvNegBalMonth);
        tvPosBalMidDay = (TextView) findViewById(R.id.tvPosBalMidDay);
        tvNegBalMidDay = (TextView) findViewById(R.id.tvNegBalMidDay);
        tvResBal = (TextView) findViewById(R.id.tvReservBalance);
        tvBalShoppList = (TextView) findViewById(R.id.tvBalanceShoppList);
        tvDateShoppList = (TextView) findViewById(R.id.tvDateShoppList);

        etValueNameNegBal = (EditText) findViewById(R.id.etValueName);
        etValueCostNegBal = (EditText) findViewById(R.id.etValueCost);

        btnPosBal = (Button) findViewById(R.id.btnPosBalance);
        btnResBal = (Button) findViewById(R.id.btnReservBalance);
        btnAddBuying = (Button) findViewById(R.id.btnAddBuying);
        btnClearShoppList = (Button) findViewById(R.id.btnClearShoppList);

        lvShoppingList = (ListView) findViewById(R.id.lvShoppList);
    }

    //add OnClickListener to view
    private void addOnClickListener() {
        btnPosBal.setOnClickListener(this);
        btnResBal.setOnClickListener(this);
        btnAddBuying.setOnClickListener(this);
        btnClearShoppList.setOnClickListener(this);
        tvPosBalMon.setOnClickListener(this);
        tvNegBalMon.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //set text in our TextView
        setTextInTextView();
    }

    //add Sum with data base to TextView
    private void setTextInTextView() {
        getSum(tvPosBalMon);
        getSum(tvNegBalMon);
        getSum(tvBalShoppList);
        getSum(tvResBal);
        setDateFromShoppingDb(tvDateShoppList);
        getAvgPos();
        getAvgNeg();
    }

    // get Sum from positive, negative balance and shopping list
    private void getSum(TextView textView) {
        switch (textView.getId()) {

            // select sum from positive balance
            case R.id.tvPosBalMonth:
                Cursor curPosBalMonth = dbBalance.rawQuery("SELECT SUM("
                        + PositiveEntry.COLUMN_VALUE + ")"
                        + " FROM " + PositiveEntry.TABLE_NAME,
                        null);
                if (curPosBalMonth.moveToFirst()) {
                    String resultsSumPosBal = curPosBalMonth.getString(0);
                    textView.setText(resultsSumPosBal);
                    curPosBalMonth.close();
                }
                break;

            //select sum from negative balance and shopping list
            case R.id.tvNegBalMonth:
                if (!tableIsEmpty(NegativeEntry.TABLE_NAME) & !tableIsEmpty(ShoppingEntry.TABLE_NAME)) {
                    Cursor curNegAndShop = dbBalance.rawQuery("SELECT "
                            + "(SELECT SUM(" + NegativeEntry.COLUMN_VALUE + ")"
                            + " FROM " + NegativeEntry.TABLE_NAME
                            + ") + "
                            + "(SELECT SUM(" + ShoppingEntry.COLUMN_VALUE_COST + ")"
                            + " FROM " + ShoppingEntry.TABLE_NAME + ")"
                            , null);
                    if (curNegAndShop.moveToFirst()) {
                        String resultsSumNegBal = curNegAndShop.getString(0);
                        textView.setText(resultsSumNegBal);
                        curNegAndShop.close();
                    }
                    return;

                } else if (tableIsEmpty(ShoppingEntry.TABLE_NAME)) {
                    Cursor curFromNegBal = dbBalance.rawQuery("SELECT SUM("
                            + NegativeEntry.COLUMN_VALUE
                            + ") FROM " + NegativeEntry.TABLE_NAME, null);
                    if (curFromNegBal.moveToFirst()) {
                        String resultsSumNegBal = curFromNegBal.getString(0);
                        textView.setText(resultsSumNegBal);
                        curFromNegBal.close();
                    }
                    return;

                } else if (tableIsEmpty(NegativeEntry.TABLE_NAME)) {
                    Cursor curFromShoppBal = dbBalance.rawQuery("SELECT SUM("
                            + ShoppingEntry.COLUMN_VALUE_COST
                            + ") FROM " + ShoppingEntry.TABLE_NAME, null);
                    if (curFromShoppBal.moveToFirst()) {
                        String resultsSumNegBal = curFromShoppBal.getString(0);
                        textView.setText(resultsSumNegBal);
                        curFromShoppBal.close();
                    }
                }
                break;

            //select sum from shopping list
            case R.id.tvBalanceShoppList:
                Cursor curShoppBalance = dbBalance.rawQuery("SELECT SUM("
                        + ShoppingEntry.COLUMN_VALUE_COST + ")"
                        + " FROM " + ShoppingEntry.TABLE_NAME, null);
                if (curShoppBalance.moveToFirst()) {
                    String resultsSumShoppBal = curShoppBalance.getString(0);
                    textView.setText(resultsSumShoppBal);
                    curShoppBalance.close();
                }
                break;

            case R.id.tvReservBalance:
                Cursor curReservBal = dbBalance.rawQuery("SELECT SUM("
                        + ReservEntry.COLUMN_VALUE + ")"
                        + " FROM " + ReservEntry.TABLE_NAME, null);
                if (curReservBal.moveToFirst()) {
                    String resultReserv = curReservBal.getString(0);
                    textView.setText(resultReserv);
                    curReservBal.close();
                }
        }
    }

    //select middle positive balance
    private void getAvgPos() {
        Cursor curGetPosBalMid = dbBalance.rawQuery("SELECT AVG("
                + PositiveEntry.COLUMN_VALUE + ")"
                + " FROM " + PositiveEntry.TABLE_NAME, null);
        if (curGetPosBalMid.moveToFirst()) {
            String resultPosBalMid = curGetPosBalMid.getString(0);
            tvPosBalMidDay.setText(resultPosBalMid);
            curGetPosBalMid.close();
        }
    }

    //select middle negative balance
    private void getAvgNeg() {
        Cursor curGetNegBalMid = dbBalance.rawQuery("SELECT AVG("
                + NegativeEntry.COLUMN_VALUE + ")"
                + " FROM " + NegativeEntry.TABLE_NAME, null);
        if (curGetNegBalMid.moveToFirst()) {
            String resultNegBalMid = curGetNegBalMid.getString(0);
            tvNegBalMidDay.setText(resultNegBalMid);
            curGetNegBalMid.close();
        }
    }


    private void setDateFromShoppingDb(TextView textView) {
        Cursor cursorDate = dbBalance.rawQuery("SELECT "
                + ShoppingEntry.COLUMN_DATE
                + " FROM " + ShoppingEntry.TABLE_NAME
                + " WHERE " + ShoppingEntry.COLUMN_COUNTER + " = 1",
                null);
        if (cursorDate.moveToFirst()) {
            String resultDate = cursorDate.getString(0);
            textView.setText(resultDate);
        }
    }

    //connect to DB
    private void connectToDB() {
        budgetDbBalance = new BudgetDbBalance(this);
        dbBalance = budgetDbBalance.getWritableDatabase();
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.delete_all_data) {
            RemoveAllData removeAllData = new RemoveAllData(budgetDbBalance, dbBalance);
            removeAllData.removeAll();
            onRestart();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //go to activity positive balance
            case R.id.btnPosBalance:
                intentNextActivity = new Intent(this, PosBalance.class);
                startActivity(intentNextActivity);
                break;
            //go to activity positive balance
            case R.id.tvPosBalMonth:
                intentNextActivity = new Intent(this, PosBalance.class);
                startActivity(intentNextActivity);
                break;
            //go to activity negative balance
            case R.id.tvNegBalMonth:
                intentNextActivity = new Intent(this, NegBalance.class);
                startActivity(intentNextActivity);
                break;
            //go to activity reservation balance
            case R.id.btnReservBalance:
                intentNextActivity = new Intent(this, ReservBalance.class);
                startActivity(intentNextActivity);
                break;
            //add new buying in shopping list
            case R.id.btnAddBuying:
                addRecShopping();
                setDateFromShoppingDb(tvDateShoppList);
                fillShoppingListFromDb();
                getSum(tvBalShoppList);
                getSum(tvNegBalMon);
                getAvgNeg();

                //clear EditText
                etValueNameNegBal.setText("");
                etValueCostNegBal.setText("");
                break;
            //clear shopping list
            case R.id.btnClearShoppList:
                //TODO зробити провірку на пусту таблицю другим способом
                Cursor cursor = dbBalance.query(ShoppingEntry.TABLE_NAME, null, null, null, null,null,null);
                if (cursor.getCount() == 0) {
                    return;
                } else {
                    //add data from shopping list to db negative
                    insertDataToNegBalMonth();

                    // drop table and reset id
                    dbBalance.delete(ShoppingEntry.TABLE_NAME, null, null);
                    dbBalance.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + ShoppingEntry.TABLE_NAME + "'");

                    //reload(clear) shopping list
                    fillShoppingListFromDb();
                    // clear date
                    setDateFromShoppingDb(tvDateShoppList);
                    // clear buying sum from shopping list
                    getSum(tvBalShoppList);
                    //reload middle negative balance
                    getAvgNeg();

                    tvDateShoppList.setText("");
                }
                break;
        }
    }

    //method add data to negative table from shopping balance
    private void insertDataToNegBalMonth() {
        String date = tvDateShoppList.getText().toString();
        String value = tvBalShoppList.getText().toString();

        ContentValues cv = new ContentValues();

        cv.put(NegativeEntry.COLUMN_DATE, date);
        cv.put(NegativeEntry.COLUMN_VALUE, value);

        dbBalance.insert(NegativeEntry.TABLE_NAME, null, cv);
    }

    //add data to shopping table
    private void addRecShopping() {
        String date = getDate();
        String time = getDateTime();
        String name = etValueNameNegBal.getText().toString();
        String cost = etValueCostNegBal.getText().toString();

        ContentValues cv = new ContentValues();

        cv.put(ShoppingEntry.COLUMN_VALUE_NAME, name);
        cv.put(ShoppingEntry.COLUMN_VALUE_COST, cost);
        cv.put(ShoppingEntry.COLUMN_TIME, time);
        cv.put(ShoppingEntry.COLUMN_DATE,date);

        dbBalance.insert(ShoppingEntry.TABLE_NAME, null, cv);
    }

    //delete rec from shopping list
    private void delRecShopp(long id) {
        dbBalance.delete(ShoppingEntry.TABLE_NAME, ShoppingEntry.COLUMN_COUNTER + "=" + id, null);
    }

    //add data to ListView
    private void fillShoppingListFromDb() {

        Cursor cursor = dbBalance.query(ShoppingEntry.TABLE_NAME, null, null, null, null, null, null, null);

        String[] from = new String[]{ShoppingEntry.COLUMN_TIME, ShoppingEntry.COLUMN_VALUE_NAME, ShoppingEntry.COLUMN_VALUE_COST};
        int[] to = new int[]{R.id.tvShoppTime, R.id.tvShoppName, R.id.tvShoppCost};

        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(this,
                R.layout.list_item_buying,
                cursor,
                from,
                to,
                0);

        lvShoppingList.setAdapter(simpleCursorAdapter);
    }

    //format time
    private String getDateTime() {
        SimpleDateFormat timeFormat = new SimpleDateFormat(
                "HH:mm:ss", Locale.getDefault());
        Date time = new Date();
        return timeFormat.format(time);
    }

    //format date
    private String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    //check is table empty or no
    private boolean tableIsEmpty(String tableName) {
        Cursor cursor = dbBalance.query(tableName, null, null, null, null, null, null);
        boolean temp = false;
        if (cursor.getCount() == 0) {
            temp = true;
        }
        cursor.close();
        return temp;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        //add context menu item
        menu.add(0, DELETE_ID, 0, R.string.delete_rec_from_table);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == DELETE_ID) {
            //get from the context menu data from ListView
            AdapterView.AdapterContextMenuInfo acmi =
                    (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

            //delete rec from ListView
            delRecShopp(acmi.id);
            //reload ListView
            fillShoppingListFromDb();

            getSum(tvNegBalMon);

            getSum(tvBalShoppList);
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbBalance.close();
    }
}

package com.example.swat.mybudget;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
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

import com.example.swat.mybudget.data.BudgetContract.PositiveEntry;
import com.example.swat.mybudget.data.BudgetDbBalance;


public class PosBalance extends ActionBarActivity implements View.OnClickListener {

    final String LOG_TAG = "myLogs";
    TextView tvSumAllPosBal;
    EditText etValuePosBal;
    Button btnAddPosBal;
    Button btnClearPosBal;
    ListView lvPosBal;
    BudgetDbBalance budgetDbPosBal;
    SimpleCursorAdapter scAdapter;
    SQLiteDatabase db;
    //add id item context menu
    private static final int DELETE_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.positive_balance);

        tvSumAllPosBal = (TextView) findViewById(R.id.tvSumAllPosBal);

        etValuePosBal = (EditText) findViewById(R.id.etValuePosBal);

        btnAddPosBal = (Button) findViewById(R.id.btnAddPosBal);
        btnAddPosBal.setOnClickListener(this);

        btnClearPosBal = (Button) findViewById(R.id.btnClearPosBal);
        btnClearPosBal.setOnClickListener(this);

        lvPosBal = (ListView) findViewById(R.id.lvPosBal);
        //add context menu in ListView
        registerForContextMenu(lvPosBal);

        budgetDbPosBal = new BudgetDbBalance(this);
        db = budgetDbPosBal.getWritableDatabase();

        fillListFromDb();
        getSum(tvSumAllPosBal);
    }

    //insert data in table
    private void addRec(String txt) {
        ContentValues cv = new ContentValues();
        cv.put(PositiveEntry.COLUMN_VALUE, txt);
        db.insert(PositiveEntry.TABLE_NAME, null, cv);
    }

    //delete rec from table
    private void delRec(long id) {
        db.delete(PositiveEntry.TABLE_NAME, PositiveEntry.COLUMN_COUNTER + "=" + id, null);
    }

    // get sum from from all columns wirh positive balance
    private void getSum(TextView textView) {
        Cursor cursor = db.rawQuery("SELECT SUM("+PositiveEntry.COLUMN_VALUE+")" +
                " FROM "+PositiveEntry.TABLE_NAME+"",null);
        if(cursor.moveToFirst()) {
            String results = cursor.getString(0);
            textView.setText(results);
        }
    }

    // add data in ListView. Use SimpleCursorAdapter.
    private void fillListFromDb() {
        //get all data from table
        Cursor cursor = db.query(PositiveEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null,
                null);

        String[] from = new String[]{PositiveEntry.COLUMN_COUNTER, PositiveEntry.COLUMN_DATE, PositiveEntry.COLUMN_VALUE};
        int[] to = new int[]{R.id.tvCounterInPosBal, R.id.tvDateInPosBal, R.id.tvValueInPosBal};

        //make simple cursor adapter
        scAdapter = new SimpleCursorAdapter(this, R.layout.list_item_pos_bal, cursor, from, to, 0);

        //add adapter in ListView
        lvPosBal.setAdapter(scAdapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        //add a context menu item
        menu.add(0, DELETE_ID, 0, R.string.delete_rec_from_table);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == DELETE_ID) {
            //get from the context menu data from ListView
            AdapterView.AdapterContextMenuInfo acmi =
                    (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            //delete rec from table with id
            delRec(acmi.id);
            // reload ListView
            fillListFromDb();
            //add sum all columns (value) in text view
            getSum(tvSumAllPosBal);
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //insert data into table and put in ListView
            case R.id.btnAddPosBal:
                Log.d(LOG_TAG, "---- Insert in positive table: ---");
                String value = etValuePosBal.getText().toString();
                addRec(value);
                fillListFromDb();
                //add sum all columns (value) in text view
                getSum(tvSumAllPosBal);
                etValuePosBal.setText("");
                break;
            //drop table and reset column _id (autoincrement)
            case R.id.btnClearPosBal:
                db.delete(PositiveEntry.TABLE_NAME, null, null);
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + PositiveEntry.TABLE_NAME + "'");
                fillListFromDb();
                //add sum all columns (value) in text view
                getSum(tvSumAllPosBal);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy");
        // close the data base
        if (budgetDbPosBal != null) {
            budgetDbPosBal.close();
        }
    }

}

package com.example.swat.mybudget;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.swat.mybudget.data.BudgetDbBalance;
import static com.example.swat.mybudget.data.BudgetContract.*;

/**
 * Created by swat on 10.07.2015.
 */
public class ReservBalance extends ActionBarActivity {
    Button btnAddResBal;
    EditText etResBal;
    TextView tvResBal;
    ListView lvResBal;

    SQLiteDatabase db;
    SimpleCursorAdapter scAdapter;
    BudgetDbBalance budgetDbResBal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reserv_balance);
        
        etResBal = (EditText) findViewById(R.id.etResBal);
        tvResBal = (TextView) findViewById(R.id.tvResBal);

        lvResBal = (ListView) findViewById(R.id.lvResBal);

        btnAddResBal = (Button) findViewById(R.id.btnAddResBal);
        btnAddResBal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertDataToTable();
            }
        });

        budgetDbResBal = new BudgetDbBalance(this);
        db = budgetDbResBal.getWritableDatabase();

        fillListFromDb();
        setDataInTextView();
    }

    private void insertDataToTable() {
        String value = etResBal.getText().toString();

        ContentValues cv = new ContentValues();
        cv.put(ReservEntry.COLUMN_VALUE, value);

        db.insert(ReservEntry.TABLE_NAME, null, cv);

        //add data in ListView
        fillListFromDb();

        //add data in TextView
        setDataInTextView();

        etResBal.setText("");
    }

    //function add data in ListView
    private void fillListFromDb() {
        //get all data from table
        Cursor curResBalList = db.query(ReservEntry.TABLE_NAME, null, null, null, null, null, null, null);

        String[] from = new String[]{ReservEntry.COLUMN_DATE, ReservEntry.COLUMN_VALUE};
        int[] to = new int[]{R.id.tvResBalDateList, R.id.tvResBalValueList};

        //make simple cursor adapter
        scAdapter = new SimpleCursorAdapter(this, R.layout.list_item_reserv_bal, curResBalList, from, to, 0);

        //add adapter in ListView
        lvResBal.setAdapter(scAdapter);
    }

    //function add data in TextView
    private void setDataInTextView() {
        Cursor cursor = db.rawQuery("SELECT SUM(" + ReservEntry.COLUMN_VALUE + ")" + " FROM " + ReservEntry.TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            String result = cursor.getString(0);
            tvResBal.setText(result);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}

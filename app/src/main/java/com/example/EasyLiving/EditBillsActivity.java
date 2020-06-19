package com.example.EasyLiving;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

import java.time.LocalDate;

public class EditBillsActivity extends AppCompatActivity {

    private SQLiteDatabase db = null;
    private final String DB_NAME = "lifeSupportDB";
    private String sellID;
    private EditText billName;
    private EditText billDueDate;
    private EditText billAmount;
    private EditText billFreq;
    private EditText billCompany;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bills);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            sellID = extras.getString("sellID");
        }

        billName = findViewById(R.id.billName);
        billDueDate = findViewById(R.id.billDueDate);
        billAmount = findViewById(R.id.billAmount);
        billFreq = findViewById(R.id.billFreq);
        billCompany = findViewById(R.id.billCompany);
        GetBill(sellID);
    }

    public void GetBill(String id) {
        db = this.openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        Cursor c = db.rawQuery("SELECT * FROM tblBill WHERE billID = " + sellID , null);

        if(c != null) {
            if(c.moveToFirst()) {
                do{
                    billName.setText(c.getString(1));
                    billDueDate.setText(c.getString(2));
                    billAmount.setText(c.getString(3));
                    billFreq.setText(c.getString(4));
                    billCompany.setText(c.getString(5));
                }while(c.moveToNext());
            }
        }
        db.close();
    } // GetBill()

    public void UpdateBill(View v) {

        /*
            UPDATE tblBill
            SET
                name = billName.getText(),
                dueDate = billDueDate.getText(),
                amount = billAmount.getText(),
                freq = billFreq.getText(),
                company = billCompany.getText()
            WHERE
	            billID = sellID
         */

        db = this.openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        db.execSQL("UPDATE tblBill SET " +
                "name = '" + billName.getText().toString() + "', " +
                "dueDate = '" + billDueDate.getText().toString() + "', " +
                "amount = '" + billAmount.getText().toString() + "', " +
                "freq = '" + billFreq.getText().toString() + "', " +
                "company = '" + billCompany.getText().toString() + "' " +
                "WHERE billID = " + sellID + "");

        db.close(); // close the door, we don't live in a barn!

    } // UpdateBill()

    public void DeleteBill(View v) {
        db = this.openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);

        db.execSQL("UPDATE tblBill SET status = 'C' WHERE billID = " + sellID + "");

        db.close(); // close the door, we don't live in a barn!
    } // DeleteBill()
}
package com.example.EasyLiving;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Locale;

public class EditBillsActivity extends AppCompatActivity {

    private SQLiteDatabase db = null;
    private final String DB_NAME = "lifeSupportDB";
    private String sellID;
    private EditText billName;
    private EditText billDueDate;
    private EditText billAmount;
    private EditText billFreq;
    private EditText billCompany;
    private EditText billStatus;
    Calendar myCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bills);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            sellID = extras.getString("sellID");
            if ((sellID!= null) && sellID.equals("AddBill")){
                Button update_button = findViewById(R.id.update_button);
                Button delete_button = findViewById(R.id.delete_button);
                update_button.setEnabled(false);
                delete_button.setEnabled(false);
                sellID = null;
            }
            else{
                Button add_bill_button = findViewById(R.id.add_bill_button);
                //add_bill_button.setEnabled(false);
            }
        }

        billName = findViewById(R.id.billName);
        billDueDate = findViewById(R.id.billDueDate);
        billAmount = findViewById(R.id.billAmount);
        billFreq = findViewById(R.id.billFreq);
        billCompany = findViewById(R.id.billCompany);

        myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        billDueDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(EditBillsActivity.this, date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        GetBill(sellID);
    }

    @Override
    // go back last page and refresh list view
    public void onBackPressed() {
        Intent billActivePage = new Intent(EditBillsActivity.this,
                BillsActivity.class);
        startActivity(billActivePage);
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        billDueDate.setText(sdf.format(myCalendar.getTime()));
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
            c.close();
        }

    } // GetBill()

    public void UpdateBill(View v) {

        db = this.openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        db.execSQL("UPDATE tblBill SET " +
                "name = '" + billName.getText().toString() + "', " +
                "dueDate = '" + billDueDate.getText().toString() + "', " +
                "amount = '" + billAmount.getText().toString() + "', " +
                "freq = '" + billFreq.getText().toString() + "', " +
                "company = '" + billCompany.getText().toString() + "' " +
                "WHERE billID = " + sellID + "");

        //Toast.makeText(this, "bill updated!", Toast.LENGTH_SHORT).show();
        db.close(); // close the door, we don't live in a barn!

        Intent billActivePage = new Intent(EditBillsActivity.this,
                BillsActivity.class);
        billActivePage.putExtra("value", "updated");
        startActivity(billActivePage);
    } // UpdateBill()

    public void AddBill(View v) {

        if (TextUtils.isEmpty(billName.getText().toString())) {
            Toast.makeText(this, "bill name is empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(billDueDate.getText().toString())) {
            Toast.makeText(this, "bill due date is empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(billAmount.getText().toString())) {
            Toast.makeText(this, "bill amount is empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(billFreq.getText().toString())) {
            Toast.makeText(this, "bill frequncy is empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(billCompany.getText().toString())) {
            Toast.makeText(this, "bill company is empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        db = this.openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        db.execSQL("INSERT INTO tblBill(name, dueDate,amount, freq, company, status) " +
                "VALUES('" + billName.getText().toString() + "', " +
                "'" + billDueDate.getText().toString() + "', " +
                "'" + billAmount.getText().toString() + "', " +
                "'" + billFreq.getText().toString() + "'," +
                "'" + billCompany.getText().toString() + "', " +
                "'A')");
        db.close(); // close the door, we don't live in a barn!

        Intent billActivePage = new Intent(EditBillsActivity.this,
                BillsActivity.class);
        billActivePage.putExtra("value", "added");
        startActivity(billActivePage);
    } // AddBill()

    public void ClearBill(View v) {
        // clear all edittext fields
        billName.getText().clear();
        billDueDate.getText().clear();
        billAmount.getText().clear();
        billFreq.getText().clear();
        billCompany.getText().clear();
    } // ClearBill()

    public void DeleteBill(View v) {
        db = this.openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);

        db.execSQL("UPDATE tblBill SET status = 'C' WHERE billID = " + sellID + "");

        db.close(); // close the door, we don't live in a barn!

        Intent billActivePage = new Intent(EditBillsActivity.this,
                BillsActivity.class);
        billActivePage.putExtra("value", "deleted");
        startActivity(billActivePage);

    } // DeleteBill()
}
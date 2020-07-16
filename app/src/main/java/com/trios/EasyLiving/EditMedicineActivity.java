package com.trios.EasyLiving;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditMedicineActivity extends AppCompatActivity {

    private SQLiteDatabase db = null;
    private final String DB_NAME = "lifeSupportDB";
    private String sellID;
    private EditText medicineName;
    private EditText medicinePurpose;
    private EditText medicineInstructions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_medicine);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            sellID = extras.getString("sellID");
            if ((sellID!= null) && sellID.equals("AddMedicine")){
                Button update_button = findViewById(R.id.medicine_update_button);
                Button delete_button = findViewById(R.id.medicine_delete_button);
                update_button.setEnabled(false);
                delete_button.setEnabled(false);
                sellID = null;
            }
            else{
                Button add_medicine_button = findViewById(R.id.medicine_add_button);
                //add_todo_button.setEnabled(false);
            }
        }

        medicineName = findViewById(R.id.medicine_name);
        medicinePurpose = findViewById(R.id.medicine_purpose);
        medicineInstructions = findViewById(R.id.medicine_instructions);

        //updateLabel();
        GetMedicine(sellID);
    }

    @Override
    // go back last page and refresh list view
    public void onBackPressed() {
        Intent medicineActivePage = new Intent(EditMedicineActivity.this,
                MedicineActivity.class);
        startActivity(medicineActivePage);
    }

    public void GetMedicine(String id) {
        db = this.openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        Cursor c = db.rawQuery("SELECT * FROM tblMedicine WHERE medicineID = " + sellID , null);

        if(c != null) {
            if(c.moveToFirst()) {
                do{
                    medicineName.setText(c.getString(1));
                    medicinePurpose.setText(c.getString(2));
                    String[] splitStr = c.getString(3).split("\\s+");
                    medicineInstructions.setText(splitStr[0]);
                }while(c.moveToNext());
            }
            c.close();
        }

    } // GetTodo()

    public void UpdateMedicine(View v) {

        if (TextUtils.isEmpty(medicineName.getText().toString())) {
            Toast.makeText(this, "Name is empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(medicinePurpose.getText().toString())) {
            Toast.makeText(this, "Purpose is empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(medicineInstructions.getText().toString())) {
            Toast.makeText(this, "Instructions are empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        db = this.openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        db.execSQL("UPDATE tblMedicine SET " +
                "name = '" + medicineName.getText().toString() + "', " +
                "purpose= '" + medicinePurpose.getText().toString() + "', " +
                "instructions= '" + medicineInstructions.getText().toString() + "' " +
                "WHERE medicineID = " + sellID + "");

        db.close(); // close the door, we don't live in a barn!

        Intent medicineActivePage = new Intent(EditMedicineActivity.this,
                MedicineActivity.class);
        medicineActivePage.putExtra("value", "updated");
        startActivity(medicineActivePage);
    } // UpdateMedicine()

    public void AddMedicine(View v) {

        if (TextUtils.isEmpty(medicineName.getText().toString())) {
            Toast.makeText(this, "name is empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(medicinePurpose.getText().toString())) {
            Toast.makeText(this, "purpose is empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(medicineInstructions.getText().toString())) {
            Toast.makeText(this, "instructions are empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        db = this.openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        db.execSQL("INSERT INTO tblMedicine(name, purpose, instructions) " +
                "VALUES('" + medicineName.getText().toString() + "', " +
                "'" + medicinePurpose.getText().toString() + "', " +
                "'" + medicineInstructions.getText().toString() + "')");
        db.close(); // close the door, we don't live in a barn!

        Intent medicineActivePage = new Intent(EditMedicineActivity.this,
                MedicineActivity.class);
        medicineActivePage.putExtra("value", "added");
        startActivity(medicineActivePage);

    } // AddMedicine()

    public void ClearMedicine(View v) {
        // clear all edittext fields
        medicineName.getText().clear();
        medicinePurpose.getText().clear();
        medicineInstructions.getText().clear();
    } // ClearMedicine()

    public void DeleteMedicine(View v) {

        db = this.openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);

        db.execSQL("DELETE FROM tblMedicine\n" +
                "WHERE medicineID = " + sellID);

        db.close(); // close the door, we don't live in a barn!

        Intent medicineActivePage = new Intent(EditMedicineActivity.this,
                MedicineActivity.class);
        medicineActivePage.putExtra("value", "deleted");
        startActivity(medicineActivePage);
    } // DeleteMedicine()
}
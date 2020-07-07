package com.trios.EasyLiving;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private SQLiteDatabase db = null;
    private final String DB_NAME = "lifeSupportDB";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbSetup();
    }

    public void SetCat(View v) {
        switch(v.getId()){
            case R.id.bills_button: {
                Intent billsActivity =
                        new Intent(MainActivity.this,
                        BillsActivity.class);
                startActivity(billsActivity);
                break;
            }
            case R.id.cart_button: {
                Intent cartActivity =
                        new Intent(MainActivity.this,
                                CartActivity.class);
                startActivity(cartActivity);
                break;
            }
            case R.id.todo_button: {
                Intent todoActivity =
                        new Intent(MainActivity.this,
                                TodoActivity.class);
                startActivity(todoActivity);
                break;
            }
            case R.id.medicine_button: {
                Intent medicineActivity =
                        new Intent(MainActivity.this,
                                MedicineActivity.class);
                startActivity(medicineActivity);
                break;
            }
        } // switch
    } // SetCat

    @Override
    // close app
    public void onBackPressed() {
        finishAffinity();
    }

    private void dbSetup() {
        // create the db if it does not have an oreo cookies
        db = this.openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);

        // create our bills table if it does not exist
        db.execSQL("CREATE TABLE IF NOT EXISTS tblBill" +
                "(billID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name VARCHAR, dueDate VARCHAR, amount NUMERIC," +
                "freq VARCHAR, company VARCHAR, status CHAR(1))");

        // create our bills history table if it does not exist
        db.execSQL("CREATE TABLE IF NOT EXISTS tblBillHistory" +
                "(billHistoryID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "amount NUMERIC, payedDate VARCHAR, billID INTEGER)");

        // create cart table if it does not exist
        db.execSQL("CREATE TABLE IF NOT EXISTS tblCart" +
                "(cartID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "category VARCHAR, name VARCHAR, createdDate VARCHAR, quantity NUMERIC," +
                "price NUMERIC, grocery VARCHAR, status CHAR(1))");

        // create cart history table if it does not exist
        db.execSQL("CREATE TABLE IF NOT EXISTS tblCartHistory" +
                "(cartHistoryID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "price NUMERIC, payedDate VARCHAR, cartID INTEGER)");

        // create todo table if it does not exist
        db.execSQL("CREATE TABLE IF NOT EXISTS tblTodo" +
                "(todoID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "task VARCHAR, todoWhere VARCHAR, time VARCHAR," +
                "status CHAR(1))");
        // time is not right name, should be date, add time2 for the right time column

        // create todo history table if it does not exist
        db.execSQL("CREATE TABLE IF NOT EXISTS tblTodoHistory" +
                "(todoHistoryID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "completTime VARCHAR, todoID INTEGER)");
    }
}
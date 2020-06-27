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

public class EditCartActivity extends AppCompatActivity {

    private SQLiteDatabase db = null;
    private final String DB_NAME = "lifeSupportDB";
    private String sellID;
    private EditText cartCategory;
    private EditText cartName;
    private EditText cartCreatedDate;
    private EditText cartQuantity;
    private EditText cartprice;
    private EditText cartGrocery;
    private EditText cartStatus;
    Calendar myCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_cart);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            sellID = extras.getString("sellID");
            if ((sellID!= null) && sellID.equals("AddCart")){
                Button update_button = findViewById(R.id.update_button);
                Button delete_button = findViewById(R.id.delete_button);
                update_button.setEnabled(false);
                delete_button.setEnabled(false);
                sellID = null;
            }
            else{
                Button add_cart_button = findViewById(R.id.add_cart_button);
                //add_cart_button.setEnabled(false);
            }
        }

        cartCategory = findViewById(R.id.cart_cartegory);
        cartName = findViewById(R.id.cart_item_name);
        cartCreatedDate = findViewById(R.id.cart_created_date);
        cartQuantity = findViewById(R.id.cart_quantity);
        cartprice = findViewById(R.id.cart_price);
        cartGrocery = findViewById(R.id.cart_grocery);

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

        cartCreatedDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(EditCartActivity.this, date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        GetCart(sellID);
    }

    @Override
    // go back last page and refresh list view
    public void onBackPressed() {
        Intent cartActivePage = new Intent(EditCartActivity.this,
                CartActivity.class);
        startActivity(cartActivePage);
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        cartCreatedDate.setText(sdf.format(myCalendar.getTime()));
    }

    public void GetCart(String id) {
        db = this.openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        Cursor c = db.rawQuery("SELECT * FROM tblCart WHERE cartID = " + sellID , null);

        if(c != null) {
            if(c.moveToFirst()) {
                do{
                    cartCategory.setText(c.getString(1));
                    cartName.setText(c.getString(2));
                    cartCreatedDate.setText(c.getString(3));
                    cartQuantity.setText(c.getString(4));
                    cartprice.setText(c.getString(5));
                    cartGrocery.setText(c.getString(6));
                }while(c.moveToNext());
            }
            c.close();
        }

    } // GetCart()

    public void UpdateCart(View v) {

        db = this.openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        db.execSQL("UPDATE tblCart SET " +
                "category = '" + cartCategory.getText().toString() + "', " +
                "name = '" + cartName.getText().toString() + "', " +
                "createdDate = '" + cartCreatedDate.getText().toString() + "', " +
                "quantity = '" + cartQuantity.getText().toString() + "', " +
                "price = '" + cartprice.getText().toString() + "', " +
                "grocery = '" + cartGrocery.getText().toString() + "' " +
                "WHERE cartID = " + sellID + "");

        //Toast.makeText(this, "bill updated!", Toast.LENGTH_SHORT).show();
        db.close(); // close the door, we don't live in a barn!

        Intent cartActivePage = new Intent(EditCartActivity.this,
                CartActivity.class);
        cartActivePage.putExtra("value", "updated");
        startActivity(cartActivePage);
    } // UpdateCart()

    public void AddCartItems(View v) {

        if (TextUtils.isEmpty(cartCategory.getText().toString())) {
            Toast.makeText(this, "Cart category is empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(cartName.getText().toString())) {
            Toast.makeText(this, "Item name is empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(cartCreatedDate.getText().toString())) {
            Toast.makeText(this, "Created date is empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(cartQuantity.getText().toString())) {
            Toast.makeText(this, "Quantity is empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(cartprice.getText().toString())) {
            Toast.makeText(this, "Price is empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(cartGrocery.getText().toString())) {
            Toast.makeText(this, "Grocery is empty!", Toast.LENGTH_SHORT).show();
            return;
        }
/*
        "(cartID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "category VARCHAR, name VARCHAR, createdDate VARCHAR, quantity NUMERIC," +
                "price NUMERIC, grocery VARCHAR, status CHAR(1))");
*/

        db = this.openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        db.execSQL("INSERT INTO tblCart(category, name, createdDate, quantity, price, grocery, status) " +
                "VALUES('" + cartCategory.getText().toString() + "', " +
                "'" + cartName.getText().toString() + "', " +
                "'" + cartCreatedDate.getText().toString() + "', " +
                "'" + cartQuantity.getText().toString() + "', " +
                "'" + cartprice.getText().toString() + "'," +
                "'" + cartGrocery.getText().toString() + "', " +
                "'A')");
        db.close(); // close the door, we don't live in a barn!

        Intent cartActivePage = new Intent(EditCartActivity.this,
                CartActivity.class);
        cartActivePage.putExtra("value", "added");
        startActivity(cartActivePage);
    } // AddCartItems()

    public void ClearCart(View v) {
        // clear all edittext fields
        cartCategory.getText().clear();
        cartName.getText().clear();
        cartCreatedDate.getText().clear();
        cartQuantity.getText().clear();
        cartprice.getText().clear();
        cartGrocery.getText().clear();
    } // ClearCart()

    public void DeleteCartItem(View v) {
        db = this.openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);

        db.execSQL("UPDATE tblCart SET status = 'C' WHERE cartID = " + sellID + "");

        db.close(); // close the door, we don't live in a barn!

        Intent cartActivePage = new Intent(EditCartActivity.this,
                CartActivity.class);
        cartActivePage.putExtra("value", "deleted");
        startActivity(cartActivePage);

    } // DeleteCartItem()

}
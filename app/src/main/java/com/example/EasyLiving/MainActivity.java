package com.example.EasyLiving;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
}
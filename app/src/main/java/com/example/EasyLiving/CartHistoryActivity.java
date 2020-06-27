package com.example.EasyLiving;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class CartHistoryActivity extends AppCompatActivity {

    private SQLiteDatabase db = null;
    private final String DB_NAME = "lifeSupportDB";
    private ArrayList<Model> cartHistoryList;
    Activity activity;
    private class ViewHolder {
        TextView mCartItemName;
        TextView mQuantity;
        TextView mPrice;
        TextView mGrocery;
        TextView mDayPaid;
    }
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_history);

        FloatingActionButton fab = findViewById(R.id.cart_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tracker = new Intent(CartHistoryActivity.this, MainActivity.class);
                startActivity(tracker);
            }
        });

        cartHistoryList = new ArrayList<Model>();
        GetCart();
        ArrayAdapter<Model> items_adapter = new ArrayAdapter<Model>(this, android.R.layout.simple_list_item_1, cartHistoryList) {

            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                // Get the current item from ListView
                CartHistoryActivity.ViewHolder holder;
                activity = (Activity) this.getContext();
                LayoutInflater inflater = activity.getLayoutInflater();

                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.listview_row_cart_history, null);
                    holder = new CartHistoryActivity.ViewHolder();
                    holder.mCartItemName = (TextView) convertView.findViewById(R.id.sCart_item_name_history);
                    holder.mQuantity = (TextView) convertView.findViewById(R.id.sQuantity_history);
                    holder.mPrice = (TextView) convertView.findViewById(R.id.sPrice_history);
                    holder.mGrocery = (TextView) convertView.findViewById(R.id.sGrocery_history);
                    holder.mDayPaid = (TextView) convertView.findViewById(R.id.sDay_Paid);
                    convertView.setTag(holder);
                } else {
                    holder = (CartHistoryActivity.ViewHolder) convertView.getTag();
                }

                Model listItem = cartHistoryList.get(position);
                holder.mCartItemName.setText(listItem.getCartItemName().toString());
                holder.mQuantity.setText(listItem.getQuantity().toString());
                holder.mPrice.setText(listItem.getPrice().toString());
                holder.mGrocery.setText(listItem.getGrocery().toString());
                holder.mDayPaid.setText(listItem.getDaysPassed().toString());

                if (position % 2 == 0) {
                    convertView.setBackgroundColor(Color.parseColor("#FFF0CCD9"));
                } else {
                    convertView.setBackgroundColor(Color.parseColor("#FFE8F4F4"));
                    // use background default color
                }

                //return view;
                return convertView;
            }
        };
        ListView items_list = findViewById(R.id.cart_history_list);
        items_list.setAdapter((items_adapter));
    } //onCreate

    public void GetCart() {
        String curItem;
        db = this.openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);

        // query needs to show items ONLY 30 days (or less) after the createdDate (from today)
        Cursor c = db.rawQuery("SELECT CA.category, CA.name, CA.createdDate, CA.quantity, CA.price, CA.Grocery, CH.payedDate FROM tblCartHistory AS CH JOIN tblCart AS CA ON CA.cartID = CH.cartID WHERE julianday(payedDate) - julianday('now') < 30", null);
        if(c != null) {
            if(c.moveToFirst()) {
                do {
                    Model item = new Model(
                            c.getString(0),
                            c.getString(1),
                            c.getString(2),
                            c.getString(3),
                            c.getString(4),
                            c.getString(5),
                            c.getString(6));
                    cartHistoryList.add(item);
                }while(c.moveToNext());
            }
            c.close();
        }
    } // GetCart()
}
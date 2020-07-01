package com.trios.EasyLiving;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {

    private SQLiteDatabase db = null;
    private final String DB_NAME = "lifeSupportDB";
    int idx;

    private ArrayList<String> itemIDs = new ArrayList<>();
    private ArrayList<String> itemAmounts = new ArrayList<>();

    private Button buy_button;
    private Button edit_button;
    private Button add_button;
    private String selID;
    private ArrayList<Model> cartList;
    Activity activity;

    private class ViewHolder {
        TextView mCategory;
        TextView mCartTtemName;
        TextView mCreatedDate;
        TextView mQuantity;
        TextView mPrice;
        TextView mGrocery;
        TextView mDayPassed;
    }

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Bundle extras = getIntent().getExtras();
        View view= findViewById(R.id.cart_title);
        if(extras != null) {
            String value = extras.getString("value");
            if (value.equals("deleted")){
                Snackbar.make(view, "Cart Deleted!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
            if (value.equals("added")){
                Snackbar.make(view, "Cart item Added!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
            if (value.equals("updated")){
                Snackbar.make(view, "Cart Updated!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }

        cartList = new ArrayList<Model>();
        GetCartItems(); // populate our cart items

        //ArrayAdapter<String> items_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, itemNames){
        ArrayAdapter<Model> items_adapter = new ArrayAdapter<Model>(this, android.R.layout.simple_list_item_1, cartList){

            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent){
                // Get the current item from ListView
                CartActivity.ViewHolder holder;
                activity = (Activity) this.getContext();
                LayoutInflater inflater = activity.getLayoutInflater();

                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.listview_row_cart, null);
                    holder = new CartActivity.ViewHolder();
                    holder.mCategory = (TextView) convertView.findViewById(R.id.sCategorye);
                    holder.mCartTtemName = (TextView) convertView.findViewById(R.id.sCart_item_name);
                    holder.mCreatedDate = (TextView) convertView.findViewById(R.id.sCreatedDate);
                    holder.mQuantity = (TextView) convertView.findViewById(R.id.sQuantity);
                    holder.mPrice = (TextView) convertView.findViewById(R.id.sPrice);
                    holder.mGrocery = (TextView) convertView.findViewById(R.id.sGrocery);
                    holder.mDayPassed = (TextView) convertView.findViewById(R.id.sDaysPassed);
                    convertView.setTag(holder);
                } else {
                    holder = (CartActivity.ViewHolder) convertView.getTag();
                }

                Model listItem = cartList.get(position);
                holder.mCategory.setText(listItem.getCategory().toString());
                holder.mCartTtemName.setText(listItem.getCartItemName().toString());
                holder.mCreatedDate.setText(listItem.getCreatedDate().toString());
                holder.mQuantity.setText(listItem.getQuantity().toString());
                holder.mPrice.setText(listItem.getPrice().toString());
                holder.mGrocery.setText(listItem.getGrocery().toString());
                holder.mDayPassed.setText(listItem.getDaysPassed().toString());

                if(position %2 == 0) {
                    convertView.setBackgroundColor(Color.parseColor("#FFF0CCD9"));
                }
                else {
                    convertView.setBackgroundColor(Color.parseColor("#FFE8F4F4"));
                    // use background default color
                }

                //return view;
                return convertView;
            }
        };
        buy_button = findViewById(R.id.buy_button);
        edit_button = findViewById(R.id.cart_edit_button);
        add_button = findViewById(R.id.cart_add_button);
        add_button.setEnabled(true);
        ListView items_list = findViewById(R.id.cart_items_list);
        items_list.setAdapter((items_adapter));
        // Create a click handler
        items_list.setOnItemClickListener(mItemClicked);
    } //OnCreate()

    private AdapterView.OnItemClickListener mItemClicked =
    new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            buy_button.setEnabled(true);
            edit_button.setEnabled(true);

            // flash button t warn user
            Animation mAnimation = new AlphaAnimation(1, 0);
            mAnimation.setDuration(200);
            mAnimation.setInterpolator(new LinearInterpolator());
            mAnimation.setRepeatCount(Animation.RELATIVE_TO_PARENT);
            mAnimation.setRepeatMode(Animation.REVERSE);
            buy_button.startAnimation(mAnimation);
            edit_button.startAnimation(mAnimation);
            view.startAnimation(mAnimation);

            // grab the list index that was clicked
            idx = position;
            selID = itemIDs.get(idx);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_cart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.cart_history) {
            Intent cartHistoryActivity =
                    new Intent(CartActivity.this,
                            CartHistoryActivity.class);
            startActivity(cartHistoryActivity);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    // go back main page
    public void onBackPressed() {
        Intent mainActivePage = new Intent(CartActivity.this,
                MainActivity.class);
        startActivity(mainActivePage);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void BuyCartItems(View v) {
        Date date = new Date();
        String theDate = new SimpleDateFormat("yyyy-MM-dd",  Locale.US).format(date);

        // first open db!
        db = this.openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        String amount = itemAmounts.get(idx);
        // insert record into tblCartHistory - id, price, payedDate, cartID
        db.execSQL("INSERT INTO tblCartHistory VALUES(?1,'" + itemAmounts.get(idx) + "','" + theDate + "', " + itemIDs.get(idx) + ")");
        db.execSQL("UPDATE tblCart SET status = 'C' WHERE cartID = " + selID + "");
        db.close(); // close the door, we don't live in a barn!

        // update cart page  after cart item bought
        Intent cartPage = new Intent(CartActivity.this,
                CartActivity.class);
        startActivity(cartPage);
    } // BuyCartItems()

    public void EditCart(View v) {
        String idx = selID;
        Intent CartEditPage = new Intent(CartActivity.this,
                EditCartActivity.class);
        CartEditPage.putExtra("sellID", selID);
        startActivity(CartEditPage);
    } // EditCart()

    public void AddCart(View v) {
        Intent cartEditPage = new Intent(CartActivity.this,
                EditCartActivity.class);
        cartEditPage.putExtra("sellID", "AddCart");
        startActivity(cartEditPage);
    } // AddCart()

    public void GetCartItems() {
        db = this.openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        // query needs to show items/bills ONLY 14 days (or less) before the duedate (from today)
        Cursor c = db.rawQuery("SELECT\n" +
                "            cartID AS 'ID:',\n" +
                "            category AS 'Category:',\n" +
                "            name AS 'Name:',\n" +
                "            createdDate AS 'Created Date:',\n" +
                "            quantity AS 'Quantity:',\n" +
                "            price AS 'Price:',\n" +
                "            grocery AS 'grocery:',\n" +
                "            CAST(julianday(createdDate) - julianday('now') AS INTEGER) || ' days' AS 'Days passed:'\n" +
                "        FROM tblCart\n" +
                "        WHERE\n" +
                "             julianday(createdDate) - julianday('now') < 30 AND status = 'A'", null);
        // julianday(dueDate) - julianday('now') < 150 AND

        if(c != null) {
            if(c.moveToFirst()) {
                do {
                    Model item = new Model(
                            c.getString(1),
                            c.getString(2),
                            c.getString(3),
                            c.getString(4),
                            c.getString(5),
                            c.getString(6),
                            c.getString(7));
                    cartList.add(item);
                    itemIDs.add(c.getString(0));
                    itemAmounts.add(c.getString(5));
                }while(c.moveToNext());
            }
            c.close();
        }

    } // GetCartItems()

}
package com.example.EasyLiving;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.os.Bundle;
import android.widget.Magnifier;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class BillsActivity extends AppCompatActivity {

    private SQLiteDatabase db = null;
    private final String DB_NAME = "lifeSupportDB";
    int idx;
    private ArrayList<String> itemNames = new ArrayList<>();
    private ArrayList<String> itemIDs = new ArrayList<>();
    private ArrayList<String> itemAmounts = new ArrayList<>();

    private Button pay_button;
    private Button edit_button;
    private Button add_button;
    private String selID;
    private ArrayList<Model> productList;
    Activity activity;

    private class ViewHolder {
        TextView mName;
        TextView mAmount;
        TextView mCompany;
        TextView mDayleft;
    }

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bills);

        Bundle extras = getIntent().getExtras();
        View view= findViewById(R.id.title);
        if(extras != null) {
            String value = extras.getString("value");
            if (value.equals("deleted")){
                Snackbar.make(view, "Bill Deleted!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
            if (value.equals("added")){
                Snackbar.make(view, "Bill Added!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
            if (value.equals("updated")){
                Snackbar.make(view, "Bill Updated!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }

        productList = new ArrayList<Model>();
        GetBills(); // populate our itemsNames

        //ArrayAdapter<String> items_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, itemNames){
        ArrayAdapter<Model> items_adapter = new ArrayAdapter<Model>(this, android.R.layout.simple_list_item_1, productList){

            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent){
                // Get the current item from ListView
                /*
                View view = super.getView(position,convertView,parent);
                TextView item = (TextView)view;
                if(position %2 == 1) {
                    // Set a background color for ListView regular row/item
                    view.setBackgroundColor(Color.parseColor("#FFCAC4C4"));
                    // Set the item text color
                    item.setTextColor(Color.parseColor("#FF211E1E"));
                    // Set the item text style to bold
                    item.setTypeface(item.getTypeface(), Typeface.BOLD);
                    // Change the item text size
                    item.setTextSize(TypedValue.COMPLEX_UNIT_DIP,18);
                }
                else {
                    // Set the background color for alternate row/item
                    view.setBackgroundColor(Color.parseColor("#FFFAF7F7"));
                    // Set the item text color
                    //item.setTextColor(Color.parseColor("#FFD17057"));
                    item.setTextColor(Color.parseColor("#FF211E1E"));
                    // Set the item text style to bold
                    item.setTypeface(item.getTypeface(), Typeface.BOLD);
                    // Change the item text size
                    item.setTextSize(TypedValue.COMPLEX_UNIT_DIP,18);
                }
                */

                ViewHolder holder;
                activity = (Activity) this.getContext();
                LayoutInflater inflater = activity.getLayoutInflater();

                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.listview_row, null);
                    holder = new ViewHolder();
                    holder.mName = (TextView) convertView.findViewById(R.id.sName);
                    holder.mAmount = (TextView) convertView.findViewById(R.id.sAmount);
                    holder.mCompany = (TextView) convertView.findViewById(R.id.sCompany);
                    holder.mDayleft = (TextView) convertView.findViewById(R.id.sDayleft);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                Model listItem = productList.get(position);
                holder.mName.setText(listItem.getName().toString());
                holder.mAmount.setText(listItem.getAmount().toString());
                holder.mCompany.setText(listItem.getCompany().toString());
                holder.mDayleft.setText(listItem.getDaysLeft().toString());

                /*
                // Set the item text style to bold
                holder.mName.setTypeface(null, Typeface.BOLD);
                holder.mAmount.setTypeface(null, Typeface.BOLD);
                holder.mCompany.setTypeface(null, Typeface.BOLD);
                holder.mDayleft.setTypeface(null, Typeface.BOLD);
                */
                if(position %2 == 0) {
                    convertView.setBackgroundColor(Color.parseColor("#FFBDCEEC"));
                }
                else {
                    convertView.setBackgroundColor(Color.parseColor("#FFE8F4F4"));
                    // use background default color
                }

                    //return view;
                return convertView;
            }
        };
        pay_button = findViewById(R.id.pay_button);
        edit_button = findViewById(R.id.edit_button);
        add_button = findViewById(R.id.add_button);
        add_button.setEnabled(true);
        ListView items_list = findViewById(R.id.items_list);
        items_list.setAdapter((items_adapter));
        // Create a click handler
        items_list.setOnItemClickListener(mItemClicked);
    } // onCreate()

    private AdapterView.OnItemClickListener mItemClicked =
        new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pay_button.setEnabled(true);
                edit_button.setEnabled(true);

                // flash button t warn user
                Animation mAnimation = new AlphaAnimation(1, 0);
                mAnimation.setDuration(200);
                mAnimation.setInterpolator(new LinearInterpolator());
                mAnimation.setRepeatCount(Animation.RELATIVE_TO_PARENT);
                mAnimation.setRepeatMode(Animation.REVERSE);
                pay_button.startAnimation(mAnimation);
                edit_button.startAnimation(mAnimation);
                view.startAnimation(mAnimation);

                // grab the list index that was clicked
                idx = position;
                selID = itemIDs.get(idx);
            }
        };

    @Override
    // go back last page and refresh list view
    public void onBackPressed() {
        Intent mainActivePage = new Intent(BillsActivity.this,
                MainActivity.class);
        startActivity(mainActivePage);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void PayBill(View v) {
        Date date = new Date();
        //String theDate = new SimpleDateFormat("yyyy-mm-dd").format(date);
        String theDate = new SimpleDateFormat("yyyy-MM-dd",  Locale.US).format(date);

        // first open db!
        db = this.openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        String amount = itemAmounts.get(idx);
        //Double convertedAmount = new Double(amount);

        // insert record into tblBillHistory - id, amount, payedDate, billID
        db.execSQL("INSERT INTO tblBillHistory VALUES(?1," + itemAmounts.get(idx) + ",'" + theDate + "', " + itemIDs.get(idx) + ")");

        LocalDate newDate = LocalDate.now().plusMonths(1);
        //db.execSQL("UPDATE tblBill SET dueDate = '" + newDate + "' WHERE billID = " + itemIDs.get(idx) + ")");
        db.execSQL("UPDATE tblBill SET dueDate = '" + newDate + "' WHERE billID = " + itemIDs.get(idx) + "");

        db.close(); // close the door, we don't live in a barn!

        // update bill page  after bll paid
        Intent billPage = new Intent(BillsActivity.this,
                BillsActivity.class);
        startActivity(billPage);
    } // PayBill()

    public void EditBill(View v) {
        String idx = selID;
        Intent billEditPage = new Intent(BillsActivity.this,
                EditBillsActivity.class);
        billEditPage.putExtra("sellID", selID);
        startActivity(billEditPage);
    } // EditBill()

    public void AddBill(View v) {
        Intent billEditPage = new Intent(BillsActivity.this,
                EditBillsActivity.class);
        billEditPage.putExtra("sellID", "AddBill");
        startActivity(billEditPage);
    } // AddBill()

    public void GetBills() {
        String curItem;
        db = this.openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        // query needs to show items/bills ONLY 14 days (or less) before the duedate (from today)
        Cursor c = db.rawQuery("SELECT\n" +
                "            billID AS 'ID:',\n" +
                "            name AS 'Bill:',\n" +
                "            amount AS 'Amount Due:',\n" +
                "            company AS 'Company:',\n" +
                "            CAST(julianday(dueDate) - julianday('now') AS INTEGER) || ' days' AS 'Days until Due:'\n" +
                "        FROM tblBill\n" +
                "        WHERE\n" +
                "             status = 'A'", null);
        // julianday(dueDate) - julianday('now') < 150 AND

        if(c != null) {
            if(c.moveToFirst()) {
                do {
                    curItem = c.getString(1) + " | $" +
                        c.getString(2) + " | " +
                        c.getString(3) + " | " +
                        c.getString(4);

                    Model item = new Model(c.getString(1), c.getString(2), c.getString(3), c.getString(4));
                    productList.add(item);

                    //itemNames.add(curItem);
                    itemIDs.add(c.getString(0));
                    itemAmounts.add(c.getString(2));
                }while(c.moveToNext());
            }
            c.close();
        }

    } // GetBills()

    /*
        SELECT
            name AS 'Bill:',
            dueDate AS 'Due Date:',
            company AS 'Company:',
            CAST(julianday(dueDate) - julianday('now') AS INTEGER) AS 'Days until Due:'
        FROM tblBill
        WHERE
            julianday(dueDate) - julianday('now') < 15
     */
}
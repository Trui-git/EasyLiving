package com.example.EasyLiving;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.os.Bundle;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bills);

        GetBills(); // populate our itemsNames
        ArrayAdapter<String> items_adapter =
        new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, itemNames){
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent){
                // Get the current item from ListView
                View view = super.getView(position,convertView,parent);
                TextView item = (TextView)view;
                if(position %2 == 1) {
                    // Set a background color for ListView regular row/item
                    view.setBackgroundColor(Color.parseColor("#FFF4EEED"));
                    // Set the item text color
                    item.setTextColor(Color.parseColor("#FF3E80F1"));
                    // Set the item text style to bold
                    item.setTypeface(item.getTypeface(), Typeface.BOLD);
                    // Change the item text size
                    item.setTextSize(TypedValue.COMPLEX_UNIT_DIP,18);
                }
                else {
                    // Set the background color for alternate row/item
                    view.setBackgroundColor(Color.parseColor("#FFD9CDCA"));
                    // Set the item text color
                    item.setTextColor(Color.parseColor("#FFD17057"));
                    // Set the item text style to bold
                    item.setTypeface(item.getTypeface(), Typeface.BOLD);
                    // Change the item text size
                    item.setTextSize(TypedValue.COMPLEX_UNIT_DIP,18);
                }
                return view;
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
        //pay_button = findViewById(R.id.pay_button);
        //edit_button = findViewById(R.id.edit_button);
        //items_list.setAdapter((items_adapter));
        // Create a click handler
        //items_list.setOnItemClickListener(mItemClicked);

    } // onCreate()

    @Override
    // go back last page and refresh list view
    public void onBackPressed() {
        Intent mainActivePage = new Intent(BillsActivity.this,
                MainActivity.class);
        startActivity(mainActivePage);
    }

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

                // grab the list index that was clicked
                idx = position;
                selID = itemIDs.get(idx);
            }
        };

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
                    itemNames.add(curItem);
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
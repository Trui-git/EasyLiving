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
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    private SQLiteDatabase db = null;
    private final String DB_NAME = "lifeSupportDB";
    private ArrayList<String> itemNames = new ArrayList<>();
    private ArrayList<Model> historyList;
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
        setContentView(R.layout.activity_history);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tracker = new Intent(HistoryActivity.this, MainActivity.class);
                startActivity(tracker);
            }
        });

        historyList = new ArrayList<Model>();
        GetBills();
        ArrayAdapter<Model> items_adapter = new ArrayAdapter<Model>(this, android.R.layout.simple_list_item_1, historyList){

            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent){
                // Get the current item from ListView
                HistoryActivity.ViewHolder holder;
                activity = (Activity) this.getContext();
                LayoutInflater inflater = activity.getLayoutInflater();

                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.listview_row_history, null);
                    holder = new HistoryActivity.ViewHolder();
                    holder.mName = (TextView) convertView.findViewById(R.id.sName_history);
                    holder.mAmount = (TextView) convertView.findViewById(R.id.sAmount_history);
                    holder.mCompany = (TextView) convertView.findViewById(R.id.sCompany_history);
                    holder.mDayleft = (TextView) convertView.findViewById(R.id.sDay_history);
                    convertView.setTag(holder);
                } else {
                    holder = (HistoryActivity.ViewHolder) convertView.getTag();
                }

                Model listItem = historyList.get(position);
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
        ListView items_list = findViewById(R.id.history_list);
        items_list.setAdapter((items_adapter));
    } //onCreate

    public void GetBills() {
        String curItem;
        db = this.openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);

        // query needs to show items/bills ONLY 14 days (or less) before the duedate (speel coorect is duumb) (from today)
        Cursor c = db.rawQuery("SELECT BI.name, BH.amount, BI.company, BH.payedDate FROM tblBillHistory AS BH JOIN tblBill AS BI ON BH.billID = BI.billID WHERE julianday(payedDate) - julianday('now') < 30", null);
        if(c != null) {
            if(c.moveToFirst()) {
                do {
                    Model item = new Model(
                            c.getString(0),
                            c.getString(1),
                            c.getString(2),
                            c.getString(3));
                    historyList.add(item);
                    //itemNames.add(curItem);
                }while(c.moveToNext());
            }
            c.close();
        }
    } // GetBills()
}
package com.trios.EasyLiving;

import androidx.appcompat.app.AppCompatActivity;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

public class MedicineActivity extends AppCompatActivity {

    private SQLiteDatabase db = null;
    private final String DB_NAME = "lifeSupportDB";
    int idx;

    private ArrayList<String> itemIDs = new ArrayList<>();

    private Button complete_button;
    private Button edit_button;
    private Button add_button;
    private String selID;
    private ArrayList<MedicineModel> medicineList;
    Activity activity;

    private class ViewHolder {
        TextView mName;
        TextView mPurpose;
        TextView mInstructions;
    }

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);

        Bundle extras = getIntent().getExtras();
        View view= findViewById(R.id.medicine_title);
        if(extras != null) {
            String value = extras.getString("value");
            if (value.equals("deleted")){
                Snackbar.make(view, "Medicine Deleted!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
            if (value.equals("added")){
                Snackbar.make(view, "Medicine Item Added!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
            if (value.equals("updated")){
                Snackbar.make(view, "Medicine Updated!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }

        medicineList = new ArrayList<MedicineModel>();
        GetMedicineItems(); // populate our medicineItems
        //ArrayAdapter<String> items_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, itemNames){
        ArrayAdapter<MedicineModel> items_adapter = new ArrayAdapter<MedicineModel>(this, android.R.layout.simple_list_item_1, medicineList){

            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent){
                // Get the current item from ListView
                MedicineActivity.ViewHolder holder;
                activity = (Activity) this.getContext();
                LayoutInflater inflater = activity.getLayoutInflater();

                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.listview_row_medicine, null);
                    holder = new MedicineActivity.ViewHolder();
                    holder.mName = (TextView) convertView.findViewById(R.id.sMedicineName);
                    holder.mPurpose = (TextView) convertView.findViewById(R.id.sPurpose);
                    holder.mInstructions = (TextView) convertView.findViewById(R.id.sInstructions);
                    convertView.setTag(holder);
                } else {
                    holder = (MedicineActivity.ViewHolder) convertView.getTag();
                }

                MedicineModel listItem = medicineList.get(position);
                holder.mName.setText(listItem.getName().toString());
                holder.mPurpose.setText(listItem.getPurpose().toString());
                holder.mInstructions.setText(listItem.getInstructions().toString());

                if(position %2 == 0) {
                    convertView.setBackgroundColor(Color.parseColor("#FFD7F6D7"));
                }
                else {
                    convertView.setBackgroundColor(Color.parseColor("#FFE8F4F4"));
                    // use background default color
                }

                //return view;
                return convertView;
            }
        };
        complete_button = findViewById(R.id.medicine_complete_button);
        edit_button = findViewById(R.id.medicine_edit_button);
        add_button = findViewById(R.id.medicine_add_button);
        add_button.setEnabled(true);
        ListView items_list = findViewById(R.id.medicine_items_list);
        items_list.setAdapter((items_adapter));
        // Create a click handler
        items_list.setOnItemClickListener(mItemClicked);

    }//onCreate

    private AdapterView.OnItemClickListener mItemClicked =
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    complete_button.setEnabled(true);
                    edit_button.setEnabled(true);

                    // flash button t warn user
                    Animation mAnimation = new AlphaAnimation(1, 0);
                    mAnimation.setDuration(200);
                    mAnimation.setInterpolator(new LinearInterpolator());
                    mAnimation.setRepeatCount(Animation.RELATIVE_TO_PARENT);
                    mAnimation.setRepeatMode(Animation.REVERSE);
                    complete_button.startAnimation(mAnimation);
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
        Intent mainActivePage = new Intent(MedicineActivity.this,
                MainActivity.class);
        startActivity(mainActivePage);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void CompleteMedicine(View v) {

        //Date date = new Date();
        //String theDate = new SimpleDateFormat("yyyy-mm-dd").format(date);
        //String theDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",  Locale.US).format(date);

        // first open db!
        //db = this.openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        //String amount = itemAmounts.get(idx);
        //Double convertedAmount = new Double(amount);

        // insert record into tblBillHistory - id, amount, payedDate, billID
        //db.execSQL("INSERT INTO tblTodoHistory VALUES(?1,'" + theDate + "', " + itemIDs.get(idx) + ")");

        //LocalDate newDate = LocalDate.now().plusMonths(1);
        //db.execSQL("UPDATE tblBill SET dueDate = '" + newDate + "' WHERE billID = " + itemIDs.get(idx) + ")");
        //db.execSQL("UPDATE tblMedicine SET status = 'C' WHERE medicineID = " + itemIDs.get(idx) + "");

        //db.close(); // close the door, we don't live in a barn!

        // update todopage
        Intent MedicinePage = new Intent(MedicineActivity.this,
                MedicineActivity.class);
        startActivity(MedicinePage);

    } // CompleteMedicine()*/

    public void EditMedicine(View v) {
        String idx = selID;
        Intent medicineEditPage = new Intent(MedicineActivity.this,
                EditMedicineActivity.class);
        medicineEditPage.putExtra("sellID", selID);
        startActivity(medicineEditPage);
    } // EditMedicine()

    public void AddMedicine(View v) {
        Intent medicineEditPage = new Intent(MedicineActivity.this,
                EditMedicineActivity.class);
        medicineEditPage.putExtra("sellID", "AddMedicine");
        startActivity(medicineEditPage);
    } // AddMedicine()

    public void GetMedicineItems() {
        db = this.openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        Cursor c = db.rawQuery("SELECT\n" +
                "            medicineID AS 'ID:',\n" +
                "            name AS 'Name:',\n" +
                "            purpose AS 'Purpose:',\n" +
                "            instructions AS 'Instructions:'\n" +
                "        FROM tblMedicine\n", null);

        if(c != null) {
            if(c.moveToFirst()) {
                do {
                    MedicineModel item = new MedicineModel(
                            c.getString(1),
                            c.getString(2),
                            c.getString(3));
                    medicineList.add(item);
                    itemIDs.add(c.getString(0));
                    //itemAmounts.add(c.getString(5));
                }while(c.moveToNext());
            }
            c.close();
        }

    } // GetTodoItems()
}
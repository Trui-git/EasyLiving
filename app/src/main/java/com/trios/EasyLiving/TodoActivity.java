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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class TodoActivity extends AppCompatActivity {

    private SQLiteDatabase db = null;
    private final String DB_NAME = "lifeSupportDB";
    int idx;

    private ArrayList<String> itemIDs = new ArrayList<>();
    //private ArrayList<String> itemAmounts = new ArrayList<>();

    private Button complete_button;
    private Button edit_button;
    private Button add_button;
    private String selID;
    private ArrayList<Model> todoList;
    Activity activity;

    private class ViewHolder {
        TextView mTask;
        TextView mPlace;
        TextView mDate;
        //TextView mTime;
    }

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        Bundle extras = getIntent().getExtras();
        View view= findViewById(R.id.todo_title);
        if(extras != null) {
            String value = extras.getString("value");
            if (value.equals("deleted")){
                Snackbar.make(view, "Todo Deleted!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
            if (value.equals("added")){
                Snackbar.make(view, "Todo Item Added!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
            if (value.equals("updated")){
                Snackbar.make(view, "Todo Updated!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }

        todoList = new ArrayList<Model>();
        GetTodoItems(); // populate our todoItems
        //ArrayAdapter<String> items_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, itemNames){
        ArrayAdapter<Model> items_adapter = new ArrayAdapter<Model>(this, android.R.layout.simple_list_item_1, todoList){

            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent){
                // Get the current item from ListView
                TodoActivity.ViewHolder holder;
                activity = (Activity) this.getContext();
                LayoutInflater inflater = activity.getLayoutInflater();

                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.listview_row_todo, null);
                    holder = new TodoActivity.ViewHolder();
                    holder.mTask = (TextView) convertView.findViewById(R.id.sTask);
                    holder.mPlace = (TextView) convertView.findViewById(R.id.sWhere);
                    holder.mDate = (TextView) convertView.findViewById(R.id.sDate);
                    convertView.setTag(holder);
                } else {
                    holder = (TodoActivity.ViewHolder) convertView.getTag();
                }

                Model listItem = todoList.get(position);
                holder.mTask.setText(listItem.getTask().toString());
                holder.mPlace.setText(listItem.getWhere().toString());
                holder.mDate.setText(listItem.getDate().toString());

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
        complete_button = findViewById(R.id.todo_complete_button);
        edit_button = findViewById(R.id.todo_edit_button);
        add_button = findViewById(R.id.todo_add_button);
        add_button.setEnabled(true);
        ListView items_list = findViewById(R.id.todo_items_list);
        items_list.setAdapter((items_adapter));
        // Create a click handler
        items_list.setOnItemClickListener(mItemClicked);

    } // onCreate

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_todo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.todo_history) {
            Intent todoHistoryActivity =
                    new Intent(TodoActivity.this,
                            TodoHistoryActivity.class);
            startActivity(todoHistoryActivity);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    // go back last page and refresh list view
    public void onBackPressed() {
        Intent mainActivePage = new Intent(TodoActivity.this,
                MainActivity.class);
        startActivity(mainActivePage);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void CompleteTodo(View v) {

        Date date = new Date();
        //String theDate = new SimpleDateFormat("yyyy-mm-dd").format(date);
        String theDate = new SimpleDateFormat("yyyy-MM-dd",  Locale.US).format(date);

        // first open db!
        db = this.openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        //String amount = itemAmounts.get(idx);
        //Double convertedAmount = new Double(amount);

        // insert record into tblBillHistory - id, amount, payedDate, billID
        db.execSQL("INSERT INTO tblTodoHistory VALUES(?1,'" + theDate + "', " + itemIDs.get(idx) + ")");

        //LocalDate newDate = LocalDate.now().plusMonths(1);
        //db.execSQL("UPDATE tblBill SET dueDate = '" + newDate + "' WHERE billID = " + itemIDs.get(idx) + ")");
        //db.execSQL("UPDATE tblTodo SET dueDate = '" + newDate + "' WHERE billID = " + itemIDs.get(idx) + "");

        db.close(); // close the door, we don't live in a barn!

        // update todopage
        Intent todoPage = new Intent(TodoActivity.this,
                TodoActivity.class);
        startActivity(todoPage);

    } // CompleteTodo()

    public void EditTodo(View v) {
        String idx = selID;
        Intent todoEditPage = new Intent(TodoActivity.this,
                EditTodoActivity.class);
        todoEditPage.putExtra("sellID", selID);
        startActivity(todoEditPage);
    } // EditTodo()

    public void AddTodo(View v) {
        Intent todoEditPage = new Intent(TodoActivity.this,
                EditTodoActivity.class);
        todoEditPage.putExtra("sellID", "AddTodo");
        startActivity(todoEditPage);
    } // AddTodo()

    public void GetTodoItems() {
        db = this.openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        // query needs to show items/bills ONLY 14 days (or less) before the duedate (from today)
        Cursor c = db.rawQuery("SELECT\n" +
                "            todoID AS 'ID:',\n" +
                "            task AS 'Task:',\n" +
                "            todoWhere AS 'Where:',\n" +
                "            time AS 'Created Date:',\n" +
                "            CAST(julianday(time) - julianday('now') AS INTEGER) || ' days' AS 'Days passed:'\n" +
                "        FROM tblTodo\n" +
                "        WHERE\n" +
                "             status = 'A'", null);

        if(c != null) {
            if(c.moveToFirst()) {
                do {
                    Model item = new Model(
                            c.getString(1),
                            c.getString(2),
                            c.getString(3));
                    todoList.add(item);
                    itemIDs.add(c.getString(0));
                    //itemAmounts.add(c.getString(5));
                }while(c.moveToNext());
            }
            c.close();
        }

    } // GetTodoItems()
}
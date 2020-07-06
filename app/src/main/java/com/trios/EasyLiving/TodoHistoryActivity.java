package com.trios.EasyLiving;

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

public class TodoHistoryActivity extends AppCompatActivity {

    private SQLiteDatabase db = null;
    private final String DB_NAME = "lifeSupportDB";
    private ArrayList<Model> todoHistoryList;
    Activity activity;
    private class ViewHolder {
        TextView mTask;
        TextView mPlace;
        TextView mDate;
    }
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_history);

        FloatingActionButton fab = findViewById(R.id.todo_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tracker = new Intent(TodoHistoryActivity.this, MainActivity.class);
                startActivity(tracker);
            }
        });

        todoHistoryList = new ArrayList<Model>();
        GetTodoItems();
        ArrayAdapter<Model> items_adapter = new ArrayAdapter<Model>(this, android.R.layout.simple_list_item_1, todoHistoryList) {

            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                // Get the current item from ListView
                TodoHistoryActivity.ViewHolder holder;
                activity = (Activity) this.getContext();
                LayoutInflater inflater = activity.getLayoutInflater();

                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.listview_row_todo_history, null);
                    holder = new TodoHistoryActivity.ViewHolder();
                    holder.mTask = (TextView) convertView.findViewById(R.id.sTask_history);
                    holder.mPlace = (TextView) convertView.findViewById(R.id.sWhere_history);
                    holder.mDate = (TextView) convertView.findViewById(R.id.sDate_history);
                    convertView.setTag(holder);
                } else {
                    holder = (TodoHistoryActivity.ViewHolder) convertView.getTag();
                }

                Model listItem = todoHistoryList.get(position);
                holder.mTask.setText(listItem.getTask().toString());
                holder.mPlace.setText(listItem.getWhere().toString());
                holder.mDate.setText(listItem.getDate().toString());

                if (position % 2 == 0) {
                    convertView.setBackgroundColor(Color.parseColor("#FFD7F6D7"));
                } else {
                    convertView.setBackgroundColor(Color.parseColor("#FFE8F4F4"));
                    // use background default color
                }

                //return view;
                return convertView;
            }
        };
        ListView items_list = findViewById(R.id.todo_history_list);
        items_list.setAdapter((items_adapter));

    } // onCreate()

    public void GetTodoItems() {
        //String curItem;
        db = this.openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);

        // query needs to show items ONLY 30 days (or less) after the createdDate (from today)
        Cursor c = db.rawQuery("SELECT TD.task, TD.todoWhere, TD.time, TH.completTime FROM tblTodoHistory AS TH JOIN tblTodo AS TD ON TH.todoID = TD.todoID WHERE julianday(TD.time) - julianday('now') < 30", null);
        if(c != null) {
            if(c.moveToFirst()) {
                do {
                    Model item = new Model(
                            c.getString(0),
                            c.getString(1),
                            //c.getString(2),
                            c.getString(3));
                    todoHistoryList.add(item);
                }while(c.moveToNext());
            }
            c.close();
        }
    } // GetTodoItems()

}
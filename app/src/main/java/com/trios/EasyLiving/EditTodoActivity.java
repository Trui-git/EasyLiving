package com.trios.EasyLiving;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditTodoActivity extends AppCompatActivity {

    private SQLiteDatabase db = null;
    private final String DB_NAME = "lifeSupportDB";
    private String sellID;
    private EditText todoTask;
    private EditText todoWhere;
    private EditText todoTime;
    Calendar myCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_todo);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            sellID = extras.getString("sellID");
            if ((sellID!= null) && sellID.equals("AddTodo")){
                Button update_button = findViewById(R.id.todo_update_button);
                Button delete_button = findViewById(R.id.todo_delete_button);
                update_button.setEnabled(false);
                delete_button.setEnabled(false);
                sellID = null;
            }
            else{
                Button add_todo_button = findViewById(R.id.todo_add_button);
                //add_todo_button.setEnabled(false);
            }
        }

        todoTask = findViewById(R.id.todo_Task);
        todoWhere = findViewById(R.id.todo_where);
        todoTime = findViewById(R.id.todo_time);

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

        todoTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(EditTodoActivity.this, date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        GetTodo(sellID);
    }
    @Override
    // go back last page and refresh list view
    public void onBackPressed() {
        Intent todoActivePage = new Intent(EditTodoActivity.this,
                TodoActivity.class);
        startActivity(todoActivePage);
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        todoTime.setText(sdf.format(myCalendar.getTime()));
    }

    public void GetTodo(String id) {
        db = this.openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        Cursor c = db.rawQuery("SELECT * FROM tblTodo WHERE todoID = " + sellID , null);

        if(c != null) {
            if(c.moveToFirst()) {
                do{
                    todoTask.setText(c.getString(1));
                    todoWhere.setText(c.getString(2));
                    todoTime.setText(c.getString(3));
                }while(c.moveToNext());
            }
            c.close();
        }

    } // GetTodo()

    public void UpdateTodo(View v) {

        if (TextUtils.isEmpty(todoTask.getText().toString())) {
            Toast.makeText(this, "todo task is empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(todoWhere.getText().toString())) {
            Toast.makeText(this, "todo where is empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(todoTime.getText().toString())) {
            Toast.makeText(this, "todo time is empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        db = this.openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        db.execSQL("UPDATE tblTodo SET " +
                "task = '" + todoTask.getText().toString() + "', " +
                "todoWhere = '" + todoWhere.getText().toString() + "', " +
                "time = '" + todoTime.getText().toString() + "' " +
                "WHERE todoID = " + sellID + "");

        db.close(); // close the door, we don't live in a barn!

        Intent todoActivePage = new Intent(EditTodoActivity.this,
                TodoActivity.class);
        todoActivePage.putExtra("value", "updated");
        startActivity(todoActivePage);
    } // UpdateTodo()

    public void AddTodo(View v) {

        if (TextUtils.isEmpty(todoTask.getText().toString())) {
            Toast.makeText(this, "todo task is empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(todoWhere.getText().toString())) {
            Toast.makeText(this, "todo where is empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(todoTime.getText().toString())) {
            Toast.makeText(this, "todo time is empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        db = this.openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        db.execSQL("INSERT INTO tblTodo(task, todoWhere, time, status) " +
                "VALUES('" + todoTask.getText().toString() + "', " +
                "'" + todoWhere.getText().toString() + "', " +
                "'" + todoTime.getText().toString() + "', " +
                "'A')");
        db.close(); // close the door, we don't live in a barn!

        Intent todoActivePage = new Intent(EditTodoActivity.this,
                TodoActivity.class);
        todoActivePage.putExtra("value", "added");
        startActivity(todoActivePage);

    } // AddTodo()

    public void ClearTodo(View v) {
        // clear all edittext fields
        todoTask.getText().clear();
        todoWhere.getText().clear();
        todoTime.getText().clear();
    } // ClearTodo()

    public void DeleteTodo(View v) {

        db = this.openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);

        db.execSQL("UPDATE tblTodo SET status = 'C' WHERE todoID = " + sellID + "");

        db.close(); // close the door, we don't live in a barn!

        Intent todoActivePage = new Intent(EditTodoActivity.this,
                TodoActivity.class);
        todoActivePage.putExtra("value", "deleted");
        startActivity(todoActivePage);
    } // DeleteTodo()
}
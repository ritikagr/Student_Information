package com.iit.ritik.studentinformation;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Student_Profile extends AppCompatActivity {

    private TextView student_name1;
    private TextView adm_no;
    private TextView emailId;

    private SQLiteDatabase db;
    private String column_name;
    private String branch_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_student);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);

        openDatabase();
        Intent intent = getIntent();
        column_name = intent.getStringExtra("ADMISSION");
        branch_name = intent.getStringExtra("BRANCH");
        student_name1 = (TextView) findViewById(R.id.st_name);
        adm_no = (TextView) findViewById(R.id.st_adm);
        emailId = (TextView) findViewById(R.id.st_email);

        String sql = "SELECT * FROM '"+branch_name+"' WHERE adm_no = '"+column_name+"'";
        Cursor c = db.rawQuery(sql,null);
        c.moveToFirst();
        while(!c.isAfterLast())
        {
            adm_no.setText(c.getString(c.getColumnIndex("adm_no")));
            student_name1.setText(c.getString(c.getColumnIndex("name")));
            emailId.setText(c.getString(c.getColumnIndex("email_id")));
            c.moveToNext();
        }
    }

    public void openDatabase()
    {
        db = openOrCreateDatabase("studentinfo", Context.MODE_PRIVATE,null);
    }

}

package com.iit.ritik.studentinformation;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
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

public class MainActivity extends AppCompatActivity {

    private EditText table_name;
    private Button ok;
    private Button add;
    private Button cancel;
    private ListView listView;
    private List<String> branchList;
    private ArrayAdapter branchAdapter;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        table_name = (EditText) findViewById(R.id.table_name);
        ok = (Button) findViewById(R.id.ok);
        add = (Button) findViewById(R.id.add_table);
        cancel = (Button) findViewById(R.id.cancel);

        listView = (ListView) findViewById(R.id.table_list);
        branchList = Collections.synchronizedList(new ArrayList<String>());

        //open database if exists else create
        openDatabase();

        updateBranchList();
        branchAdapter = new ArrayAdapter(MainActivity.this,R.layout.list_item_branch,R.id.row_branch_name,branchList);

        listView.setAdapter(branchAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String s = (String) adapterView.getItemAtPosition(position);

                Intent intent = new Intent(MainActivity.this,Branch_Info.class);
                intent.putExtra("TABLE_NAME",s);
                startActivity(intent);
            }
        });

        db.close();
    }

    public void openDatabase()
    {
        db = openOrCreateDatabase("studentinfo", Context.MODE_PRIVATE,null);
    }

    public void updateBranchList()
    {
        openDatabase();
        try {
            String sql = "SELECT name FROM sqlite_master where type='table'";
            Cursor c = db.rawQuery(sql,null);
            branchList.clear();

            if(c.moveToFirst())
            while(!c.isAfterLast())
            {
                String s = c.getString(c.getColumnIndex("name"));
                if(!s.equals("android_metadata"))
                branchList.add(s);
                c.moveToNext();
            }
        }catch(ArrayIndexOutOfBoundsException e)
        {
        }

        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void add(View view)
    {
        table_name.setVisibility(View.VISIBLE);
        ok.setVisibility(View.VISIBLE );
        add.setVisibility(View.GONE);
        cancel.setVisibility(View.VISIBLE);
    }

    public void ok(View view)
    {
        openDatabase();
        String tableName = table_name.getText().toString();
        if(tableName.length()>0) {
            table_name.setVisibility(View.GONE);
            ok.setVisibility(View.GONE);
            cancel.setVisibility(View.GONE);
            add.setVisibility(View.VISIBLE);

            //creating new table with branch name
            try {
                String sql = "CREATE TABLE '"+tableName+"'(adm_no varchar(10) primary key not null," +
                        "name varchar(30) not null,email_id varchar(30) not null)";
                SQLiteStatement stmt = db.compileStatement(sql);
                stmt.execute();
            }catch (SQLiteException e)
            {
            }

            updateBranchList();
            branchAdapter.notifyDataSetChanged();
            table_name.setText("");
        }
        else{
            Toast.makeText(getApplicationContext(),"Please Enter Valid Branch Name",Toast.LENGTH_SHORT).show();
        }

        db.close();
    }
    public void cancel(View view)
    {
        table_name.setVisibility(View.GONE);
        ok.setVisibility(View.GONE);
        cancel.setVisibility(View.GONE);
        add.setVisibility(View.VISIBLE);
    }
    private void deleteBranch(View view)
    {
        openDatabase();
        TextView textView = (TextView) getWindow().getDecorView().findViewById(R.id.row_name);
        String table_name = textView.getText().toString();
        String sql = "DROP TABLE '"+table_name+"'";
        SQLiteStatement stmt = db.compileStatement(sql);
        stmt.executeUpdateDelete();
        Toast.makeText(this,table_name + "Branch Deleted Successfully",Toast.LENGTH_SHORT).show();
        updateBranchList();
        branchAdapter.notifyDataSetChanged();
        db.close();
    }
}
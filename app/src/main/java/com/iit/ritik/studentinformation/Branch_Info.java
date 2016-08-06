package com.iit.ritik.studentinformation;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Branch_Info extends AppCompatActivity {

    private EditText student_name1;
    private EditText adm_no;
    private EditText emailId;
    private Button ok1;
    private Button add1;
    private Button cancel1;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private List<String> studentList;
    private SQLiteDatabase db;
    private String table_name1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch__info);

        openDatabase();
        Intent intent = getIntent();
        table_name1 = intent.getStringExtra("TABLE_NAME");

        student_name1 = (EditText) findViewById(R.id.student_name1);
        adm_no = (EditText) findViewById(R.id.adm_no);
        emailId = (EditText) findViewById(R.id.email_id);

        ok1 = (Button) findViewById(R.id.ok1);
        add1 = (Button) findViewById(R.id.add_table1);
        cancel1 = (Button) findViewById(R.id.cancel1);

        listView = (ListView) findViewById(R.id.student_list);
        studentList = Collections.synchronizedList(new ArrayList<String>());
        updateStudentList();
        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.list_item,R.id.row_name,studentList);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent1 = new Intent(Branch_Info.this,Student_Profile.class);
                String s = (String)adapterView.getItemAtPosition(position);
                intent1.putExtra("ADMISSION",s);
                intent1.putExtra("BRANCH",table_name1);
                startActivity(intent1);
            }
        });
    }

    public void openDatabase()
    {
        db = openOrCreateDatabase("studentinfo", Context.MODE_PRIVATE,null);
    }

    public void add1(View view)
    {
        student_name1.setVisibility(View.VISIBLE);
        adm_no.setVisibility(View.VISIBLE);
        emailId.setVisibility(View.VISIBLE);
        ok1.setVisibility(View.VISIBLE );
        add1.setVisibility(View.GONE);
        cancel1.setVisibility(View.VISIBLE);
    }

    public void ok1(View view)
    {
        String studentName1 = student_name1.getText().toString();
        String adm = adm_no.getText().toString();
        String email = emailId.getText().toString();

        if(studentName1.length()>0 && adm.length()>0 && email.length()>0) {
            student_name1.setVisibility(View.GONE);
            adm_no.setVisibility(View.GONE);
            emailId.setVisibility(View.GONE);
            ok1.setVisibility(View.GONE);
            cancel1.setVisibility(View.GONE);
            add1.setVisibility(View.VISIBLE);

            String sql = "INSERT INTO '"+table_name1+"' (adm_no,name,email_id) VALUES ('"+adm+"','"+studentName1+"','"+email+"')";
            SQLiteStatement stmt = db.compileStatement(sql);
            stmt.executeInsert();
            /*ContentValues data = new ContentValues();
            data.put(DatabaseContract.DataEntry.COLUMN_ID,"'"+adm+"'");
            data.put(DatabaseContract.DataEntry.COLUMN_NAME,"'"+studentName1+"'");
            data.put(DatabaseContract.DataEntry.COLUMN_EMAIL,"'"+email+"'");
            long id = db.insert("'"+table_name1+"'",null,data);
            if(id==1)
            {
                Toast.makeText(this,"Information inserted successfully",Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show();
            }
*/
            updateStudentList();
            arrayAdapter.notifyDataSetChanged();
            student_name1.setText("");
            adm_no.setText("");
            emailId.setText("");
        }
        else{
            Toast.makeText(getApplicationContext(),"Please Enter Valid Branch Name",Toast.LENGTH_SHORT).show();
        }
    }

    public void updateStudentList()
    {
        /*String[] projection = {DatabaseContract.DataEntry.COLUMN_ID};
        Cursor c = db.query("'"+table_name1+"'",projection,null,null,null,null,null);*/

        String sql = "SELECT adm_no FROM '"+table_name1+"'";
        Cursor c = db.rawQuery(sql,null);

        c.moveToFirst();
        while(c.isAfterLast()!=true) {
            String itemId = c.getString(c.getColumnIndexOrThrow(DatabaseContract.DataEntry.COLUMN_ID));
            studentList.add(itemId);
            c.moveToNext();
        }
    }
    public void cancel1(View view)
    {
        student_name1.setVisibility(View.GONE);
        ok1.setVisibility(View.GONE);
        cancel1.setVisibility(View.GONE);
    }

}

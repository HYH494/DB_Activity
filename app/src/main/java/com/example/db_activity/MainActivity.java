package com.example.db_activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.db_activity.R.id.b_read;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText e_name,e_email,e_mobilenumber;
    Button b_write,b_read,b_update,b_remove;
    TextView t_record;
    MyHelper myhelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        e_name = (EditText)findViewById(R.id.e_name);
        e_email = (EditText)findViewById(R.id.e_email);
        e_mobilenumber = (EditText)findViewById(R.id.e_mobilenumber);
        b_write = (Button) findViewById(R.id.b_write);
        b_read = (Button)findViewById(R.id.b_read);
        b_update = (Button)findViewById(R.id.b_update);
        b_remove = (Button)findViewById(R.id.b_remove);
        t_record = (TextView) findViewById(R.id.t_record);
        t_record.setMovementMethod(ScrollingMovementMethod.getInstance());
        myhelper = new MyHelper(this);
        b_write.setOnClickListener(this);
        b_read.setOnClickListener(this);
        b_update.setOnClickListener(this);
        b_remove.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        String name,email,mobilenumber;
        SQLiteDatabase db;
        ContentValues values;
        switch(v.getId()){
            case R.id.b_write:
                if((e_name.length()!=0)&&(e_email.length()!=0)&&(e_mobilenumber.length()!=0)) {
                    name = e_name.getText().toString();
                    email = e_email.getText().toString();
                    db = myhelper.getWritableDatabase();
                    mobilenumber = e_mobilenumber.getText().toString();
                    Cursor cursor = db.query("info",null,null,null,null,null,null);
                    if(cursor.getCount()==0) {
                        values = new ContentValues();
                        values.put("name", name);
                        values.put("email", email);
                        values.put("mobilenumber", mobilenumber);
                        db.insert("info",null,values);
                        Toast.makeText(this,"Write successfully!",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        cursor.moveToFirst();
                        String temp = cursor.getString(3);
                        int count = 0;
                        if(temp.equals(mobilenumber))
                            count += 1;
                        while(cursor.moveToNext()){
                            temp = cursor.getString(3);
                            if(temp.equals(mobilenumber))
                                count += 1;
                        }
                        if(count > 0){
                            Toast.makeText(this,"The mobile number have exits!",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            values = new ContentValues();
                            values.put("name", name);
                            values.put("email", email);
                            values.put("mobilenumber", mobilenumber);
                            db.insert("info",null,values);
                            Toast.makeText(this,"Write successfully!",Toast.LENGTH_SHORT).show();
                        }
                    }
                    cursor.close();
                    db.close();
                }
                else
                    Toast.makeText(this,"The content can't be null!",Toast.LENGTH_SHORT).show();
                break;
            case R.id.b_read:
                db = myhelper.getWritableDatabase();
                Cursor cursor = db.query("info",null,null,null,null,null,null);
                if(cursor.getCount()==0)
                    Toast.makeText(this,"No record!",Toast.LENGTH_SHORT).show();
                else{
                    cursor.moveToFirst();
                    t_record.setText("Name: "+cursor.getString(1)+"\nEmail: "+
                            cursor.getString(2)+"\nMobile number: "+
                            cursor.getString(3));
                }
                while(cursor.moveToNext()){
                    t_record.append("\n\nName: "+cursor.getString(1)+"\nEmail: "+
                            cursor.getString(2)+"\nMobile number: "+
                            cursor.getString(3));
                }
                Toast.makeText(this,"Read successfully!",Toast.LENGTH_SHORT).show();
                cursor.close();
                db.close();
                break;
            case R.id.b_update:
                db = myhelper.getWritableDatabase();
                values = new ContentValues();
                if((e_name.length()!=0)&&(e_email.length()!=0)&&(e_mobilenumber.length()!=0)) {
                    values.put("email",e_email.getText().toString());
                    values.put("mobilenumber",e_mobilenumber.getText().toString());
                    db.update("info",values,"name = ?",new String[]{e_name.getText().toString()});
                    Toast.makeText(this,"Update successfully!",Toast.LENGTH_SHORT).show();
                    db.close();
                }
                else
                    Toast.makeText(this,"The content can't be null!",Toast.LENGTH_SHORT).show();
                break;
            case R.id.b_remove:
                db = myhelper.getWritableDatabase();
                db.delete("info",null,null);
                Toast.makeText(this,"Remove successfully!",Toast.LENGTH_SHORT).show();
                db.close();
                t_record.setText("");
                break;
        }
    }
}

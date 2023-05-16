package com.example.sqlite2;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8;
    TextView textView1;
    String dbName = "PRODUCT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView1 = (TextView) findViewById(R.id.view_log);
        DB db = new DB(this, dbName, null, 1);
        db.getWritableDatabase();

        btn1 = (Button) findViewById(R.id.insert);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.insertData("CD-0100", "Cookie", 3000f);
                db.insertData("CD-0101", "Candy", 3000f);
                db.insertData("CD-0102", "Cake", 3000f);
            }
        });
        btn2 = (Button) findViewById(R.id.search);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLoad(db);
            }
        });
        btn3 = (Button) findViewById(R.id.delete1);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.deleteData(0);
            }
        });
        btn4 = (Button) findViewById(R.id.delete2);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.deleteData(1);
            }
        });
        btn5 = (Button) findViewById(R.id.delete3);
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.deleteData(2);
            }
        });
        btn6 = (Button) findViewById(R.id.delete_all);
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.deleteAll();
            }
        });
        btn7 = (Button) findViewById(R.id.update1);
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.onUpdate(2000f);
            }
        });
        btn8 = (Button) findViewById(R.id.update2);
        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.onUpdate(3000f);
            }
        });

    }

    void onLoad(DB db) {
        Cursor cursor = db.searchData();
        if (cursor.getCount() == 0) {
            textView1.setText("\n 데이터가 없습니다.");
        } else {
            cursor.moveToFirst();
            textView1.setText("");
            for (int i = 0; i < cursor.getCount(); i++) {
                String code = cursor.getString(0);
                String name = cursor.getString(1);
                float price = cursor.getFloat(2);
                textView1.append("\n"
                        .concat(code).concat(" - ")
                        .concat(name).concat(" - ")
                        .concat(String.format("%.0f", price))
                );
                cursor.moveToNext();
            }
        }
        cursor.close();
    }
}
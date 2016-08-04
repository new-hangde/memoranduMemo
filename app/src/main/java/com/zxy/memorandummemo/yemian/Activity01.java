 package com.zxy.memorandummemo.yemian;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.zxy.memorandummemo.R;
import com.zxy.memorandummemo.Service.DbHelper;
import com.zxy.memorandummemo.Service.Deed;

/**
 * Created by zxy on 2016/7/27.
 */
public class Activity01 extends AppCompatActivity{

    SQLiteDatabase db;
    EditText ed1;
    Button bt;
    TextView tv2;
    String date;
    String content;
    String temp;
    Bundle bundle;
    private boolean flag=true;
    DbHelper helper = new DbHelper(this,"db_bwl",null,1);
    final String TAG ="Activity01";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main2);
        tv2 = (TextView) findViewById(R.id.tv2);
        bt = (Button) findViewById(R.id.bt);
        ed1 = (EditText) findViewById(R.id.ed1);
        bundle = this.getIntent().getExtras();

        if (bundle != null) {
            date = bundle.getString("date");
            temp = "今天:\t" + date;
            ed1.setText(bundle.getString("content"));
        } else {
            date = Deed.getDate();
            temp = "今天:\t" + date;
        }

        db = helper.getWritableDatabase();
        tv2.setHint(temp);
        tv2.setTextColor(Color.BLACK);

        ed1.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        bt.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                if (bundle != null) {
                    content = ed1.getText().toString();
                    if (!TextUtils.isEmpty(content)) {
                        ContentValues cv = new ContentValues();
                        cv.put("content", content);
                        db.update("pic1", cv, "date=?", new String[]{date});
                    }
                } else {
                    if (flag) {
                        content = ed1.getText().toString();
                        flag = false;
                        initDatabase(db);
                    }
                }
            }
        });

    }

    public void initDatabase(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        if(!TextUtils.isEmpty(content)) {
            cv.put("content", content);
            cv.put("date", date);
            db.insert("pic1", null, cv);
        }
    }
    }

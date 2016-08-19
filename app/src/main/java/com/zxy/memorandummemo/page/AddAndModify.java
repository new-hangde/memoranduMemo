 package com.zxy.memorandummemo.page;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zxy.memorandummemo.Service.Data;
import com.zxy.memorandummemo.Service.DataListAdapter;
import com.zxy.memorandummemo.page.R;
import com.zxy.memorandummemo.Service.DbHelper;
import com.zxy.memorandummemo.Service.TimeRead;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

 /**
 * Created by zxy on 2016/7/27.
 */
public class AddAndModify extends AppCompatActivity{

    SQLiteDatabase db;
    EditText ed1;
    Button bt;
    TextView tv2;
    String date;
    String content;
    String temp;
    Bundle bundle;
    String currentId;
     String id;
    private boolean flag=true;
    DbHelper helper = new DbHelper(this,"db_bwl",null,1);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences settings = getSharedPreferences("Customers",MODE_APPEND);
        currentId = settings.getString("id","");
        Log.d("id",currentId);
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
            date = TimeRead.getDate();
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

                if (TextUtils.isEmpty(currentId)) {
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
                } else {
                    content = ed1.getText().toString();
                    if (bundle == null) {
                        if (!TextUtils.isEmpty(content)) {
                            Data data = new Data();
                            data.setId(currentId);
                            data.setContent(content);
                            data.setDate(date);
                            data.save(new SaveListener<String>() {
                                @Override
                                public void done(String s, BmobException e) {
                                    if (e == null) {
                                        Toast toast = Toast.makeText(AddAndModify.this, "保存成功！", Toast.LENGTH_SHORT);
                                        toast.show();
                                    } else {
                                        Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                                    }
                                }
                            });
                        }
                    } else {
                        content = ed1.getText().toString();
                        BmobQuery<Data> query =new BmobQuery<Data>();
                        query.addWhereEqualTo("date",date);
                        query.setLimit(1);
                        query.findObjects(new FindListener<Data>() {
                            @Override
                            public void done(List<Data> object, BmobException e) {
                                if(e==null){
                                    Toast toast = Toast.makeText(AddAndModify.this, "查询成功：共"+object.size()+"条数据。", Toast.LENGTH_SHORT);
                                    toast.show();
                                    for (Data data : object) {
                                         String ObjectId =data.getObjectId();
                                        Log.d(id,ObjectId);
                                        id =ObjectId;
                                        data.setContent(content);
                                        data.update(id, new UpdateListener() {
                                            @Override
                                            public void done(BmobException e) {
                                                if(e==null){
                                                    Toast toast = Toast.makeText(AddAndModify.this, "更新成功", Toast.LENGTH_SHORT);
                                                }else{
                                                    Toast toast = Toast.makeText(AddAndModify.this, "更新失败", Toast.LENGTH_SHORT);
                                                }
                                            }
                                        });
                                    }
                                }else{
                                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                }
                            }
                        });

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

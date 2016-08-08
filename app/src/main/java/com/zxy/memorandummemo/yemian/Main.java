package com.zxy.memorandummemo.yemian;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.zxy.memorandummemo.R;
import com.zxy.memorandummemo.Service.DbHelper;

public class Main extends AppCompatActivity {
    private ListView lv1;
    public String db_name = "db_bwl";
    public String table_name = "pic1";
    String deleteContent ="";
    String deleteDate ="";
    private TextView tv1;
    String tvContent;
    DbHelper helper = new DbHelper(this, db_name, null, 1);
    SimpleCursorAdapter adapter;
    SQLiteDatabase db;
    Button bt1;
    Button bt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        bt1 = (Button) findViewById(R.id.bt1);
        bt2 = (Button) findViewById(R.id.bt2);
        lv1 = (ListView) findViewById(R.id.lv1);
        tv1 = (TextView)findViewById(R.id.tv1);
        bt1.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Main.this, AddAndModify.class);
                startActivity(intent);
            }
        });

        bt2.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                builder .setTitle("全部删除")
                        .setMessage("是否全部删除")
                        .setPositiveButton("是",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        db = helper.getWritableDatabase();

                                            db.execSQL("DELETE FROM pic1");
                                        viewList();
                                        tv1.setText(tvContent);
                                    }
                                })
                        .setNegativeButton("否",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        return;
                                    }

                                });
                AlertDialog ad =builder.create();
                ad.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG);
                ad.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                ad.setCanceledOnTouchOutside(false);
                ad.show();

            }
        });

        lv1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                 deleteContent = ((TextView) view.findViewById(R.id.content)).getText().toString();
                 deleteDate = ((TextView) view.findViewById(R.id.date)).getText().toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                builder .setTitle("删除")
                        .setMessage("是否删除")
                        .setPositiveButton("是",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                       db = helper.getWritableDatabase();
                                        Cursor cursor = db.query(table_name, new String[]{"_id", "content", "date"}, null, null, null, null, "date desc");
                                        adapter = new SimpleCursorAdapter(Main.this,
                                                R.layout.list, cursor, new String[]{"content",
                                                "date"}, new int[]{R.id.content, R.id.date});
                                        while (cursor.moveToNext()) {
                                            String Text = cursor.getString(cursor.getColumnIndex("content"));
                                            String nText = cursor.getString(cursor.getColumnIndex("date"));
                                            if (deleteContent.equals(Text) && deleteDate.equals(nText)) {
                                                deleteData(cursor.getInt(0));
                                                viewList();
                                                tv1.setText(tvContent);
                                                break;
                                            }
                                        }
                                    }
                                })
                        .setNegativeButton("否",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        return;
                                    }

                                });
                AlertDialog ad =builder.create();
                ad.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG);
                ad.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                ad.setCanceledOnTouchOutside(false);
                ad.show();

                return true;
            }
        });

            lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String content = ((TextView) view.findViewById(R.id.content)).getText().toString();
                    String date = ((TextView) view.findViewById(R.id.date)).getText().toString();
                    Intent intent = new Intent();
                    intent.setClass(Main.this, AddAndModify.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("date", date);
                    bundle.putString("content", content);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }

    public void onResume() {
        super.onResume();
        viewList();
        tv1.setText(tvContent);
    }

    public void onRestart(){
        super.onRestart();
    }

    public void deleteData(int index) {
        db = helper.getWritableDatabase();
        db.execSQL("DELETE FROM " + "pic1" + " WHERE _id="
                + Integer.toString(index));
        db.close();
    }

    public void viewList(){
        int number =0;
        db = helper.getWritableDatabase();
        Cursor cursor = db.query(table_name, new String[]{"_id", "content", "date"}, null, null, null, null, "date desc");
        adapter = new SimpleCursorAdapter(Main.this,
                R.layout.list, cursor, new String[]{"content",
                "date"}, new int[]{R.id.content, R.id.date});
        lv1.setAdapter(adapter);
        while(cursor.moveToNext()){
            number++;
        }
        if(number==0){
            tvContent ="您现在并没有存储备忘录.";
        } else{
            tvContent ="您现在存储了"+number+"条备忘录.";
        }
    }

    }

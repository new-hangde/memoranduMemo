package com.zxy.memorandummemo.page;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.app.AlertDialog;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.zxy.memorandummemo.Service.Data;
import com.zxy.memorandummemo.Service.DataListAdapter;
import com.zxy.memorandummemo.page.R;
import com.zxy.memorandummemo.Service.DbHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main extends AppCompatActivity {
    private ListView lv1;
    public String db_name = "db_bwl";
    public String table_name = "pic1";
    String deleteContent = "";
    String deleteDate = "";
    String currentId;
    private TextView tv1;
    boolean flag =false;
    String tvContent;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private LinearLayout mLinearLayout;
    DbHelper helper = new DbHelper(this, db_name, null, 1);
    SimpleCursorAdapter adapter;
    SQLiteDatabase db;
    DbHelper helper1 = new DbHelper(this, db_name, null, 1);
    Button bt1;
    Button bt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.pic3);
        Bmob.initialize(this, "b3ee00ae603344986740ed675c712031");
        final BmobConfig config = new BmobConfig.Builder(this)
                .setApplicationId("b3ee00ae603344986740ed675c712031")
                .setConnectTimeout(30)
                .setUploadBlockSize(1024 * 1024)
                .setFileExpiration(2500)
                .build();
        Bmob.initialize(config);
        setContentView(R.layout.main);
        bt1 = (Button) findViewById(R.id.bt1);
        bt2 =(Button)findViewById(R.id.exit);
        lv1 = (ListView) findViewById(R.id.lv1);
        tv1 = (TextView) findViewById(R.id.tv1);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mLinearLayout =(LinearLayout)findViewById(R.id.left_drawer);
        mDrawerList = (ListView) findViewById(R.id.left_list);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    if(TextUtils.isEmpty(currentId)){
                        Intent intent = new Intent();
                        intent.setClass(Main.this, Login.class);
                        startActivityForResult(intent,0);
                    } else{
                        return;
                    }
                }
            }
        });
        bt1.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Main.this, AddAndModify.class);
                startActivity(intent);
            }
        });

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(currentId)) {
                    flag = true;
                }
                if (flag) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                    builder.setTitle("退出")
                            .setMessage("是否退出")
                            .setPositiveButton("是",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            currentId = "";
                                            SharedPreferences uiState = getSharedPreferences("Customers", MODE_APPEND);
                                            SharedPreferences.Editor editor = uiState.edit();
                                            editor.putString("id", currentId);
                                            editor.commit();
                                            viewList();
                                            viewList2();
                                            flag=false;
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
                    AlertDialog ad = builder.create();
                    ad.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG);
                    ad.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                    ad.setCanceledOnTouchOutside(false);
                    ad.show();
                }
            }

        });

        lv1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deleteContent = ((TextView) view.findViewById(R.id.content)).getText().toString();
                deleteDate = ((TextView) view.findViewById(R.id.date)).getText().toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                builder.setTitle("删除")
                        .setMessage("是否删除")
                        .setPositiveButton("是",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (TextUtils.isEmpty(currentId)) {
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
                                        } else{
                                            BmobQuery<Data> query = new BmobQuery<Data>();
                                            query.addWhereEqualTo("id",currentId);
                                            query.setLimit(50);
                                            query.findObjects(new FindListener<Data>() {
                                                @Override
                                                public void done(List<Data> object, BmobException e) {
                                                    if(e==null){
                                                        for (Data data : object) {
                                                            String x =data.getContent();
                                                            String y =data.getObjectId();
                                                            String z =data.getDate();
                                                            if(deleteDate.equals(z)&&deleteContent.equals(x)){
                                                                data.setObjectId(y);
                                                                data.delete(new UpdateListener() {
                                                                    @Override
                                                                    public void done(BmobException e) {
                                                                        if(e==null){
                                                                            Log.i("bmob","成功");
                                                                        }else{
                                                                            Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                                                        }
                                                                    }
                                                                });
                                                                break;
                                                            }
                                                        }
                                                    }else{
                                                        Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                                    }
                                                    enquire();
                                                }
                                            });
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
                AlertDialog ad = builder.create();
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

    private void toggle() {
        if(mDrawerLayout.isDrawerOpen(mLinearLayout)) {
            mDrawerLayout.closeDrawer(mLinearLayout);
        } else {
            mDrawerLayout.openDrawer(mLinearLayout);
        }
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                toggle();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                Bundle bundle = data.getExtras();
                currentId = bundle.getString("id");
        }
    }
    public void onResume() {
        super.onResume();
        viewList();
        enquire();
        viewList2();
    }

    public void onRestart() {
        super.onRestart();
    }

    public void enquire(){
        if(!TextUtils.isEmpty(currentId)) {
            BmobQuery<Data> query = new BmobQuery<Data>();
            query.addWhereEqualTo("id", currentId);
            query.setLimit(50);
            query.findObjects(new FindListener<Data>() {
                @Override
                public void done(List<Data> object, BmobException e) {
                    if (e == null) {
                        Toast toast = Toast.makeText(Main.this, "查询成功：共" + object.size() + "条数据。", Toast.LENGTH_SHORT);
                        toast.show();
                        for (Data data : object) {
                            String x = data.getContent();
                            String y = data.getObjectId();
                            String z = data.getDate();
                            Log.d("content", x);
                            Log.d("id", y);
                            Log.d("date", z);
                        }
                        DataListAdapter adapter = new DataListAdapter(Main.this, R.layout.list, object);
                        lv1.setAdapter(adapter);
                    } else {
                        Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                    }
                }
            });
        }
    }

    public void deleteData(int index) {
        db = helper.getWritableDatabase();
        db.execSQL("DELETE FROM " + "pic1" + " WHERE _id="
                + Integer.toString(index));
        db.close();
    }

    public void viewList() {
        if(TextUtils.isEmpty(currentId)) {
            int number = 0;
            db = helper.getWritableDatabase();
            Cursor cursor = db.query(table_name, new String[]{"_id", "content", "date"}, null, null, null, null, "date desc");
            adapter = new SimpleCursorAdapter(Main.this,
                    R.layout.list, cursor, new String[]{"content",
                    "date"}, new int[]{R.id.content, R.id.date});
            lv1.setAdapter(adapter);
            while (cursor.moveToNext()) {
                number++;
            }
            if (number == 0) {
                tvContent = "您现在并没有存储备忘录.";
            } else {
                tvContent = "您现在存储了" + number + "条备忘录.";
            }
        }
        tv1.setText(tvContent);
    }

    public void viewList2(){
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, getListData(), R.layout.pic,
                new String[]{"image", "text"}, new int[]{R.id.imageView, R.id.textView});
        mDrawerList.setAdapter(simpleAdapter);
    }

    public List<Map<String, Object>> getListData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (TextUtils.isEmpty(currentId)) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("image", R.drawable.pic);
            map.put("text", "您尚未登录，请登录");
            list.add(map);
        } else {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("image", R.drawable.pic2);
            map.put("text", currentId);
            list.add(map);
        }
        HashMap<String, Object> map1 = new HashMap<String, Object>();
        map1.put("image", R.drawable.pic2);
        map1.put("text", "更换壁纸");
        list.add(map1);

        HashMap<String, Object> map2 = new HashMap<String, Object>();
        map2.put("image", R.drawable.pic2);
        map2.put("text", "帮助");
        list.add(map2);
        return list;
    }

}

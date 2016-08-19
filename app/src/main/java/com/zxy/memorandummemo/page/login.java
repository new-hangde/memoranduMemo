package com.zxy.memorandummemo.page;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;


import com.zxy.memorandummemo.Service.Customers;
import com.zxy.memorandummemo.page.R;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.socketio.callback.StringCallback;

/**
 * Created by zxy on 2016/8/12.
 */
public class Login extends AppCompatActivity {
    EditText login_edit;
    EditText edit_pwd;
    Button btn_forget;
    Button btn_create;
    Button btn_login;
    CheckBox checkBox1;
    String id,pwd;
    String save_pwd;
    boolean flag = false;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, "b3ee00ae603344986740ed675c712031");
        BmobConfig config = new BmobConfig.Builder(this)
                .setApplicationId("b3ee00ae603344986740ed675c712031")
                .setConnectTimeout(30)
                .setUploadBlockSize(1024 * 1024)
                .setFileExpiration(2500)
                .build();
        Bmob.initialize(config);
        setContentView(R.layout.login);
        login_edit =(EditText)findViewById(R.id.login_edit);
        edit_pwd =(EditText)findViewById(R.id.edit_pwd);
        btn_create =(Button)findViewById(R.id.btn_create);
        btn_forget =(Button)findViewById(R.id.btn_forget);
        btn_login =(Button)findViewById(R.id.btn_login);
        checkBox1 =(CheckBox)findViewById(R.id.checkBox1);
        login_edit.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edit_pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = login_edit.getText().toString();
                BmobQuery<Customers> query = new BmobQuery<Customers>();
                query.addWhereEqualTo("id", id);
                query.setLimit(1);
                query.findObjects(new FindListener<Customers>() {
                    @Override
                    public void done(List<Customers> object, BmobException e) {
                        if (e == null) {
                            int x = object.size();
                            if (x == 0) {
                                Toast toast = Toast.makeText(Login.this, "账号不存在!", Toast.LENGTH_SHORT);
                                toast.show();
                            } else if(x==1){
                                for (Customers customers : object) {
                                    save_pwd= customers.getPassword();
                                }
                                flag = true;
                            }
                        } else {
                            Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());

                        }
                    }
                });
                if (flag) {
                    pwd =edit_pwd.getText().toString();
                    if (!TextUtils.isEmpty(save_pwd) && pwd.equals(save_pwd)) {
                        SharedPreferences uiState = getSharedPreferences("Customers",MODE_APPEND);
                        SharedPreferences.Editor editor = uiState.edit();
                        editor.putString("id", id);
                        editor.commit();
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putString("id", id);
                        intent.putExtras(bundle);
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        Toast toast = Toast.makeText(Login.this, "密码错误!", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }
        });

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Login.this, Register.class);
                startActivity(intent);
            }
        });

        btn_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(checkBox1.isChecked()){

                }
            }
        });
    }
}

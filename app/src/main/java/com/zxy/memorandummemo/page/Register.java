package com.zxy.memorandummemo.page;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.zxy.memorandummemo.Service.Customers;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


/**
 * Created by zxy on 2016/8/15.
 */
public class Register extends AppCompatActivity {
    EditText register_customer;
    EditText register_pwd;
    EditText ensure_pwd;
    EditText register_name;
    RadioGroup radio;
    RadioButton man,woman;
    EditText register_email;
    ImageView im_pwd;
    ImageView im_pwd2;
    ImageView im_name;
    ImageView im_gender;
    ImageView im_email;
    ImageView im_id;
    Button register;
    String id,pwd,pwd2,name,gender,email,currentId;
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
        setContentView(R.layout.register);
        register_customer =(EditText)findViewById(R.id.register_customer);
        register_pwd =(EditText)findViewById(R.id.register_pwd);
        ensure_pwd =(EditText)findViewById(R.id.ensure_pwd);
        register_name =(EditText)findViewById(R.id.register_name);
        radio =(RadioGroup) findViewById(R.id.radio);
        man =(RadioButton) findViewById(R.id.man);
        woman =(RadioButton)findViewById(R.id.woman);
        register_email =(EditText)findViewById(R.id.register_email);
        im_id =(ImageView)findViewById(R.id.im_id);
        im_pwd =(ImageView)findViewById(R.id.im_pwd);
        im_pwd2 =(ImageView)findViewById(R.id.im_pwd2);
        im_name =(ImageView)findViewById(R.id.im_name);
        im_gender =(ImageView)findViewById(R.id.im_gender);
        im_email =(ImageView)findViewById(R.id.im_email);
        register =(Button)findViewById(R.id.register);
        register_customer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){

                } else{
                    analyseId();
                }
            }
        });
        /*register_customer.addTextChangedListener(new TextWatcher() {
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

        analyseId();*/

        register_pwd.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                 pwd = register_pwd.getText().toString();
                if (TextUtils.isEmpty(pwd)) {
                    return;
                } else {
                    if (temp.length() >= 6) {
                        im_pwd.setImageResource(R.drawable.right);
                    } else {
                        im_pwd.setImageResource(R.drawable.wrong);
                    }
                }
            }
        });

        ensure_pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                pwd2 = ensure_pwd.getText().toString();
                if (TextUtils.isEmpty(pwd2)) {
                    return;
                } else {
                    if (pwd2.equals(pwd)) {
                        im_pwd2.setImageResource(R.drawable.right);
                        currentId =pwd2;
                    } else {
                        im_pwd2.setImageResource(R.drawable.wrong);
                    }
                }
            }
        });

        register_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                name = register_name.getText().toString();
                if(TextUtils.isEmpty(name)){
                   return;
                } else {
                    im_name.setImageResource(R.drawable.right);
                }
            }
        });

        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            public void onCheckedChanged(RadioGroup group,int checkId){
                if(checkId==man.getId()){
                    gender ="男";
                } else{
                    gender ="女";
                }
            }
        });

        register_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                email =register_email.getText().toString();
                if(TextUtils.isEmpty(gender)){
                    return;
                } else {
                    im_email.setImageResource(R.drawable.right);
                }
            }
        });

        register.setOnClickListener (new View.OnClickListener()   {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(currentId)) {
                    Customers customers = new Customers();
                    customers.setId(id);
                    customers.setGender(gender);
                    customers.setName(name);
                    customers.setEmail(email);
                    customers.setPassword(currentId);
                    customers.save(new SaveListener<String>() {
                        public void done(String objectId,BmobException e) {
                            if(e==null){
                                return;
                            }else{
                                Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                            }
                        }
                    });
                }
            }
        });
    }

    public  void analyseId(){
        id = register_customer.getText().toString();
        Log.d("id",id);
        if (TextUtils.isEmpty(id)) {
            return;
        } else {
            BmobQuery<Customers> query = new BmobQuery<Customers>();
            query.addWhereEqualTo("id", id);
            query.findObjects(new FindListener<Customers>() {
                @Override
                public void done(List<Customers> object, BmobException e) {
                    if (e == null) {
                        Toast toast = Toast.makeText(Register.this, "查询成功：共"+object.size()+"条数据。", Toast.LENGTH_SHORT);
                        toast.show();
                        int x = object.size();
                        if(x==0){
                            im_id.setImageResource(R.drawable.right);
                        } else {
                            im_id.setImageResource(R.drawable.wrong);
                        }
                    } else {
                        im_id.setImageResource(R.drawable.wrong);
                    }
                }
            });
        }
    }

}

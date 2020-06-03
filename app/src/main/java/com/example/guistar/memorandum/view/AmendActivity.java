package com.example.guistar.memorandum.view;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.example.guistar.memorandum.Database.MyDB;
import com.example.guistar.memorandum.R;
import com.example.guistar.memorandum.fragment.FragmentHome;
import com.example.guistar.memorandum.fragment.FragmentMain;
import com.example.guistar.memorandum.enity.Record;


public class AmendActivity extends AppCompatActivity implements View.OnClickListener{

    MyDB myDB;
    private Button btnSave;
    private Button btnBack;
    private TextView amendTime;
    private TextView amendTitle;
    private EditText amendBody;
    private Record record;
    private AlertDialog.Builder dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amend);
        init();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_save:
                if (updateFunction(amendBody.getText().toString())){
                    intentStart();
                }
                break;
            case R.id.button_back:
                showDialog(  amendBody.getText().toString());
                clearDialog();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //当返回按键被按下
            if (!isShowIng()){
                showDialog(amendBody.getText().toString());
                clearDialog();
            }
        }
        return false;
    }
    /*
     * 初始化函数
     */
    @SuppressLint("SetTextI18n")
    void init(){
        myDB = new MyDB(this);
        btnBack = findViewById(R.id.button_back);
        btnSave = findViewById(R.id.button_save);
        amendTitle = findViewById(R.id.amend_title);
        amendBody = findViewById(R.id.amend_body);
        amendTime = findViewById(R.id.amend_title_time);

        btnSave.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        Intent intent = this.getIntent();
        if (intent!=null){

            String title=intent.getStringExtra("title_name");
            String body=intent.getStringExtra("text_body");
            String time=intent.getStringExtra("create_time");
            amendTitle.setText(title);
            amendTime.setText(time);
            amendBody.setText(body);
        }else{
            Toast.makeText(this,"内容过长",Toast.LENGTH_SHORT).show();
        }
    }
    /*
     * 返回主界面
     */
    void intentStart(){
    //   Intent intent = new Intent(this,FragmentMain.class);
    //   startActivity(intent);
        Intent intent=new Intent();
        setResult(1,intent);
        this.finish();

    }
    /*
     * 保存函数
     */
    public boolean updateFunction(String body){
        boolean flag = true;
       SQLiteDatabase db;
        ContentValues values;
        if (body.length()>200){
            Toast.makeText(this,"内容过长",Toast.LENGTH_SHORT).show();
            flag = false;
        }
        if(flag){
            // update
            db = myDB.getWritableDatabase();
            values = new ContentValues();
            values.put(MyDB.RECORD_BODY, amendBody.getText().toString());
            values.put(MyDB.RECORD_TIME,getNowTime());
            db.update("record",values,"title_name=?",
                    new String[]{amendTitle.getText().toString()});
            Toast.makeText(this,"修改成功",Toast.LENGTH_SHORT).show();
            db.close();
        }
       return flag;
    }
    /*
     * 弹窗函数
     */
    void showDialog(final String body){
        dialog = new AlertDialog.Builder(AmendActivity.this);
        dialog.setTitle("提示");
        dialog.setMessage("是否保存当前编辑内容");
        dialog.setPositiveButton("保存",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateFunction(body);
                        intentStart();
                    }
                });

        dialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        intentStart();
                    }
                });
        dialog.show();
    }

    void clearDialog(){
        dialog = null;
    }

    boolean isShowIng(){
        if (dialog!=null){
            return true;
        }else{
            return false;
        }
    }
    /*
     * 得到当前时间
     */
    String getNowTime(){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

}

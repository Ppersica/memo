package com.example.guistar.memorandum.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.guistar.memorandum.R;
import com.example.guistar.memorandum.Database.UserDBHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Forget extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private EditText et_name;
    private EditText et_username;
    private EditText et_password;
    private Button bt_reset;
    private SQLiteDatabase db;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        et_name= (EditText) findViewById(R.id.textname);
        et_username = (EditText) findViewById(R.id.textuser);
        et_password= (EditText) findViewById(R.id.textword);
        bt_reset= (Button) findViewById(R.id.btnforget);
       bt_reset.setOnClickListener(this) ;
        }
    public void onClick(View v) {
      ReSetPass();
    }
    public void ReSetPass() {
        boolean isReSetPassFinish=false;
        String Name = et_name.getText().toString();
        String UserName = et_username.getText().toString();
        String newPass = et_password.getText().toString();
        //是否完整输入各项内容
        if (Name.equals("") || UserName.equals("") || newPass.equals("")) {
            Toast.makeText(this, "请输入改密所需的每项内容", Toast.LENGTH_SHORT).show();
        }else {
           db = new UserDBHelper(this).getWritableDatabase();
           Cursor cursor = db.query("userData", new String[]{"name","user"}, null, null, null, null, null);
           //验证账号与改密口令是否相符
            while (cursor.moveToNext()) {
                if (Name.equals(cursor.getString(cursor.getColumnIndex("name")))&& UserName.equals(cursor.getString(cursor.getColumnIndex("user")))) {
                    ContentValues values = new ContentValues();
                    values.put("word", newPass);
                    db.update("userData",values,"name = ? and user= ?",new String[]{Name,UserName});
                    Toast.makeText(this,"密码修改成功！\n请牢记新密码并使用新密码登录",Toast.LENGTH_LONG).show();
                    isReSetPassFinish=true;
                    break;
                }
            }
            //未成功匹配账号&改密口令
            if (!isReSetPassFinish){
            Toast.makeText(this,"账号不存在或姓名错误",Toast.LENGTH_LONG).show();
            }
        }
    }
}

package com.example.guistar.memorandum.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.guistar.memorandum.R;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.guistar.memorandum.Database.UserDBHelper;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

public class Register extends AppCompatActivity implements View.OnClickListener {
    private Button register;
    private EditText user;
    private EditText username;
    private EditText password;
    private UserDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username = (EditText) findViewById(R.id.textname);
        user= (EditText) findViewById(R.id.textuser);
        password= (EditText) findViewById(R.id.textword);
        register = (Button) findViewById(R.id.btnregister);
        register.setOnClickListener(this);
    }
    public void onClick(View v) {
      RegisterSave();
    }
    private void RegisterSave() {
        String name = username.getText().toString();
        String username = user.getText().toString();
        String pass= password.getText().toString();
        boolean creatUser = true;
        if (username.equals("") || name.equals("") || pass.equals("")) {
            Toast.makeText(this, "请完整输入各项注册内容", Toast.LENGTH_SHORT).show();
        } else if (pass.length() < 6) {
            Toast.makeText(this, "密码小于六位数，请重新输入", Toast.LENGTH_SHORT).show();
        } else {
            dbHelper = new UserDBHelper(Register.this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.query("userData", new String[]{"user"}, null, null, null, null, null);
            while (cursor.moveToNext()) {
                if (username.equals(cursor.getString(cursor.getColumnIndex("user")))) {
                    Toast.makeText(Register.this, "该账户已存在", Toast.LENGTH_SHORT).show();
                    creatUser = false;
                }
            }
            if (creatUser) {
                ContentValues values = new ContentValues();
                values.put("name", name);
                values.put("user", username);
                values.put("word", pass);
                db.insert("userData", null, values);
                AlertDialog.Builder dialog = new AlertDialog.Builder(Register.this);
                dialog.setTitle("注册成功");
                dialog.setMessage("您已成功注册账户，请返回登录界面");
                dialog.setPositiveButton("返回登录界面", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                dialog.setNegativeButton("留在注册界面", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog.show();
            }
        }
}
}

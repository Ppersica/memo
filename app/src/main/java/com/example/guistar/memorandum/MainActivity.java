package com.example.guistar.memorandum;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;
import com.example.guistar.memorandum.Database.UserDBHelper;
import com.example.guistar.memorandum.fragment.FragmentMain;
import com.example.guistar.memorandum.view.Register;
import com.example.guistar.memorandum.view.Forget;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {
    private Button bt_login;
    private EditText et_Name;
    private EditText et_Pass;
    private CheckBox remember;
    private TextView tv_register;
    private TextView tv_forget;
    private SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        bt_login = (Button) findViewById(R.id.btnlogin);
        et_Name = (EditText) findViewById(R.id.textname);
        et_Pass = (EditText) findViewById(R.id.textword);
        remember = (CheckBox) findViewById(R.id.checkBox);
        tv_forget= (TextView) findViewById(R.id.textforget);
        tv_register = (TextView) findViewById(R.id.textregister);
        bt_login.setOnClickListener(this);
        tv_forget.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        isRemember();
    }
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnlogin: {
                    Login();
                }
                break;
                case R.id.textregister: {
                    Intent intent = new Intent(MainActivity.this, Register.class);
                    startActivity(intent);
                }
                break;
                case R.id.textforget: {
                    Intent intent = new Intent(MainActivity.this, Forget.class);
                    startActivity(intent);
                }
            }
        }
        /**     * 登录账号验证     * 判断是否勾选记住密码     */
        private void Login() {
            String name = et_Name.getText().toString();
            String pass = et_Pass.getText().toString();        //判断是否输入内容
            if (name.equals("") || pass.equals("")) {
                Toast.makeText(this, "请输入账号或密码", Toast.LENGTH_SHORT).show();
            } else {//输入了账号&密码
               db = new UserDBHelper(this).getWritableDatabase();
               Cursor cursor = db.query("userData", new String[]{"user", "word"}, null, null, null, null, null);
               boolean login = false;//账号密码是否匹配            //从数据库中匹配账号密码
               while (cursor.moveToNext()) {
                   if (name.equals(cursor.getString(cursor.getColumnIndex("user"))) && pass.equals(cursor.getString(cursor.getColumnIndex("word")))) {
                       login = true;
                       break;
                   }
               }
               //如果匹配成功，判断是否勾选记住账号，并进行账号信息存储
                if (login) {
                   SharedPreferences.Editor editor = getSharedPreferences("remember", MODE_PRIVATE).edit();
                   if (remember.isChecked()) {
                       editor.putBoolean("remember", true);
                       editor.putString("name", name);
                       editor.putString("pass", pass);
                       editor.commit();
                   } else {
                       editor.clear();
                   }
                   editor.commit();
                   Intent intent = new Intent(this, FragmentMain.class);
                   startActivity(intent);
                   this.finish();
               } else {
                   //账号密码在数据库中匹配不成功
                    Toast.makeText(this, "账号与密码不匹配", Toast.LENGTH_SHORT).show();
                    }
            }
        }
        private void InitPopup() {

        }
        /**     * 判断是否是否保存过密码     */
        public void isRemember() {
            SharedPreferences prefer = getSharedPreferences("remember", MODE_PRIVATE);
            boolean isRemember = prefer.getBoolean("remember", false);
            if (isRemember) {
                et_Name.setText(prefer.getString("name", ""));
                et_Pass.setText(prefer.getString("pass", ""));
                remember.setChecked(true);
            }
        }
}


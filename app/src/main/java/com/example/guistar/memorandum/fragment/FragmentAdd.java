package com.example.guistar.memorandum.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.guistar.memorandum.Database.MyDB;
import com.example.guistar.memorandum.util.MyTimeGetter;
import com.example.guistar.memorandum.R;
import java.text.SimpleDateFormat;
import java.util.Date;
import static com.example.guistar.memorandum.util.MyFormat.getTimeStr;

public class FragmentAdd extends Fragment {
    MyDB myDB;
    private final static String TAG = "FragmentAdd";
    private Button btnSave;
    private Button btnBack;
    private TextView editTime;
    private EditText editTitle;
    private EditText editBody;
    private AlertDialog.Builder dialog;
    private DatePickerDialog dialogDate;
    private TimePickerDialog dialogTime;
    private String createDate;//完整的创建时间，插入数据库
    private String dispCreateDate;//创建时间-显示变量可能会去除年份
    private Integer year;
    private Integer month;
    private Integer dayOfMonth;
    private Integer hour;
    private Integer minute;
    private boolean timeSetTag;
    MyTimeGetter myTimeGetter;
    private String title;
     private String body;
    public FragmentAdd() {
        // TODO Auto-generated constructor stub
    }
    @Override
     public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
     // return inflater.inflate(R.layout.activity_fragment_add, container, false);
        View view = inflater.inflate(R.layout.activity_fragment_add, container, false);
        btnBack = (Button) view.findViewById(R.id.button_back);
        btnSave = (Button) view.findViewById(R.id.button_save);
        editTitle = (EditText)view.findViewById(R.id.edit_title);
        editBody = (EditText)view.findViewById(R.id.edit_body);
        editTime =(TextView)view.findViewById(R.id.edit_title_time);
        title = editTitle.getText().toString().trim();
        body = editBody.getText().toString().trim();
        return view;
    }
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        init();
        if (editTime.getText().length()==0)
            editTime.setText(dispCreateDate);
        btnSave.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if (saveFunction(title,body,createDate)){
                    intentStart();
                }
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if (title!=null||body!=null){
                    showDialog(title,body,createDate);
                    clearDialog();
                } else {
                    intentStart();
                }
            }
        });
    }
    /*
     * 初始化函数
     */
    void init(){
        myDB = new MyDB(getActivity());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");// HH:mm:ss
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        createDate = simpleDateFormat.format(date);
        dispCreateDate =getTimeStr(date);
        dialogDate = null;
        dialogTime = null;
        hour = 0;
        minute = 0;
        year = 0;
        month = 0;
        dayOfMonth = 0;
        timeSetTag = false;
    }
    /*
     *  返回键监听，消除误操作BUG
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            String title;
            String body;
            String createDate;
            title = editTitle.getText().toString();
            body = editBody.getText().toString();
            createDate = editTime.getText().toString();
            //当返回按键被按下
            if (!isShowIng()){
                if (!"".equals(title)||!"".equals(body)){
                    showDialog(title,body,createDate);
                    clearDialog();
                } else {
                    intentStart();
                }
            }
        }
        return false;
    }
    /*
     * 返回主界面
     */
    void intentStart(){
        Intent intent = new Intent(getActivity(),FragmentMain.class);
        startActivity(intent);
        getActivity().finish();
    }
    /*
     * 备忘录保存函数
     */
    boolean saveFunction(String title,String body,String createDate){
        boolean flag = true;
        if (title==null){
            Toast.makeText(getActivity(),"标题不能为空",Toast.LENGTH_SHORT).show();
            flag = false;
        }
        if (title.length()>10){
            Toast.makeText(getActivity(),"标题过长",Toast.LENGTH_SHORT).show();
            flag = false;
        }
        if (body.length()>200){
            Toast.makeText(getActivity(),"内容过长",Toast.LENGTH_SHORT).show();
            flag = false;
        } else if("".equals(createDate)){
            Toast.makeText(getActivity(),"时间格式错误",Toast.LENGTH_SHORT).show();
            flag = false;
        }

        if(flag){
            SQLiteDatabase db;
            ContentValues values;
            //  存储备忘录信息
            db = myDB.getWritableDatabase();
            values = new ContentValues();
            values.put(MyDB.RECORD_TITLE,editTitle.getText().toString().trim());
            values.put(MyDB.RECORD_BODY,editBody.getText().toString().trim());
            values.put(MyDB.RECORD_TIME,createDate);
            db.insert(MyDB.TABLE_NAME_RECORD,null,values);
            Toast.makeText(getActivity(),"保存成功",Toast.LENGTH_SHORT).show();
            db.close();
        }
        return flag;
    }
    /*
     * 弹窗函数
     */
    void showDialog(final String title, final String body, final String createDate){
        dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("提示");
        dialog.setMessage("是否保存当前编辑内容");
        dialog.setPositiveButton("保存",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveFunction(title, body, createDate);
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
    /*
     *  清空弹窗
     */
    void clearDialog(){
        dialog = null;
    }
    /*
     *  判断是否弹窗是否显示
     */
    boolean isShowIng(){
        if (dialog!=null){
            return true;
        }else{
            return false;
        }
    }
}
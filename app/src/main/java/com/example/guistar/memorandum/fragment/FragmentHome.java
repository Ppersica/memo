package com.example.guistar.memorandum.fragment;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.example.guistar.memorandum.Database.MyDB;
import com.example.guistar.memorandum.R;
import com.example.guistar.memorandum.view.AmendActivity;
import android.support.v7.app.AlertDialog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.content.DialogInterface;
import android.widget.Toast;

public class FragmentHome extends Fragment {

    private MyDB myDB;
    private ListView myListView;
    MyBaseAdapter myBaseAdapter;
    List<Map<String,Object>> recordList;
    Map<String,Object> listdateils;//存储点击的启事的数据
    public FragmentHome() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_home, container, false);
        myListView = (ListView)view.findViewById(R.id.list_view);
        recordList = new ArrayList<Map<String,Object>>();
        listdateils=new HashMap<String,Object>();
        // 创建一个Adapter的实例
        myBaseAdapter = new MyBaseAdapter(records(),getActivity());
        myListView.setAdapter(myBaseAdapter);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listdateils=recordList.get(position);
                detailsActiviy();
            }
        });
        myListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String,Object> map= recordList.get(position);
                showDialog(map,position);
                return true;
            }
        });
        return view;
    }

    public void detailsActiviy(){
        Intent intent = new Intent(getActivity(), AmendActivity.class);
        intent.putExtra("title_name",(String)listdateils.get("title_name"));
        intent.putExtra("text_body",(String) listdateils.get("text_body"));
        intent.putExtra("create_time",(String) listdateils.get("create_time"));
        startActivityForResult(intent,1);
    }

    private List<Map<String,Object>> records(){
        this.myDB = MyDB.getInstance(getActivity());
        SQLiteDatabase db = myDB.getReadableDatabase();
        String sql = "SELECT _id,title_name,text_body,create_time FROM record";

        Cursor cur = db.rawQuery(sql, null);
        if(cur.getCount()==0){
            Toast.makeText(getActivity(), "目前没有新的备忘录", Toast.LENGTH_SHORT).show();
        }else {
            for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("_id", cur.getInt(0));
                //Bitmap imagebitmap = BitmapFactory.decodeByteArray(cur.getBlob(1), 0, cur.getBlob(1).length);
                map.put("title_name", cur.getString(1));
                map.put("text_body", cur.getString(2));
                map.put("create_time", cur.getString(3));
                recordList.add(map);
            }
        }
        db.close();

        return recordList;
    }
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==1&&resultCode==1){
            clear();
            lost();
        }
    }

    public void clear() {
        if (recordList.size() > 0) {
            recordList.removeAll(recordList);
            myBaseAdapter.notifyDataSetChanged();
            //myListView.setAdapter(myBaseAdapter);

        }
    }
    private void lost(){
        myBaseAdapter = new MyBaseAdapter(records(),getActivity());
        myListView.setAdapter(myBaseAdapter);
    }
    class MyBaseAdapter extends BaseAdapter {
        private List<Map<String,Object>> list;
        private LayoutInflater inflater;
        public MyBaseAdapter(List<Map<String,Object>> list, Context context) {
            this.list = list;
            inflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            if (recordList!=null&&recordList.size()>0)
                return recordList.size();
            else
                return 0;
        }

        @Override
        public Object getItem(int position) {
            if (recordList!=null&&recordList.size()>0)
                return recordList.get(position);
            else
                return null;
        }

        public void removeItem(int position){
            recordList.remove(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
        public View getView(int postion,View convertView,ViewGroup parent){
            ViewHolder viewHolder;
            if(convertView==null) {
                convertView = LayoutInflater.from(getActivity().getApplicationContext()).
                        inflate(R.layout.list_item, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.titleView = convertView.findViewById(R.id.list_item_title);
                viewHolder.bodyView = convertView.findViewById(R.id.list_item_body);
                viewHolder.timeView = convertView.findViewById(R.id.list_item_time);
                convertView.setTag(viewHolder);
            }else{
                viewHolder=(ViewHolder)convertView.getTag();
            }
            viewHolder.titleView.setText((String)recordList.get(postion).get("title_name"));

            viewHolder.bodyView.setText((String)recordList.get(postion).get("text_body"));
//
            viewHolder.timeView.setText((String)recordList.get(postion).get("create_time"));
            return convertView;
        }

    }

    class ViewHolder{
        TextView titleView;
        TextView bodyView;
        TextView timeView;
    }

    void showDialog( final Map<String,Object> map,final int position){

        final AlertDialog.Builder dialog =
                new AlertDialog.Builder(getActivity());
        dialog.setTitle("是否删除？");
        String textBody = (String)map.get("text_body");
        dialog.setMessage(
                textBody.length()>150?textBody.substring(0,150)+"...":textBody);
        dialog.setPositiveButton("删除",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase db = myDB.getWritableDatabase();
                        db.delete("record",
                                "title_name=?",
                                new String[]{(String)map.get("title_name")});
                        db.close();
                        myBaseAdapter.removeItem(position);
                        myListView.post(new Runnable() {
                            @Override
                            public void run() {
                                myBaseAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                });
        dialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        dialog.show();
    }
}

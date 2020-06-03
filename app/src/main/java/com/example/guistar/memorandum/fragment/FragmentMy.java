package com.example.guistar.memorandum.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.example.guistar.memorandum.R;
import com.example.guistar.memorandum.view.Forget;

public class FragmentMy extends Fragment  {
    private RelativeLayout tuichu;
    private RelativeLayout xiugai;
    public FragmentMy() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.activity_fragment_my, container, false);
        View view = inflater.inflate(R.layout.activity_fragment_my, container, false);
        tuichu=(RelativeLayout)view.findViewById(R.id.rl_exit_login);
        xiugai=(RelativeLayout)view.findViewById(R.id.rl_modify_psw);
        return view;
}
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        tuichu.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
          getActivity().finish();
            }
        });
        xiugai.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(getActivity(),Forget.class);
                startActivity(intent);
            }
        });
    }}
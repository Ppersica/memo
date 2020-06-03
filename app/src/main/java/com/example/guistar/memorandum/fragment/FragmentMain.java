package com.example.guistar.memorandum.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import com.example.guistar.memorandum.R;
import android.os.Bundle;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.example.guistar.memorandum.MyFragmentPageAdapter;
import java.util.ArrayList;
import java.util.List;

public class FragmentMain extends FragmentActivity implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {
    //定义Fragment
    private Fragment fragment1;
    private Fragment fragment2;
    private Fragment fragment3;
    //定义FragmentManager
    private FragmentManager fragmentManager;
    //定义组件
    private ViewPager viewPager;
    private List<Fragment> fragmentLists;
    private MyFragmentPageAdapter adapter;
    private RadioGroup radioGroup;
    private RadioButton home; // 表示第一个RadioButton 组件

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//取消ActionBar
        setContentView(R.layout.activity_fragment_main);
        //初始化界面组件
        init();
        //初始化ViewPager
        initViewPager();
    }

    private void initViewPager() {
        fragment1 = new FragmentHome();
        fragment2 = new FragmentAdd();
        fragment3 = new FragmentMy();

        fragmentLists = new ArrayList<Fragment>();
        fragmentLists.add(fragment1);
        fragmentLists.add(fragment2);
        fragmentLists.add(fragment3);
        //获取FragmentManager对象
        fragmentManager = getSupportFragmentManager();
        //获取FragmentPageAdapter对象
        adapter = new MyFragmentPageAdapter(fragmentManager, fragmentLists);
        //设置Adapter，使ViewPager 与 Adapter 进行绑定
        viewPager.setAdapter(adapter);
        //设置ViewPager默认显示第一个View
        viewPager.setCurrentItem(0);
        //设置第一个RadioButton为默认选中状态
        home.setChecked(true);
        //ViewPager页面切换监听
        viewPager.addOnPageChangeListener(this);
    }

    private void init() {
        radioGroup = (RadioGroup) findViewById(R.id.radioGrop);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        home = (RadioButton) findViewById(R.id.main_home);
        //RadioGroup状态改变监听
        radioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.main_home: // 首页
                //显示第一个Fragment并关闭动画效果
                viewPager.setCurrentItem(0,false);
                break;
            case R.id.main_add: // 添加
                viewPager.setCurrentItem(1,false);
                break;
            case R.id.main_my: // 我的
                viewPager.setCurrentItem(2,false);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {}

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {}

    /**
     * ViewPager切换Fragment时，RadioGroup做相应的监听
     */
    @Override
    public void onPageSelected(int arg0) {
        switch (arg0) {
            case 0:
                radioGroup.check(R.id.main_home);
                break;
            case 1:
                radioGroup.check(R.id.main_add);
                break;
            case 2:
                radioGroup.check(R.id.main_my);
                break;
        }
    }
}
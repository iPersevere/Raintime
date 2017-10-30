package com.dell.raintime.fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dell.raintime.R;

//import static com.baidu.location.d.j.R;


/**
 * 文章页面
 * @author itlanbao.com
 */
public class FramlandConditionFragment extends MyFragment   {

    private Context context;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_farmland_condition, container, false);

        // 缓存的view需要判断是否已经被加过parent，
        // 如果有parent需要从parent删除，要不然会发生这个view已经有parent的错误。
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        return view;
    }


    @Override
    protected void onVisible(boolean isInit) {
        if(isInit){
            initView();//初始化控件
//            netGetData();//网络访问，获取列表数据
        }
    }

    /**
     * 适配器填充listView数据
     */
    private void initView() {

    }


    /**
     * /网络访问，获取列表数据
     */
    private void netGetData() {

    }


}

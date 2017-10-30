package com.dell.raintime.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dell.raintime.R;

//import static com.baidu.location.d.j.R;

/**
 * 信息
 *
 * @author itlanbao.com
 */
public class MessageFragment extends MyFragment implements View.OnClickListener  {
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_message, container, false);

        return view;
    }



    /**
     * onClick事件
     */
    @Override
    public void onClick(View view) {
    }




    @Override
    protected void onVisible(boolean isInit) {

    }
}

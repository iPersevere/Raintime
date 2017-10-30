package com.dell.raintime.fragment;

/**
 * Created by Administrator on 2015/9/30 0030.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.dell.raintime.activity.BaseActivity;
import com.dell.raintime.application.Constant;
import com.dell.raintime.tabhost.FragmentInterface;


/**
 * 自定义Fragment，用来处理多层Fragment时，点击back键返回上一层
 *
 * @author itlanbao.com
 *
 */
public abstract class MyFragment extends Fragment implements FragmentInterface {

    private boolean isOnInit = true;

    @Override
    public void onBackPressed() {
//		this.getActivity().finish();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null && isSaveState()) {
            isOnInit = savedInstanceState.getBoolean(com.dell.raintime.application.Constant.KEY_TAB_INIT);
        }

        if (isVisibleToUser) {
            onVisible(isOnInit);
            isOnInit = false;
        }
    }

    private boolean isVisibleToUser = false;
    //setUserVisibleHint和onResume只记录一次,用些变量作为标记
    private boolean hasTrace = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        if (isVisibleToUser) {
            hasTrace = true;
        }


        if (isVisibleToUser && getView() != null) {
            onVisible(isOnInit);
            isOnInit = false;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (isSaveState()) {
            outState.putBoolean(Constant.KEY_TAB_INIT, isOnInit);
        }
    }

    @Override
    public void onResume() {
        if (isVisibleToUser && !hasTrace) {
            if (isTrace()) {
                // 在退出app的时候activity执行onNewIntent时，再次执行onResume导致fragment执行onResume
                try {
                    if(!((BaseActivity)getActivity()).isExit()){
//                        UmsAgent.onCome(getActivity(), this);
                    }
                } catch (Exception e) {
                }
            }
        }
        hasTrace = false;
        super.onResume();
    }

    /**
     * 是否记录事件轨迹
     *
     * @return
     */
    protected boolean isTrace() {
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
        hasTrace = false;
    }

    /**
     * 是否保存初始化状态
     *
     * @return
     */
    public boolean isSaveState() {
        return true;
    }

    /**
     * 监听Fragment是否显示，isInit是否初为首次初始化，当把Fragment加入TabFragment时使用<br>
     *
     * 由于ViewPager是预加载，所以联网获取数据需要判断当前Fragment是否显示，然后在获取数据
     */
    protected abstract void onVisible(boolean isInit);
}

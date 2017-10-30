package com.dell.raintime.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.dell.raintime.R;
import com.dell.raintime.amap.pathrecord.BasicmapActivity;
import com.dell.raintime.entity.TabMode;
import com.dell.raintime.fragment.HomeFragment;
import com.dell.raintime.fragment.MessageFragment;
import com.dell.raintime.fragment.ShopFragment;
import com.dell.raintime.libs.DragLayout;
import com.dell.raintime.libs.MyRelativeLayout;
import com.dell.raintime.tabhost.TabFragment;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends BaseActivity {

    public static final int HOME_TAB = 1000;
    public static final int SEARCH_TAB = 2000;
    public static final int HOME_TAB_MESSAGE = 3000;


    private Context context;
    /**
     * 左边侧滑菜单
     */
    private DragLayout mDragLayout;
    private LinearLayout menu_header;
    private TextView menu_setting;

    private ListView menuListView;// 菜单列表


    private TabFragment actionBarFragment;



    private MyRelativeLayout myRelativeLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initView();// 初始化控件

        // 左侧页面listView添加数据
        List<Map<String, Object>> data = getMenuData();

        menuListView.setAdapter(new SimpleAdapter(this, data,
                R.layout.left_menu_item, new String[]{"item", "image", "iv"},
                new int[]{R.id.tv_item, R.id.iv_item, R.id.iv}));

    }

    /**
     * 控件初始化
     */
    private void initView() {
        context = this;
        MangerActivitys.addActivity(context);


        // 点击back按钮
        actionBarFragment = (TabFragment) getSupportFragmentManager().findFragmentById(R.id.tab_bar_fragment);

        int code = 1;
        final ArrayList<TabMode> listTabModes = new ArrayList<TabMode>();
        {// 缘分
            final TabMode tabMode = new TabMode(HOME_TAB, R.drawable.tab_1_selector,
                    "设备", R.color.tab_text_color_selector, new HomeFragment(), code == 1);
            listTabModes.add(tabMode);
        }

        {// 搜索
            final TabMode tabMode = new TabMode(SEARCH_TAB, R.drawable.tab_2_selector,
                    "商城", R.color.tab_text_color_selector, new ShopFragment(), code == 2);//
            listTabModes.add(tabMode);

        }

        {// 消息
            final TabMode tabMode = new TabMode(HOME_TAB_MESSAGE, R.drawable.tab_3_selector,
                    "信息", R.color.tab_text_color_selector, new MessageFragment(), code == 3);
            listTabModes.add(tabMode);
        }




        actionBarFragment.creatTab(HomeActivity.this, listTabModes, new TabFragment.IFocusChangeListener() {

            @Override
            public void OnFocusChange(int currentTabId, int tabIndex) {

            }
        });




        //这部分是底部menu的view控件
        menu_setting = (TextView) this.findViewById(R.id.iv_setting);
        menu_header = (LinearLayout) this.findViewById(R.id.menu_header);
        mDragLayout = (DragLayout) findViewById(R.id.dl);


        myRelativeLayout = (MyRelativeLayout) findViewById(R.id.rl_layout);
        mDragLayout.setBorder(actionBarFragment);
        myRelativeLayout.setDragLayout(mDragLayout);



        /**
         * 抽屜动作监听
         */
        mDragLayout.setOnLayoutDragingListener(new DragLayout.OnLayoutDragingListener() {

            @Override
            public void onOpen() {
                //打开
            }
            @Override
            public void onDraging(float percent) {
                //滑动中
            }
            @Override
            public void onClose() {
                //关闭
            }
        });

        menuListView = (ListView) findViewById(R.id.menu_listview);// 抽屉列表
        initResideListener();// 个人中心、设置点击事件
    }

    public void QRScan(View view){
        Intent intent = new Intent(HomeActivity.this, CaptureActivity.class);
        startActivityForResult(intent,0);
    }

    public void updateLocation(View view){
        Intent intent = new Intent(HomeActivity.this,BasicmapActivity.class);
        startActivity(intent);

        //Toast.makeText(HomeActivity.this,"位置已更新，请注意查看",Toast.LENGTH_LONG).show();
    }

    /**
     * 个人中心、设置点击事件
     */
    private void initResideListener() {
        // 点击个人中心
        menu_header.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            }
        });
        // 进入设置界面
        menu_setting.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //                Intent intent = new Intent(context, SettingsActivity.class);
                //                startActivity(intent);
            }
        });

    }

    /**
     * 加载抽屉列表数据
     *
     * @return
     */
    private List<Map<String, Object>> getMenuData() {
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        Map<String, Object> item;

        item = new HashMap<String, Object>();
        item.put("item", "租赁管理");
        item.put("image", R.drawable.ic_launcher);
        item.put("iv", R.drawable.icon_kehu_arrow);
        data.add(item);

        item = new HashMap<String, Object>();
        item.put("item", "历史查询");
        item.put("image", R.drawable.ic_launcher);
        item.put("iv", R.drawable.icon_kehu_arrow);
        data.add(item);

        item = new HashMap<String, Object>();
        item.put("item", "使用帮助");
        item.put("image", R.drawable.ic_launcher);
        item.put("iv", R.drawable.icon_kehu_arrow);
        data.add(item);

        item = new HashMap<String, Object>();
        item.put("item", "设置");
        item.put("image", R.drawable.ic_launcher);
        item.put("iv", R.drawable.icon_kehu_arrow);
        data.add(item);

        return data;
    }



    /**
     * activity对象回收
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (int i = 0; i < MangerActivitys.activitys.size(); i++) {
            if (MangerActivitys.activitys.get(i) != null) {
                ((Activity) MangerActivitys.activitys.get(i)).finish();
            }
        }
        finish();
        System.gc();
    }

    /**
     * 返回键监听
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("确定要退出吗？")
                    .setCancelable(false)
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    Intent intent = new Intent(
                                            Intent.ACTION_MAIN);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.addCategory(Intent.CATEGORY_HOME);
                                    startActivity(intent);
                                }
                            })
                    .setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

}


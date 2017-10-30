package com.dell.raintime.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

//import com.dell.Raintime.R;
import com.dell.raintime.R;

//import static com.baidu.location.d.j.R;


/**
 * 推荐页面
 * @author itlanbao.com
 */
public class DeviceInformationFragment extends MyFragment implements OnItemClickListener {

    private View view;

    private ListView listView;
    private MyAdapter adapter;
    private int[] names = {R.string.device_info_1,R.string.device_info_2};
    private int[] icons = {R.mipmap.picture_my_procduct1,R.mipmap.picture_my_procduct2};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_device_information, container, false);

        // 缓存的view需要判断是否已经被加过parent，
        // 如果有parent需要从parent删除，要不然会发生这个view已经有parent的错误。
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }

        adapter = new MyAdapter();
        listView = (ListView)view.findViewById(R.id.lv);
        listView.setAdapter(adapter);

        return view;
    }

    @Override
    protected void onVisible(boolean isInit) {

        if(isInit){
            initView();//初始化控件
            netGetData();//网络访问，获取列表数据
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

    /**
     * 控件监听事件
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


    }

    public class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return names.length;
        }

        @Override
        public Object getItem(int position) {
            return names[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //            View view = View.inflate(TwoActivity.this,R.layout.item,null);
            //            ImageView imageView = (ImageView)view.findViewById(R.id.iv_my_picture);
            //            imageView.setImageResource(icons[position]);
            //            TextView textView = (TextView)view.findViewById(R.id.tv_my_device_info);
            //            textView.setText(names[position]);
            //            return view;

            ViewHolder holder;
            if(convertView  == null){
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_device_information_item,parent,false);
                holder = new ViewHolder();
                holder.imageView = (ImageView)convertView.findViewById(R.id.iv_my_picture);
                holder.textView = (TextView)convertView.findViewById(R.id.tv_my_device_info);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder)convertView.getTag();
            }
            holder.imageView.setImageResource(icons[position]);
            holder.textView.setText(names[position]);
            return convertView;

        }
        class ViewHolder{
            ImageView imageView;
            TextView textView;
        }
    }


}

package com.dell.raintime.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dell.raintime.R;
import com.dell.raintime.activity.RecyclerBannerActivity;
import com.dell.raintime.activity.RecyclerBannerViewActivity;

import java.util.ArrayList;
import java.util.List;

//import static com.baidu.location.d.j.R;

/**
 * 搜索好友页面
 *
 * @author itlanbao.com
 */
public class ShopFragment extends MyFragment  {

    private View view;

//    private BannerViewPager bannerViewPager;
//    private ViewPagerAdapter mAdapter;

    RecyclerBannerViewActivity pager;
    private List<RecyclerBannerViewActivity.BannerEntity> urls = new ArrayList<>();


    private boolean isRefresh = true;// 获取数据成功还是失败 为后面执行刷新还是加载 成功或者失败


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_shop, container, false);

        // 缓存的view需要判断是否已经被加过parent，
        // 如果有parent需要从parent删除，要不然会发生这个view已经有parent的错误。
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }

        //使用recycleview
        pager = (RecyclerBannerViewActivity) view.findViewById(R.id.r);
//        pager.setOnPagerClickListener(new RecyclerBannerViewActivity.OnPagerClickListener() {
//            @Override
//            public void onClick(RecyclerBannerViewActivity.BannerEntity entity) {
//                Toast.makeText(ShopFragment.this, entity.getUrl(), Toast.LENGTH_SHORT).show();
//            }
//        });
        urls.add(new Entity("http://pic.58pic.com/58pic/12/46/13/03B58PICXxE.jpg"));
        urls.add(new Entity("http://www.jitu5.com/uploads/allimg/121120/260529-121120232T546.jpg"));
        urls.add(new Entity("http://pic34.nipic.com/20131025/2531170_132447503000_2.jpg"));
        urls.add(new Entity("http://img5.imgtn.bdimg.com/it/u=3462610901,3870573928&fm=206&gp=0.jpg"));
        pager.setDatas(urls);

        //使用viewpager
//        bannerViewPager = (BannerViewPager) view.findViewById(R.id.banner);
//        //btn = (Button) findViewById(btn);
//
//        ImageView iv1 = (ImageView) LayoutInflater.from(view.getContext()).inflate(R.layout.fragment_shop_item,bannerViewPager,false);
//        ImageView iv2 = (ImageView) LayoutInflater.from(view.getContext()).inflate(R.layout.fragment_shop_item,bannerViewPager,false);
//        ImageView iv3 = (ImageView) LayoutInflater.from(view.getContext()).inflate(R.layout.fragment_shop_item,bannerViewPager,false);
//        ImageView iv4 = (ImageView) LayoutInflater.from(view.getContext()).inflate(R.layout.fragment_shop_item,bannerViewPager,false);
//        ImageView iv5 = (ImageView) LayoutInflater.from(view.getContext()).inflate(R.layout.fragment_shop_item,bannerViewPager,false);
//        final ImageView iv6 = (ImageView) LayoutInflater.from(view.getContext()).inflate(R.layout.fragment_shop_item,bannerViewPager,false);
//
//        iv1.setImageResource(R.mipmap.ic_img01);
//        iv2.setImageResource(R.mipmap.ic_img02);
//        iv3.setImageResource(R.mipmap.ic_img03);
//        iv4.setImageResource(R.mipmap.ic_img04);
//        iv5.setImageResource(R.mipmap.ic_img05);
//        iv6.setImageResource(R.mipmap.ic_img06);
//        //一开始只添加5个Item
//        final List<ImageView> mViews = new ArrayList<ImageView>();
//        mViews.add(iv1);
//        mViews.add(iv2);
//        mViews.add(iv3);
//        mViews.add(iv4);
//        mViews.add(iv5);
//
//        mAdapter = new ViewPagerAdapter(mViews, new OnPageClickListener() {
//            @Override
//            public void onPageClick(View view, int position) {
//                Log.d("cylog","position:"+position);
//            }
//        });
//        bannerViewPager.setAdapter(mAdapter);

        return view;
    }


    @Override
    protected void onVisible(boolean isInit) {

        if(isInit){
            initView();//初始化控件
        }
    }

    /**
     * 适配器填充listView数据
     */
    private void initView() {


    }

    //使用recycleview
    private class Entity implements RecyclerBannerViewActivity.BannerEntity {

        String url;

        public Entity(String url) {
            this.url = url;
        }

        @Override
        public String getUrl() {
            return url;
        }
    }
}

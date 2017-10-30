package com.dell.raintime.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dell.raintime.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerBannerActivity extends AppCompatActivity {

    RecyclerBannerViewActivity pager;
    private List<RecyclerBannerViewActivity.BannerEntity> urls = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerbanner);
        pager = (RecyclerBannerViewActivity) findViewById(R.id.r);
        pager.setOnPagerClickListener(new RecyclerBannerViewActivity.OnPagerClickListener() {
            @Override
            public void onClick(RecyclerBannerViewActivity.BannerEntity entity) {
                Toast.makeText(RecyclerBannerActivity.this, entity.getUrl(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    int i;

    public void update(View v) {
        i++;
        urls.clear();
        if (i % 2 == 0) {
            urls.add(new Entity("http://pic.58pic.com/58pic/12/46/13/03B58PICXxE.jpg"));
            urls.add(new Entity("http://www.jitu5.com/uploads/allimg/121120/260529-121120232T546.jpg"));
            urls.add(new Entity("http://pic34.nipic.com/20131025/2531170_132447503000_2.jpg"));
            urls.add(new Entity("http://img5.imgtn.bdimg.com/it/u=3462610901,3870573928&fm=206&gp=0.jpg"));
        } else {
            urls.add(new Entity("http://img0.imgtn.bdimg.com/it/u=726278301,2143262223&fm=11&gp=0.jpg"));
            urls.add(new Entity("http://pic51.nipic.com/file/20141023/2531170_115622554000_2.jpg"));
            urls.add(new Entity("http://img3.imgtn.bdimg.com/it/u=2968209827,470106340&fm=21&gp=0.jpg"));
        }
        long t = System.currentTimeMillis();
        pager.setDatas(urls);
        Log.w("---", System.currentTimeMillis() - t + "");
    }


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
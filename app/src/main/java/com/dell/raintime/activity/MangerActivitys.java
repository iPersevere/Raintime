package com.dell.raintime.activity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author itlanbao
 * @Description 管理activity，把activity增加到集閤中，以便退出時候全部銷毁
 */
public class MangerActivitys {
    public static List<Object> activitys = new ArrayList<Object>();

    public static void addActivity(Object object) // 添加新創建的activity
    {
        activitys.add(object);
    }

}

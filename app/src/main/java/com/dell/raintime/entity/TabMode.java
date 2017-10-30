package com.dell.raintime.entity;

import com.dell.raintime.fragment.MyFragment;

/**
 * Created by Administrator on 2015/9/30 0030.
 */

public class TabMode {
    // 设置tab id
    private int tabId;
    // 设置icon
    private int iconResId = -1;
    // 设置tab 名称
    private String tabText = null;
    // 设置字体颜色
    private int textColorResId = -1;
    // 设置字号单位dip
    private int textSize = -1;
    // 设置选中背景效果
    private int tabSelectorResId = -1;
    // tab内容Fragment
    private MyFragment tabContent = null;
    // 是否为默认显示
    private boolean isDefaultShow = false;
    // 是否显示Fragment页面，如“消息”页，就不能显示Fragment，它只响应点击消息tab事件，并不切换view。因为它是弹出界面
    private boolean isShowFragment = true;
    // 是否设置中间tab按钮
    private boolean isSetMidButton = false;

    public TabMode(int tabId, int iconResId, String tabText, int textColorResId, MyFragment tabContent) {
        this.tabId = tabId;
        this.iconResId = iconResId;
        this.tabText = tabText;
        this.textColorResId = textColorResId;
        this.tabContent = tabContent;
    }

    public TabMode(int tabId, int iconResId, String tabText, int textColorResId, MyFragment tabContent, boolean isDefaultShow) {
        this.tabId = tabId;
        this.iconResId = iconResId;
        this.tabText = tabText;
        this.textColorResId = textColorResId;
        this.tabContent = tabContent;
        this.isDefaultShow = isDefaultShow;
    }

    public TabMode(int tabId, int iconResId, String tabText, int textColorResId, int textSize, MyFragment tabContent, boolean isDefaultShow) {
        this.tabId = tabId;
        this.iconResId = iconResId;
        this.tabText = tabText;
        this.textColorResId = textColorResId;
        this.textSize = textSize;
        this.tabContent = tabContent;
        this.isDefaultShow = isDefaultShow;
    }

    public TabMode(int tabId, int iconResId, String tabText, int textColorResId, int textSize, int tabSelectorResId, MyFragment tabContent, boolean isDefaultShow) {
        this.tabId = tabId;
        this.iconResId = iconResId;
        this.tabText = tabText;
        this.textColorResId = textColorResId;
        this.textSize = textSize;
        this.tabSelectorResId = tabSelectorResId;
        this.tabContent = tabContent;
        this.isDefaultShow = isDefaultShow;
    }

    public int getTabId() {
        return tabId;
    }

    public void setTabId(int tabId) {
        this.tabId = tabId;
    }

    public String getTabText() {
        return tabText;
    }

    public void setTabText(String tabText) {
        this.tabText = tabText;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    public int getTextColorResId() {
        return textColorResId;
    }

    public void setTextColorResId(int textColorResId) {
        this.textColorResId = textColorResId;
    }

    public int getTabSelectorResId() {
        return tabSelectorResId;
    }

    public void setTabSelectorResId(int tabSelectorResId) {
        this.tabSelectorResId = tabSelectorResId;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public boolean isDefaultShow() {
        return isDefaultShow;
    }

    public void setDefaultShow(boolean isDefaultShow) {
        this.isDefaultShow = isDefaultShow;
    }

    public MyFragment getTabContent() {
        return tabContent;
    }

    public void setTabContent(MyFragment tabContent) {
        this.tabContent = tabContent;
    }

    public boolean isShowFragment() {
        return isShowFragment;
    }

    public void setShowFragment(boolean isShowFragment) {
        this.isShowFragment = isShowFragment;
    }

    public boolean isSetMidButton() {
        return isSetMidButton;
    }

    public void setSetMidButton(boolean isSetMidButton) {
        this.isSetMidButton = isSetMidButton;
    }

}
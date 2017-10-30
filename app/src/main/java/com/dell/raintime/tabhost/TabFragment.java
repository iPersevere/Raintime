package com.dell.raintime.tabhost;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.Toast;

import com.dell.raintime.R;
import com.dell.raintime.activity.BaseActivity;
import com.dell.raintime.application.Constant;
import com.dell.raintime.entity.BadgeView;
import com.dell.raintime.entity.TabMode;
import com.dell.raintime.fragment.MyFragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 自定义TAB控件
 *
 * @author itlanbao.com
 *
 */
public class TabFragment extends Fragment implements MyRadioGroup.OnCheckedChangeListener
{
    private ArrayList<TabMode> mListTabModes = null;
	// 保存显示消息数的tab BadgeView对象
	private HashMap<Integer, BadgeView> mMsgNumBadgeMap = null;
	// 当前Tab页面索引
	private int currentTabIndex = -1;
	private IFocusChangeListener mListener;
	private MyRadioGroup mRadioGroup = null;

	private int lastTabId = -1;
	private CustomViewPager mViewPager;

	// 点击数
    private static final int MAX_DOUBLE_CLICK_COUNT = 1;
    private int onClickCount = 0;
    private boolean isDoubleClick = false;
    private ITabOnDoubleClickListener onDoubleClickListener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		return inflater.inflate(R.layout.tab_layout, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mTabHost = (TabHost) getView().findViewById(android.R.id.tabhost);
        mTabHost.setup();

        mViewPager = (CustomViewPager) getView().findViewById(R.id.tab_content_pager);
        // 禁止左右滑动
        mViewPager.setScanScroll(false);

        {// 设置滑动切换动画
//            final int sdk = android.os.Build.VERSION.SDK_INT;
//            if(sdk >= android.os.Build.VERSION_CODES.HONEYCOMB) {
//                mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
//                mViewPager.setPageTransformer(true, new DepthPageTransformer());
//            }
        }

		mRadioGroup = (MyRadioGroup) getView().findViewById(R.id.tabs_rg);
		mRadioGroup.setOnCheckedChangeListener(this);

		if (savedInstanceState != null && mTabHost != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString(Constant.KEY_TAB));
        }
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Constant.KEY_TAB, mTabHost.getCurrentTabTag());
    }

    /**
	 * 获取底部Tab高度
	 *
	 * @return
	 */
	public int getTabLayoutHeight() {
		int height = 0;
		if (mRadioGroup != null) {
			height = mRadioGroup.getHeight();
		}

		if (height == 0) {
			height = (int) getResources().getDimension(R.dimen.tab_layout_default_height);
		}

		return height;
	}

	private void showToast(String message){
		Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();;
	}

	private TabHost mTabHost;
	private TabsAdapter mTabsAdapter;

	/**
	 * 初始化Tab
	 *
	 * @param listTabModes
	 */
	public void creatTab(BaseActivity activity, ArrayList<TabMode> listTabModes, IFocusChangeListener listener) {
		if (listTabModes != null) {
			if (mTabsAdapter == null) {
				mTabsAdapter = new TabsAdapter(activity, mTabHost, mViewPager);
			}

			final int size = listTabModes.size();

			if (size == 0) {
				showToast("没有设置Tab标签内容");
                return;
			} else if (size > 5) {
				showToast("tab标签不能超过5项");
                return;
            }

			mViewPager.setOffscreenPageLimit(5);//添加此处可以优化切换速度，但会占用内存

			mListTabModes = listTabModes;

			mListener = listener;

			for (int i = 0; i < size; i ++) {
				final TabMode tabMode = listTabModes.get(i);
				if (tabMode == null) {
					return;
				}

				final String radioButtonIdName = "tab_rb_layout" + i;
				// 根据名称获取资源id
				int radioButtonId = getResources().getIdentifier(radioButtonIdName, "id", activity.getPackageName());
				if (radioButtonId <= 0) {
					// 容错处理，以防某些手机不能通过getIdentifier查找到id
					if (i == 0) {
						radioButtonId = R.id.tab_rb_layout_0;
					} else if (i == 1) {
						radioButtonId = R.id.tab_rb_layout_1;
					} else if (i == 2) {
						radioButtonId = R.id.tab_rb_layout_2;
					} else if (i == 3) {
						radioButtonId = R.id.tab_rb_layout_3;
					} else if (i == 4) {
						radioButtonId = R.id.tab_rb_layout_4;
					}
				}

				final View btnView = getView().findViewById(radioButtonId);
				if (btnView instanceof ViewStub) {
					final ViewStub stub = (ViewStub) btnView;
		            stub.inflate();
		            final RadioButton radioButton = (RadioButton) getView().findViewById(R.id.tab_rb);
		            // 保存当前tab index和数据源
		            radioButton.setTag(R.id.tab_index, i);
		            radioButton.setTag(R.id.tab_mode_obj, tabMode);
					{// 必设项
						// id
						radioButton.setId(tabMode.getTabId());
						// Text
						radioButton.setText(tabMode.getTabText());
						// icon
						radioButton.setCompoundDrawablesWithIntrinsicBounds(0, tabMode.getIconResId(), 0, 0);
						// 字体颜色
						final int textColorResId = tabMode.getTextColorResId();
						try {
							final ColorStateList colors = getResources().getColorStateList(textColorResId);
							if (colors != null) {
								radioButton.setTextColor(colors);
							} else {
								radioButton.setTextColor(getResources().getColor(textColorResId));
							}
						} catch (Exception e) {
							e.printStackTrace();
							radioButton.setTextColor(getResources().getColor(textColorResId));
						}

					}

					// 处理连续点击功能
					radioButton.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if (onDoubleClickListener != null) {
                                final int id = v.getId();
                                if (id == getCurrentTabId()) {
                                    if (onClickCount == 0) {
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                isDoubleClick = false;
                                                onClickCount = 0;
                                            }
                                        }, 800);
                                    }

                                    onClickCount ++;

                                    if (onClickCount > MAX_DOUBLE_CLICK_COUNT) {
                                        isDoubleClick = true;
                                    }

                                    if (isDoubleClick) {
                                        onDoubleClickListener.onDoubleClick(id);
                                        onClickCount = 0;
                                        isDoubleClick = false;
                                    }

                                }
                            }
                        }
                    });

					{// 可填项
						// 设置字号，单位dip
						final int textSize = tabMode.getTextSize();
						if (textSize > -1) {
							final CharSequence text = radioButton.getText();
							final SpannableStringBuilder style = new SpannableStringBuilder(text);
							style.setSpan(new AbsoluteSizeSpan(textSize, true), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
							radioButton.setText(style);
						}
						// Background
						final int tabSelectorResId = tabMode.getTabSelectorResId();
						if (tabSelectorResId > -1) {
							radioButton.setBackgroundResource(tabSelectorResId);
						}
					}

					if (!tabMode.isSetMidButton()) {
						mTabsAdapter.addTab(mTabHost.newTabSpec(tabMode.getTabText()).setIndicator(tabMode.getTabText()),
								tabMode.getTabContent().getClass(), null);

						stub.setVisibility(View.VISIBLE);

						// 默认显示Tab
						if (tabMode.isDefaultShow()) {
							radioButton.setChecked(true);
							mTabHost.setCurrentTabByTag(tabMode.getTabText());
						}
					} else {
						stub.setVisibility(View.INVISIBLE);
						final Button middleButton = (Button) getView().findViewById(R.id.tab_middle_button);
						middleButton.setTag(R.id.tab_index, i);
						middleButton.setTag(R.id.tab_mode_obj, tabMode);
						middleButton.setVisibility(View.VISIBLE);
						middleButton.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								final TabMode mode = (TabMode) v.getTag(R.id.tab_mode_obj);
								if (mode == null) {
									return;
								}
								final int index = (Integer) radioButton.getTag(R.id.tab_index);
								if (mListener != null) {
									mListener.OnFocusChange(mode.getTabId(), index);
								}
							}
						});
					}
				}

			}
		}
	}

	@Override
	public void onCheckedChanged(MyRadioGroup group, int checkedId) {
        if (getView() == null) {
            return;
        }
        onClickCount = 0;
        isDoubleClick = false;
		final View view = getView().findViewById(checkedId);
		if (view instanceof RadioButton) {
			// 保存当前已显示的TAB id
			lastTabId = getCurrentTabId();

			final RadioButton radioButton = (RadioButton) view;
			final TabMode tabMode = (TabMode) radioButton
					.getTag(R.id.tab_mode_obj);
			if (tabMode == null) {
				return;
			}

			mTabHost.setCurrentTabByTag(tabMode.getTabText());

			final int index = (Integer) radioButton.getTag(R.id.tab_index);
			// 更新目标tab为当前tab
	        currentTabIndex = index;

			if (mListener != null) {
				mListener.OnFocusChange(radioButton.getId(), index);
			}
		}
	}

	/**
	 * 设置Tab控件背景
	 *
	 * @param resid
	 */
	public void setTabBackGround(int resid) {
		if (mRadioGroup == null) {
			mRadioGroup = (MyRadioGroup) getView().findViewById(R.id.tabs_rg);
		}
		mRadioGroup.setBackgroundResource(resid);
	}

	/**
	 * 获取当前显示内容
	 * @return
	 */
    public MyFragment getCurrentFragment(){

		if (mListTabModes != null && currentTabIndex != -1) {
	    	final int size = mListTabModes.size();
	    	if (currentTabIndex < size) {
	    		return mListTabModes.get(currentTabIndex).getTabContent();
	    	}
    	}
		return null;
    }

    /**
	 * 获取当前tab id
	 * @return
	 */
    public int getCurrentTabId(){
    	if (mListTabModes != null && currentTabIndex != -1) {
	    	final int size = mListTabModes.size();
	    	if (currentTabIndex < size) {
	    		return mListTabModes.get(currentTabIndex).getTabId();
	    	}
    	}
    	return -1;
    }

    /**
     * 获取上一步Tab id
     * @return
     */
    public int getLastTabId() {
    	return lastTabId;
    }

    /**
     * 显示指定的tab内容
     *
     * @param tabId
     */
    public void setShowTab(int tabId) {
		final View view = getView().findViewById(tabId);
		if (view instanceof RadioButton) {
			final RadioButton radioButton = (RadioButton) view;
			radioButton.setChecked(true);
		}
    }

    /**
     * 显示新消息数
     *
     * @param tabId 指定要显示的Tab id
     * @param msgNum 消息数
     */
    @SuppressLint("UseSparseArrays")
    public void showMessageNum(int tabId, int msgNum) {
    	showMessageNum(tabId, msgNum, 8);
    }

    @SuppressLint("UseSparseArrays")
    public void showNewMsg(int tabId, boolean isShow) {
        try {
            final View view = getView().findViewById(tabId);
            if (view instanceof RadioButton) {

                BadgeView badge = null;
                if (mMsgNumBadgeMap == null) {
                    mMsgNumBadgeMap = new HashMap<Integer, BadgeView>();
                } else {
                    badge = mMsgNumBadgeMap.get(tabId);
                }
                if (badge == null) {
                    final RadioButton radioButton = (RadioButton) view;
                    badge = new BadgeView(getActivity(), radioButton);
                    badge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
                    badge.setBadgeMargin(0);
                    mMsgNumBadgeMap.put(tabId, badge);
                }
                if (isShow) {
                    if (!badge.isShown()) {
                        badge.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.new_tweet_icon, 0, 0);
                        badge.setBackgroundResource(R.color.transparent);
                        badge.show(true);
                    }
                } else {
                    badge.hide();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	@SuppressLint("UseSparseArrays")
    public void showMessageNum(int tabId, int msgNum, int textSize) {
        try {
			final View view = getView().findViewById(tabId);
            if (view instanceof RadioButton) {

                BadgeView badge = null;
                if (mMsgNumBadgeMap == null) {
                    mMsgNumBadgeMap = new HashMap<Integer, BadgeView>();
                } else {
                    badge = mMsgNumBadgeMap.get(tabId);
                }
                if (badge == null) {
                    final RadioButton radioButton = (RadioButton) view;
                    badge = new BadgeView(getActivity(), radioButton);
                    badge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
                    badge.setTextSize(textSize);
                    badge.setBadgeMargin(0);
                    final int color = getResources().getColor(R.color.new_msg_num_text_color);
                    badge.setTextColor(color);
                    mMsgNumBadgeMap.put(tabId, badge);
                }
                if (msgNum > 0) {
                    if (msgNum > 99) {
                        badge.setText("99+");
                        badge.setBackgroundResource(R.drawable.tab_msg_num_big_bg);
                    } else {
                        badge.setText(String.valueOf(msgNum));
                        badge.setBackgroundResource(R.drawable.tab_msg_num_bg);
                    }
                    if (!badge.isShown()) {
                        badge.show(true);
                    	if(getActivity() != null){
                    		final DisplayMetrics metric = getActivity().getApplicationContext().getResources().getDisplayMetrics();
                    		if(metric != null){
                    			if(textSize > 8){
                    				badge.setapplyLayoutParams((int)getResources().getDimension(R.dimen.tab_un_read_margin), 0, 0, 0);
                    			}else {
                    				if(metric.density >= 2 ){
                        				badge.setapplyLayoutParams((int)getResources().getDimension(R.dimen.tab_un_read_margin), 0, 0, 0);
                        			}
                    			}
                        	}
                    	}
                    }
                } else {
                    badge.hide();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 隐藏新消息数
     *
     * @param tabId
     */
    public void hideMessageNum(int tabId) {
    	if (mMsgNumBadgeMap != null) {
    		final BadgeView badgeView = mMsgNumBadgeMap.get(tabId);
    		if (badgeView != null) {
    			badgeView.hide(true);
    		}
    	}
    }

    /**
     * 设置扩展功能View
     *
     * @param extensionView
     */
    public void setTabExtensionView(View extensionView) {
        if (getView() != null) {
            final RelativeLayout tabExtensionLayout = (RelativeLayout) getView().findViewById(R.id.tab_extension_layout);
            if (extensionView != null) {
                tabExtensionLayout.addView(extensionView);
                tabExtensionLayout.setVisibility(View.VISIBLE);
            } else {
                tabExtensionLayout.removeAllViews();
                tabExtensionLayout.setVisibility(View.GONE);
            }
        }
    }

    public interface IFocusChangeListener {
        /**
         * Tab切换监听
         *
         * @param currentTabId
         * @param tabIndex
         */
        public void OnFocusChange(int currentTabId, int tabIndex);
    }

    /**
	 * 替换新的tab
	 *
	 * @param position
	 * @param tabMode
	 */
	public void replaceTab(int position, TabMode tabMode) {
		mTabsAdapter.replaceTab(position, tabMode);

	}

	public String getCurrentTag(int position) {
		String tag = "";
		try {
			RadioButton radioButton = (RadioButton) ((ViewGroup) mRadioGroup
					.getChildAt(position)).getChildAt(0);
			tag = radioButton.getText().toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tag;
	}

    public class TabsAdapter extends FragmentStatePagerAdapter
            implements TabHost.OnTabChangeListener, CustomViewPager.OnPageChangeListener {
        private final Context mContext;
        private final TabHost mTabHost;
        private final CustomViewPager mViewPager;
        private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

        public final class TabInfo {
            private final String tag;
            private final Class<?> clss;
            private final Bundle args;

            public TabInfo(String _tag, Class<?> _class, Bundle _args) {
                tag = _tag;
                clss = _class;
                args = _args;
            }
        }
        public void replaceTab(int position, TabMode tabMode) {
			try {
				final TabInfo info = new TabInfo(tabMode.getTabText(), tabMode
						.getTabContent().getClass(), null);
				mTabs.remove(position);
				mTabs.add(position, info);
				mViewPager.setAdapter(this);
				mViewPager.setCurrentItem(mTabHost.getCurrentTab());
				if (mRadioGroup == null) {
					mRadioGroup = (MyRadioGroup) getView().findViewById(
							R.id.tabs_rg);
				}
				mTabHost.setCurrentTab(mTabHost.getCurrentTab());
				RadioButton radioButton = (RadioButton) ((ViewGroup) mRadioGroup
						.getChildAt(position)).getChildAt(0);
				radioButton.setText(tabMode.getTabText());
				// icon
				radioButton.setCompoundDrawablesWithIntrinsicBounds(0,
						tabMode.getIconResId(), 0, 0);
				notifyDataSetChanged();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

        public class DummyTabFactory implements TabHost.TabContentFactory {
            private final Context mContext;

            public DummyTabFactory(Context context) {
                mContext = context;
            }

            @Override
            public View createTabContent(String tag) {
                final View v = new View(mContext);
                v.setMinimumWidth(0);
                v.setMinimumHeight(0);
                return v;
            }
        }

        public TabsAdapter(FragmentActivity activity, TabHost tabHost, CustomViewPager pager) {
            super(getChildFragmentManager());
            mContext = activity;
            mTabHost = tabHost;
            mViewPager = pager;
            mTabHost.setOnTabChangedListener(this);
            mViewPager.setAdapter(this);
            mViewPager.setOnPageChangeListener(this);
        }

        public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
            tabSpec.setContent(new DummyTabFactory(mContext));
            final String tag = tabSpec.getTag();
            final TabInfo info = new TabInfo(tag, clss, args);
            mTabs.add(info);
            mTabHost.addTab(tabSpec);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mTabs.size();
        }

        @Override
        public Fragment getItem(int position) {
        	final TabInfo info = mTabs.get(position);
            return Fragment.instantiate(mContext, info.clss.getName(), info.args);
        }

        @Override
        public void onTabChanged(String tabId) {
        	final int position = mTabHost.getCurrentTab();
//            mViewPager.setCurrentItem(position);
            mViewPager.setCurrentItem(position, false);// 不显示切换动画
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            final TabWidget widget = mTabHost.getTabWidget();
            final int oldFocusability = widget.getDescendantFocusability();
            widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
            mTabHost.setCurrentTab(position);
            widget.setDescendantFocusability(oldFocusability);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

        public Object instantiateItem(ViewGroup container, int position) {
            // 解决程序切到后台，清理下进程在返回出现异常
            // android.os.BadParcelableException: ClassNotFoundException when unmarshalling: android.support.v4.app.FragmentManagerState
            final Object fragment = super.instantiateItem(container, position);
            try {
                final Field saveFragmentStateField = Fragment.class.getDeclaredField("mSavedFragmentState");
                saveFragmentStateField.setAccessible(true);
                final Bundle savedFragmentState = (Bundle) saveFragmentStateField.get(fragment);
                if (savedFragmentState != null) {
                    savedFragmentState.setClassLoader(Fragment.class.getClassLoader());
                }
            } catch (Exception e) {
//                LogUtils.e("CustomFragmentStatePagerAdapter", "Could not get mSavedFragmentState field: " + e);
            }
            return fragment;
        }
    }

    private class DepthPageTransformer implements CustomViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            if (position < -1) {
                view.setAlpha(0);
            } else if (position <= 0) {
                view.setAlpha(1);
                view.setTranslationX(0);
                view.setScaleX(1);
                view.setScaleY(1);
            } else if (position <= 1) {
                view.setAlpha(1 - position);
                view.setTranslationX(pageWidth * -position);
                float scaleFactor = MIN_SCALE + (1 - MIN_SCALE)
                        * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
            } else {
                view.setAlpha(0);
            }
        }
    }

    private class ZoomOutPageTransformer implements CustomViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.85f;

        private static final float MIN_ALPHA = 0.5f;

        @Override
        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                                    // This page is way off-screen to the left.
                view.setAlpha(0);
            } else if (position <= 1) { // [-1,1]
                                        // Modify the default slide transition to
                                        // shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }
                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE)
                        / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
            } else { // (1,+Infinity]
                        // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }

    /**
     * 设置当前显示tab连续点击监听
     * @param onDoubleClickListener
     */
    public void setTabOnDoubleClickListener(ITabOnDoubleClickListener onDoubleClickListener) {
        this.onDoubleClickListener = onDoubleClickListener;
    }

    public interface ITabOnDoubleClickListener {
        public void onDoubleClick(int tabId);
    }
}

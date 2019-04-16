package com.me.component_base.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.me.component_base.R;
import com.me.component_base.R2;
import com.me.component_base.presenter.PresenterDispatch;
import com.me.component_base.presenter.PresenterProviders;


import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XFragment;


/**
 * @author yang
 */
public abstract class BaseFragment<P extends BasePresenter> extends XFragment {

    @BindView(R2.id.tv_title)
    TextView tvTitle;
    @BindView(R2.id.frameLayout)
    FrameLayout frameLayout;
    @BindView(R2.id.rl_root)
    RelativeLayout rlRoot;
    @BindView(R2.id.rl_header)
    RelativeLayout rlHeader;
    @BindView(R2.id.tv_title_center)
    TextView tvTitleCenter;
    @BindView(R2.id.tv_custom_text)
    TextView tvCustomText;


    protected ImmersionBar immersionBar;
    private PresenterProviders mPresenterProviders;
    private PresenterDispatch mPresenterDispatch;
    protected View mView;
    protected Context mContext;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_base;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initPresenter(savedInstanceState);
        initStatusBar();
        initLayout();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mPresenterDispatch.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenterDispatch.detachView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (immersionBar != null) {
            immersionBar.destroy();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && immersionBar != null) {
            immersionBar.init();
        }
    }

    @Override
    public Object newP() {
        return null;
    }

    private void initLayout() {
        if (frameLayout != null) {
            mView = addLayoutToFrameLayout(getLayoutInflater(), frameLayout);
            init(mView);
        }
    }

    protected void initStatusBar() {
        immersionBar = ImmersionBar.with(this);
        immersionBar.keyboardEnable(true).navigationBarWithKitkatEnable(false).init();
    }

    private void initPresenter(Bundle savedInstanceState) {
        mPresenterProviders = PresenterProviders.inject(this);
        mPresenterDispatch = new PresenterDispatch(mPresenterProviders);
        mPresenterDispatch.attachView(getActivity(), this);
        mPresenterDispatch.onCreatePresenter(savedInstanceState);
    }

    protected P getPresenter() {
        return mPresenterProviders.getPresenter(0);
    }

    public PresenterProviders getPresenterProviders() {
        return mPresenterProviders;
    }

    public TextView getBarTitle() {
        return tvTitle;
    }

    public TextView getBarTitleCenter() {
        return tvTitleCenter;
    }

    public RelativeLayout getRlHeader() {
        return rlHeader;
    }


    public RelativeLayout getRlRoot() {
        return rlRoot;
    }


    public TextView getCustomText() {
        return tvCustomText;
    }

    public void enterActivity(Class clsActivity) {
        Activity activity = getActivity();
        if (activity != null) {
            activity.startActivity(new Intent(getActivity(), clsActivity));
        }
    }

    public void enterActivity(Intent intent) {
        startActivity(intent);
    }

    /**
     * 隐藏键盘
     */
    protected void hideSoftKeyboard(View view) {
        view.clearFocus();
        if (mContext != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 显示键盘
     */
    protected void showSoftKeyboard(View view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.showSoftInput(view, 0);
        }
    }

    /**
     * 初始化
     *
     * @param view view
     */
    protected abstract void init(View view);

    /**
     * 加载布局
     *
     * @param layoutInflater layoutInflater
     * @param frameLayout    frameLayout
     * @return view
     */
    protected abstract View addLayoutToFrameLayout(LayoutInflater layoutInflater, FrameLayout frameLayout);
}

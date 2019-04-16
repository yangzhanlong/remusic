package com.me.component_base.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.me.component_base.R;
import com.me.component_base.R2;
import com.me.component_base.manager.ActivityManager;
import com.me.component_base.presenter.PresenterDispatch;
import com.me.component_base.presenter.PresenterProviders;


import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.mvp.XActivity;


/**
 * @author yang
 */
public abstract class BaseActivity<P extends BasePresenter> extends XActivity {

    @BindView(R2.id.frameLayout)
    FrameLayout frameLayout;
    @BindView(R2.id.iv_return)
    ImageView ivReturn;
    @BindView(R2.id.tv_title)
    TextView tvTitle;
    @BindView(R2.id.tv_custom)
    TextView tvCustom;
    @BindView(R2.id.rl_root)
    RelativeLayout rlRoot;
    @BindView(R2.id.rl_header)
    RelativeLayout rlHeader;

    protected ImmersionBar immersionBar;

    private PresenterProviders mPresenterProviders;
    private PresenterDispatch mPresenterDispatch;

    @Override
    public int getLayoutId() {
        return R.layout.activity_base;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        ActivityManager.getAppManager().addActivity(this);
        initPresenter(savedInstanceState);
        initLayout();
        initStatusBar();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mPresenterDispatch.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenterDispatch != null) {
            mPresenterDispatch.detachView();
        }
        if (immersionBar != null) {
            immersionBar.destroy();
        }
        ActivityManager.getAppManager().removeActivity(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public Object newP() {
        return null;
    }

    protected void initStatusBar() {
        immersionBar = ImmersionBar.with(this);
        immersionBar.statusBarColor(R.color.white).statusBarDarkFont(true).init();
    }

    private void initPresenter(Bundle savedInstanceState) {
        mPresenterProviders = PresenterProviders.inject(this);
        mPresenterDispatch = new PresenterDispatch(mPresenterProviders);
        mPresenterDispatch.attachView(this, this);
        mPresenterDispatch.onCreatePresenter(savedInstanceState);
    }

    private void initLayout() {
        if (frameLayout != null) {
            View view = addLayoutToFrameLayout(getLayoutInflater(), frameLayout);
            init(view);
        }
    }

    @OnClick({R2.id.iv_return})
    public void onViewClicked(View v) {
        int id = v.getId();
        if (id == R.id.iv_return) {
            finish();
            hideSoftKeyboard(v);
        }
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

    public ImageView getReturn() {
        return ivReturn;
    }

    public TextView getCustomView() {
        return tvCustom;
    }

    public RelativeLayout getHeader() {
        return rlHeader;
    }

    public RelativeLayout getRlRoot() {
        return rlRoot;
    }

    protected void enterActivity(Class clsActivity) {
        startActivity(new Intent(this, clsActivity));
    }

    protected void enterActivity(Intent intent) {
        startActivity(intent);
    }


    /**
     * 隐藏键盘
     */
    protected void hideSoftKeyboard(View view) {
        view.clearFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 显示键盘
     */
    protected void showSoftKeyboard(View view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.showSoftInput(view, 0);
        }
    }

    protected void setClickable(int id, boolean clickable) {
        View view = findViewById(id);
        view.setClickable(clickable);
    }


    /**
     * 初始化
     * @param view view
     */
    protected abstract void init(View view);

    /**
     * 加载布局
     * @param layoutInflater layoutInflater
     * @param frameLayout frameLayout
     * @return view
     */
    protected abstract View addLayoutToFrameLayout(LayoutInflater layoutInflater, FrameLayout frameLayout);
}

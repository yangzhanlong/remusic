package cn.droidlover.xdroidmvp.mvp;

import android.os.Bundle;
import android.view.View;

/**
 *
 * @author wanglei
 * @date 2016/12/29
 */

public interface IView<P> {
    /**
     * bindUI
     * @param rootView rootView
     */
    void bindUI(View rootView);

    /**
     * bindEvent
     */
    void bindEvent();

    /**
     * initDara
     * @param savedInstanceState savedInstanceState
     */
    void initData(Bundle savedInstanceState);

    /**
     * getOptionsMenuId
     * @return int
     */
    int getOptionsMenuId();

    /**
     * getLayoutId
     * @return int
     */
    int getLayoutId();

    /**
     * useEventBus
     * @return boolean
     */
    boolean useEventBus();

    /**
     * newP
     * @return P
     */
    P newP();
}

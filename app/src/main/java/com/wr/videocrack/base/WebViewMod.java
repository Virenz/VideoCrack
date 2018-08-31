package com.wr.videocrack.base;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;

public class WebViewMod extends WebView {

    //手指按下时的位置Y,如果手指滑动并没有移动,而是内容的滚动,则这个值实时更新为手指当前的位置Y
    private float startY = 0;

    public WebViewMod(Context context) {
        super(context);
    }

    public WebViewMod(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WebViewMod(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //重写onScrollChanged 方法
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        scrollTo(0,0);
    }

    @Override
    public boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY,
                                int scrollRangeX, int scrollRangeY, int maxOverScrollX,
                                int maxOverScrollY, boolean isTouchEvent) {
        return false;
    }

    /**
     * 使WebView不可滚动
     * */
    @Override
    public void scrollTo(int x, int y){
        super.scrollTo(0,0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        int action = e.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                //手指按下时初始化参数
                startY = e.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //获取此刻手指的Y值,用于计算滑动的距离
                float nowY = e.getY();
                //手指在屏幕上滑动的距离
                int deltaY = (int) (nowY - startY);
                if (deltaY != 0)
                    return true;
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(e);
    }
}

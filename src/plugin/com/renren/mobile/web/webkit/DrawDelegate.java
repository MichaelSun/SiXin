package com.renren.mobile.web.webkit;

import android.graphics.Canvas;

/**
 * at 8:50 PM, 3/31/12
 *
 * @author afpro
 * @see com.renren.mobile.web.webkit.WebViewEx#getDrawDelegate()
 * @see com.renren.mobile.web.webkit.WebViewEx#setDrawDelegate(com.renren.mobile.web.webkit.DrawDelegate)
 * @see android.view.View#invalidate()
 */
public interface DrawDelegate {
    public final static int DRAW_WEBVIEW = 1;
    public final static int DRAW_DELEGATE = 2;

    public final static int[] SEQUENCE_WEBVIEW_ONLY = null;
    public final static int[] SEQUENCE_DELEGATE_ONLY = {DRAW_DELEGATE};
    public final static int[] SEQUENCE_WEBVIEW_AND_DELEGATE = {DRAW_WEBVIEW, DRAW_DELEGATE};

    /**
     * 绘制动作
     * <p/>
     * 比如{@code int[]{DRAW_WEBVIEW, DRAW_DELEGATE}}代表先绘制webview，后绘制delegate
     *
     * @return WebViewEx绘制状态
     * @see #DRAW_WEBVIEW
     * @see #DRAW_DELEGATE
     * @see #SEQUENCE_WEBVIEW_ONLY
     * @see #SEQUENCE_DELEGATE_ONLY
     * @see #SEQUENCE_WEBVIEW_AND_DELEGATE
     */
    int[] drawSequence();

    /**
     * 绘制Delegate
     *
     * @param canvas Canvas原点是可视区域的左上角
     * @param x      WebViewEx左上角相对于屏幕的x坐标
     * @param y      WebViewEx左上角相对于屏幕的y坐标
     * @param w      WebViewEx实际宽度
     * @param h      WebViewEx实际高度
     */
    void draw(Canvas canvas, int x, int y, int w, int h);
}

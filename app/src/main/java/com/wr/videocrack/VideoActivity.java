package com.wr.videocrack;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.Display;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/7/11/011.
 */

public class VideoActivity extends Activity {

    private WebView webView;
    private TextView title;
    private String jiekou;
    private String url;
    private String titlecontent;
    private Boolean touchCancel = false;

    /** 视频全屏参数 */
    protected static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    private View customView;
    private FrameLayout fullscreenContainer;
    private WebChromeClient.CustomViewCallback customViewCallback;

    /** 屏幕调节亮度和音量 */
    private AudioManager mAudioManager;
    /** 最大声音 */
    private int mMaxVolume;
    /** 当前声音 */
    private int mVolume = -1;
    /** 当前亮度 */
    private float mBrightness = -1f;
    private GestureDetector mGestureDetector;
    private VerticalProgressBar vpb_left,vpb_right;
    private int leftProgress = 0,rightProgress = 0;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制横屏

        jiekou = getIntent().getStringExtra("VIDEO_JIEKOU");
        url = getIntent().getStringExtra("VIDEO_URL");//传进来视频链接
        titlecontent = getIntent().getStringExtra("VIDEO_TITLE");
        setContentView(R.layout.videoview);

        webView = new WebView(getApplicationContext());
        FrameLayout view =  (FrameLayout) findViewById(R.id.videowebview);
        view.addView(webView);
        initWebView();

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mGestureDetector = new GestureDetector(this, new MyGestureListener());
        vpb_left = (VerticalProgressBar)findViewById(R.id.vpb_left);
        vpb_right = (VerticalProgressBar)findViewById(R.id.vpb_right);

        title = (TextView) findViewById(R.id.titlecontent);
        title.setText(titlecontent);
        title.setVisibility(View.GONE);

    }

    @Override
    protected void onStop() {
        super.onStop();
        webView.onPause();
        webView.pauseTimers();
        //webView.reload();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        webView.resumeTimers();
        webView.onResume();
    }

    @Override
    protected  void onPause() {
        super.onPause();
        webView.onPause();
        webView.pauseTimers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.resumeTimers();
        webView.onResume();
    }

    /** 展示网页界面 **/
    public void initWebView() {
        WebChromeClient wvcc = new WebChromeClient();
        webView.setHorizontalScrollBarEnabled(false);//水平不显示
        webView.setVerticalScrollBarEnabled(false); //垂直不显示
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        // 让JavaScript可以自动打开windows
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        webSettings.setUseWideViewPort(true); // 关键点
        webSettings.setAllowFileAccess(false); // 允许访问文件
        webSettings.setSupportZoom(true); // 支持缩放
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 不加载缓存内容
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        webView.setWebChromeClient(wvcc);
        WebViewClient wvc = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url == null) return false;
                try {
                    if(url.startsWith("http://") || url.startsWith("https://")
                        //其他自定义的scheme
                            ) {
                        webView.loadUrl(url);
                        return true;
                    }
                } catch (Exception e) { //防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                    return false;
                }
                return true;
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                /*String fun="javascript:function hideAd() { var aEle=document.getElementsByTagName('img'); var i=0; for(i<0;i<aEle.length;i++) { aEle[i].style.display='none';} }";
                view.loadUrl(fun);
                view.loadUrl("javascript:hideAd();");*/
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String urllink) {

                String url = urllink.toLowerCase();
                if((url.endsWith(".png")||url.endsWith(".gif")||url.endsWith(".jpg")||url.endsWith(".jpeg"))&&
                        (!url.contains("play.png"))&&(!url.contains("load.gif"))&&(!url.contains("close"))){
                    System.out.println(url);
                    try {
                        InputStream localCopy = getResources().openRawResource(+R.drawable.line2);
                        return new WebResourceResponse("image/*", "UTF-8", localCopy);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return super.shouldInterceptRequest(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

        };
        webView.setWebViewClient(wvc);

        webView.setWebChromeClient(new WebChromeClient() {
           /*** 视频播放相关的方法 **/

           @Override
           public View getVideoLoadingProgressView() {
               FrameLayout frameLayout = new FrameLayout(VideoActivity.this);
               frameLayout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
               return frameLayout;
           }

           @Override
           public void onShowCustomView(View view, CustomViewCallback callback) {
               //showCustomView(view, callback);
               //播放时横屏幕，如果需要改变横竖屏，只需该参数就行//
               setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
           }
           @Override
           public void onHideCustomView(){
               hideCustomView();
               setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//不播放时竖屏
           }
            //网络请求部分
            @Override
            public void onProgressChanged(WebView view, int newProgress) {

                view.loadUrl("javascript:function setTop(){document.querySelector('.ad-footer').style.display=\"none\";}setTop();");

                super.onProgressChanged(view, newProgress);
            }
        });

        if (url.contains(JieXiInterface.HOST)){
            new PostTask().execute(url);
        } else {
            webView.loadUrl(jiekou+url);
        }
    }

    public String getIFrameUrl(String url){
        String htmlcontent = XmlUtils.getInfo(url);
        System.out.println("Video" + htmlcontent);
        if(htmlcontent == null) return null;

        Pattern pattern = Pattern.compile("<iframe scrolling=\"no\" width=\"100%\" height=\"100%\" frameborder=0 src=\"([a-zA-z]+://[^\\s]*)\" allowfullscreen></iframe>");
        Matcher m = pattern.matcher(htmlcontent);
        if(m.find()) {
            return m.group(1);
        }
        else
            return null;
    }

    /** 视频播放全屏 **/
    private void showCustomView(View view, WebChromeClient.CustomViewCallback callback) {
        // if a view already exists then immediately terminate the new one
        if (customView != null) {
            callback.onCustomViewHidden();
            return;
        }

        VideoActivity.this.getWindow().getDecorView();

        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
        fullscreenContainer = new FullscreenHolder(this);
        fullscreenContainer.addView(view, COVER_SCREEN_PARAMS);
        decor.addView(fullscreenContainer, COVER_SCREEN_PARAMS);
        customView = view;
        setStatusBarVisibility(false);
        customViewCallback = callback;
    }

    /** 隐藏视频全屏 */
    private void hideCustomView() {
        if (customView == null) {
            return;
        }

        setStatusBarVisibility(true);
        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
        decor.removeView(fullscreenContainer);
        fullscreenContainer = null;
        customView = null;
        customViewCallback.onCustomViewHidden();
        webView.setVisibility(View.VISIBLE);
    }

    /** 全屏容器界面 */
    static class FullscreenHolder extends FrameLayout {

        public FullscreenHolder(Context ctx) {
            super(ctx);
            setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
        }

        @Override
        public boolean onTouchEvent(MotionEvent evt) {
            return true;
        }
    }

    private void setStatusBarVisibility(boolean visible) {
        int flag = visible ? 0 : WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setFlags(flag, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            // 返回键退回
            Intent intent = getIntent();
            Bundle data = new Bundle();
            data.putString("VIDEO_URL", url);
            data.putString("VIDEO_TITLE", titlecontent);
            intent.putExtras(data);
            // 设置该MainActivity结果码，并设置结束之后退回的Activity
            setResult(0, intent);
            finish();
            return true;
        }

        // If it wasn't the Back key or there's no web page history, bubble up
        // to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }

    //销毁Webview
    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.clearCache(true); //清空缓存
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();
            ViewGroup webViewLayout = ((ViewGroup) webView.getParent());
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                if (webViewLayout != null) {
                    webViewLayout.removeView(webView);
                }
                webView.removeAllViews();
                webView.destroy();
            }else {
                webView.removeAllViews();
                webView.destroy();
                if (webViewLayout != null) {
                    webViewLayout.removeView(webView);
                }
            }
            webView.setWebChromeClient(null);
            webView.setWebViewClient(null);
            webView = null;
        }
        super.onDestroy();
        System.exit(0);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        if (mGestureDetector.onTouchEvent(event)) {
            return true;
        }

        // 处理手势结束
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                endGesture();
                break;
        }

        return super.dispatchTouchEvent(event);
    }

    /** 手势结束 */
    private void endGesture() {
        mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        // 显示
        rightProgress = (int)(mVolume * 100 / mMaxVolume);
        vpb_right.setProgress(rightProgress);

        // 隐藏
        mDismissHandler.removeMessages(0);
        mDismissHandler.sendEmptyMessageDelayed(0, 500);
    }

    /** 显示标题 */
    private void showTitle() {
        if (!touchCancel) {
            title.setVisibility(View.VISIBLE);
            touchCancel = true;
        }
        else {
            title.setVisibility(View.GONE);
            touchCancel = false;
        }
    }


    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        /** 双击 */
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            showTitle();
            return super.onSingleTapConfirmed(e);
        }

        /** 滑动 */
        @SuppressWarnings("deprecation")
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            float mOldX = e1.getRawX(), mOldY = e1.getRawY();
            int y = (int) e2.getRawY();
            Display disp = getWindowManager().getDefaultDisplay();
            int windowWidth = disp.getWidth();
            int windowHeight = disp.getHeight();

            if (mOldX > windowWidth * 1.0 / 2)// 右边滑动
                onVolumeSlide((mOldY - y) / windowHeight);
            else if (mOldX < windowWidth * 1.0 / 2)// 左边滑动
                onBrightnessSlide(distanceY / windowHeight);
                //onSystemBrightnessSlide(distanceY / windowHeight);

            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }

    /** 定时隐藏 */
    private Handler mDismissHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            vpb_left.setVisibility(View.GONE);
            vpb_right.setVisibility(View.GONE);

        }
    };

    /**
     * 滑动改变声音大小
     *
     * @param percent
     */
    private void onVolumeSlide(float percent) {
        if (mVolume == -1) {
            mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (mVolume < 0)
                mVolume = 0;
            // 显示
            rightProgress = (int)(mVolume * 100 / mMaxVolume);
            vpb_right.setProgress(rightProgress);
        }
        vpb_right.setVisibility(View.VISIBLE);

        int index = (int)(percent * mMaxVolume) + mVolume;
        if (index > mMaxVolume)
            index = mMaxVolume;
        else if (index < 0)
            index = 0;
        // 变更声音
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);

        int progress = rightProgress + (int)(percent * 100);
        if(progress > 100){
            progress = 100;
        }else if(progress < 0){
            progress = 0;
        }
        vpb_right.setProgress(progress);
    }

    /**
     * 滑动改变屏幕亮度
     *
     * @param percent
     */
    public void onBrightnessSlide(float percent) {
        mBrightness = getWindow().getAttributes().screenBrightness;
        if (mBrightness <= 0.00f)
            mBrightness = 0.50f;
        if (mBrightness < 0.01f)
            mBrightness = 0.01f;

        // 显示
        vpb_left.setVisibility(View.VISIBLE);
        leftProgress = (int) (mBrightness * 100);
        vpb_left.setProgress(leftProgress);
        WindowManager.LayoutParams lpa = getWindow().getAttributes();
        lpa.screenBrightness = mBrightness + percent;
        if (lpa.screenBrightness > 1.0f)
            lpa.screenBrightness = 1.0f;
        else if (lpa.screenBrightness < 0.01f)
            lpa.screenBrightness = 0.01f;
        getWindow().setAttributes(lpa);

        leftProgress = leftProgress + (int) (percent * 100);
        if (leftProgress > 100) {
            leftProgress = 100;
        } else if (leftProgress < 0) {
            leftProgress = 0;
        }
        vpb_left.setProgress(leftProgress);
    }

    /**
     * 滑动改变系统亮度
     * @param percent
     */
    public void onSystemBrightnessSlide(float percent){
        try {
            mBrightness = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        if(mBrightness < 0){
            mBrightness = 0;
        }
        // 显示  系统屏幕亮度最大值为255
        vpb_left.setVisibility(View.VISIBLE);
        leftProgress = (int) (mBrightness * 100 / 255);
        vpb_left.setProgress(leftProgress);

        int brightness = (int)(mBrightness + percent * 255);
        if(brightness > 255){
            brightness = 255;
        }else if(brightness < 0){
            brightness = 0;
        }
        Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS,brightness);

        leftProgress = leftProgress + (int) (percent * 100);
        if (leftProgress > 100) {
            leftProgress = 100;
        } else if (leftProgress < 0) {
            leftProgress = 0;
        }
        vpb_left.setProgress(leftProgress);
    }

    //获取系统亮度  Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
    //设置系统亮度  Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS,systemBrightness);
    /** 开启自动亮度调节后，改不了系统亮度(可以改变屏幕亮度)，要先关闭自动亮度调节 ***/
    /** * 停止自动亮度调节 */

    public void stopAutoBrightness(Activity activity) {
        Settings.System.putInt(activity.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
    }
    /**
     * * 开启亮度自动调节 *
     *
     * @param activity
     */

    public void startAutoBrightness(Activity activity) {
        Settings.System.putInt(activity.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
    }


    private class PostTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            return getIFrameUrl(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            webView.loadUrl(s);
        }
    }
}


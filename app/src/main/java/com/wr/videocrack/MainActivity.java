package com.wr.videocrack;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.hjm.bottomtabbar.BottomTabBar;
import com.wr.videocrack.View.fragment.CartoonFragment;
import com.wr.videocrack.View.fragment.HomeFragment;
import com.wr.videocrack.View.fragment.MoveFragment;
import com.wr.videocrack.View.fragment.TVplayFragment;

import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private BottomTabBar mTabBar;

    private FlexiableWebView webView;
    private FrameLayout view;
    private View ckmove;
    private FloatingActionsMenu menuChangeMaptype;//解析菜单按钮
    private FloatingActionButton menuStreetMapBtn;//解析接口1
    private FloatingActionButton menuSatelliteMapBtn;//解析接口2
    private FloatingActionButton menuTopoMapBtn;//解析接口3
    private FloatingActionButton menuJiekou4;//解析接口4

    //记录用户首次点击返回键的时间
    private long firstTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }*/
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);


        menuChangeMaptype = (FloatingActionsMenu)findViewById(R.id.changeMaptypeBtn);
        //获得按钮并设置点击的监听函数
        menuStreetMapBtn = (FloatingActionButton)findViewById(R.id.streetMapBtn);
        menuStreetMapBtn.setOnClickListener(new MapListener());
        menuSatelliteMapBtn = (FloatingActionButton)findViewById(R.id.satelliteMapBtn);
        menuSatelliteMapBtn.setOnClickListener(new MapListener());
        menuTopoMapBtn = (FloatingActionButton)findViewById(R.id.topoMapBtn);
        menuTopoMapBtn.setOnClickListener(new MapListener());
        menuJiekou4 = (FloatingActionButton) findViewById(R.id.jiexi4);
        menuJiekou4.setOnClickListener(new MapListener());


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        webView = new FlexiableWebView(getApplicationContext());
        LayoutInflater inflate = LayoutInflater.from(this);
        ckmove = inflate.inflate(R.layout.activity_home,null);
        view =  (FrameLayout) findViewById(R.id.videowebview);
        view.addView(ckmove);
        mTabBar = (BottomTabBar)findViewById(R.id.tabBar);
        mTabBar.init(getSupportFragmentManager())
                .setImgSize(50, 50)
                .setFontSize(10)
                .setTabPadding(4, 6, 10)
                .setChangeColor(Color.RED, Color.DKGRAY)
                .addTabItem("首页", R.drawable.start, HomeFragment.class)
                .addTabItem("电影", R.drawable.film, MoveFragment.class)
                .addTabItem("电视剧", R.drawable.tv, TVplayFragment.class)
                .addTabItem("动漫", R.drawable.dongman, CartoonFragment.class);
        initCKMove();

        WebSettings webSettings = webView.getSettings();
        // 让WebView能够执行javaScript
        webSettings.setJavaScriptEnabled(true);
        //webView.addJavascriptInterface(new InJavaScriptLocalObj(), "java_obj");
        // 让JavaScript可以自动打开windows
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        // 设置缓存
        webSettings.setAppCacheEnabled(true);
        // 设置缓存模式,一共有四种模式
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        // 设置缓存路径
        // webSettings.setAppCachePath("");
        // 支持缩放(适配到当前屏幕)
        webSettings.setSupportZoom(true);
        // 将图片调整到合适的大小
        webSettings.setUseWideViewPort(true);
        // 支持内容重新布局,一共有四种方式
        // 默认的是NARROW_COLUMNS
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        // 设置可以被显示的屏幕控制
        webSettings.setDisplayZoomControls(true);
        // 设置默认字体大小
        webSettings.setDefaultFontSize(12);
        webSettings.setDomStorageEnabled(true);

        webSettings.setAllowFileAccess(true); // 允许访问文件
        webSettings.setLoadWithOverviewMode(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        //showVideoTotal("http://m.v.qq.com/index.html");
    }

    public void initCKMove() {
        view.removeAllViews();
        view.addView(ckmove);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            showVideoTotal("http://m.iqiyi.com/dianshiju/");
        } else if (id == R.id.nav_gallery) {
            showVideoTotal("http://m.v.qq.com/index.html");
        } else if (id == R.id.nav_slideshow) {
            showVideoTotal("http://tv.youku.com/");
        } else if (id == R.id.nav_manage) {
            showVideoTotal("https://m.tv.sohu.com/");
        } else if (id == R.id.nav_bilibili) {
            showVideoTotal("https://m.bilibili.com/index.html");
        } else if (id == R.id.nav_acfun) {
            showVideoTotal("http://m.acfun.cn/");
        } else if (id == R.id.nav_douyu) {
            showVideoTotal("http://m.xigua567.com/index.html");
        } else if (id == R.id.nav_share) {
            showVideoHistroy();
        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_start) {
            initCKMove();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
        webView.onPause();
        webView.pauseTimers();
    }

    @Override
    public void onResume(){
        super.onResume();
        webView.resumeTimers();
        webView.onResume();
    }

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

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                /** 回退键 事件处理 优先级:视频播放全屏-网页回退-关闭页面 */

                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    long secondTime = System.currentTimeMillis();
                    if (secondTime - firstTime > 2000) {
                        Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                        firstTime = secondTime;
                        return true;
                    } else {
                        finish();
                    }
                }
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }

    private void showVideoTotal(String url){

        /*// 设置WebView的客户端
        webView.setWebViewClient(new WebViewClient(){
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
            public void onPageFinished(WebView view, String url) {
                // 在结束加载网页时会回调

               *//* // 获取页面内容
                view.loadUrl("javascript:window.java_obj.showSource("
                        + "document.getElementsByTagName('html')[0].innerHTML);");*//*

                // 获取解析<meta name="share-description" content="获取到的值">
                *//*view.loadUrl("javascript:window.java_obj.showDescription("
                        + "document.querySelector('meta[name=\"share-description\"]').getAttribute('content')"
                        + ");");*//*
                super.onPageFinished(view, url);
            }
        });
        webView.setWebChromeClient(new WebChromeClient());*/
        view.removeAllViews();
        webView.loadUrl(url);
        view.addView(webView);
    }

    private void showVideoHistroy(){
        List<TableInfo> list = XmlUtils.readxml();
        OptionCenterDialog optionCenterDialog = new OptionCenterDialog();
        optionCenterDialog.show(this, list, webView);
    }

    //选择解析接口监听监听函数
    class MapListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int index = 0;
            switch (v.getId()){
                case R.id.streetMapBtn:
                    //设置解析接口1
                    index = 0;
                    menuChangeMaptype.collapse();
                    break;
                case R.id.satelliteMapBtn:
                    //设置解析接口2
                    index = 1;
                    menuChangeMaptype.collapse();
                    break;
                case R.id.topoMapBtn:
                    //设置解析接口3
                    index = 2;
                    menuChangeMaptype.collapse();
                    break;
                case R.id.jiexi4:
                    //设置解析接口4
                    index = 3;
                    menuChangeMaptype.collapse();
                    break;
            }
            if (!(webView.getUrl() == null && webView.getTitle() == null)) {
                Intent intent = new Intent();

                //用Bundle携带数据
                Bundle bundle = new Bundle();
                //传递name参数
                bundle.putString("VIDEO_JIEKOU", JieXiInterface.jiekou[index]);
                bundle.putString("VIDEO_URL", webView.getUrl());
                bundle.putString("VIDEO_TITLE", webView.getTitle());
                intent.putExtras(bundle);

                List<TableInfo> list = XmlUtils.readxml();
                TableInfo info = new TableInfo();
                info.setTitle(webView.getTitle());
                info.setContent(webView.getUrl());
                list.add(0, info);
                XmlUtils.savexml(list);

                intent.setClass(MainActivity.this, VideoActivity.class);
                startActivityForResult(intent, 0);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == 0) {
            // 取出Intent里的数据
            /*String url = data.getStringExtra("VIDEO_URL");
            webView.loadUrl(url);*/
        }
    }
}

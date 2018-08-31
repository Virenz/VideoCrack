package com.wr.videocrack.View.activity;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.wr.videocrack.R;
import com.wr.videocrack.View.fragment.CartoonFragment;
import com.wr.videocrack.View.fragment.HomeFragment;
import com.wr.videocrack.View.fragment.MoveFragment;
import com.wr.videocrack.View.fragment.TVplayFragment;
import com.hjm.bottomtabbar.BottomTabBar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tq on 2018/8/9.
 */

public class HomeActivity extends AppCompatActivity {
    @BindView(R.id.tabBar)
    BottomTabBar mTabBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        ButterKnife.bind(this);
        mTabBar.init(getSupportFragmentManager())
                .setImgSize(50, 50)
                .setFontSize(10)
                .setTabPadding(4, 6, 10)
                .setChangeColor(Color.RED, Color.DKGRAY)
                .addTabItem("首页", R.drawable.start, HomeFragment.class)
                .addTabItem("电影", R.drawable.film, MoveFragment.class)
                .addTabItem("电视剧", R.drawable.tv, TVplayFragment.class)
                .addTabItem("动漫", R.drawable.dongman, CartoonFragment.class);
    }


}

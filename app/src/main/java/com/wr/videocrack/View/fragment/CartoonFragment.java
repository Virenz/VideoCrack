package com.wr.videocrack.View.fragment;

import android.view.View;

/**
 * Created by tq on 2018/8/9.
 */

public class CartoonFragment extends HomeFragment{

    @Override
    protected String setTitle() {
        return "动漫";
    }
    @Override
    protected void setViibilly() {
        page.setVisibility(View.VISIBLE);
    }

    @Override
    protected void intitView() {
        hostUrl = "http://m.yiybb.com/Dhp/";
        super.intitView();
        mThisPage = 1;
        listTpye = "List_17_";
        mTvThisPage.setVisibility(View.VISIBLE);


    }
}

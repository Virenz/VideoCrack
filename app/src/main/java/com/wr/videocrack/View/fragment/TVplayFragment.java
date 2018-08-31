package com.wr.videocrack.View.fragment;

import android.view.View;

/**
 * Created by tq on 2018/8/9.
 */

public class TVplayFragment extends HomeFragment {

    @Override
    protected String setTitle() {
        return "电视剧";
    }
    @Override
    protected void setViibilly() {
        page.setVisibility(View.VISIBLE);
    }
    @Override
    protected void intitView() {
        hostUrl = "http://m.yiybb.com/dianshiju/";
        super.intitView();
        mThisPage = 1;
        listTpye = "List_16_";
        mTvThisPage.setVisibility(View.VISIBLE);

    }
}

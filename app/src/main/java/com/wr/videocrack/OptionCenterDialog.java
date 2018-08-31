package com.wr.videocrack;

import android.app.AlertDialog;
import android.app.VoiceInteractor;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.List;


public class OptionCenterDialog implements MyAdapter.OnRecyclerViewListener {
    private AlertDialog albumDialog;
    private RecyclerView dialog_lv;
    private SwipeRefreshLayout refreshLayout1;
    private MyAdapter photoDialogAdapter;
    private FlexiableWebView webView;


    public void show(Context context, List<TableInfo> longData, FlexiableWebView webView) {
        this.webView = webView;
        albumDialog = new AlertDialog.Builder(context).create();
        albumDialog.setCanceledOnTouchOutside(true);
        albumDialog.setCancelable(true);
        View v = LayoutInflater.from(context).inflate(
                R.layout.__picker_dialog_photo_pager, null);
        albumDialog.show();
        albumDialog.setContentView(v);
        albumDialog.getWindow().setGravity(Gravity.CENTER);
        albumDialog.getWindow().setBackgroundDrawableResource(R.drawable.__picker_bg_dialog);

        DisplayMetrics dm = new DisplayMetrics();
        //获取屏幕信息
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);

        int screenWidth = dm.widthPixels;
        int screenHeigh = dm.heightPixels;
        WindowManager.LayoutParams params =
                albumDialog.getWindow().getAttributes();//获取dialog信息

        params.width = screenWidth - 100;
        params.height = screenHeigh - 200 ;
        albumDialog.getWindow().setAttributes(params);//设置大小

        refreshLayout1 = (SwipeRefreshLayout)v.findViewById(R.id.swipeRefreshLayout1);
        refreshLayout1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                photoDialogAdapter.notifyDataSetChanged(); // refreshLayout.setRefreshing(false);
                refreshLayout1.setRefreshing(false);
            }
        });

        dialog_lv = (RecyclerView)v.findViewById(R.id.recycler_view_test_rv);
        dialog_lv.setHasFixedSize(true);
        //dialog_lv.addItemDecoration(new RecycleViewDivider(context, LinearLayoutManager.VERTICAL, R.drawable.line1));


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        dialog_lv.setLayoutManager(layoutManager);

        photoDialogAdapter = new MyAdapter(context, longData);
        photoDialogAdapter.setOnRecyclerViewListener(this);
        dialog_lv.setAdapter(photoDialogAdapter);
    }

    // 接口回调第四步, 这是才是最终被调用的方法
    @Override
    public void onItemClick(int position, TableInfo posinfo) {
        albumDialog.dismiss();
        this.webView.loadUrl(posinfo.getContent());
    }

    @Override
    public boolean onItemLongClick(int position) {
        return false;
    }

}

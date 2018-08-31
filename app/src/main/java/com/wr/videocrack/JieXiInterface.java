package com.wr.videocrack;

/**
 * Created by Administrator on 2018/7/13/013.
 */

public class JieXiInterface {
    public static String[] jiekou = {"http://17kyun.com/api.php?url=", "http://jx.52xftv.cn/?url=", "http://api.ledboke.com/vip/?url=", "https://cdn.yangju.vip/k/?url=", "http://api.wlzhan.com/sudu/?url="};
    public static final String GET_FRAME_CONTENT_STR =
            "document.getElementsByTagName('html')[0].innerHTML);"; // document.getElementById('你自己要查找的框架的id').contentWindow.document.body.innerHTML
    public static final String COURSES_DIR_NAME = "videosrc.html";
    public static String HOST = "m.xigua567.com";
    public static String ADHIDE = "http://www.cmmob.cn/";

    // 需要隐藏的dom元素id
    public static final String[] HIDE_DOM_IDS = {"con_latest_1", "nav_menu", "banner", "YKComment", "Bigdrama", "AdsPlace", "vip_header","header","2016_banner","vip_prevue","vip_movie_clips","vip_privilege","vip_activity","2016_comment","2016_mini","2016_recommendation","vip_recommendation","vip_ad","nav_header","ad_banner"};
    public static final String[] HIDE_DOM_CLASSES = {
            "vStars border-bottom",
            "recommend border-bottom",
            "aroundnews",
            "shortVideo border-bottom",
            "x-download-panel x-dl-shake",
            "btn-xz-app",
            "app-vbox ph-vbox",
            "app-vbox app-star-vbox",
            "app-view-box main-view-box main-rec-view-box",
            "app-view-box app-qianfan-box",
            "app-vbox app-guess-vbox",
            "float-app-btn show fade-in",
            "channel-m main",
            "main vod-botx-title"};


    public static String getDomOperationStatements(String[] hideDomIds, String[] hideDomClasses) {
        StringBuilder builder = new StringBuilder();
        // add javascript prefix
        builder.append("javascript:function getClass(parent,sClass){" +
                "var aEle=parent.getElementsByTagName('div');" +
                "var aResult=[];" +
                "var i=0;" +
                "for(i<0;i<aEle.length;i++){" +
                "if(aEle[i].className==sClass){" +
                "aResult.push(aEle[i]);}}" +
                "if(aResult.length != 0){aResult[0].style.display='none';}}" +
                "javascript:function getId(parent,sId){" +
                "var aEle=parent.getElementById(sId);" +
                "if(aEle != null){" +
                "aEle.remove();}}" +
                "function hideOther() {");
        for (String domId : hideDomIds) {
            builder.append("getId(document,'").append(domId).append("');");
        }
        for (String domClass : hideDomClasses) {
            builder.append("getClass(document,'").append(domClass).append("');");
        }
        // add javascript suffix
        builder.append("}");
        return builder.toString();
    }
}

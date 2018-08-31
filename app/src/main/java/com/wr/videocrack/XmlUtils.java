package com.wr.videocrack;

import android.os.Environment;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class XmlUtils {
    /**
     * 向SD卡写入一个XML文件
     */
    public static void savexml(List<TableInfo> list) {

        try {
            //System.out.println(Environment.getExternalStorageDirectory());
            File file = new File("/mnt/sdcard",
                    "histroys.xml");
            FileOutputStream fos = new FileOutputStream(file);
            // 获得一个序列化工具
            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(fos, "utf-8");
            // 设置文件头
            serializer.startDocument("utf-8", true);
            serializer.startTag(null, "histroys");
            TableInfo info;
            for (int index = 0; index < 20 && index < list.size(); index++) {
                info = list.get(index);

                serializer.startTag(null, "histroy");
                // 写标题
                serializer.startTag(null, "title");
                serializer.text(info.getTitle());
                serializer.endTag(null, "title");
                // 写链接
                serializer.startTag(null, "link");
                serializer.text(info.getContent());
                serializer.endTag(null, "link");
                // 写缩略图
                serializer.startTag(null, "image");
                serializer.text("");
                serializer.endTag(null, "image");

                serializer.endTag(null, "histroy");
            }
            serializer.endTag(null, "histroys");
            serializer.endDocument();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 读取SD卡中的XML文件,使用pull解析
     *
     */
    public static List<TableInfo> readxml() {

        List<TableInfo> infos = new ArrayList<>();
        try {
            File path = new File("/mnt/sdcard",
                    "histroys.xml");
            FileInputStream fis = new FileInputStream(path);

            // 获得pull解析器对象
            XmlPullParser parser = Xml.newPullParser();
            // 指定解析的文件和编码格式
            parser.setInput(fis, "utf-8");

            int eventType = parser.getEventType(); // 获得事件类型

            String title = null;
            String link = null;
            String image = null;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName(); // 获得当前节点的名称

                switch (eventType) {
                    case XmlPullParser.START_TAG: // 当前等于开始节点 <person>
                        if ("histroys".equals(tagName)) { // <persons>
                        } else if ("histroy".equals(tagName)) { // <person id="1">
                        } else if ("title".equals(tagName)) { // <name>
                            title = parser.nextText();
                        } else if ("link".equals(tagName)) { // <age>
                            link = parser.nextText();
                        } else if ("image".equals(tagName)) { // <age>
                            image = parser.nextText();
                        }
                        break;
                    case XmlPullParser.END_TAG: // </persons>
                        if ("histroy".equals(tagName)) {
                            TableInfo info = new TableInfo();
                            info.setTitle(title);
                            info.setContent(link);
                            infos.add(info);
                        }
                        break;
                    default:
                        break;
                }
                eventType = parser.next(); // 获得下一个事件类型
            }
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            return infos;
        }

    }

    public static String getInfo(String urlstr){
        try {
            URL url = new URL(urlstr);// 提交地址
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true);// 打开写入属性
            httpURLConnection.setDoInput(true);// 打开读取属性
            httpURLConnection.setRequestMethod("GET");// 设置提交方法
            httpURLConnection.setConnectTimeout(50000);// 连接超时时间
            httpURLConnection.setReadTimeout(50000);

            httpURLConnection.connect();

            //读取GET之后的返回值
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(httpURLConnection.getInputStream()));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            in.close();
            httpURLConnection.disconnect();//断开连接
            return sb.toString();
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

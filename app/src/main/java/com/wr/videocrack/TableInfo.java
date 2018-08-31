package com.wr.videocrack;

/**
 * Created by wb-weirui on 2016/1/26.
 */
public class TableInfo
{
    private StringBuffer title = new StringBuffer();
    private StringBuffer content = new StringBuffer();

    public TableInfo()
    {

    }

    public TableInfo(String title, String content)
    {
        this.title.append(title);
        this.content.append(content);
    }

    public void setTitle(String title)
    {
        this.title.append(title);
    }

    public void setContent(String content)
    {
        this.content.append(content);
    }

    public String getTitle()
    {
        return this.title.toString();
    }

    public String getContent()
    {
        return this.content.toString();
    }
}

package com.wxq.example.model;


import java.io.IOException;
import java.io.Serializable;
import java.net.URL;

import com.wxq.example.util.URLUtil;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;


/**
 * Created by lvxiaojun on 16/8/3.
 * PDF页眉和页脚属性
 */
public class PdfHeader implements Serializable {

    private ResourceLoader resourceLoader = new DefaultResourceLoader();

    public static final String DEFAULT_SPLIT_LINE_PATH = "classpath:static/";

    /*默认的下划线图片文件名*/
    public static final String DEFAULT_SPLIT_LINE_NAME = "line.png";
    /*默认logo*/    
    public static final String DEFAULT_LOGO_NAME = "logo.png";

    /**
     * 页眉左边图片
     */
    private URL imgLeft;

    /**
     * 页眉右边文字
     */
    private String textRight;
    
    /**
     * 页眉左边文字
     */
    private String textLeft;

    /**
     * 页码起始页数
     */
    private int pageNoStart;

    /**
     * 分割线
     */
    private URL splitLine;

    public URL getImgLeft() {
        return imgLeft;
    }

    public void setImgLeft(URL imgLeft) {
        this.imgLeft = imgLeft;
    }

    public String getTextRight() {
        return textRight;
    }

    public void setTextRight(String textRight) {
        this.textRight = textRight;
    }

    public int getPageNoStart() {
        return pageNoStart;
    }

    public void setPageNoStart(int pageNoStart) {
        this.pageNoStart = pageNoStart;
    }

    public URL getSplitLine() {
        return splitLine;
    }

    public void setSplitLine(URL splitLine) {
        this.splitLine = splitLine;
    }

    public String getTextLeft() {
		return textLeft;
	}

	public void setTextLeft(String textLeft) {
		this.textLeft = textLeft;
	}

	public PdfHeader(URL imgLeft, String textRight, String textLeft, int pageNoStart) {
        this.imgLeft = imgLeft;
        try {
            this.splitLine = resourceLoader.getResource(DEFAULT_SPLIT_LINE_PATH + DEFAULT_SPLIT_LINE_NAME).getURL();
        } catch (IOException ignored) {
        }
        this.textRight = textRight;
        this.textLeft = textLeft;
        this.pageNoStart = pageNoStart;
    }

    public PdfHeader(String imgLeft, String splitLine, String textRight, String textLeft, int pageNoStart) {
        try {
            this.imgLeft = URLUtil.toURL(imgLeft);
        } catch (IOException | NullPointerException ignored) {
        }
        try {
            this.splitLine = resourceLoader.getResource(DEFAULT_SPLIT_LINE_PATH + splitLine).getURL();
        } catch (IOException ignored) {
        }
        this.textRight = textRight;
        this.textLeft = textLeft;
        this.pageNoStart = pageNoStart;
    }

    public PdfHeader(String imgLeft, String textRight, String textLeft, int pageNoStart) {
        try {
            this.imgLeft = URLUtil.toURL(imgLeft);
        } catch (IOException | NullPointerException ignored) {
        }
        try {
            this.splitLine = resourceLoader.getResource(DEFAULT_SPLIT_LINE_PATH + DEFAULT_SPLIT_LINE_NAME).getURL();
        } catch (IOException ignored) {
        }
        this.textRight = textRight;
        this.textLeft = textLeft;
        this.pageNoStart = pageNoStart;
    }
    
    public PdfHeader(String textRight, String textLeft, int pageNoStart) {
        try {
            this.imgLeft = resourceLoader.getResource(DEFAULT_SPLIT_LINE_PATH + DEFAULT_LOGO_NAME).getURL();
        } catch (IOException | NullPointerException ignored) {
        }
        try {
            this.splitLine = resourceLoader.getResource(DEFAULT_SPLIT_LINE_PATH + DEFAULT_SPLIT_LINE_NAME).getURL();
        } catch (IOException ignored) {
        }
        this.textRight = textRight;
        this.textLeft = textLeft;
        this.pageNoStart = pageNoStart;
    }

}

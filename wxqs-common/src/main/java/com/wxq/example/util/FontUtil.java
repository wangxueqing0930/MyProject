package com.wxq.example.util;

import java.io.IOException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;

public class FontUtil {

    public static Font getChineseFont() throws DocumentException, IOException {
        BaseFont baseFont = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", false);
        return new Font(baseFont, 12, Font.NORMAL, BaseColor.BLACK);
    }

    public static Font getCourier() throws DocumentException, IOException {
        BaseFont baseFont = BaseFont.createFont(BaseFont.COURIER, BaseFont.WINANSI, false);
        return new Font(baseFont, 12, Font.BOLD, BaseColor.BLACK);
    }

    public static Font getVersion() throws IOException, DocumentException {
        return new Font(getSimsun(), 8, Font.NORMAL, BaseColor.BLACK);
    }

    public static BaseFont getWaterFont() throws DocumentException, IOException {
        return BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
    }

    public static BaseFont getWaterChFont() throws DocumentException, IOException {
        return BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", false);
    }

    public static BaseFont getSimsun() throws DocumentException, IOException {
    	String os = System.getProperty("os.name");  
    	if(os.toLowerCase().startsWith("win")){
    		 return BaseFont.createFont("C:\\Windows\\Fonts\\msyh.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
    	}else{
    		return BaseFont.createFont("/usr/share/fonts/msyh.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
    	}
        
    }

}

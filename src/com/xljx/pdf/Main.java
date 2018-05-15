package com.xljx.pdf;

import com.itextpdf.awt.geom.Rectangle2D;
import com.itextpdf.awt.geom.RectangularShape;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;
import org.apache.pdfbox.text.TextPosition;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    private static final String FILE_PATH = "D:/test/关于什么的决定.pdf";

    public static void main(String[] args) {

        try {
            PdfReader pdfReader = new PdfReader(FILE_PATH);
            pdfReader.isEncrypted();
            pdfReader.isAppendable();
            pdfReader.getPermissions();

        } catch (IOException e) {
            e.printStackTrace();
        }

        // 查询关键词组
        List<String> keywordList = new ArrayList<>();
        keywordList.add("签字");
        keywordList.add("画押");
        keywordList.add("签名");
        keywordList.add("盖章");
        keywordList.add("印章");
        keywordList.add("公章");
        keywordList.add("印戳");
        String[] keywords = new String[keywordList.size()];
        keywordList.toArray(keywords);

        List<Map.Entry<String, Rectangle2D.Float>> entries = PdfUtil.searchPosition(FILE_PATH, keywords);
        if(entries == null){
            System.out.println("未搜索到关键字！");
        }else{
            System.out.println("查找到 " + entries.size() + " 个关键字");
            float x = entries.get(0).getValue().x;
            float y = entries.get(0).getValue().y;
            PdfUtil.addWaterMaker(FILE_PATH, x, y, "output.pdf");
        }
    }



}

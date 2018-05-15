package com.xljx.pdf;

import com.itextpdf.awt.geom.Rectangle2D;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.sun.prism.impl.BaseContext;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PdfUtil {

    private  PdfUtil(){}

    public static List<Map.Entry<String, Rectangle2D.Float>> searchPosition(String pdfPath, String[] keywordList) {
        List<String> list = new ArrayList<>();
        if(keywordList == null || keywordList.length <= 0){
            return null;
        }
        for(int index = 0; index < keywordList.length; index++){
            list.add(keywordList[index]);
        }
        return searchPosition(pdfPath, list);
    }


    /**
     * 查询关键字坐标
     *
     * @param pdfPath
     * @param keywordList
     */
    public static List<Map.Entry<String, Rectangle2D.Float>> searchPosition(String pdfPath, List<String> keywordList) {
        if(pdfPath == null || keywordList == null || keywordList.size() <= 0){
            return null;
        }
        List<Map.Entry<String, Rectangle2D.Float>> searchResult = new ArrayList<>();
        try {
            PdfReader reader = new PdfReader(pdfPath);
            //新建一个PDF解析对象
            PdfReaderContentParser parser = new PdfReaderContentParser(reader);
            //包含了PDF页面的信息，作为处理的对象
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream("d:/test.pdf"));
            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                //新建一个ImageRenderListener对象，该对象实现了RenderListener接口，作为处理PDF的主要类
                TestRenderListener listener = new TestRenderListener();
                //解析PDF，并处理里面的文字
                parser.processContent(i, listener);
                //获取文字的矩形边框
                List<Rectangle2D.Float> rectText = listener.rectText;
                List<String> textList = listener.textList;
                List<Float> listY = listener.listY;
                List<Map<String, Rectangle2D.Float>> list_text = listener.rows_text_rect;
                for (int k = 0; k < list_text.size(); k++) {
                    Map<String, Rectangle2D.Float> map = list_text.get(k);
                    for (Map.Entry<String, Rectangle2D.Float> entry : map.entrySet()) {
                        System.out.println(entry.getKey() + "---" + entry.getValue());
                        if(keywordList.contains(entry.getKey())){
                            searchResult.add(entry);
                        }
                    }
                }
            }

            // todo:
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }finally {
            return searchResult;
        }
    }

    public static void addWaterMaker(String pdfPath, float postionX, float positionY, String outputFileName){
        try {
            PdfReader reader = new PdfReader(pdfPath);
            File targetFile = new File(pdfPath);
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(new File(targetFile.getParent(), outputFileName)));
            int pageCount = reader.getNumberOfPages() + 1;
            stamper.getUnderContent(0);

//            BaseFont baseFont = BaseFont.createFont("印章", BaseFont.);
            Image image = Image.getInstance("D:/test/timg.jpg");
            image.setAbsolutePosition(postionX, positionY);
//            image.setLayer();
            PdfGState gs1 = new PdfGState();
            gs1.setFillOpacity(0.1f);
            BaseColor baseColor = new BaseColor(20, 20, 20, 0);
            image.setBorderColor(baseColor);
            image.setScaleToFitHeight(true);
            PdfContentByte pdfContentByte = stamper.getOverContent(1);
            pdfContentByte.setGState(gs1);
            pdfContentByte.addImage(image);

            // todo: ---
            stamper.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BadElementException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}

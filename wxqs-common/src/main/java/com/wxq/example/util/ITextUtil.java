package com.wxq.example.util;

import com.itextpdf.text.*;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.pdf.*;
import com.wxq.example.exception.FileGenException;
import com.wxq.example.model.PdfHeader;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2017/11/10.
 */
public class ITextUtil {
    private static Log logger = LogFactory.getLog(ITextUtil.class);

    private static BaseFont bfChinese;
    private static BaseFont bFont;
    private static Font cfont ;


    /**
     * 生成pdf
     * @param html
     * @param targetUrl
     * @return
     * @throws Exception
     */
    public static PdfWriter createPdfByHtml(String html, String targetUrl) throws Exception {
        logger.info("开始生成pdf");
        logger.info("targetUrl-----------"+targetUrl);
        List<Element> parseToList = HTMLWorker.parseToList(new StringReader(html), null, new HashMap<String, Object>());
        Document d = new Document(PageSize.A4);
        logger.info("执行1");
        PdfWriter writer=PdfWriter.getInstance(d, new FileOutputStream(new File(targetUrl)));
        d.open();
        logger.info("执行2");
        for (Element e : parseToList) {
            fixChineseCoding(e);
            d.add(e);
        }
        logger.info("targetUrl-----------"+targetUrl);
        System.out.println("pdf已生成");
        d.close();
        writer.close();
        return writer;
    }

    /**
     * 修正中文乱码问题
     * 现只支持table与p,div,span等
     * @param e
     * @return
     * @throws IOException
     * @throws DocumentException
     */
    private static void  fixChineseCoding(Element e) throws IOException, DocumentException {
        String os = System.getProperty("os.name");
        if(bfChinese==null){
            if(os.toLowerCase().startsWith("win")){
                bfChinese = BaseFont.createFont("C:\\Windows\\Fonts\\simfang.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
                bFont = BaseFont.createFont("C:\\Windows\\Fonts\\seguisym.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            }else{
                bfChinese = BaseFont.createFont("/usr/share/fonts/simfang.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
                bFont = BaseFont.createFont("/usr/share/fonts/seguisym.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            }
        }

        if(e instanceof Paragraph || e instanceof com.itextpdf.text.List){
            System.out.println(e.getClass());
            for(Chunk c:e.getChunks()){//解决中文乱码
                System.out.println(c.getContent());
                if(c.getContent().equals("☑")||c.getContent().equals("☒")||c.getContent().equals("☐")){
                    cfont = new Font(bFont, 20, Font.NORMAL);
                }else{
                    cfont = new Font(bfChinese, 20, Font.NORMAL);
                }
                cfont.setColor(c.getFont().getColor());
                cfont.setSize(c.getFont().getSize());
                cfont.setStyle(c.getFont().getStyle());
                c.setFont(cfont);
            }
            return;
        }
        if(e instanceof PdfPTable){
            PdfPTable table=(PdfPTable)e;
            for(PdfPRow row:table.getRows()){
                for(PdfPCell cell:row.getCells()){
                    if(cell!=null&&cell.getCompositeElements()!=null){
                        //cell.setBorderColor(BaseColor.BLACK);
                        cell.setBorderWidth(1);
                        for(Element comp:cell.getCompositeElements()){
                            fixChineseCoding(comp);
                            //表格问题
                            System.out.println("颜色-----"+cell.getBackgroundColor());
                            System.out.println("边框-----"+cell.getBorder());
                            System.out.println("边框颜色-----"+cell.getBorderColor());
                            System.out.println("边框的宽-----"+cell.getBorderWidth());
                            cell.setBackgroundColor(cell.getBackgroundColor());
                            cell.setBorder(cell.getBorder());
                            cell.setBorderColor(BaseColor.BLACK);
                            cell.setBorderWidth(1);
                            cell.setBackgroundColor(cell.getBackgroundColor());

                        }
                    }
                }
            }
        }
    }

    /**
     * 添加水印
     * @param waterMarkPicPath
     * @param os
     * @param reader
     * @throws DocumentException
     * @throws IOException
     */
    private static void watermark(Object waterMarkPicPath, OutputStream os, PdfReader reader) throws DocumentException,
            IOException {
        Rectangle r = reader.getPageSize(1);
        PdfStamper stamper = new PdfStamper(reader, os);
        int total = reader.getNumberOfPages() + 1;
        Image image = null;
        if(waterMarkPicPath instanceof URL){
            image = Image.getInstance((URL)waterMarkPicPath);
        }
        if(waterMarkPicPath instanceof String){
            image = Image.getInstance((String)waterMarkPicPath);
        }
        image.setAbsolutePosition(200, 400);
        PdfContentByte content;
        for (int i = 1; i < total; i++) {
            content = stamper.getOverContent(i);
            content.saveState();
            // set Transparency
            PdfGState gs = new PdfGState();
            gs.setFillOpacity(0.5f);// 设置透明度为0.2
            content.setGState(gs);

            content.beginText();
            content.setColorFill(BaseColor.GRAY);
            content.setFontAndSize(FontUtil.getWaterFont(), 60);
            float middle = (r.getLeft() + r.getRight()) / 2;

            content.addImage(image, 300, 0, 0, 163, middle - 150, 150);
            content.addImage(image, 300, 0, 0, 163, middle - 150, 350);
            content.addImage(image, 300, 0, 0, 163, middle - 150, 550);
            content.restoreState();// 注意这里必须调用一次restoreState 否则设置无效
        }
        stamper.close();
        reader.close();
        os.close();
    }

    /**
     * 加页眉页脚
     * @param originPdf
     * @param targetPdf
     * @param header
     * @return
     * @throws FileGenException
     */
    public static int addHeaderAndPageNum(String originPdf, String targetPdf, PdfHeader header) throws FileGenException {
        logger.info("开始分页");
        PdfReader reader;
        try {
            reader = new PdfReader(originPdf);
        } catch (IOException e) {
            throw new FileGenException(e);
        }
        FileOutputStream os = null;
        try {
            logger.info("开始分页1");
            os = new FileOutputStream(targetPdf);
            //初始化一些参数
            Rectangle r = reader.getPageSize(1);
            PdfStamper stamper = new PdfStamper(reader, os);
            int total = reader.getNumberOfPages() + 1;
            ImagePosition splitLineImage = getSplitLineImagePosition(r, header.getSplitLine());
            //ImagePosition logoImage = getLogoImagePosition(r, header.getImgLeft());
            PdfContentByte content;
            for (int i = 1; i < total; i++) {
                content = stamper.getOverContent(i);
                content.saveState();
                //添加logo
                //addImage(logoImage, content);
                //添加分割线
                addImage(splitLineImage, content);
                //添加页码
                //addPageNumber(i - header.getPageNoStart(), r, content);
                //添加页眉右边文字信息
                addRightHeaderText(header.getTextRight(), r, content);
                //添加页眉左边文字信息
                addLeftHeaderText(header.getTextLeft(), r, content);
                content.restoreState();
            }
            stamper.close();
            reader.close();
            os.close();
            return total - 1;
        } catch (IOException | DocumentException e) {
            logger.info("开始分页2");
            throw new FileGenException(e);
        } finally {
            logger.info("开始分页3");
            reader.close();
            IOUtils.closeQuietly(os);
        }
    }

    /**
     * 合并pdf
     * @param streamOfPDFFiles
     * @param outputStream
     * @param paginate
     */
    public static void MergePDFs(List<InputStream> streamOfPDFFiles, OutputStream outputStream, boolean paginate) {
        logger.info("开始----------------------合并");
        Document document = new Document();
        try {
            List<InputStream> pdfs = streamOfPDFFiles;
            List<PdfReader> readers = new ArrayList<PdfReader>();
            int totalPages = 0;
            Iterator<InputStream> iteratorPDFs = pdfs.iterator();

            // Create Readers for the pdfs.
            while (iteratorPDFs.hasNext()) {
                InputStream pdf = iteratorPDFs.next();
                PdfReader pdfReader = new PdfReader(pdf);
                readers.add(pdfReader);
                totalPages += pdfReader.getNumberOfPages();
            }
            // Create a writer for the outputstream
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);

            document.open();
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA,
                    BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            PdfContentByte cb = writer.getDirectContent(); // Holds the PDF data

            PdfImportedPage page;
            int currentPageNumber = 0;
            int pageOfCurrentReaderPDF = 0;
            Iterator<PdfReader> iteratorPDFReader = readers.iterator();

            // Loop through the PDF files and add to the output.
            while (iteratorPDFReader.hasNext()) {
                PdfReader pdfReader = iteratorPDFReader.next();
                // Create a new page in the target for each source page.
                while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) {
                    document.newPage();
                    pageOfCurrentReaderPDF++;
                    currentPageNumber++;
                    page = writer.getImportedPage(pdfReader,
                            pageOfCurrentReaderPDF);
                    cb.addTemplate(page, 0, 0);

                    // Code for pagination.
                    if (paginate/*&&currentPageNumber<totalPages*/) {
                        cb.beginText();
                        cb.setFontAndSize(bf, 10);
                        /*cb.showTextAligned(PdfContentByte.ALIGN_CENTER, ""
                                + currentPageNumber + " of " + totalPages, 520,
                                5, 0);*/
                        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, ""
                                + currentPageNumber, (float) 297.5, 20, 0);
                        cb.endText();
                    }
                }
                pageOfCurrentReaderPDF = 0;
            }
            outputStream.flush();
            document.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (document.isOpen())
                document.close();
            try {
                if (outputStream != null)
                    outputStream.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    public static byte[] watermark(byte[] fileBytes, String waterMarkAbsolutePicPath) throws FileGenException {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PdfReader reader = new PdfReader(fileBytes);
            watermark(new URL(waterMarkAbsolutePicPath), bos, reader);
            return bos.toByteArray();
        } catch (IOException | DocumentException e) {
            logger.error(e);
            throw new FileGenException(e);
        }
    }

    public static byte[] watermark(byte[] fileBytes, URL waterMarkPicPath) throws FileGenException {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PdfReader reader = new PdfReader(fileBytes);
            watermark(waterMarkPicPath, bos, reader);
            return bos.toByteArray();
        } catch (IOException | DocumentException e) {
            logger.error(e);
            throw new FileGenException(e);
        }
    }

    public static void watermark(String originPdf, String targetPdf, String waterMarkAbsolutePicPath) throws FileGenException {
        try {
            PdfReader reader = new PdfReader(originPdf);
            FileOutputStream fos = new FileOutputStream(targetPdf);
            /*if (!waterMarkAbsolutePicPath.startsWith("file://")) {
                waterMarkAbsolutePicPath = "file://" + waterMarkAbsolutePicPath;
            }*/
            watermark(waterMarkAbsolutePicPath, fos, reader);
        } catch (IOException | DocumentException e) {
            throw new FileGenException(e);
        }
    }

    public static void watermark(String originPdf, String targetPdf, Object waterMarkPicPath) throws FileGenException {
        try {
            PdfReader reader = new PdfReader(originPdf);
            FileOutputStream fos = new FileOutputStream(targetPdf);
            watermark(waterMarkPicPath, fos, reader);
        } catch (IOException | DocumentException e) {
            throw new FileGenException(e);
        }
    }

    public static void watermark(InputStream in, String targetPdf, Object waterMarkPicPath) throws FileGenException {
        try {
            PdfReader reader = new PdfReader(in);
            FileOutputStream fos = new FileOutputStream(targetPdf);
            watermark(waterMarkPicPath, fos, reader);
        } catch (IOException | DocumentException e) {
            throw new FileGenException(e);
        }
    }

    public static int getNumberOfPages(InputStream inputStream) throws FileGenException {
        PdfReader reader;
        try {
            reader = new PdfReader(inputStream);
        } catch (IOException e) {
            throw new FileGenException("获取PDF总页数出错!", e);
        }
        int num = reader.getNumberOfPages();
        reader.close();
        return num;
    }

    public static int getNumberOfPages(byte[] bytes) throws FileGenException {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        return getNumberOfPages(bis);
    }

    public static int getNumberOfPages(File file) throws FileGenException {
        FileInputStream fis;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new FileGenException("获取PDF总页数出错!", e);
        }
        return getNumberOfPages(fis);
    }

    public static int getNumberOfPages(String path) throws FileGenException {
        return getNumberOfPages(new File(path));
    }



    private static void addImage(ImagePosition image, PdfContentByte content) throws DocumentException {
        if (image != null) {
            content.addImage(image.image, image.width, 0, 0, image.height, image.x, image.y);
        }
    }

    private static ImagePosition getSplitLineImagePosition(Rectangle r, URL imageUrl) throws IOException, BadElementException {
        if (imageUrl != null) {
            float x = r.getLeft() + 28f;
            float y = r.getTop() - 32f;
            float width = r.getRight() - 56f;
            float height = 1f;
            Image image = Image.getInstance(imageUrl);
            image.setAbsolutePosition(200, 400);
            return new ImagePosition(x, y, width, height, image);
        } else {
            return null;
        }

    }

    private static ImagePosition getLogoImagePosition(Rectangle r, URL imageUrl) throws IOException, BadElementException {
        if (imageUrl != null) {
            float x = r.getLeft() + 20f;
            float y = r.getTop() - 32f;
            Image image = Image.getInstance(imageUrl);
            float height = 30f;
            float width = height * image.getWidth() / image.getHeight();
            image.setAbsolutePosition(200, 400);
            return new ImagePosition(x, y, width, height, image);
        } else {
            return null;
        }
    }

    private static void addPageNumber(int pageNumber, Rectangle r, PdfContentByte content) throws DocumentException, IOException {
        if (pageNumber > 0) {
            ColumnText.showTextAligned(content, Element.ALIGN_CENTER, new
                    Phrase(String.valueOf(pageNumber), FontUtil.getCourier()), (r.getLeft() + r.getRight()) / 2, 30f, 0);
        }
    }

    private static void addRightHeaderText(String headerText, Rectangle r, PdfContentByte content) throws IOException, DocumentException {
        if (org.apache.commons.lang3.StringUtils.isNotBlank(headerText)) {
            Font font = FontUtil.getVersion();
            /*ColumnText.showTextAligned(content, Element.ALIGN_CENTER, new
                    Phrase(headerText, font), (r.getLeft() + r.getRight()) / 2, r.getTop() - 30f, 0);*/
            ColumnText.showTextAligned(content, Element.ALIGN_CENTER, new
                    Phrase(headerText, font), r.getRight()-80f, r.getTop() - 30f, 0);
        }
    }

    private static void addLeftHeaderText(String headerText, Rectangle r, PdfContentByte content) throws IOException, DocumentException {
        if (org.apache.commons.lang3.StringUtils.isNotBlank(headerText)) {
            Font font = FontUtil.getVersion();
            ColumnText.showTextAligned(content, Element.ALIGN_CENTER, new
                    Phrase(headerText, font), r.getLeft()+90f, r.getTop() - 30f, 0);
        }
    }

    /**
     * 将图片插入到pdf
     * @param imageUrllist
     * @param mOutputPdfFileName
     * @return
     */
    public static File imageToPdf(ArrayList<String> imageUrllist,String mOutputPdfFileName) {
        Document doc = new Document(PageSize.A4, 20, 20, 20, 20);
        try {
            PdfWriter.getInstance(doc, new FileOutputStream(mOutputPdfFileName));
            doc.open();
            for (int i = 0; i < imageUrllist.size(); i++) {
                doc.newPage();
                Image img = Image.getInstance(imageUrllist.get(i));
                float heigth = img.getHeight();
                float width = img.getWidth();
                int percent = getPercent2(heigth, width);
                img.setAlignment(Image.MIDDLE);
                img.scalePercent(percent+3);// 表示是原来图像的比例;
                doc.add(img);
            }
            doc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File mOutputPdfFile = new File(mOutputPdfFileName);
        if (!mOutputPdfFile.exists()) {
            mOutputPdfFile.deleteOnExit();
            return null;
        }
        return mOutputPdfFile;
    }

    /**
     * 第一种解决方案 在不改变图片形状的同时，判断，如果h>w，则按h压缩，否则在w>h或w=h的情况下，按宽度压缩
     *
     * @param h
     * @param w
     * @return
     */

    public static int getPercent(float h, float w) {
        int p = 0;
        float p2 = 0.0f;
        if (h > w) {
            p2 = 297 / h * 100;
        } else {
            p2 = 210 / w * 100;
        }
        p = Math.round(p2);
        return p;
    }

    /**
     * 第二种解决方案，统一按照宽度压缩 这样来的效果是，所有图片的宽度是相等的，自我认为给客户的效果是最好的
     *
     */
    public static int getPercent2(float h, float w) {
        int p = 0;
        float p2 = 0.0f;
        p2 = 530 / w * 100;
        p = Math.round(p2);
        return p;
    }

    private static class ImagePosition {
        private float x;
        private float y;
        private float width;
        private float height;
        private Image image;

        ImagePosition(float x, float y, float width, float height, Image image) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.image = image;
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

        public float getWidth() {
            return width;
        }

        public float getHeight() {
            return height;
        }

        public Image getImage() {
            return image;
        }
    }
}

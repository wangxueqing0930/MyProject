package com.wxq.example.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.List;

public class FileUtil {
    // 最大10M
    private static final int FILE_MAX_SIZE = 10485760;

    private static final int BUFFER_SIZE = 8192;

    /**
     * 读取文件中的所有字节
     *
     * @param file 文件
     * @return 文件所有字节
     */
    public static byte[] readFileBytes(File file) {
        if (!file.isFile()) {
            throw new IllegalArgumentException("This is not a file!" + "path:" + file.getAbsolutePath());
        }
        long size = FileUtils.sizeOf(file);
        if (size > FILE_MAX_SIZE) {
            throw new IllegalArgumentException("Size of file is too long to read!" + "path:" + file.getAbsolutePath());
        } else {
            try {
                return FileUtils.readFileToByteArray(file);
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

    /**
     * 删除文件
     *
     * @param file 文件
     * @return 删除是否成功
     */
    public static boolean deleteFileQuietly(File file) {
        if (file.isDirectory()) {
            return false;
        }
        try {
            return FileUtils.deleteQuietly(file);
        } catch (Exception ignored) {
            return false;
        }
    }

    /**
     * 删除文件
     *
     * @param files 文件对象数组
     */
    public static void deleteFileQuietly(File... files) {
        for (File file : files) {
            deleteFileQuietly(file);
        }
    }

    public static void deleteFileQuietly(String... filePaths) {
        for (String path : filePaths) {
            deleteFileQuietly(new File(path));
        }
    }

    public static void deleteFileQuietly(List<File> filePathList) {
        for (File file : filePathList) {
            deleteFileQuietly(file);
        }
    }

    /**
     * html文件转成pdf
     *
     * @param htmlPath html路径
     * @param pdfPath  pdf目标路径
     * @return 转换是否成功
     * @throws IOException
     * @throws InterruptedException
     */
    /*public static boolean transHtml2Pdf(String htmlPath, String pdfPath) throws IOException, InterruptedException {
        String osName = System.getProperty("os.name");
        String phantomPathKey = "phantom.path.linux";
        String pageJs = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "static/script/page2a4.js";
        if (osName.toLowerCase().contains("windows")) {
            phantomPathKey = "phantom.path.windows";
            if (pageJs.startsWith("/")) {
                pageJs = pageJs.substring(1);
            }
        }
        pageJs = new File(pageJs).getAbsolutePath();
        String phantom = "\"" + AppResource.get(phantomPathKey) + "\"";
        StringBuilder cmd = new StringBuilder();
        cmd.append(phantom).append(" ");
        cmd.append("\"").append(pageJs).append("\" ");
        cmd.append("\"").append(htmlPath).append("\" ");
        cmd.append("\"").append(pdfPath).append("\" ");
        if (osName.toLowerCase().contains("windows")) {
            return CommandUtil.callCmd(cmd.toString());
        }
        return CommandUtil.callShell(cmd.toString());
    }*/
    
    /**
     * pdf转成swf文件
     *
     * @param pdfPath pdf路径
     * @param swfPath swf目标路径
     * @return 转换是否成功
     * @throws InterruptedException
     * @throws IOException
     */
    /*public static boolean transPdf2Swf(String pdfPath, String swfPath) throws InterruptedException, IOException {
        String osName = System.getProperty("os.name");
        String swfToolsPathKey = "swfTools.path.linux";
        String xpdfPathKey = "xpdf.path.linux";

        if (osName.toLowerCase().contains("windows")) {
            swfToolsPathKey = "swfTools.path.windows";
            xpdfPathKey = "xpdf.path.windows";
        }
        String swfToolsPath = "\"" + AppResource.get(swfToolsPathKey) + "\"";
        String xpdfPath = "\"" + AppResource.get(xpdfPathKey) + "\"";
        StringBuilder cmd = new StringBuilder();
        cmd.append(swfToolsPath).append(" -s languagedir=");
        cmd.append(xpdfPath).append(" -s flashversion=9 ");
        cmd.append("\"").append(pdfPath).append("\" -o ").append("\"").append(swfPath).append("\"");
        if (osName.toLowerCase().contains("windows")) {
            return CommandUtil.callCmd(cmd.toString());
        }
        return CommandUtil.callShell(cmd.toString());
    }*/

    /**
     * 将字节写到本地文件
     *
     * @param path  文件路径
     * @param bytes 字节
     * @throws IOException
     */
    public static void genLocalFile(String path, byte[] bytes) throws IOException {
        FileOutputStream fos = null;
        File file = new File(path);
        File parent = file.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }
        file.createNewFile();
        try {
            fos = new FileOutputStream(path);
            fos.write(bytes);
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }

    /**
     * 将输入流写入到本地文件
     *
     * @param path 文件路径
     * @param in   输入流
     * @throws IOException
     */
    public static void genLocalFile(String path, InputStream in) throws IOException {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path);
            File file = new File(path);
            File parent = file.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            file.createNewFile();
            byte[] buf = new byte[BUFFER_SIZE];
            int n;
            while ((n = in.read(buf)) > 0) {
                fos.write(buf, 0, n);
            }
            fos.flush();
        } finally {
            if (in != null) {
                in.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
    }

    /**
     * 使用uuid生成随机的文件路径
     *
     * @param parentPath 文件所在目录
     * @param extension  扩展名
     * @return 文件的路径
     */
    public static String genRandomFileName(String parentPath, String extension) {
        if (!extension.startsWith(".")) {
            extension = "." + extension;
        }
        String path = parentPath + CommonUtil.getRandomFileName() + extension;
        File pdfFile = new File(path);
        if (!pdfFile.getParentFile().exists()) {
            pdfFile.getParentFile().mkdirs();
        }
        return path;
    }

    /**
     * 使用uuid生成随机的文件名
     *
     * @param extension 扩展名
     * @return 文件名
     */
    public static String genRandomFileName(String extension) {
        return CommonUtil.getRandomFileName() + "." + extension;
    }

    /**
     * 将流读入字节数组,不建议直接使用,文件如果比较大可能会导致内存溢出
     *
     * @param in 输入流
     * @return 字节数组
     * @throws IOException
     */
    public static byte[] readInputStream2Bytes(InputStream in) throws IOException {
        try {
            return IOUtils.toByteArray(in);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    /**
     * 輸入流转入输出流
     *
     * @param in  输入
     * @param out 输出
     * @throws IOException
     */
    public static void inputStream2OutputStream(InputStream in, OutputStream out) throws IOException {
        byte[] buf = new byte[BUFFER_SIZE];
        int n;
        while ((n = in.read(buf)) > 0) {
            out.write(buf, 0, n);
        }
        in.close();
        out.close();
        out.flush();
    }

    /**
     * 根据全路径生成文件的包括父目录
     *
     * @param path   文件全路径
     * @param isFile 文件还是目录
     */
    public static void mkDirs(String path, boolean isFile) {
        File file = new File(path);
        if (isFile && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        } else if (!isFile && !file.exists()) {
            file.mkdirs();
        }
    }
    
    /**
     * 删除文件夹
     * @param folderPath
     */
    public static void delFolder(String folderPath) {
        try {
           delAllFile(folderPath); //删除完里面所有内容
           String filePath = folderPath;
           filePath = filePath.toString();
           File myFilePath = new File(filePath);
           myFilePath.delete(); //删除空文件夹
        } catch (Exception e) {
          e.printStackTrace(); 
        }
    }
    
    /**
     * 删除文件夹下所有文件
     * @param path
     * @return
     */
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
          return flag;
        }
        if (!file.isDirectory()) {
          return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
           if (path.endsWith(File.separator)) {
              temp = new File(path + tempList[i]);
           } else {
               temp = new File(path + File.separator + tempList[i]);
           }
           if (temp.isFile()) {
              temp.delete();
           }
           if (temp.isDirectory()) {
              delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
              delFolder(path + "/" + tempList[i]);//再删除空文件夹
              flag = true;
           }
        }
        return flag;
      }
    
    // 创建目录
 	public static boolean createDir(String destDirName) {
 		File dir = new File(destDirName);
 		if (dir.exists()) {// 判断目录是否存在
 			System.out.println("创建目录失败，目标目录已存在！");
 			return false;
 		}
 		if (!destDirName.endsWith(File.separator)) {// 结尾是否以"/"结束
 			destDirName = destDirName + File.separator;
 		}
 		if (dir.mkdirs()) {// 创建目标目录
 			System.out.println("创建目录成功！" + destDirName);
 			return true;
 		} else {
 			System.out.println("创建目录失败！");
 			return false;
 		}
 	}

}

package com.senit.javautil.util;

import com.senit.javautil.model.FileType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by sen on 2016/8/15.
 */
public class FileTypeCheckUtil {
    /** * Constructor */
    private FileTypeCheckUtil() {
    }

    /** * 将文件头转换成16进制字符串 * * @param 原生byte * @return 16进制字符串 */
    private static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 得到文件头
     *
     * @param filePath
     *            文件路径
     * @return 文件头
     * @throws IOException
     */
    private static String getFileContent(String filePath) throws IOException {
        byte[] b = new byte[28];
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filePath);
            inputStream.read(b, 0, 28);
        } catch (IOException e) {
            throw e;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw e;
                }
            }
        }
        return bytesToHexString(b);
    }

    /** * 判断文件类型 * * @param filePath * 文件路径 * @return 文件类型 */
    public static FileType getType(String filePath) throws IOException {
        String fileHead = getFileContent(filePath);
        if (fileHead == null || fileHead.length() == 0) {
            return null;
        }
        fileHead = fileHead.toUpperCase();
        FileType[] fileTypes = FileType.values();
        for (FileType type : fileTypes) {
            if (fileHead.startsWith(type.getValue())) {
                return type;
            }
        }
        return null;
    }

    /**
     * 判断文件类型
     *
     * @param filePath
     *            文件路径
     * @return 文件类型
     */
    public static FileType getFileType(String fileHead) throws IOException {
        if (fileHead == null || fileHead.length() == 0) {
            return null;
        }
        fileHead = fileHead.toUpperCase();
        FileType[] fileTypes = FileType.values();
        for (FileType type : fileTypes) {
            if (fileHead.startsWith(type.getValue())) {
                return type;
            }
        }
        return null;
    }

    /**
     * 判断文件类型
     *
     * @param filePath
     *            文件路径
     * @return 文件类型
     */
    public static FileType getFileType(byte[] fileHeadByte) {
        if (fileHeadByte == null || fileHeadByte.length == 0) {
            return null;
        }
        String fileHead = bytesToHexString(fileHeadByte).toUpperCase();
        FileType[] fileTypes = FileType.values();
        for (FileType type : fileTypes) {
            if (fileHead.startsWith(type.getValue())) {
                return type;
            }
        }
        return null;
    }

    /**
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static String getFileHead(File file) throws IOException {
        byte[] b = new byte[28];
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            inputStream.read(b, 0, 28);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw e;
                }
            }
        }
        return bytesToHexString(b);
    }

//    public static void main(String args[]) throws Exception {
//        System.out.println(FileTypeCheckUtil.getType("C:\\Users\\niow\\Desktop\\IMG_0655.JPG"));
//        System.out.println(FileTypeCheckUtil.getType("d:\\Beyond Compare 3.rar"));
//        System.out.println(FileTypeCheckUtil.getType("d:\\IE11-Windows6.1.exe"));
//        System.out.println(FileTypeCheckUtil.getType("d:\\jquery.form.js"));
//        System.out.println(FileTypeCheckUtil.getType("d:\\SQLFULL_CHS.iso"));
//        System.out.println(FileTypeCheckUtil.getType("d:\\site-1.8.8.zip"));
//        System.out.println(FileTypeCheckUtil.getType("d:\\httpd-2.2.25-win32-x86-no_ssl.msi"));
//    }
}

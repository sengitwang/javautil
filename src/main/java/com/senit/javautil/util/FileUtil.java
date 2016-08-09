package com.senit.javautil.util;
import org.apache.commons.lang3.StringUtils;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by sen on 2016/8/9.
 * 文件操作
 */
public class FileUtil {

    public static final String DOT = ".";
    /**
     * 根据文件名获取resource目录下的文件
     * @param fileName
     * @return
     * @throws IOException
     */
    public static InputStream readXmlFile(String fileName) throws IOException {
        return Class.class.getClass().getResource("/" + fileName).openStream();
    }


    /**
     * 获取没有扩展名的文件名
     *
     * @param fileName
     * @return
     */
    public static String getWithoutExtension(String fileName) {
        String ext = StringUtils.substring(fileName, 0,
                StringUtils.lastIndexOf(fileName, DOT));
        return StringUtils.trimToEmpty(ext);
    }

    /**
     * 关闭一个或多个流对象
     *
     * @param closeables
     *            可关闭的流对象列表
     * @throws IOException
     */
    public static void close(Closeable... closeables) throws IOException {
        if (closeables != null) {
            for (Closeable closeable : closeables) {
                if (closeable != null) {
                    closeable.close();
                }
            }
        }
    }

    /**
     *
     * 关闭一个或多个流对象
     *
     * @param closeables
     *            可关闭的流对象列表
     */
    public static void closeQuietly(Closeable... closeables) {
        try {
            close(closeables);
        } catch (IOException e) {
            // do nothing
        }
    }

    /**
     * 文件压缩
     * @param filePath 文件路径
     * @param isOrNotZip 是否压缩
     */
    public static void FileZip(String filePath,boolean isOrNotZip){
        File source = new File(filePath);
        if(source.exists()){
            Long fileLength=source.length();
            if(!isOrNotZip) {
                if (fileLength > 102400) {//102400可更换，字节大小
                    ZipUtil.zip(filePath);
                }
            }else{
                ZipUtil.zip(filePath);
            }
        }

    }

}

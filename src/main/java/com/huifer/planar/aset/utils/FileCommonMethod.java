package com.huifer.planar.aset.utils;

import com.csvreader.CsvWriter;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;

/**
 * <p>Title : FileCommonMethod </p>
 * <p>Description : 文件工具</p>
 *
 * @author huifer
 * @date 2019-01-18
 */
public class FileCommonMethod {


    public static void writeCSV(String path, String[] headers, List<String> writeArraylist)
            throws IOException {

        String csvFilePath = path;

        // 创建CSV写对象 例如:CsvWriter(文件路径，分隔符，编码格式);
        CsvWriter csvWriter = new CsvWriter(csvFilePath, ';', Charset.forName("GBK"));
        // 写内容
        csvWriter.writeRecord(headers);
        for (int i = 0; i < writeArraylist.size(); i++) {
            String[] writeLine = writeArraylist.get(i).split(";");
            csvWriter.writeRecord(writeLine);
        }
        csvWriter.close();
    }

    /**
     * 当前项目路径下的 xxx
     *
     * @param fileParent 当前项目路径下，文件前面的路径
     * @param fileName   当前项目路径下 + 文件前面路径 + 文件名
     * @return 完整路径
     */
    public static String getProjectPath(String fileParent, String fileName) {
        File directory = new File(".");
        String fn = null;
        try {
            fn = directory.getCanonicalPath();
            if (fileParent != null) {
                fn = fn + File.separator + fileParent + File.separator
                        + fileName;
            } else {
                fn = fn + File.separator + fileName;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return fn;
        }
        return fn;
    }

    /**
     * 根据类加载器获取路径,并且获取资源文件的字节流
     *
     * @param getclass class
     * @param fileName 文件名
     * @return 路径
     */
    public static InputStream getClassFileInputStreamPathByClassLoader(
            Class<? extends Object> getclass,
            String fileName) {
        InputStream is = null;
        try {
            if (getclass != null) {
                if (getclass.getClassLoader() != null
                        && getclass.getClassLoader().getResourceAsStream(fileName) != null) {
                    is = getclass.getClassLoader().getResourceAsStream(fileName);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            return is;
        }
        return is;
    }


    /**
     * 根据类来获取路径
     *
     * @param getclass class
     * @param fileName 名称
     * @return 路径
     */
    public static String getClassFilePathByClass(Class<? extends Object> getclass,
                                                 String fileName) {
        String path = null;
        try {
            if (getclass != null) {
                if (getclass.getResource(fileName) != null) {
                    path = getclass.getResource(fileName)
                            .toString();
                }
                if (path != null && path.contains("file:/")) {
                    path = path.replaceAll("file:/", "");
                }

                if (path != null && path.contains("jar:")) {
                    path = path.replaceAll("jar:", "");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return path;
        }
        return path;
    }

    /**
     * jar路径
     *
     * @param getclass class
     * @return jar路径
     */
    public static String getJarClassFilePath(Class<? extends Object> getclass) {
        String jarFilePath = null;
        try {
            jarFilePath = getclass.getProtectionDomain().getCodeSource()
                    .getLocation().getFile();
            jarFilePath = java.net.URLDecoder.decode(jarFilePath, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return jarFilePath;
        }
        return jarFilePath;
    }


    /**
     * 根据类加载器获取项目跟路径
     *
     * @param projectName 项目名称
     * @return 路径
     */
    public static String getRootPathByClassLoader(String projectName) {
        String path = null;
        try {
            if (Object.class != null) {
                if (Object.class.getClassLoader() != null
                        && Object.class.getClassLoader().getResource("") != null) {
                    path = Object.class.getClassLoader().getResource("")
                            .toString();

                }
                if (path != null && path.contains("file:/")) {
                    path = path.replaceAll("file:/", "");
                }

                if (path != null && path.contains("file://")) {
                    path = path.replaceAll("file://", "");
                }
                if (path != null && path.contains("null")) {
                    path = path.replaceAll("null", "");
                }
                if (path != null && path.contains(projectName)) {
                    path = path.substring(0, path.indexOf(projectName)) + projectName;
                }

                if (path != null && path.contains("jar:")) {
                    path = path.replaceAll("jar:", "");
                }

                if (path != null && path.contains("%20")) {
                    path = path.replaceAll("%20", " ");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return path;
        }
        return path;
    }

    /**
     * 根据类加载器获取项目根路径
     *
     * @param getclass    class
     * @param projectName 项目名称
     * @return 路径
     */
    public static String getRootPathByClassLoader(Class<? extends Object> getclass,
                                                  String projectName) {
        String path = null;
        try {
            if (getclass != null) {
                if (getclass.getClassLoader() != null
                        && getclass.getClassLoader().getResource("") != null) {
                    path = getclass.getClassLoader().getResource("")
                            .toString();

                }
                if (path != null && path.contains("file:/")) {
                    path = path.replaceAll("file:/", "");
                }

                if (path != null && path.contains("file://")) {
                    path = path.replaceAll("file://", "");
                }

                if (path != null && path.contains(projectName)) {
                    path = path.substring(0, path.indexOf(projectName)) + projectName;
                }

                if (path != null && path.contains("jar:")) {
                    path = path.replaceAll("jar:", "");
                }

                if (path != null && path.contains("%20")) {
                    path = path.replaceAll("%20", " ");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return path;
        }
        return path;
    }

    /**
     * 根据类加载器获取路径
     *
     * @param getclass class
     * @param fileName 文件名
     * @return 路径
     */
    public static String getClassFilePathByClassLoader(Class<? extends Object> getclass,
                                                       String fileName) {
        String path = null;
        try {
            if (getclass != null) {
                if (getclass.getClassLoader() != null
                        && getclass.getClassLoader().getResource(fileName) != null) {
                    path = getclass.getClassLoader().getResource(fileName)
                            .toString();

                }
                if (path != null && path.contains("file:/")) {
                    path = path.replaceAll("file:/", "");
                }

                if (path != null && path.contains("jar:")) {
                    path = path.replaceAll("jar:", "");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return path;
        }
        return path;
    }


    /**
     * 文件追加内容
     *
     * @param filePath 文件位置
     * @param content  内容
     * @param isClean  是否清空
     */
    public static void fileAdd(String filePath, String content, boolean isClean) {
        FileWriter fileWriter = null;
        try {
            File f = new File(filePath);
            fileWriter = new FileWriter(f, true);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            if (isClean) {
                fileWriter.write("");
            }
            printWriter.println(content);
            printWriter.flush();
            fileWriter.flush();
            printWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

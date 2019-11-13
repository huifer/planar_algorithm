package com.huifer.planar.aset.utils.geo;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.data.collection.CollectionFeatureSource;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.geojson.GeoJSON;
import org.geotools.geojson.feature.FeatureJSON;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Title : GeoJsonUtil </p>
 * <p>Description : geojson工具</p>
 *
 * @author huifer
 * @date 2019-01-18
 */
public class GeoJsonUtil {

    private static String EQST = "";

    /**
     * json转成 geojson
     *
     * @param json    json
     * @param lonStr  经度字段的key
     * @param latStr  纬度字段的key
     * @param geoType 1点类型 2线类型 3面类型
     */
    public static String jsonToGeoJson(String json, String lonStr,
                                       String latStr, int geoType) {

        String returnStr = null;
        String type1 = "Point";
        String type2 = "LineString";
        String type3 = "Polygon";
        Map<String, Object> mapFeatureCollection = new HashMap<String, Object>();
        List<Map<String, Object>> features = new ArrayList<Map<String, Object>>();

        Map<String, Object> mapFeature = new HashMap<String, Object>();
        Map<String, Object> mapGeometry = new HashMap<String, Object>();
        Map<String, Object> mapProperties = new HashMap<String, Object>();
        List<Double> coordinates = new ArrayList<Double>();
        // 开始拼装
        mapFeatureCollection.put("type", "FeatureCollection");
        mapFeatureCollection.put("features", features);
        try {
            // (json, ArrayList.class);
            Object jo = new JSONParser().parse(json);
            if (jo instanceof JSONArray) {
                JSONArray jsonarr = (JSONArray) jo;
                for (int i = 0; i < jsonarr.size(); i++) {
                    Object js = jsonarr.get(i);
                    if (js instanceof Map) {
                        mapProperties = (Map<String, Object>) js;
                        // 单个要素
                        mapFeature = new HashMap<String, Object>();
                        mapGeometry = new HashMap<String, Object>();
                        coordinates = new ArrayList<Double>();
                        // 属性
                        mapProperties.put("type", "Feature");
                        // 几何
                        if (geoType == 1) {
                            mapGeometry.put("type", type1);
                            if (mapProperties.containsKey(lonStr)
                                    && mapProperties.containsKey(latStr)) {
                                coordinates.add(Double
                                        .parseDouble(mapProperties.get(lonStr)
                                                .toString()));
                                coordinates.add(Double
                                        .parseDouble(mapProperties.get(latStr)
                                                .toString()));
                            }
                        }
                        if (geoType == 2) {
                            mapGeometry.put("type", type2);
                        }
                        if (geoType == 3) {
                            mapGeometry.put("type", type3);
                        }

                        mapGeometry.put("coordinates", coordinates);
                        // 要素
                        mapFeature.put("type", "Feature");
                        mapFeature.put("geometry", mapGeometry);
                        mapFeature.put("properties", mapProperties);

                        // 存放
                        features.add(mapFeature);
                    }

                }
            }
            returnStr = JSONObject.toJSONString(mapFeatureCollection);
        } catch (Exception e) {
            e.printStackTrace();
            return returnStr;
        }

        return returnStr;

    }

    /**
     * 读取GeoJSON文件 返回 SimpleFeatureCollection 要素集合
     *
     * @param geojsonFilePah geojson 文件路径
     * @return {@link SimpleFeatureCollection} 要素集合
     */
    public static SimpleFeatureCollection readGeoJsonByFeatureJSON(
            String geojsonFilePah) {
        FileInputStream fis = null;
        SimpleFeatureCollection fs = null;
        try {
            fis = new FileInputStream(geojsonFilePah);
            // geojson读取工具
            FeatureJSON fjson = new FeatureJSON();
            // 获取要素集合
            fs = (SimpleFeatureCollection) fjson.readFeatureCollection(fis);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return fs;

    }

    /**
     * 读取GeoJSON文件 返回 FeatureSource 要素集合
     *
     * @param geojsonFilePah geojson 文件路径
     * @return {@link SimpleFeatureCollection}  要素集合
     */
    public static FeatureSource readGeoJsonByGeojsonToFeatureSource(
            String geojsonFilePah) {
        FileInputStream fis = null;
        @SuppressWarnings("rawtypes")
        FeatureSource fss = null;
        try {
            fis = new FileInputStream(geojsonFilePah);
            // geojson读取工具
            FeatureJSON fjson = new FeatureJSON();
            // 获取要素集合
            SimpleFeatureCollection fs = (SimpleFeatureCollection) fjson.readFeatureCollection(fis);
            fss = creatFeatureSourceByCollection(fs);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return fss;

    }

    /**
     * 根据要素集合 创建数据源
     *
     * @param collection 要素集合
     * @return {@link FeatureSource}
     */
    public static FeatureSource creatFeatureSourceByCollection(SimpleFeatureCollection collection) {
        FeatureSource source = null;
        try {
            source = new CollectionFeatureSource(collection);
        } catch (Exception e) {
            e.printStackTrace();
            return source;
        }
        return source;
    }

    /**
     * 读取GeoJSON字符串 返回 SimpleFeatureCollection 要素集合
     *
     * @param geojson GeoJSON字符串
     * @return {@link SimpleFeatureCollection} 要素集合
     */
    public static SimpleFeatureCollection readGeoJsonByString(String geojson) {
        ByteArrayInputStream bis = null;
        SimpleFeatureCollection fs = null;
        try {
            // 字符串流
            bis = new ByteArrayInputStream(geojson.getBytes());
            // geojson读取工具
            FeatureJSON fjson = new FeatureJSON();
            // 获取要素集合
            fs = (SimpleFeatureCollection) fjson.readFeatureCollection(bis);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return fs;

    }


    /**
     * 15.0才生效 读取GeoJSON文件 返回 DataStore
     *
     * @param path GeoJSON文件所在路径
     * @return {@link DataStore}
     */
    public static DataStore readeGeoJSON(String path) {
        DataStore dataStore = null;
        File file = new File(path);
        Map<String, String> connectionParams = new HashMap<String, String>();
        connectionParams.put("DriverName", "GeoJSON");
        connectionParams.put("DatasourceName", file.getAbsolutePath());
        try {
            dataStore = DataStoreFinder.getDataStore(connectionParams);
        } catch (IOException e) {
            e.printStackTrace();
            return dataStore;
        }
        return dataStore;

    }

    /**
     * 把要素集合写成geojson文件到本地
     *
     * @param path     本地路径
     * @param features 要素集合
     * @param cs       编码
     */
    public static void simpleFeatureCollectionToGeoJSON(String path,
                                                        SimpleFeatureCollection features, String cs) {
        final StringWriter sw = new StringWriter();
        try {
            GeoJSON.write(features, sw);
            toFileByCS(new File(path), sw.getBuffer().toString(), cs);
        } catch (IOException e) {
            e.printStackTrace();
            FeatureJSON fj = new FeatureJSON();
            try {
                fj.writeFeatureCollection(features, sw);
                toFileByCS(new File(path), sw.getBuffer().toString(), cs);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * 根据编码 把字符串写入文件
     *
     * @param saveFile 路径
     * @param content  内容
     * @param cs       编码
     */
    public static void toFileByCS(File saveFile, String content, String cs) {
        File parent = saveFile.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }
        PrintWriter out = null;
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        try {
            fos = new FileOutputStream(saveFile);
            if (cs.equals(EQST)) {
                osw = new OutputStreamWriter(fos);
            } else {
                osw = new OutputStreamWriter(fos, cs);
            }
            out = new PrintWriter(osw);
            out.print(content);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (osw != null) {
                try {
                    osw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 把文件读成字符串
     *
     * @param path     路径
     * @param encoding 编码
     */
    public static String readerFileToStrings(String path, String encoding)
            throws Exception {
        FileInputStream input = null;
        InputStreamReader insreader = null;
        BufferedReader bin = null;
        File flie = null;
        StringBuffer returnStr = null;
        try {
            flie = new File(path);
            returnStr = new StringBuffer();
            if (flie.exists() && flie.canRead()) {
                input = new FileInputStream(flie);
                if (encoding.equals(EQST)) {
                    insreader = new InputStreamReader(input);
                } else {
                    insreader = new InputStreamReader(input, encoding);
                }

                bin = new BufferedReader(insreader);

                String line;
                while ((line = bin.readLine()) != null) {
                    returnStr.append(line);
                }
                bin.close();
                insreader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("err!");
        } finally {
            if (bin != null) {
                try {
                    bin.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (insreader != null) {
                try {
                    insreader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return returnStr.toString();
    }

}

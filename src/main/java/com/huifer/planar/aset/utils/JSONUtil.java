package com.huifer.planar.aset.utils;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonFilter;
import org.codehaus.jackson.map.ser.FilterProvider;
import org.codehaus.jackson.map.ser.impl.SimpleBeanPropertyFilter;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;
import org.codehaus.jackson.map.type.TypeFactory;

/**
 * <p>Title : JSONUtil </p>
 * <p>Description : json工具</p>
 *
 * @author huifer
 * @date 2019-01-18
 */
public class JSONUtil {

    private static ObjectMapper mapper;
    private static ObjectMapper mapperNumbAsString;
    private static String EXCLUSION_FILTER_NAME = "defaultExclusionFilter";

    static {
        mapper = new ObjectMapper();
        mapper.setDeserializationConfig(mapper.getDeserializationConfig()
                .withDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")));
        mapper.setSerializationConfig(mapper.getSerializationConfig()
                .withDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")));
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        mapper.configure(
                org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
                false);

        mapperNumbAsString = new ObjectMapper();
        mapperNumbAsString.setDeserializationConfig(mapperNumbAsString.getDeserializationConfig()
                .withDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")));
        mapperNumbAsString.setSerializationConfig(mapperNumbAsString.getSerializationConfig()
                .withDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")));
        mapperNumbAsString.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        mapperNumbAsString.configure(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS, true);
        mapperNumbAsString.configure(JsonGenerator.Feature.QUOTE_NON_NUMERIC_NUMBERS, true);

        mapperNumbAsString.configure(
                org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
                false);
    }

    /**
     * obj2json
     *
     * @param obj obj
     * @return json字符串
     */
    public static String toJson(Object obj) {

        String jsonStr = "";
        try {
            jsonStr = mapper.writeValueAsString(obj);
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
        return jsonStr;

    }

    /**
     * utf8 编码 json
     * @param obj obj
     * @return json字符串
     * @throws Exception
     */
    public static String encodeJson(Object obj) throws Exception {
        String jsonStr = "";
        try {
            jsonStr = mapper.writeValueAsString(obj);
            Map<String, String> map = new HashMap<String, String>();
            map.put("content", URLEncoder.encode(jsonStr, "UTF-8"));
            jsonStr = mapper.writeValueAsString(map);
        } catch (Exception ex) {
            throw new Exception(ex);
        }
        return jsonStr;

    }

    /**
     * vo2json
     * @param vo
     * @param hidePropertys
     * @return
     * @throws Exception
     */
    public static String toJson(Object vo, String... hidePropertys)
            throws Exception {

        String json = "";

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationConfig(objectMapper
                .getSerializationConfig().withDateFormat(
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")));

        FilterProvider filter = new SimpleFilterProvider().addFilter(
                EXCLUSION_FILTER_NAME, SimpleBeanPropertyFilter
                        .serializeAllExcept(hidePropertys));

        objectMapper.setFilters(filter);
        objectMapper.getSerializationConfig().addMixInAnnotations(
                vo.getClass(), MixIn.class);

        try {
            json = objectMapper.writeValueAsString(vo);
        } catch (Exception ex) {
            throw new Exception(ex);
        }

        return json;
    }

    @Deprecated
    public static String toJson(List<?> voList,
            String... hidePropertys) throws Exception {

        String json = "";

        if (voList.size() == 0) {
            return json;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationConfig(objectMapper
                .getSerializationConfig().withDateFormat(
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")));
        FilterProvider filter = new SimpleFilterProvider().addFilter(
                EXCLUSION_FILTER_NAME, SimpleBeanPropertyFilter
                        .serializeAllExcept(hidePropertys));

        objectMapper.setFilters(filter);
        objectMapper.getSerializationConfig().addMixInAnnotations(
                voList.get(0).getClass(), MixIn.class);

        try {
            json = objectMapper.writeValueAsString(voList);
        } catch (Exception ex) {
            throw new Exception(ex);
        }

        return json;
    }

    public static Object toObject(String jsonString, Class<?> beanClass)
            throws Exception {

        Object obj = "";
        try {
            obj = mapper.readValue(jsonString, beanClass);
        } catch (Exception ex) {
            throw new Exception(ex);
        }
        return obj;
    }

    public static Object toObjectWithNumStr2Mum(String jsonString, Class<?> beanClass)
            throws Exception {
        Object obj = "";
        try {
            obj = mapperNumbAsString.readValue(jsonString, beanClass);
        } catch (Exception ex) {
            throw new Exception(ex);
        }
        return obj;
    }


    public static List<?> toList(String jsonString, Class<?> beanClass) throws Exception {
        List<?> list = null;
        TypeFactory factory = TypeFactory.defaultInstance();
        try {
            list = mapper.readValue(jsonString, factory
                    .constructCollectionType(ArrayList.class, beanClass));
        } catch (Exception ex) {
            throw new Exception(ex);
        }
        return list;

    }

    public static List<?> toListWithNumStr2Mum(String jsonString, Class<?> beanClass)
            throws Exception {
        List<?> list = null;
        TypeFactory factory = TypeFactory.defaultInstance();
        try {
            list = mapperNumbAsString.readValue(jsonString, factory
                    .constructCollectionType(ArrayList.class, beanClass));
        } catch (Exception ex) {
            throw new Exception(ex);
        }
        return list;

    }

    @JsonFilter("defaultExclusionFilter")
    interface MixIn {

    }

}

package com.huifer.planar.aset.algo;

import java.text.ParseException;
import java.util.List;

/**
 * 工作日日期接口
 *
 * @author huifer
 */
public interface DateInterface {

    /**
     * 获取 xxxx 年 xx 月的所有日期
     *
     * @param year  年
     * @param month 月
     * @return 月历
     */
    List<String> monthlyCalendar(int year, int month);

    /**
     * 获取 xxxx 年 xx 月 总天数
     *
     * @param year  年
     * @param month 月
     * @return 天数
     */
    int maxDayInMonth(int year, int month);

    /**
     * 是否是周末
     *
     * @param dateString 日期 2019-01-01
     * @return boolean
     * @throws ParseException 异常
     */
    boolean isWeekends(String dateString) throws ParseException;

    /**
     * 是否法定假日
     *
     * @param dateString 日期 2019-01-01
     * @return boolean
     */
    boolean isStatutoryHoliday(String dateString);


    /**
     * 是否额外工作日
     *
     * @param dateString 日期 2019-01-01
     * @return boolean
     */
    boolean isExtraWorkingDay(String dateString);


    /**
     * 是否休息日
     *
     * @param dateString 日期 2019-01-01
     * @return boolean
     * @throws ParseException 异常
     */
    boolean isDayOff(String dateString) throws ParseException;


}

package com.huifer.planar.aset.algo.impl;

import com.huifer.planar.aset.algo.DateInterface;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>Title : DateInterfaceImpl </p>
 * <p>Description : DateInterface 具体实现</p>
 *
 * @author huifer
 * @date 2019-01-02
 */
public class DateInterfaceImpl implements DateInterface {

    /**
     * 匹配我设置的日期
     */
    private static Pattern MY_DATE_FORMAT = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
    /**
     * 放假
     */
    private List<String> lawHolidays = Arrays.asList("2019-01-01");
    /**
     * 额外工作日期
     */
    private List<String> extraWorkdays = Arrays.asList("2018-12-29");

    /**
     * 是否符合定义的日期规则
     */
    private static boolean isMyDateFormat(String dateString) {
        Matcher matcher = MY_DATE_FORMAT.matcher(dateString);
        boolean flag = matcher.matches();
        return flag;
    }

    @Override
    public List<String> monthlyCalendar(int year, int month) {
        List<String> res = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        int startDay = calendar.get(Calendar.DAY_OF_WEEK);
        int count = startDay - 1;
        int maxDay = maxDayInMonth(year, month);
        String week = "周日 周一 周二 周三 周四 周五 周六";
        for (int i = 1; i <= maxDay; i++) {
            res.add(
                    String.format("%d-%d-%d %s", year, month, i, week.split(" ")[count]));
            count++;
            if (count >= 7) {
                count = 0;
            }
        }
        return res;
    }

    @Override
    public int maxDayInMonth(int year, int month) {
        int max = 30;
        if (month == 1 | month == 3 | month == 5 | month == 7 | month == 8 | month == 10
                | month == 12) {

            max = 31;
        } else if (month == 2) {
            max = 28;
        } else if (month == 2 & (year % 4 == 0) && (year % 100 != 0) || (year % 400 == 0)) {
            max = 29;
        }
        return max;
    }


    @Override
    public boolean isWeekends(String dateString) throws ParseException {
        assert isMyDateFormat(dateString);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(dateString);
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        return ca.get(Calendar.DAY_OF_WEEK) == 1
                || ca.get(Calendar.DAY_OF_WEEK) == 7;
    }

    @Override
    public boolean isStatutoryHoliday(String dateString) {
        assert isMyDateFormat(dateString);
        return lawHolidays.contains(dateString);
    }

    @Override
    public boolean isExtraWorkingDay(String dateString) {
        assert isMyDateFormat(dateString);

        return extraWorkdays.contains(dateString);
    }

    @Override
    public boolean isDayOff(String dateString) throws ParseException {
        assert isMyDateFormat(dateString);
        // 法定节假日休息
        if (this.isStatutoryHoliday(dateString)) {
            return true;
        }
        // 从日常周末排除
        if (!this.isWeekends(dateString)) {
            return false;
        }
        // 额外工作日排除
        return !this.isExtraWorkingDay(dateString);
    }
}

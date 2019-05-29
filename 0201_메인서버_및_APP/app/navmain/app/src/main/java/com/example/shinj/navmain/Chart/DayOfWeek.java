package com.example.shinj.navmain.Chart;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/* @ 날짜를 생성자 인수로 받으면 해당하는 요일을 구해주는 클래스
* */
public class DayOfWeek {

    public String yoiL;
    public String date;

    public DayOfWeek() {
    }

    DayOfWeek(String date){
        this.date = date;
        this.yoiL = getDayOfWeek(this.date);
    }

    public static String getDayOfWeek(String date) {
        String day = "" ;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.KOREA);
        try {
            Date nDate = dateFormat.parse(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(nDate);
            int dayNum = cal.get(Calendar.DAY_OF_WEEK);
            switch (dayNum) {
                case 1:
                    day = "일";
                    break;
                case 2:
                    day = "월";
                    break;
                case 3:
                    day = "화";
                    break;
                case 4:
                    day = "수";
                    break;
                case 5:
                    day = "목";
                    break;
                case 6:
                    day = "금";
                    break;
                case 7:
                    day = "토";
                    break;
            }
        } catch (Exception e){

        }
        return day ;
    }

    public static String transformDateString(String dateString) {

        return dateString.substring(5); // 날짜 2019/xx/xx에서 년도는 빼고 return
    }

    public static String getTodayDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd", Locale.KOREA);
        String str_date = df.format(new Date());    // 예시: str_date=2019/12/22
        transformDateString(str_date);
        return str_date;
    }

    public static String calculDate(String date, int howManyDay) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Calendar c = Calendar.getInstance();
        try {
            Date d = sdf.parse(date);
            c.setTime(d);
            c.add(Calendar.DATE, howManyDay);
        }
        catch(ParseException e) {
            e.printStackTrace();
        }
        String targetDate = sdf.format(c.getTime());
        String targetYear = targetDate.substring(0, 4);
        String targetMonth = targetDate.substring(5, 7);
        String targetDay = targetDate.substring(8, 10);
        if(targetMonth.charAt(0) == '0') {
            targetMonth = targetMonth.substring(1,2);}
        if(targetDay.charAt(0) == '0') {
            targetDay = targetDay.substring(1,2);
        }
        targetDate = targetYear + "/" + targetMonth + "/" + targetDay;
        return targetDate;
    }

    public static String setDateOnWeeklyChart(String today, int howManyDay){
        return transformDateString(calculDate(today, howManyDay));
    }
}

package BuhlmannZHL16;

/**
 * Created by Jens on 27.12.2015.
 */
public class SystemDateTime {


    private final int year;
    private final int month;
    private final int day;
    private final int hour;
    private final int minute;
    private final int second;

    public SystemDateTime(int year, int month, int day, int hour, int minute, int second) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    public String toString(){
        return pad(day) + "." + pad(month) + "." + pad(year) + " " + pad(hour) + ":" + pad(minute) + ":" + pad(second);
    }

    private String pad(long value) {
        return value <10?"0"+ value : ""+value;
    }


    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }
}

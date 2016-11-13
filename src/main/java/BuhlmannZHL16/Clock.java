package BuhlmannZHL16;

import java.util.concurrent.*;

/**
 * Created by Jens on 26.12.2015.
 */
public class Clock {
    private long currentTimeStamp;
    private int [] daysUntilMonth           = new int[]{0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334};
    private int [] daysUntilMonthLeapYear   = new int[]{0, 31, 60, 91, 121, 152, 182, 213, 244, 274, 305, 335};

    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    public Clock(){
        ScheduledFuture timerTick = executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                currentTimeStamp++;
            }
        } ,0, 200, TimeUnit.MILLISECONDS);
    }


    public long startTimer(long timer){
        return currentTimeStamp;
    }

    public int getTimer(long timer){
        return (int) (currentTimeStamp-timer);
    }

    public int getTimerMiuntes(long timer){ return getTimer(timer)/60;}

    public int setTimeZone(int timeZoneHour){

        return 60*60*timeZoneHour;
    }

    public long getCurrentTime(){
        return currentTimeStamp;
    }


    private String pad(int toPad){
        String returnValue="";
        if (toPad < 10){
            returnValue = "0"+ toPad;
        }
        else{
            returnValue = "" + toPad;
        }
        return returnValue;
    }

    public void setCurrentTimeStamp(long timeStamp){
        currentTimeStamp = timeStamp;
    }

    public void setCurrentDateTime(int year, int month, int day, int hour, int minute, int second){
        setCurrentTimeStamp(dateTimeToUnixtime(new SystemDateTime(year, month, day, hour, minute, second)));
    }

    public long dateTimeToUnixtime(SystemDateTime dateTime){
//        int [] daysUntilMonth = new int[]{0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334};
        int days = (dateTime.getYear()-1970)*365 + numberOfLeapYears(dateTime.getYear()-1)+daysUntilMonth[dateTime.getMonth()-1]+dateTime.getDay()-1;
        if (dateTime.getMonth()>2 && isLeapYear(dateTime.getYear())){
            days++;
        }
        return dateTime.getSecond() + 60* dateTime.getMinute() + 3600 * dateTime.getHour() + 86400L * days;
    }

    public String getCurrentDateTimeString(int offset){
        return unixtimeToDateTime(currentTimeStamp, offset).toString();
    }

    public String getDateString(int timeZoneOffeset){
        return pad(unixtimeToDateTime(currentTimeStamp,timeZoneOffeset).getDay()) + "." + pad(unixtimeToDateTime(currentTimeStamp,timeZoneOffeset).getMonth()) + "." + pad(unixtimeToDateTime(currentTimeStamp,timeZoneOffeset).getYear());
    }

    public String getTimeString(int timeZoneOffeset){
        return pad(unixtimeToDateTime(currentTimeStamp,timeZoneOffeset).getHour()) + ":" + pad(unixtimeToDateTime(currentTimeStamp,timeZoneOffeset).getMinute()) + ":" + pad(unixtimeToDateTime(currentTimeStamp,timeZoneOffeset).getSecond());
    }

    public String getTimeStringNoSeconds(int timeZoneOffeset){
        return pad(unixtimeToDateTime(currentTimeStamp,timeZoneOffeset).getHour()) + ":" + pad(unixtimeToDateTime(currentTimeStamp,timeZoneOffeset).getMinute());
    }


    private boolean isLeapYear(int year){
        return (year % 4 == 0) && (year % 100 != 0) || (year % 400 == 0);
    }

    private int numberOfLeapYears(int year){
        return (year/4 - year/100 + year/400) - (1970/4 - 1970/100 + 1970/400);
    }

    public SystemDateTime unixtimeToDateTime(long unixtimeStamp, int offset){

        long timeStamp=unixtimeStamp+offset;

        int second =  (int) (timeStamp % 60);
        int minute = (int) (((timeStamp) % 3600)/60);
        int hour = (int) (((timeStamp)%(24*60*60))/3600);

        int days = (int) (timeStamp/(24*60*60));
        int year = (int) days/365 + 1970;

        int dayOfYear = (int) days % 365 - numberOfLeapYears(year-1);

        while (dayOfYear<0){
            year--;
            dayOfYear = (int) days - ((year-1970) * 365 + numberOfLeapYears(year-1));
        }


        int month = 0;
        int day = 0;

        if(isLeapYear(year)){
            while (month<daysUntilMonthLeapYear.length && dayOfYear>=daysUntilMonthLeapYear[month]){
                month++;
            }
            day = dayOfYear-daysUntilMonthLeapYear[month-1];
        }
        else{
            while (month<daysUntilMonth.length && dayOfYear>=daysUntilMonth[month]){
                month++;
            }
            day = dayOfYear-daysUntilMonth[month-1];
        }

        return new SystemDateTime(year, month, day+1, hour, minute, second);
    }


}

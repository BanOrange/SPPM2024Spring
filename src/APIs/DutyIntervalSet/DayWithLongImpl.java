package APIs.DutyIntervalSet;

import java.text.DateFormat;

import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDate;

import java.time.temporal.ChronoUnit;

public class DayWithLongImpl implements DayWithLong{
    @Override
    public long getInterval(String day1, String day2) throws DateTimeException {
        LocalDate start = LocalDate.parse(day1);
        LocalDate end = LocalDate.parse(day2);
        long interval = start.until(end, ChronoUnit.DAYS);

            if(interval<0){
                return -1;
            }else{
                return interval;
            }
    }

    @Override
    public String addDays(String day, long length) throws DateTimeException{
        DateFormat DFT = new SimpleDateFormat("yyyy-MM-dd");
        if(length<0){
            return "";
        }
        int i = 0;
        LocalDate start = LocalDate.parse(day);
        while(i<length){
            start = start.plusDays(1);
            i++;
        }
        return start.toString();
    }
}

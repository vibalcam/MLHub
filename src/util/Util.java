package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Util {
    private static final SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd");

    public static Calendar parseDate(String htmlDate) throws ParseException {
        if(htmlDate == null)
            return null;
        else {
            Calendar calendar = Calendar.getInstance();
           calendar.setTime(dateParser.parse(htmlDate));
           return calendar;
        }
    }
}

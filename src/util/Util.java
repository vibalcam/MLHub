package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Util {
    private static final SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd");

    public static Calendar parseDate(String htmlDate) throws ParseException {
        if (htmlDate == null || htmlDate.isBlank())
            return null;
        else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateParser.parse(htmlDate));
            return calendar;
        }
    }
}

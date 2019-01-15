package com.example.client;

import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Simple age service which returns age depend on the the date of birth.
 * This will register it self with the eureka server.
 */
@RestController
public class AgeCheckController {

    @RequestMapping(value = "/age", method = RequestMethod.GET)
    public int getCurrentAge(@RequestParam String dob) {
        int diff = -1;

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date = format.parse(dob);
            Date current  = new Date();
            Calendar a = getCalendar(date);
            Calendar b = getCalendar(current);
            diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);
            if (a.get(Calendar.MONTH) > b.get(Calendar.MONTH) ||
                    (a.get(Calendar.MONTH) == b.get(Calendar.MONTH) && a.get(Calendar.DATE) > b.get(Calendar.DATE))) {
                diff--;
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return diff;
    }

    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        return cal;
    }
}


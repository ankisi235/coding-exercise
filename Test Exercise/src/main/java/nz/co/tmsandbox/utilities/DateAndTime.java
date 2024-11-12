package nz.co.tmsandbox.utilities;

import nz.co.tmsandbox.webinteractivities.DebugMessageLogger;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateAndTime {


     /**
     * Creates date and time in the specific format
     *
     * @param desiredDateTime desired date, supports  now+/-2d, now+/-2m, now+/-2y, for current date now+0d
     * @param dateTimeFormat  specific the date time format, supports all the java date time formats
     * @return specified date in the specified format
     */
    public String createDateTime(String desiredDateTime, String dateTimeFormat) {
        LocalDateTime currDt = LocalDateTime.now(ZoneId.of("Pacific/Auckland"));
        LocalDateTime calculatedDt = null;
        String calculatedDateTime;
        if (desiredDateTime.equalsIgnoreCase("now"))
            calculatedDateTime = formatDateTime(currDt, dateTimeFormat);
        else {
            char operation = desiredDateTime.charAt(3);
            char span = desiredDateTime.charAt(desiredDateTime.length() - 1);
            String count = desiredDateTime.replace("now", "").replace(Character.toString(operation), "").replace(Character.toString(span), "");
            switch (Character.toString(span)) {
                case "d":
                    if (Character.toString(operation).equals("+"))
                        calculatedDt = currDt.plusDays(Long.parseLong(count));
                    if (Character.toString(operation).equals("-"))
                        calculatedDt = currDt.minusDays(Long.parseLong(count));
                    break;
                case "M":
                    if (Character.toString(operation).equals("+"))
                        calculatedDt = currDt.plusMonths(Long.parseLong(count));
                    if (Character.toString(operation).equals("-"))
                        calculatedDt = currDt.minusMonths(Long.parseLong(count));
                    break;
                case "y":
                    if (Character.toString(operation).equals("+"))
                        calculatedDt = currDt.plusYears(Long.parseLong(count));
                    if (Character.toString(operation).equals("-"))
                        calculatedDt = currDt.minusYears(Long.parseLong(count));
                    break;
                case "H":
                    if (Character.toString(operation).equals("+"))
                        calculatedDt = currDt.plusHours(Long.parseLong(count));
                    if (Character.toString(operation).equals("-"))
                        calculatedDt = currDt.minusHours(Long.parseLong(count));
                    break;
                case "m":
                    if (Character.toString(operation).equals("+"))
                        calculatedDt = currDt.plusMinutes(Long.parseLong(count));
                    if (Character.toString(operation).equals("-"))
                        calculatedDt = currDt.minusMinutes(Long.parseLong(count));
                    break;
                default:
                    DebugMessageLogger.debugMessageLogger.logInformation("Please enter desired date in the format: now+2d, now-2m or now+2y");
            }
            calculatedDateTime = formatDateTime(calculatedDt, dateTimeFormat);
        }


        DebugMessageLogger.debugMessageLogger.logInformation("Desired format: " + desiredDateTime + " Value:" + calculatedDateTime);
        return calculatedDateTime;
    }

    public String formatDateTime(LocalDateTime dt, String dtFormat) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dtFormat);
        return dt.format(dateTimeFormatter);
    }

}

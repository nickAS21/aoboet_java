package com.dcloud.zxing.client.result;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.dcloud.common.adapter.util.Logger;

/* loaded from: classes.dex */
public final class CalendarParsedResult extends ParsedResult {
    private static final DateFormat DATE_FORMAT;
    private static final DateFormat DATE_TIME_FORMAT;
    private final String[] attendees;
    private final String description;
    private final Date end;
    private final boolean endAllDay;
    private final double latitude;
    private final String location;
    private final double longitude;
    private final String organizer;
    private final Date start;
    private final boolean startAllDay;
    private final String summary;
    private static final Pattern RFC2445_DURATION = Pattern.compile("P(?:(\\d+)W)?(?:(\\d+)D)?(?:T(?:(\\d+)H)?(?:(\\d+)M)?(?:(\\d+)S)?)?");
    private static final long[] RFC2445_DURATION_FIELD_UNITS = {604800000, 86400000, 3600000, 60000, 1000};
    private static final Pattern DATE_TIME = Pattern.compile("[0-9]{8}(T[0-9]{6}Z?)?");

    static {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Logger.TIMESTAMP_YYYY_MM_DD, Locale.ENGLISH);
        DATE_FORMAT = simpleDateFormat;
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        DATE_TIME_FORMAT = new SimpleDateFormat("yyyyMMdd'T'HHmmss", Locale.ENGLISH);
    }

    public CalendarParsedResult(String str, String str2, String str3, String str4, String str5, String str6, String[] strArr, String str7, double d, double d2) {
        super(ParsedResultType.CALENDAR);
        this.summary = str;
        try {
            Date parseDate = parseDate(str2);
            this.start = parseDate;
            if (str3 == null) {
                long parseDurationMS = parseDurationMS(str4);
                this.end = parseDurationMS < 0 ? null : new Date(parseDate.getTime() + parseDurationMS);
            } else {
                try {
                    this.end = parseDate(str3);
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e.toString());
                }
            }
            this.startAllDay = str2.length() == 8;
            this.endAllDay = str3 != null && str3.length() == 8;
            this.location = str5;
            this.organizer = str6;
            this.attendees = strArr;
            this.description = str7;
            this.latitude = d;
            this.longitude = d2;
        } catch (ParseException e2) {
            throw new IllegalArgumentException(e2.toString());
        }
    }

    public String getSummary() {
        return this.summary;
    }

    public Date getStart() {
        return this.start;
    }

    public boolean isStartAllDay() {
        return this.startAllDay;
    }

    public Date getEnd() {
        return this.end;
    }

    public boolean isEndAllDay() {
        return this.endAllDay;
    }

    public String getLocation() {
        return this.location;
    }

    public String getOrganizer() {
        return this.organizer;
    }

    public String[] getAttendees() {
        return this.attendees;
    }

    public String getDescription() {
        return this.description;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    @Override // com.dcloud.zxing.client.result.ParsedResult
    public String getDisplayResult() {
        StringBuilder sb = new StringBuilder(100);
        maybeAppend(this.summary, sb);
        maybeAppend(format(this.startAllDay, this.start), sb);
        maybeAppend(format(this.endAllDay, this.end), sb);
        maybeAppend(this.location, sb);
        maybeAppend(this.organizer, sb);
        maybeAppend(this.attendees, sb);
        maybeAppend(this.description, sb);
        return sb.toString();
    }

    private static Date parseDate(String str) throws ParseException {
        if (!DATE_TIME.matcher(str).matches()) {
            throw new ParseException(str, 0);
        }
        if (str.length() == 8) {
            return DATE_FORMAT.parse(str);
        }
        if (str.length() == 16 && str.charAt(15) == 'Z') {
            Date parse = DATE_TIME_FORMAT.parse(str.substring(0, 15));
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            long time = parse.getTime() + gregorianCalendar.get(15);
            gregorianCalendar.setTime(new Date(time));
            return new Date(time + gregorianCalendar.get(16));
        }
        return DATE_TIME_FORMAT.parse(str);
    }

    private static String format(boolean z, Date date) {
        if (date == null) {
            return null;
        }
        return (z ? DateFormat.getDateInstance(2) : DateFormat.getDateTimeInstance(2, 2)).format(date);
    }

    private static long parseDurationMS(CharSequence charSequence) {
        if (charSequence == null) {
            return -1L;
        }

        Matcher matcher = RFC2445_DURATION.matcher(charSequence);
        if (!matcher.matches()) {
            return -1L;
        }

        long result = 0;
        for (int i = 0; i < RFC2445_DURATION_FIELD_UNITS.length; i++) {
            String group = matcher.group(i + 1);
            if (group != null) {
                result += RFC2445_DURATION_FIELD_UNITS[i] * Integer.parseInt(group);
            }
        }

        return result;
    }

}

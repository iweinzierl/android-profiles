package de.iweinzierl.easyprofiles.util.time;

import android.content.Context;

import de.iweinzierl.easyprofiles.R;
import de.iweinzierl.easyprofiles.domain.Day;

public final class DayOfWeekHelper {

    private final Context context;

    public DayOfWeekHelper(Context context) {
        this.context = context;
    }

    public String getShortnameOfDay(Day day) {
        switch (day) {
            case MONDAY:
                return context.getString(R.string.monday_short);
            case TUESDAY:
                return context.getString(R.string.tuesday_short);
            case WEDNESDAY:
                return context.getString(R.string.wednesday_short);
            case THURSDAY:
                return context.getString(R.string.thursday_short);
            case FRIDAY:
                return context.getString(R.string.friday_short);
            case SATURDAY:
                return context.getString(R.string.saturday_short);
            case SUNDAY:
                return context.getString(R.string.sunday_short);
            default:
                return "unknown";
        }
    }

    public String getNameOfDay(Day day) {
        switch (day) {
            case MONDAY:
                return context.getString(R.string.monday);
            case TUESDAY:
                return context.getString(R.string.tuesday);
            case WEDNESDAY:
                return context.getString(R.string.wednesday);
            case THURSDAY:
                return context.getString(R.string.thursday);
            case FRIDAY:
                return context.getString(R.string.friday);
            case SATURDAY:
                return context.getString(R.string.saturday);
            case SUNDAY:
                return context.getString(R.string.sunday);
            default:
                return "unknown";
        }
    }
}

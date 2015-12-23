package de.iweinzierl.easyprofiles.util.time;

import org.joda.time.DateTime;

import de.iweinzierl.easyprofiles.domain.TimeBasedTrigger;

public class TimeBasedRequestCodeGenerator {

    private static final String TIME_FORMAT = "HHmm";
    private static final String REQUEST_CODE_TEMPLATE = "%s%s";

    private final TimeBasedTrigger trigger;

    public TimeBasedRequestCodeGenerator(TimeBasedTrigger trigger) {
        this.trigger = trigger;
    }

    public int createActivationRequestCode() {
        int time = transformTimeToInt(trigger.getActivationTime().toDateTimeToday());
        return Integer.valueOf(String.format(REQUEST_CODE_TEMPLATE, trigger.getId(), time));
    }

    public int createDeactivationRequestCode() {
        int time = transformTimeToInt(trigger.getDeactivationTime().toDateTimeToday());
        return -1 * Integer.valueOf(String.format(REQUEST_CODE_TEMPLATE, trigger.getId(), time));
    }

    private int transformTimeToInt(DateTime time) {
        return Integer.valueOf(time.toString(TIME_FORMAT));
    }
}

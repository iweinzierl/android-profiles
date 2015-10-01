package de.iweinzierl.easyprofiles.domain;

public enum Day {

    SUNDAY(0),
    MONDAY(1),
    TUESDAY(2),
    WEDNESDAY(3),
    THURSDAY(4),
    FRIDAY(5),
    SATURDAY(6);

    private int dayOfWeek;

    private Day(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
}

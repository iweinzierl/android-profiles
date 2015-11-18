package de.iweinzierl.easyprofiles.domain;

public class TimePeriod {

    private int period;
    private TimeUnitExt unit;

    public TimePeriod(int period, TimeUnitExt unit) {
        this.period = period;
        this.unit = unit;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public TimeUnitExt getUnit() {
        return unit;
    }

    public void setUnit(TimeUnitExt unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "TimePeriod{" +
                "period=" + period +
                ", unit=" + unit +
                '}';
    }
}

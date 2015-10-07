package de.iweinzierl.easyprofiles.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.collect.Lists;

import java.util.HashSet;
import java.util.Set;

import de.iweinzierl.easyprofiles.R;
import de.iweinzierl.easyprofiles.domain.Day;

public class SelectableWeekDaysAdapter extends ListAdapter<Day> {

    public static final java.util.ArrayList<Day> WEEK_DAYS = Lists.newArrayList(
            Day.MONDAY, Day.TUESDAY, Day.WEDNESDAY, Day.THURSDAY, Day.FRIDAY, Day.SATURDAY, Day.SUNDAY);

    private final Set<Day> selected = new HashSet<>();

    public SelectableWeekDaysAdapter(Context context) {
        super(context, WEEK_DAYS);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Day day = (Day) getItem(i);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View container = inflater.inflate(R.layout.list_item_selectable_day, null);

        final TextView dayView = (TextView) container.findViewById(R.id.day);
        setDayText(day, dayView);

        final ImageView checkButton = (ImageView) container.findViewById(R.id.check_btn);
        checkButton.setOnClickListener(new DaySelectionListener(day, checkButton, dayView).invoke());

        return container;
    }

    public Set<Day> getSelectedDays() {
        return selected;
    }

    private void setDayText(Day day, TextView dayView) {
        switch (day) {
            case MONDAY:
                dayView.setText(R.string.monday);
                break;
            case TUESDAY:
                dayView.setText(R.string.tuesday);
                break;
            case WEDNESDAY:
                dayView.setText(R.string.wednesday);
                break;
            case THURSDAY:
                dayView.setText(R.string.thursday);
                break;
            case FRIDAY:
                dayView.setText(R.string.friday);
                break;
            case SATURDAY:
                dayView.setText(R.string.saturday);
                break;
            case SUNDAY:
                dayView.setText(R.string.sunday);
                break;
        }
    }

    private class DaySelectionListener {
        private final Day day;
        private final ImageView checkButton;
        private final TextView dayView;

        public DaySelectionListener(Day day, ImageView checkButton, TextView dayView) {
            this.day = day;
            this.checkButton = checkButton;
            this.dayView = dayView;
        }

        public View.OnClickListener invoke() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Resources resources = context.getResources();

                    int colorSelected = resources.getColor(R.color.ColorPrimary);
                    int colorUnselected = resources.getColor(R.color.ColorSecondary);

                    Drawable iconSelected = resources.getDrawable(R.drawable.ic_check_green_48px, context.getTheme());
                    Drawable iconUnselected = resources.getDrawable(R.drawable.ic_check_grey_48px, context.getTheme());

                    if (selected.contains(day)) {
                        dayView.setTextColor(colorUnselected);
                        dayView.setTypeface(null, Typeface.NORMAL);
                        checkButton.setImageDrawable(iconUnselected);
                        selected.remove(day);
                    } else {
                        dayView.setTextColor(colorSelected);
                        dayView.setTypeface(null, Typeface.BOLD);
                        checkButton.setImageDrawable(iconSelected);
                        selected.add(day);
                    }
                }
            };
        }
    }
}

package de.iweinzierl.easyprofiles.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.inselhome.android.utils.UiUtils;
import de.iweinzierl.easyprofiles.R;
import de.iweinzierl.easyprofiles.domain.TimePeriod;

public class TimeRangeAdapter extends ListAdapter<TimePeriod> {

    public TimeRangeAdapter(Context context, List<TimePeriod> items) {
        super(context, items);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item = inflater.inflate(R.layout.spinner_item_timerange, viewGroup, false);

        TimePeriod timePeriod = (TimePeriod) getItem(i);

        TextView period = UiUtils.getGeneric(TextView.class, item, R.id.period);
        TextView unit = UiUtils.getGeneric(TextView.class, item, R.id.unit);

        period.setText(String.valueOf(timePeriod.getPeriod()));
        unit.setText(timePeriod.getUnit().name());

        return item;
    }
}

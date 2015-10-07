package de.iweinzierl.easyprofiles.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.Set;

import de.iweinzierl.easyprofiles.R;
import de.iweinzierl.easyprofiles.adapter.SelectableWeekDaysAdapter;
import de.iweinzierl.easyprofiles.domain.Day;


public class SelectableWeekDayListFragment extends Fragment {

    private ListView dayList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selectable_days, container, false);

        dayList = (ListView) view.findViewById(R.id.day_list);
        dayList.setAdapter(new SelectableWeekDaysAdapter(getActivity()));

        return view;
    }

    public Set<Day> getSelectedDays() {
        return ((SelectableWeekDaysAdapter) dayList.getAdapter()).getSelectedDays();
    }
}

package de.iweinzierl.easyprofiles.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import de.iweinzierl.easyprofiles.R;
import de.iweinzierl.easyprofiles.adapter.TriggerAdapter;
import de.iweinzierl.easyprofiles.persistence.Trigger;

public class TriggerListFragment extends Fragment {

    private ListView triggerList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trigger_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        triggerList = (ListView) view.findViewById(R.id.wifi_list);
    }

    public void setTriggers(List<Trigger> triggers) {
        triggerList.setAdapter(new TriggerAdapter(getActivity(), triggers));
    }
}

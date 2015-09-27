package de.iweinzierl.easyprofiles.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import de.iweinzierl.easyprofiles.R;
import de.iweinzierl.easyprofiles.adapter.TriggerTypeAdapter;
import de.iweinzierl.easyprofiles.persistence.TriggerType;

public class TriggerTypeListFragment extends Fragment {

    public interface Callback {
        void onTriggerTypeClicked(TriggerType triggerType);
    }

    private Callback callback;

    private ListView triggerTypeList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trigger_type_selection, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        triggerTypeList = (ListView) view.findViewById(R.id.trigger_type_list);
        triggerTypeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (callback != null) {
                    TriggerType triggerType = (TriggerType) adapterView.getAdapter().getItem(i);
                    callback.onTriggerTypeClicked(triggerType);
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        triggerTypeList.setAdapter(new TriggerTypeAdapter(getActivity()));

        Activity activity = getActivity();
        if (activity instanceof Callback) {
            callback = (Callback) activity;
        }
    }
}

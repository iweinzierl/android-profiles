package de.iweinzierl.easyprofiles.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import de.iweinzierl.easyprofiles.R;
import de.iweinzierl.easyprofiles.adapter.WifiSSIDAdapter;

public class WifiListFragment extends Fragment {

    public interface Callback {
        void onWifiSelected(String ssid);
    }

    private Callback callback;
    private ListView wifiList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wifi_list, container, false);

        wifiList = (ListView) view.findViewById(R.id.wifi_list);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View emptyView = view.findViewById(R.id.list_empty_view);

        wifiList = (ListView) view.findViewById(R.id.wifi_list);
        wifiList.setEmptyView(emptyView);
        wifiList.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        wifiList.setSelector(R.color.ColorPrimary);
        wifiList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapterView.setSelection(i);

                String ssid = (String) adapterView.getAdapter().getItem(i);

                if (callback != null) {
                    callback.onWifiSelected(ssid);
                }
            }
        });
    }

    public void setWifiSSIDs(List<String> ssids) {
        wifiList.setAdapter(new WifiSSIDAdapter(getActivity(), ssids));
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }
}

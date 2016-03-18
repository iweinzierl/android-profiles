package de.iweinzierl.easyprofiles.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

import java.util.ArrayList;
import java.util.List;

import de.iweinzierl.easyprofiles.R;
import de.iweinzierl.easyprofiles.adapter.TriggerCardAdapter;
import de.iweinzierl.easyprofiles.domain.LocationBasedTrigger;
import de.iweinzierl.easyprofiles.domain.TimeBasedTrigger;
import de.iweinzierl.easyprofiles.domain.Trigger;
import de.iweinzierl.easyprofiles.domain.WifiBasedTrigger;
import de.iweinzierl.easyprofiles.persistence.PersistentTrigger;

public class TriggerListFragment extends Fragment {

    public interface Callback {
        void onTriggerEnabled(PersistentTrigger persistentTrigger);

        void onTriggerDisabled(PersistentTrigger persistentTrigger);

        void onTriggerRemoved(PersistentTrigger persistentTrigger);
    }

    private RecyclerView triggerListView;
    private Callback callback;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trigger_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        triggerListView = (RecyclerView) view.findViewById(R.id.trigger_list);
        triggerListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        triggerListView.setHasFixedSize(false);

        ItemTouchHelper touchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(0, ItemTouchHelper.RIGHT);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                Trigger trigger = ((TriggerCardAdapter.TriggerHolder) viewHolder).getTrigger();
                callback.onTriggerRemoved(trigger.export());
            }
        });

        touchHelper.attachToRecyclerView(triggerListView);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (getActivity() instanceof Callback) {
            callback = (Callback) getActivity();
        }
    }

    public void setTriggers(List<PersistentTrigger> persistentTriggers) {
        final TriggerCardAdapter adapter = new TriggerCardAdapter(
                getActivity(),
                new ArrayList<>(Collections2.transform(persistentTriggers, new Function<PersistentTrigger, Trigger>() {
                            @Override
                            public Trigger apply(@Nullable PersistentTrigger input) {
                                if (input == null) {
                                    return null;
                                }

                                switch (input.getType()) {
                                    case LOCATION_BASED:
                                        LocationBasedTrigger lt = new LocationBasedTrigger();
                                        lt.apply(input);
                                        return lt;
                                    case TIME_BASED:
                                        TimeBasedTrigger tt = new TimeBasedTrigger();
                                        tt.apply(input);
                                        return tt;
                                    case WIFI:
                                        WifiBasedTrigger wt = new WifiBasedTrigger();
                                        wt.apply(input);
                                        return wt;
                                }

                                return null;
                            }
                        }
                )));

        adapter.setOnItemClickListener(new TriggerCardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Trigger trigger) {
                if (trigger.isEnabled()) {
                    callback.onTriggerDisabled(trigger.export());
                } else {
                    callback.onTriggerEnabled(trigger.export());
                }
            }
        });
        triggerListView.setAdapter(adapter);
    }
}

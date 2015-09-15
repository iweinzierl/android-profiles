package de.iweinzierl.easyprofiles.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.hudomju.swipe.SwipeToDismissTouchListener;
import com.hudomju.swipe.adapter.ListViewAdapter;

import java.util.List;

import de.iweinzierl.easyprofiles.R;
import de.iweinzierl.easyprofiles.adapter.TriggerAdapter;
import de.iweinzierl.easyprofiles.persistence.Trigger;

public class TriggerListFragment extends Fragment {

    public interface Callback {
        void onTriggerEnabled(Trigger trigger);

        void onTriggerDisabled(Trigger trigger);

        void onTriggerRemoved(Trigger trigger);
    }

    private ListView triggerList;
    private Callback callback;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trigger_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        triggerList = (ListView) view.findViewById(R.id.trigger_list);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof Callback) {
            callback = (Callback) activity;
        }
    }

    public void setTriggers(List<Trigger> triggers) {
        final TriggerAdapter adapter = new TriggerAdapter(getActivity(), triggers, new TriggerAdapter.Callback() {
            @Override
            public void onTriggerEnabled(Trigger trigger) {
                if (callback != null) {
                    callback.onTriggerEnabled(trigger);
                }
            }

            @Override
            public void onTriggerDisabled(Trigger trigger) {
                if (callback != null) {
                    callback.onTriggerDisabled(trigger);
                }
            }
        });
        triggerList.setAdapter(adapter);

        setupSwipeToDismiss();
    }

    private void setupSwipeToDismiss() {
        final SwipeToDismissTouchListener<ListViewAdapter> touchListener =
                new SwipeToDismissTouchListener<>(
                        new ListViewAdapter(triggerList),
                        new SwipeToDismissTouchListener.DismissCallbacks<ListViewAdapter>() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListViewAdapter view, int position) {
                                callback.onTriggerRemoved((Trigger) triggerList.getAdapter().getItem(position));
                            }
                        });

        triggerList.setOnTouchListener(touchListener);
        triggerList.setOnScrollListener((AbsListView.OnScrollListener) touchListener.makeScrollListener());
    }
}

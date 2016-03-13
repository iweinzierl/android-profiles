package de.iweinzierl.easyprofiles.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orm.SugarRecord;

import java.util.List;

import de.iweinzierl.easyprofiles.R;
import de.iweinzierl.easyprofiles.adapter.ModifiableProfileCardAdapter;
import de.iweinzierl.easyprofiles.persistence.Profile;

public class ProfileListFragment extends Fragment implements ModifiableProfileCardAdapter.OnItemEditListener, ModifiableProfileCardAdapter.OnItemClickListener {

    public interface Callback {
        void onProfileClick(Profile profile);

        void onProfileModify(Profile profile);

        void onProfileRemoved(Profile profile);
    }

    private Callback callback;
    private RecyclerView profileListView;

    public ProfileListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_cards, container, false);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profileListView = (RecyclerView) view.findViewById(R.id.profile_list);
        profileListView.setHasFixedSize(false);
        profileListView.setLayoutManager(new LinearLayoutManager(getActivity()));

        final ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView profileListView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                ModifiableProfileCardAdapter.ViewHolder holder = (ModifiableProfileCardAdapter.ViewHolder) viewHolder;
                Profile profile = holder.getProfile();

                if (SugarRecord.delete(profile)) {
                    fireProfileRemoved(profile);
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(profileListView);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity = getActivity();
        if (activity instanceof Callback) {
            callback = (Callback) activity;
        }
    }

    @Override
    public void onItemClick(Profile profile) {
        callback.onProfileClick(profile);
    }

    @Override
    public void onItemEdit(Profile profile) {
        callback.onProfileModify(profile);
    }

    public void setProfiles(List<Profile> profiles, boolean modifiable) {
        ModifiableProfileCardAdapter profileCardAdapter = new ModifiableProfileCardAdapter(getActivity(), null);
        profileCardAdapter.setOnItemClickListener(this);
        profileCardAdapter.setOnItemEditListener(this);
        profileListView.setAdapter(profileCardAdapter);
        profileCardAdapter.setItems(profiles);
    }

    private void fireProfileRemoved(Profile profile) {
        if (callback != null) {
            callback.onProfileRemoved(profile);
        }
    }
}

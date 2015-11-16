package de.iweinzierl.easyprofiles.navigation;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import de.iweinzierl.easyprofiles.LogDisplayActivity;
import de.iweinzierl.easyprofiles.ProfileListActivity;
import de.iweinzierl.easyprofiles.R;
import de.iweinzierl.easyprofiles.TriggerListActivity;

public class NavigationClickListener implements AdapterView.OnItemClickListener {

    private final Context context;

    public NavigationClickListener(Context context) {
        this.context = context;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        int item = (int) adapterView.getAdapter().getItem(i);

        switch (item) {
            case R.string.activity_profilelist:
                context.startActivity(new Intent(context, ProfileListActivity.class));
                break;
            case R.string.activity_triggerlist:
                context.startActivity(new Intent(context, TriggerListActivity.class));
                break;
            case R.string.activity_logs:
                context.startActivity(new Intent(context, LogDisplayActivity.class));
        }
    }
}

package de.iweinzierl.easyprofiles.navigation;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.collect.Lists;

import java.util.List;

import de.iweinzierl.easyprofiles.R;
import de.iweinzierl.easyprofiles.adapter.ListAdapter;

public class NavigationAdapter extends ListAdapter<Integer> {

    private static final List<Integer> ITEMS = Lists.newArrayList(
            R.string.navigation_header,
            R.string.activity_profilelist,
            R.string.activity_triggerlist,
            R.string.activity_locationlist,
            R.string.activity_logs
    );

    public NavigationAdapter(Context context) {
        super(context, ITEMS);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (i == 0) {
            return setupHeader(view, viewGroup);
        } else {
            return setupItem(i, view, viewGroup);
        }
    }

    private View setupHeader(View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.nav_drawer_header, null);
    }

    private View setupItem(int i, View view, ViewGroup viewGroup) {
        int navItem = (int) getItem(i);

        Resources res = context.getResources();
        Resources.Theme theme = context.getTheme();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.list_item_navigation, null);

        TextView tv = (TextView) view.findViewById(R.id.title);
        tv.setText(navItem);

        ImageView iv = (ImageView) view.findViewById(R.id.icon);
        switch (navItem) {
            case R.string.activity_triggerlist:
                iv.setImageDrawable(res.getDrawable(R.drawable.ic_schedule_black_36dp, theme));
                break;
            case R.string.activity_profilelist:
                iv.setImageDrawable(res.getDrawable(R.drawable.ic_group_black_36dp, theme));
                break;
            case R.string.activity_logs:
                iv.setImageDrawable(res.getDrawable(R.drawable.ic_group_black_36dp, theme));
                break;
            case R.string.activity_locationlist:
                iv.setImageDrawable(res.getDrawable(R.drawable.ic_location_on_black_36dp, theme));
                break;
        }

        return view;
    }
}

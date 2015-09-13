package de.iweinzierl.easyprofiles.navigation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.common.collect.Lists;

import java.util.List;

import de.iweinzierl.easyprofiles.R;
import de.iweinzierl.easyprofiles.adapter.ListAdapter;

public class NavigationAdapter extends ListAdapter<Integer> {

    private static final List<Integer> ITEMS = Lists.newArrayList(
            R.string.activity_profilelist,
            R.string.activity_triggerlist
    );

    public NavigationAdapter(Context context) {
        super(context, ITEMS);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        int navItem = (int) getItem(i);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.list_item_navigation, null);

        TextView tv = (TextView) view.findViewById(R.id.title);
        tv.setText(navItem);

        return view;
    }
}

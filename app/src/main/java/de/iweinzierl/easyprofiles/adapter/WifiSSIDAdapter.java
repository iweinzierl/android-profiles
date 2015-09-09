package de.iweinzierl.easyprofiles.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import de.iweinzierl.easyprofiles.R;

public class WifiSSIDAdapter extends BaseAdapter {

    private final Context context;
    private final List<String> ssids;

    public WifiSSIDAdapter(Context context, List<String> ssids) {
        this.context = context;
        this.ssids = ssids;
    }

    @Override
    public int getCount() {
        return ssids.size();
    }

    @Override
    public Object getItem(int i) {
        return ssids.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View layout = inflater.inflate(R.layout.list_item_wifissid, null);
        TextView textView = (TextView) layout.findViewById(R.id.ssid);
        textView.setText(((String) getItem(i)).replace("\"", ""));

        return layout;
    }
}

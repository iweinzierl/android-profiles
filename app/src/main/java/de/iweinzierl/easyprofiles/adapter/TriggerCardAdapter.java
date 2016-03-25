package de.iweinzierl.easyprofiles.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.iweinzierl.easyprofiles.R;
import de.iweinzierl.easyprofiles.domain.Day;
import de.iweinzierl.easyprofiles.domain.LocationBasedTrigger;
import de.iweinzierl.easyprofiles.domain.TimeBasedTrigger;
import de.iweinzierl.easyprofiles.domain.Trigger;
import de.iweinzierl.easyprofiles.domain.WifiBasedTrigger;
import de.iweinzierl.easyprofiles.util.time.DayOfWeekHelper;
import de.iweinzierl.easyprofiles.widget.recyclerview.Removable;

public class TriggerCardAdapter extends RecyclerView.Adapter<TriggerCardAdapter.TriggerHolder> implements Removable {

    private final int TYPE_WIFI = 1;
    private final int TYPE_LOCATION = 2;
    private final int TYPE_TIME = 3;
    private final int TYPE_UNKNOWN = 4;

    public interface OnItemClickListener {
        void onItemClick(Trigger trigger);
    }

    public interface OnItemRemoveListener {
        void onItemRemove(Trigger trigger);
    }

    public abstract class TriggerHolder extends RecyclerView.ViewHolder {
        protected final CardView cardView;
        protected final View enabledView;
        protected Trigger trigger;

        public TriggerHolder(View itemView) {
            super(itemView);
            this.cardView = (CardView) itemView.findViewById(R.id.card_view);
            this.enabledView = itemView.findViewById(R.id.enabled);
        }

        public void setTrigger(Trigger trigger) {
            this.trigger = trigger;
            if (trigger.isEnabled()) {
                enabledView.setVisibility(View.VISIBLE);
            } else {
                enabledView.setVisibility(View.GONE);
            }
        }

        public Trigger getTrigger() {
            return trigger;
        }

        public void setOnItemClickListener(final OnItemClickListener onItemClickListener) {
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(trigger);
                    }

                    if (trigger != null && !trigger.isEnabled()) {
                        enabledView.setVisibility(View.VISIBLE);
                        trigger.setEnabled(true);
                    } else if (trigger != null) {
                        enabledView.setVisibility(View.GONE);
                        trigger.setEnabled(false);
                    }
                }
            });
        }
    }

    public class WifiViewHolder extends TriggerHolder {
        protected final TextView ssidView;
        protected final TextView connectView;
        protected final TextView disconnectView;

        public WifiViewHolder(View itemView) {
            super(itemView);
            this.ssidView = (TextView) itemView.findViewById(R.id.ssid);
            this.connectView = (TextView) itemView.findViewById(R.id.connect);
            this.disconnectView = (TextView) itemView.findViewById(R.id.disconnect);
        }

        @Override
        public void setTrigger(Trigger trigger) {
            super.setTrigger(trigger);

            if (!(trigger instanceof WifiBasedTrigger)) {
                throw new IllegalArgumentException(
                        "Only WifiBasedTrigger allowed, but got " + trigger.getClass().getSimpleName());
            }

            ssidView.setText(((WifiBasedTrigger) trigger).getSsid().replaceAll("\"", ""));

            if (trigger.getOnActivateProfile() != null) {
                connectView.setText(trigger.getOnActivateProfile().getName());
            }

            if (trigger.getOnDeactivateProfile() != null) {
                disconnectView.setText(trigger.getOnDeactivateProfile().getName());
            }
        }
    }

    public class LocationViewHolder extends TriggerHolder {
        protected final TextView locationView;
        protected final TextView connectView;
        protected final TextView disconnectView;

        public LocationViewHolder(View itemView) {
            super(itemView);
            this.locationView = (TextView) itemView.findViewById(R.id.location);
            this.connectView = (TextView) itemView.findViewById(R.id.connect);
            this.disconnectView = (TextView) itemView.findViewById(R.id.disconnect);
        }

        @Override
        public void setTrigger(Trigger trigger) {
            super.setTrigger(trigger);

            if (!(trigger instanceof LocationBasedTrigger)) {
                throw new IllegalArgumentException(
                        "Only LocationBasedTrigger allowed, but got " + trigger.getClass().getSimpleName());
            }

            if (trigger.getOnActivateProfile() != null) {
                connectView.setText(trigger.getOnActivateProfile().getName());
            }

            if (trigger.getOnDeactivateProfile() != null) {
                disconnectView.setText(trigger.getOnDeactivateProfile().getName());
            }
        }
    }

    public class TimeViewHolder extends TriggerHolder {
        protected final TextView startView;
        protected final TextView endView;
        protected final LinearLayout repeatingDaysView;
        protected final TextView connectView;
        protected final TextView disconnectView;

        public TimeViewHolder(View itemView) {
            super(itemView);
            this.startView = (TextView) itemView.findViewById(R.id.start);
            this.endView = (TextView) itemView.findViewById(R.id.end);
            this.repeatingDaysView = (LinearLayout) itemView.findViewById(R.id.repeating);
            this.connectView = (TextView) itemView.findViewById(R.id.connect);
            this.disconnectView = (TextView) itemView.findViewById(R.id.disconnect);
        }

        @Override
        public void setTrigger(Trigger trigger) {
            super.setTrigger(trigger);

            if (!(trigger instanceof TimeBasedTrigger)) {
                throw new IllegalArgumentException(
                        "Only TimeBasedTrigger allowed, but got " + trigger.getClass().getSimpleName());
            }

            TimeBasedTrigger timeTrigger = (TimeBasedTrigger) trigger;

            startView.setText(timeTrigger.getActivationTime().toString("HH:mm"));
            endView.setText(timeTrigger.getDeactivationTime().toString("HH:mm"));

            if (trigger.getOnActivateProfile() != null) {
                connectView.setText(trigger.getOnActivateProfile().getName());
            }

            if (trigger.getOnDeactivateProfile() != null) {
                disconnectView.setText(trigger.getOnDeactivateProfile().getName());
            }

            for (Day day : timeTrigger.getRepeatOnDays()) {
                TextView tv = new TextView(context, null, R.style.AppTheme_List_Item_Text, R.style.AppTheme_List_Item_Text);
                tv.setText(dayOfWeekHelper.getShortnameOfDay(day));
                repeatingDaysView.addView(tv);
            }
        }
    }

    public class UnknownHolder extends TriggerHolder {
        public UnknownHolder(View itemView) {
            super(itemView);
            TextView messageView = (TextView) itemView.findViewById(R.id.message);
            messageView.setText("Unknown");
        }
    }

    private Context context;
    private List<Trigger> items;
    private OnItemClickListener onItemClickListener;
    private OnItemRemoveListener onItemRemoveListener;
    private DayOfWeekHelper dayOfWeekHelper;

    public TriggerCardAdapter(Context context, List<Trigger> items) {
        this.context = context;
        this.items = items;
        this.dayOfWeekHelper = new DayOfWeekHelper(context);
    }

    @Override
    public int getItemViewType(int position) {
        Trigger trigger = getItem(position);

        if (trigger instanceof WifiBasedTrigger) {
            return TYPE_WIFI;
        } else if (trigger instanceof LocationBasedTrigger) {
            return TYPE_LOCATION;
        } else if (trigger instanceof TimeBasedTrigger) {
            return TYPE_TIME;
        } else {
            return TYPE_UNKNOWN;
        }
    }

    @Override
    public TriggerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        TriggerHolder viewHolder;

        switch (viewType) {
            case TYPE_WIFI:
                viewHolder = new WifiViewHolder(layoutInflater.inflate(R.layout.card_item_trigger_wifi, parent, false));
                break;
            case TYPE_LOCATION:
                viewHolder = new LocationViewHolder(layoutInflater.inflate(R.layout.card_item_trigger_location, parent, false));
                break;
            case TYPE_TIME:
                viewHolder = new TimeViewHolder(layoutInflater.inflate(R.layout.card_item_trigger_time, parent, false));
                break;
            default:
                viewHolder = new UnknownHolder(layoutInflater.inflate(R.layout.card_item_trigger_unknown, parent, false));
        }

        viewHolder.setOnItemClickListener(onItemClickListener);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TriggerHolder holder, int position) {
        Trigger trigger = getItem(position);
        holder.setTrigger(trigger);
    }

    @Override
    public void remove(RecyclerView.ViewHolder viewHolder) {
        Trigger trigger = null;

        if (viewHolder instanceof LocationViewHolder) {
            trigger = ((LocationViewHolder) viewHolder).getTrigger();
        } else if (viewHolder instanceof TimeViewHolder) {
            trigger = ((TimeViewHolder) viewHolder).getTrigger();
        } else if (viewHolder instanceof WifiViewHolder) {
            trigger = ((WifiViewHolder) viewHolder).getTrigger();
        }

        if (onItemRemoveListener != null && trigger != null) {
            onItemRemoveListener.onItemRemove(trigger);
        }

        items.remove(viewHolder.getAdapterPosition());
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public Trigger getItem(int position) {
        return items == null || items.size() <= position ? null : items.get(position);
    }

    public void setItems(List<Trigger> items) {
        this.items = items;
        Collections.sort(this.items, new Comparator<Trigger>() {
            @Override
            public int compare(Trigger lhs, Trigger rhs) {
                return lhs.getType().name().compareTo(rhs.getType().name());
            }
        });

        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemRemoveListener(OnItemRemoveListener onItemRemoveListener) {
        this.onItemRemoveListener = onItemRemoveListener;
    }
}

package de.iweinzierl.easyprofiles.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.iweinzierl.easyprofiles.R;
import de.iweinzierl.easyprofiles.persistence.Profile;

public class ModifiableProfileCardAdapter extends RecyclerView.Adapter<ModifiableProfileCardAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Profile profile);
    }

    public interface OnItemEditListener {
        void onItemEdit(Profile profile);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        protected final CardView cardView;
        protected final TextView nameView;
        protected final View editView;

        protected Profile profile;

        public ViewHolder(View itemView) {
            super(itemView);
            this.cardView = (CardView) itemView.findViewById(R.id.card_view);
            this.nameView = (TextView) itemView.findViewById(R.id.name);
            this.editView = itemView.findViewById(R.id.edit);
        }

        public void setProfile(Profile profile) {
            this.profile = profile;
        }

        public Profile getProfile() {
            return profile;
        }

        public void setOnItemClickListener(final OnItemClickListener onItemClickListener) {
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(profile);
                }
            });
        }

        public void setOnItemEditListener(final OnItemEditListener onItemEditListener) {
            editView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemEditListener.onItemEdit(profile);
                }
            });
        }
    }

    private Context context;
    private List<Profile> items;
    private OnItemClickListener onItemClickListener;
    private OnItemEditListener onItemEditListener;

    public ModifiableProfileCardAdapter(Context context, List<Profile> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View itemView = layoutInflater.inflate(R.layout.card_item_profile_modifiable, parent, false);

        ViewHolder viewHolder = new ViewHolder(itemView);
        viewHolder.setOnItemClickListener(onItemClickListener);
        viewHolder.setOnItemEditListener(onItemEditListener);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Profile profile = getItem(position);

        if (profile != null) {
            holder.nameView.setText(profile.getName());
            holder.profile = profile;
        }
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public Profile getItem(int position) {
        return items == null || items.size() <= position ? null : items.get(position);
    }

    public void setItems(List<Profile> items) {
        this.items = items;
        Collections.sort(this.items, new Comparator<Profile>() {
            @Override
            public int compare(Profile lhs, Profile rhs) {
                return lhs.getName().compareTo(rhs.getName());
            }
        });

        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemEditListener(OnItemEditListener onItemEditListener) {
        this.onItemEditListener = onItemEditListener;
    }
}

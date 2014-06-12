package io.vinnie.servitetracker.android;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EventsAdapter extends BaseAdapter {

    private Context context;
    private List<ParseObject> events;

    public EventsAdapter(Context context, List<ParseObject> parseObjects) {
        this.context = context;
        events = parseObjects;
        Collections.sort(events, new Comparator<ParseObject>() {
            @Override
            public int compare(ParseObject lhs, ParseObject rhs) {
                return -lhs.getUpdatedAt().compareTo(rhs.getUpdatedAt());
            }
        });
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Object getItem(int position) {
        return events.get(position);
    }

    @Override
    public long getItemId(int position) {
        return events.get(position).getObjectId().hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView != null && convertView instanceof TextView) {
            ((TextView) convertView).setText(events.get(position).getString("name"));
            return convertView;
        }
        TextView view = (TextView) View.inflate(context, android.R.layout.simple_list_item_1, null);
        view.setText(events.get(position).getString("name"));
        return view;
    }
}
package io.vinnie.servitetracker.android.fragments;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import io.vinnie.servitetracker.android.R;
import io.vinnie.servitetracker.android.TitleProvider;

public class EventLogFragment extends ListFragment implements TitleProvider {

    private String id;
    private String name;

    public static EventLogFragment newInstance(String objectId, String name) {
        Bundle args = new Bundle();
        args.putString("eventId", objectId);
        args.putString("name", name);
        EventLogFragment frag = new EventLogFragment();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.id = getArguments().getString("eventId");
        this.name = getArguments().getString("name");
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadData();
    }

    private void loadData() {
        ParseQuery.getQuery("EventRecord").whereEqualTo("event", id).findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                Log.d("event", "found " + parseObjects.size() + " records for " + id);
                setListAdapter(new EventAdapter(parseObjects));
            }
        });
    }

    @Override
    public String getTitle(Context context) {
        return name;
    }

    private class EventAdapter extends BaseAdapter {

        private List<ParseObject> parseObjects;

        public EventAdapter(List<ParseObject> parseObjects) {
            this.parseObjects = parseObjects;
        }

        @Override
        public int getCount() {
            return parseObjects.size();
        }

        @Override
        public Object getItem(int position) {
            return parseObjects.get(position);
        }

        @Override
        public long getItemId(int position) {
            return parseObjects.get(position).hashCode();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ParseObject parseObject = parseObjects.get(position);
            View view;
            ViewHolder holder;
            if (convertView != null && convertView.getTag() instanceof ViewHolder) {
                view = convertView;
                holder = (ViewHolder) convertView.getTag();
            } else {
                view = View.inflate(getActivity(), R.layout.event_log_item, null);
                holder = new ViewHolder();
                holder.name = (TextView) view.findViewById(R.id.student_name);
                holder.priory = (TextView) view.findViewById(R.id.student_priory);
                holder.year = (TextView) view.findViewById(R.id.student_year);
            }
            holder.name.setText(parseObject.getString("name"));
            holder.priory.setText(parseObject.getString("priory"));
            holder.year.setText(parseObject.getString("year"));
            return view;
        }

        private class ViewHolder {
            public TextView name;
            public TextView priory;
            public TextView year;
        }
    }
}

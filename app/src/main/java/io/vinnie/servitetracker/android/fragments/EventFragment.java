package io.vinnie.servitetracker.android.fragments;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import io.vinnie.servitetracker.android.R;
import io.vinnie.servitetracker.android.TitleProvider;

public class EventFragment extends ListFragment implements TitleProvider {

    private String id;
    private String name;

    public static EventFragment newInstance(ParseObject object) {
        Bundle args = new Bundle();
        args.putString("eventId", object.getObjectId());
        args.putString("name", object.getString("name"));
        EventFragment frag = new EventFragment();
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

        private ArrayList<Pair<String, Integer>> counts = new ArrayList<Pair<String, Integer>>();

        public EventAdapter(List<ParseObject> parseObjects) {
            HashMap<String, Integer> gradeCounts = new HashMap<String, Integer>();
            HashMap<String, Integer> prioryCounts = new HashMap<String, Integer>();
            HashMap<String, HashMap<String, Integer>> prioryGradeCounts = new HashMap<String, HashMap<String, Integer>>();

            for (ParseObject record : parseObjects) {
                String year = record.getString("year");
                String priory = record.getString("priory");

                if (!gradeCounts.containsKey(year))
                    gradeCounts.put(year, 0);
                if (!prioryCounts.containsKey(priory))
                    prioryCounts.put(priory, 0);
                if (!prioryGradeCounts.containsKey(priory))
                    prioryGradeCounts.put(priory, new HashMap<String, Integer>());
                if (!prioryGradeCounts.get(priory).containsKey(year))
                    prioryGradeCounts.get(priory).put(year, 0);

                gradeCounts.put(year, gradeCounts.get(year) + 1);
                prioryCounts.put(priory, prioryCounts.get(priory) + 1);
                prioryGradeCounts.get(priory).put(year, prioryGradeCounts.get(priory).get(year) + 1);
            }

            //add counts for the different years and sort them
            ArrayList<Pair<String, Integer>> grades = new ArrayList<Pair<String, Integer>>();
            for (String year : gradeCounts.keySet()) {
                grades.add(Pair.create(year, gradeCounts.get(year)));
            }
            Collections.sort(grades, new Comparator<Pair<String, Integer>>() {
                @Override
                public int compare(Pair<String, Integer> lhs, Pair<String, Integer> rhs) {
                    return lhs.first.compareTo(rhs.first);
                }
            });
            counts.addAll(grades);

            //add counts for the different priories and sort them
            ArrayList<Pair<String, Integer>> priories = new ArrayList<Pair<String, Integer>>();
            for (String priory : prioryCounts.keySet()) {
                priories.add(Pair.create(priory, prioryCounts.get(priory)));
            }
            Collections.sort(priories, new Comparator<Pair<String, Integer>>() {
                @Override
                public int compare(Pair<String, Integer> lhs, Pair<String, Integer> rhs) {
                    return lhs.first.compareTo(rhs.first);
                }
            });
            counts.addAll(priories);

            //add counts for the priory grade counts
            ArrayList<Pair<String, Integer>> prioryGrades = new ArrayList<Pair<String, Integer>>();
            for (String priory : prioryGradeCounts.keySet()) {
                HashMap<String, Integer> gradesForPriory = prioryGradeCounts.get(priory);
                for (String grade : gradesForPriory.keySet()) {
                    prioryGrades.add(Pair.create(priory + " - " + grade, gradesForPriory.get(grade)));
                }
            }
            Collections.sort(prioryGrades, new Comparator<Pair<String, Integer>>() {
                @Override
                public int compare(Pair<String, Integer> lhs, Pair<String, Integer> rhs) {
                    return lhs.first.compareTo(rhs.first);
                }
            });
            counts.addAll(prioryGrades);
        }

        @Override
        public int getCount() {
            return counts.size();
        }

        @Override
        public Object getItem(int position) {
            return counts.get(position);
        }

        @Override
        public long getItemId(int position) {
            return counts.get(position).hashCode();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Pair<String, Integer> pair = counts.get(position);
            View view;
            ViewHolder holder;
            if (convertView != null && convertView.getTag() instanceof ViewHolder) {
                view = convertView;
                holder = (ViewHolder) convertView.getTag();
            } else {
                view = View.inflate(getActivity(), R.layout.event_total_item, null);
                holder = new ViewHolder();
                holder.title = (TextView) view.findViewById(R.id.title);
                holder.value = (TextView) view.findViewById(R.id.total);
            }
            holder.title.setText(pair.first);
            holder.value.setText(pair.second.toString());
            return view;
        }

        private class ViewHolder {
            public TextView title;
            public TextView value;
        }
    }
}

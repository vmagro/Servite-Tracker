package io.vinnie.servitetracker.android.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.HashMap;
import java.util.List;

import io.vinnie.servitetracker.android.R;
import io.vinnie.servitetracker.android.TitleProvider;

public class EventFragment extends Fragment implements TitleProvider {

    private String name;

    public static EventFragment newInstance(String name) {
        Bundle args = new Bundle();
        args.putString("name", name);
        EventFragment frag = new EventFragment();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.name = getArguments().getString("name");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event, null);

        loadData();
        return view;
    }

    private void loadData() {
        ParseQuery.getQuery("EventRecord").whereEqualTo("event", name).findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
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
                        prioryGradeCounts.put(year, new HashMap<String, Integer>());
                    if (!prioryGradeCounts.get(priory).containsKey(year))
                        prioryGradeCounts.get(priory).put(year, 0);

                    gradeCounts.put(year, gradeCounts.get(year) + 1);
                    prioryCounts.put(year, prioryCounts.get(year) + 1);
                    prioryGradeCounts.get(priory).put(year, prioryGradeCounts.get(priory).get(year) + 1);
                }
            }
        });
    }

    @Override
    public String getTitle(Context context) {
        return name;
    }
}

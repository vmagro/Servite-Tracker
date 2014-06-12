package io.vinnie.servitetracker.android.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import io.vinnie.servitetracker.android.CardListener;
import io.vinnie.servitetracker.android.EventsAdapter;
import io.vinnie.servitetracker.android.R;
import io.vinnie.servitetracker.android.TitleProvider;

public class ScanFragment extends Fragment implements TitleProvider, CardListener, View.OnClickListener {

    private View lastCardContainer;
    private TextView studentName;
    private TextView studentPriory;
    private TextView studentYear;

    private EditText manualEntry;

    private Spinner eventSpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan, null);

        lastCardContainer = view.findViewById(R.id.last_card_layout);
        studentName = (TextView) view.findViewById(R.id.student_name);
        studentPriory = (TextView) view.findViewById(R.id.student_priory);
        studentYear = (TextView) view.findViewById(R.id.student_year);

        manualEntry = (EditText) view.findViewById(R.id.manual_card_entry);
        view.findViewById(R.id.btn_go).setOnClickListener(this);

        eventSpinner = (Spinner) view.findViewById(R.id.event_spinner);
        loadSpinner();

        return view;
    }

    @Override
    public String getTitle(Context context) {
        return context.getString(R.string.scan);
    }

    @Override
    public void onCardScanned(String cardData) {
        ParseQuery.getQuery("Student").whereEqualTo("idNumber", cardData).findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                ParseObject user = parseObjects.get(0);
                ParseObject eventRecord = new ParseObject("EventRecord");
                eventRecord.put("name", user.getString("firstName") + " " + user.getString("lastName"));
                eventRecord.put("event", eventSpinner.getSelectedItem());


                studentName.setText(user.getString("firstName") + " " + user.getString("lastName"));
                studentPriory.setText(user.getString("priory"));
                studentYear.setText(user.getString("year"));
                if (lastCardContainer.getVisibility() != View.VISIBLE)
                    lastCardContainer.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_go:
                onCardScanned(manualEntry.getText().toString());
                break;
        }
    }

    private void loadSpinner() {
        ParseQuery.getQuery("Event").findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                eventSpinner.setAdapter(new EventsAdapter(getActivity(), parseObjects));
            }
        });
    }

}

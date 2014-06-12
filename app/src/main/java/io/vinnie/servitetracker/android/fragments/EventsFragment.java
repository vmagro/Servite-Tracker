package io.vinnie.servitetracker.android.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

import io.vinnie.servitetracker.android.EventsAdapter;
import io.vinnie.servitetracker.android.MainActivity;
import io.vinnie.servitetracker.android.R;
import io.vinnie.servitetracker.android.TitleProvider;

public class EventsFragment extends ListFragment implements TitleProvider {

    @Override
    public String getTitle(Context context) {
        return context.getString(R.string.events);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        registerForContextMenu(getListView());

        loadEvents();

        setHasOptionsMenu(true);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        ParseObject object = (ParseObject) getListView().getItemAtPosition(position);
        Fragment frag = EventFragment.newInstance(object.getString("name"));
        ((MainActivity) getActivity()).swapFragment(frag);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_events, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new:
                showEditDialog(new ParseObject("Event"));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.fragment_events_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.action_delete:
                ((ParseObject) getListView().getItemAtPosition(info.position))
                        .deleteEventually(new DeleteCallback() {
                            @Override
                            public void done(ParseException e) {
                                loadEvents();
                            }
                        });
                break;
            case R.id.action_edit:
                showEditDialog((ParseObject) getListView().getItemAtPosition(info.position));
        }
        return super.onContextItemSelected(item);
    }

    private void loadEvents() {
        ParseQuery.getQuery("Event").findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                setListAdapter(new EventsAdapter(getActivity(), parseObjects));
            }
        });
    }

    private void showEditDialog(final ParseObject object) {
        final EditText input = new EditText(getActivity());
        input.setHint(R.string.name);
        new AlertDialog.Builder(getActivity())
                .setTitle(object.has("name") ? "Edit " + object.getString("name") : getString(R.string.new_event))
                .setView(input)
                .setPositiveButton(getString(R.string.add), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (TextUtils.isEmpty(input.getText().toString())) {
                            Toast.makeText(getActivity(), "Can't have empty name", Toast.LENGTH_LONG).show();
                            return;
                        }
                        object.put("name", input.getText().toString());
                        object.saveEventually(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null)
                                    loadEvents();
                            }
                        });
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

}

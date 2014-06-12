package io.vinnie.servitetracker.android.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

import io.vinnie.servitetracker.android.CardListener;
import io.vinnie.servitetracker.android.R;
import io.vinnie.servitetracker.android.TitleProvider;

public class UsersFragment extends Fragment implements CardListener, View.OnClickListener, TitleProvider {

    private EditText manualEntry;
    private CheckBox adminCheckbox;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, null);

        manualEntry = (EditText) view.findViewById(R.id.manual_card_entry);
        view.findViewById(R.id.btn_go).setOnClickListener(this);

        adminCheckbox = (CheckBox) view.findViewById(R.id.admin_checkbox);

        return view;
    }

    @Override
    public void onCardScanned(String cardData) {
        ParseQuery.getQuery("Student").whereEqualTo("idNumber", cardData).findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                final ParseObject user = parseObjects.get(0);
                user.put("admin", adminCheckbox.isChecked());
                user.saveEventually(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null)
                            Toast.makeText(getActivity(), getString(R.string.error), Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(getActivity(), user.get("firstName") + " " + getString(user.getBoolean("admin") ? R.string.is_now_admin : R.string.is_not_admin), Toast.LENGTH_LONG).show();
                    }
                });
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

    @Override
    public String getTitle(Context context) {
        return context.getString(R.string.users);
    }
}

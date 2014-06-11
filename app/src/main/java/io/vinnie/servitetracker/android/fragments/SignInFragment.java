package io.vinnie.servitetracker.android.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.vinnie.servitetracker.android.R;
import io.vinnie.servitetracker.android.TitleProvider;

/**
 * Created by vinnie on 6/11/14.
 */
public class SignInFragment extends Fragment implements TitleProvider {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signin, null);
        return view;
    }

    @Override
    public String getTitle(Context context) {
        return context.getString(R.string.signin);
    }
}

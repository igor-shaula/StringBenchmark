package com.igor.shaula.string_benchmark.screens;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.igor.shaula.string_benchmark.R;
import com.igor.shaula.string_benchmark.annotations.TypeDoc;

@TypeDoc(createdBy = "igor shaula", createdOn = "04.01.2018", purpose = "" +
        "try to use dedicated settings page")

public final class PrefsFragment0 extends PreferenceFragment {

    @Nullable
    private static PrefsFragment0 thisInstance;
    // as default constructor must be public for a Fragment - we don't

    public static PrefsFragment0 getInstance() {
        if (thisInstance == null) {
            return thisInstance = new PrefsFragment0();
        } else {
            return thisInstance;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_for_fragment_0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View fragmentView = super.onCreateView(inflater, container, savedInstanceState);
        if (fragmentView != null) {
//            fragmentView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            fragmentView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
            fragmentView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//            fragmentView.findViewById(R.id.switch1).setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
//            fragmentView.findViewById(R.id.checkbox1).setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        }
        return fragmentView;
    }

    // TODO: 07.01.2018 change colors of inner widgets anyhow - or may be change the whole theme here \\
}
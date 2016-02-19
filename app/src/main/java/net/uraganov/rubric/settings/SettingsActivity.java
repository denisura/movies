package net.uraganov.rubric.settings;


import android.support.v4.app.Fragment;

import net.uraganov.rubric.SingleFragmentActivity;


public class SettingsActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return SettingsFragment.newInstance();
    }

}

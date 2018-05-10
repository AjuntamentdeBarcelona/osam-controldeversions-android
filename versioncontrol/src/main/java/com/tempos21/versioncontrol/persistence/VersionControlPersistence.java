package com.tempos21.versioncontrol.persistence;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * VersionControlPersistence
 */
public class VersionControlPersistence {

    private static final String VERSION_CONTROL_SHARED_PREFERENCES = "VERSION_CONTROL_SHARED_PREFERENCES";

    private static final String LAST_VERSION_ACCEPTED_KEY = "LAST_VERSION_ACCEPTED_KEY";

    private SharedPreferences sharedPreferences;

    public VersionControlPersistence(Context context) {
        sharedPreferences = context.getSharedPreferences(VERSION_CONTROL_SHARED_PREFERENCES, Context.MODE_PRIVATE);
    }

    public Integer getLastVersionAccepted() {
        return sharedPreferences.getInt(LAST_VERSION_ACCEPTED_KEY, 0);
    }

    public void setLastVersionAccepted(Integer version) {
        sharedPreferences.edit()
                .putInt(LAST_VERSION_ACCEPTED_KEY, version)
                .apply();
    }

    public boolean hasLastVersionAccepted() {
        return sharedPreferences.contains(LAST_VERSION_ACCEPTED_KEY);
    }

    public void removeLastVersionAccepted() {
        sharedPreferences.edit()
                .remove(LAST_VERSION_ACCEPTED_KEY)
                .apply();
    }
}

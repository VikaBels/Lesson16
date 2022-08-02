package com.example.lesson16.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.lesson16.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}
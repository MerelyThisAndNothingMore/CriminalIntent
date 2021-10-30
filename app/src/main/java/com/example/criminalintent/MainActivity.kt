package com.example.criminalintent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.criminalintent.view.CrimeFragment
import com.example.criminalintent.view.CrimeListFragment
import java.util.*

class MainActivity : AppCompatActivity(), CrimeListFragment.Callbacks {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (supportFragmentManager.findFragmentById(R.id.fragment_container) == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, CrimeListFragment.newInstance())
                .commit()
        }
    }

    override fun onCrimeSelected(crimeId: UUID) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, CrimeFragment.newInstance(crimeId))
            .addToBackStack(null)
            .commit()
    }
}
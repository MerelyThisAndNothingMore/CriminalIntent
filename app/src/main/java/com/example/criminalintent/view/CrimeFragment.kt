package com.example.criminalintent.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.criminalintent.R
import com.example.criminalintent.model.Crime
import com.example.criminalintent.viewmodel.CrimeDetailViewModel
import java.util.*

/****
 * @author : zhangjin.rolling
 * @date : 2021/10/20
 */

class CrimeFragment : Fragment() {

    companion object {

        private const val ARG_CRIME_ID = "crime_id"

        fun newInstance(crimeId: UUID): CrimeFragment {
            return CrimeFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_CRIME_ID, crimeId)
                }
            }
        }
    }

    private var mCrime = Crime()

    private val crimeDetailViewModel by lazy {
        ViewModelProvider(this).get(CrimeDetailViewModel::class.java)
    }

    private var mCrimeTitle: EditText? = null
    private var mCrimeData: Button? = null
    private var mCrimeSolved: CheckBox? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val crimeId = arguments?.getSerializable(ARG_CRIME_ID) as UUID
        crimeDetailViewModel.loadCrime(crimeId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime, container, false)
        mCrimeTitle = view.findViewById(R.id.crime_title)
        mCrimeData = view.findViewById(R.id.crime_date)
        mCrimeData?.apply {
            text = mCrime.date.toString()
            isEnabled = false
        }
        mCrimeSolved = view.findViewById(R.id.crime_solved)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        crimeDetailViewModel.crimeLiveData.observe(viewLifecycleOwner) { crime ->
            crime?.let {
                mCrime = crime
                updateUI()
            }
        }
    }

    private fun updateUI() {
        mCrimeTitle?.setText(mCrime.title)
        mCrimeData?.text = mCrime.date.toString()
        mCrimeSolved?.apply {
            isChecked = mCrime.isSolved
            jumpDrawablesToCurrentState()
        }
    }

    override fun onStart() {
        super.onStart()

        mCrimeTitle?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mCrime.title = s.toString()
            }

        })

        mCrimeSolved?.setOnCheckedChangeListener { _, isChecked ->
            mCrime.isSolved = isChecked
        }
    }

    override fun onStop() {
        super.onStop()
        crimeDetailViewModel.saveCrime(mCrime)
    }
}
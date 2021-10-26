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
import com.example.criminalintent.R
import com.example.criminalintent.model.Crime

/****
 * @author : zhangjin.rolling
 * @date : 2021/10/20
 */

class CrimeFragment : Fragment() {

    private val mCrime by lazy {
        Crime()
    }
    private var mCrimeTitle: EditText? = null
    private var mCrimeData: Button? = null
    private var mCrimeSolved: CheckBox? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            text = mCrime.data.toString()
            isEnabled = false
        }
        mCrimeSolved = view.findViewById(R.id.crime_solved)
        return view
    }

    override fun onStart() {
        super.onStart()

        mCrimeTitle?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                TODO("Not yet implemented")
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mCrime.title = s.toString()
            }

        })

        mCrimeSolved?.setOnCheckedChangeListener { _, isChecked ->
            mCrime.isSolved = isChecked
        }
    }
}
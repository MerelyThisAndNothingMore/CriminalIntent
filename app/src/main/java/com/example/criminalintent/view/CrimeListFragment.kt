package com.example.criminalintent.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.criminalintent.R
import com.example.criminalintent.model.Crime
import com.example.criminalintent.viewmodel.CrimeListViewModel
import java.text.DateFormat

/****
 * @author : zhangjin.rolling
 * @date : 2021/10/22
 */
class CrimeListFragment: Fragment() {
    companion object {
        const val TAG = "CrimeListFragment"
        fun newInstance(): CrimeListFragment {
            return CrimeListFragment()
        }
    }
    private var mRecyclerView: RecyclerView? = null

    private val crimeListViewModel by lazy {
        ViewModelProvider(this).get(CrimeListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)
        mRecyclerView = view.findViewById(R.id.crime_list_recycler_view)
        mRecyclerView?.layoutManager = LinearLayoutManager(context)
        mRecyclerView?.adapter = CrimeAdapter(crimeListViewModel.crimeList)
        return view
    }



    private inner class CrimeHolder(view: View) : RecyclerView.ViewHolder(view) {

        private var mCrime: Crime = Crime()
        private val mCrimeTitle: TextView by lazy {
            itemView.findViewById(R.id.crime_title)
        }
        private val mCrimeDate: TextView by lazy {
            itemView.findViewById(R.id.crime_date)
        }
        private val mCrimeSolved: ImageView by lazy {
            itemView.findViewById(R.id.crime_solved)
        }

        init {
            initView()
            setClickListener()
        }

        private fun initView() {

        }

        private fun setClickListener() {
            itemView.setOnClickListener {
                Toast.makeText(context, "${mCrime.title} passed", Toast.LENGTH_SHORT).show()
            }
        }

        fun bind(crime: Crime) {
            mCrime = crime
            mCrimeTitle.text = crime.title
            mCrimeDate.text = crime.date.toString()
            mCrimeSolved.setImageResource(if (mCrime.isSolved) R.drawable.crime_solved_pic else R.drawable.crime_unsolved_pic)
        }

    }



    private inner class CrimeAdapter(var crimes: List<Crime>) : RecyclerView.Adapter<CrimeHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
            val view = layoutInflater.inflate(R.layout.list_item_crime, parent, false)
            return CrimeHolder(view)
        }

        override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
            val crime = crimes[position]
            holder.bind(crime)
        }

        override fun getItemCount(): Int {
            return crimes.size
        }

    }
}
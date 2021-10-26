package com.example.criminalintent.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.criminalintent.R
import com.example.criminalintent.model.Crime
import com.example.criminalintent.viewmodel.CrimeListViewModel
import org.w3c.dom.Text

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
        return view
    }

    private inner class CrimeHolder(view: View) : RecyclerView.ViewHolder(view) {

        val mCrimeTitle: TextView = itemView.findViewById(R.id.crime_title)
        val mCrimeDate: TextView = itemView.findViewById(R.id.crime_date)

    }

    private inner class CrimeAdapter(var crimes: List<Crime>) : RecyclerView.Adapter<CrimeHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
            val view = layoutInflater.inflate(R.layout.list_item_crime, parent, false)
            return CrimeHolder(view)
        }

        override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
            val crime = crimes[position]
            holder.apply {
                mCrimeTitle.text = crime.title
                mCrimeDate.text = crime.data.toString()
            }
        }

        override fun getItemCount(): Int {
            return crimes.size
        }

    }
}
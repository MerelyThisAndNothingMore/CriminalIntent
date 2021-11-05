package com.example.criminalintent.view

import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.view.*
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.criminalintent.R
import com.example.criminalintent.model.Crime
import com.example.criminalintent.viewmodel.CrimeListViewModel
import java.util.*

/****
 * @author : zhangjin.rolling
 * @date : 2021/10/22
 */
class CrimeListFragment : Fragment() {
    companion object {
        const val TAG = "CrimeListFragment"
        fun newInstance(): CrimeListFragment {
            return CrimeListFragment()
        }
    }

    private var callbacks: Callbacks? = null

    private var mRecyclerView: RecyclerView? = null

    private val mCrimeListViewModel by lazy { ViewModelProvider(this).get(CrimeListViewModel::class.java) }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)
        mRecyclerView = view.findViewById(R.id.crime_list_recycler_view)
        mRecyclerView?.layoutManager = LinearLayoutManager(context)
        mRecyclerView?.adapter = CrimeAdapter()
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mCrimeListViewModel.crimeListLiveData.observe(
            viewLifecycleOwner,
            Observer { crimes ->
                crimes?.let {
                    mCrimeListViewModel.saveCrimeList = it.toMutableList()
                    mRecyclerView?.adapter = CrimeAdapter()
                }
            }
        )
    }

    override fun onStop() {
        super.onStop()
        mCrimeListViewModel.updateCrimes()
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_crime_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.new_crime -> {
                val crime = Crime()
                mCrimeListViewModel.addCrime(crime)
                callbacks?.onCrimeSelected(crime.id)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }


    private inner class CrimeHolder(view: View) : RecyclerView.ViewHolder(view) {

        private var mCrime: Crime = Crime()
        private var mPosition: Int? = null
        private val mCrimeTitle: TextView by lazy {
            itemView.findViewById(R.id.crime_title)
        }
        private val mCrimeDate: TextView by lazy {
            itemView.findViewById(R.id.crime_date)
        }
        private val mCrimeSolved: CheckBox by lazy {
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
                callbacks?.onCrimeSelected(mCrime.id)
            }

            mCrimeSolved.setOnCheckedChangeListener { _, isChecked ->
                mCrime.isSolved = isChecked
                checkUI()
                mPosition?.let { mCrimeListViewModel.saveCrimeList[it] = mCrime }
            }
        }

        fun bind(crime: Crime, position: Int) {
            mCrime = crime
            mPosition = position
            mCrimeTitle.text = crime.title
            mCrimeDate.text = crime.date.toString()
            checkUI()
        }

        private fun checkUI() {
            mCrimeSolved.isChecked = mCrime.isSolved
            if (mCrime.isSolved) {
                mCrimeTitle.paintFlags = mCrimeTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                mCrimeDate.paintFlags = mCrimeDate.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                mCrimeTitle.paintFlags =
                    mCrimeTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                mCrimeDate.paintFlags = mCrimeDate.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }

    }


    private inner class CrimeAdapter() :
        RecyclerView.Adapter<CrimeHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
            val view = layoutInflater.inflate(R.layout.list_item_crime, parent, false)
            return CrimeHolder(view)
        }

        override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
            val crime = mCrimeListViewModel.saveCrimeList[position]
            holder.bind(crime, position)
        }

        override fun getItemCount(): Int {
            return mCrimeListViewModel.saveCrimeList.size
        }

    }

    interface Callbacks {
        fun onCrimeSelected(crimeId: UUID)
    }
}
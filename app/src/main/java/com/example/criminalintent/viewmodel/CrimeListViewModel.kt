package com.example.criminalintent.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.criminalintent.CrimeRepository
import com.example.criminalintent.model.Crime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.concurrent.Executor

/****
 * @author : zhangjin.rolling
 * @date : 2021/10/22
 */
class CrimeListViewModel: ViewModel() {

    private val mCrimeRepository by lazy { CrimeRepository.instance() }

    val crimeListLiveData: LiveData<List<Crime>> by lazy { mCrimeRepository.getAllCrimes() }

    fun insertCrimes() {

        GlobalScope.launch(Dispatchers.IO) {
            val crimes : MutableList<Crime> = mutableListOf()
            for (i in 1..100) {
                crimes.add(Crime().apply {
                    title = "This is Crime # $i"
                    isSolved = i % 2 == 0
                })
            }
            Log.e("init crimes", "start crimes")
            mCrimeRepository.insertCrimes(crimes)
            Log.e("init crimes", "end crimes")
        }

    }

}
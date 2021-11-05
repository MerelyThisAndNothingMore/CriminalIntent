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
class CrimeListViewModel : ViewModel() {

    private val mCrimeRepository by lazy { CrimeRepository.instance() }

    val crimeListLiveData: LiveData<List<Crime>> by lazy { mCrimeRepository.getAllCrimes() }

    var saveCrimeList: MutableList<Crime> = mutableListOf()

    fun updateCrimes() {
        mCrimeRepository.updateCrimes(saveCrimeList)
    }

    fun addCrime(crime: Crime) {
        mCrimeRepository.insertCrime(crime)
    }

}
package com.example.criminalintent.viewmodel

import androidx.lifecycle.ViewModel
import com.example.criminalintent.model.Crime

/****
 * @author : zhangjin.rolling
 * @date : 2021/10/22
 */
class CrimeListViewModel: ViewModel() {

    val crimeList: MutableList<Crime> = mutableListOf()

    init {
        for (i in 0..100) {
            crimeList.add(Crime().apply {
                title = "Crime $i"
                isSolved = i % 2 == 0
            })
        }
    }


}
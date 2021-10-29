package com.example.criminalintent

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.criminalintent.database.CrimeDatabase
import com.example.criminalintent.model.Crime
import java.util.*

/****
 * @author : zhangjin.rolling
 * @date : 2021/10/28
 */
class CrimeRepository private constructor(context: Context) {

    private val database: CrimeDatabase by lazy {
        Room.databaseBuilder(
            context.applicationContext,
            CrimeDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    private val crimeDao by lazy {
        database.getCrimeDao()
    }

    fun getAllCrimes(): LiveData<List<Crime>> = crimeDao.getAllCrimes()

    fun getCrime(id: UUID): LiveData<Crime?> = crimeDao.getCrime(id)

    suspend fun insertCrime(crime: Crime?) {
        crimeDao.insertCrime(crime)
    }

    suspend fun insertCrimes(crimes: List<Crime>) {
        crimeDao.insertCrimes(crimes)
    }

    companion object {
        private var INSTANCE: CrimeRepository? = null
        private const val DATABASE_NAME = "crime_database"

        fun init(context: Context) {
            INSTANCE = INSTANCE ?: CrimeRepository(context)
        }

        fun instance(): CrimeRepository {
            return INSTANCE
                ?: throw IllegalAccessException("CrimeRepository hasn't been initialized.")
        }
    }
}
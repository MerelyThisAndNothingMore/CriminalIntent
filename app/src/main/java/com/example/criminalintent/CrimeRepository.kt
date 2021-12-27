package com.example.criminalintent

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.criminalintent.database.CrimeDatabase
import com.example.criminalintent.database.migration_1_2
import com.example.criminalintent.model.Crime
import java.io.File
import java.util.*
import java.util.concurrent.Executors

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
        )
            .addMigrations(migration_1_2)
            .build()
    }

    private val crimeDao by lazy { database.getCrimeDao() }

    private val executor = Executors.newSingleThreadExecutor()
    private val filesDir = context.applicationContext.filesDir

    private fun execute(command: () -> Unit) = executor.execute(command)

    fun getAllCrimes(): LiveData<List<Crime>> = crimeDao.getAllCrimes()

    fun getCrime(id: UUID): LiveData<Crime?> = crimeDao.getCrime(id)

    fun insertCrime(crime: Crime) = execute { crimeDao.insertCrime(crime) }

    fun insertCrimes(crimes: List<Crime>) = execute { crimeDao.insertCrimes(crimes) }

    fun getPhotoFile(crime: Crime) = File(filesDir, crime.photoFileName)

    fun updateCrime(crime: Crime) = execute { crimeDao.updateCrime(crime) }

    fun updateCrimes(crimes: List<Crime>) = execute { crimeDao.updateCrimes(crimes) }

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
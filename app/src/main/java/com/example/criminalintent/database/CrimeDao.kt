package com.example.criminalintent.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.criminalintent.model.Crime
import java.util.*

/****
 * @author : zhangjin.rolling
 * @date : 2021/10/28
 */
@Dao
interface CrimeDao {

    @Query("SELECT * FROM crime")
    fun getAllCrimes(): LiveData<List<Crime>>

    @Query("SELECT * FROM crime WHERE id=(:id)")
    fun getCrime(id: UUID): LiveData<Crime?>

    @Update
    fun updateCrime(crime: Crime)

    @Update
    fun updateCrimes(crimes: List<Crime>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCrime(crime: Crime)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCrimes(crimes: List<Crime>)

}
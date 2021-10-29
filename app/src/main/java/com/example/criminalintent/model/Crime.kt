package com.example.criminalintent.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/****
 * @author : zhangjin.rolling
 * @date : 2021/10/20
 */
@Entity
data class Crime(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    var title: String = "",
    var date: Date = Date(),
    var isSolved: Boolean = false
)
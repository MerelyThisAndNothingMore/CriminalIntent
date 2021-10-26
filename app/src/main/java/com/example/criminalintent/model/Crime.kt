package com.example.criminalintent.model

import java.util.*

/****
 * @author : zhangjin.rolling
 * @date : 2021/10/20
 */
data class Crime(
    val id: UUID = UUID.randomUUID(),
    var title: String = "",
    var data: Date = Date(),
    var isSolved: Boolean = false
)
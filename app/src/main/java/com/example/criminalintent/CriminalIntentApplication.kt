package com.example.criminalintent

import android.app.Application
import android.content.Context
import me.weishu.reflection.Reflection

/****
 * @author : zhangjin.rolling
 * @date : 2021/10/28
 */
class CriminalIntentApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        CrimeRepository.init(this)
    }

    /**
     * call this for initializing FreeReflection
     */
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        Reflection.unseal(base)
    }
}
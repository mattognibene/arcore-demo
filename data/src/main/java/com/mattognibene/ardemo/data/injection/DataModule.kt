package com.mattognibene.ardemo.data.injection

import com.mattognibene.ardemo.cache.room.DatabaseModule
import com.mattognibene.ardemo.domain.executors.AppExecutors
import com.mattognibene.ardemo.remote.NetworkModule

import java.util.concurrent.Executors

import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@Module(includes = [DatabaseModule::class, NetworkModule::class])
object DataModule {
    @Provides
    @JvmStatic
    fun providesExecutors(): AppExecutors {
        return object : AppExecutors {
            override fun diskIo(): Scheduler {
                return Schedulers.from(Executors.newFixedThreadPool(3))
            }

            override fun networkIo(): Scheduler {
                return Schedulers.io()
            }

            override fun mainThread(): Scheduler {
                return AndroidSchedulers.mainThread()
            }
        }
    }
}

package com.example.drivex.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.drivex.data.model.Refuel
import com.example.drivex.data.RefuelDao
import com.example.drivex.data.RefuelRoomDatabase


class RefuelRepositoryImpl(application: Application) :
    RefuelRepository {
    private val refuelDao: RefuelDao by lazy {
        val db = RefuelRoomDatabase.getInstance(application)
        db.refuelDao()
    }

    override suspend fun insert(refuel: Refuel) {
       return refuelDao.insert(refuel)
    }

    override suspend fun getAllRefuel(): List<Refuel> =refuelDao.getAllRefuel()
    override fun getRefuelById(id: Long): LiveData<Refuel> {
        return refuelDao.getRefuelById(id)
    }
    override suspend fun addRefuel(refuel: Refuel) {
        refuelDao.addRefuel(refuel)
    }

    override suspend fun getSumOfExpenses(): LiveData<Double> {
        return refuelDao.getSumOfExpenses()
    }

    override suspend fun getLastMileage(): LiveData<Int> {
        return refuelDao.getLastMileage()
    }

    override suspend fun getSumByTittle(title: String): LiveData<Int> {
        return refuelDao.getSumByTittle(title)
    }


}

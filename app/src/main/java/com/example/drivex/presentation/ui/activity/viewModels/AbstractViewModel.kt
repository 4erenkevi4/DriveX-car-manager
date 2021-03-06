package com.example.drivex.presentation.ui.activity.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.example.drivex.data.RefuelDao
import com.example.drivex.data.RefuelRoomDatabase
import com.example.drivex.data.model.Refuel
import com.example.drivex.data.repository.RefuelRepository
import com.example.drivex.data.repository.RefuelRepositoryImpl
import com.example.drivex.utils.Constans
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*

class AbstractViewModel(application: Application) : AndroidViewModel(application) {

private val refuelRepository:RefuelRepository
init {
    refuelRepository = RefuelRepositoryImpl(application)
    }
    private val refuelDao: RefuelDao by lazy {
        val db = RefuelRoomDatabase.getInstance(application)
        db.refuelDao()
    }

     suspend fun readAllDataByDate(): List<Refuel> {
        return refuelRepository.getAllRefuel()
    }

     fun readRefuelById(id:Long): LiveData<Refuel> {
        return refuelRepository.getRefuelById(id)
    }

     fun addRefuel(refuel: Refuel) {
        viewModelScope.launch(Dispatchers.IO) {
            refuelRepository.addRefuel(refuel)
        }
    }
    fun insert(refuel: Refuel) {
        viewModelScope.launch(Dispatchers.IO) {
            refuelRepository.insert(refuel)
        }
    }

    fun sum(title: String) {
        viewModelScope.launch(Dispatchers.IO) {
            refuelRepository.getSumByTittle(title)
        }
    }

    val allExpenses: LiveData<Int> = Transformations.map(refuelDao.getSumOfExpenses()) {sumOfCosts ->
        sumOfCosts.toInt()
    }



    val allExpensesSum: LiveData<String> = Transformations.map(refuelDao.getSumOfExpenses()) { sumOfCosts ->
        NumberFormat.getCurrencyInstance(Locale("ru", "BY")).format(sumOfCosts?: 0.0)
    }
    val allFuelCostSum: LiveData<String> = Transformations.map(refuelDao.getSUmExpensesById("????????????????")) { sumOfCosts ->
        NumberFormat.getCurrencyInstance(Locale("ru", "BY")).format(sumOfCosts?: 0.0)
    }

    val allServiceCostSum: LiveData<String> = Transformations.map(refuelDao.getSUmExpensesById("????????????")) { sumOfCosts ->
        NumberFormat.getCurrencyInstance(Locale("ru", "BY")).format(sumOfCosts?: 0.0)
    }


    val refuelSum: LiveData<Int> = refuelDao.getSUmExpensesById("????????????????")
    val serviceSum: LiveData<Int> = refuelDao.getSUmExpensesById("????????????")
    val shoppingSum: LiveData<Int> = refuelDao.getSUmExpensesById("??????????????")
    val paymentsSum: LiveData<Int> = refuelDao.getSUmExpensesById("????????????")



    private val allFuelVolumeSumInt: LiveData<Int> = refuelDao.getSummVolumeById("????????????????")
    val allFuelVolumeSum: LiveData<String> = Transformations.map(allFuelVolumeSumInt){ summ ->
        (summ?.toString() ?: "0") + " ??."
    }

    private val lastMileage: LiveData<Int> = refuelDao.getLastMileage()
    val lastMileageStr: LiveData<String> = Transformations.map(lastMileage){ checkpoint ->
        (checkpoint?.toString() ?: "0") + " Km."
    }

}
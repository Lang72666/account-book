package com.example.accountbook.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.accountbook.data.AccountDao
import com.example.accountbook.data.AccountRecord
import com.example.accountbook.data.AppDatabase
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AccountViewModel(application: Application) : AndroidViewModel(application) {
    private val dao: AccountDao = AppDatabase.getDatabase(application).accountDao()

    val allRecords: StateFlow<List<AccountRecord>> = dao.getAllRecords()
        .stateIn(viewModelScope, kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000), emptyList())

    val totalExpense: StateFlow<Double> = dao.getTotalExpense()
        .stateIn(viewModelScope, kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000), 0.0)

    val totalIncome: StateFlow<Double> = dao.getTotalIncome()
        .stateIn(viewModelScope, kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000), 0.0)

    fun addRecord(amount: Double, type: Int, remark: String) {
        viewModelScope.launch {
            dao.insert(AccountRecord(amount = amount, type = type, remark = remark))
        }
    }
}

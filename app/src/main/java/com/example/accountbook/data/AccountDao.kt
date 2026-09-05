package com.example.accountbook.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {
    @Insert
    suspend fun insert(record: AccountRecord)

    @Query("SELECT * FROM account_records ORDER BY time DESC")
    fun getAllRecords(): Flow<List<AccountRecord>>

    @Query("SELECT SUM(amount) FROM account_records WHERE type = 0")
    fun getTotalExpense(): Flow<Double>

    @Query("SELECT SUM(amount) FROM account_records WHERE type = 1")
    fun getTotalIncome(): Flow<Double>
}

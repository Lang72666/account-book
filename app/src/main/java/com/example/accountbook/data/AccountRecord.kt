package com.example.accountbook.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "account_records")
data class AccountRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val amount: Double,
    val type: Int,
    val remark: String,
    val time: Long = System.currentTimeMillis()
)

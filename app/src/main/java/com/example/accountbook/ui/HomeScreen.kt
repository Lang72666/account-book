package com.example.accountbook.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.accountbook.data.AccountRecord
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: AccountViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    val records by remember { mutableStateOf(viewModel.allRecords) }
    val expense by remember { mutableStateOf(viewModel.totalExpense) }
    val income by remember { mutableStateOf(viewModel.totalIncome) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("记账本") }) },
        floatingActionButton = {
            Button(onClick = { showDialog = true }) { Text("记一笔") }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Card(
                    modifier = Modifier.weight(1f).padding(end = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("总支出", style = MaterialTheme.typography.labelMedium)
                        Text("¥${String.format("%.2f", expense.value)}", style = MaterialTheme.typography.titleLarge)
                    }
                }
                Card(
                    modifier = Modifier.weight(1f).padding(start = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("总收入", style = MaterialTheme.typography.labelMedium)
                        Text("¥${String.format("%.2f", income.value)}", style = MaterialTheme.typography.titleLarge)
                    }
                }
            }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(records.value) { record ->
                    RecordItem(record)
                }
            }
        }
    }

    if (showDialog) {
        AddRecordDialog(
            onDismiss = { showDialog = false },
            onConfirm = { amount, type, remark ->
                viewModel.addRecord(amount, type, remark)
                showDialog = false
            }
        )
    }
}

@Composable
fun RecordItem(record: AccountRecord) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(record.remark, style = MaterialTheme.typography.titleMedium)
                Text(
                    SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date(record.time)),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Text(
                text = "${if (record.type == 0) "-" else "+"}¥${String.format("%.2f", record.amount)}",
                color = if (record.type == 0) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
fun AddRecordDialog(onDismiss: () -> Unit, onConfirm: (Double, Int, String) -> Unit) {
    var amountText by remember { mutableStateOf("") }
    var remark by remember { mutableStateOf("") }
    var type by remember { mutableStateOf(0) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("添加记录") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    label = { Text("金额") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = remark,
                    onValueChange = { remark = it },
                    label = { Text("备注") },
                    singleLine = true
                )
                Row {
                    Button(
                        onClick = { type = 0 },
                        modifier = Modifier.weight(1f).padding(end = 4.dp),
                        colors = if (type == 0) ButtonDefaults.buttonColors() else ButtonDefaults.outlinedButtonColors()
                    ) { Text("支出") }
                    Button(
                        onClick = { type = 1 },
                        modifier = Modifier.weight(1f).padding(start = 4.dp),
                        colors = if (type == 1) ButtonDefaults.buttonColors() else ButtonDefaults.outlinedButtonColors()
                    ) { Text("收入") }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val amount = amountText.toDoubleOrNull() ?: 0.0
                if (amount > 0) onConfirm(amount, type, remark.ifBlank { "无备注" })
            }) { Text("确定") }
        },
        dismissButton = {
            Button(onClick = onDismiss) { Text("取消") }
        }
    )
}

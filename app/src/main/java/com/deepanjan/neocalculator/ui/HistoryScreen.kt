package com.deepanjan.neocalculator.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.deepanjan.neocalculator.viewmodel.HistoryViewModel
import com.deepanjan.neocalculator.viewmodel.CalculatorViewModel
import com.deepanjan.neocalculator.data.HistoryEntity

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HistoryScreen(navController: NavController) {
    val historyViewModel: HistoryViewModel = hiltViewModel()
    val calcViewModel: CalculatorViewModel = hiltViewModel()
    
    // সঠিক পদ্ধতি: 'by' ব্যবহার করা
    val historyList by historyViewModel.history
    var itemToDelete by remember { mutableStateOf<HistoryEntity?>(null) }

    // স্ক্রিনে ঢোকার সময় লেটেস্ট ডাটা লোড করা
    LaunchedEffect(Unit) {
        historyViewModel.loadHistory()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("History") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    TextButton(onClick = { historyViewModel.clearAll() }) {
                        Text("Clear All", color = MaterialTheme.colorScheme.error)
                    }
                }
            )
        }
    ) { padding ->
        if (historyList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No calculations yet")
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp)) {
                items(historyList, key = { it.id }) { item -> // 'key' দিলে লিস্ট আপডেট স্মুথ হয়
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                            .combinedClickable(
                                onClick = {
                                    calcViewModel.loadFromHistory(item.expression)
                                    navController.popBackStack()
                                },
                                onLongClick = { itemToDelete = item }
                            )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = item.expression, style = MaterialTheme.typography.titleMedium)
                            Text(text = "= ${item.result}", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }
        }

        if (itemToDelete != null) {
            AlertDialog(
                onDismissRequest = { itemToDelete = null },
                title = { Text("Delete Entry?") },
                text = { Text("Do you want to delete this specific calculation?") },
                confirmButton = {
                    TextButton(onClick = {
                        itemToDelete?.let { historyViewModel.deleteItem(it) }
                        itemToDelete = null
                    }) { Text("Delete", color = MaterialTheme.colorScheme.error) }
                },
                dismissButton = { TextButton(onClick = { itemToDelete = null }) { Text("Cancel") } }
            )
        }
    }
}

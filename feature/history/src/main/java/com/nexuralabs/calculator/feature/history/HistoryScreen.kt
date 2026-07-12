package com.nexuralabs.calculator.feature.history

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
import com.nexuralabs.calculator.core.data.db.HistoryEntity
import com.nexuralabs.calculator.core.navigation.returnSelectedExpressionToCalculator

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HistoryScreen(navController: NavController) {
    val historyViewModel: HistoryViewModel = hiltViewModel()

    val historyList by historyViewModel.history
    var itemToDelete by remember { mutableStateOf<HistoryEntity?>(null) }

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
                items(historyList, key = { it.id }) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                            .combinedClickable(
                                onClick = {
                                    // This used to grab hiltViewModel<CalculatorViewModel>() directly and
                                    // write to it - but Navigation-Compose scopes that ViewModel per
                                    // destination, so it silently updated a throwaway instance and never
                                    // reached the real Calculator screen. Now the value travels back
                                    // through the NavController's SavedStateHandle instead, which also
                                    // means this feature no longer needs to depend on feature:calculator.
                                    navController.returnSelectedExpressionToCalculator(item.expression)
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

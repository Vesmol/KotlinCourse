package org.example.project

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.pluralStringResource
import kotlinproject.composeapp.generated.resources.*

data class ShoppingListItem(
    val description: String,
    val bought: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToSecond: (String) -> Unit,
    onNavigateToLocation: () -> Unit
) {
    val shoppingList = remember {
        mutableStateListOf(
            ShoppingListItem("Молоко"),
            ShoppingListItem("Мука"),
            ShoppingListItem("Яйца")
        )
    }
    var newItemDesc by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var deleteItemIndex by remember { mutableStateOf<Int?>(null) }
    val itemToDelete = deleteItemIndex?.let { shoppingList[it] }

    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(Res.string.app_title)) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    actions = {
                        IconButton(onClick = {
                            val info = "Всего товаров: ${shoppingList.size}"
                            onNavigateToSecond(info)
                        }) {
                            Icon(Icons.Default.Info, contentDescription = "О приложении")
                        }
                        IconButton(onClick = onNavigateToLocation) {
                            Icon(Icons.Default.MyLocation, contentDescription = "Геолокация")
                        }
                    }
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = pluralStringResource(Res.plurals.items_count, shoppingList.size, shoppingList.size),
                    modifier = Modifier.padding(bottom = 8.dp),
                    style = MaterialTheme.typography.bodyMedium
                )

                LazyColumn {
                    item {
                        OutlinedTextField(
                            value = newItemDesc,
                            onValueChange = { newItemDesc = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text(stringResource(Res.string.input_label)) },
                            trailingIcon = {
                                IconButton(onClick = {
                                    if (newItemDesc.isNotBlank()) {
                                        val newItem = ShoppingListItem(newItemDesc.trim())
                                        shoppingList.add(newItem)
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Добавлено: ${newItem.description}")
                                        }
                                        newItemDesc = ""
                                    }
                                }) {
                                    Icon(Icons.Default.Add, contentDescription = stringResource(Res.string.add_button))
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    itemsIndexed(shoppingList) { index, item ->
                        ShoppingListElement(
                            item = item,
                            onBoughtChange = { shoppingList[index] = item.copy(bought = it) },
                            onDelete = { deleteItemIndex = index }
                        )
                    }
                }
            }
        }

        if (itemToDelete != null) {
            AlertDialog(
                onDismissRequest = { deleteItemIndex = null },
                title = { Text("Удаление") },
                text = { Text("Удалить \"${itemToDelete.description}\"?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            scope.launch {
                                snackbarHostState.showSnackbar("Удалено: ${itemToDelete.description}")
                            }
                            shoppingList.removeAt(deleteItemIndex!!)
                            deleteItemIndex = null
                        }
                    ) {
                        Text("Удалить")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { deleteItemIndex = null }) {
                        Text("Отмена")
                    }
                }
            )
        }
    }
}

@Composable
fun ShoppingListElement(
    item: ShoppingListItem,
    onBoughtChange: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Checkbox(
            checked = item.bought,
            onCheckedChange = onBoughtChange
        )
        Text(
            text = item.description,
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = onDelete) {
            Icon(
                Icons.Default.Delete,
                contentDescription = stringResource(Res.string.delete_desc)
            )
        }
    }
}
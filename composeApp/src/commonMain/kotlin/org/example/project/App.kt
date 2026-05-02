package org.example.project

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.pluralStringResource
import kotlinproject.composeapp.generated.resources.*

data class ShoppingListItem(
    val description: String,
    val bought: Boolean = false
)

@Composable
fun App() {
    val shoppingList = remember {
        mutableStateListOf(
            ShoppingListItem("Молоко"),
            ShoppingListItem("Мука"),
            ShoppingListItem("Яйца")
        )
    }
    var newItemDesc by remember { mutableStateOf("") }

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(Res.string.app_title),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(16.dp)
                )

                Text(
                    text = pluralStringResource(Res.plurals.items_count, shoppingList.size, shoppingList.size),
                    modifier = Modifier.padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    item {
                        OutlinedTextField(
                            value = newItemDesc,
                            onValueChange = { newItemDesc = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            label = { Text(stringResource(Res.string.input_label)) },
                            trailingIcon = {
                                IconButton(onClick = {
                                    if (newItemDesc.isNotBlank()) {
                                        shoppingList.add(ShoppingListItem(newItemDesc.trim()))
                                        newItemDesc = ""
                                    }
                                }) {
                                    Icon(Icons.Default.Add, contentDescription = stringResource(Res.string.add_button))
                                }
                            }
                        )
                    }

                    itemsIndexed(shoppingList) { index, item ->
                        ShoppingListElement(
                            item = item,
                            onBoughtChange = { shoppingList[index] = item.copy(bought = it) },
                            onDelete = { shoppingList.removeAt(index) }
                        )
                    }
                }
            }
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
            .padding(horizontal = 16.dp, vertical = 4.dp)
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
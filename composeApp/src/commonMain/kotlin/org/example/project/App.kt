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

data class ShoppingItem(
    val name: String,
    val isBought: Boolean = false
)

@Composable
fun App() {
    val shoppingList = remember { mutableStateListOf<ShoppingItem>() }
    var newItemName by remember { mutableStateOf("") }

    MaterialTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            // Поле ввода и кнопка добавления
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = newItemName,
                    onValueChange = { newItemName = it },
                    label = { Text("Название продукта") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        if (newItemName.isNotBlank()) {
                            shoppingList.add(ShoppingItem(newItemName.trim()))
                            newItemName = ""
                        }
                    }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Добавить")
                    Text("Добавить")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Список покупок
            LazyColumn {
                itemsIndexed(shoppingList) { index, item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = item.isBought,
                                onCheckedChange = { isChecked ->
                                    shoppingList[index] = item.copy(isBought = isChecked)
                                }
                            )
                            Text(
                                text = item.name,
                                modifier = Modifier.weight(1f),
                                style = if (item.isBought) MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                ) else MaterialTheme.typography.bodyLarge
                            )
                            IconButton(onClick = { shoppingList.removeAt(index) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Удалить")
                            }
                        }
                    }
                }
            }
        }
    }
}
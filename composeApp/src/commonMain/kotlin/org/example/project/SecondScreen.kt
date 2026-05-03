package org.example.project

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecondScreen(
    onBack: () -> Unit,
    initialPostId: Int = 1,
    onSavePostId: (Int) -> Unit = {}
) {
    var post by remember { mutableStateOf<Post?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var postId by remember { mutableStateOf(initialPostId) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        if (initialPostId > 0) {
            scope.launch {
                isLoading = true
                try {
                    post = NetworkService.fetchPost(initialPostId)
                } catch (e: Exception) {
                    post = null
                } finally {
                    isLoading = false
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Информация") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else if (post != null) {
                Card(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Заголовок: ${post!!.title}", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Текст: ${post!!.body}", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            } else {
                Text("Введите ID и нажмите кнопку", style = MaterialTheme.typography.bodyLarge)
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = postId.toString(),
                onValueChange = { newId ->
                    newId.toIntOrNull()?.let { postId = it }
                },
                label = { Text("ID поста") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(0.6f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    scope.launch {
                        onSavePostId(postId)
                        isLoading = true
                        try {
                            post = NetworkService.fetchPost(postId)
                        } catch (e: Exception) {
                            post = null
                        } finally {
                            isLoading = false
                        }
                    }
                }
            ) {
                Text("Загрузить пост #$postId")
            }
        }
    }
}
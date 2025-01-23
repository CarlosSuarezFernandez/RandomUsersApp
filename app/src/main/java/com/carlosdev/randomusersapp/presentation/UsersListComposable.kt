package com.carlosdev.randomusersapp.presentation

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.carlosdev.randomusersapp.data.model.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(viewModel: UsersViewModel = koinViewModel()) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "userList") {
        composable("userList") {
            UsersList(viewModel = viewModel, onUserClick = { user ->
                val userId = "${user.id?.name ?: ""}+*+${user.id?.value ?: ""}"
                navController.navigate("userDetail/$userId")
            })
        }
        composable("userDetail/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            if (userId != null) {
                viewModel.getUserById(userId)
            }
            val user by viewModel.user.collectAsState(null)
            user?.let {
                UserDetail(user = it)
            }
        }
    }
}

@Composable
fun UsersList(viewModel: UsersViewModel = koinViewModel(), onUserClick: (User) -> Unit) {
    val users by viewModel.users.collectAsState(initial = emptyList())
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var searchTerm by remember { mutableStateOf(TextFieldValue("")) }

    LaunchedEffect(Unit) {
        viewModel.getUsers()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars.only(WindowInsetsSides.Top))
            .padding(16.dp)
    ) {
        TextField(
            value = searchTerm,
            onValueChange = { newValue ->
                searchTerm = newValue
                coroutineScope.launch {
                    delay(300)
                    viewModel.filterUsers(newValue.text)
                }
            },
            label = { Text("Search") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                if (searchTerm.text.isNotEmpty()) {
                    IconButton(onClick = {
                        searchTerm = TextFieldValue("")
                        coroutineScope.launch {
                            viewModel.filterUsers("")
                        }
                    }) {
                        Icon(Icons.Filled.Clear, contentDescription = "Clear search")
                    }
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(users.size) { index ->
                if (index < users.size) {
                    val user = users[index]
                    UserCard(
                        user = user,
                        onDelete = { viewModel.deleteUser(it) },
                        onClick = { onUserClick(user) }
                    )
                } else {
                    println("Index out of bounds: $index, Size: ${users.size}")
                }
            }
            item {
                Button(
                    onClick = { viewModel.getMoreUsers() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Text(
                        text = "Load More Users",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun UserCard(user: User, onDelete: (User) -> Unit, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = user.picture.large,
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "${user.name.first} ${user.name.last}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = user.email,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = user.phone,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = { onDelete(user) }) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
fun UserDetail(user: User) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .windowInsetsPadding(WindowInsets.statusBars.only(WindowInsetsSides.Top))
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                AsyncImage(
                    model = user.picture.large,
                    contentDescription = null,
                    modifier = Modifier
                        .size(128.dp)
                        .clip(CircleShape)
                        .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "${user.name.first} ${user.name.last}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = user.email,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
                Divider()
                Spacer(modifier = Modifier.height(16.dp))
                UserInfoRow(label = "Gender", value = user.gender)
                UserInfoRow(label = "Location", value = "${user.location.street}, ${user.location.city}, ${user.location.state}")
                UserInfoRow(label = "Registered", value = user.registered.date)
            }
        }
    }
}

@Composable
fun UserInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$label:",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(2f)
        )
    }
}
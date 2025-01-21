package com.carlosdev.randomusersapp.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import com.carlosdev.randomusersapp.data.model.User

class UsersListComposable {

    @Preview
    @Composable
    fun UsersList(users: List<User>) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(users) { user ->
                UserCard(user)
            }
        }
    }

    @Composable
    fun UserCard(user: User) {
        Card(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(modifier = Modifier.padding(16.dp)) {
                Image(
                    painter = rememberAsyncImagePainter(user.picture.large),
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "${user.name.first} ${user.name.last}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = user.email, fontSize = 16.sp)
                    Text(text = user.phone, fontSize = 16.sp)
                }
            }
        }
    }
}
package com.example.studygether.View

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studygether.Model.Message
import com.example.studygether.Model.TextMessage
import com.example.studygether.R
import com.example.studygether.ViewModels.BottomBarState
import com.example.studygether.ViewModels.AppBarsViewModel
import com.example.studygether.ViewModels.TopBarState
import com.example.studygether.ui.theme.Typography
import com.example.studygether.ui.theme.tokens.AppSpacing

@Composable
fun ConvoListScreen(mainViewModel: AppBarsViewModel= viewModel(),
                    modifier: Modifier,
                    onNavigateToChat:(name:String, image:Int)->Unit)
{
    LaunchedEffect(Unit)
    {
        mainViewModel.setTitleBar(
            title = {Text("Direct Messages",style= Typography.headlineMedium)},
            actions = {IconButton(onClick={})
            {
                Icon(Icons.Default.Face,contentDescription = null)
            }
            }
        )

        mainViewModel.setBottomBarType(BottomBarState(BottomBars.NavBar))
    }

    val testMessage by remember { mutableStateOf(TextMessage(
        id="1",
        senderId = "Subash",
        timestamp = "June 8",
        isRead = false,
        content = "Test Message is being showed here"

    )) }
    Box(modifier=modifier)
    {
        Box(modifier = Modifier.fillMaxSize().background(color= MaterialTheme.colorScheme.primary)){
            LazyColumn(modifier = Modifier.fillMaxSize().background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            ).padding(all = AppSpacing.small),
                verticalArrangement = Arrangement.spacedBy(AppSpacing.medium)
            )
            {

                items(4)
                {
                    MessageCard(userName ="Subash" , testMessage,R.drawable.avatar,onClick=onNavigateToChat)

                }


            }
        }

    }

}


@Composable
fun MessageCard(
    userName:String,
    message: Message,
    image:Int,
    onClick: (name:String, image:Int) -> Unit ,
    modifier: Modifier = Modifier,
) {

    val name:String


    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick(userName,image) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,

        ) {

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)

            ) {
                Image(painter = painterResource(image),contentDescription = null)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top,
            ) {
                Text(text=message.senderId,
                    fontSize = 12.sp,
                    fontWeight = if (!message.isRead) FontWeight.Bold else FontWeight.Normal,
                )

                Spacer(modifier = Modifier.height(6.dp))
                when(message)
                {
                    is TextMessage -> Text(text=(message as TextMessage).content,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        modifier= Modifier.fillMaxWidth()

                    )
                    else -> {Text("Random message go brr")}
                }
            }
        }
    }
}


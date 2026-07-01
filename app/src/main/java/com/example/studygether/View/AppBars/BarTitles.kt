package com.example.studygether.View.AppBars

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.studygether.ui.theme.Typography
import com.example.studygether.ui.theme.tokens.AppSpacing




@Composable
fun ChatUserLabelCard(
    name:String,
    profileImage:Int,
    onClick: ()->Unit
)
{
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }.height(AppSpacing.huge),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.inverseOnSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 0.dp, vertical = 0.dp),
            verticalAlignment = Alignment.CenterVertically,

            ) {

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .padding( AppSpacing.tiny)

            ) {
                Image(painter = painterResource(profileImage), contentDescription = null,
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.inverseOnSurface))
            }

            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.padding(AppSpacing.tiny)
            )
            {
                Text(
                    text = name,
                    style = Typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(6.dp))
            }
        }
    }
}


@Composable
fun ChannelTitleCard( channelName:String,
                      channelLogo:Int,
                      channelMemberCount:Int,
                      )
{
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.inverseOnSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 0.dp, vertical = 0.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,

            )
        {
            Column(Modifier.fillMaxWidth().weight(1f))
            {
                Box(contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .padding( AppSpacing.tiny))
                {
                    Image(painter = painterResource(channelLogo),contentDescription = null,
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.inverseOnSurface))

                }

            }
            Column(Modifier.fillMaxWidth().weight(4f))
            {
                Text(channelName,style=Typography.titleMedium,color = MaterialTheme.colorScheme.onSurface)
                Text(channelMemberCount.toString(),style=Typography.bodyMedium,color = MaterialTheme.colorScheme.onSurface)

            }
        }
    }


}
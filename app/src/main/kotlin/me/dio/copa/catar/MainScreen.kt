package me.dio.copa.catar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import me.dio.copa.catar.domain.extensions.getDate
import me.dio.copa.catar.domain.model.MatchDomain
import me.dio.copa.catar.domain.model.TeamDomain
import me.dio.copa.catar.ui.theme.Shapes

typealias NotificationOnClick = (match: MatchDomain) -> Unit

@Composable
fun MainScreen(matches: List<MatchDomain>, onNotificationClick: NotificationOnClick) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(brush = Brush.linearGradient(colors = listOf(Color(30,0,0), Color.Black)))
    ) {
        TitleSoccer()
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {

            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(matches) { match ->
                    MatchInfo(match, onNotificationClick)
                }
            }
        }
    }
}

@Preview
@Composable
fun TitleSoccer() {
    Column(
        modifier = Modifier.padding(12.dp).fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_soccer),
            contentDescription = null,
            modifier = Modifier
                .size(84.dp)
                .padding(end = 6.dp),

            )
        Text(
            text = "Copa do Mundo 2022",
            style = MaterialTheme.typography.h4.copy(color = Color.White),
            maxLines = 1,
        )
        Text(
            text = "Jogos",
            style = MaterialTheme.typography.h5.copy(color = Color.White),
        )
    }

}

@Composable
fun MatchInfo(match: MatchDomain, onNotificationClick: NotificationOnClick) {
    Card(shape = Shapes.large, modifier = Modifier.fillMaxWidth(), elevation = 8.dp) {
        Box {
            AsyncImage(
                model = match.stadium.image,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.height(160.dp),
                colorFilter = if (!match.notificationEnabled) ColorFilter.tint(Color.Black, BlendMode.Color) else null

            )

            Column(
                Modifier.padding(16.dp)
            ) {
                Notification(match = match, onNotificationClick)
                Title(match = match)
                Teams(match = match)
            }
        }
    }
}

@Composable
fun Notification(match: MatchDomain, onClick: NotificationOnClick) {
    val drawable = if (match.notificationEnabled) R.drawable.ic_notifications_active
    else R.drawable.ic_notifications
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        Image(
            painter = painterResource(id = drawable),
            contentDescription = null,
            modifier = Modifier.clickable {
                onClick(match)
            }

        )
    }
}

@Composable
fun Title(match: MatchDomain) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "${match.date.getDate()} - ${match.name}",
            style = MaterialTheme.typography.h6.copy(color = Color.White)
        )
    }
}

@Composable
fun Teams(match: MatchDomain) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        TeamItem(team = match.team1)
        Text(text = "x", modifier = Modifier.padding(end = 16.dp, start = 16.dp), style = MaterialTheme.typography.h6.copy(color = Color.White))
        TeamItem(team = match.team2)
    }
}

@Composable
fun TeamItem(team: TeamDomain) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = team.flag,
            modifier = Modifier.align(Alignment.CenterVertically),
            style = MaterialTheme.typography.h4.copy(color = Color.White)
        )
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            text = team.displayName,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h4.copy(color = Color.White)
        )
    }
}

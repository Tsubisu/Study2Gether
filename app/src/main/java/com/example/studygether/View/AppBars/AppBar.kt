package com.example.studygether.View.AppBars

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.outlined.Attachment
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.studygether.Navigation.bottomNavItems
import com.example.studygether.ViewModels.AppBarsViewModel
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.studygether.ViewModels.TopBarState
import com.example.studygether.ui.theme.Typography
import com.example.studygether.View.EmojiPickerSheet


enum class BottomBars
{
    NavBar,
    MessageBar,
    None
}

@Composable
fun TopBar(topBars: TopBarState,navController: NavController)
{
    when(topBars)
    {
        is TopBarState.TitleBar -> TitleBar(navController)
        is TopBarState.None -> Unit
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleBar(navController: NavController)
{
    val activity = LocalActivity.current as ComponentActivity
    val viewModel: AppBarsViewModel= viewModel(activity)
    val dividerColor= MaterialTheme.colorScheme.outlineVariant
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val navScreens = navBackStackEntry?.destination?.let { dest ->
        bottomNavItems.any { dest.hasRoute(it.route::class) }
    }?:false
    var state= viewModel.topBarState
    if(state== TopBarState.None) return
    else
        state=state as TopBarState.TitleBar
    TopAppBar(
        title = state.title,
        colors= TopAppBarDefaults.topAppBarColors(
            containerColor =if(state.barColor==Color.Unspecified){
                MaterialTheme.colorScheme.primary}else{state.barColor},
            titleContentColor = Color.White
        ),
        navigationIcon = {
            if (state.showBackButton) {
                IconButton (onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "Go back"
                    )
                }
            }
        },
        actions = state.actions,
        modifier =  if (!navScreens)
                        {Modifier.drawBehind({drawLine(color = dividerColor,
                            start = Offset(0f, size.height),
                            end = Offset(size.width, size.height),
                            strokeWidth = 2.dp.toPx())})
                        }
                    else
                    Modifier


    )
}
@Composable
fun BottomBar(bottomBars: BottomBars,navController: NavController)
{
    when(bottomBars){
        BottomBars.NavBar -> BottomNavBar(navController)
        BottomBars.MessageBar -> BottomMessageBar()
        BottomBars.None -> Unit

    }

}

@Composable
fun BottomNavBar(navController: NavController)
{
    val dividerColor= MaterialTheme.colorScheme.outlineVariant
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val showBottomNav = navBackStackEntry?.destination?.let { dest ->
        bottomNavItems.any { dest.hasRoute(it.route::class) }
    }?:false
    if(showBottomNav)
    {
            NavigationBar (
            modifier = Modifier.drawBehind({drawLine(color = dividerColor,
                start = Offset(0f, 0f),
                end = Offset(size.width, 0f),
                strokeWidth = 1.dp.toPx())})
                .fillMaxWidth(),
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            tonalElevation = 0.dp
        )
        {
            bottomNavItems.forEach { item ->
                var isSelected=navBackStackEntry?.destination?.hasRoute(item.route::class)?:false
                NavigationBarItem(
                    selected = isSelected,

                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(
                                navController.graph.findStartDestination().id
                            ) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },

                    icon = {
                        Icon(
                            imageVector = if (isSelected) item.selectedIcon
                                    else item.unSelectedIcon,
                            contentDescription = item.label,
                            Modifier.size(26.dp),
                            tint= MaterialTheme.colorScheme.primary
                        )
                    },
                    label = { Text(item.label,color= MaterialTheme.colorScheme.primary,fontWeight = if(isSelected) FontWeight.Bold else FontWeight.Normal, style = Typography.labelMedium) }
                )
            }
        }
    }
}


@Composable
fun BottomMessageBar()
{

    var showEmojiPicker by remember { mutableStateOf(false) }
    var textFieldValue by remember { mutableStateOf(TextFieldValue("")) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val dividerColor= MaterialTheme.colorScheme.outlineVariant

    BottomAppBar(modifier = Modifier.fillMaxWidth().drawBehind({drawLine(color = dividerColor,
        start = Offset(0f, 0f),
        end = Offset(size.width, 0f),
        strokeWidth = 1.dp.toPx())}),
        containerColor = MaterialTheme.colorScheme.background)
    {
        Row(modifier = Modifier.fillMaxWidth())
        {
            IconButton(onClick = {})
            {
                Icon(Icons.Outlined.Image,contentDescription = null)
            }
            IconButton(onClick = {})
            {
                Icon(Icons.Outlined.Attachment,contentDescription = null)
            }

            IconButton(onClick = {})
            {
                Icon(Icons.Outlined.Mic,contentDescription = null)
            }

            OutlinedTextField(
                value =textFieldValue,
                onValueChange = {textFieldValue=it},
                placeholder = {Text("Message")},
                shape = RoundedCornerShape(24.dp),
                trailingIcon = {IconButton(onClick = {
                    keyboardController?.hide()
                    showEmojiPicker = true
                }) {
                    Icon(Icons.Default.EmojiEmotions, contentDescription = "Emoji")
                }}

            )
        }

    }
    EmojiPickerSheet(
        visible = showEmojiPicker,
        onDismiss = { showEmojiPicker = false },
        onEmojiSelected = { emoji ->
            textFieldValue = insertEmoji(textFieldValue, emoji)
        }
    )
}
fun insertEmoji(current: TextFieldValue, emoji: String): TextFieldValue {
    val text = current.text
    val selection = current.selection
    val newText = text.substring(0, selection.start) +
            emoji +
            text.substring(selection.end)
    return TextFieldValue(
        text = newText,
        selection = TextRange(selection.start + emoji.length)
    )
}


@Preview
@Composable
fun TestPreview()
{
    BottomMessageBar()
}
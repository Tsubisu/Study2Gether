package com.example.studygether.View

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiFlags
import androidx.compose.material.icons.filled.Face
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
import androidx.compose.material3.Typography
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
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.studygether.Navigation.bottomNavItems
import com.example.studygether.ViewModels.MainActivityViewModel
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.studygether.ui.theme.Typography
import com.studygether.View.EmojiPickerSheet


enum class BottomBars
{
    NavBar,
    MessageBar,
    None
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavController)
{
    val viewModel: MainActivityViewModel= viewModel()
    val dividerColor= MaterialTheme.colorScheme.outlineVariant
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val navScreens = navBackStackEntry?.destination?.let { dest ->
        bottomNavItems.any { dest.hasRoute(it.route::class) }
    }?:false
    TopAppBar(
        title = viewModel.topBarState.title,
        colors= TopAppBarDefaults.topAppBarColors(
            containerColor =if(viewModel.topBarState.barColor==Color.Unspecified){
                MaterialTheme.colorScheme.primary}else{viewModel.topBarState.barColor},
            titleContentColor = Color.White
        ),
        navigationIcon = {
            if (viewModel.topBarState.showBackButton) {
                IconButton (onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "Go back"
                    )
                }
            }
        },
        actions = viewModel.topBarState.actions,
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
                strokeWidth = 1.dp.toPx())}),
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            tonalElevation = 0.dp
        )
        {
            bottomNavItems.forEach { item ->
                NavigationBarItem(
                    selected = navBackStackEntry?.destination?.hasRoute(item.route::class)?:false,

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
                            imageVector = item.icon,
                            contentDescription = item.label
                        )
                    },
                    label = { Text(item.label, style = Typography.labelMedium) }
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
                    Icon(Icons.Default.EmojiFlags, contentDescription = "Emoji")
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
package com.example.studygether.View

//import android.content.Context
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.grid.GridCells
//import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
//import androidx.compose.foundation.lazy.grid.items
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//import kotlinx.serialization.Serializable
//import kotlinx.serialization.builtins.ListSerializer
//import kotlinx.serialization.json.Json
//
//// ─────────────────────────────────────────────
//// Data
//// ─────────────────────────────────────────────
//
//@Serializable
//data class EmojiItem(
//    val char: String,
//    val name: String,
//    val category: String
//)
//
//private val json = Json { ignoreUnknownKeys = true }
//
//// Maps emoji.json category strings → display label + Material icon
//private data class CategoryMeta(val label: String, val icon: ImageVector)
//
//private val categoryMeta: Map<String, CategoryMeta> = mapOf(
//    "people"     to CategoryMeta("Smileys",  Icons.Default.EmojiEmotions),
//    "nature"     to CategoryMeta("Nature",   Icons.Default.EmojiNature),
//    "foods"      to CategoryMeta("Food",     Icons.Default.EmojiFoodBeverage),
//    "activity"   to CategoryMeta("Activity", Icons.Default.EmojiEvents),
//    "places"     to CategoryMeta("Travel",   Icons.Default.EmojiTransportation),
//    "objects"    to CategoryMeta("Objects",  Icons.Default.EmojiObjects),
//    "symbols"    to CategoryMeta("Symbols",  Icons.Default.EmojiSymbols),
//    "flags"      to CategoryMeta("Flags",    Icons.Default.EmojiFlags),
//)
//
//suspend fun loadEmojisByCategory(context: Context): Map<String, List<EmojiItem>> =
//    withContext(Dispatchers.IO) {
//        val rawJson = context.assets.open("emoji.json").bufferedReader().readText()
//        val list = json.decodeFromString(ListSerializer(EmojiItem.serializer()), rawJson)
//        val grouped = list.groupBy { it.category }
//        categoryMeta.keys.associateWith { key -> grouped[key] ?: emptyList() }
//            .filterValues { it.isNotEmpty() }
//    }
//
//
//
///**
// * Usage:
// *
// *   var showPicker by remember { mutableStateOf(false) }
// *   var textValue by remember { mutableStateOf(TextFieldValue("")) }
// *
// *   EmojiPickerSheet(
// *       visible = showPicker,
// *       onDismiss = { showPicker = false },
// *       onEmojiSelected = { emoji ->
// *           textValue = insertEmoji(textValue, emoji)
// *       }
// *   )
// */
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun EmojiPickerSheet(
//    visible: Boolean,
//    onDismiss: () -> Unit,
//    onEmojiSelected: (String) -> Unit,
//) {
//    if (!visible) return
//
//    val context = LocalContext.current
//    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
//
//    // Load emoji once from assets
//    var emojiMap by remember { mutableStateOf<Map<String, List<EmojiItem>>>(emptyMap()) }
//    var loading by remember { mutableStateOf(true) }
//
//    LaunchedEffect(Unit) {
//        emojiMap = loadEmojisByCategory(context)
//        loading = false
//    }
//
//    ModalBottomSheet(
//        onDismissRequest = onDismiss,
//        sheetState = sheetState,
//        tonalElevation = 4.dp,
//    ) {
//        if (loading) {
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(300.dp),
//                contentAlignment = Alignment.Center
//            ) {
//                CircularProgressIndicator()
//            }
//        } else {
//            EmojiPickerContent(
//                emojiMap = emojiMap,
//                onEmojiSelected = {
//                    onEmojiSelected(it)
//                    // Don't auto-dismiss — lets user pick multiple
//                }
//            )
//        }
//    }
//}
//
//
//@Composable
//private fun EmojiPickerContent(
//    emojiMap: Map<String, List<EmojiItem>>,
//    onEmojiSelected: (String) -> Unit,
//) {
//    val categories = remember(emojiMap) { emojiMap.keys.toList() }
//    var selectedTab by remember { mutableIntStateOf(0) }
//
//    if (categories.isEmpty()) {
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(300.dp),
//            contentAlignment = Alignment.Center
//        ) {
//            CircularProgressIndicator()
//        }
//        return
//    }
//
//    Column(modifier = Modifier.fillMaxWidth()) {
//
//        // Category tab row
//        ScrollableTabRow(
//            selectedTabIndex = selectedTab,
//            edgePadding = 8.dp,
//            divider = {},
//        ) {
//            categories.forEachIndexed { index, key ->
//                val meta = categoryMeta[key]
//                Tab(
//                    selected = selectedTab == index,
//                    onClick = { selectedTab = index },
//                    icon = {
//                        meta?.icon?.let {
//                            Icon(
//                                imageVector = it,
//                                contentDescription = meta.label,
//                                modifier = Modifier.size(22.dp)
//                            )
//                        }
//                    }
//                )
//            }
//        }
//
//        HorizontalDivider()
//
//        // Emoji grid for selected category
//        val currentEmojis = emojiMap[categories.getOrNull(selectedTab)] ?: emptyList()
//
//        LazyVerticalGrid(
//            columns = GridCells.Adaptive(minSize = 44.dp),
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(260.dp),
//            contentPadding = PaddingValues(8.dp),
//        ) {
//            items(currentEmojis, key = { it.char }) { emoji ->
//                Text(
//                    text = emoji.char,
//                    fontSize = 26.sp,
//                    textAlign = TextAlign.Center,
//                    modifier = Modifier
//                        .aspectRatio(1f)
//                        .clickable { onEmojiSelected(emoji.char) }
//                        .padding(4.dp)
//                )
//            }
//        }
//
//        // Bottom safe area padding
//        Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
//    }
//}
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studygether.Utility.emojiData
import com.example.studygether.ui.theme.tokens.AppSpacing

// ─────────────────────────────────────────────
// Recents — backed by SharedPreferences
// ─────────────────────────────────────────────

private const val PREFS_NAME = "emoji_recents"
private const val PREFS_KEY = "recents"
private const val MAX_RECENTS = 30

private fun loadRecents(context: Context): List<String> {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    val raw = prefs.getString(PREFS_KEY, "") ?: ""
    return if (raw.isEmpty()) emptyList() else raw.split(",")
}

private fun saveRecents(context: Context, recents: List<String>) {
    context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        .edit()
        .putString(PREFS_KEY, recents.joinToString(","))
        .apply()
}

private fun addToRecents(context: Context, emoji: String): List<String> {
    val current = loadRecents(context).toMutableList()
    current.remove(emoji)          // remove duplicate if exists
    current.add(0, emoji)          // add to front
    val trimmed = current.take(MAX_RECENTS)
    saveRecents(context, trimmed)
    return trimmed
}

// ─────────────────────────────────────────────
// Category metadata
// ─────────────────────────────────────────────

private data class CategoryMeta(val label: String, val icon: ImageVector)

private val categoryMeta: LinkedHashMap<String, CategoryMeta> = linkedMapOf(
    "Recent"            to CategoryMeta("Recent",   Icons.Default.History),
    "Smileys & Emotion" to CategoryMeta("Smileys",  Icons.Default.EmojiEmotions),
    "People & Body"     to CategoryMeta("People",   Icons.Default.EmojiPeople),
    "Animals & Nature"  to CategoryMeta("Nature",   Icons.Default.EmojiNature),
    "Food & Drink"      to CategoryMeta("Food",     Icons.Default.EmojiFoodBeverage),
    "Travel & Places"   to CategoryMeta("Travel",   Icons.Default.EmojiTransportation),
    "Activities"        to CategoryMeta("Activity", Icons.Default.EmojiEvents),
    "Objects"           to CategoryMeta("Objects",  Icons.Default.EmojiObjects),
    "Symbols"           to CategoryMeta("Symbols",  Icons.Default.EmojiSymbols),
    "Flags"             to CategoryMeta("Flags",    Icons.Default.EmojiFlags),
)

// ─────────────────────────────────────────────
// Main composable
// ─────────────────────────────────────────────

/**
 * Usage:
 *
 *   var showPicker by remember { mutableStateOf(false) }
 *   var textValue by remember { mutableStateOf(TextFieldValue("")) }
 *
 *   EmojiPickerSheet(
 *       visible = showPicker,
 *       onDismiss = { showPicker = false },
 *       onEmojiSelected = { emoji ->
 *           textValue = insertEmoji(textValue, emoji)
 *       }
 *   )
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmojiPickerSheet(
    visible: Boolean,
    onDismiss: () -> Unit,
    onEmojiSelected: (String) -> Unit,
) {
    if (!visible) return

    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Recents loaded once when sheet opens
    var recents by remember { mutableStateOf(loadRecents(context)) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        tonalElevation = 4.dp,
        containerColor = MaterialTheme.colorScheme.background
    ) {
        EmojiPickerContent(
            recents = recents,
            onEmojiSelected = { emoji ->
                recents = addToRecents(context, emoji)  // update recents tab live
                onEmojiSelected(emoji)
            }
        )
    }
}

// ─────────────────────────────────────────────
// Content: tabs + grid
// ─────────────────────────────────────────────

@Composable
private fun EmojiPickerContent(
    recents: List<String>,
    onEmojiSelected: (String) -> Unit,
) {
    // Build full map: Recents first (only if non-empty), then all categories from emojiData
    val emojiMap: Map<String, List<String>> = remember(recents) {
        buildMap {
            if (recents.isNotEmpty()) put("Recent", recents)
            putAll(emojiData)
        }
    }

    val categories = remember(emojiMap) { emojiMap.keys.toList() }

    // Start on Recents if available, otherwise Smileys
    var selectedTab by remember(recents.isNotEmpty()) {
        mutableIntStateOf(0)
    }

    if (categories.isEmpty()) return

    Column(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = AppSpacing.extraLarge),
            horizontalArrangement = Arrangement.Center
        ) {
            Card(
                shape = RoundedCornerShape(50),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState())
                ) {
                    categories.forEachIndexed { index, key ->
                        val meta = categoryMeta[key]
                        val selected = selectedTab == index
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(50))
                                .background(
                                    if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                                    else Color.Transparent
                                )
                                .clickable { selectedTab = index }.padding(horizontal = AppSpacing.hairline)
                        ) {
                            meta?.icon?.let {
                                Icon(
                                    imageVector = it,
                                    contentDescription = meta.label,
                                    modifier = Modifier.size(20.dp),
                                    tint = if (selected) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        val currentEmojis = emojiMap[categories.getOrNull(selectedTab)] ?: emptyList()

        if (currentEmojis.isEmpty()) {
            // Recents tab but nothing used yet
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No recent emoji yet",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 44.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp).padding(top= AppSpacing.small),
                contentPadding = PaddingValues(8.dp),
            ) {
                items(currentEmojis, key = { it }) { emoji ->
                    Text(
                        text = emoji,
                        fontSize = 26.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clickable { onEmojiSelected(emoji) }
                            .padding(4.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
    }
}
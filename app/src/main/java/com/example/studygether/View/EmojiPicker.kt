package com.studygether.View

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

// ─────────────────────────────────────────────
// Data
// ─────────────────────────────────────────────

@Serializable
data class EmojiItem(
    val char: String,
    val name: String,
    val category: String
)

private val json = Json { ignoreUnknownKeys = true }

// Maps emoji.json category strings → display label + Material icon
private data class CategoryMeta(val label: String, val icon: ImageVector)

private val categoryMeta: Map<String, CategoryMeta> = mapOf(
    "people"     to CategoryMeta("Smileys",  Icons.Default.EmojiEmotions),
    "nature"     to CategoryMeta("Nature",   Icons.Default.EmojiNature),
    "foods"      to CategoryMeta("Food",     Icons.Default.EmojiFoodBeverage),
    "activity"   to CategoryMeta("Activity", Icons.Default.EmojiEvents),
    "places"     to CategoryMeta("Travel",   Icons.Default.EmojiTransportation),
    "objects"    to CategoryMeta("Objects",  Icons.Default.EmojiObjects),
    "symbols"    to CategoryMeta("Symbols",  Icons.Default.EmojiSymbols),
    "flags"      to CategoryMeta("Flags",    Icons.Default.EmojiFlags),
)

suspend fun loadEmojisByCategory(context: Context): Map<String, List<EmojiItem>> =
    withContext(Dispatchers.IO) {
        val rawJson = context.assets.open("emoji.json").bufferedReader().readText()
        val list = json.decodeFromString(ListSerializer(EmojiItem.serializer()), rawJson)
        val grouped = list.groupBy { it.category }
        categoryMeta.keys.associateWith { key -> grouped[key] ?: emptyList() }
            .filterValues { it.isNotEmpty() }
    }



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

    // Load emoji once from assets
    var emojiMap by remember { mutableStateOf<Map<String, List<EmojiItem>>>(emptyMap()) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        emojiMap = loadEmojisByCategory(context)
        loading = false
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        tonalElevation = 4.dp,
    ) {
        if (loading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            EmojiPickerContent(
                emojiMap = emojiMap,
                onEmojiSelected = {
                    onEmojiSelected(it)
                    // Don't auto-dismiss — lets user pick multiple
                }
            )
        }
    }
}


@Composable
private fun EmojiPickerContent(
    emojiMap: Map<String, List<EmojiItem>>,
    onEmojiSelected: (String) -> Unit,
) {
    val categories = remember(emojiMap) { emojiMap.keys.toList() }
    var selectedTab by remember { mutableIntStateOf(0) }

    if (categories.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    Column(modifier = Modifier.fillMaxWidth()) {

        // Category tab row
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            edgePadding = 8.dp,
            divider = {},
        ) {
            categories.forEachIndexed { index, key ->
                val meta = categoryMeta[key]
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    icon = {
                        meta?.icon?.let {
                            Icon(
                                imageVector = it,
                                contentDescription = meta.label,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                )
            }
        }

        HorizontalDivider()

        // Emoji grid for selected category
        val currentEmojis = emojiMap[categories.getOrNull(selectedTab)] ?: emptyList()

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 44.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp),
            contentPadding = PaddingValues(8.dp),
        ) {
            items(currentEmojis, key = { it.char }) { emoji ->
                Text(
                    text = emoji.char,
                    fontSize = 26.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clickable { onEmojiSelected(emoji.char) }
                        .padding(4.dp)
                )
            }
        }

        // Bottom safe area padding
        Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
    }
}
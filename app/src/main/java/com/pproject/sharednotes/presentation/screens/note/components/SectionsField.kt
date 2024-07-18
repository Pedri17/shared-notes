package com.pproject.sharednotes.presentation.screens.note.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Section(
    val position: Int,
    val text: String,
    val type: Type,
) {
    enum class Type {
        SIMPLE_TEXT,
        CHECKBOX,
        ENUMERATION,
    }
}

@Composable
fun SectionsField(
    sections: List<Section>,
    onChange: (Section, String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
        items(
            items = sections,
            key = { section -> section.position }
        ) { section ->
            when (section.type) {
                Section.Type.SIMPLE_TEXT ->
                    TextSection(
                        value = section.text,
                        onChange = { newText ->
                            onChange(section, newText)
                        },
                        modifier = modifier,
                    )

                Section.Type.CHECKBOX ->
                    CheckboxSection(
                        text = section.text,
                        onChange = { newText ->
                            onChange(section, newText)
                        },
                        onChangeCheckbox = { changed ->
                            val newCage: String = when (changed) {
                                true -> "[V]"
                                else -> "[X]"
                            }
                            onChange(
                                section, section.text.replace(
                                    Regex("\\[[xXvV]]"),
                                    newCage
                                )
                            )
                        },
                        onNotMoveNext = {},
                        onBackspace = {},
                        modifier = modifier,
                    )

                Section.Type.ENUMERATION ->
                    EnumerationSection(
                        text = section.text,
                        onChange = { newText ->
                            onChange(section, newText)
                        },
                        modifier = modifier,
                    )
            }
        }
    }
}

@Composable
fun TextSection(
    value: String,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    SimpleTextEditable(
        text = value,
        onChange = onChange,
        modifier = modifier,
    )
}

@Composable
fun EnumerationSection(
    text: String,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BasicText(
            text = " · ",
            style = TextStyle(
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
            ),
            modifier = modifier
                .scale(2.0f)
                .padding(horizontal = 10.dp),
        )
        SimpleTextEditable(
            text = text.substring(2),
            onChange = { onChange(text.substring(0, 2) + it) },
            modifier = modifier,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckboxSection(
    text: String,
    onChange: (String) -> Unit,
    onChangeCheckbox: (Boolean) -> Unit,
    onNotMoveNext: () -> Unit,
    onBackspace: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    val isChecked: Boolean = when (text.substring(1, 2)) {
        "v", "V" -> true
        "x", "X" -> false
        else -> false
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = onChangeCheckbox,
                modifier = modifier.padding(horizontal = 10.dp)
            )
        }
        SimpleTextEditable(
            text = text.substring(3),
            onChange = { onChange(StringBuilder(text.substring(0, 3)).append(it).toString()) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    if (!focusManager.moveFocus(FocusDirection.Down)) {
                        onNotMoveNext()
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                }
            ),
            modifier = modifier,
        )
    }
}

/** Simple text composable used on every Section of content.
 * @param text the input [String] that will be shown.
 * @param onChange the callback that is triggered when the text is updated. The updated text is the
 * parameter of the callback.
 */
@Composable
fun SimpleTextEditable(
    text: String,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    keyboardActions: KeyboardActions = KeyboardActions()
) {
    BasicTextField(
        value = text,
        onValueChange = onChange,
        modifier = modifier,
        textStyle = TextStyle(
            fontSize = 16.sp,
            textAlign = TextAlign.Start,
            color = Color.White
        ),
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions
    )
}
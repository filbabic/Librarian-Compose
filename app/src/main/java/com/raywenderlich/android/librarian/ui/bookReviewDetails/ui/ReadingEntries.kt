package com.raywenderlich.android.librarian.ui.bookReviewDetails.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.raywenderlich.android.librarian.R
import com.raywenderlich.android.librarian.model.ReadingEntry
import com.raywenderlich.android.librarian.ui.composeUi.ActionButton
import com.raywenderlich.android.librarian.ui.composeUi.InputField
import com.raywenderlich.android.librarian.utils.formatDateToText

@Composable
fun AddReadingEntry(
  onDismiss: () -> Unit,
  onReadingEntryFinished: (String) -> Unit
) {
  val entryState = remember { mutableStateOf("") }

  Dialog(onDismissRequest = onDismiss) {
    Column(
      horizontalGravity = Alignment.CenterHorizontally,
      modifier = Modifier.background(MaterialTheme.colors.surface, shape = RoundedCornerShape(16.dp)),
    ) {

      Spacer(modifier = Modifier.height(16.dp))

      Text(
        text = stringResource(id = R.string.add_reading_entry_title),
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colors.onPrimary
      )

      Spacer(modifier = Modifier.height(8.dp))

      InputField(
        value = entryState.value,
        onStateChanged = { newValue -> entryState.value = newValue },
        isInputValid = entryState.value.isNotEmpty(),
        label = stringResource(id = R.string.reading_entry_hint)
      )

      Spacer(modifier = Modifier.height(8.dp))

      ActionButton(
        modifier = Modifier.fillMaxWidth(0.7f),
        text = stringResource(id = R.string.add_reading_entry_button_text),
        isEnabled = entryState.value.isNotEmpty(),
        onClick = { onReadingEntryFinished(entryState.value) }
      )
    }
  }
}

@Composable
fun ReadingEntries(
  readingEntries: List<ReadingEntry>,
  onItemLongClick: (ReadingEntry) -> Unit
) {
  LazyColumnFor(
    items = readingEntries,
    modifier = Modifier.padding(top = 16.dp, bottom = 96.dp),
  ) { entry ->
    ReadingEntryItem(entry, onItemLongClick)
  }
}

@Composable
fun ReadingEntryItem(
  entry: ReadingEntry,
  onItemLongClick: (ReadingEntry) -> Unit
) {
  Card(
    elevation = 8.dp,
    border = BorderStroke(1.dp, MaterialTheme.colors.primary),
    shape = RoundedCornerShape(16.dp),
    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
  ) {
    Row(
      Modifier.fillMaxWidth()
        .clickable(
          onClick = {},
          onLongClick = { onItemLongClick(entry) },
          indication = null
        )
    ) {
      Spacer(modifier = Modifier.width(16.dp))

      Column(Modifier.fillMaxWidth()) {

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = formatDateToText(entry.dateOfEntry),
          fontStyle = FontStyle.Italic,
          color = MaterialTheme.colors.primaryVariant,
          fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = entry.comment,
          modifier = Modifier.padding(end = 16.dp),
          color = MaterialTheme.colors.onPrimary
        )

        Spacer(modifier = Modifier.height(16.dp))
      }
    }
  }
}

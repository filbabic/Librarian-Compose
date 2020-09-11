package com.raywenderlich.android.librarian.ui.books.ui

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ColumnScope.gravity
import androidx.compose.material.Button
import androidx.compose.material.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.raywenderlich.android.librarian.R
import com.raywenderlich.android.librarian.model.Genre
import com.raywenderlich.android.librarian.ui.books.filter.ByGenre
import com.raywenderlich.android.librarian.ui.books.filter.ByRating
import com.raywenderlich.android.librarian.ui.books.filter.Filter
import com.raywenderlich.android.librarian.ui.composeUi.ActionButton
import com.raywenderlich.android.librarian.ui.composeUi.RatingBar
import com.raywenderlich.android.librarian.ui.composeUi.SpinnerPicker

@Composable
fun BookFilter(
  filter: Filter?,
  genres: List<Genre>,
  onFilterSelected: (Filter?) -> Unit
) {
  val currentFilter = remember {
    mutableStateOf(
      when (filter) {
        null -> 0
        is ByGenre -> 1
        is ByRating -> 2
      }
    )
  } // 0 - no filter, 1 - ByGenre, 2 - By Rating

  val currentGenreFilter = remember { mutableStateOf<Genre?>(null) }
  val currentRatingFilter = remember { mutableStateOf(0) }

  Column(
    modifier = Modifier
      .gravity(Alignment.CenterHorizontally),
    horizontalGravity = Alignment.CenterHorizontally
  ) {

    Column {
      Row {
        RadioButton(
          selected = currentFilter.value == 0, onClick = { currentFilter.value = 0 },
          modifier = Modifier.padding(8.dp)
        )

        Text(
          text = stringResource(id = R.string.no_filter),
          modifier = Modifier.gravity(Alignment.CenterVertically)
        )
      }

      Row {
        RadioButton(
          selected = currentFilter.value == 1,
          onClick = { currentFilter.value = 1 },
          modifier = Modifier.padding(8.dp)
        )

        Text(
          text = stringResource(id = R.string.filter_by_genre),
          modifier = Modifier.gravity(Alignment.CenterVertically)
        )
      }
      Row {
        RadioButton(
          selected = currentFilter.value == 2,
          onClick = { currentFilter.value = 2 },
          modifier = Modifier.padding(8.dp)
        )

        Text(
          text = stringResource(id = R.string.filter_by_rating),
          modifier = Modifier.gravity(Alignment.CenterVertically)
        )
      }
    }

    val currentlySelectedGenre = currentGenreFilter.value

    if (currentFilter.value == 1) {
      SpinnerPicker(
        preselectedItem = currentlySelectedGenre,
        pickerText = stringResource(id = R.string.genre_select),
        items = genres,
        itemToName = { genre -> genre.name },
        onItemPicked = { genre -> currentGenreFilter.value = genre })
    }

    if (currentFilter.value == 2) {
      RatingBar(
        range = 1..5,
        currentRating = currentRatingFilter.value,
        isLargeRating = true,
        onRatingChanged = { newRating -> currentRatingFilter.value = newRating })
    }

    ActionButton(
      modifier = Modifier.fillMaxWidth(),
      text = stringResource(id = R.string.confirm_filter),
      onClick = {
        val newFilter = when (currentFilter.value) {
          0 -> null
          1 -> ByGenre(currentGenreFilter.value?.id ?: "")
          2 -> ByRating(currentRatingFilter.value)
          else -> throw IllegalArgumentException("Unknown filter!")
        }

        onFilterSelected(newFilter)
      }
    )
  }
}
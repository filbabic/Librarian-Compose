package com.raywenderlich.android.librarian.ui.reviews

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raywenderlich.android.librarian.model.relations.BookReview
import com.raywenderlich.android.librarian.repository.LibrarianRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookReviewsViewModel @Inject constructor(
  private val repository: LibrarianRepository
) : ViewModel() {

  val bookReviewsState: Flow<List<BookReview>> = repository.getReviewsFlow()

  var deleteReviewState by mutableStateOf<BookReview?>(null)
    private set

  fun onItemLongTapped(bookReview: BookReview) {
    deleteReviewState = bookReview
  }

  fun deleteReview(bookReview: BookReview) {
    viewModelScope.launch {
      repository.removeReview(bookReview.review)
      onDialogDismissed()
    }
  }

  fun onDialogDismissed() {
    deleteReviewState = null
  }
}
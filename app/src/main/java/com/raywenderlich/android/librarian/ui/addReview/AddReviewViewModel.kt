package com.raywenderlich.android.librarian.ui.addReview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raywenderlich.android.librarian.model.Review
import com.raywenderlich.android.librarian.model.relations.BookAndGenre
import com.raywenderlich.android.librarian.model.state.AddBookReviewState
import com.raywenderlich.android.librarian.repository.LibrarianRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddReviewViewModel @Inject constructor(
  private val repository: LibrarianRepository
) : ViewModel() {

  var bookReviewState by mutableStateOf(AddBookReviewState())
    private set

  val booksState: Flow<List<BookAndGenre>> = repository.getBooksFlow()

  private lateinit var addReviewView: AddReviewView

  fun setView(addReviewView: AddReviewView) {
    this.addReviewView = addReviewView
  }

  fun addBookReview() {
    val state = bookReviewState

    viewModelScope.launch {
      val bookId = state.bookAndGenre.book.id
      val imageUrl = state.bookImageUrl
      val notes = state.notes
      val rating = state.rating

      if (bookId.isNotEmpty() && imageUrl.isNotBlank() && notes.isNotBlank()) {
        val bookReview = Review(
          bookId = bookId,
          rating = rating,
          notes = notes,
          imageUrl = imageUrl,
          lastUpdatedDate = Date(),
          entries = emptyList()
        )
        repository.addReview(bookReview)

        addReviewView.onReviewAdded()
      }
    }
  }

  fun onBookPicked(bookAndGenre: BookAndGenre) {
    bookReviewState = bookReviewState.copy(bookAndGenre = bookAndGenre)
  }

  fun onRatingSelected(rating: Int) {
    bookReviewState = bookReviewState.copy(rating = rating)
  }

  fun onImageUrlChanged(imageUrl: String) {
    bookReviewState = bookReviewState.copy(bookImageUrl = imageUrl)
  }

  fun onNotesChanged(notes: String) {
    bookReviewState = bookReviewState.copy(notes = notes)
  }
}
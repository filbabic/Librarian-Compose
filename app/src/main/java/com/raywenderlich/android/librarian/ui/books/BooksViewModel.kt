package com.raywenderlich.android.librarian.ui.books

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raywenderlich.android.librarian.model.Book
import com.raywenderlich.android.librarian.model.Genre
import com.raywenderlich.android.librarian.model.relations.BookAndGenre
import com.raywenderlich.android.librarian.repository.LibrarianRepository
import com.raywenderlich.android.librarian.ui.books.filter.ByGenre
import com.raywenderlich.android.librarian.ui.books.filter.ByRating
import com.raywenderlich.android.librarian.ui.books.filter.Filter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BooksViewModel @Inject constructor(
  private val repository: LibrarianRepository
) : ViewModel() {

  var filter: Filter? = null

  var booksState by mutableStateOf(emptyList<BookAndGenre>())
    private set

  var deleteBookState by mutableStateOf<BookAndGenre?>(null)
    private set

  var genresState by mutableStateOf<List<Genre>>(emptyList())

  fun loadGenres() {
    viewModelScope.launch {
      genresState = repository.getGenres()
    }
  }

  fun loadBooks() {
    viewModelScope.launch {
      booksState = when (val currentFilter = filter) {
        is ByGenre -> repository.getBooksByGenre(currentFilter.genreId)
        is ByRating -> repository.getBooksByRating(currentFilter.rating)
        else -> repository.getBooks()
      }
    }
  }

  fun removeBook(book: Book) {
    viewModelScope.launch {
      repository.removeBook(book)
      loadBooks()
      cancelDeleteBook()
    }
  }

  fun showDeleteBook(bookAndGenre: BookAndGenre) {
    deleteBookState = bookAndGenre
  }

  fun cancelDeleteBook() {
    deleteBookState = null
  }
}
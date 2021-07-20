package com.raywenderlich.android.librarian.ui.addBook

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raywenderlich.android.librarian.model.Book
import com.raywenderlich.android.librarian.model.Genre
import com.raywenderlich.android.librarian.model.state.AddBookState
import com.raywenderlich.android.librarian.repository.LibrarianRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddBookViewModel @Inject constructor(
  private val repository: LibrarianRepository
) : ViewModel() {

  var addBookState by mutableStateOf(AddBookState())
    private set

  var genresState by mutableStateOf<List<Genre>>(emptyList())
    private set

  private lateinit var view: AddBookView

  fun setView(view: AddBookView) {
    this.view = view
  }


  fun loadGenres() {
    viewModelScope.launch {
      genresState = repository.getGenres()
    }
  }

  fun onAddBookTapped() {
    val bookState = addBookState

    if (bookState.name.isNotEmpty() &&
      bookState.description.isNotEmpty() &&
      bookState.genreId.isNotEmpty()
    ) {
      viewModelScope.launch {
        repository.addBook(
          Book(
            name = bookState.name,
            description = bookState.description,
            genreId = bookState.genreId
          )
        )

        view.onBookAdded()
      }
    }
  }

  fun onNameChanged(name: String) {
    addBookState = addBookState.copy(name = name)
  }

  fun onDescriptionChanged(description: String) {
    addBookState = addBookState.copy(description = description)
  }

  fun genrePicked(genre: Genre) {
    addBookState = addBookState.copy(genreId = genre.id)
  }
}
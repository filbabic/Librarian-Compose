package com.raywenderlich.android.librarian.ui.readingListDetails

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raywenderlich.android.librarian.model.BookItem
import com.raywenderlich.android.librarian.model.ReadingList
import com.raywenderlich.android.librarian.model.relations.BookAndGenre
import com.raywenderlich.android.librarian.model.relations.ReadingListsWithBooks
import com.raywenderlich.android.librarian.repository.LibrarianRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReadingListDetailsViewModel @Inject constructor(
  private val repository: LibrarianRepository
) : ViewModel() {

  var addBookState by mutableStateOf(emptyList<BookItem>())
    private set

  var readingListState by mutableStateOf(ReadingListsWithBooks("", "", emptyList()))
    private set

  var deleteBookState by mutableStateOf<BookAndGenre?>(null)
    private set

  fun setReadingList(readingListsWithBooks: ReadingListsWithBooks) {
    viewModelScope.launch {
      repository.getReadingListByIdFlow(readingListsWithBooks.id).collect {
        readingListState = it
      }
    }

    refreshBooks()
  }

  fun refreshBooks() {
    viewModelScope.launch {
      val books = repository.getBooks()
      val readingListBooks = readingListState.books.map { it.book.id }

      val freshBooks = books.filter { it.book.id !in readingListBooks }

      addBookState = freshBooks.map { BookItem(it.book.id, it.book.name, false) }
    }
  }

  fun addBookToReadingList(bookId: String?) {
    val data = readingListState

    if (bookId != null) {
      val bookIds = (data.books.map { it.book.id } + bookId).distinct()

      val newReadingList = ReadingList(
        data.id,
        data.name,
        bookIds
      )

      updateReadingList(newReadingList)
    }
  }

  fun onItemLongTapped(bookAndGenre: BookAndGenre) {
    deleteBookState = bookAndGenre
  }

  fun onDialogDismiss() {
    deleteBookState = null
  }

  fun removeBookFromReadingList(bookId: String) {
    val data = readingListState

    if (data.id.isNotEmpty()) {
      val bookIds = data.books.map { it.book.id } - bookId

      val newReadingList = ReadingList(
        data.id,
        data.name,
        bookIds
      )

      updateReadingList(newReadingList)
      onDialogDismiss()
    }
  }

  private fun updateReadingList(newReadingList: ReadingList) {
    viewModelScope.launch {
      repository.updateReadingList(newReadingList)

      refreshBooks()
    }
  }

  fun bookPickerItemSelected(bookItem: BookItem) {
    val books = addBookState
    val newBooks = books.map { BookItem(it.bookId, it.name, it.bookId == bookItem.bookId) }

    addBookState = newBooks
  }
}
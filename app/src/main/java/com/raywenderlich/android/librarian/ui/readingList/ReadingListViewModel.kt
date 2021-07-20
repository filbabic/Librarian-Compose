package com.raywenderlich.android.librarian.ui.readingList

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raywenderlich.android.librarian.model.ReadingList
import com.raywenderlich.android.librarian.model.relations.ReadingListsWithBooks
import com.raywenderlich.android.librarian.repository.LibrarianRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReadingListViewModel @Inject constructor(
  private val repository: LibrarianRepository
) : ViewModel() {

  val readingListsState: Flow<List<ReadingListsWithBooks>> =
    repository.getReadingListsFlow()

  var isShowingAddReadingListState by mutableStateOf(false)
    private set

  var deleteReadingListState by mutableStateOf<ReadingListsWithBooks?>(null)
    private set

  fun onAddReadingListTapped() {
    this.isShowingAddReadingListState = true
  }

  fun deleteReadingList(readingListsWithBooks: ReadingListsWithBooks) {
    viewModelScope.launch {
      repository.removeReadingList(
        ReadingList(
          readingListsWithBooks.id,
          readingListsWithBooks.name,
          readingListsWithBooks.books.map { it.book.id }
        )
      )

      onDialogDismiss()
    }
  }

  fun onDeleteReadingList(readingListsWithBooks: ReadingListsWithBooks) {
    deleteReadingListState = readingListsWithBooks
  }

  fun addReadingList(readingListName: String) {
    viewModelScope.launch {
      repository.addReadingList(ReadingList(name = readingListName, bookIds = emptyList()))

      onDialogDismiss()
    }
  }

  fun onDialogDismiss() {
    isShowingAddReadingListState = false
    deleteReadingListState = null
  }
}
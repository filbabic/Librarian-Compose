package com.raywenderlich.android.librarian.ui.readingList

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.raywenderlich.android.librarian.model.ReadingList
import com.raywenderlich.android.librarian.model.relations.ReadingListsWithBooks
import com.raywenderlich.android.librarian.repository.LibrarianRepository
import kotlinx.coroutines.launch

class ReadingListViewModel @ViewModelInject constructor(
  private val repository: LibrarianRepository
) : ViewModel() {

  val readingListsState: LiveData<List<ReadingListsWithBooks>> =
    repository.getReadingListsFlow().asLiveData()

  private val _isShowingAddReadingListState = MutableLiveData(false)
  val isShowingAddReadingListState: LiveData<Boolean> = _isShowingAddReadingListState

  private val _deleteReadingListState = MutableLiveData<ReadingListsWithBooks>()
  val deleteReadingListState: LiveData<ReadingListsWithBooks> = _deleteReadingListState

  fun onAddReadingListTapped() {
    this._isShowingAddReadingListState.value = true
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
    _deleteReadingListState.value = readingListsWithBooks
  }

  fun addReadingList(readingListName: String) {
    viewModelScope.launch {
      repository.addReadingList(ReadingList(name = readingListName, bookIds = emptyList()))

      onDialogDismiss()
    }
  }

  fun onDialogDismiss() {
    _isShowingAddReadingListState.value = false
    _deleteReadingListState.value = null
  }
}
package com.raywenderlich.android.librarian.ui.readingList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.raywenderlich.android.librarian.model.relations.ReadingListsWithBooks
import com.raywenderlich.android.librarian.repository.LibrarianRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReadingListViewModel @Inject constructor(
  private val repository: LibrarianRepository
) : ViewModel() {

  val readingListsState: LiveData<List<ReadingListsWithBooks>> =
    repository.getReadingListsFlow().asLiveData()

  private val _isShowingAddReadingListState = MutableLiveData(false)
  val isShowingAddReadingListState: LiveData<Boolean> = _isShowingAddReadingListState

  private val _deleteReadingListState = MutableLiveData<ReadingListsWithBooks>()
  val deleteReadingListState: LiveData<ReadingListsWithBooks> = _deleteReadingListState
}
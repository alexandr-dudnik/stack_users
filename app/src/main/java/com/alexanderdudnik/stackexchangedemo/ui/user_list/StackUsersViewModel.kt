package com.alexanderdudnik.stackexchangedemo.ui.user_list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alexanderdudnik.stackexchangedemo.App
import com.alexanderdudnik.stackexchangedemo.data.StackUserEntity
import com.alexanderdudnik.stackexchangedemo.data.toStackUserEntity
import com.alexanderdudnik.stackexchangedemo.ui.user_info.StackUsersInfoModel
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Stack users view model
 */
class StackUsersViewModel: ViewModel() {
    private val _userList = mutableListOf<StackUserEntity>()
    private val _usersLiveData = MutableLiveData<List<StackUserEntity>>()
    private val _stateLiveData = MutableLiveData<LoadingState>()

    val usersLiveData:LiveData<List<StackUserEntity>>
        get() = _usersLiveData
    val stateLiveData:LiveData<LoadingState>
        get() = _stateLiveData

    private var filter : String? = null
    private var lastPageLoaded = 0
    private var endOfList = false

    private lateinit var apiDisposable: Disposable


    /**
     * Apply filter to the list
     *
     * @param newFilter
     */
    fun setFilter(newFilter:String){
        if (filter != newFilter) {
            filter = newFilter

            loadUsers(initial = true)
        }
    }

    /**
     * Load bunch of users from api
     *
     * @param initial - defines if we need to load from first page
     */
    fun loadUsers(initial:Boolean = false){
        if (initial){
            _userList.clear()
            lastPageLoaded = 0
            endOfList = false
        }
        if (endOfList) return

        apiDisposable = App.getSelf().retrofit.getUserList(filter = filter?:"", page = lastPageLoaded+1, pageSize = 20)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.newThread())
            .flatMap {
                endOfList = !it.hasMore
                Observable.fromIterable(it.userList)
            }
            .map { it.toStackUserEntity() }
            .doFinally {
                _stateLiveData.postValue(LoadingState.IDLE)
                apiDisposable.dispose()
            }
            .subscribe(
                {
                    _userList.add(it)
                },
                {
                    Log.e("API_ERROR", "API Call error", it)
                    _stateLiveData.postValue(LoadingState.IDLE)
                },
                {
                    lastPageLoaded++
                    _usersLiveData.postValue(_userList)
                },
                {
                    _stateLiveData.postValue(LoadingState.LOADING)
                }

            )
    }

    /**
     * State describing loading states
     */
    enum class LoadingState {
        IDLE,
        LOADING
    }
}
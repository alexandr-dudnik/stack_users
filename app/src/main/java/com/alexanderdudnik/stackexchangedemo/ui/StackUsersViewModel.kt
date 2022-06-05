package com.alexanderdudnik.stackexchangedemo.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alexanderdudnik.stackexchangedemo.App
import com.alexanderdudnik.stackexchangedemo.data.StackUserEntity
import com.alexanderdudnik.stackexchangedemo.data.toStackUserEntity
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

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


    fun setFilter(newFilter:String){
        if (filter != newFilter) {
            filter = newFilter

            loadUsers(initial = true)
        }
    }

    fun loadUsers(initial:Boolean = false){
        if (initial){
            _userList.clear()
            lastPageLoaded = 0
            endOfList = false
        }
        if (endOfList) return

        apiDisposable = App.getSelf().retrofit.getUserList(filter = filter?:"", page = lastPageLoaded+1, pageSize = 50)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.newThread())
            .doOnSubscribe { _stateLiveData.postValue(LoadingState.LOADING) }
            .flatMap {
                endOfList = !it.hasMore
                Observable.fromIterable(it.userList)
            }
            .map { it.toStackUserEntity() }
            .doOnNext {
                _userList.add(it)
            }
            .doOnError {
                Log.e("API_ERROR", "API Call error", it)
            }
            .doOnComplete {
                lastPageLoaded++
                _usersLiveData.postValue(_userList)
            }
            .doFinally {
                apiDisposable.dispose()
                _stateLiveData.postValue(LoadingState.IDLE)
            }
            .subscribe()
    }

    fun getUserData(id: Int): StackUserEntity?  = _userList.firstOrNull { it.accountId == id }

    enum class LoadingState {
        IDLE,
        LOADING
    }
}
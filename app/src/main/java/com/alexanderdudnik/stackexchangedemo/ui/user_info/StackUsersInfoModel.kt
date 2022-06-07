package com.alexanderdudnik.stackexchangedemo.ui.user_info

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alexanderdudnik.stackexchangedemo.App
import com.alexanderdudnik.stackexchangedemo.data.StackUserEntity
import com.alexanderdudnik.stackexchangedemo.data.toStackUserEntity
import com.alexanderdudnik.stackexchangedemo.ui.user_list.StackUsersViewModel
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Stack users view model
 */
class StackUsersInfoModel: ViewModel() {
    private val _userLiveData = MutableLiveData<StackUserEntity>()
    private val _stateLiveData = MutableLiveData<LoadingState>(LoadingState.LOADING)

    val userLiveData:LiveData<StackUserEntity>
        get() = _userLiveData
    val stateLiveData:LiveData<LoadingState>
        get() = _stateLiveData

    private lateinit var apiDisposable: Disposable

    /**
     * Load information about user from api
     *
     * @param userId
     */
    fun loadUserInfo(userId: Int){
        apiDisposable = App.getSelf().retrofit.getUserInfo(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.newThread())
            .flatMap {
                Observable.fromIterable(it.userList)
            }
            .map {
                it.toStackUserEntity()
            }
            .doFinally {
                apiDisposable.dispose()
            }
            .subscribe(
                {
                    _userLiveData.postValue(it)
                },
                {
                    Log.e("API_ERROR", "API Call error", it)
                    _stateLiveData.postValue(LoadingState.ERROR)
                },
                {
                    _stateLiveData.postValue(LoadingState.IDLE)
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
        LOADING,
        ERROR,
    }
}
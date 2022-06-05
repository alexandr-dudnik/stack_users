package com.alexanderdudnik.stackexchangedemo.network

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Api client for retrofit
 *
 */
interface ApiClient {

    /**
     * Get user list from API
     *
     * @param filter
     * @param page
     * @param pageSize
     * @return
     */
    @GET("2.3/users?order=asc&sort=name&site=stackoverflow")
    fun getUserList(
        @Query("inname") filter: String = "",
        @Query("page") page: Int = 1,
        @Query("pagesize") pageSize: Int = 50,
    ): Observable<StackUserListResponse>

}
package com.alexanderdudnik.stackexchangedemo.network

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
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
    @GET("?order=asc&sort=name&site=stackoverflow")
    fun getUserList(
        @Query("inname") filter: String = "",
        @Query("page") page: Int = 1,
        @Query("pagesize") pageSize: Int = 50,
    ): Observable<StackUserListResponse>

    @GET("{id}?site=stackoverflow")
    fun getUserInfo(@Path("id") userId: Int): Observable<StackUserListResponse>

}
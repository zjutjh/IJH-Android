/**
 * Implementations of APIResultCallAdapter.
 */


package com.zjutjh.ijh.network

import okhttp3.Request
import okio.Timeout
import retrofit2.*
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class APIResultCallAdapterFactory : CallAdapter.Factory() {

    override fun get(
        returnType: Type, annotations: Array<out Annotation>, retrofit: Retrofit
    ): CallAdapter<*, *> {
        // Check if the returnType is Call<T> type
        check(getRawType(returnType) == Call::class.java) { "$returnType must be retrofit2.Call." }
        check(returnType is ParameterizedType) { "$returnType must be parameterized. Raw types are not supported." }

        // Check if the R inside the Call<R> is APIResult<T> type (check if Call<APIResult<T>>)
        val apiResultType = getParameterUpperBound(0, returnType)
        check(getRawType(apiResultType) == Result::class.java) { "$apiResultType must be ApiResult." }
        check(apiResultType is ParameterizedType) { "$apiResultType must be parameterized. Raw types are not supported." }

        // Take out the T inside APIResult<T>
        val dataType = getParameterUpperBound(0, apiResultType)

        return ApiResultCallAdapter<Any>(dataType)
    }
}

class ApiResultCallAdapter<T>(private val type: Type) : CallAdapter<T, Call<Result<T>>> {

    override fun responseType(): Type = type

    override fun adapt(call: Call<T>): Call<Result<T>> {
        return APIResultCall(call)
    }
}

class APIResultCall<T>(private val delegate: Call<T>) : Call<Result<T>> {

    override fun enqueue(callback: Callback<Result<T>>) {
        delegate.enqueue(object : Callback<T> {

            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    val result = if (body == null) {
                        Result.Err(-1, "Empty response body")
                    } else {
                        Result.Ok(body)
                    }
                    callback.onResponse(this@APIResultCall, Response.success(result))
                } else {
                    val result = Result.Err(-2, "HTTP status code: ${response.code()}")
                    callback.onResponse(this@APIResultCall, Response.success(result))
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                val failureResult = if (t is APIResponseException) {
                    Result.Err(t.code, t.msg)
                } else {
                    val message = t.message
                    Result.Err(-1, (message ?: t.toString()))
                }

                callback.onResponse(this@APIResultCall, Response.success(failureResult))
            }
        })
    }

    @Deprecated(
        "Synchronous execution is not recommend.", replaceWith = ReplaceWith("enqueue()")
    )
    override fun execute(): Response<Result<T>> {
        try {
            val response = delegate.execute()
            return if (response.isSuccessful) {
                val body = response.body()
                val result = if (body == null) {
                    Result.Err(-1, "Empty response body")
                } else {
                    Result.Ok(body)
                }
                Response.success(result)
            } else {
                val result = Result.Err(-2, "HTTP status code: ${response.code()}")
                Response.success(result)
            }
        } catch (e: APIResponseException) {
            return Response.success(Result.Err(e.code, e.msg))
        } catch (t: Throwable) {
            val message = t.message
            return Response.success(Result.Err(-1, (message ?: t.toString())))
        }
    }

    override fun clone(): Call<Result<T>> = APIResultCall(delegate.clone())

    override fun isExecuted(): Boolean = delegate.isExecuted

    override fun cancel() = delegate.cancel()

    override fun isCanceled(): Boolean = delegate.isCanceled

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()
}
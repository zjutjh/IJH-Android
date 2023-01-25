package com.zjutjh.ijh.network.adapter

import com.zjutjh.ijh.exception.EmptyResponseException
import com.zjutjh.ijh.exception.HttpStatusException
import okhttp3.Request
import okio.Timeout
import retrofit2.*
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ResultCallAdapterFactory : CallAdapter.Factory() {

    override fun get(
        returnType: Type, annotations: Array<out Annotation>, retrofit: Retrofit
    ): CallAdapter<*, *> {
        // Check if the returnType is Call<T> type
        check(getRawType(returnType) == Call::class.java) { "$returnType must be retrofit2.Call." }
        check(returnType is ParameterizedType) { "$returnType must be parameterized. Raw types are not supported." }

        // Check if the R inside the Call<R> is APIResult<T> type (check if Call<APIResult<T>>)
        val resultType = getParameterUpperBound(0, returnType)
        check(getRawType(resultType) == Result::class.java) { "$resultType must be Result." }
        check(resultType is ParameterizedType) { "$resultType must be parameterized. Raw types are not supported." }

        // Take out the T inside APIResult<T>
        val dataType = getParameterUpperBound(0, resultType)

        // Unit type as nullable flag
        val nullable = if (getRawType(dataType) == Unit::class.java) Unit else null

        return ResultCallAdapter<Any>(dataType, nullable)
    }
}

internal class ResultCallAdapter<T>(private val type: Type, private val nullable: T?) :
    CallAdapter<T, Call<Result<T>>> {

    override fun responseType(): Type = type

    override fun adapt(call: Call<T>): Call<Result<T>> {
        return ResultCall(call, nullable)
    }
}

class ResultCall<T>(private val delegate: Call<T>, private val nullable: T? = null) :
    Call<Result<T>> {

    override fun enqueue(callback: Callback<Result<T>>) {
        delegate.enqueue(object : Callback<T> {

            override fun onResponse(call: Call<T>, response: Response<T>) =
                callback.onResponse(
                    this@ResultCall,
                    Response.success(responseHandler(response), response.raw())
                )

            override fun onFailure(call: Call<T>, t: Throwable) =
                callback.onResponse(this@ResultCall, Response.success(Result.failure(t)))

        })
    }

    @Deprecated(
        "Synchronous execution is not recommend.", replaceWith = ReplaceWith("enqueue()")
    )
    override fun execute(): Response<Result<T>> {
        return try {
            val response = delegate.execute()
            Response.success(responseHandler(response), response.raw())
        } catch (t: Throwable) {
            Response.success(Result.failure(t))
        }
    }

    private fun responseHandler(response: Response<T>): Result<T> =
        if (response.isSuccessful) {
            val body = response.body()
            if (body == null) {
                if (nullable != null) {
                    Result.success(nullable)
                } else {
                    Result.failure(
                        EmptyResponseException()
                    )
                }
            } else {
                Result.success(body)
            }
        } else {
            Result.failure(
                HttpStatusException(response.code())
            )
        }


    override fun clone(): Call<Result<T>> = ResultCall(delegate.clone())

    override fun isExecuted(): Boolean = delegate.isExecuted

    override fun cancel() = delegate.cancel()

    override fun isCanceled(): Boolean = delegate.isCanceled

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()
}
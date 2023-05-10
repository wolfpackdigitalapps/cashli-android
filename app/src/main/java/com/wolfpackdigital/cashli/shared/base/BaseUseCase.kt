package com.wolfpackdigital.cashli.shared.base

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wolfpackdigital.cashli.shared.exceptions.RefreshTokenExpiredException
import com.wolfpackdigital.cashli.shared.utils.extensions.getParsedError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

abstract class BaseUseCase<in P, R> where P : Any {

    abstract suspend fun run(params: P): Result<R>

    open suspend operator fun invoke(
        params: P,
        dispatcher: CoroutineContext = Dispatchers.Main,
    ): Result<R> {
        return withContext(dispatcher) {
            try {
                run(params)
            } catch (throwable: Throwable) {
                when (throwable) {
                    is RefreshTokenExpiredException -> {
                        throw throwable
                    }
                    else -> {
                        Log.e("HERE AND HERE", throwable.toString())
                        Result.Error(throwable.getParsedError())
                    }
                }
            }
        }
    }

    open operator fun invoke(
        scope: CoroutineScope,
        params: P,
        result: MutableLiveData<Result<R>>,
        dispatcher: CoroutineContext = Dispatchers.Main
    ) {
        scope.launch {
            withContext(dispatcher) {
                result.postValue(run(params))
            }
        }
    }

    open operator fun invoke(
        scope: CoroutineScope,
        params: P,
        dispatcher: CoroutineContext = Dispatchers.Main
    ): LiveData<Result<R>> {
        val result = MutableLiveData<Result<R>>()
        this(scope, params, result, dispatcher)
        return result
    }

    open suspend fun executeNow(params: P): Result<R> = run(params)
}

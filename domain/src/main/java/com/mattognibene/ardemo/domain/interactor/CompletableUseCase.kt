package com.mattognibene.ardemo.domain.interactor

import io.reactivex.Completable

abstract class CompletableUseCase<in T> : BaseUseCase<T, Completable>()
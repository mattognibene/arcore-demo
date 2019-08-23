package com.mattognibene.ardemo.domain.interactor

import io.reactivex.Observable

abstract class ObservableUseCase<in T, R> : BaseUseCase<T, Observable<R>>()
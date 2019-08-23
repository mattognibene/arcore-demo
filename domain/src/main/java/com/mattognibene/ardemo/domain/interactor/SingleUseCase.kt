package com.mattognibene.ardemo.domain.interactor

import io.reactivex.Single

abstract class SingleUseCase<in T, R> : BaseUseCase<T, Single<R>>()
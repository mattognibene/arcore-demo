package com.mattognibene.ardemo.domain.interactor

import io.reactivex.Maybe

abstract class MaybeUseCase<in T, R> : BaseUseCase<T, Maybe<R>>()
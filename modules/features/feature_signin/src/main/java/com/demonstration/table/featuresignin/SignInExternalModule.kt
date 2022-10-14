package com.demonstration.table.featuresignin

import com.demonstration.table.coreapi.keys.MediatorKey
import com.demonstration.table.featuresigninapi.SignInMediator
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface SignInExternalModule {
    @Binds
    @IntoMap
    @MediatorKey(SignInMediator::class)
    fun putExpensesMediator(tableSignInMediator: SignInMediatorImpl): Any
}
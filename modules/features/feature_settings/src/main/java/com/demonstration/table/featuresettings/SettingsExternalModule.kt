package com.demonstration.table.featuresettings

import com.demonstration.table.coreapi.keys.MediatorKey
import com.demonstration.table.featuresettingsapi.SettingsMediator
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface SettingsExternalModule {
    @Binds
    @IntoMap
    @MediatorKey(SettingsMediator::class)
    fun putSettingsMediator(greetingMediator: SettingsMediatorImpl): Any
}
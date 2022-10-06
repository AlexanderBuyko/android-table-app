package com.demonstration.table

import com.demonstration.table.featureregistration.RegistrationExternalModule
import com.demonstration.table.featurecomponents.ComponentsExternalModule
import com.demonstration.table.coreapi.providers.application.AppAggregatingProvider
import com.demonstration.table.coreapi.providers.application.CoreProvider
import com.demonstration.table.featurebooking.BookingExternalModule
import com.demonstration.table.featuregreeting.GreetingExternalModule
import com.demonstration.table.featurehome.HomeExternalModule
import com.demonstration.table.featuresignin.SignInExternalModule
import com.demonstrations.table.featuresettings.SettingsExternalModule
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [
        ComponentsExternalModule::class,
        RegistrationExternalModule::class,
        GreetingExternalModule::class,
        SignInExternalModule::class,
        BookingExternalModule::class,
        SettingsExternalModule::class,
        HomeExternalModule::class
    ],
    dependencies = [CoreProvider::class]
)
@Singleton
interface TableAppComponent : AppAggregatingProvider {

    @Component.Factory
    interface Factory {
        fun create(coreProvider: CoreProvider): TableAppComponent
    }

    companion object {
        fun create(coreProvider: CoreProvider): TableAppComponent {
            return DaggerTableAppComponent.factory().create(coreProvider)
        }
    }
}
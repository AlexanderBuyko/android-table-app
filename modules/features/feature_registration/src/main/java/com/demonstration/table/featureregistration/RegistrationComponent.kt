package com.demonstration.table.featureregistration

import com.demonstration.table.coreapi.providers.AggregatingProvider
import com.demonstration.table.coreapi.scopes.FeatureScope
import dagger.Component

@FeatureScope
@Component(dependencies = [AggregatingProvider::class])
interface RegistrationComponent {

    fun inject(fragment: RegistrationFragment)

    @Component.Factory
    interface Factory {
        fun create(aggregatingProvider: AggregatingProvider) : RegistrationComponent
    }

    companion object {
        fun create(aggregatingProvider: AggregatingProvider): RegistrationComponent {
            return DaggerRegistrationComponent.factory().create(aggregatingProvider)
        }
    }
}
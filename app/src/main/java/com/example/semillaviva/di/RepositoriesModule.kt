package com.example.semillaviva.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoriesModule {

  /*  @ExperimentalCoroutinesApi
    @Singleton
    @Binds*/
   // abstract fun bindDriverRepository(repository: DriverRepositoryImpl): DriverRepository
    //@Singleton
   // @Binds
    //abstract fun bindTravelRepository(repository: TravelRepositoryImpl): TravelRepository

}

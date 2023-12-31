package com.abdur.projectgetvideos.di

import com.abdur.projectgetvideos.MyApp
import dagger.Component
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface ApplicationComponent : AndroidInjector<MyApp>


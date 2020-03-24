package covid19.coronavirus.di.repository

import covid19.coronavirus.repository.CovidRepository
import covid19.coronavirus.repository.CovidRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    factory<CovidRepository> { CovidRepositoryImpl(androidContext(), get(), get(), get()) }
}
package covid19.coronavirus.di.main

import covid19.coronavirus.di.data.dataModule
import covid19.coronavirus.di.feature.*
import covid19.coronavirus.di.repository.repositoryModule

val listModule = listOf(
    dataModule,
    repositoryModule,
    splashModule,
    homeModule,
    searchModule,
    globalCasesModule,
    firebaseModule,
    feedbackModule
)
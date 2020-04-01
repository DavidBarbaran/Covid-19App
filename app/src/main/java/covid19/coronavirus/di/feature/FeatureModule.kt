package covid19.coronavirus.di.feature

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import covid19.coronavirus.feature.feedback.FeedbackViewModel
import covid19.coronavirus.feature.global.GlobalCasesAdapter
import covid19.coronavirus.feature.global.GlobalCasesViewModel
import covid19.coronavirus.feature.home.HomeViewModel
import covid19.coronavirus.feature.search.SearchAdapter
import covid19.coronavirus.feature.search.SearchViewModel
import covid19.coronavirus.feature.splash.SplashViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val splashModule = module {
    viewModel { SplashViewModel(androidApplication(), get(), get()) }
}

val homeModule = module {
    viewModel { HomeViewModel(androidApplication(), get(), get()) }
}

val searchModule = module {
    viewModel { SearchViewModel(androidApplication(), get()) }
    factory { SearchAdapter() }
}

val globalCasesModule = module {
    viewModel { GlobalCasesViewModel(get()) }
    factory { GlobalCasesAdapter() }
}

val firebaseModule = module {
    single { FirebaseRemoteConfig.getInstance() }
    single { FirebaseFirestore.getInstance() }
}

val feedbackModule = module {
    viewModel { FeedbackViewModel(androidApplication(), get()) }
}
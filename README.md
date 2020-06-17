
## Covid 19 App - Map, info & help

![](https://i.imgur.com/7xqYm47.png)

See the latest Covid 19 cases in more than 200 countries. 🌎

Also find out about the symptoms of this virus to take the necessary precautions and have an emergency guide at hand in case you suspect you are infected or someone close to you is.

Download app here  👉 [https://appcoronavirus.com](https://appcoronavirus.com)

### Why the application is not in the Play Store?

Being an informative app of covid19, which is a tragic event in the world, reporting confirmed, low and recovered, apparently we did not comply with the policies of sensitive events of Google Play, so they suspended and eliminated the application :(  

### Getting Started 🚀  
  
These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.  
  
### Pre requirements 📋  
- [Android Studio](https://developer.android.com/studio/)  
- [Git](https://git-scm.com/downloads)  
  
### Installation 🔧  
After download Android Studio and git in your local machine,  
execute command:  
  
> git clone https://github.com/DavidBarbaran/Covid19App 
  
Open Android Studio and click the option **Open an existing project in Android Studio** and select folder where your cloned project.  
  
  
### Architecture pattern usage: [MVVM](https://developer.android.com/jetpack/docs/guide)  
  
![](https://developer.android.com/topic/libraries/architecture/images/final-architecture.png)  
  
### Architecture Components usage:  
- [Room](https://developer.android.com/topic/libraries/architecture/room)  
- [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)  
- [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)  
  
### Dependencies usage in project:  
 - [Koin](https://github.com/InsertKoinIO/koin) for dependency injection.  
 - [Retrofit](https://github.com/square/retrofit) as an http client for web services consumption.  
 - [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) for async programming.  
 - [Gson](https://github.com/google/gson) for serialization/deserialization library to convert Java Objects into JSON and back.  
  
### Firebase dependencies usage in project:  
 - [Crashlytics](https://firebase.google.com/docs/crashlytics) for crash reporting.  
 - [Cloud Messaging](https://firebase.google.com/docs/crashlytics) for receiving push notifications.  
 - [Remote Config](https://firebase.google.com/docs/crashlytics) for changing variables of your app without requiring users to download an app update.  
 - [Firestore](https://firebase.google.com/docs/firestore) for save data.
 
### Reporting issues or improvements  🛠

Found a bug or a problem on a specific feature? Open an issue on  [Github issues](https://github.com/DavidBarbaran/Covid19App/issues)

### References & credits

Thanks for data 👉 

[CSSE](https://github.com/CSSEGISandData/COVID-19)

Thanks for API 👉 

[NovelCOVID](https://github.com/NovelCOVID)
[Nuttaphat Arunoprayoch](https://github.com/nat236919)

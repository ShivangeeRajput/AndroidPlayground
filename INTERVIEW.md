# Machine coding starter (non-functional shell)

Included: Compose + Material3, Navigation, Retrofit + Gson, Koin (DI),
safeCall, Loading/Error/Empty views, test dependencies, MainDispatcherRule.

## 90 minute plan
- 0-15 min: ask questions, write requirements + approach as comments
- 15-75 min: model -> repository -> ViewModel -> UI -> error handling -> 2-3 tests
- 75-90 min: run, explain trade-offs and what you would improve

## Questions to ask
- Which screens and actions are must-have?
- Real API or fake data? Is there pagination?
- Should data survive app restart? Offline support?
- What should happen on error / empty result?

## Write order (hamesha yahi)
1. ApiService.kt (data class + interface)
2. RetrofitClient.kt
3. Repository
4. ViewModel + State
5. Screen
6. di/AppModule + MainActivity -> RUN
7. Test (fake API)

## RetrofitClient (ratt lo)
```kotlin
object RetrofitClient {
    val api: ApiService = Retrofit.Builder()
        .baseUrl("https://example.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)
}
```

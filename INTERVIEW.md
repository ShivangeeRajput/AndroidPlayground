# Machine coding starter (non-functional shell)

Included: Compose + Material3, Navigation, Retrofit + Gson + logging, DataStore, Koin (DI),
UiState, safeCall, Loading/Error/Empty views, test dependencies, MainDispatcherRule.

## 90 minute plan
- 0-15 min: ask questions, write requirements + approach as comments
- 15-75 min: model -> repository -> ViewModel -> UI -> error handling -> 2-3 tests
- 75-90 min: run, explain trade-offs and what you would improve

## Questions to ask
- Which screens and actions are must-have?
- Real API or fake data? Is there pagination?
- Should data survive app restart? Offline support?
- What should happen on error / empty result?

## Write order (always the same)
1. `data/`  model, API interface, repository interface + impl
2. `ui/`    UiState, ViewModel (StateFlow), Screen (stateless composables)
3. `di/`    register in AppModule
4. NavHost route in MainActivity
5. Unit test the ViewModel or logic with a fake repository

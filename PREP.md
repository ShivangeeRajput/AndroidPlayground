# Paginated search + list + detail (simple version)

## Kaunsi file kya karti hai (isi order mein likho)
1. ApiService.kt        data class + API ke URL
2. RetrofitClient.kt    Retrofit object (HTTP + JSON)
3. ProductRepository.kt API call + error pakadna
4. ProductViewModel.kt  state, pagination, search
5. ProductListScreen.kt UI
6. di/AppModule.kt + MainActivity -> RUN
7. DetailViewModel + DetailScreen + navigation
8. Test

## Doc ki capabilities kahan cover hui
- Basic UI: ProductListScreen, DetailScreen
- HTTP client: RetrofitClient
- JSON: GsonConverterFactory
- State management: ViewModel + StateFlow
- Navigation: NavHost in MainActivity
- Storage: abhi nahi hai. Maange to SharedPreferences se last search save karo
- DI: AppModule (Koin)
- Testing: ProductViewModelTest

## 3 baatein yaad rakho
- Pagination: skip = products.size
- Search: job.cancel() + delay(400)
- loadMore guard: loading / error / sab aa gaya

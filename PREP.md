# Stock list + detail + polling (simple version)

## Likhne ka order
1. ApiService.kt      Stock + fake API
2. StockViewModel.kt  while(true) { call; delay }
3. StockScreens.kt    list + detail
4. StockApp.kt        navigation + start/stop
5. di/AppModule + MainActivity -> RUN
6. Test

## Yaad rakho
- Polling = while(true) + delay
- LifecycleStartEffect se background mein band
- Error pe purana data mat hatao

## Follow ups
- Repository layer, exponential backoff, WebSocket
- Detail ke liye alag API: getStock(symbol) alag se poll

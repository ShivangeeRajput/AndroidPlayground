# Stock market list + detail + polling

## Pehle poochna
- Real API ya fake? Polling interval? Background mein polling band honi chahiye?
- Error pe purana data dikhana hai ya error screen?

## Yaad rakhne wali 3 cheezein
1. Polling = `flow { while (true) { emit(safeCall{}); delay(x) } }`
2. `stateIn(WhileSubscribed(5000))` + `collectAsStateWithLifecycle` -> background mein auto stop
3. `runningFold` se purana data bachao jab poll fail ho

## Likhne ka order
Stock + API -> Repository.poll -> ViewModel -> list -> detail -> test

## Follow ups
- Exponential backoff on failures
- Polling vs WebSocket vs SSE
- Sirf visible symbols poll karna, batch API

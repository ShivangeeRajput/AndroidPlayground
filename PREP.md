# Paginated API + search + list/detail  (API: dummyjson.com/products)

## Pehle poochna
- Pagination kaisi hai: skip/limit, page number, ya cursor?
- Search server side hai? Detail ke liye alag API?

## Yaad rakhne wali 4 cheezein
1. Ek hi `job`: query badle to cancel (purana result naye ko overwrite nahi karega)
2. Debounce = request se pehle `delay(400)` usi job ke andar
3. Guard: `if (isLoading || endReached || error != null) return`
4. `skip = items.size`: page counter alag se rakhne ki zaroorat nahi

## Likhne ka order
model + API -> Repository -> ListViewModel -> ListScreen -> detail -> NavHost -> test

## Follow ups
- Paging 3 library, Room cache for offline, pull to refresh
- Flow version: queryFlow.debounce(400).distinctUntilChanged().flatMapLatest

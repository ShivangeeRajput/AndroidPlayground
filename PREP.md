# Custom LiveData

## Pehle poochna
- Lifecycle aware chahiye? Sticky behaviour? postValue? map/switchMap?

## Yaad rakhne wali 4 cheezein
1. `version` (data) vs `seenVersion` (observer) -> duplicate delivery nahi
2. Active = at least STARTED
3. ON_DESTROY pe remove (memory leak nahi)
4. postValue = main thread executor pe setValue

## Likhne ka order
observeForever + setValue (5 min mein working) -> version -> lifecycle -> postValue -> test

## Follow ups
- Real LiveData postValue jaldi jaldi call ho to sirf last value deta hai; yahan har value post hoti hai
- Real LiveData observer ke andar setValue (re-entrancy) alag flags se handle karta hai
- LiveData vs StateFlow: StateFlow ko initial value chahiye, equal values skip karta hai

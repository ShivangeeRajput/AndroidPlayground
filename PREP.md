# Custom LiveData (simple version)

## Likhne ka order
1. value + setValue + observer list
2. version / lastVersion
3. Lifecycle check (STARTED, DESTROYED)
4. postValue, screen, test

## Yaad rakho
- Sirf STARTED owner ko bhejo
- DESTROYED pe hatao (leak nahi)
- version se same value dobara nahi

## Follow ups
- observeForever, map / switchMap (MediatorLiveData)
- Real LiveData mein MutableLiveData alag hota hai, taaki UI value set na kar sake
- LiveData vs StateFlow

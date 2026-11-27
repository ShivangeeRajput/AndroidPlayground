# Image loading (bina Coil/Glide ke)

## Pehle poochna
- Library allowed hai? Agar haan to Coil `AsyncImage` 2 line hai. Zyaadatar "khud banao" bolte hain.
- Disk cache chahiye? Cache size limit?

## Yaad rakhne wali 4 cheezein
1. memory -> disk -> network
2. `inFlight` map of Deferred + Mutex: ek URL ek hi download
3. `inSampleSize` se downsample (OOM se bachav)
4. URL change / scroll pe purani coroutine cancel (produceState + key)

## Likhne ka order
download + decode + NetworkImage (chala ke dikhao) -> memory cache -> dedupe -> disk cache -> test

## Follow ups
- Disk cache eviction (LRU by lastModified, size limit)
- View size ke hisaab se maxSize (abhi 512 fixed)
- Download ko cancel karna jab koi bhi wait nahi kar raha (reference counting)
- placeholder, crossfade

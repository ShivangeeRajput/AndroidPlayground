# Image loading (simple version)

## Likhne ka order
1. ImageLoader.load: sirf download + decode
2. NetworkImage + grid -> RUN
3. Memory cache (LruCache)
4. Disk cache (getBytes), phir test

## Yaad rakho
- memory -> disk -> internet
- Network aur decode Dispatchers.IO pe
- LaunchedEffect(url): purani request cancel

## Follow ups (yahan nahi likha, bolna aana chahiye)
- Same url ek saath do baar download ho sakta hai: Map<url, Deferred> + Mutex
- LruCache count se nahi, size se: override sizeOf
- inSampleSize view size ke hisaab se
- Disk cache ka size limit; hashCode ki jagah SHA-256
- Retry button; production mein Coil

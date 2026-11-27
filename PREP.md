# Timer: with and without foreground service

## Pehle poochna
- Countdown ya stopwatch? Background / app close hone pe chalna chahiye?

## Yaad rakhne wali 4 cheezein
1. `endTime - now()` use karo, `time--` nahi (drift). Clock: `SystemClock.elapsedRealtime`
2. Start pe guard: `if (job?.isActive == true) return`
3. Engine pure Kotlin -> same code ViewModel, Service aur test mein
4. Service: permission (FOREGROUND_SERVICE + type), manifest entry, `startForeground` turant, scope cancel in onDestroy

## Likhne ka order
Engine -> test -> ViewModel + Screen (yahi pehle chala ke dikhao) -> Service + manifest

## Follow ups
- ViewModel timer rotation pe bachta hai, process death pe nahi (endTime save karo)
- Exact alarm chahiye to AlarmManager. WorkManager exact timer ke liye nahi hai
- START_STICKY vs START_NOT_STICKY
- Abhi notification mein Pause button nahi hai: PendingIntent + action se add hota hai

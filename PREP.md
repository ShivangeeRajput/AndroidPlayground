# Timer (simple version)

## Likhne ka order
1. Timer.kt           countdown logic
2. TimerViewModel.kt + TimerScreen.kt -> RUN (bina service wala ho gaya)
3. TimerService.kt + AndroidManifest (permission + service)
4. Test

## Yaad rakho
- job?.isActive guard, warna do timer chalenge
- Service: startForeground turant, scope.cancel() onDestroy mein

## Zaroor bolna (limitation)
`delay(1000); seconds--` thoda drift karta hai aur pause pe adha second kho jaata hai.
Fix: end time save karo
    val endTime = SystemClock.elapsedRealtime() + remainingMillis
    remainingMillis = endTime - SystemClock.elapsedRealtime()

## Follow ups
- Notification mein Pause button: PendingIntent
- Exact alarm: AlarmManager

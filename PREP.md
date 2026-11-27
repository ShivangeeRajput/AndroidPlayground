# Payment state machine

## Pehle poochna
- Problem ke baaki rules? (duplicate event, out of order event, thread safety, persistence)
- eventId globally unique hai ya per payment?

## Yaad rakhne wali 3 cheezein
1. Transition rules ek jagah: `PaymentStatus.canMoveTo` (when)
2. Duplicate event: `Set<eventId>` (idempotency)
3. Thread safety: `@Synchronized`, kyunki check + update atomic hona chahiye

## Likhne ka order
enum -> processor -> tests -> ViewModel -> Screen

## Follow ups
- Persist: Room table (payment + processed_events) ek transaction mein
- Zyada throughput: per paymentId lock (global eventId set ko thread safe rakhna padega)
- Out of order events: buffer karke baad mein apply

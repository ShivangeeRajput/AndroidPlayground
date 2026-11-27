# Payment state machine

## Question
- processEvent(paymentId, eventId, newStatus), har payment ka current status maintain karo
- Naya payment INITIATED
- Allowed: INITIATED->PENDING, INITIATED->FAILED, PENDING->SUCCESS, PENDING->FAILED
- SUCCESS aur FAILED terminal. Invalid transition se status nahi badalta

## Pehle poochna
- eventId ka kya karna hai? Question mein rule nahi hai. Duplicate ignore karna humara assumption hai
- Out of order events, thread safety, persistence chahiye?
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

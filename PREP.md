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

## Likhne ka order
1. PaymentProcessor.kt (enum + rules)
2. Test
3. ViewModel + Screen, AppModule, MainActivity

## Yaad rakho
- Rules `canMoveTo` mein
- Duplicate eventId: Set
- @Synchronized: check + update ek saath

## Follow ups
- Persist: Room, status + eventId ek transaction mein
- Result enum return karo taaki pata chale kyun ignore hua

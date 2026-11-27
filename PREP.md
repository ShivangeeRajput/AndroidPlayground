# Todo app

## Pehle poochna
- Sirf add/toggle/delete, ya edit aur detail screen bhi?
- Restart ke baad data rehna chahiye? Sync with server?

## Yaad rakhne wali 3 cheezein
1. Repository interface -> ViewModel test mein fake de sakte ho
2. `combine(todos, filter, error).stateIn(WhileSubscribed(5000))`
3. UI stateless: state neeche, events upar (state hoisting)

## Likhne ka order
Todo -> Repository (pehle in-memory MutableStateFlow, baad mein DataStore) -> ViewModel -> Screen -> test

## Follow ups
- DataStore vs Room: Room jab list badi ho, query/partial update chahiye
- Edit: `copy(title=...)`, swipe to delete: SwipeToDismissBox

## Test gotcha
- ViewModel ko test mein `by lazy` se banao, warna Dispatchers.setMain se pehle ban jaata hai

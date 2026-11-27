# Todo app (simple version)

## Likhne ka order
1. TodoStore.kt       Todo data class + storage
2. TodoViewModel.kt   add / toggle / delete
3. TodoScreen.kt      UI
4. di/AppModule + MainActivity -> RUN
5. Test

## Yaad rakho
- List ko kabhi badlo mat, nayi list banao (+, map, filter)
- Har change: state update + save

## Follow ups
- Filter All/Done: screen mein `todos.filter { it.done }`
- Edit: `copy(title = newTitle)`
- Badi list: Room database (background thread pe), Repository layer

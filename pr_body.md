## Factorial Screen — Performance & Stability Fixes

### Summary
`experiment/core-upgrade` → `main` merge এর জন্য FactorialScreen-এর সম্পূর্ণ optimization ও bugfix সেট।

### Changes

**Performance**
- Divide-and-conquer (binary split) BigInteger multiplication দিয়ে factorial calculation drastically fast করা হয়েছে
- Single SelectionContainer + monospace Text দিয়ে ছোট রেজাল্ট (≤5000 digit) render, যাতে UI জ্যাং না হয়
- বড় রেজাল্ট (>5000 digit, n up to 100,000 = ~456K digit) এর জন্য virtualized LazyColumn chunked rendering, ANR এড়াতে

**Bug Fixes**
- Empty/partial digit rendering bug ফিক্স হয়েছে (আগে দুই পাশে সংখ্যা কেটে যেত)
- Validation failure হলে আগের stale result আর দেখাবে না — এখন error দেখানোর সময় result state clear হয়
- CancellationException এখন আলাদা করে catch করে rethrow করা হয়, যাতে composable dispose হলে সেটাকে calculation error হিসেবে ভুল করে দেখানো না হয়
- Scientific notation output ফরম্যাট আরও readable করা হয়েছে (≈ x.xxx × 10^n)
- Input validation error message আরও নির্দিষ্ট (empty / invalid / negative / too large আলাদা করে দেখায়)

### CodeRabbit Review — Addressed
- ✅ Stale result on validation failure → fixed
- ✅ CancellationException swallowed → fixed (caught & rethrown before generic Exception)
- ✅ Non-virtualized single Text ANR risk on large input → fixed with threshold-based chunked LazyColumn

### Testing
- Verified factorial(20000) renders correctly and completely (no missing digits)
- Verified factorial(100000) doesn't freeze UI (chunked rendering kicks in)
- Verified error states clear old results properly

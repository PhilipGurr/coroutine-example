This is a simple example using Coroutines and Channels in Kotlin.

Expected output:
```
--- Async - Await ---
Result: 30

--- Channels ---
0
1
2
3
4

Receiver 1:  String #0
Receiver 2:  String #1
Receiver 1:  String #2
Receiver 2:  String #3
Receiver 1:  String #4

--- Flow API ---
Mail(receiver=test@mail.com, subject=Subject 1, message=Message 1)
Mail(receiver=test@mail.com, subject=Subject 2, message=Message 2)
```

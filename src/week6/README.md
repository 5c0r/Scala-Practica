## Week 6


#### Implicit conversion
- Implicit conversion can be made by :
    - Adding an implicit method for conversion , method will be called when a conversion is needed
    - Adding implicit class if you need more structural conversion , or just following convention
    - Order of conversion of implicit function or classes : From Companion object of source or target class -> Classes in Scope

- Threads and locks 
    - It's similar Java Thread!
    - Lock interface ( lock() and unlock() method)
        - ReentrantLock - waits if another thread is holding the lock, in other cases returns immediately ( the calling thread holds the lock )
        - ReentrantReadWriteLock - two types of lock: read and write. 
            - Multiple threads may hold a read lock
            - A write lock is held by single thread only , excludes both rw locks


- Shared multiprocessor memory:
    - Memory is far away from the processor . Cache needed
        - Modified : memory line has been modified in cache , but not other cache
        - Exclusive : Line is not modified , and not in any other cache
        - Shared: Line is not modified , other processor may have it in cache
        - Invalid : Line contains no valid data

- Parallel collection:
    - Collection can be turned parallel using **.par** ( some operations will be unavailable )
    - Parallel Loop depends if you are using **yield** or not  ( yield will preserve order )
    - Turn back to sequential collection with **.seq**
    
- Futures:
    - Helps to run block of code asynchronously
    - Future executions interleave in non-predictable way
    - Results is available after future has been completed
    - Getting future results using **Await.result** or callback **onComplete**
    - Futures can be combined , can be treated as collection


### Reflection
- Threads are familiar
- Futures combination and more usage is also familiar to me

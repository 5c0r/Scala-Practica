### Akka reflection week 3


## Chapter 5

- Futures : a block of code that can run asynchronously ( nobody blocks )
- Future result is available after future has completed 
```scala
val f3 = Future { Thread.sleep(10000); 42 }
val result = Await.result(f3, 11.seconds)
println("f3 result = " + result)
```

- Future can be combined , generally used with Higher-Order functions ( zip , fold , traverse )

```scala
val future1 = Future { getData1() }
val future2 = Future { getData2() }
val combined = future1.flatMap(n1 => future2.map(n2 => n1 + n2))

```
- Futures make it possible to define transformation from one result to the next => No surprise that functional approach needs to be taken to combine these result


## Chapter 6

Reason for distributed programming model:
- Latency
- Partial Failure
- Memory Access
- Concurrency

=> There are some circumstances which are needed to be taken into account when scaling out to network ( which local-only app will never/ rarely happends ) 

In remote system , actor can be connected by : 
- Using  .actorSelection(actorPath) for Actor lookup
- Deploy child remote node 
```scala
val ref = system.actorOf(Props[SampleActor]
.withDeploy(Deploy(scope = RemoteScope(address))))
```

## Chapter 8

### Pipe and Filter

- Use actors to implement filters
- Function ~ The order of the filters / pipes should not change the end result

```scala
class SpeedFilter(minSpeed: Int, pipe: ActorRef) extends Actor {
    def receive = {
    case msg: Photo =>
    if (msg.speed > minSpeed)
    pipe ! msg
    }
}
```

### Scatter - Gather
- Broadcasts a message to multiple recipients and re-aggregate the response back into single message
![](http://www.enterpriseintegrationpatterns.com/img/BroadcastAggregate.gif)

### Routing Slip
- Attach a Routing Slip to each message, specifying the sequence of processing steps. Wrap each component with a special message router that reads the Routing Slip and routes the message to the next component in the list.
![](http://www.enterpriseintegrationpatterns.com/img/RoutingTableSimple.gif)

## Reflection

- As far as I can understand , Futures can somehow be interpreted as a implementation of Async in different OOPL 
- A general implementation was called Rx, which has different library for many OOPL ( RxJava, Rx.NET )
- Future must be immutable ( no shared mutable state ) . It's the paradigm of Message passing pattern also , where message must be immutable
- Scaling out to network should be trivial for Akka
- The patterns in this chapter are similar to some EIP where the whole system works mainly on message passing 
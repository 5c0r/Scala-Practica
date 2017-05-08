## Streaming

## Clustering 

## Actor persistence

My favorite picture when talking about Event Sourcing , CQRS and DDD 

![EventSourcing](http://codeofrob.com/images/internal_codeofrob_com/DDDOverview_big.jpg)


- Actor Persistence : Stateful actors can achieve persistence by atomically persisting each event to their internal state . *State should not be persisted in the process*

- Recover state by playback the event history. Faster playback can be issued using snapshot ( Latest state = Snapshotted state + uncommitted events )

- Default backend for persistence is FileSystem , however it is **pluggable**

```scala
class PActor extends PersistentActor {
    var contributorList = List[Contributor]()
    var numOfContributor = 0

    // Recover
    override def receiveRecover: Receiver = {
        case RecoveryCompleted => println("Recovery Success")
        case AddContributor(n) => contributorList = n :: contributorList
        case _ => println("Unknown msg, ignored")
    }

    override def receiveCommand: Receive = {
        case e @ AddInt(n) =>
            persist(e) { event =>
                contributorList = n :: contributorList
            }
        case GetResult => println(contributorList.length);

        override def persistenceId: String = $"persist$self";
    }
}
```

- Cluster singleton is used to **make sure only one instance of actor is running in the whole system **

- **Handy** for centralization , **Unhandy** for scaling, performance and resilience

- TODO: Cluster sharding

### ACID
- Atomicity: Whole transaction is success or nothing is persisted
- Consistent: Starts from consistent state and leaves in consistent state
- Isolated: Doesn't interfere with other transaction
- Durable: Changes are made persistent

### CAP
- Consistency
- Availability
- Partition Tolerance
- You can only achieve only two within three of those properties.


** On Consistency **
- Strong: Updated value is returned after successful update
- Weak: Not guaranteed latest value to be returned
- Eventual Consistency: If no new update are applied then updated value will be returned by all read

** Eventual Consistency variations **
- Casual : A updates then informs B , B reads updated value
- Read-your-writes: Read from updater returns updated value
- Session: read-your-writes guaranteed while session is valid
- Monotonic-read: old value is never read after newer one is read
- Monotonic-write: writes from same process are serialized

### udpate + read options
- Update: WriteLocal , WriteTo(n), WriteMajority , WriteAll
- Read: ReadLocal, ReadFrom(n), ReadMajority, ReadAll

## CRDT
- Data types must be convergent CRDT ( Conflict Free Replicated Data Type )

Counters: GCounter, PNCounter
Sets: GSet, ORSet
Maps: ORMap, ORMultiMap, LWWMap, PNCounterMap
Registers: LWWRegister, Flag

## Reactive Manifesto

Reactive system is a system that :
- Responsive: Rapid and consistent response time
- Resilient: Part of system can fail and recover without compromising the whole system
- Elastic: Flexible allocation of resource depends on I/O
- Message-driven: Rely on async message-passing => Loose-coupling + Isolation + Location transparency


## Reflection
- These terminologies are pretty familiar with me since I am learning towards building a Reactive System
- Distributed persistence is always a pain and it is difficult to keep everything in sync
- CQRS, DDD, ES plays well together
- Event Storage is also a nice topic ( https://cqrs.wordpress.com/documents/building-event-storage/ )
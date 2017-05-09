## Streaming

- Akka streams is used to process and transfer sequence of events in a bounded buffer space
    - Stream : active process that involves moving and transform data
    - Element: Processing unit of stream . Elements are transferred upstream to downstream
    - Back-pressure: flow-control to notify a producer about their availability , effectively adjust to match consumption needs
    - Non-blocking: certain operation does not hinder the progress of calling thread 
    - Graph: Description of stream processing topology , defining the pathways through which elements shall flow when the stream is running

- Basic concepts :
    - Source: Processing stage with just one output , emit data when downstream are ready to receive
    - Flow: One input and one output , connect up/down stream by transfer elements 
    - Sink: Processing stage with exactly one input , request and accept data possibly slowing down upstream producer
    - Runnable Graph:  Flow with both ends attached to Source and Sink 
    -  -> Flow ->
    - Source -> Flow  == Source ->
    -   -> Flow -> Sink ==> -> Sink
    - Source -> Flow -> Sink == Runnable Graph

- Graph functions - Fan-out
    - Broadcast ( 1I - NO)
    - Balance ( 1I- NO) emits to one of its output
    - UnzipWith : 
    - Unzip ( 1I , 2O) : split a stream to 2 of different types

- Graph functions - Fan-in
    - Merge
    - MergePreferred
    - ZipWith
    - Zip
    - Concat
- Back Presssure : Producer can apply one of the below strategies
    - Not generate elements , if it is able to control production rate
    - Try buffering elements in bounded manner until more demand is signalled
    - Drop elements until more demand is signalled
    - Tear down stream if all fails
    => One way of control is to use buffer stage with overflow strategy


## Clustering 
- Akka Cluster features:
    - Membership in cluster is managed while allowing for node failures
    - Routing message to actors in different cluster , allow LB
    - Cluster nodes may be given roles , routing can be done based on roles

- List of seed nodes can be configure . Join is sent to each and the fastest responds
- Cluster member downing :
    - When a member becomes unreachable, joins and graceful leaves in the cluster are not possible -> need a tactic with it
    - Naive strat - mark unreachable as down 
        - Quorum : cluster part with at least quorum nodes stays up ,  lesser nodes -> down
        - Keep majority: Cluster part with majority of latest known number of nodes stays up
        - Keep oldeest : Cluster with oldest member stays up
        - Keep referee : Cluster with designated referee node stays up

- Routing to cluster nodes : ClusterRouterPool , using .actorSelection
- Load balancing : AdaptiveLoadBalancing, use some extra metrics
    - heap : used and max jvm heap memory . weight based on remaining heap capacity
    - load: system load average. weights based on remaining load capacity
    - cpu : CPU utilization in percentage. weight based on remaining cpu capacity
    - mix : combination of above . weight based on mean of remaining capacity

- ** ClusterClient ** is used to send message to cluster nodes
    - ClusterClient: message with ClusterReceptionist that should be run in each cluster node
    - ClusterClient.Send: send to recipient with matching path / multiple matchees
    - ClusterClient.SendToAll
    - ClusterClient.Publish

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


### Cluster sharding

- A logic is created to :
    - Identity the actor ( extractIdentityId )
    - Identity the shard
- ShardRegion actors take care of forwarding messages to shards ( with help from ShardingCoordinator) 
    - Creating new shard if needed
    - Shard fw message to another entity , creating if entity does not exist

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
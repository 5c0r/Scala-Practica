## Akka Week 4 : Routing 

Routers are essential when you want to scale up or out .
#### 3 Reasons to use Routers for Actors :

- **Performance** : Message can be processed in parallel . However each message may take time to process  => Message can be sent among instances
- **Message Content**: Depends on the content of the message , it  can be routed to a suitable router
- **State**: Depend on the State of router that where the message will be sent to

#### Balance Load using Akka routers
Another reason to use router is the balance the load over different actors when it is required to process a lot of message .
This can be scale up ( **local** actor) or scale out ( **remote** server)

The **router** logic will decide which routee is selected  and can be used withtin actor to make a selection

#### Two varieties of built-in routers
- Pool 
    - Manage Routees
    - Responsible for creating / removing routees when terminated
    - Can be used when :
        - when all routees are created and distributed the same way
        - isn't a need for special recovery
- Group 
    - Don't manage routees , don't watch routees => Have to implement somewhere else in the system
    - Routees have to be created by the system
    - Use actor selection to find routees
    - Can be used when:
        - When you need control the routees' lifecycle in special way
        - Need of gaining control over where routees are instantiated


| Logic  | Pool   | Group   | Description   |  
|---|---|---|---|
| Round-robin  | Yes  | Yes  | Send message to the first, then the second ,...n . Then back  |
| Random-Routing  | Yes   | Yes  | Randomly chosen routee   |
| Smallest mailbox  | Yes   | **No**  | Choose the routee with the smallest mailbox  |
| **No** | Balancing    | **No**   | Distributes message to idle routees. One mailbox for all routeees   |
| Broadcast  | Yes  | Yes   | Send the received message to all routees   |
| Scatter-Gather-FirstCompleted  | Yes  | Yes   | Send message to all routees and send the fastest response to sender   |
| ConsistentHashing  | Yes   | Yes   |  Use consistent hashing to select routee, different message will be routed to different routee  |

3 Most common : Round-Robin , Balancing , Content-Hashing


## Akka pool Router
- Can be used when :
    - All routees are created and distributed the same way
    - No special management / recovery needed 

Create pool router using configuration
```scala
val router = system.actorOf( // Define router using configuration
    FromConfig.props(Props(new GetLicense(endProbe.ref))// How router should cretae routees
    "poolRouter") // Name 
)

// COnfiguring router
akka.actor.deployment {
    /poolRouter {
        router = balancing-pool // Logic used
        nr-of-instance = 5 // number of routees
    }
}
```

Create in code 
```scala
var router = system.actorOf(
    BalancingPool(5).props(Props(new GetLicense(endProbe.ref))),
    "poolRouter"
)
```

**Huom!**

- Some message are processed by the router : **Kill** , **PoisonPill**
- **Broadcast** Messages work on pool and group
- Due to parent-child relationship , all routees will be terminated when router is terminated

#### Configuring remote routeees
- Possiblity to use routers between multiple servers

```scala
var addresses = Seq(address1, address2)
var routerRemote1 = system.actorOf(
    RemoteRouterConfig(FromConfig(),addresses).props(
        Props(new GetLicense(endProbe.ref))), "poolRouter-config")

val routerRemote2 = system.actorOf(
    RemoteRouterConfig( RoundRobinPool(5),addresses).props(
        Props(new GetLicense(endProbe.ref))), "poolRouter-code")
```

#### Dynamic resize poool

Later on you will want to change the number of routees depends on the number of message received 

```scala

akka.actor.deployment {
    /poolRouter {
        router = round-robin-pool
        resizer{
            enable = on // Enable resizer

            lower-bound =1 // Fewer number of  routees should ever have
            upper-bound = 100 // Maximum routees should have have

            pressure-threshold = 0.8 // when routees is under pressure 
            rampup-rate = 0.25 // How fast of adding routees
            backoff-threshhold = 0.3 // When number of routees should ecrease
            backoff-rate = 0.1 // How fast removing routees

            message-per-resize = 10 // How fast you 're able to resize
        }
    }
}

```

#### Supervision

- In pool router , router creates routees , so it is the supervisor of the actors
- If one routee fails , router will escalate to supervisors => Unwanted behavior where supervisor will restart router -> all routeees restartted 
==> We can give the router its own supervisor strategy whehn creating router

** When using resizer **
- The router will not terminate and keep minimum number of routees
- Router Pools are flexible 


## Group router
- You have to instantiate the routees yourself , but you have the wanted control **when** and **where** they are created


#### Using configuration
```scala

// First creator a routee creator
class RouteeCreator(nrActors: Int) extends Actor {
    override def preStart() {
        super.preStart();
        (0 until nrActors).map( nr =>
        context.actorOf(Props[GetLicense], "GetLicense" + nr))
        system.actorOf(Props( new RouteeCreator(5)),"RouteeCreatorr") // Create routees
    }
}

system.actorOf(Props( new RouteeCreator(5),"RouteeCreator")) // Create routee creator


// Configuration
akka.actor.deployment{
    /groupRouter {
        router = round-robin-group

        // Wow , remote configuration 
        routees.path = [
            "akka.tcp://AkkaSystemName@10.0.0.1:2552/user/RouteeCreatorr/GetLicense",
            "akka.tcp://AkkaSystemName@10.0.0.1:2553/user/RouteeCreatorr/GetLicense"        
        ]
    }
}
```

#### Using code
```scala

val paths = List(
            "akka.tcp://AkkaSystemName@10.0.0.1:2552/user/RouteeCreatorr/GetLicense",
            "akka.tcp://AkkaSystemName@10.0.0.1:2553/user/RouteeCreatorr/GetLicense"
            )
val router = system.actorOf(
    RoundRobinGroup(paths).props(), "groupRouter")
)

```

**Huom!**
When routee is terminates . Router still sends message to routees . Because router doesn't manage the routees . So we have re-create another routees when a routee is terminated

Router group can be dynamically resized . But because of group router behavior . It is bit more difficult to dynamically resize routees

Three message for managing group routees :
- GetRoutees()
- AddRoutee ( routee: Routee)
- RemoveRoutee ( routee: Routee)

RouteeImplementations:
- ActorRefRoutee( ref:ActorRef)
- ActorSelectionRoutee( selection: ActorSelection) // May need to use when removing a routee
- SeveralRoutee( routees: immutable.IndexedSeq[Routee])


Few points about dynamically resize group router :
- Uncautious routee removal might lead to losing messages
- When RemoveRoutee, should wait for GetRoutees message to begin next action


## ConsistentHashing Router

- The idea is that message with the same key are forwarded to the same Routee

Steps:
- Translate message into message key
- Translate message key -> hash code
- Map hashcode to virtual node
- Map virtual nodes to routees

Hashing can be done from either or in every component of the whole message system

- Router : Specifying a partial function

``` scala
def hashMapping: ConsistentHashMapping = {
    case msg: GatherMsg => msg.id
}

val router = system.actorOf(
    ConsistentHashingPool( 10,
    virtualNodesFactor = 10,
    hashMapping = hashMapping
    ).props(Props(new SimpleGather(endProbe.ref))),
    name = 'routerMapping ' 
)
```

- Message : **Implement ConsistentHashable**

```
case class GatherMessageWithHash( id: String, values: Seq[String]) extends GatherMessage with ConsistentHashable {
    override def consistentHashKey: Any = id
}

router ! GatherMessageWithHash("2",Seq("msg5"))
```

- Sender : Wrap **in ConsistentHashableEvenelope**
```scala
    router ! ConsistentHashableEnvelope(
        message = GatherMessageNormalImpl("1",Seq("msg1"), hashKey = "1")
    )
```


## Content-based Routing 

- Based on the content of the message , a flow is chosen

## State-based routing

- Changing the routing behavior based on the state of the router
- Router state is **not thread-safe** => use normal actor instead


```scala
class SwitchRouter( normalFlow: ActorRef, cleanUp: ActorRef) extends Actor with ActorLogging {

    def receive = {
        case msg:AnyRef => off(msg) // Origin receive function
    }

    def on : Receive = {
        case RouteStateOn => log("It's on already")
        case RouteStateOff => context.unbecome() // Unbecome , mean not become on , become to default receive function
        case msg: AnyRef => normalFlow ! msg
    }

    def off:Receive = {
        case RouterStateOff => log("It's off already")
        case RouterStateOn => context.become(on) // Since this is the default receive function , it needs to become something else
        case msg: AnyRef => cleanUp ! msg
    }
}
```

**Huom** After restarted, behavior of actor is returned to initial state


## Reflection

- Getting Router up and Running is easy and straighforward , however, I tends to prefer using configuration than using code , seems I get more control 
- Adding remote actor is painless 
- The terminology of 'mailbox' seems to be difficult to understand [Mailbox](http://getakka.net/docs/working-with-actors/Mailbox)
- Depends on the use case whether who will prefer using Pool Router or Group Router
- Dynamic resize seems to be easier in pool router , but less easy to manage ( not manageable in some manner )

- Huy were having some trouble understanding the Pool Router ,so I helped him a bit. But we managed 

Akka Week 2
====

## Testing actor types 


#### Single-threaded UT : 
- Actor instance normally is not accessible directly
- Using *TestActorRef*

#### Multithread UT:
- *TestKit* and *TestProbe* are provided in order to receive replies from actor
- Actors are run using normal dispatcher in multithreaded environment

#### JVM Testing:
- Akka provides tools for testing multiple JVM
- In case of using remote actor system


## Actor lifecycle

![Actor Lifecycle](https://www.codeproject.com/KB/cs/1007161/life1Big.png)



## Supervisor strategy

Example supervisor strategy

```scala
import akka.actor.Actor
 
class Supervisor extends Actor {
  import akka.actor.OneForOneStrategy
  import akka.actor.SupervisorStrategy._
  import scala.concurrent.duration._
 
  override val supervisorStrategy =
    OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 minute) {
      case _: ArithmeticException      => Resume
      case _: NullPointerException     => Restart
      case _: IllegalArgumentException => Stop
      case _: Exception                => Escalate
    }
 
  def receive = {
    case p: Props => sender() ! context.actorOf(p)
  }
}
```


##### What I have learned
- The way of testing Actors is pretty straighforward and variable . Testing one actor or a set of actor should be easy

- The lifecycle of Akka Actor is also easy to understand, also for the Supervisor Strategy
while there was some similar implementation which lives on a Master-Slave mechanism. Extending the strategy should not be difficult 

- There were no difficulties so far at this moment

- This time I tried more reading on Akka in Action and Reactive Messaging Patterns by Vaugh Vernon
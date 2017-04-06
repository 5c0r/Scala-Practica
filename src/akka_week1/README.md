Introduction to Akka
--

1. What is Actor
An “actor” is really just an analog for human participants in a system. Actors communicate with each other just like how humans do, by exchanging messages. Actors, like humans, can do work between messages.

In OOP and Procedural Programming, function calls is the main way to call other object . Class A calls a function on Class B and waits for that function to return before Class A can move onto the rest of its work.
In Akka and the Actor model, actors communicate with each-other by sending messages.

Actors can :
1. Create other actors
2. Send messages to other actors (such as the Sender of the current message)
3. Change its own behavior and process the next message it receives differently

```scala
object MyActor {
  case class Greeting(from: String)
  case object Goodbye
}
class MyActor extends Actor with ActorLogging {
  import MyActor._
  def receive = {
    case Greeting(greeter) => log.info(s"I was greeted by $greeter.")
    case Goodbye           => log.info("Someone said goodbye to me.")
  }
}

```

- Actor lifecycle

2. What is a Message 
It's basically a Message that will be consumed by an Actor . 
However, actors are typically programmed only to handle a few specific types of messages, but nothing bad happens if an actor receives a message it can’t handle. Typically it just logs the message as “unhandled” and moves on.

```scala
case class Goodbye {
    val somethingUseful: String;
}
```

3. Akka Parent Actor

```scala
class ParentBot extends Actor {
 import ChatBot._
 var childRef: ActorRef = _

 // Supervisor strategy
 override val supervisorStrategy = OneForOneStrategy() {
   case ResumeException => Resume
   case RestartException => Restart
   case StopException => Stop
   case _: Exception => Escalate
 }

 // preStart lifecycle
 override def preStart()= {
   childRef = context.actorOf(Props[ChatBot], "chatbot")
   Thread.sleep(100)
 }

 override def receive = {
   case msg =>
     childRef ! msg
     Thread.sleep(100)
 }
}
```


4. Simple Demo with Akka HTTP

```scala
class ChatBot extends Actor {

 def receive = {
   case _ => sender ! "Who 's there"
 }
}

object Test extends App {
 implicit val system = ActorSystem("Warcraft")
 implicit val actorMaterializer = ActorMaterializer()
 implicit val timeout: Timeout = 20 seconds
 val bot = system.actorOf(Props[ChatBot], "chatbot")

 val routes =
   get {
     pathSingleSlash {
       val future = bot ? "Knock knock"
       onComplete(future) {
         case util.Success(f) => complete(f.toString)
         case util.Failure(ex) => complete("Nooooooooo")
       }
     }
   }

 Http().bindAndHandle(routes,"localhost",8080)
}


```

### What were interesting subject matters?
- Concurrency and Scalability
- Parallel Programming
- Synchronous vs Asynchronous

### What topics have been hard for you?
- Concurrency and Scalability was always a **magic word** 
- The Actor Model

![Actor - Model](https://petabridge.com/images/2015/what-is-an-actor/wrong-actor-model.jpg)

### Helps
- Huy has been helping a lot to get acquainted with Akka HTTP demo , we worked on the code together

### Interests
- Actor Model , Asynchronous Messaging Patterns

#### References / Material Used for this week
- [Akka in Action](https://www.manning.com/books/akka-in-action)
- [Reactive Messaging Patterns with the Actor Model](https://www.amazon.com/Reactive-Messaging-Patterns-Actor-Model/dp/0133846830) - Vaugh Vernon
- [Akka.io](akka.io)

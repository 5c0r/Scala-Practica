Scala Practice Week 2 Diary
--

1. Chapter 8 : Inheritance

Highlights
-  _extends_ and _final_ keywords is the same as Java
- _override_ is required while overriding a method
- **Only** the **primary constructor** can call primary **superclass constructor**
- Fields are overridable

#### Examples , Examples...
```scala
import scala.math

// Usage of abstract classes
abstract class Shape {
    val id: Int
    var code : String
    def area: Double // No method body 
}

class Triangle( val one: Double, val two:Double, val three:Double ) extends Shape {
    val Id = 123
    var code = "Okie"
    def perimeter = one + two + three;
    def area = math.sqrt( ( perimeter / 2) * ( ( perimeter / 2 ) - one ) * ( ( perimeter / 2) - two ) * ( ( perimeter /2 ) - three ) )
}

// Inherits and override
class IsocelesTriangle( val twoSides : Double , val base: Double ) extends Triangle ( twoSides, twoSides, base ) {
    override def perimeter = twoSides * 2 + base

    // Possibility to override parameterless methods 
    override def area = (  ( math.sqrt( twoSides * twoSides - 0.25 * base * base ) ) + base ) / 2
}

class Rectangle ( val width: Double , val height : Double ) extends Shape {
   def perimeter = ( width + height ) * 2 
   def area = width * height
}

class Square ( val side: Double  ) extends Rectangle( side, side ) extends Rectangle  {
    override def area = side * side
}

// Override fields
class SomePolygon( override val width: Double, override val height: Double ) extends Rectangle ( width, height ) {
      def area = width * height / 2
}

object SomePolygon {
    // Revision with apply
    def apply ( w: Double, h:Double) => new SomePolygon(w,h)
}

var newPolygon = Polygon.apply(2,3);
var anotherPolygon = new SomePolygon(2,3);

newPolygon.area == anotherPolygon.area ==> true

```

Careful :

- Equals method 

```scala
// Parameter type must always be Any , then using asInstanceOf for type casting
final override def equals(other: Any) = {
  val that = other.asInstanceOf[Rectangle]
  if (that == null) false
  else area == that.area
}
```

- Protected keywords . 
[Exercise 10 explanation](http://stackoverflow.com/questions/30020306/whats-the-implication-of-protected-keywords-in-class-definition-in-scala)


2. Chapter 10 : Traits
#### Key points
```
• Similar to Java or C# , Scala does not allow a class to inherit from multiple superclasses ( Diamond inheritance ) 
• A class can implement any number of traits.
• Traits can require that implementing classes have certain fields, methods, or superclasses.
• Unlike Java interfaces, a Scala trait can provide implementations of methods and fields.
• When you layer multiple traits, the order matters—the trait whose methods execute first goes to the back.
```

- Let's treat trait as some higher level of other OOPL interfaces
- Trait can have both abstract and concrete methods
- Class can implement multiple traits

```scala

trait Logger with Cloneable with Serializable {
    def log(msg: String)
    def scream( screamMsg: String ) {
        println("Logger Screaming" + screamMsg)
    }
}

trait MyLogger extends Logger {
    override def log( msg: String ) {
        println("Logging " + msg)
    }
}

class ErrorLogger extends Logger {
    // Cool, extendings traits is similar to implements  interfaces
    def log(msg: String) { println(msg) }
}

// Trait with concrete implementation
trait Screamer {
    def scream( screamMsg: String ) {
        printfn(screamMsg);
    }
}

// Trait with abstract field
trait OneLogger extends Screamer {
    val logPath: String
}

class Account {
    var balance = 9.5
}

 class MyAccount extends Account with Scream with OneLogger {
     val logPath : String = "Hello"
     override def toString = logPath
     override def scream( msg: String ) { print(msg) }
}

var account = new MyAccount

account.scream("I'm screaming");
=> I'm screamingg

```


3. Chapter 14 ( 1- 9 + 16 ) :  Pattern Matching and Case Classes

```scala

// A switch 
var sign = ...
val ch: Char = ...

ch match {
  case '+' => sign = 1
  case '-' => sign = -1
  case _ => sign = 0 // Default ( catch_all pattern)
}

// Guard clause
ch match {
  case '+' => sign = 1
  case '-' => sign = -1
  case _ if Character.isDigit(ch) => digit = Character.digit(ch, 10)
  case _ => sign = 0
}

// Pattern matching variable
str(i) match {
  case '+' => sign = 1
  case '-' => sign = -1
  case ch => digit = Character.digit(ch, 10) // ch is considered as a variable , possible to use in GUARD also
}

// Matching again types
obj match {
  case _: BigInt => Int.MaxValue // Matches any object of type BigInt
  case BigInt => -1 // Matches the BigInt object of type Class
}

// !!! : Matching map is not possible
case m: Map[String, Int] => ... // Don't

// Instead map a generic Map
case m: Map[_, _] => ... // OK

// or Array
case a: Array[String] => ...

// Array matching
arr match {
    case Array(0) => "0"
  case Array(x, y) => x + " " + y
  case Array(0, _*) => "0 ..."
  case _ => "something else"
}


// Patterns in variable declation

val ( x, y ) = ( 4 , 3 )


// Case classes

abstract class Amount
case class Dollar(value: Double) extends Amount
case class Currency(value: Double, unit: String) extends Amount

amt match {
  case Dollar(v) => "$" + v
  case Currency(_, u) => "Oh noes, I got " + u
  case Nothing => ""
}


// Sealed classes must be in the same file as the class
sealed abstract class Amount
case class Dollar(value: Double) extends Amount
case class Currency(value: Double, unit: String) extends Amount
```



When declaring Case Class 
```
• Each of the constructor parameters becomes a val unless it is explicitly declared as a var (which is not recommended).
• An apply method is provided for the companion object that lets you construct objects without new, such as Dollar(29.95) or Currency(29.95, "EUR").
• An unapply method is provided that makes pattern matching work—see Chapter 11 for the details. (You don’t really need to know these details to use case classes for pattern matching.)
• Methods toString, equals, hashCode, and copy are generated unless they are explicitly provided.
```


4. Chapter 21.5 : Implicit Parameters
- Function or Method can have parameter list that is marked _implicit_
- Compiler will look for default values to supply for the function call

```scala
implicit val multiplier : Integer = 10
implicit val multiplier2 : Integer = 5

def multiple(num : Integer ) ( implicit number : Integer ) = num * number

// So it's not possible to have multiple implicits at same Type
// If multiplier2 is removed 
then multiple(2)  
=> res1 : Int = 20
```

### What were interesting subject matters?
- Inheritance construction order
- Traits

### What topics have been hard for you?
- Extending traits , trait construction order
- Trait usage can be as similar to interfaces , what are the good uses of concreted traits ?
- Is trait can be somehow called a decorator ?
- Extractors ( unapply, unapplySeq )

### Other
- Huy helped me a lot while learning the fun part of Scala

#### References / Material Used for this week
- Scala for the Impatient
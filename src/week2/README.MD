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

// Use "concreted implementation" from Screamer trait 
// Mixin ? Sounds familiar to 'React guys'
class AccountManager extends Something with ErrorLogger {
    def doSomething( value : Double ) {
        scream("Error here ! ")
        value
    }
}

class Account {
    var balance = 9.5
}

class SavingAccount extends Account with ErrorLogger {

}
 TODO TODO TODO


```

3. Chapter 14
4. Chapter 21.5


### What were interesting subject matters?
- Inheritance construction order
- Traits

### What topics have been hard for you?
- Extending traits , trait construction order
- Trait usage can be as similar to interfaces , what are the good uses of concreted traits ?
- Is trait can be somehow called a decorator ?

### Other
- Huy helped me a lot while learning the fun part of Scala

#### References / Material Used for this week
- Scala for the Impatient
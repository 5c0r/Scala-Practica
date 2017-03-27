
## Week 1

### What I have learned
1. [Scala Basic Expression](http://docs.scala-lang.org/tutorials/tour/basics.html
)
2. Get used to usage of Scala REPL
```scala
10 to 1
res1: scala.collection.immutable.Range.Inclusive = empty Range 10 to 1

```

3. Declare value and variables and Common used / Primitives data types

```scala
object PrimitiveTypes extends App {
  val number: Int = 10 + 10;
  val string: String = "This is a string !";
  val char: Char = 'a';
  val aBool: Boolean = true;
  val aDouble: Double = 2.120f;
  val aLong: Long = 200000000;
  val aShort: Short = 123;

  var aVariableInt : Int = 2;

  // Unit type ? Not sure about this Type , a result returned from an expression
  val aUnit: Unit = () => aVariableInt = 1;
}
```
5. Operators , Loops
- All operators are function on a class 
- Operators are left-associative
i.e : x + y + z is interpreted as ( x + y ) + z
6. Defining functions
```scala
def square( x: Double ) 
==> square: (x: Double) :Double     
```
7. Class , Object , Trait
```scala
// Approach for getting main Program

object HelloWorld extends App {
  println("Hello world !")
}

object AnotherHelloWorld {
  def main(args: Array[String]): Unit = {
    println("Hello another world!");
  }
}

// Class declarations
class Counter {
  private var value = 0 // You must initialize the field
  def increment() { value += 1 } // Methods are public by default
  def current() = value
}

// Class declarations with Auxiliary constructor
class Person {
  private var name = ""
  private var age = 0

  def this(name: String) { // An auxiliary constructor
    this() // Calls primary constructor
    this.name = name
  }
  def this(name: String, age: Int) { // Another auxiliary constructor
    this(name) // Calls previous auxiliary constructor
    this.age = age
  }

  def description = name + " is " + age + " years old"
}

val p1 = new Person // Primary constructor
val p2 = new Person("Fred") // First auxiliary constructor
val p3 = new Person("Fred", 42) // Second auxiliary constructor




```
8. Anonymous function
```scala
- Basic 
( x: Int , y: Int) => x * y 

- Can declare a name also
{ def f( x: Int , y: Int ) = x + y }
```

9. Array declaration
```scala
// Fixed length Array 
val fixedArr = new Array[Any](5)

val anotherFixedArray = Array(1,2,3,4)

// Access an element
anotherFixedArray(0)

// Variable Length Array
val b = ArrayBuffer[String]()
b += "Hello"

// Concatenate 
b ++= Array("World","Goodbye","wORD")

// Traversing
for ( i <- 0 until b.length) println(b(i))
```

### What were interesting subject matters?
- Functional Programming , it is more different than I thought
- [Pattern Matching](https://www.scala-exercises.org/scala_tutorial/structuring_information)
- Data structure in Scala

### What topics have been hard for you?
- Case Classes
- OOP in Scala seems like different than other OOPL ( C# , Java )


### Interests
- Functional Programming
- Some 

### Other

#### References / Material Used for this week
- Scala for the Impatient
- [Scala Exercices](https://www.scala-exercises.org/scala_tutorial/) [ Progress 5/ 15]
- [Scala Cheatsheet](http://www.cheat-sheets.org/saved-copy/Scala_Cheatsheet.pdf)
- [Tour of Scala](http://docs.scala-lang.org/tutorials/tour/classes.html)
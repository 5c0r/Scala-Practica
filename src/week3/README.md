
Weekly Reflection 3
==========


## Chapter 11

Key points
```
- Identifiers contain either alphanumeric or operator characters
- Unary and Binary operators are method calls
- Operator precedence depends on the first character, associativity on the last
- The apply and update methods are called when evaluating expr(arg)
- Extractors extract typles or seequence of values from an input
```

Precedence and associative
- + and -> have the same precedence , but + operator is left-associative

```scala
3 + 4 -> 5 
```
==> 7 -> 5 -> ( 7 , 5) : ( Int, Int)

```scala
3 -> 4 + 5
```
==> (3, 4) + 5 => Invalid

2) The BigInt class has a pow method, not an operator. Why didn’t the Scala library designers choose ** (as in Fortran) or ^ (as in Pascal) for a power operator?

- Usage of pow() is look more OOP friendly
- Using '**' or ^ would violate the precedence order , since ** or ^ ( as power ) will need to be evaluated first than other operator , those symbols falls into the last precedence before assignment operator



```scala
class Fraction(n: Int, d: Int) {
  private val num: Int = if (d == 0) 1 else n * sign(d) / gcd(n, d);
  private val den: Int = if (d == 0) 0 else d * sign(d) / gcd(n, d);
  override def toString = num + "/" + den
  def sign(a: Int) = if (a > 0) 1 else if (a < 0) -1 else 0
  def gcd(a: Int, b: Int): Int = if (b == 0) abs(a) else gcd(b, a % b)
  def +(other: Fraction): Fraction = new Fraction( num * other.den + den * other.num, den * other.den )
  def -(other: Fraction): Fraction = new Fraction( num * other.den - den * other.num, den * other.den )
  def *(other: Fraction): Fraction = new Fraction( num * other.num,den * other.den )
  def /(other: Fraction): Fraction = new Fraction( num * other.den,den * other.num )
}

object Fraction {
  apply( n: Int, d: Int) => new Fraction(n,d)
  def unapply( i: Fraction) => Some((i.num,i.den))
}

object GreaterThanZero {
  def unapply(input: Int ) => input > 0
}

// unapplySeq
object Name {
  def unapplySeq(input: String): Option[Seq[String]] =
    if (input.trim == "") None else Some(input.trim.split("\\s+"))
}

```



## Chapter 12

Function is "first-class citizen" . You can store function in variable
```scala
import scala.math._
val num = 3.14
val fun = ceil _

// Anonymous function
double( x : Int ) => x * 2

double(2) ==> 4 : Int

// Function with function parameters
def valueAtOneQuarter( f:( Double) => Double ) = f(0.25)

valueAtOneQuarter( ceil _ )
res1: Double = 1.0

valueAtOneQuarter( sqrt )
res2: Double = 0.5

valueAtOneQuarter( sqrt ) = sqrt(0.25)

valueAtOneQuarter( x => 3 )
res4: Double = 3.0

Why ? Because f(x) = 3 , so f(0.25) = 3

// Higher order function
Array(1,2,3).map(double);
Array(2,3,4).map( ( x: Int) => x * 3 )
Array(2,3,4).filter( x => x % 2 == 0 )
Array(1,2,3,4,5).map(3); => [3,3,3,3,3];

val f = ( x: Int, y: Int ) => x max y
val min = (x : Int , y: Int) => x min y

 res15.reduceLeft(min) => 4
 res15.reduceLeft(f) => 20

// Closure
def multiplyBy( factor : Double ) => ( x: Double ) => x * factor;
val triple = multiplyBy(3)

val double = multiplyBy(2)

```

### What were interesting and HARD

- Higher-order functions , I have been using this a lot from Javascript , with map , filter , but never know they were taken from 'function programming paradigm'

- Apply / Unapply can be considered as a 'object-to-any' mapper , depends on user usage

- Anonymous function is cool , adding function as a parameter is cool , just imagine you are applying math function to another math function.

- Scala seems to be very 'open' about operator , the convention of creating operator and precedence seems to be pretty straighforward. 
=> By this , this makes a class more 'powerful' than a regular class , even that other OOPL does provide operator , but only at some extends

- Extractor , this is something which is coming in C# also , except the pattern matching 

#### References / Material Used for this week
- Scala for the Impatient
- Your slide
package week1

/**
  * Created by nmt19 on 3/26/2017.
  * Primitives
  */
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

  println("Primitives Types");

  println("This is a number ", number);
  println("This is a string ", string);
  println("This is a char ", char);
  println("This is a Boolean ", aBool);
  println("This is a double ", aDouble);
  println("This is a long ", aLong);
  println("This is a short ", aShort);

  println("Unit type ? Not sure about this Type , a result returned from an expression ");
  println(" Value of unit type while assign value to expression ", aUnit);

}

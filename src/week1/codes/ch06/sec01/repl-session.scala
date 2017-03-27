// These are meant to be typed into the REPL. You can also run
// scala -Xnojline < repl-session.scala to run them all at once.

object Accounts {
  private var lastNumber: Int = 0
  def newUniqueNumber() : Int = { lastNumber += 1; lastNumber }
}

Accounts.newUniqueNumber()
Accounts.newUniqueNumber()

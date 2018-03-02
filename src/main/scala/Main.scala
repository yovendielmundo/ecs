import scala.language.implicitConversions

object Main {
  sealed trait Command
  case object Unknown extends Command
  case object Exit extends Command
  case object Step extends Command
  case object Status extends Command
  case class Update(id: Int, floor: Int, goalFloor: Int) extends Command
  case class Pickup(goalFloor: Int, direction: Int) extends Command

  implicit def string2Command(line: String): Command = line match {
    case "status" => Status
    case "step" => Step
    case "pickup" => Pickup(0, 1)
    case "update" => Update(1, 0, 1)
    case "exit" => Exit
    case _ => Unknown
  }

  lazy val help =
    """
      |==================== ECS ====================
      |commands:
      |$: status
      |$: step
      |$: pickup <goal-floor> <direction>
      |$: update <elevator-id> <floor> <goal-floor>
      |$: exit
    """.stripMargin

  def showHelp: Unit = {
    println(help)
  }

  def askCommand: Command = {
    println()
    print("ecs $: ")
    scala.io.StdIn.readLine()
  }

  def printCommand(command: Command): Unit = {
    println(s"ecs-command:$command =>")
  }

  def main(args: Array[String]): Unit = {
    showHelp
    var command: Command = Unknown

    while (command != Exit) {
      command = askCommand
      printCommand(command)

      command match {
        case Status => println("Status")
        case Step => println("Step")
        case Pickup(goalFloor, direction) => println(s"Pickup(goal floor: $goalFloor, direction: $direction")
        case Update(id, floor, goalFloor) => println(s"Update(elevator: $id, floor: $floor, goal floor: $goalFloor")
        case Exit => println("Bye...")
        case Unknown => showHelp
      }
    }

  }

}

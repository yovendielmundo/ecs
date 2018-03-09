import scala.language.implicitConversions

object Main {
  sealed trait Command
  case object Unknown extends Command
  case object Exit extends Command
  case object Step extends Command
  case object Status extends Command
  case class Update(id: Int, floor: Int, goalFloor: Int) extends Command
  case class Pickup(pickupFloor: Int, direction: Int) extends Command

  val patternPickup = "pickup ([0-9]+) (-?[1])".r
  val patternUpdate = "update ([0-9]+) ([0-9]+) ([0-9]+)".r


  implicit def string2Command(line: String): Command = line match {
    case "status" => Status
    case "step" => Step
    case "exit" => Exit
    case patternUpdate(id, floor, goalFloor) => Update(id.toInt, floor.toInt, goalFloor.toInt)
    case patternPickup(floor, direction) => Pickup(floor.toInt, direction.toInt)
    case _ => Unknown
  }

  lazy val help =
    """
      |==================== ECS ====================
      |commands:
      |$: status
      |$: step
      |$: pickup <pickup-floor> <direction>
      |$: update <elevator-id> <floor> <goal-floor>
      |$: exit
    """.stripMargin

  def showHelp: Unit = println(help)

  def askCommand: Command = {
    println()
    print("ecs $: ")
    scala.io.StdIn.readLine()
  }

  def printCommand(command: Command): Unit = println(s"ecs-command:$command =>")

  def whileCommand(executor: Command => Unit): Unit = {
    val command = askCommand
    printCommand(command)
    executor(command)
    if (command != Exit) whileCommand(executor)
  }

  def main(args: Array[String]): Unit = {
    val ecs = new MyElevatorControlSystem(numberOfElevators = 3)

    showHelp

    whileCommand {
      case Status => println(ecs.status)
      case Step => {
        ecs.step
        println("step")
      }
      case Update(id, floor, goalFloor) => {
        try {
          ecs.update(id, floor, goalFloor)
          println(s"elevator: $id, floor: $floor, goal floor: $goalFloor")
        } catch {
          case e: IllegalArgumentException => println(e.getMessage)
        }
      }
      case Pickup(pickupFloor, direction) => {
        ecs.pickup(pickupFloor, direction)
        println(s"pickup floor: $pickupFloor, direction: $direction")
      }
      case Exit => println("bye...")
      case Unknown => showHelp
    }

  }

}

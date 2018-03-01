import Elevator.{Direction, Id, NoDir, Status}

object Elevator {
  object Status {
    def apply(id: Int, floor: Int = 0, goalFloor: List[Int] = List()) = new Status(Id(id), floor, goalFloor)
  }
  case class Status(id: Id, floor: Int, goalFloor: List[Int])

  case class Id(id: Int) { override def toString: String = s"L$id" }

  sealed trait Direction
  object Down extends Direction
  object Up extends Direction
  object NoDir extends Direction

  def apply(id: Int, maxNumberOfGoals: Int = 3) = new Elevator(Id(id), maxNumberOfGoals)
}

case class Elevator(id: Id, maxNumberOfGoals: Int) {
  var floor: Int = 0
  var goalFloor: List[Int] = List()
  var direction: Direction = NoDir

  def update(floor: Int, goalFloor: Int) = {
    this.floor = floor
    this.goalFloor = List(goalFloor)
  }

  def status: Status = Status(id, floor, goalFloor)

  def pickup(floor:Int, direction: Direction): Unit = {
    if (canPickup) {
      if (this.direction == NoDir) this.direction = direction
      if (this.direction == direction) goalFloor = goalFloor :+ floor
    }

  }

  def canPickup: Boolean = goalFloor.size < maxNumberOfGoals

  def step: Unit = {
    if (!goalFloor.isEmpty) {
      floor += 1
    }
    updateState
  }

  private def updateState(): Unit = {
    if (!goalFloor.isEmpty  && floor == goalFloor.head)
      goalFloor = goalFloor.tail
  }

}


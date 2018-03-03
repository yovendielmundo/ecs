import Elevator._

object Elevator {
  object Status {
    def apply(id: Int, floor: Int = 0, goalFloor: List[Int] = List()) = new Status(Id(id), floor, goalFloor)
  }
  case class Status(id: Id, floor: Int, goalFloor: List[Int])

  case class Id(id: Int) { override def toString: String = s"L$id" }

  sealed trait Direction
  object Down extends Direction
  object Up extends Direction
  object NoDirection extends Direction

  def apply(id: Int, maxNumberOfGoals: Int = 3) = new Elevator(Id(id), maxNumberOfGoals)
}

case class Elevator(id: Id, maxNumberOfGoals: Int) {
  var floor: Int = 0
  var goalFloors: List[Int] = List()
  var pickupDirection: Direction = NoDirection

  def update(floor: Int, goalFloor: Int): Unit = {
    this.floor = floor
    goalFloors = List(goalFloor)
  }

  def status: Status = Status(id, floor, goalFloors)

  def pickup(goalFloor: Int, direction: Direction): Unit = {
    if (canPickup(goalFloor, direction)) {
      if (isStopped) pickupDirection = direction
      goalFloors = insert(goalFloor, goalFloors)(currentOrd)
    }
  }

  def canPickup(goalFloor: Int, direction: Direction): Boolean = {
    if (goalFloors.size >= maxNumberOfGoals) false
    else if (pickupDirection != NoDirection && direction != pickupDirection) false
    else if (!isOnTheWay(goalFloor)) false
    else true
  }

  def step: Unit = {
    if (isMoving)
      floor = nextFloor

    updateState
  }

  def isAscending: Boolean = isMoving && goalFloors.head > floor

  def isDescending: Boolean = isMoving && goalFloors.head < floor

  def isStopped: Boolean = goalFloors.isEmpty

  def isMoving: Boolean = !isStopped

  def lessBusyFactor: Int = maxNumberOfGoals - goalFloors.size

  private def nextFloor: Int = floor + (if (isAscending) 1 else if (isDescending) -1 else 0)

  def isOnTheWay(goalFloor: Int): Boolean =
    if (isStopped) true
    else if (isAscending) goalFloor > floor
    else if (isDescending) goalFloor < floor
    else false

  private def updateState: Unit = {
    if (goalFloors.nonEmpty  && floor == goalFloors.head)
      goalFloors = goalFloors.tail

    if (goalFloors.isEmpty)
      pickupDirection = NoDirection
  }

  private def currentOrd: Ordering[Int] =
    if (isAscending) (x: Int, y: Int) => x - y
    else (x: Int, y: Int) => y - x

  private def insert(y: Int, ys: List[Int])(implicit ord: Ordering[Int]): List[Int] = ys match {
    case List() => y :: List()
    case z :: zs =>
      if (ord.lt(y, z)) y :: z :: zs
      else z :: insert(y, zs)
  }
}


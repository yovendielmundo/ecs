import Elevator.{Direction, Down, Up}

class MyElevatorControlSystem(val numberOfElevators: Int = 3) extends ElevatorControlSystem {

  /**
    * Elevators in the control system
    */
  val elevators = (1 to numberOfElevators).map(id => id -> Elevator(id)).toMap

  /**
    * Querying the state of the elevators.
    * what floor are they on and where they are going
    *
    * @return
    */
  override def status: Seq[Elevator.Status] = elevators.map(_._2.status).toSeq

  /**
    * Receiving an update about the status of an elevator.
    * Here we have chosen to represent elevator state as 3 integers:
    * Elevator ID, Floor Number, Goal Floor Number
    *
    * @param elevatorId
    * @param floor
    * @param goalFloor
    */
override def update(elevatorId: Int, floor: Int, goalFloor: Int): Unit = {
  require(elevatorId > 0 && elevatorId <= numberOfElevators, s"invalid elevator id: $elevatorId")
  elevators(elevatorId).update(floor, goalFloor)
}

  /**
    * Receiving a pickup request.
    * A pickup request is two integers:
    * Pickup Floor, Direction (negative for down, positive for up)
    *
    * @param pickupFloor
    * @param direction
    */
  override def pickup(pickupFloor: Int, direction: Int): Unit = {
    val dir: Direction = if (direction < 0) Down else Up

    val priorityList = elevators.filter(_._2.canPickup(pickupFloor, dir))
      .foldRight(List[Elevator]()) {
        case ((_, elevator), acc) => elevator :: acc
      }
      // We can improved the priority list just changing this function adding more variables
      .sortWith((e1, e2) => e1.lessBusyFactor > e2.lessBusyFactor)

    if (priorityList.nonEmpty) priorityList.head.pickup(pickupFloor, dir)
  }

  /**
    * Time-stepping the simulation
    */
  override def step: Unit = {
    elevators.foreach(_._2.step)
  }
}

class MyElevatorControlSystem(val numberOfElevators: Int) extends ElevatorControlSystem {

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
  override def status: Seq[Elevator.Status] = ???

  /**
    * Receiving an update about the status of an elevator.
    * Here we have chosen to represent elevator state as 3 integers:
    * Elevator ID, Floor Number, Goal Floor Number
    *
    * @param elevatorId
    * @param floor
    * @param goalFloor
    */
override def update(elevatorId: Int, floor: Int, goalFloor: Int): Unit = ???

  /**
    * Receiving a pickup request.
    * A pickup request is two integers:
    * Pickup Floor, Direction (negative for down, positive for up)
    *
    * @param pickupFloor
    * @param direction
    */
  override def pickup(pickupFloor: Int, direction: Int): Unit = ???

  /**
    * Time-stepping the simulation
    */
  override def step: Unit = ???
}

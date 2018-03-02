trait ElevatorControlSystem {

  /**
    * Querying the state of the elevators.
    * what floor are they on and where they are going
    * @return
    */
  def status: Seq[Elevator.Status]


  /**
    * Receiving an update about the status of an elevator.
    * Here we have chosen to represent elevator state as 3 integers:
    * Elevator ID, Floor Number, Goal Floor Number
    * @param elevatorId
    * @param floor
    * @param goalFloor
    */
  def update(elevatorId:Int, floor:Int, goalFloor:Int)

  /**
    * Receiving a pickup request.
    * A pickup request is two integers:
    * Pickup Floor, Direction (negative for down, positive for up)
    * @param pickupFloor
    * @param direction
    */
  def pickup(pickupFloor:Int, direction:Int)

  /**
    * Time-stepping the simulation
    */
  def step
}

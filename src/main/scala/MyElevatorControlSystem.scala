import scala.collection.mutable

class MyElevatorControlSystem(val numberOfElevators: Int = 3) extends ElevatorControlSystem {

  /**
    * Elevators in the control system
    */
  val elevators = (1 to numberOfElevators).map(id => id -> Elevator(id)).toMap


  /**
    * Ordering in priority queue
    */
  val lessBusyElevator: Ordering[Elevator] = (x: Elevator, y: Elevator) => x.lessBusyFactor - y.lessBusyFactor
  /**
    * Priority queue of elevators to send pickup requests
    * The priority is the less busy elevator
    */
  val priorityQueue = mutable.PriorityQueue[Elevator]()(ord = lessBusyElevator)
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
    // 1. dequeue elevator from priorityQueue
    // 2. send pickup request
    // 3. enqueue elevator again if canPickup
  }

  /**
    * Time-stepping the simulation
    */
  override def step: Unit = {
    // 1. dequeue all elevators
    // 2. iterate all elevators and do step
    // 3. enqueue all elevators again if canPickup
  }
}

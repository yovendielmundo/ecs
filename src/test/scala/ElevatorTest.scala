import Elevator._
import org.scalatest.{FunSuite, Matchers}

class ElevatorTest extends FunSuite with Matchers {

  test("test create an instance") {
    val elevator = Elevator(id = 1)

    elevator.id should be(Id(1))
    elevator.floor should be(0)
    elevator.goalFloors should be(List())
  }

  test("test get status") {
    val elevator = Elevator(1)

    elevator.status should be(Status(id = 1, goalFloor = List()))
  }

  test("test update status") {
    val elevator = Elevator(id = 1)

    elevator.update(floor = 1, goalFloor = 5)

    elevator.floor should be(1)
    elevator.goalFloors should be(List(5))
    elevator.status should be(Status(id = 1, floor = 1, goalFloor = List(5)))
  }

  test("test set to pickup some passenger") {
    val elevator = Elevator(id = 1)

    elevator.pickup(goalFloor = 3, direction = Down)

    elevator.pickupDirection should be(Down)
    elevator.status should be(Status(1, 0, List(3)))
  }

  test("test canPickup is false when the elevator is full") {
    val elevator = Elevator(id = 1, maxNumberOfGoals = 2)

    elevator.pickup(goalFloor = 3, direction = Down)
    elevator.pickup(goalFloor = 1, direction = Down)

    elevator.canPickup(goalFloor = 4, direction = Down) should be(false)
  }

  test("test canPickup is false when pickup floor is not in the way") {
    val elevator = Elevator(id = 1)

    elevator.update(floor = 3, goalFloor = 5)
    elevator.pickupDirection = Down

    elevator.canPickup(goalFloor = 2, direction = Down) should be(false)
  }

  test("test canPickup is false when pickup direction is different from current pickup dorection") {
    val elevator = Elevator(id = 1)

    elevator.update(floor = 3, goalFloor = 5)
    elevator.pickupDirection = Down

    elevator.canPickup(goalFloor = 6, direction = Up) should be(false)
  }

  test("test canPickup is true when the elevator is stopped") {
    val elevator = Elevator(id = 1)

    elevator.canPickup(goalFloor = 5, direction = Down)  should be(true)
  }

  test("test canPickup is true when the elevator is not full") {
    val elevator = Elevator(id = 1, maxNumberOfGoals = 5)

    elevator.pickup(goalFloor = 1, direction = Down)
    elevator.pickup(goalFloor = 2, direction = Down)
    elevator.pickup(goalFloor = 3, direction = Down)
    elevator.pickup(goalFloor = 4, direction = Down)

    elevator.canPickup(goalFloor = 5, direction = Down)  should be(true)
  }

  test("test doesn't pickup passengers in an opposite direction") {
    val elevator = Elevator(id = 1)

    elevator.pickup(goalFloor = 3, direction = Down)

    elevator.pickup(goalFloor = 1, direction = Up)

    elevator.pickupDirection should be(Down)
    elevator.status should be(Status(1, 0, List(3)))
  }

  test("test doesn't pickup passengers when is full") {
    val elevator = Elevator(id = 1, maxNumberOfGoals = 1)

    elevator.pickup(goalFloor = 3, direction = Down)
    // elevator is already full

    elevator.pickup(goalFloor = 5, direction = Down)

    elevator.pickupDirection should be(Down)
    elevator.status should be(Status(1, 0, List(3)))
  }

  test("test doesn't pickup passengers when goal floor is not on the way") {
    val elevator = Elevator(id = 1)

    elevator.update(floor = 4, goalFloor = 5)
    elevator.pickupDirection = Down
    // elevator is in the 4th floor

    elevator.pickup(goalFloor = 3, direction = Down)
    elevator.status should be(Status(1, 4, List(5)))
  }

  test("test make many pickup requests in the same direction") {
    val elevator = Elevator(id = 1)

    elevator.pickup(goalFloor = 3, direction = Down)
    elevator.status should be(Status(1, 0, List(3)))

    elevator.pickup(goalFloor = 5, direction = Down)
    elevator.status should be(Status(1, 0, List(3, 5)))

    elevator.pickup(goalFloor = 6, direction = Down)
    elevator.status should be(Status(1, 0, List(3, 5, 6)))
  }

  test("test goalFloors list is ascending when elevator is ascending") {
    val elevator = Elevator(id = 1, maxNumberOfGoals = 5)

    elevator.pickup(goalFloor = 3, direction = Down)
    elevator.status should be(Status(1, 0, List(3)))
    elevator.isAscending should be(true)

    elevator.pickup(goalFloor = 7, direction = Down)
    elevator.status should be(Status(1, 0, List(3, 7)))
    elevator.isAscending should be(true)

    elevator.pickup(goalFloor = 6, direction = Down)
    elevator.status should be(Status(1, 0, List(3, 6, 7)))
    elevator.isAscending should be(true)

    elevator.pickup(goalFloor = 5, direction = Down)
    elevator.status should be(Status(1, 0, List(3, 5, 6, 7)))
    elevator.isAscending should be(true)
  }

  test("test the goalFloors list is descending when elevator is descending") {
    val elevator = Elevator(id = 1, maxNumberOfGoals = 5)

    elevator.update(floor = 10, goalFloor = 0)
    elevator.pickupDirection = Down
    elevator.status should be(Status(1, 10, List(0)))
    elevator.isDescending should be(true)

    elevator.pickup(goalFloor = 3, direction = Down)
    elevator.status should be(Status(1, 10, List(3, 0)))
    elevator.isDescending should be(true)

    elevator.pickup(goalFloor = 7, direction = Down)
    elevator.status should be(Status(1, 10, List(7, 3, 0)))
    elevator.isDescending should be(true)

    elevator.pickup(goalFloor = 6, direction = Down)
    elevator.status should be(Status(1, 10, List(7, 6, 3, 0)))
    elevator.isDescending should be(true)
  }

  test("test to do steps is useless when there is no goals") {
    val elevator = Elevator(id = 1)

    elevator.step
    elevator.step
    elevator.step

    elevator.floor should be(0)
    elevator.pickupDirection should be(NoDirection)
    elevator.isStopped should be(true)
    elevator.status should be(Status(1, 0, List()))
  }

  test("test to do steps ascending and reach a goal floor") {
    val elevator = Elevator(id = 1)

    elevator.pickup(goalFloor = 3, direction = Down)

    elevator.step
    elevator.floor should be(1)


    elevator.step
    elevator.floor should be(2)


    elevator.step
    elevator.floor should be(3)

    elevator.pickupDirection should be(NoDirection)
    elevator.isStopped should be(true)
    elevator.status should be(Status(1, 3, List()))
  }

  test("test to do steps descending and reach a goal floor") {
    val elevator = Elevator(id = 1)

    elevator.update(floor = 3, goalFloor = 0)
    elevator.pickupDirection = Up

    elevator.step
    elevator.floor should be(2)

    elevator.step
    elevator.floor should be(1)

    elevator.step
    elevator.floor should be(0)

    elevator.pickupDirection should be(NoDirection)
    elevator.isStopped should be(true)
    elevator.status should be(Status(1, 0, List()))
  }

  test("test doesn't pickup passengers in an opposite direction when elevator is moving") {
    val elevator = Elevator(id = 1)

    elevator.pickup(goalFloor = 4, direction = Down)
    elevator.isAscending should be(true)

    elevator.step
    elevator.floor should be(1)

    // this pickup request should be ignored
    elevator.pickup(goalFloor = 3, direction = Up)
    elevator.status should be(Status(1, 1, List(4)))

    elevator.step
    elevator.floor should be(2)
    elevator.status should be(Status(1, 2, List(4)))

    elevator.step
    elevator.floor should be(3)

    elevator.step
    elevator.floor should be(4)
    elevator.pickupDirection should be(NoDirection)
    elevator.status should be(Status(1, 4, List()))
  }

  test("test to do steps an many pickup request at the same time") {
    val elevator = Elevator(id = 1)

    elevator.pickup(goalFloor = 4, direction = Down)

    elevator.step
    elevator.floor should be(1)

    elevator.pickup(goalFloor = 3, direction = Down)

    elevator.step
    elevator.floor should be(2)
    elevator.status should be(Status(1, 2, List(3, 4)))

    elevator.step
    elevator.floor should be(3)
    elevator.status should be(Status(1, 3, List(4)))
    elevator.isMoving should be(true)

    // pickup should be ignored
    elevator.pickup(goalFloor = 1, direction = Down)
    elevator.status should be(Status(1, 3, List(4)))

    // pickup accepted
    elevator.pickup(goalFloor = 6, direction = Down)
    elevator.status should be(Status(1, 3, List(4, 6)))

    elevator.step
    elevator.floor should be(4)
    elevator.status should be(Status(1, 4, List(6)))
    elevator.isMoving should be(true)

    elevator.step
    elevator.step
    elevator.pickupDirection should be(NoDirection)
    elevator.isStopped should be(true)
    elevator.status should be(Status(1, 6, List()))
  }


  test("test to do steps ascending and descending") {
    val elevator = Elevator(id = 1)

    elevator.pickup(goalFloor = 4, direction = Down)

    elevator.step
    elevator.floor should be(1)

    elevator.step
    elevator.floor should be(2)

    elevator.step
    elevator.floor should be(3)

    // pickup should be ignored
    elevator.pickup(goalFloor = 1, direction = Up)
    elevator.status should be(Status(1, 3, List(4)))

    elevator.step
    elevator.floor should be(4)
    elevator.status should be(Status(1, 4, List()))
    elevator.isStopped should be(true)

    // this time the pickup should be accepted
    elevator.pickup(goalFloor = 1, direction = Up)
    elevator.status should be(Status(1, 4, List(1)))

    elevator.step
    elevator.floor should be(3)

    elevator.step
    elevator.floor should be(2)

    elevator.step
    elevator.floor should be(1)
    elevator.pickupDirection should be(NoDirection)
    elevator.isStopped should be(true)
    elevator.status should be(Status(1, 1, List()))
  }

}

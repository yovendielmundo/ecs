import Elevator.{Down, Id, Status, Up}
import org.scalatest.{FunSuite, Matchers}

class ElevatorTest extends FunSuite with Matchers {

  test("test create an instance") {
    val elevator = Elevator(id = 1)

    elevator.id should be(Id(1))
    elevator.floor should be(0)
    elevator.goalFloor should be(List())
  }

  test("test get status") {
    val elevator = Elevator(1)

    elevator.status should be(Status(id = 1, floor = 0, goalFloor = List()))
  }

  test("test update status") {
    val elevator = Elevator(id = 1)

    elevator.update(floor = 1, goalFloor = 5)

    elevator.floor should be(1)
    elevator.goalFloor should be(List(5))
    elevator.status should be(Status(id = 1, floor = 1, goalFloor = List(5)))
  }

  test("test set to pickup some passenger") {
    val elevator = Elevator(id = 1)

    elevator.pickup(floor = 3, direction = Down)
    elevator.status should be(Status(1, 0, List(3)))
  }

  test("test canPickup is false when the elevator is full") {
    val elevator = Elevator(id = 1, maxNumberOfGoals = 2)

    elevator.pickup(floor = 3, direction = Down)
    elevator.pickup(floor = 1, direction = Down)

    elevator.canPickup should be(false)
  }

  test("test canPickup is true when the elevator is not full") {
    val elevator = Elevator(id = 1, maxNumberOfGoals = 5)

    elevator.pickup(floor = 1, direction = Down)
    elevator.pickup(floor = 2, direction = Down)
    elevator.pickup(floor = 3, direction = Down)
    elevator.pickup(floor = 4, direction = Down)

    elevator.canPickup should be(true)
  }

  test("test does not pickup passengers in an opposite direction") {
    val elevator = Elevator(id = 1)

    elevator.pickup(floor = 3, direction = Down)
    elevator.status should be(Status(1, 0, List(3)))

    elevator.pickup(floor = 1, direction = Up)
    elevator.status should be(Status(1, 0, List(3)))
  }

  test("test does not pickup passengers when is full") {
    val elevator = Elevator(id = 1, maxNumberOfGoals = 1)

    elevator.pickup(floor = 3, direction = Down)
    elevator.status should be(Status(1, 0, List(3)))

    elevator.pickup(floor = 1, direction = Down)
    elevator.status should be(Status(1, 0, List(3)))
  }

  test("test set to pickup many passengers in the same direction") {
    val elevator = Elevator(id = 1)

    elevator.pickup(floor = 3, direction = Down)
    elevator.status should be(Status(1, 0, List(3)))

    elevator.pickup(floor = 5, direction = Down)
    elevator.status should be(Status(1, 0, List(3, 5)))

    elevator.pickup(floor = 6, direction = Down)
    elevator.status should be(Status(1, 0, List(3, 5, 6)))
  }

  test("test do useless steps when there is no goals") {
    val elevator = Elevator(id = 1)

    elevator.step
    elevator.step
    elevator.step

    elevator.floor should be(0)
    elevator.status should be(Status(1, 0, List()))
  }

  test("test do steps and reach one goal floor") {
    val elevator = Elevator(id = 1)

    elevator.pickup(floor = 3, direction = Down)
    elevator.step
    elevator.floor should be(1)
    elevator.step
    elevator.floor should be(2)
    elevator.step
    elevator.floor should be(3)

    elevator.status should be(Status(1, 3, List()))
  }

}

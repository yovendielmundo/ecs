import Elevator.Status
import org.scalatest.{FunSuite, Matchers}

class MyElevatorControlSystemTest extends FunSuite with Matchers {

  test("test my ecs has elevators") {
    val ecs = new MyElevatorControlSystem(numberOfElevators = 3)

    ecs.elevators should contain theSameElementsAs
      Map(1 -> Elevator(1), 2 -> Elevator(2), 3 -> Elevator(3))
  }

  test("test ecs status listing the elevators status") {
    val ecs = new MyElevatorControlSystem()

    ecs.status should contain theSameElementsAs
      Seq(Status(1), Status(2), Status(3))
  }

  test("test ecs update elevator status") {
    val ecs = new MyElevatorControlSystem()

    ecs.update(1, 5, 0)

    ecs.status should contain theSameElementsAs
      Seq(Status(1, 5, List(0)), Status(2), Status(3))
  }

  test("test ecs update invalid elevator throws an exception") {
    val ecs = new MyElevatorControlSystem()

    intercept[IllegalArgumentException] {
      ecs.update(0, 5, 0)
    }

    ecs.status should contain theSameElementsAs
      Seq(Status(1), Status(2), Status(3))
  }

  test("test ecs handle many pickup requests") {
    val ecs = new MyElevatorControlSystem()

    // pickup request at 3st floor to go down
    ecs.pickup(pickupFloor = 3, direction = -1)

    ecs.status should contain theSameElementsAs
      Seq(Status(1, 0, List(3)), Status(2), Status(3))

    // pickup request at 4st floor to go down
    ecs.pickup(pickupFloor = 4, direction = -1)

    ecs.status should contain theSameElementsAs
      Seq(Status(1, 0, List(3)), Status(2), Status(3, 0, List(4)))

    // pickup request at 5st floor to go down
    ecs.pickup(pickupFloor = 5, direction = -1)

    ecs.status should contain theSameElementsAs
      Seq(Status(1, 0, List(3)), Status(2, 0, List(5)), Status(3, 0, List(4)))

    // pickup request at 6st floor to go down
    ecs.pickup(pickupFloor = 6, direction = -1)

    ecs.status should contain theSameElementsAs
      Seq(Status(1, 0, List(3)), Status(2, 0, List(5)), Status(3, 0, List(4, 6)))

  }

  test("test ecs make some steps") {
    val ecs = new MyElevatorControlSystem()

    ecs.pickup(pickupFloor = 3, direction = -1)
    ecs.pickup(pickupFloor = 4, direction = -1)

    ecs.step
    ecs.step
    ecs.step

    ecs.status should contain theSameElementsAs
      Seq(Status(1, 3), Status(2), Status(3, 3, List(4)))
  }

}

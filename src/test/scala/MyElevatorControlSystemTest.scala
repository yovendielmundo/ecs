import org.scalatest.{FunSuite, Matchers}

class MyElevatorControlSystemTest extends FunSuite with Matchers {

  test("test my elevator control system has elevators") {
    val ecs = new MyElevatorControlSystem(numberOfElevators = 3)

    ecs.elevators should contain theSameElementsAs
      Map(1 -> Elevator(1), 2 -> Elevator(2), 3 -> Elevator(3))
  }

}

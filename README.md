# ECS
Elevator control system

The scheduling problem is handled by a `PriorityQueue`.  
The priority is an `Ordering[Elevator]` function, so I have implemented a simple less-busy strategy which is the Elevator with least pickup requests.  
This priority function can be improved by having in mind more parameters.  


## Test
Run the test cases in your terminal. You can play a simulation running the test cases.
```bash
$ sbt test
```

## Shell interface
I have built a basic shell interface to play the simulation. ...it's so funny ;)

```bash
==================== ECS ====================
commands:
$: status
$: step
$: pickup <pickup-floor> <direction>
$: update <elevator-id> <floor> <goal-floor>
$: exit
```

**Running the shell interface**  

```bash
$ sbt run
```

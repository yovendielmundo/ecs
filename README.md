# ECS
Elevator control system


## Test
Run the test cases in your terminal
```bash
$ sbt test
```

## Shell interface
Basic shell interface to play the simulation

```bash
==================== ECS ====================
commands:
$: status
$: step
$: pickup <goal-floor> <direction>
$: update <elevator-id> <floor> <goal-floor>
$: exit
```

**Running the shell interface**  

```bash
$ sbt run
```

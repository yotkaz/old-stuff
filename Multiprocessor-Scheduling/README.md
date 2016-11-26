# Multiprocessor-Scheduling
Algorithms comparator for "Multiprocessor Scheduling" problem (Java)

Classic Genetic Algorithm vs Simulated Annealing

**Compile:**
```
mvn clean package
```

**Usage:**
```
java -jar multiprocessor-scheduling-0.0.1.jar [properties]
```
By default, program tries to find configuration.properties in the current directory.

**Sample output:**
```
---------------------------------------------------------------------------------------------------
|   GA value    | MultiSA value |   SA value    |||    GA time    | MultiSA time  |    SA time    |
----------------|---------------|---------------|||---------------|---------------|---------------|
| 91            | 148           | 186           ||| 3653          | 459           | 41            |
| 92            | 155           | 174           ||| 2838          | 465           | 51            |
| 51            | 89            | 108           ||| 1761          | 252           | 26            |
| 42            | 59            | 73            ||| 1437          | 165           | 17            |
| 111           | 180           | 173           ||| 3512          | 530           | 45            |
---------------------------------------------------------------------------------------------------
Values are CPU cycles. All time measurements are in milliseconds.
```

# BlueStaq-Elevator
This is my submission for the BlueStaq coding challenge.

## How to Build & Run


Open a terminal at the root project directory: <i>bluestaq-elevator-app</i>
> mvn clean install

> java -cp .\target\bluestaq-elevator-app-1.0-SNAPSHOT.jar com.maxruiz.app.App 

<b><i>OR</i></b>

Navigate to the folder: <i>bluestaq-elevator-app/scripts/</i>

Run the batch files:
* build.bat
* run.bat

Then to see the output of the program,
navigate to the root folder and open
the file <b>bluestaq-elevator-app/system_out.txt</b>

## Normal Run
### Events
Events are randomly created within the Building and affect the elevator
These events include
* Idle - Nothing happening at the moment, continue operations as normal
* Passenger - A passenger shows up
  * random floor 
  * random desination
  * Places a request
* Stuck - The elevator gets stuck 
  * Not fully developed
  * Elevator stops moving
  * maintenance staff arrives on floor
  * Fixes the elevator and leaves
  * Flow returns to normal
* Fire - A fire occurs on a random floor
  * Not full developed
  * all people on all floors evacuate
  * firefighter shows up
  * Travels to the floor
  * Puts out the fire
  * Returns to the ground floor and leaves
  * Flow returns to normal

### Passengers
A passenger will appear on a random floor with a random destination.

The passenger can get sick while traveling in an elevator and will get off at the next floor
they arrive at.

A passenger will not get on an elevator if the elevator is intended to go the wrong direction.

A passenger will not get on an elevator if the elevator can't take them to their destination.

A passenger takes up space, therefore sometimes they can't fit on the elevator and will have to 
submit another request.

A passenger may miss the elevator if there are too many people waiting for the elevator and the
time the elevator has it's doors open is too short.

### Elevator
There can be any number of elevators.

The elevators share the same request queue so multiple elevators won't try to pick up the same
passenger.

The elevators will fulfill the requests of on-board passengers before focusing on a request of 
a passenger waiting on a floor.

The elevator will stop and accept a passenger if the passenger is on the way to the target floor.

The elevator will let a passenger off if the elevator passes by the passengers destination floor
while on the way to the elevators target floor.

The elevator will drop a sick passenger off at the next available floor and will continue to that
passengers original request, even without them, because the passenger didn't magically remove their
request after they got off.

## Configure the application
<b>Due to difficult circumstances, my 
program won't run based on the json
config files found in the <i>bluestaq-elevator-app/resources/</i>
folder.</b>

> Please read my notes in: <i>bluestaq-elevator-app/notes.txt</i> for an explanation.

<b>However</b> You can still configure the file.

You just need to go into the corresponding config
java files and mess with the default parameters.

* AppConfig.java - <i>loadDefaultAppConfig()</i>
  * Frame Rate of the application
  * Print to File (or to console) flag
  * Print to File, Filename
* BuildingConfig.java - <i>loadDefaultBuildingConfig()</i>
  * Lowest Floor
  * Highest Floor
  * Maximum number of passengers that can wait on a floor for the elevator
  * Use a set of custom events
  * Elevator configuration set - <b>Can be any number of elevators</b>
* ElevatorConfig.java - <i>loadDefaultElevatorConfig()</i>
  * Elevator ID
  * Starting Floor
  * Square Footage of the elevator
  * Lowest Floor that the elevator can go (can be different than then buildings lowest floor)
  * Highest Floor that the elevator can go (doesn't have to be the same as the buildings highest floor)
  * Number of Frames that the elevator takes while waiting at the simulated floor
  * Number of Frames that the elevator takes while moving between floors
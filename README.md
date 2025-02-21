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

## Configure the application
<b>Due to difficult circumstances, my 
program won't run based on the json
config files found in the <i>bluestaq-elevator-app/resources/</i>
folder.</b>

Please read my notes in: <i>bluestaq-elevator-app/notes.txt</i>
for an explanation.

> <b>However</b> You can still configure the file.

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
  * Elevator configuration set
* ElevatorConfig.java - <i>loadDefaultElevatorConfig()</i>
  * Elevator ID
  * Starting Floor
  * Square Footage of the elevator
  * Lowest Floor that the elevator can go (doesn't have to be the same as the buildings lowest floor)
  * Highest Floor that the elevator can go (doesn't have to be the same as the buildings highest floor)
  * Number of Frames that the elevator takes while waiting at the simulated floor
  * Number of Frames that the elevator takes while moving between floors
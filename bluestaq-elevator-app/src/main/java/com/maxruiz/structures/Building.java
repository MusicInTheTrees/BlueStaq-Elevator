package com.maxruiz.structures;

import java.util.ArrayList;
import java.util.Random;

import com.maxruiz.config.ElevatorConfig;
import com.maxruiz.config.BuildingConfig;
import com.maxruiz.utility.EventController;
import com.maxruiz.utility.Direction;
import com.maxruiz.passengers.*;

/**
 * This class is what runs the simulation environment for the elevator.
 * This class contains one or more elevators and contains an event controller.
 * The event controller will generate some event that affects how the elevator(s)
 * operate(s). 
 * The elevator(s) run(s) an algorithm to handle these events.
 * 
 * @author Max Ruiz
 */
public class Building
{
  private BuildingConfig m_buildingConfig = null;

  private EventController m_eventController = new EventController();
  private EventController.EventType m_currentEvent = EventController.EventType.IDLE;

  private ArrayList<Elevator> m_elevators = new ArrayList<>();
  private Elevator m_elevator;

  private ArrayList<ArrayList<Passenger>> m_passengersPerFloor = new ArrayList<ArrayList<Passenger>>();

  private boolean m_onFire = false;

  Random m_rng = new Random();

  /**
   * Constructor for building which takes all of the values a building needs to be configured.
   * @param lowestFloor - set the lowest floor, or ground level, that the building has. 
   *                      This could be  negative if there was a basement, for example.
   * @param highestFloor - set the highest floor, or roof level, that the building has.
   * @param maxNumPassengerPerFloor - this parameter limits how many random passengers
   *                                  can be generated per floor so that there is no 
   *                                  overflow or memory issues. Plus, in reality, there
   *                                  can only be so many people.
   */
  public Building(int lowestFloor, int highestFloor, int maxNumPassengerPerFloor, 
                  boolean useCustomEvents, ArrayList<ElevatorConfig> elevatorConfigs)
  {
    m_buildingConfig = new BuildingConfig(lowestFloor, highestFloor, maxNumPassengerPerFloor, 
                                          useCustomEvents, elevatorConfigs);

    init();
    
  }

  /**
   * Constructor for Building which takes in a BuildingConfig class, which holds all the values
   * needed to configure the building.
   * @param bc BuildingConfig is a class that holds all the values needed to configure the building.
   * @see BuildingConfig
   */
  public Building(BuildingConfig bc)
  {
    m_buildingConfig = bc;

    init();
  }

  /**
   * Constructor for Building which takes no parameters and uses the default configurations from
   * the class BuildingConfig, which holds all the values needed to configure the building.
   * @see BuildingConfig
   */
  public Building()
  {
    m_buildingConfig = new BuildingConfig();
  }

  /**
   * Macro function to initialize the Building. This function exists because there are more than
   * one constructors that need to do the same thing, and there are many things to do.
   * This method (and essentially constuctor) throws an error if the provided lowest and highest floors
   * are equal or opposite of their intended relative value
   * @throws IllegalArgumentException
   */
  private void init()
  {
    if (m_buildingConfig.getLowestFloor() >= m_buildingConfig.getHighestFloor())
    {
      throw new IllegalArgumentException("Lowest and highest floor are invalid.");
    }

    // We only need floor many columns
    int numFloors = m_buildingConfig.getHighestFloor() - m_buildingConfig.getLowestFloor();

    // Want highest floor accessable
    for (int i = 0; i <= numFloors; i++)
    {
      m_passengersPerFloor.add(new ArrayList<Passenger>());
    }

    loadCustomEvents();

    updateCurrentEvent();

    loadElevators();
  }

  /**
   * Create the elevator with a set of parameters.
   */
  private void loadElevators()
  {
    for (ElevatorConfig ec : m_buildingConfig.getElevatorConfigs())
    {
      m_elevators.add(new Elevator(ec));

      System.out.println("\n===== Elevator Initialization =====");
      System.out.println("Building: Elevator ID: " + ec.getID());
      System.out.println("Building: Elevator Sqft: " + ec.getSqft());
      System.out.println("Building: Lowest Floor: " + ec.getLowestFloor());
      System.out.println("Building: Highest Floor: " + ec.getHighestFloor());
      System.out.println("Building: Elevator Starting Floor: " + ec.getStartingFloor());
      System.out.println("\n==================================");
    }
  }

  /**
   * Load a list of custom events to force the events of the building.
   */
  private void loadCustomEvents()
  {
    // Desired feature: Extract events data from json config file

    m_eventController.loadCustomEventList();
  }

  /**
   * Set the event to affect the rest of the system. The event 
   * is either pulled from the list of custom events or it is
   * randomly generated.
   */
  private void updateCurrentEvent()
  {
    if (m_buildingConfig.usingCustomEvents())
    {
      m_currentEvent = m_eventController.getNextCustomEvent();
    }
    else 
    {
      m_currentEvent = m_eventController.getNextRandomEvent();
    }
  }

  /**
   * Run the event loop for the building.
   * Events are procurred and applied to the system.
   */
  public void operate()
  {
    switch (m_currentEvent)
    {
      case IDLE:
        System.out.println("\nBuilding: ----- IDLE EVENT -----\n");
        handleIdleEvent();
        break;

      case PASSENGERS:
        System.out.println("\nBuilding: ----- PASSENGER EVENT -----\n");
        handlePassengersEvent();
        break;

      case STUCK:
        System.out.println("\nBuilding: ----- STUCK EVENT -----\n");
        handleStuckEvent();
        break;

      case FIRE:
        System.out.println("\nBuilding: ----- FIRE EVENT -----\n");
        handleFireEvent();
        break;

      default:
        System.out.println("\nBuilding: ----- DEFAULT IDLE EVENT -----\n");
        handleIdleEvent();
    }

    handleElevator();

    updateCurrentEvent();
  }

  /**
   * Handle method for an IDLE event.
   * @see EventController
   */
  private void handleIdleEvent()
  {
    // Nothing atm
  }

  /**
   * Handle method for a PASSENGERS event. Essentially this creates 
   * a basic passenger with priority: Civilian. The passenger is placed
   * in a ListArray row based on what floor they were generated at
   * and at a column next in line with other passengers on that floor,
   * assuming they can fit.
   * @see PassengerPriority
   * @see EventController
   * @see Elevator
   */
  private void handlePassengersEvent()
  {
    if (m_onFire)
    {
      return;
    }

    // Generate 1 regular passenger (aka civilian)

    int passengerFloor = getRandomFloor();
    
    Civilian civ = new Civilian(passengerFloor, m_buildingConfig.getLowestFloor(), m_buildingConfig.getHighestFloor());;
    
    boolean canFitPassenger = canFitPassengerOnFloor(passengerFloor);

    if (canFitPassenger)
    {
      m_passengersPerFloor.get(passengerFloor).add(civ);
      System.out.println("Building: Passenger " + civ.getID() + " Submitted Request: Request Floor: " + passengerFloor + " Destination Floor: " + civ.getDestinationFloor());
    }
    else 
    {
      System.out.println("Building: Could not fit passenger on floor.");
    }

    // Try to get their request to an elevator
    for (Elevator e : m_elevators)
    {
      if (canFitPassenger)
      {
        e.receivePassengerRequest(passengerFloor);

        // No need to continue if the passenger floor request has been accepted.
        break;
      }
    }
  }

  /**
   * Handle method for a STUCK event. This creates a passenger of
   * priority: Maintenance. It shoves the passenger onto it's corresponding floor,
   * even if there was no room (by removing another passenger) and causes a state
   * change for the elevator.
   * @see PassengerPriority
   * @see EventController
   * @see Elevator
   */
  private void handleStuckEvent()
  {
    if (m_onFire)
    {
      return;
    }

    int stuckElevatorIndex;
    Elevator elevator = null;
    ArrayList<Elevator> tempElevators = null;

    tempElevators = new ArrayList<Elevator>(m_elevators);
    
    // Gaurentee a stuck elevator, if there are any that are not already stuck
    for (int i = 0; i < tempElevators.size(); i++)
    {
      stuckElevatorIndex = m_rng.nextInt(tempElevators.size());
      elevator = tempElevators.get(stuckElevatorIndex);

      if (elevator.requiresMaintenance())
      {
        tempElevators.remove(elevator);
        continue;
      }
      else 
      {
        elevator = m_elevators.get(stuckElevatorIndex);
        break;
      }
    }

    int stuckFloor = elevator.getCurrentFloor();
    MaintenanceStaff staff = new MaintenanceStaff(stuckFloor, m_buildingConfig.getLowestFloor(), m_buildingConfig.getHighestFloor());

    System.out.println("Building: Stuck Floor: " + stuckFloor);
    if (canFitPassengerOnFloor(stuckFloor))
    {
      m_passengersPerFloor.get(stuckFloor).add(staff);
    }
    else 
    {
      System.out.println("Building: Maintenance Staff forced their way in line.");
      m_passengersPerFloor.get(stuckFloor).remove(0);
      m_passengersPerFloor.get(stuckFloor).add(staff);
    }

    System.out.println("Building: Maintenance Request Submitted");
    System.out.println("Building: ID: " + staff.getID());

    elevator.receiveMaintenanceRequest(stuckFloor);

    // Future endeavor, but for now...
    elevator.hasBeenRepaired();
  }

  /**
   * Handle method for a FIRE event. This creates a passenger of 
   * priority: Firefighter. It shoves the firefighter onto the ground floor
   * even if there was no room (by removing another passenger) and causes a 
   * state change for the elevator.
   * @see PassengerPriority
   * @see EventController
   * @see Elevator
   */
  private void handleFireEvent()
  {
    // scope of this project is to handle only 1 floor on fire at a time

    if (m_onFire)
    {
      return;
    }

    m_onFire = true;
    
    int floorOnFire = m_rng.nextInt(m_buildingConfig.getHighestFloor());
    Elevator elevator = null;

    for (int i = 0; i < m_elevators.size(); i++)
    {
      elevator = m_elevators.get(i);

      if (false == elevator.canReachFloor(floorOnFire))
      {
        continue;
      }
      else 
      {
        break;
      }
    }
    
    
    Firefighter firefighter = new Firefighter(floorOnFire, m_buildingConfig.getLowestFloor(), m_buildingConfig.getHighestFloor());

    System.out.println("Building: Floor on Fire: " + floorOnFire);

    System.out.println("Building: Everyone is evacuating via the stairs.");

    for (ArrayList<Passenger> plist : m_passengersPerFloor)
    {
      plist.clear();
    }

    m_passengersPerFloor.get(0).add(firefighter);

    System.out.println("Building: Firefighter Request Submitted.");
    System.out.println("Building: ID: " + firefighter.getID());

    m_elevator.receiveFirefighterRequest(0, floorOnFire);

    // Future endeavor, but for now...
    m_onFire = false;
  }

  /**
   * Handle the elevator event loop. Events have been created and applied to the 
   * system, now the elevator needs to react to those events.
   * @see Elevator
   */
  private void handleElevator()
  {

    for (Elevator elevator : m_elevators)
    {
      // Let elevator drop people off, if it's at a floor, or keep moving if it's not
      elevator.operate();

      // Check if anyone can get on the elevator at this floor
      if (elevator.atFloor())
      {
        System.out.println("Building: Elevator " + elevator.getID() + " is at a floor: " + elevator.getCurrentFloor());
        
        ArrayList<Passenger> passengers = m_passengersPerFloor.get(elevator.getCurrentFloor());
        
        System.out.println("Building: Number of passengers on floor " + elevator.getCurrentFloor() + " is: " + passengers.size());
        
        for (int i = 0; i < passengers.size(); i++)
        {
          Passenger p = passengers.get(i);

          // if there are passengers waiting on the floor, then they will only try to
          // board the elevator if it's going in the same direction 
          // or if the elevator is not yet going anywhere 
          // or if there are no passengers on board 
          // or if the elevator reached its destination, then it's not just moving along.
          if (p.getDestinationDirection() == elevator.getCurrentDirection() ||
              Direction.IDLE == elevator.getCurrentDirection() ||
              false == elevator.hasPassengers() ||
              true == elevator.atTargetFloor())
          {
            // Try to accept the passenger
            if (elevator.acceptPassenger(p))
            {
              System.out.println("Building: Passenger " + p.getID() + " left floor " + elevator.getCurrentFloor());
              passengers.remove(i);
              i--;
            }
            else 
            {
              // The passenger could not fit on the elevator
              System.out.println("Building: Passenger " + p.getID() + " could not fit on the elevator.");
              System.out.println("Building: Passenger resubmitted request.");
              System.out.println("Building: ID: " + p.getID() + " Request Floor: " + p.getOriginFloor() + " Destination Floor: " + p.getDestinationFloor());
          
              elevator.receivePassengerRequest(p.getOriginFloor());
            }
          }
          else 
          {
            // Passenger requests this floor again, but for the opposite direction
            System.out.println("Building: Passenger " + p.getID() + " did not get on the elevator because it was going the wrong direction.");
            System.out.println("Building: Passenger resubmitted request.");
            System.out.println("Building: ID: " + p.getID() + " Request Floor: " + p.getOriginFloor() + " Destination Floor: " + p.getDestinationFloor());
        
            elevator.receivePassengerRequest(p.getOriginFloor());
          }
        }
      }
      else 
      {
        System.out.println("Building: Elevator " + elevator.getID() + " is between floors.");
      }
    }
  }

  /**
   * @return int - a random floor value within the bounds of
   *             LOWEST_FLOOR and HIGHEST_FLOOR
   */
  private int getRandomFloor()
  {
    return m_rng.nextInt(m_buildingConfig.getLowestFloor(), m_buildingConfig.getHighestFloor());
  }

  /**
   * @param floor - provide the floor that the passenger is trying to fit onto.
   * @return boolean - whether or not the passenger can fit on the floor based on
   *                   how many passengers are currently on the floor, bound by
   *                   the limit: m_maxNumPassengerPerFloor
   */
  private boolean canFitPassengerOnFloor(int floor)
  {
    if (floor < m_buildingConfig.getLowestFloor() || floor > m_buildingConfig.getHighestFloor())
    {
      return false;
    }

    if (m_passengersPerFloor.get(floor).size() < m_buildingConfig.getMaxNumPassengerPerFloor())
    {
      return true;
    }

    return false;
  }

}

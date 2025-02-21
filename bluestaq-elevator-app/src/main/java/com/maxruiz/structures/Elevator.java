package com.maxruiz.structures;

import java.util.ArrayList;

import com.maxruiz.passengers.Passenger;
import com.maxruiz.config.ElevatorConfig;
import com.maxruiz.utility.Direction;

/**
 * This class simulates the brain of the elevator and runs the algorithms
 * for various states, driven by the events of the building and passengers.
 * 
 * This class is NOT thread safe. Each instance shares a queue of requests.
 * @author Max Ruiz
 */
public class Elevator 
{
  private enum Position 
  {
    AT_FLOOR,
    MOVING,
    SIZE
  }

  private enum State 
  {
    NORMAL,
    MAINTENANCE,
    FIRE,
    SIZE
  }

  private final int ELEVATOR_ID;
  private final int MAX_SQFT;
  private final int HIGHEST_FLOOR;
  private final int LOWEST_FLOOR;
  private final int AT_FLOOR_FRAMES;
  private final int MOVING_FRAMES;

  private Position m_position = Position.AT_FLOOR;
  private Direction m_currentDirection = Direction.IDLE;
  private State m_state = State.NORMAL;

  private int m_atFloorFrameCounter = 0;
  private int m_movingFrameCounter = 0;

  private int m_targetFloor;
  private boolean m_targetFloorIsRequestFloor = true;
  private int m_currentFloor;
  
  private static ArrayList<Integer> m_requestFloorList = new ArrayList<>();
  private static ArrayList<Integer> m_requestsBeingHandled = new ArrayList<>();
  private int m_passengerAtFloorIndex = 0;

  private int m_onBoardSpaceTaken = 0;
  private ArrayList<Passenger> m_passengers = new ArrayList<>();

  /**
   * @param id - Elevator ID
   * @param sqft - effective size of the elevator, this will limit the number of passengers
   * @param lowestFloor - lowest floor of the building
   * @param highestFloor - highest floor of the building
   * @param startingFloor - starting floor of the elevator
   */
  public Elevator(int id, int sqft, int lowestFloor, int highestFloor, int startingFloor, 
                  int atFloorFrames, int movingFrames)
  {
    ELEVATOR_ID = id;
    MAX_SQFT = sqft;
    LOWEST_FLOOR = lowestFloor;
    HIGHEST_FLOOR = highestFloor;
    m_currentFloor = startingFloor;
    m_targetFloor = m_currentFloor;

    // POSSIBLE FEATURE: Pull this data in from a config file
    AT_FLOOR_FRAMES = atFloorFrames;
    MOVING_FRAMES = movingFrames;
  }

  /**
   * @param ec - Elevator config class that holds necessary intialization values.
   */
  public Elevator(ElevatorConfig ec)
  {
    ELEVATOR_ID = ec.getID();
    MAX_SQFT = ec.getSqft();
    LOWEST_FLOOR = ec.getLowestFloor();
    HIGHEST_FLOOR = ec.getHighestFloor();
    m_currentFloor = ec.getStartingFloor();
    m_targetFloor = m_currentFloor;

    // POSSIBLE FEATURE: Pull this data in from a config file
    AT_FLOOR_FRAMES = ec.getAtFloorFrames();
    MOVING_FRAMES = ec.getMovingFrames();
  }

  /**
   * @return
   */
  public int getID()
  {
    return ELEVATOR_ID;
  }

  /**
   * @return is the position of the elevator at a floor or not (i.e. in motion)?
   */
  public boolean atFloor()
  {
    return (Position.AT_FLOOR == m_position);
  }

  /**
   * @return
   */
  public int getCurrentFloor()
  {
    return m_currentFloor;
  }

  /**
   * @return
   */
  public Direction getCurrentDirection()
  {
    return m_currentDirection;
  }

  /**
   * @return is the current floor the target floor the elevator was headed?
   */
  public boolean atTargetFloor()
  {
    return m_targetFloor == m_currentFloor;
  }

  /**
   * Check if the requested floor is a viable option for this particular elevator
   * @param requestFloor
   * @return boolean
   */
  public boolean passengerRequestInRange(int requestFloor)
  {
    return ((requestFloor <= HIGHEST_FLOOR) || 
            (requestFloor >= LOWEST_FLOOR));
  }

  /**
   * This function will push back a passengers request IFF it's not already in the queue
   * It does not accept multiple requests even though a passenger could be requesting
   * to go up or down
   * @param requestFloor int - floor request from a passenger in the building
   */
  public void receivePassengerRequest(int requestFloor)
  {  
    if (m_requestFloorList.contains(requestFloor))
    {
      System.out.println("Elevator Control: Floor " + requestFloor + " has already been requested.");
      return;
    }

    m_requestFloorList.add(requestFloor);

    System.out.println("Elevator Control: Accepted passenger request at floor: " + requestFloor);
  }

  /**
   * @param stuckFloor floor that the building event caused the elevator to be stuck at
   */
  public void receiveMaintenanceRequest(int stuckFloor)
  {
    m_state = State.MAINTENANCE;
  }

  /**
   * @param originFloor floor that the firefighter starts on
   * @param floorOnFire floor that's on fire
   */
  public void receiveFirefighterRequest(int originFloor, int floorOnFire)
  {
    m_state = State.FIRE;
  }

  /**
   * The elevator has a finite amount of space, check how much of that is used and send back the remainder
   * @return int 
   */
  public int remainingSpace()
  {
    return (MAX_SQFT - m_onBoardSpaceTaken);
  }

  /**
   * Check if the passenger can get aboard the elevator.
   * Can they fit?
   * Is their request within the elevators range?
   * @param p Passenger
   * @return boolean
   * @see Passenger
   */
  public boolean canAcceptPassenger(Passenger p)
  {
    // Passenger is too big
    if (p.getSqft() > remainingSpace())
    {
      System.out.println(elevatorHeaderStr() + "Passenger is too big. Passenger denied!");
      return false;
    }

    // Passenger wants to go somewhere this elevator can't
    if (p.getDestinationFloor() < LOWEST_FLOOR ||
        p.getDestinationFloor() > HIGHEST_FLOOR)
    {
      System.out.println(elevatorHeaderStr() + "Passenger wants to go to a floor this elevator cannot get to. Passenger denied!");
      return false;
    }
    
    return true;
  }

  /**
   * If the passenger meets the conditions to come aboard, then let them.
   * Add the passenger to the elevator list
   * Set the passenger flag to onBoard
   * Remove the passengers request from the elevator request list
   * Effectively take away the passengers space from available space on the elevator
   * @param passenger
   * @return boolean - true: passenger accepted, false: passenger denied
   * @see Passenger
   * @see canAcceptPassenger
   */
  public boolean acceptPassenger(Passenger passenger)
  {
    if (false == canAcceptPassenger(passenger))
    {
      return false;
    }

    passenger.setOnElevator(true);

    m_passengers.add(passenger);

    System.out.println(elevatorHeaderStr() + "Passenger " + passenger.getID() + " entered the elevator");

    Integer passengerOriginFloor = Integer.valueOf(passenger.getOriginFloor());

    if (m_requestFloorList.contains(passengerOriginFloor))
    {
      m_requestFloorList.remove(passengerOriginFloor);
      System.out.println(elevatorHeaderStr() + "Passenger " + passenger.getID() + "'s request " + passenger.getOriginFloor() + " was removed.");  
    }
    
    m_onBoardSpaceTaken += passenger.getSqft();

    return true;
  }

  /**
   * Does this elevator contain any passengers?
   * @return
   */
  public boolean hasPassengers()
  {
    return (!m_passengers.isEmpty());
  }

  /**
   * Main event loop handler for the elevator
   * This is the elevators action entry point
   */
  public void operate()
  {
    switch (m_state)
    {
      case NORMAL:
        System.out.println(elevatorHeaderStr() + "NORMAL OPERATION");
        normalOperation();
        break;
      case MAINTENANCE:
        System.out.println(elevatorHeaderStr() + "MAINTENANCE OPERATION");
        maintenanceOperation();
        break;
      case FIRE:
        System.out.println(elevatorHeaderStr() + "FIRE OPERATION");
        fireOperation();
        break;
      default:
        System.out.println(elevatorHeaderStr() + "DEFAULT NORMAL OPERATION");
        normalOperation();
    }
  }

  /**
   * The elevator is working as expected and under no outstanding circumstances
   */
  private void normalOperation()
  {
    for (Passenger p : m_passengers)
    {
      p.operate();
    }

    handleAtFloorPositionNormal();

    handleMovingPositionNormal();
  }

  /**
   * The elevator has been affected by the building and requires maintenance
   */
  private void maintenanceOperation()
  {
    handleAtFloorPositionMaintenance();

    handleMovingPositionMaintenance();

    // Future endeavor
    m_state = State.NORMAL;
  }

  /**
   * One of the floors in the building caught fire and now this elevator needs to run in firefighter mode
   */
  private void fireOperation()
  {
    handleAtFloorPositionFire();

    handleMovingPositionFire();

    // Future endeavor
    m_state = State.NORMAL;
  }

  /**
   * There is a static list of requests that passengers enter for all instances of elevators to check on
   * and this method will check if the elevator meets the requirements to accept the next request,
   * assuming there is one
   * @return boolean
   */
  private boolean canTakeNextRequestFromList()
  {
    // Are there more requests to take?
    if (m_requestFloorList.isEmpty())
    {
      System.out.println(elevatorHeaderStr() + "Cannot take request. Request floor list is empty.");
      return false;
    }

    int possibleNextRequest = m_requestFloorList.get(0);
    
    // Can this particular elevator even travel to that floor?
    if (false == passengerRequestInRange(possibleNextRequest))
    {
      System.out.println(elevatorHeaderStr() + "Cannot take request. It's out of range.");
      return false;
    }

    // Is another elevator already working on this request?
    if (m_requestsBeingHandled.contains(Integer.valueOf(possibleNextRequest)))
    {
      System.out.println(elevatorHeaderStr() + "Cannot take request. Already being handled by another elevator.");
      return false;
    }

    return true;
  }

  /**
   * A request floor is made by a passenger on a floor of the building
   * This can vary from a request made by a passenger who is already on the elevator
   * Therefore we need to distinguish if the targetFloor is from a passenger from
   * outside or inside the elevator. The reason to distinguish this is so that
   * multiple elevators aren't trying to head to the same passenger who made a 
   * request on a floor of the building. So we also keep track of the proper 
   * requests being handled, so that any requests in the queue, made by
   * passengers on a floor, can be polled appropriately by the next elevator
   * without any overlap.
   * @return boolean
   */
  private boolean setTargetFloorAsNextRequestFromList()
  {
    if (false == canTakeNextRequestFromList())
    {
      return false;
    }

    m_targetFloor = m_requestFloorList.get(0);

    m_requestFloorList.remove(Integer.valueOf(m_targetFloor));

    m_requestsBeingHandled.add(m_targetFloor);

    m_targetFloorIsRequestFloor = true;

    return true;
  }

  /**
   * If the target floor was reached and this target floor was a request made by a passenger
   * not in the elevator, but rather a passenger on a floor of the building, then we
   * need to make sure that we remove the request floor from the queue that holds the floors
   * actively being handled by an elevator, as this elevator handled this request.
   */
  private void requestHandled()
  {
    if (m_targetFloorIsRequestFloor)
    {
      m_requestsBeingHandled.remove(Integer.valueOf(m_targetFloor));
    }
    
  }

  /**
   * When the elevator is at a floor, it needs to run operations to control
   * the flow of passengers and choose where it needs to go next, if anywhere
   */
  private void handleAtFloorPositionNormal()
  {
    // Wrong state, return
    if (Position.MOVING == m_position)
    {
      return;
    }

    // We're only going to operate on one passenger per frame
    // There may be a case where not all the passengers who
    // should get off at this floor CAN get off at this floor
    // Why? This simulates a packed elevator
    if (m_passengers.size() > m_passengerAtFloorIndex)
    {
      Passenger p = m_passengers.get(m_passengerAtFloorIndex);
      m_passengerAtFloorIndex++;

      // Remove passenger if this was their destination
      if (p.isAtDestinationFloor() || p.isSick())
      {
        System.out.println(elevatorHeaderStr() + "Passenger " + p.getID() + " got OFF the elevator");
        m_passengers.remove(p);
        m_onBoardSpaceTaken -= p.getSqft();
        // one fewer passengers
        m_passengerAtFloorIndex--;

        // If passenger gets off early, i.e. they're sick
        // the elevators target floor is still set to their original destination
        // In reality, it would still be set there even though they got off early
        // so if no one was on the elevator, it would still continue to the
        // original destination floor, even if someone, somewhere else had put in
        // a request in the opposite direction.
      }
      else if (atTargetFloor())
      {
        // If this was not the next passengers destination, but was the elevator destination floor
        // or some other passengers destination, then the next passenger gets to choose the 
        // new destination, assuming there are any passengers on board

        System.out.println(elevatorHeaderStr() + "Next Passenger " + p.getID() + " has set target floor: " + p.getDestinationFloor());
        m_targetFloor = p.getDestinationFloor();
        m_targetFloorIsRequestFloor = false;

        // The target floor cannot be equal to the current floor
        // otherwise this passenger would have been removed
        if (m_targetFloor < m_currentFloor)
        {
          m_currentDirection = Direction.DOWN;
        }
        else 
        {
          m_currentDirection = Direction.UP;
        }
      }
      else 
      {
        System.out.println(elevatorHeaderStr() + "Passenger " + p.getID() + " is waiting.");
      }
    }

    // If there are no passengers, and the elevator is idle
    // Then is must be waiting for a request
    if (m_passengers.size() == 0 && 
        Direction.IDLE == m_currentDirection)
    {
      // Scan for new requests
      if (setTargetFloorAsNextRequestFromList())
      {
        m_currentDirection = (m_targetFloor < m_currentFloor) ? Direction.DOWN : Direction.UP;
        
        // 1) There's no one in the elevator
        // 2) The current request is at another floor
        // There's no reason for the elevator to stay here
        // If someone, for example, would push the request button on this
        // floor, one tick after the other request was made,
        // then they, sadly, missed the elevator. Whomp.
        if (m_targetFloor != m_currentFloor)
        {
          System.out.println(elevatorHeaderStr() + "New request, no one aboard, time to go!");
          m_atFloorFrameCounter = AT_FLOOR_FRAMES;
        }

        System.out.println(elevatorHeaderStr() + "Next request being processed: " + m_targetFloor);
        System.out.println(elevatorHeaderStr() + "Target Direction: " + directionString());
      }
      else 
      {
        // we stay here
        System.out.println(elevatorHeaderStr() + "Doors remaining Closed.");
        m_atFloorFrameCounter = 0;
      }
    }


    if (AT_FLOOR_FRAMES == m_atFloorFrameCounter)
    {
      m_atFloorFrameCounter = 0;
      m_position = Position.MOVING;
      m_passengerAtFloorIndex = 0;
      System.out.println(elevatorHeaderStr() + "Closing Doors.");

      // If we're at the target floor by the time the doors are about to close
      // then no one has made any new requests and we can start scanning for
      // more
      if (atTargetFloor())
      {
        m_currentDirection = Direction.IDLE;

        System.out.println(elevatorHeaderStr() + "No new requests. Idling.");
      }

    }
    else 
    {
      if (m_currentDirection != Direction.IDLE)
      {
        if (atTargetFloor())
        {
          // If we're at the target floor we need to check
          // 1) Are there any passengers that need to leave?
          // 2) Are there any new requests?
          m_currentDirection = Direction.IDLE;

          // set request handled if the target floor was also a request floor
          requestHandled();
        }
        else
        {
          m_atFloorFrameCounter++;
          System.out.println(elevatorHeaderStr() + "Doors remaining open.");
        }
      }
    }
  }

  /**
   * When the elevator is moving, it needs to run operations to control
   * the flow of passengers 
   */
  private void handleMovingPositionNormal()
  {
    // Wrong state, return
    if (Position.AT_FLOOR == m_position)
    {
      return;
    }

    // we know we're not moving because there has been no
    // new requests
    if (Direction.IDLE == m_currentDirection)
    {
      m_position = Position.AT_FLOOR;
      return;
    }

    // Cannot continue to move down if we're at the lowest floor
    // and that's the direction we're moving, so idle
    if (movingDown() && atGround())
    {
      m_movingFrameCounter = MOVING_FRAMES;
      m_currentDirection = Direction.IDLE;
      System.out.println(elevatorHeaderStr() + "At Ground floor. Cannot travel further down.");
    }

    // Cannot continue to move up if we're at the highest floor
    // and that's the direction we're moving, so idle
    if (movingUp() && atRoof())
    {
      m_movingFrameCounter = MOVING_FRAMES;
      m_currentDirection = Direction.IDLE;
      System.out.println(elevatorHeaderStr() + "At Roof floor. Cannot travel further up.");
    }

    // done moving
    if (MOVING_FRAMES == m_movingFrameCounter)
    {
      // reset moving counter
      m_movingFrameCounter = 0;

      // set new state
      m_position = Position.AT_FLOOR;

      // Set new floor
      if (movingDown())
      {
        m_currentFloor--;

        // All passengers have now successfully moved down
        for (Passenger p : m_passengers)
        {
          p.traveledDown();
        }

        System.out.println(elevatorHeaderStr() + "Reached floor: " + m_currentFloor);
      }
      else if (movingUp())
      {
        m_currentFloor++;

        // All passengers have now successfully moved up
        for (Passenger p : m_passengers)
        {
          p.traveledUp();
        }

        System.out.println(elevatorHeaderStr() + "Reached floor: " + m_currentFloor);
      }

      // No one has made a request
      // and we haven't made it to the target floor
      // and there are no people on the elevator
      if (false == m_requestFloorList.contains(m_currentFloor) &&
          m_currentFloor != m_targetFloor) //&&
          //true == m_passengers.isEmpty())
      {
        m_position = Position.MOVING;

        // QUESTIONABLE
        for (Passenger p : m_passengers)
        {
          if (p.isAtDestinationFloor())
          {
            m_position = Position.AT_FLOOR;
          }
        }
        
        if (Position.MOVING == m_position)
        {
          System.out.println(elevatorHeaderStr() + "Continuing on.");
        }
        
      }
      else 
      {
        
        System.out.println(elevatorHeaderStr() + "Opening doors.");
      }
    }
    else 
    {
      m_movingFrameCounter++;
      System.out.println(elevatorHeaderStr() + "Moving: " + directionString());
    }
  }
  
  /**
   * Future Endeavor
   */
  private void handleAtFloorPositionMaintenance()
  {

  }

  /**
   * Future Endeavor
   */
  private void handleMovingPositionMaintenance()
  {

  }

  /**
   * Future Endeavor
   */
  private void handleAtFloorPositionFire()
  {

  }

  /**
   * Future Endeavor
   */
  private void handleMovingPositionFire()
  {

  }

  /**
   * Is the elevator direction UP?
   * @return boolean
   */
  private boolean movingUp()
  {
    return (Direction.UP == m_currentDirection);
  }

  /**
   * Is the elevator direction DOWN
   * @return boolean
   */
  private boolean movingDown()
  {
    return (Direction.DOWN == m_currentDirection);
  }

  /**
   * Is the elevator at the highest floor it can reach?
   * @return boolean
   */
  private boolean atRoof()
  {
    return (HIGHEST_FLOOR == m_currentFloor);
  }

  /**
   * Is the elevator at the lowest floor it can reach?
   * @return boolean
   */
  private boolean atGround()
  {
    return (LOWEST_FLOOR == m_currentFloor);
  }

  /**
   * Is the state of the elevator in maintenance mode?
   * @return boolean
   */
  public boolean requiresMaintenance()
  {
    return State.MAINTENANCE == m_state;
  }

  /**
   * If the state of the elevator was in maintenance mode, then change it back to normal
   * as it has been repaired
   */
  public void hasBeenRepaired()
  {
    if (State.MAINTENANCE == m_state)
    {
      m_state = State.NORMAL;
    }
  }

  /**
   * Can the elevator make it to the value of floor. 
   * e.g. is the value of the floor within the elevators range
   * @param floor
   * @return boolean
   */
  public boolean canReachFloor(int floor)
  {
    return ( (floor >= LOWEST_FLOOR) && (floor <= HIGHEST_FLOOR) );
  }

  /**
   * A macro to convert the direction of the elevator into an informing string to print
   * @return String
   */
  private String directionString()
  {
    if (movingUp())
    {
      return "Going UP";
    }
    else if (movingDown())
    {
      return "Going DOWN";
    }
    else 
    {
      return "Remaining IDLE";
    }
  }

  /**
   * A macro to identify this elevator for printing
   * @return String
   */
  private String elevatorHeaderStr()
  {
    return "Elevator (" + String.valueOf(ELEVATOR_ID) + "): ";
  }

  // private boolean atMidway()
  // {
  //   // Thank you De Morgan
  //   return ( ! (atRoof() || atGround()) );
  // }

  // private boolean notMoving()
  // {
  //   return (Direction.IDLE == m_currentDirection);
  // }

}

package com.maxruiz.passengers;

import java.util.Random;

import javax.swing.text.Highlighter.Highlight;

import com.maxruiz.utility.Direction;

import com.maxruiz.config.PassengerConfig;

// The idea here is that there can be different types of people who 
// need to interact with the elevator.
// For Example:
// A civilian is a normal interaction, they wait in line, 
//    they have a weight, they go up, they go down...
// A Maintenance person jumps to the front of a civilian line,
//    haults the elevator, gets everyone off, works on the elevator,
//    gets off the elevator
// A Firefighter jumps to the front of the line no matter what,
//    haults the elevator, gets everyone off permanently, goes to
//    the fire (technically they go no closer than 2 floors to the fire
//    and use the stairs), gets off, puts out the fire, then gets back on,
//    then goes to the ground floor and leaves.
//    All other people in the building would take the stairs, meaning
//    all the queues are empty.


/**
 * This class defines a passenger that waits in line for an elevator,
 * puts in requests, gets on the elevator, has a destination, and gets off
 * the elevator. The passenger also has the ability to cause other events
 * internally that effect the operation of the elevator. An example is a 
 * passenger becoming sick or being too big to fit on the elevator.
 */
public class Passenger 
{
  protected final int HIGHEST_FLOOR;
  protected final int LOWEST_FLOOR;
  protected final int SQFT;
  protected final Integer PRIORITY;
  protected final int ID;

  protected Direction m_destinationDirection;
  protected int m_destinationFloor;
  protected int m_originFloor;
  protected int m_floorsTraveled = 0;
  protected int m_currentFloor;
  protected boolean m_isSick = false;
  protected boolean m_setSickFloor = false;
  protected boolean m_onElevator = false;
  protected Random m_randomGen;
  protected static int m_passengerCount = 0;
  
  /**
   * Constructor for Passenger which randomly generates which floor this passenger
   * is on, where they want to go, how big they are and other attributes that affect
   * the elevator system.
   * @param priority - This parameter defines what kind of status a passenger has which
   *                   could possibly affect events and how passengers playout in the 
   *                   overall system.
   * @param originFloor - the floor that this passenger starts on
   * @param lowestFloor - the lowest floor in the building
   * @param highestFloor - the highest floor in the building
   */
  public Passenger(Integer priority, int originFloor, int lowestFloor, 
                   int highestFloor, int sqft)
  {
    ID = m_passengerCount++;
    SQFT = sqft;
    LOWEST_FLOOR = lowestFloor;
    HIGHEST_FLOOR = highestFloor;
    PRIORITY = priority;
    m_originFloor = originFloor;
    m_currentFloor = m_originFloor;

    init();
    
  }

  /**
   * Constructor for Passenger that takes in a PassengerConfig to build this instance
   * @param pc
   * @see PassengerConfig
   */
  public Passenger(PassengerConfig pc)
  {
    ID = m_passengerCount++;
    LOWEST_FLOOR = pc.getLowestFloor();
    HIGHEST_FLOOR = pc.getHighestFloor();
    SQFT = pc.getSqft();
    PRIORITY = pc.getPriority();
    m_originFloor = pc.getOriginFloor();
    m_currentFloor = m_originFloor;

    init();
  }

  private void init()
  {
    if (null == PRIORITY)
    {
      throw new IllegalArgumentException("Passenger Priority is Invalid.");
    }

    if (PRIORITY < 0)
    {
      throw new IllegalArgumentException("Priority is invalid.");
    }

    if (LOWEST_FLOOR >= HIGHEST_FLOOR)
    {
      throw new IllegalArgumentException("Lowest floor and highest floor values are invalid.");
    }

    if (m_originFloor < LOWEST_FLOOR || m_originFloor > HIGHEST_FLOOR)
    {
      throw new IllegalArgumentException("Origin floor is invalid.");
    }

    if (SQFT <= 0)
    {
      throw new IllegalArgumentException("Sqft value is invalid.");
    }

    m_randomGen = new Random();

    //m_destinationFloor
    setRandomDestinationFloor(m_originFloor, LOWEST_FLOOR, HIGHEST_FLOOR);

    if (m_destinationFloor < m_originFloor)
    {
      m_destinationDirection = Direction.DOWN;
    }
    else 
    {
      m_destinationDirection = Direction.UP;
    }

    m_isSick = false;
  }

  /**
   * @return int - passengers static ID
   */
  public final int getID()
  {
    return ID;
  }

  /**
   * Utility method to set this passengers destination to a random floor
   * @param originFloor - the floor that this passenger starts on
   * @param lowestFloor - the lowest floor in the building
   * @param highestFloor - the highest floor in the building
   */
  private final void setRandomDestinationFloor(int originFloor, int lowestFloor, int highestFloor)
  {
    // takes care of lowest and highest floors being the same floor or out of order
    // No delta
    if (lowestFloor >= highestFloor)
    {
      m_destinationFloor = highestFloor;
    }

    // Try to get a random destination floor
    for (int i = 0; i < 3; i++)
    {
      m_destinationFloor = m_randomGen.nextInt(lowestFloor, highestFloor);

      if (m_destinationFloor != originFloor)
      {
        return;
      }
    }

    // If the destination floor kept being the origin floor, force it to not be
    // This could reasonably be the case if the lowestFloor and highestFloor 
    //    were 1 floor apart
    if (originFloor == lowestFloor)
    {
      m_destinationFloor = originFloor + 1;
    }
    else 
    {
      m_destinationFloor = originFloor - 1;
    }
  }

  /**
   * @return int
   */
  public final int getDestinationFloor()
  {
    return m_destinationFloor;
  }

  /**
   * @param floor
   */
  public final void setDestinationFloor(int floor)
  {
    m_destinationFloor = floor;
  }

  /**
   * @return Direction
   * @see Direction
   */
  public final Direction getDestinationDirection()
  {
    return m_destinationDirection;
  }

  /**
   * @return int
   */
  public final int getSqft()
  {
    return SQFT;
  }

  /**
   * @return int
   */
  public final int getOriginFloor()
  {
    return m_originFloor;
  }

  /**
   * @param value
   */
  public final void setOnElevator(boolean value)
  {
    m_onElevator = value;
  }

  /**
   * @return boolean
   */
  public final boolean getOnElevator()
  {
    return m_onElevator;
  }

  /**
   * @param floor
   */
  public final void setOriginFloor(int floor)
  {
    m_originFloor = floor;
  }

  /**
   * @return int
   */
  public final int getCurrentFloor()
  {
    return m_currentFloor;
  }

  /**
   * @return boolean - is the currentFloor equal to the destination floor?
   */
  public final boolean isAtDestinationFloor()
  {
    return (m_currentFloor == m_destinationFloor);
  }

  /**
   * A passenger on board the elevator will move up a floor
   * and this will also change how many floors the passenger has moved overall
   */
  public final void traveledUp()
  {
    m_currentFloor++;
    m_floorsTraveled++;
  }

  /**
   * A passenger on board the elevator will move down a floor
   * and this will also change how many floors the passenger has moved overall
   */
  public final void traveledDown()
  {
    m_currentFloor--;
    m_floorsTraveled++;
  }

  /**
   * @return int
   */
  public final int getPriority()
  {
    return PRIORITY.intValue();
  }

  /**
   * @return boolean
   */
  public final boolean isSick()
  {
    return m_isSick;
  }

  /**
   * This method is expected to be overriden by a subclass to use some function
   * to determine if this passenger gets sick.
   * 
   * The fact that a passenger gets sick adds another layer of complexity to the 
   * system of the elevator and how it operates.
   */
  protected void calculateIfSick()
  {
    // do stuff in subclasses
    m_isSick = false;
  }

  /**
   * This is the main event loop for a passenger which runs its algorithm
   */
  public void operate()
  {
    if (m_onElevator)
    {
      calculateIfSick();

      if (m_isSick && !m_setSickFloor)
      {
        m_setSickFloor = true;

        if (Direction.UP == m_destinationDirection)
        {
          m_destinationFloor = m_currentFloor + 1;
        }
        else 
        {
          m_destinationFloor = m_currentFloor - 1;
        }
      }
    }
  }
}

package com.maxruiz.passengers;

import java.util.Random;

import com.maxruiz.utility.Direction;

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
public class Passenger {
  final int MIN_PASSENGER_SQFT = 2;

  // with rng max boundary being exclusive
  // the effective max will be MAX_PASSENGER_SQFT-1
  final int MAX_PASSENGER_SQFT = 10; 

  protected final int m_sqft;
  protected final Integer m_priority;
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
  protected final int ID;
 

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
  public Passenger(Integer priority, int originFloor, int lowestFloor, int highestFloor)
  {
    m_randomGen = new Random();

    ID = m_passengerCount;
    m_passengerCount++;

    m_sqft = m_randomGen.nextInt(MIN_PASSENGER_SQFT, MAX_PASSENGER_SQFT);

    m_originFloor = originFloor;
    m_currentFloor = m_originFloor;

    //m_destinationFloor
    setRandomDestinationFloor(m_originFloor, lowestFloor, highestFloor);

    if (m_destinationFloor < m_originFloor)
    {
      m_destinationDirection = Direction.DOWN;
    }
    else 
    {
      m_destinationDirection = Direction.UP;
    }

    m_isSick = false;

    m_priority = priority;

    if (null == m_priority)
    {
      throw new IllegalArgumentException("Passenger Priority is Invalid.");
    }
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
    return m_sqft;
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
    return m_priority.intValue();
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

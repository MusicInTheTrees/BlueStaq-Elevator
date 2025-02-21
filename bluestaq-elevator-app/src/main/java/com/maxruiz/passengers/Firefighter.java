package com.maxruiz.passengers;

import com.maxruiz.config.PassengerConfig;

/**
 * This class superclasses Passenger to override how this passenger gets sick
 * as well as provide a specific priority.
 * @see Passenger
 */
public class Firefighter extends Passenger
{
  private final double SICK_FACTOR;

  /**
   * Constructor for Firefighter that assumes a value for how and when this passenger
   * will get sick.
   * @param destinationFloor - where the firefighter needs to go, where the fire is
   * @param lowestFloor - the lowest floor in the building
   * @param highestFloor - the highest floor in the building
   */
  public Firefighter(int destinationFloor, int lowestFloor, int highestFloor, int sqft)
  {
    super(PassengerPriority.get().getPriorityFromTitle("firefighter"), 
          0, lowestFloor, highestFloor, sqft);
    SICK_FACTOR = 0.001;
  }

  /**
   * Consturctor for Firefighter that takes in a parameter to determine how and when this 
   * passenger will get sick.
   * @param destinationFloor - where the firefighter needs to go, where the fire is
   * @param sickFactor - the factor that determines how and when this passenger will get sick
   * @param lowestFloor - the lowest floor in the building
   * @param highestFloor - the highest floor in the building
   */
  public Firefighter(int destinationFloor, double sickFactor, int lowestFloor, int highestFloor, int sqft)
  {
    super(PassengerPriority.get().getPriorityFromTitle("firefighter"), 
          0, lowestFloor, highestFloor, sqft);
    SICK_FACTOR = sickFactor;
  }

  /**
   * Constructor for Firefighter that takes in a PassengerConfig to initialize all of the parameters
   * to build this instance.
   * @param pc
   * @see PassengerConfig
   */
  public Firefighter(PassengerConfig pc)
  {
    super(PassengerPriority.get().getPriorityFromTitle("firefighter"), 
          0, pc.getLowestFloor(), pc.getHighestFloor(), pc.getSqft());

    SICK_FACTOR = pc.getSickFactorAt(0);
  }

  /**
   * This method will use a simple equation with a single factor to determine how
   * and when this passenger will get sick.
   */
  @Override
  protected void calculateIfSick()
  {
    if (m_isSick)
    {
      return;
    }

    // Awww gross, I puked in my mask -_-
    m_isSick = (m_randomGen.nextDouble() < SICK_FACTOR ? true : false);
  }
  
}

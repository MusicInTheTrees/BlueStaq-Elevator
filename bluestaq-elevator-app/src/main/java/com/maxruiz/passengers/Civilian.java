package com.maxruiz.passengers;

import com.maxruiz.config.PassengerConfig;

/**
 * This class superclasses Passenger to override how this passenger gets
 * sick, as well as provide a specific priority.
 * @see Passenger
 */
public class Civilian extends Passenger
{
  private final double SICK_FACTOR_X0;
  private final double SICK_FACTOR_X1;

  /**
   * This constructor assumes defaults for the parameters affecting how
   * and when this passenger gets sick.
   * @param originFloor - where the passenger starts
   * @param lowestFloor - the lowest floor of the building
   * @param highestFloor - the highest floor of the building
   * @see Passenger
   */
  public Civilian(int originFloor, int lowestFloor, int highestFloor, int sqft)
  {
    super(PassengerPriority.get().getPriorityFromTitle("civilian"), 
          originFloor, lowestFloor, highestFloor, sqft);
    SICK_FACTOR_X0 = 0.01;
    SICK_FACTOR_X1 = 1.1;
  }

  /**
   * This constuctor accepts input values to calculate how and when this passenger
   * gets sick.
   * @param originFloor - where the passenger starts
   * @param sickFactorX0 - first factor in determining how and when this passenger gets sick
   * @param sickFactorX1 - second factor in determining how and when this passenger gets sick
   * @param lowestFloor - the lowest floor of the building
   * @param highestFloor - the highest floor of the building
   * @see Passenger
   */
  public Civilian(int originFloor, double sickFactorX0, double sickFactorX1,
                  int lowestFloor, int highestFloor, int sqft)
  {
    super(PassengerPriority.get().getPriorityFromTitle("civilian"), 
          originFloor, lowestFloor, highestFloor, sqft);
    SICK_FACTOR_X0 = sickFactorX0;
    SICK_FACTOR_X1 = sickFactorX1;
  }

  /**
   * Constructor for Civilian that takes in PassengerConfig to set the parameters for 
   * the parent class, Passenger, and uses default values for the sick factor parameters
   * @param pc
   */
  public Civilian(PassengerConfig pc)
  {
    super(pc.getPriority(), pc.getOriginFloor(), pc.getLowestFloor(), 
          pc.getHighestFloor(), pc.getSqft());
    SICK_FACTOR_X0 = pc.getSickFactorAt(0);
    SICK_FACTOR_X1 = pc.getSickFactorAt(1);
  }

  /**
   * This method will use parameters, as well as the knowledge of floors
   * this passenger has traveled, to determine whether or not this passenger gets sick.
   */
  @Override
  protected void calculateIfSick()
  {
    if (m_isSick)
    {
      return;
    }
    
    // I'm totally 1hunnit% sick bro  \m/
    double sickChance = m_floorsTraveled * SICK_FACTOR_X1;
    sickChance *= SICK_FACTOR_X0;
    m_isSick = (m_randomGen.nextDouble() < sickChance ? true : false);

    if (m_isSick)
    {
      System.out.println("Civilian: I've become sick :( ID: " + ID);
    }
  }
}

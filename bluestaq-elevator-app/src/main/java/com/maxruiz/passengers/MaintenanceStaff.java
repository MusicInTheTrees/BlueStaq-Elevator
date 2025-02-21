package com.maxruiz.passengers;

/**
 * This class superclasses Passenger to override how this passenger gets sick
 * as well as provide a specific priority.
 * @see Passenger
 */
public class MaintenanceStaff extends Passenger
{
  private final double SICK_FACTOR;
  
  /**
   * Constructor for MaintenanceStaff that assumes a value for how and when this passenger
   * will get sick.
   * @param destinationFloor - where the firefighter needs to go, where the fire is
   * @param lowestFloor - the lowest floor in the building
   * @param highestFloor - the highest floor in the building
   */
  public MaintenanceStaff(int originFloor, int lowestFloor, int highestFloor)
  {
    super(PassengerPriority.get().getPriorityFromTitle("maintenance"), 
          originFloor, lowestFloor, highestFloor);
    SICK_FACTOR = 0.01;
  }

  /**
   * Consturctor for MaintenanceStaff that takes in a parameter to determine how and when 
   * this passenger will get sick.
   * @param originFloor - which floor the MaintenanceStaff needs to service
   * @param sickFactor - the factor that determines how and when this passenger will get sick
   * @param lowestFloor - the lowest floor in the building
   * @param highestFloor - the highest floor in the building
   */
  public MaintenanceStaff(int originFloor, double sickFactor, int lowestFloor, int highestFloor)
  {
    super(PassengerPriority.get().getPriorityFromTitle("maintenance"), 
          originFloor, lowestFloor, highestFloor);
    SICK_FACTOR = sickFactor;
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

    // Being sick on the job suuuuucks >:(
    m_isSick = (m_randomGen.nextDouble() < SICK_FACTOR ? true : false);
  }
}

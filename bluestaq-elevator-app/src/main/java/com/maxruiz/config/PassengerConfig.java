package com.maxruiz.config;

import java.util.ArrayList;
import java.util.List;

import com.maxruiz.passengers.PassengerPriority;

public class PassengerConfig
{
  Integer m_priority;
  int m_originFloor;
  int m_lowestFloor;
  int m_highestFloor;
  int m_sqft;
  ArrayList<Double> m_sickFactors;

  /**
   * Constructor for PassengerConfig that takes all the paramaters used to build an instance of a 
   * Passenger
   * @param priority
   * @param originFloor
   * @param lowestFloor
   * @param highestFloor
   * @param minSqft
   * @param maxSqft
   */
  public PassengerConfig(Integer priority, int originFloor, int lowestFloor, int highestFloor, 
                         int sqft, ArrayList<Double> sickFactors)
  {
    m_priority = priority;
    m_originFloor = originFloor;
    m_lowestFloor = lowestFloor;
    m_highestFloor = highestFloor;
    m_sqft = sqft;
    if (null == sickFactors)
    {
      m_sickFactors = new ArrayList<>();
    }
    else 
    {
      m_sickFactors = sickFactors;
    }
    
  }

  /**
   * Constructor for PassengerConfig that loads all the parameters from a json config file if
   * possible, otherwise uses a set of default values
   */
  public PassengerConfig()
  {
    // Desired feature: Extract data from a json config file

    loadDefaultPassengerConfig();
  }

  public void loadDefaultPassengerConfig()
  {
    m_priority = PassengerPriority.get().getLowestPriority();
    m_originFloor = 0;
    m_lowestFloor = 0;
    m_highestFloor = 1;
    m_sqft = 4;
    m_sickFactors = new ArrayList<>( List.of(0.01, 1.1));
  }

  public Integer getPriority() {
    return m_priority;
  }

  public int getOriginFloor() {
    return m_originFloor;
  }

  public int getLowestFloor() {
    return m_lowestFloor;
  }

  public int getHighestFloor() {
    return m_highestFloor;
  }

  public int getSqft() {
    return m_sqft;
  }

  public ArrayList<Double> getSickFactors()
  {
    return m_sickFactors;
  }

  public Double getSickFactorAt(int index)
  {
    if (index >= m_sickFactors.size())
    {
      return 0.0;
    }

    return m_sickFactors.get(index);
  }
}

package com.maxruiz.config;

import java.util.ArrayList;

import com.maxruiz.structures.Building;

/**
 * This class holds the configuration data necessary to build a Building instance
 * @see Building
 */
public class BuildingConfig
{
  private int m_lowestFloor;
  private int m_highestFloor;
  private int m_maxNumPassengersPerFloor;
  private boolean m_useCustomEvents;
  private ArrayList<ElevatorConfig> m_elevatorConfigs = new ArrayList<>();

  /**
   * Constructor for BuildingConfig that accepts all of the configuration values as parameters
   * @param lowestFloor
   * @param highestFloor
   * @param maxNumPassengersPerFloor
   * @param useCustomEvents
   * @param elevatorConfigs
   */
  public BuildingConfig(int lowestFloor, int highestFloor, 
                        int maxNumPassengersPerFloor, boolean useCustomEvents,
                        ArrayList<ElevatorConfig> elevatorConfigs)
  {
    if (lowestFloor >= highestFloor)
    {
      throw new IllegalArgumentException("Lowest and highest floor are invalid.");
    }

    if (maxNumPassengersPerFloor <= 0)
    {
      throw new IllegalArgumentException("maxNumPassengersPerFloor is invalid.");
    }

    if (elevatorConfigs.isEmpty())
    {
      throw new IllegalArgumentException("There must be one or more ElevatorConfigs in the list.");
    }
    
    m_lowestFloor = lowestFloor;
    m_highestFloor = highestFloor;
    m_maxNumPassengersPerFloor = maxNumPassengersPerFloor;
    m_useCustomEvents = useCustomEvents;
    m_elevatorConfigs = elevatorConfigs;
  }

  /**
   * Constructor for BuildingConfig that loads a set of configuration data from a json file
   * or uses a set of default values if it cannot get the values from the json file
   */
  public BuildingConfig()
  {
    // Disired feature: Pull data from json file using the ResourceManager
    
    // else

    loadDefaultBuildingConfig();
  }

  /**
   * Assign a set of default configuration values
   */
  public void loadDefaultBuildingConfig()
  {
    m_useCustomEvents = false;

    m_lowestFloor = 0;
    m_highestFloor = 10;
    m_maxNumPassengersPerFloor = 3;
    m_elevatorConfigs.clear();
    m_elevatorConfigs.add(new ElevatorConfig(m_lowestFloor, m_highestFloor));

    m_elevatorConfigs.add(new ElevatorConfig(m_lowestFloor, m_highestFloor));
  }

  public int getLowestFloor() {
    return m_lowestFloor;
  }

  public int getHighestFloor() {
    return m_highestFloor;
  }

  public int getMaxNumPassengersPerFloor() {
    return m_maxNumPassengersPerFloor;
  }

  public boolean usingCustomEvents() {
    return m_useCustomEvents;
  }

  public ArrayList<ElevatorConfig> getElevatorConfigs()
  {
    return m_elevatorConfigs;
  }
}

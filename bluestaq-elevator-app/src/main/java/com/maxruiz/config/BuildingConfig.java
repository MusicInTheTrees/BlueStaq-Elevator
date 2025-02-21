package com.maxruiz.config;

import java.util.ArrayList;
import java.util.List;

import com.maxruiz.passengers.PassengerPriority;
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
  private boolean m_useCustomPassengers;
  private ArrayList<PassengerConfig> m_passengerConfigs = new ArrayList<>();

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
                        ArrayList<ElevatorConfig> elevatorConfigs,
                        boolean useCustomPassengers,
                        ArrayList<PassengerConfig> passengerConfigs)
  {    
    m_lowestFloor = lowestFloor;
    m_highestFloor = highestFloor;
    m_maxNumPassengersPerFloor = maxNumPassengersPerFloor;
    m_useCustomEvents = useCustomEvents;
    m_elevatorConfigs = elevatorConfigs;
    m_useCustomPassengers = useCustomPassengers;
    m_passengerConfigs = passengerConfigs;
  }

  /**
   * Constructor for BuildingConfig that loads a set of configuration data from a json file
   * or uses a set of default values if it cannot get the values from the json file
   */
  public BuildingConfig()
  {
    // Disired feature: Pull data from json config file
    
    // else

    loadDefaultBuildingConfig(false, false);
  }

  /**
   * Assign a set of default configuration values
   */
  public void loadDefaultBuildingConfig(boolean useCustomEvents, boolean useCustomPassengers)
  {
    m_useCustomEvents = useCustomEvents;

    m_lowestFloor = 0;
    m_highestFloor = 10;
    m_maxNumPassengersPerFloor = 3;

    m_elevatorConfigs.clear();
    m_elevatorConfigs.add(new ElevatorConfig(m_lowestFloor, m_highestFloor));
    m_elevatorConfigs.add(new ElevatorConfig(m_lowestFloor, m_highestFloor));

    m_useCustomPassengers = useCustomPassengers;
    
    m_passengerConfigs.clear();

    ArrayList<Double> sickFactors = new ArrayList<>( List.of(0.01, 1.1));

    m_passengerConfigs.add( new PassengerConfig(PassengerPriority.get().getLowestPriority(), 
                            m_lowestFloor, m_lowestFloor, m_highestFloor, 4, sickFactors));

    m_passengerConfigs.add (new PassengerConfig(PassengerPriority.get().getLowestPriority(),
                            (m_highestFloor - m_lowestFloor) / 2, m_lowestFloor, 
                             m_highestFloor, 6, sickFactors));
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

  public boolean usingCustomPassengers()
  {
    return m_useCustomPassengers;
  }

  public ArrayList<PassengerConfig> getPassengerConfigs()
  {
    return m_passengerConfigs;
  }
}

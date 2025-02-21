package com.maxruiz.config;

/**
 * This class holds the configuration data used to create an Elevator instance
 * @see Elevator
 */
public class ElevatorConfig 
{
    private int m_ID = 0;
    private int m_startingFloor = 0;
    private int m_sqft = 15;
    private int m_lowestFloor = 0;
    private int m_highestFloor = 1;
    private int m_atFloorFrames = 4;
    private int m_movingFrames = 4;

    private boolean m_hasUpdatedInstanceCounter = false;

    private static int m_elevatorCount = 0;

    /**
     * Constructor for ElevatorConfig that takes in the minimum required data from the building
     * and either extracts data from a json config file or uses a set of default values
     * @param lowestFloor
     * @param highestFloor
     * @see Elevator
     */
    public ElevatorConfig(int lowestFloor, int highestFloor) // , JsonObject elevatorJObj)
    {
      m_lowestFloor = lowestFloor;
      m_highestFloor = highestFloor;
      
      // Desired Feature: Extract data from a json configuration file
      // loadElevatorConfig(JsonObject elevatorJObj)

      loadDefaultElevatorConfig();
    }

    /**
     * Constructor for ElevatorConfig that takes in all of the required parameters used to build
     * an instance of an Elevator
     * @param lowestFloor
     * @param highestFloor
     * @param id
     * @param startingFloor
     * @param sqft
     * @param atFloorFrames
     * @param movingFrames
     * @see Elevator
     */
    public ElevatorConfig(int lowestFloor, int highestFloor, int id, int startingFloor, 
                          int sqft, int atFloorFrames, int movingFrames)
    {
      m_lowestFloor = lowestFloor;
      m_highestFloor = highestFloor;
      m_ID = id;
      m_startingFloor = startingFloor;
      m_sqft = sqft;
      m_atFloorFrames = atFloorFrames;
      m_movingFrames = movingFrames;

      m_elevatorCount++;
      m_hasUpdatedInstanceCounter = true;
    }

    // public void loadElevatorConfig(JsonObject elevatorJObj)
    // {
    //   // load from json object
    //   // i.e. { "ID" : "1234", "starting_floor" : 0, "sqft" : 15, "at_floor_frames" : 4, "moving_frames" : 4}
    // }

    /**
     * Load a set of default configuration values
     */
    public void loadDefaultElevatorConfig()
    {
      if (!m_hasUpdatedInstanceCounter)
      {
        m_ID = m_elevatorCount++;
        m_hasUpdatedInstanceCounter = true;
      }

      m_startingFloor = m_lowestFloor;
      m_sqft = 15;
      m_atFloorFrames = 4;
      m_movingFrames = 4;
    }

    public int getID() {
      return m_ID;
    }

    public void setID(int m_ID) {
      this.m_ID = m_ID;
    }

    public int getStartingFloor() {
      return m_startingFloor;
    }

    public void setStartingFloor(int m_startingFloor) {
      this.m_startingFloor = m_startingFloor;
    }

    public int getSqft() {
      return m_sqft;
    }

    public void setSqft(int m_sqft) {
      this.m_sqft = m_sqft;
    }

    public int getAtFloorFrames() {
      return m_atFloorFrames;
    }

    public void setAtFloorFrames(int m_atFloorFrames) {
      this.m_atFloorFrames = m_atFloorFrames;
    }

    public int getMovingFrames() {
      return m_movingFrames;
    }

    public void setMovingFrames(int m_movingFrames) {
      this.m_movingFrames = m_movingFrames;
    }

    public int getLowestFloor()
    {
      return m_lowestFloor;
    }

    public int getHighestFloor()
    {
      return m_highestFloor;
    }

    public static int getElevatorCount() {
      return m_elevatorCount;
    }

    
}

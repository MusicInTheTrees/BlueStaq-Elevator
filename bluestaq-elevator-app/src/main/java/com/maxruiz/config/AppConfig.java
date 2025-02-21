package com.maxruiz.config;

/**
 * This class is used to extract configuration data from a json file to setup how the application runs
 * or use a set of defaults.
 * @author Max Ruiz
 */
public class AppConfig
{
  private int m_frameRate = 5;
  private int m_frameTimeMillis = 200;
  private boolean m_printToFile = true;
  private String m_printToFileName = "system_out/test_output.txt";

  /**
   * Constuctor for AppConfig which takes in all the parameters to configure the main application
   * @param frameRate
   * @param printToFile
   * @param printToFileName
   */
  public AppConfig(int frameRate, boolean printToFile, String printToFileName)
  {
    m_frameRate = 5;
    m_frameTimeMillis = convertFrameRateToMillis(frameRate);
    m_printToFile = printToFile;
    m_printToFileName = printToFileName;
  }

  /**
   * Constructor for AppConfig which uses default values to configure the main application
   */
  public AppConfig()
  {
    // Desired Feature: Extract Data from json config file

    loadDefaultAppConfig();
  }

  /**
   * Load a default set of values for the main app configuration data
   */
  public void loadDefaultAppConfig()
  {
    m_frameRate = 5;
    m_frameTimeMillis = convertFrameRateToMillis(m_frameRate);
    m_printToFile = true;
    m_printToFileName = "system_out/test_output.txt";
  }

  /**
   * A Macro to convert the provided frame rate into milliseconds used to delay between frames
   * @param frameRate
   * @return int
   */
  public int convertFrameRateToMillis(int frameRate)
  {
    if (frameRate <= 0)
    {
      return 1;
    }

    double m = (1.0 / (double)frameRate) * 100.0;

    return (int)m;
  }

  public int getFrameRate() {
    return m_frameRate;
  }

  public int getFrameTimeMillis() {
    return m_frameTimeMillis;
  }

  public boolean getPrintToFile() {
    return m_printToFile;
  }

  public String getPrintToFileName() {
    return m_printToFileName;
  }

}

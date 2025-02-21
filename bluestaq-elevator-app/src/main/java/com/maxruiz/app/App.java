package com.maxruiz.app;

import java.lang.Runtime;
import java.lang.Thread;
import java.lang.System;
import java.util.concurrent.TimeUnit;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.IOException;

import com.maxruiz.config.AppConfig;
import com.maxruiz.config.BuildingConfig;
import com.maxruiz.structures.Building;

/*
* This application runs an event loop containing
* a building which contains one or more elevators. The building 
* causes events that affect the elevator(s).
* <p>This program will catch an os exit of ctrl+c to exit
* as well as prints to a file <i>test_output/test_output.txt<i></p>
* @author Max Ruiz
*/

public class App
{
    // cc == control + c
    // ......^.........^
    static volatile boolean ccExit = false;

    static File file;
    static FileOutputStream fos;
    static PrintStream ps;

    static AppConfig appConfig = new AppConfig();

    /**
     * @param args command line arguments
     * @throws IllegalArgumentException dumps stack trace. The building requires
     *                                  a specific range of inputs. The application
     *                                  will end if this error is caught.
     * @throws Exception dumps stack trace. Catch all error handler. the application
     *                   will end if this error is caught.
     */
    public static void main(String[] args)
    {
        // Setup console output to go to a text file
        openOutputStream();

        // Create thread to capture ctrl+c and clean up before exiting.
        createExitThread();

        boolean appExit = false;

        Building building = null;

        try 
        {
            building = loadBuilding();
        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
            appExit = true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            appExit = true;
        }

        if (null == building)
        {
            appExit = true;
        }

        while (!appExit)
        {
            // Run the main operation of this event loop
            building.operate();

            // Sleep some amount of time to give concept of real time
            try
            {
                TimeUnit.MILLISECONDS.sleep(App.appConfig.getFrameTimeMillis());
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
                appExit = true;
            }

            appExit = (appExit == true) ? true : ccExit;
        }

        // clean up output stream
        closeOutputStream();
    }


    /**
     * Set parameters for the building to run.
     * @return Building
     */
    private static Building loadBuilding()
    {
        // DESIRED FEATURE: Pull in this data via a json file

        // Set values to generate building
        BuildingConfig bc = new BuildingConfig();

        return new Building(bc);
    }

    /**
     * Create thread to catch os exit ctrl+c and clean up output stream
     */
    private static void createExitThread()
    {
        Thread userExitThread = new Thread(() -> {
            System.out.println("Detected Interrupt - Ctrl+C");
            ccExit = true;
            closeOutputStream();
        });
        Runtime.getRuntime().addShutdownHook(userExitThread);
    }

    /**
     * Try to create an output stream to print console content to a file.
     */
    private static void openOutputStream()
    {
        if (false == App.appConfig.getPrintToFile())
        {
            return;
        }

        try 
        {
            file = new File(App.appConfig.getPrintToFileName());
            fos = new FileOutputStream(file);
            ps = new PrintStream(fos);
            System.setOut(ps);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Try to clean up output stream
     */
    private static void closeOutputStream()
    {
        if (false == App.appConfig.getPrintToFile())
        {
            return;
        }

        try 
        {
            ps.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        try 
        {
            fos.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

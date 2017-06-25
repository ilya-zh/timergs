package com.homedev.timergs.utilities;

import com.homedev.timergs.enums.SettingsKeys;

/**
 * @author Ilya Zhuravliov, Date: 05/02/11 Time: 19:01
 */

public class SettingsManager
{
  public static boolean isAlwaysOnTopSetting()
  {
    final String property = PropertiesManager.getInstance().getProperty( SettingsKeys.ALWAYS_ON_TOP );
    return Fn.isStringPopulated( property ) && property.equalsIgnoreCase( "ON" );
  }

  public static void setAlwaysOnTop( final boolean b )
  {
    PropertiesManager.getInstance().setProperty( SettingsKeys.ALWAYS_ON_TOP, b ? "ON" : "OFF" );
  }

  public static boolean isDebugEnabled()
  {
    final String property = PropertiesManager.getInstance().getProperty( SettingsKeys.DEBUG_ENABLED );
    return Fn.isStringPopulated( property ) && property.equalsIgnoreCase( "true" );
  }

  public static void commit()
  {
    PropertiesManager.getInstance().storeProperties();
  }

  public static void cancel()
  {
    PropertiesManager.getInstance().cancel();
  }

  public static void setLastTimerSetting( final int value )
  {
    final PropertiesManager propertiesManager = PropertiesManager.getInstance();
    propertiesManager.setProperty( SettingsKeys.LAST_TIMER_SETTING, String.valueOf( value ) );
  }

  public static void setLastShutdownTime( final String time )
  {
    final PropertiesManager propertiesManager = PropertiesManager.getInstance();
    propertiesManager.setProperty( SettingsKeys.LAST_SHUTDOWN_TIME, time );
  }

  public static int getLastTimerSetting()
  {
    return Integer.parseInt( PropertiesManager.getInstance().getProperty( SettingsKeys.LAST_TIMER_SETTING ) );
  }

  public static String getLastShutdownTime()
  {
    return PropertiesManager.getInstance().getProperty( SettingsKeys.LAST_SHUTDOWN_TIME );
  }

  public static boolean isHibernate()
  {
    return Boolean.parseBoolean( PropertiesManager.getInstance().getProperty( SettingsKeys.HIBERNATE ) );
  }
}

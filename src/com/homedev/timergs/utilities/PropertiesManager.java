package com.homedev.timergs.utilities;

import com.homedev.timergs.TimerFrame;

import javax.swing.*;
import java.io.*;
import java.util.Properties;

/**
 * @author Ilya Zhuravliov, Date: 08-Mar-2009 Time: 08:54:07
 */
public class PropertiesManager
{
  private final Properties properties = new Properties();
  private String errorCode = null;
  private final static String PROPERTIES_FILE_NAME = "settings.properties";
  private final static String DEFAULT_PROPERTIES_FILE_NAME = "defaultSettings.properties";
  private final File propertiesFile;
  private static PropertiesManager propertiesManager;

  public static PropertiesManager getInstance()
  {
    if ( propertiesManager == null )
    {
      propertiesManager = new PropertiesManager();
    }
    return propertiesManager;
  }

  private PropertiesManager()
  {
    propertiesFile = new File( PROPERTIES_FILE_NAME );
    try
    {
      setupProps();
    }
    catch ( IOException e )
    {
      errorCode = e.getMessage();
    }
  }

  private void setupProps()
    throws IOException
  {
    if ( propertiesFile != null && propertiesFile.exists() )
    {
      loadProperties( propertiesFile );
    }
    else
    {
      loadDefaultProperties( DEFAULT_PROPERTIES_FILE_NAME );
    }
  }

  private void loadDefaultProperties( final String defaultPropertiesFile )
  {
    final InputStream inputStream = TimerFrame.class.getResourceAsStream( defaultPropertiesFile );
    try
    {
      properties.load( inputStream );
    }
    catch ( IOException e )
    {
      e.printStackTrace();
    }
  }

  private void loadProperties( final File propertiesFile )
  {
    FileInputStream in = null;
    try
    {
      in = new FileInputStream( propertiesFile );
      properties.load( in );
    }
    catch ( FileNotFoundException e )
    {
      JOptionPane.showMessageDialog( null, e );
    }
    catch ( IOException e )
    {
      JOptionPane.showMessageDialog( null, e );
    }
    finally
    {
      if ( in != null )
      {
        try
        {
          in.close();
        }
        catch ( IOException e )
        {
          JOptionPane.showMessageDialog( null, e );
        }
      }
    }
  }

  public synchronized void storeProperties()
  {
    FileOutputStream outputStream = null;
    try
    {
//            propertiesFile.createNewFile();
      outputStream = new FileOutputStream( propertiesFile );
      properties.store( outputStream, "*-----TimeGS Settings-----*" );
    }
    catch ( IOException e )
    {
      e.printStackTrace();
    }
    finally
    {
      if ( outputStream != null )
      {
        try
        {
          outputStream.close();
        }
        catch ( IOException e )
        {
          //oh well, at least we tried...
        }
      }
    }
  }

  public synchronized void setProperty( final String key, final String value )
  {
    properties.setProperty( key, value );
  }

  public synchronized String getProperty( final String key )
  {
    return properties.getProperty( key );
  }


  public synchronized String getErrorCode()
  {
    return errorCode;
  }

  public synchronized void cancel()
  {
    try
    {
      setupProps();
    }
    catch ( IOException e )
    {
      JOptionPane.showMessageDialog( null, e );
    }
  }
}

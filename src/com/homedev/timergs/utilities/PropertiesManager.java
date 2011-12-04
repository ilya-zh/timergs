package com.homedev.timergs.utilities;

import com.homedev.timergs.TimerFrame;

import java.io.*;
import java.util.Properties;

/**
 * User: Admin
 * Date: 08-Mar-2009
 * Time: 08:54:07
 */
public class PropertiesManager
{
    private final Properties properties = new Properties();
    private final File PROPERTIES_FILE;
    private final String DEFAULT_PROPERTIES_FILE_NAME;
    private String errorCode = null;

    public PropertiesManager( String propertiesFileName, String defPropsFileName )
    {
        PROPERTIES_FILE = new File(propertiesFileName);
        DEFAULT_PROPERTIES_FILE_NAME = defPropsFileName;
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
        if ( PROPERTIES_FILE != null && PROPERTIES_FILE.exists() )
        {
            loadProperties( PROPERTIES_FILE );
        }
        else
        {
            loadDefaultProperties( DEFAULT_PROPERTIES_FILE_NAME );
        }
    }

    private void loadDefaultProperties( String defaultPropertiesFile )
    {
        InputStream inputStream = TimerFrame.class.getResourceAsStream( defaultPropertiesFile );
        try
        {
            properties.load( inputStream );
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
    }

    private void loadProperties( File propertiesFile ) throws IOException
    {
        FileInputStream in = new FileInputStream( propertiesFile );
        properties.load( in );
        in.close();
    }

    public void storeProperties()
    {
        try
        {
            PROPERTIES_FILE.createNewFile();
            FileOutputStream outputStream = new FileOutputStream( PROPERTIES_FILE );
            properties.store( outputStream, "*-----TimeGS Settings-----*" );
            outputStream.close();
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
    }

    public void setProperty( String key, String value )
    {
        properties.setProperty( key, value );
    }

    public String getProperty( String key )
    {
        return properties.getProperty( key );
    }


    public String getErrorCode()
    {
        return errorCode;
    }
}

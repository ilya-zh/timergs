package com.homedev.timergs.utilities;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author Ilya Zhuravliov Date: 08-Mar-2009 Time: 11:17:21
 */
public class LoggerGS

{
  private File logfile;

  public LoggerGS( final String file )
  {
    logfile = new File( file );
    if ( !logfile.exists() )
    {
      try
      {
        logfile.createNewFile();
        log( "# Logging started: time now is: " + new Date().toString() );
      }
      catch ( IOException e )
      {
        e.printStackTrace();
      }
    }
  }

  public void log( final String msg )
  {
    try
    {
      final BufferedWriter out = new BufferedWriter( new FileWriter( logfile, true ) );
      out.append( msg ).append( "\n" );
      out.close();
    }
    catch ( IOException e )
    {
      e.printStackTrace();
    }

  }

  public ArrayList<String> getLoggedMessages()
  {
    final ArrayList<String> list = new ArrayList<String>();
    try
    {
      final BufferedReader reader = new BufferedReader( new FileReader( logfile ) );
      reader.readLine();
      String s;
      while ( ( s = reader.readLine() ) != null )
      {
        list.add( s );
      }
    }
    catch ( FileNotFoundException e )
    {
      e.printStackTrace();
    }
    catch ( IOException e )
    {
      e.printStackTrace();
    }
    return list;
  }
}

package com.homedev.timergs.utilities.loggers;

/**
 * Reports general usage information
 *
 * @author Ilya Zhuravliov, Date: 20/10/13
 */

public class TrackingLogger extends LoggerGS
{
  public TrackingLogger()
  {
    super( "time.log" );
  }

  public void logInfo( final String msg )
  {
    log( msg );
  }
}

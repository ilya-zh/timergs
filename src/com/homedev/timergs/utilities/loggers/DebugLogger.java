package com.homedev.timergs.utilities.loggers;

import com.homedev.timergs.utilities.SettingsManager;

/**
 * Logger that only records debug messages
 *
 * @author Ilya Zhuravliov, Date: 20/10/13
 */

public class DebugLogger extends LoggerGS
{
  public DebugLogger( final String file )
  {
    super( file );
  }

  public void logDebug( final String msg )
  {
    if ( SettingsManager.isDebugEnabled() )
    {
      log( msg );
    }
  }
}

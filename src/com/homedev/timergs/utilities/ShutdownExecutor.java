package com.homedev.timergs.utilities;

import com.homedev.timergs.utilities.loggers.DebugLogger;

import java.io.IOException;

/**
 * Attempts to hibernates/switch off the computer
 *
 * @author Ilya Zhuravliov, Date: 20/10/13
 */

public class ShutdownExecutor
{
  private DebugLogger mainLogger;
  private final String[] PROCESSES_PREVENTING_HIBERNATION = { "realplay.exe" };
  public static final String WIN_SCREEN_SAVER = "C:\\Windows\\System32\\GPhotos.scr /s";

  public void turnOffComputerAndExit()
  {
    turnOffPC();
    exit();
  }

  private void turnOffPC()
  {
    tryKillingProcessesPreventingShutdown();
    if ( !SettingsManager.isHibernate() || !tryHibernateAndReportResult() )
    {
      tryShutdown();
    }
  }

  private boolean tryHibernateAndReportResult()
  {
    getMainLogger().logDebug( "Trying to hibernate" );
    enableHibernate();
    boolean succeeded = hibernate1();
    if ( !succeeded )
    {
      succeeded = hibernate2();
    }
    return succeeded;
  }

  private boolean enableHibernate()
  {
    final Runtime runtime = Runtime.getRuntime();
    getMainLogger().logDebug( "Trying to enable hibernate..." );
    final Runnable tryHibernate = new Runnable()
    {
      public void run()
      {
        Process enableHibernate = null;//try enabling hibernate
        try
        {
          enableHibernate = runtime.exec( "powercfg -h on" );
          enableHibernate.waitFor();
        }
        catch ( Exception e )
        {
          getMainLogger().logDebug( "Error trying to hibernate: " + e );
        }
        handleExitValue( enableHibernate );
      }
    };
    final boolean ok = executeRunnableAndWaitABit( tryHibernate );
    if ( ok )
    {
      getMainLogger().logDebug( "Hibernate function enabled successfully." );
    }
    return ok;
  }

  private boolean hibernate1()
  {
    getMainLogger().logDebug( "Trying 'shutdown -h'..." );
    final Runnable r = new Runnable()
    {
      public void run()
      {
        Process hibernate = null;
        try
        {
          hibernate = Runtime.getRuntime().exec( "shutdown -h" );
          hibernate.waitFor();
        }
        catch ( Exception e )
        {
          getMainLogger().logDebug( "Error trying to hibernate: " + e );
        }
        handleExitValue( hibernate );
      }
    };
    final boolean ok = executeRunnableAndWaitABit( r );
    if ( ok )
    {
      getMainLogger().logDebug( "'shutdown -h' executed correctly." );
    }
    return ok;
  }

  private boolean hibernate2()
  {
    final Runtime runtime = Runtime.getRuntime();
    getMainLogger().logDebug( "Trying 'rundll32.exe'..." );
    final Runnable r = new Runnable()
    {
      public void run()
      {
        Process rundll = null;
        try
        {
          rundll = runtime.exec( "rundll32.exe PowrProf.dll,SetSuspendState" );//hibernate alternative
          rundll.waitFor();
        }
        catch ( Exception e )
        {
          getMainLogger().logDebug( "Error trying to shut down: " + e );
        }
        handleExitValue( rundll );
      }
    };
    final boolean ok = executeRunnableAndWaitABit( r );
    if ( ok )
    {
      getMainLogger().logDebug( "'rundll32.exe' executed correctly." );
    }
    return ok;
  }

  private boolean tryShutdown()
  {
//    Runtime.getRuntime().exec( "Shutdown -h -t 15 -c \"TimerGS is shutting down this computer\"" );
    getMainLogger().logDebug( "Trying to shutdown" );
    final Runnable r = new Runnable()
    {
      public void run()
      {
        Process shutdown = null;
        try
        {
          shutdown = Runtime.getRuntime().exec( "Shutdown -s" );
          shutdown.waitFor();
        }
        catch ( Exception e )
        {
          getMainLogger().logDebug( "Error running 'Shutdown -s': " + e );
        }
        handleExitValue( shutdown );
      }
    };
    final boolean ok = executeRunnableAndWaitABit( r );
    if ( ok )
    {
      getMainLogger().logDebug( "Shutdown function called successfully." );
    }
    return ok;
  }

  private void handleExitValue( final Process process )
  {
    if ( process != null )
    {
      try
      {
        getMainLogger().logDebug( process + ", exit value=" + process.exitValue() );
      }
      catch ( Exception e )
      {
        getMainLogger().logDebug( e + "" );
        throw new RuntimeException( e );
      }
      if ( process.exitValue() != 0 )
      {
        throw new RuntimeException( "Process execution failed: " + process );
      }
    }
  }

  private void exit()
  {
    getMainLogger().logDebug( "Exiting TimerGS" );
    Runtime.getRuntime().exit( 0 );
    getMainLogger().logDebug( "Runtime.getRuntime().exit( 0 ) called..." );
  }

  private void tryKillingProcessesPreventingShutdown()
  {
    final DebugLogger main = getMainLogger();
    main.logDebug( "trying to kill processes preventing shutdown" );
    for ( final String s : PROCESSES_PREVENTING_HIBERNATION )
    {

      final Runnable runnable = new Runnable()
      {
        public void run()
        {
          try
          {
            main.logDebug( "Killing " + s + "..." );
            final Process exec = Runtime.getRuntime().exec( "tskill " + s );//kill process
            main.logDebug( exec + ", exit value=" + exec.exitValue() );
          }
          catch ( IOException e )
          {
            main.logDebug( "Error killing processes: " + e );
          }
        }
      };
      final boolean ok = executeRunnableAndWaitABit( runnable );
      if ( ok )
      {
        getMainLogger().logDebug( "Successfully tried to kill processes preventing shutdown." );
      }
    }
  }

  private boolean executeRunnableAndWaitABit( final Runnable r )
  {
    final boolean[] ok = { true };
    final Thread.UncaughtExceptionHandler h = new Thread.UncaughtExceptionHandler()
    {
      public void uncaughtException( final Thread th, final Throwable ex )
      {
        ok[0] = false;
        getMainLogger().logDebug( "Exception : " + ex );
        getMainLogger().logDebug( "Thread: " + th );
      }
    };

    try
    {
      final Thread thread = new Thread( r );
      thread.setUncaughtExceptionHandler( h );
      thread.start();
      thread.join( 5000 );
    }
    catch ( Exception e )
    {
      ok[0] = false;
      getMainLogger().logDebug( e + "" );
    }
    return ok[0];
  }

  private DebugLogger getMainLogger()
  {
    if ( mainLogger == null )
    {
      mainLogger = new DebugLogger( "main.log" );
    }
    return mainLogger;
  }
}

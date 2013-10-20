package com.homedev.timergs;

import com.homedev.timergs.sound.SoundManager;
import com.homedev.timergs.utilities.ShutdownExecutor;
import com.homedev.timergs.utilities.loggers.DebugLogger;
import com.homedev.timergs.utilities.SettingsManager;
import com.homedev.timergs.utilities.TimerActionListener;
import com.homedev.timergs.utilities.loggers.TrackingLogger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Date;

/**
 * Main class containing the decision making code User: Ilya Zhuravliov, Date: 04-Mar-2009 Time: 22:02:29
 */

public class TimerLogic
{
  public Timer timer;
  public int seconds = 60;
  protected int timeUntilSwitchOff;//seconds
  private final JLabel displayLbl;
  private final JComboBox timeToWait_cmb;
  private final SoundManager soundManager = new SoundManager();
  private TrackingLogger offLogger;
  private ShutdownExecutor shutdownExecutor = new ShutdownExecutor();
  public TimerLogic( final JLabel displayLbl, final JComboBox timeToWait_cmb )
  {
    this.displayLbl = displayLbl;
    this.timeToWait_cmb = timeToWait_cmb;
  }

  public void init()
  {
    resetTimer();
    setDisplayTimeUntilShutDown();
    loadLastSetting();
    initTimer();
  }

  protected void resetTimer()
  {
    seconds = 60;
    updateTimeDisplay( "00:00" );
  }

  private void saveLastTimerSetting()
  {
    SettingsManager.setLastTimerSetting( getSelectedComboSetting() );
    SettingsManager.commit();
  }

  protected void startTimer()
    throws IOException
  {
    resetTimer();
    setDisplayTimeUntilShutDown();
    saveLastTimerSetting();
    timer.setDelay( 1000 );
    timer.start();
  }

  protected void initTimer()
  {
    timer = new Timer( 0, new ActionListener()
    {
      public void actionPerformed( final ActionEvent e )
      {
        final int secondsToDisplay = timeUntilSwitchOff % 60;
        updateTimeDisplay( timeUntilSwitchOff / 60 + ":"
                             + ( secondsToDisplay < 10 ? "0" + secondsToDisplay : secondsToDisplay ) );

        if ( isTimeToWarnOfImminentShutDown() )
        {
          doFiveBeeps();
        }
        if ( timeUntilSwitchOff == 0 )
        {
          timer.stop();
          saveLastShutdownTime();
          shutdownExecutor.turnOffComputerAndExit();
        }
        timeUntilSwitchOff--;
      }
    } );
  }

  private boolean isTimeToWarnOfImminentShutDown()
  {
    return timeUntilSwitchOff == 60;
  }

  private void saveLastShutdownTime()
  {
    final String time = new Date().toString();
    SettingsManager.setLastShutdownTime( time );
    SettingsManager.commit();
    getOffLogger().logInfo( "Shutting down...the time is now: " + time );

  }

  protected void setDisplayTimeUntilShutDown()
  {
    final int index = getSelectedComboSetting();
    if ( index == 0 )
    {
      timeUntilSwitchOff = 60;
    }
    else if ( index == 1 )
    {
      timeUntilSwitchOff = 300;
    }
    else if ( index == getNumberOfComboSettings() - 1 )
    {
      timeUntilSwitchOff = 120 * 60;
    }
    else

    {
      timeUntilSwitchOff = ( index * 10 - 10 ) * 60;
    }

//        timeUntilSwitchOff *= 60;
    updateTimeDisplay( timeUntilSwitchOff / 60 + ":" + "00" );
  }

  private void updateTimeDisplay( final String s )
  {
    displayLbl.setText( s );
  }

  private void setSelectedComboSetting( final int lastSetting )
  {
    timeToWait_cmb.setSelectedIndex( lastSetting );
  }

  private int getSelectedComboSetting()
  {
    return timeToWait_cmb.getSelectedIndex();
  }

  private int getNumberOfComboSettings()
  {
    return timeToWait_cmb.getItemCount();
  }

  protected void doBeep()
  {
    soundManager.doBeep();
  }

  protected void doFiveBeeps()
  {
    soundManager.doFiveBeeps();
  }

  private TrackingLogger getOffLogger()
  {
    if ( offLogger == null )
    {
      offLogger = new TrackingLogger();
    }
    return offLogger;
  }

  protected void loadLastSetting()
  {
    try
    {
      setSelectedComboSetting( SettingsManager.getLastTimerSetting() );
    }
    catch ( NumberFormatException e )
    {
//            errors.add( e.getMessage() );
    }
  }


  public class TimerActionListenerImpl implements TimerActionListener
  {
    public TimerActionListenerImpl( final Component component )
    {
      if ( component instanceof JLabel )
      {
        ( (JLabel) component ).setText( "" );
      }
    }

    public void timerActionListenerPerformed()
    {

      //To change body of implemented methods use File | Settings | File Templates.
    }

  }
}

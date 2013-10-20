package com.homedev.timergs;

import com.homedev.timergs.utilities.LoggerGS;
import com.homedev.timergs.utilities.SettingsManager;
import com.homedev.timergs.utilities.TimerActionListener;
import com.sun.media.sound.JavaSoundAudioClip;

import javax.swing.*;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Date;

/**
 * User: Ilya Zhuravliov, Date: 04-Mar-2009 Time: 22:02:29
 */

public class TimerLogic
{
  public Timer timer;
  public int seconds = 60;
  protected int timeUntilSwitchOff;
  private JLabel displayLbl;
  private JComboBox timeToWait_cmb;
  private final String[] PROCESSES_PREVENTING_HIBERNATION = { "realplay" };
  public static final String WIN_SCREEN_SAVER = "C:\\Windows\\System32\\GPhotos.scr /s";
  private AudioClip audioClip;

  public LoggerGS getOffLogger()
  {
    return offLogger;
  }

  private LoggerGS offLogger;
//    private ArrayList<String> errors = new ArrayList<String>();


  public TimerLogic( JLabel displayLbl, JComboBox timeToWait_cmb )
  {
    this.displayLbl = displayLbl;
    this.timeToWait_cmb = timeToWait_cmb;
    offLogger = new LoggerGS( "time.log" );
//        final String propsError = propertiesManager.getErrorCode();
//        if ( propsError != null ) errors.add( propsError );


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
    throws IOException
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
      public void actionPerformed( ActionEvent e )
      {
        final int secondsToDisplay = timeUntilSwitchOff % 60;
        updateTimeDisplay( timeUntilSwitchOff / 60 + ":"
                             + ( secondsToDisplay < 10 ? "0" + secondsToDisplay : secondsToDisplay ) );

        if ( isTimeToWarnOfImminentShutDown() )
        {
          doBeep();
        }
        if ( timeUntilSwitchOff == 0 )
        {
          timer.stop();
          saveLastShutdownTime();
          turnOffComputerAndExit();
        }
        timeUntilSwitchOff--;
      }
    } );
  }

  private boolean isTimeToWarnOfImminentShutDown()
  {
    return timeUntilSwitchOff < 61 && timeUntilSwitchOff > 54;
  }

  private void turnOffComputerAndExit()
  {
    try
    {
      turnOffPC();
      exit();
    }
    catch ( IOException e1 )
    {
      e1.printStackTrace();
    }
  }

  private void turnOffPC()
    throws IOException
  {
    tryKillingProcessesPreventingShutdown();
    tryHibernate();
    tryShutdown();
  }

  private void tryHibernate()
    throws IOException
  {
    final Runtime runtime = Runtime.getRuntime();
    runtime.exec( "powercfg -h on" );//try enabling hibernate
    runtime.exec( "rundll32.exe PowrProf.dll,SetSuspendState" );//hibernate
  }

  private void tryShutdown()
    throws IOException
  {
//    Runtime.getRuntime().exec( "Shutdown -h -t 15 -c \"TimerGS is shutting down this computer\"" );
//    Runtime.getRuntime().exec( "shutdown -h" );
    Runtime.getRuntime().exec( "Shutdown -s" );
  }

  private void exit()
  {
    Runtime.getRuntime().exit( 0 );
  }

  private void tryKillingProcessesPreventingShutdown()
    throws IOException
  {
    for ( final String s : PROCESSES_PREVENTING_HIBERNATION )
    {
      System.out.println( "Killing " + s + "..." );
      Runtime.getRuntime().exec( "tskill " + s );//kill process
    }
  }

  private void saveLastShutdownTime()
  {
    final String time = new Date().toString();
    SettingsManager.setLastShutdownTime( time );
    SettingsManager.commit();
    offLogger.log( "Shutting down...the time is now: " + time );

  }

  protected void setDisplayTimeUntilShutDown()
  {
    int index = getSelectedComboSetting();
    if ( index == 0 )
    {
      timeUntilSwitchOff = 1;
    }
    else if ( index == 1 )
    {
      timeUntilSwitchOff = 5 + 60;
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
    updateTimeDisplay( String.valueOf( timeUntilSwitchOff / 60 ) + ":" + "00" );
  }


  private void updateTimeDisplay( String s )
  {
    displayLbl.setText( s );
  }

  private void setSelectedComboSetting( int lastSetting )
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
    if ( audioClip == null )
    {
      activateScreenSaver();
      try
      {
        InputStream stream = getClass().getResourceAsStream( "sounds/ding.wav" );


        audioClip = new JavaSoundAudioClip( stream );
      }
      catch ( IOException e )
      {
        //Failed to load sound file
      }
//            catch ( URISyntaxException e )
//            {
//                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//            }
    }
//        Toolkit.getDefaultToolkit().beep();
    if ( audioClip != null )
    {
      SwingUtilities.invokeLater( new Runnable()
      {
        public void run()
        {
          audioClip.play();
        }
      } );
    }
  }

  private void activateScreenSaver()
  {
    //                String substring = stream2.
//                if ( timeUntilSwitchOff == 60 )
//                {
//                    URL url = getClass().getResource( "screensavers/scrnsave.scr" );
//                    File file = new File( url.toURI() );

//                    String replace = substring.replaceAll( "/", "\\" );
//                    JOptionPane.showMessageDialog( null, stream2 ==null? "null":stream2.toString() );
//                    String fileName = ClassLoader.getSystemResource("/scrnsave.scr").getFile();
//                    Runtime.getRuntime().exec( "rundll32 url.dll,FileProtocolHandler " + fileName );
//                    JOptionPane.showMessageDialog( null, "null" );
//                    File file = new File( "scrnsave.scr" );
//                    Runtime.getRuntime().exec( "", null, file );
//                }

//                JOptionPane.showMessageDialog( null,url.getFile() );
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
    public TimerActionListenerImpl( Component component )
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

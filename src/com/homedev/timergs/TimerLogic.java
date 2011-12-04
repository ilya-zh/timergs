package com.homedev.timergs;

import com.homedev.timergs.utilities.LoggerGS;
import com.homedev.timergs.utilities.PropertiesManager;
import com.homedev.timergs.utilities.TimerActionListener;
import java.util.Date;
import com.sun.media.sound.JavaSoundAudioClip;
import org.omg.CORBA.SystemException;

import javax.swing.*;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

/**
 * User: Admin
 * Date: 04-Mar-2009
 * Time: 22:02:29
 */

public class TimerLogic
{
    public Timer timer;
    public int mins = 60;
    protected int timeUntilSwitchOff;
    private JLabel displayLbl;
    private JComboBox timeToWait_cmb;
    private static final String LAST_TIMER_SETTING = "lastSetting";
    private static final String LAST_SHUTDOWN_TIME = "lastShutdownTime";
    private final String PROPERTIES_FILE_NAME = "settings.properties";
    private final String DEFAULT_PROPERTIES_FILE_NAME = "defaultSettings.properties";
    private final PropertiesManager propertiesManager;
    public static final String WIN_SCREEN_SAVER = "C:\\Windows\\System32\\GPhotos.scr /s";
    private AudioClip audioClip;

    static
    {

    }

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
        propertiesManager = new PropertiesManager( PROPERTIES_FILE_NAME, DEFAULT_PROPERTIES_FILE_NAME );
        offLogger = new LoggerGS( "time.log" );
//        final String propsError = propertiesManager.getErrorCode();
//        if ( propsError != null ) errors.add( propsError );
        final InputStream stream = getClass().getResourceAsStream("sounds/ding.wav");
        try {
            audioClip = new JavaSoundAudioClip(stream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load sound file: " + e );
        }
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
        mins = 60;
        updateTimeDisplay( "00:00" );
    }

    private void saveLastTimerSetting() throws IOException
    {
        final int value = getSelectedComboSetting();
        propertiesManager.setProperty( LAST_TIMER_SETTING, String.valueOf( value ) );
        propertiesManager.storeProperties();

    }

    protected void startTimer() throws IOException
    {

        resetTimer();
        setDisplayTimeUntilShutDown();

        saveLastTimerSetting();


        timer.setDelay( 1000 );
        timer.start();


        BigDecimal bigDecimal = BigDecimal.ONE;
        bigDecimal = bigDecimal.setScale(3);

        BigDecimal bigDecimal2= BigDecimal.ONE;;
        bigDecimal2 = bigDecimal2.setScale(2);

        JOptionPane.showMessageDialog(displayLbl.getParent(),bigDecimal +"AND 2:" + bigDecimal2 );
        JOptionPane.showMessageDialog(displayLbl.getParent(),bigDecimal.compareTo(bigDecimal2) );
        JOptionPane.showMessageDialog(displayLbl.getParent(),bigDecimal.equals(bigDecimal2) );
        System.out.println(bigDecimal.compareTo(bigDecimal2)+"");
        System.out.println(bigDecimal.equals(bigDecimal2)+"");
    }

    protected void initTimer()
    {
        timer = new Timer( 0, new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                timeUntilSwitchOff--;
                String minsString;
                if ( --mins < 10 )
                {
                    minsString = "0" + String.valueOf( mins );
                }
                else
                {
                    minsString = String.valueOf( mins );
                }
                updateTimeDisplay( String.valueOf( timeUntilSwitchOff / 60 ) + ":" + minsString );
                if ( mins == 0 )
                {
                    mins = 60;
                }
                if ( timeUntilSwitchOff < 61 )
                {
                    if ( timeUntilSwitchOff == 60
                            || timeUntilSwitchOff == 30
                            || ( timeUntilSwitchOff < 10 && timeUntilSwitchOff > 6 ) )
                    {
                        doBeep();
                    }
                }
                if ( timeUntilSwitchOff == 0 )
                {
                    timer.stop();
                    saveLastShutdownTime();
                    turnOffComputerAndExit();
                }
            }
        } );
    }

    private void turnOffComputerAndExit()
    {
        try
        {
            Runtime.getRuntime().exec( "Shutdown -s" );
            Runtime.getRuntime().exit( 0 );
        }
        catch ( IOException e1 )
        {
            e1.printStackTrace();
        }
    }

    private void saveLastShutdownTime()
    {
        final String time = new Date().toString();
        propertiesManager.setProperty( LAST_SHUTDOWN_TIME, time );
        propertiesManager.storeProperties();
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
            timeUntilSwitchOff = 5;
        }
        else if ( index == getNumberOfComboSettings() - 1 )
        {
            timeUntilSwitchOff = 120;
        }
        else

            timeUntilSwitchOff = index * 10 - 10;

        timeUntilSwitchOff *= 60;
        updateTimeDisplay( String.valueOf( timeUntilSwitchOff / 60 ) + ":" + "00" );
    }



    private void updateTimeDisplay( String s )
    {
        displayLbl.setText( s );
    }

//    public ArrayList<String> getErrors()
//    {
//        return errors;
//    }

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

    private int getLastTimerSetting()
    {
        return Integer.parseInt( propertiesManager.getProperty( LAST_TIMER_SETTING ) );
    }

    protected void doBeep()
    {
//        Toolkit.getDefaultToolkit().beep();

        audioClip.play();

    }

    protected String getLastShutdownTime()
    {
        return propertiesManager.getProperty( LAST_SHUTDOWN_TIME );
    }

    protected void loadLastSetting()
    {
        try
        {
            setSelectedComboSetting( getLastTimerSetting() );
        }
        catch ( NumberFormatException e )
        {
//            errors.add( e.getMessage() );
        }
    }


    public class TimerActionListenerImpl implements TimerActionListener
    {
        public TimerActionListenerImpl( Component component)
        {
            if(component instanceof JLabel)
            {
                ( ( JLabel )component ).setText("" );
            }
        }

        public void timerActionListenerPerformed()
        {

            //To change body of implemented methods use File | Settings | File Templates.
        }

    }
}

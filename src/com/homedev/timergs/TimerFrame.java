package com.homedev.timergs;

import com.homedev.timergs.utilities.SettingsManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author Ilya Zhuravliov, Date: 04-Mar-2009 Time: 21:18:57
 */

public class TimerFrame extends JFrame
{
  private final JLabel displayLbl = new JLabel( "centre", SwingConstants.CENTER );
  private final JComboBox timeToWait_cmb = new JComboBox();
  TimerLogic logic;
  private static final Map<Integer, Integer> settingToMinutesValueMap = new HashMap<Integer, Integer>();
  private static final String[] ITEMS = new String[]{
    "1 minute", "5 minutes", "10 minutes", "20 minutes", "30 minutes", "40 minutes",
    "50 minutes", "60 minutes", "70 minutes", "80 minutes", "90 minutes", "120 minutes"
  };
  private static final int INCREMENT_TIME_BY = 15 * 60;

  enum ActionType
  {
    SHOW_LAST_SHUTDOWN_TIME, SHOW_LOG_FILE
  }

  public TimerFrame()
  {
    super( "TimerGS" );
    setAlwaysOnTop( SettingsManager.isAlwaysOnTopSetting() );
    initSize();
    setupUI();

    logic = new TimerLogic( displayLbl, timeToWait_cmb );
    logic.init();
//        try
//        {
//            UIManager.setLookAndFeel(
//                    UIManager.getCrossPlatformLookAndFeelClassName() );
//        }
//        catch ( Exception e )
//        {
//            e.printStackTrace();
//        }
    //
  }


  private void setupUI()
  {
    final JButton start_btn = new JButton();
    final JButton stop_btn = new JButton();
    final JButton exit_btn = new JButton();

    initSettingValuesMap();
    addWindowListener( new WindowAdapter()
    {
      public void windowClosing( final WindowEvent e )
      {//Closes virtual machine as well as the application
        System.exit( 0 );
      }
    } );

    displayLbl.setFont( new Font( displayLbl.getName(), Font.BOLD, 60 ) );

    timeToWait_cmb.setModel( new DefaultComboBoxModel( ITEMS ) );
    timeToWait_cmb.addActionListener( new ActionListener()
    {
      public void actionPerformed( final ActionEvent e )
      {
        logic.resetTimer();
        logic.setDisplayTimeUntilShutDown();
      }
    } );

    final JLabel tictacIn = new JLabel( "Shutdown in:" );
    tictacIn.setLabelFor( timeToWait_cmb );
    start_btn.setText( "Start" );
    start_btn.addActionListener( new ActionListener()
    {
      public void actionPerformed( final ActionEvent e )
      {
        if ( e.getModifiers() == 16 )
        {
          try
          {
            logic.startTimer();
          }
          catch ( IOException ex )
          {
            ex.printStackTrace();
          }
        }
      }
    } );

    start_btn.addKeyListener( new KeyAdapter()
    {
      public void keyReleased( final KeyEvent evt )
      {
        final int lastTimerSettingInSeconds = settingToMinutesValueMap.get( SettingsManager.getLastTimerSetting() ) * 60;
        if ( !evt.isActionKey() && logic.timeUntilSwitchOff < lastTimerSettingInSeconds )
        {   //  If a press of a button Over this control is detected a 15 minute period is added to the timer
          //  Used to inform the program "I'm still awake, don't switch off computer"
          final int potentialResult = logic.timeUntilSwitchOff + INCREMENT_TIME_BY;

          //make sure user doesn't increment time delay past the last setting used
          if ( potentialResult < lastTimerSettingInSeconds )
          {
            logic.timeUntilSwitchOff += INCREMENT_TIME_BY;
          }
          else if ( potentialResult > lastTimerSettingInSeconds )
          {
            logic.timeUntilSwitchOff = lastTimerSettingInSeconds;
          }

          logic.doBeep();
        }
      }
    } );

    stop_btn.setText( "Stop" );
    stop_btn.addActionListener( new ActionListener()
    {
      public void actionPerformed( final ActionEvent evt )
      {
        logic.timer.stop();
        logic.setDisplayTimeUntilShutDown();
      }
    } );

    exit_btn.setText( "Exit" );
    exit_btn.addActionListener( new ActionListener()
    {
      public void actionPerformed( final ActionEvent e )
      {
        System.exit( 0 );
      }
    } );
    final Dimension buttonSize = new Dimension( 70, 30 );
    start_btn.setPreferredSize( buttonSize );
    stop_btn.setPreferredSize( buttonSize );
    exit_btn.setPreferredSize( buttonSize );

    //setup panels
    final JPanel selectionPanel = new JPanel();
//        ImageIcon icon = createImageIcon("images/last.png", "a pretty but meaningless splat");
    final JLabel lastSwitchOff = new JLabel( "last", JLabel.CENTER );
    final JLabel allSwitchOffs = new JLabel( "log", JLabel.CENTER );
    setupLabel( lastSwitchOff, ActionType.SHOW_LAST_SHUTDOWN_TIME );
    setupLabel( allSwitchOffs, ActionType.SHOW_LOG_FILE );


    selectionPanel.setBorder( BorderFactory.createEtchedBorder() );
    selectionPanel.add( tictacIn );
    selectionPanel.add( timeToWait_cmb );
    selectionPanel.add( Box.createRigidArea( new Dimension( 10, 0 ) ) );
    selectionPanel.add( lastSwitchOff );
    selectionPanel.add( allSwitchOffs );
    final JPanel displayPanel = new JPanel();
    displayPanel.setLayout( new GridLayout( 1, 1 ) );
    displayPanel.add( displayLbl );

    final JPanel buttonsPanel = new JPanel();
    buttonsPanel.add( start_btn );
    buttonsPanel.add( stop_btn );
    buttonsPanel.add( exit_btn );
    buttonsPanel.add( Box.createRigidArea( new Dimension( 0, 50 ) ) );

    final JMenuBar menuBar = new JMenuBar();
    // Define and add two drop down menu to the menubar
    final JMenu fileMenu = new JMenu( "File" );
    menuBar.add( fileMenu );

    // Create and add simple menu item to one of the drop down menu
    final JMenuItem settings = new JMenuItem( "Settings" );
    settings.addActionListener( new ActionListener()
    {
      public void actionPerformed( final ActionEvent e )
      {
        final SettingsForm settingsForm = new SettingsForm( TimerFrame.this );
        settingsForm.setVisible( true );
      }
    } );

    final JMenuItem exitAction = new JMenuItem( "Exit" );
    exitAction.addActionListener( new ActionListener()
    {
      public void actionPerformed( final ActionEvent e )
      {
        //To change body of implemented methods use File | Settings | File Templates.
      }
    } );

    fileMenu.add( settings );
    fileMenu.add( exitAction );
    setJMenuBar( menuBar );
    final Container contentPane = getContentPane();
    contentPane.add( selectionPanel, BorderLayout.PAGE_START );
    contentPane.add( displayPanel );
    contentPane.add( buttonsPanel, BorderLayout.PAGE_END );

    getRootPane().setDefaultButton( start_btn );
    pack();

  }

  private void initSettingValuesMap()
  {
    for ( int i = 0; i < ITEMS.length; i++ )
    {
      final String item = ITEMS[i];
      settingToMinutesValueMap.put( i, Integer.parseInt( item.substring( 0, item.indexOf( " " ) ) ) );
    }
  }

  private void setupLabel( final JLabel lastSwitchOff, final ActionType actionType )
  {
    lastSwitchOff.setOpaque( true );
    lastSwitchOff.setPreferredSize( new Dimension( 35, 25 ) );
//        lastSwitchOff.setBackground( Color.gray );
    lastSwitchOff.setBorder( BorderFactory.createEtchedBorder() );
    lastSwitchOff.addMouseListener( new MouseAdapter()
    {
      public void mouseClicked( final MouseEvent e )
      {
        getMouseClickedAction( actionType );
      }
    } );
  }

  private void getMouseClickedAction( final ActionType actionType )
  {
    if ( actionType == ActionType.SHOW_LAST_SHUTDOWN_TIME )
    {
      final String message = SettingsManager.getLastShutdownTime() == null ?
                       "Nothing found" : SettingsManager.getLastShutdownTime();
      JOptionPane.showMessageDialog( this, "Last shutdown time: " + message );
    }
    else if ( actionType == ActionType.SHOW_LOG_FILE )
    {
      try
      {
        final File file = new File( "time.log" );
//                Process process = runtime.exec( "C:\\Program Files\\Winamp\\winamp.exe D:\\Firesuite.mp3");
        Runtime.getRuntime().exec( "cmd /c start " + file.getPath() );
      }
      catch ( IOException e )
      {
        e.printStackTrace();
      }
    }
  }

  private void initSize()
  {
    final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    final Dimension size = new Dimension( 313, 268 );
//        Dimension size = new Dimension( 320, 268 );
    if ( size.height > screenSize.height ) size.height = screenSize.height;
    if ( size.width > screenSize.width ) size.width = screenSize.width;
    setSize( size );
    setMinimumSize( size );
    setLocation( ( screenSize.width - size.width ) / 2, ( screenSize.height - size.height ) / 2 );
  }

  public static void main( final String[] args )
  {
    new TimerFrame().setVisible( true );
//                        SettingsForm settingsForm = new SettingsForm( TimerFrame.this );
//                settingsForm.setVisible(true);
  }

  protected ImageIcon createImageIcon( final String path, final String description )
  {
    final java.net.URL imgURL = getClass().getResource( path );
    if ( imgURL != null )
    {
      return new ImageIcon( imgURL, description );
    }
    else
    {
      System.err.println( "Couldn't find file: " + path );
      return null;
    }
  }

}

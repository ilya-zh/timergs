package com.homedev.timergs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

/**
 * User: Admin
 * Date: 04-Mar-2009
 * Time: 21:18:57
 */

public class TimerFrame extends JFrame
{
    private JLabel displayLbl = new JLabel( "centre", SwingConstants.CENTER );
    private JComboBox timeToWait_cmb = new JComboBox();
    TimerLogic logic;

    enum ActionType {SHOW_LAST_SHUTDOWN_TIME, SHOW_LOG_FILE}

    public TimerFrame()
            throws HeadlessException
    {
        super( "TimerGS" );
        setAlwaysOnTop( true );
        initSize();
        setupUI();

        logic = new TimerLogic( displayLbl, timeToWait_cmb );
        logic.init();

        System.arraycopy(new byte[0], 0, new int[9], 0,0);
        try {
            throw new Throwable();
        } catch (Throwable throwable) {
            throwable.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
//        try
//        {
//            UIManager.setLookAndFeel(
//                    UIManager.getCrossPlatformLookAndFeelClassName() );
//        }
//        catch ( Exception e )
//        {
//            e.printStackTrace();
//        }
        setVisible( true );
    }

    private void setupUI()
    {
        JButton start_btn = new JButton();
        JButton stop_btn = new JButton();
        JButton exit_btn = new JButton();
        String[] items = {"1 minute", "5 minutes", "10 minutes", "20 minutes", "30 minutes", "40 minutes",
                          "50 minutes", "60 minutes", "70 minutes", "80 minutes", "90 minutes", "120 minutes"};

        addWindowListener( new WindowAdapter()
        {
            public void windowClosing( WindowEvent e )
            {//Closes virtual machine as well as the application
                System.exit( 0 );
            }
        } );

        displayLbl.setFont( new Font( Font.SANS_SERIF, Font.BOLD, 60 ) );

        timeToWait_cmb.setModel( new DefaultComboBoxModel( items ) );
        timeToWait_cmb.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {

                logic.resetTimer();
                logic.setDisplayTimeUntilShutDown();
            }
        } );

        JLabel tictacIn = new JLabel( "Shutdown in:" );
        tictacIn.setLabelFor( timeToWait_cmb );
        start_btn.setText( "Start" );
        start_btn.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                if ( e.getModifiers() == 16 )
                {
                    try
                    {
                        logic.startTimer();
//                        System.out.println( "getSize() = " + getSize() );
                    }
                    catch ( IOException ex )
                    {
                        ex.printStackTrace();
                    }
                }
            }
        } );

//        start_btn.addKeyListener( new KeyAdapter()
//        {
//            public void keyReleased( KeyEvent evt )
//            {
//                if ( !evt.isActionKey() )
//                {   //If a press of a button Over this control is detected a 15 minute period is added to the timer
//                    Used to inform the program "I'm still awake, don't switch off computer"
//                    logic.timeUntilSwitchOff += 15 * 60;
//                    logic.doBeep();
//                }
//            }
//        } );

        stop_btn.setText( "Stop" );
        stop_btn.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent evt )
            {
                logic.timer.stop();
                logic.setDisplayTimeUntilShutDown();
            }
        } );

        exit_btn.setText( "Exit" );
        exit_btn.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                System.exit( 0 );
            }
        } );
        Dimension buttonSize = new Dimension( 70, 30 );
        start_btn.setPreferredSize( buttonSize );
        stop_btn.setPreferredSize( buttonSize );
        exit_btn.setPreferredSize( buttonSize );

        //setup panels
        JPanel selectionPanel = new JPanel();
//        ImageIcon icon = createImageIcon("images/last.png", "a pretty but meaningless splat");
        JLabel lastSwitchOff = new JLabel( "last", JLabel.CENTER );
        JLabel allSwitchOffs = new JLabel( "log", JLabel.CENTER );
        setupLabel( lastSwitchOff, ActionType.SHOW_LAST_SHUTDOWN_TIME );
        setupLabel( allSwitchOffs, ActionType.SHOW_LOG_FILE );


        selectionPanel.setBorder( BorderFactory.createEtchedBorder() );
        selectionPanel.add( tictacIn );
        selectionPanel.add( timeToWait_cmb );
        selectionPanel.add( Box.createRigidArea( new Dimension( 10, 0 ) ) );
//        selectionPanel.add( lastSwitchOff );
        selectionPanel.add( allSwitchOffs );

        JPanel displayPanel = new JPanel();
        displayPanel.setLayout( new GridLayout( 1, 1 ) );
        displayPanel.add( displayLbl );

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add( start_btn );
        buttonsPanel.add( stop_btn );
        buttonsPanel.add( exit_btn );
        buttonsPanel.add( Box.createRigidArea( new Dimension( 0, 50 ) ) );

        getContentPane().add( selectionPanel, BorderLayout.PAGE_START );
        getContentPane().add( displayPanel );
        getContentPane().add( buttonsPanel, BorderLayout.PAGE_END );

        getRootPane().setDefaultButton( start_btn );
    }

    private void setupLabel( JLabel lastSwitchOff, final ActionType actionType )
    {
        lastSwitchOff.setOpaque( true );
        lastSwitchOff.setPreferredSize( new Dimension( 50, 25 ) );
//        lastSwitchOff.setBackground( Color.gray );
        lastSwitchOff.setBorder( BorderFactory.createEtchedBorder() );
        lastSwitchOff.addMouseListener( new MouseListener()
        {
            public void mouseClicked( MouseEvent e )
            {
                getMouseClickedAction( actionType );
            }

            public void mousePressed( MouseEvent e ){}
            public void mouseReleased( MouseEvent e ){}
            public void mouseEntered( MouseEvent e ){}
            public void mouseExited( MouseEvent e ){}
        } );
    }

    private void getMouseClickedAction( ActionType actionType )
    {
        if ( actionType == ActionType.SHOW_LAST_SHUTDOWN_TIME )
        {
            String message = logic.getLastShutdownTime() == null ?
                    "Nothing found" : logic.getLastShutdownTime();
            JOptionPane.showMessageDialog( this, "Last shutdown time: " + message );
        }
        else if ( actionType == ActionType.SHOW_LOG_FILE )
        {
            try
            {
                File file = new File( "time.log" );
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
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension size = new Dimension( 313, 268 );
//        Dimension size = new Dimension( 320, 268 );
        if ( size.height > screenSize.height ) size.height = screenSize.height;
        if ( size.width > screenSize.width ) size.width = screenSize.width;
        setSize( size );
        setMinimumSize( size );
        setLocation( ( screenSize.width - size.width ) / 2, ( screenSize.height - size.height ) / 2 );
    }

    public static void main( String[] args )
    {
        new TimerFrame();
    }

    protected ImageIcon createImageIcon( String path,
                                         String description )
    {
        java.net.URL imgURL = getClass().getResource( path );
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

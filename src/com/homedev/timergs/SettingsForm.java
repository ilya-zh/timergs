package com.homedev.timergs;

import com.homedev.timergs.utilities.Fn;
import com.homedev.timergs.utilities.SettingsManager;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author Ilya Zhuravliov, Date: 04-Apr-2010 Time: 18:04:04
 */
public class SettingsForm extends JFrame
{
  private static final Insets INSETS = new Insets( 5, 5, 5, 5 );
  private TimerFrame timerFrame;

  public SettingsForm( TimerFrame timerFrame )
  {
    super( "Settings" );
    setAlwaysOnTop( true );
    setDefaultCloseOperation( WindowConstants.DO_NOTHING_ON_CLOSE );
    setLayout( new GridBagLayout() );
    setupUI();
    this.timerFrame = timerFrame;
  }

  private void setupUI()
  {
    JPanel mainPane = new JPanel( new GridBagLayout() );
    mainPane.setBorder( BorderFactory.createEmptyBorder() );

    mainPane.add( createGenralOptionsPane(), getDefaultLayout( 0 ) );
    mainPane.add( createPathToScreenSaverPane(), getDefaultLayout( 1 ) );

    GridBagConstraints c = getMainSettingsPanelLayout();
    add( mainPane, c );
    c.gridx = 0;
    c.gridy = GridBagConstraints.RELATIVE;
    c.weighty = 1;
    add( Box.createVerticalGlue(), c );

    JPanel buttonGroup = getButtonGroup();
    add( buttonGroup, getDefaultLayout( GridBagConstraints.RELATIVE ) );

    initSize();
//        pack();//or specify size else text field will show as a line!
  }

  private GridBagConstraints getMainSettingsPanelLayout()
  {
    GridBagConstraints c = new GridBagConstraints();
    c.anchor = GridBagConstraints.FIRST_LINE_START;
    c.weightx = 0.1;
    c.weighty = 0;
    c.fill = GridBagConstraints.HORIZONTAL;
    return c;
  }

  private JPanel createGenralOptionsPane()
  {
    JPanel ganeral = new JPanel( new GridBagLayout() );

    //construct checkbox
    JLabel alwOnTopLbl = new JLabel( "Always on top: " );

    JCheckBox alwOnTop = new JCheckBox();
    alwOnTop.addActionListener( new AlwayOnTopAction( alwOnTop ) );
    alwOnTop.setSelected( SettingsManager.isAlwaysOnTopSetting() );
    alwOnTopLbl.setLabelFor( alwOnTop );

    GridBagConstraints c = new GridBagConstraints();
    c.anchor = GridBagConstraints.FIRST_LINE_START;
    c.weightx = 0;
    c.insets = new Insets( 5, 5, 5, 0 );
    ganeral.add( alwOnTopLbl, c );
    c.weightx = 1;
    ganeral.add( alwOnTop, c );
    String title = "General";
    ganeral.setName( title );

    TitledBorder titledBorder = getTitledBorder( title );

    ganeral.setBorder( titledBorder );
    return ganeral;  //To change body of created methods use File | Settings | File Templates.
  }

  private JPanel createPathToScreenSaverPane()
  {
    JPanel pane = new JPanel( new GridBagLayout() );
    JTextField pathToScreensaver = new JTextField( "", 20 );

    //configure label
    JLabel pathToSLable = new JLabel( "Path to screensaver" + ": " );
    pathToSLable.setLabelFor( pathToScreensaver );
    GridBagConstraints c = new GridBagConstraints();
    c.gridy = 0;
    c.gridx = 0;
    c.insets = INSETS;
    c.weightx = 0.0;
    c.anchor = GridBagConstraints.FIRST_LINE_START;
    pane.add( pathToSLable, c );

    //layout textfield
    pathToScreensaver.setText( "Test" );
    c = new GridBagConstraints();
    c.gridx = 1;
    c.insets = INSETS;
    c.weightx = 0.5;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.anchor = GridBagConstraints.FIRST_LINE_START;

    pane.add( pathToScreensaver, c );

    //add button
    JButton button = new JButton();
    button.setText( "Choose" );
    Fn.defaultButtonSize( button );
    c = new GridBagConstraints();
    c.gridx = 2;
    c.anchor = GridBagConstraints.FIRST_LINE_START;
    c.insets = new Insets( 5, 0, 0, 5 );
    pane.add( button, c );

    //Create a file chooser
//        final JFileChooser fc = new JFileChooser();
    pane.setBorder( getTitledBorder( "Screensaver" ) );

    return pane;
  }


  private JPanel getButtonGroup()
  {
    JPanel buttonGroup = new JPanel( new GridBagLayout() );

    JButton cancel = new JButton( "Cancel" );
    JButton saveChanges = new JButton( "Save changes" );
    Fn.defaultButtonSize( cancel );
    Fn.defaultButtonSize( saveChanges );
    saveChanges.addActionListener( new SaveAction() );
    cancel.addActionListener( new CancelAction() );
    GridBagConstraints c = new GridBagConstraints();
    c.anchor = GridBagConstraints.LAST_LINE_END;
    c.fill = GridBagConstraints.NONE;
    c.gridy = 0;
    c.gridx = GridBagConstraints.RELATIVE;
    c.insets = INSETS;
    c.weightx = 1;
    c.weighty = 1;
    buttonGroup.add( saveChanges, c );
    c.weightx = 0;
    c.weighty = 0;
    buttonGroup.add( cancel, c );
    return buttonGroup;
  }

  private GridBagConstraints getDefaultLayout( int currentRow )
  {
    GridBagConstraints c = new GridBagConstraints();
    c.anchor = GridBagConstraints.FIRST_LINE_START;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.weightx = 0.1;
    c.weighty = 0;
    c.gridheight = 1;
    c.gridwidth = 1;
    c.gridx = 0;
    c.gridy = currentRow;
    c.insets = new Insets( 4, 3, 0, 3 );
    c.ipady = 20;
    return c;
  }

  private void initSize()
  {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension appSize = getDefaultSize();
    if ( appSize.height > screenSize.height ) appSize.height = screenSize.height;
    if ( appSize.width > screenSize.width ) appSize.width = screenSize.width;
    setSize( appSize );
    setMinimumSize( appSize );
    setLocationRelativeTo( null );
    centreOnScreen( screenSize, appSize );
  }

  private Dimension getDefaultSize()
  {
    return new Dimension( 500, 300 );
  }

  private void centreOnScreen( Dimension screenSize, Dimension appSize )
  {
    setLocation( ( screenSize.width - appSize.width ) / 2, ( screenSize.height - appSize.height ) / 2 );
  }

  private TitledBorder getTitledBorder( String title )
  {
    return BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder(), title );
  }

  private class AlwayOnTopAction extends AbstractAction
  {
    private JCheckBox alwOnTop;

    public AlwayOnTopAction( JCheckBox alwOnTop )
    {
      this.alwOnTop = alwOnTop;
    }

    public void actionPerformed( ActionEvent e )
    {
      SettingsManager.setAlwaysOnTop( alwOnTop.isSelected() );
    }
  }

  private class SaveAction extends AbstractAction
  {
    public void actionPerformed( ActionEvent e )
    {
      timerFrame.setAlwaysOnTop( SettingsManager.isAlwaysOnTopSetting() );
      SettingsManager.commit();
      SettingsForm.this.dispose();
    }
  }

  private class CancelAction extends AbstractAction
  {
    public void actionPerformed( ActionEvent e )
    {
      SettingsManager.cancel();
      SettingsForm.this.dispose();
    }
  }
}

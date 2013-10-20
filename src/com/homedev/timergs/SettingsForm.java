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
  private final TimerFrame timerFrame;

  public SettingsForm( final TimerFrame timerFrame )
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
    final JPanel mainPane = new JPanel( new GridBagLayout() );
    mainPane.setBorder( BorderFactory.createEmptyBorder() );

    mainPane.add( createGenralOptionsPane(), getDefaultLayout( 0 ) );
    mainPane.add( createPathToScreenSaverPane(), getDefaultLayout( 1 ) );

    final GridBagConstraints c = getMainSettingsPanelLayout();
    add( mainPane, c );
    c.gridx = 0;
    c.gridy = GridBagConstraints.RELATIVE;
    c.weighty = 1;
    add( Box.createVerticalGlue(), c );

    final JPanel buttonGroup = getButtonGroup();
    add( buttonGroup, getDefaultLayout( GridBagConstraints.RELATIVE ) );

    initSize();
//        pack();//or specify size else text field will show as a line!
  }

  private GridBagConstraints getMainSettingsPanelLayout()
  {
    final GridBagConstraints c = new GridBagConstraints();
    c.anchor = GridBagConstraints.FIRST_LINE_START;
    c.weightx = 0.1;
    c.weighty = 0;
    c.fill = GridBagConstraints.HORIZONTAL;
    return c;
  }

  private JPanel createGenralOptionsPane()
  {
    final JPanel ganeral = new JPanel( new GridBagLayout() );

    //construct checkbox
    final JLabel alwOnTopLbl = new JLabel( "Always on top: " );

    final JCheckBox alwOnTop = new JCheckBox();
    alwOnTop.addActionListener( new AlwaysOnTopAction( alwOnTop ) );
    alwOnTop.setSelected( SettingsManager.isAlwaysOnTopSetting() );
    alwOnTopLbl.setLabelFor( alwOnTop );

    final GridBagConstraints c = new GridBagConstraints();
    c.anchor = GridBagConstraints.FIRST_LINE_START;
    c.weightx = 0;
    c.insets = new Insets( 5, 5, 5, 0 );
    ganeral.add( alwOnTopLbl, c );
    c.weightx = 1;
    ganeral.add( alwOnTop, c );
    final String title = "General";
    ganeral.setName( title );

    final TitledBorder titledBorder = getTitledBorder( title );

    ganeral.setBorder( titledBorder );
    return ganeral;  //To change body of created methods use File | Settings | File Templates.
  }

  private JPanel createPathToScreenSaverPane()
  {
    final JPanel pane = new JPanel( new GridBagLayout() );
    final JTextField pathToScreensaver = new JTextField( "", 20 );

    //configure label
    final JLabel pathToSLable = new JLabel( "Path to screensaver" + ": " );
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
    final JButton button = new JButton();
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
    final JPanel buttonGroup = new JPanel( new GridBagLayout() );

    final JButton cancel = new JButton( "Cancel" );
    final JButton saveChanges = new JButton( "Save changes" );
    Fn.defaultButtonSize( cancel );
    Fn.defaultButtonSize( saveChanges );
    saveChanges.addActionListener( new SaveAction() );
    cancel.addActionListener( new CancelAction() );
    final GridBagConstraints c = new GridBagConstraints();
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

  private GridBagConstraints getDefaultLayout( final int currentRow )
  {
    final GridBagConstraints c = new GridBagConstraints();
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
    final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    final Dimension appSize = getDefaultSize();
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

  private void centreOnScreen( final Dimension screenSize, final Dimension appSize )
  {
    setLocation( ( screenSize.width - appSize.width ) / 2, ( screenSize.height - appSize.height ) / 2 );
  }

  private TitledBorder getTitledBorder( final String title )
  {
    return BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder(), title );
  }

  private class AlwaysOnTopAction extends AbstractAction
  {
    private final JCheckBox alwOnTop;

    private AlwaysOnTopAction( final JCheckBox alwOnTop )
    {
      this.alwOnTop = alwOnTop;
    }

    public void actionPerformed( final ActionEvent e )
    {
      SettingsManager.setAlwaysOnTop( alwOnTop.isSelected() );
    }
  }

  private class SaveAction extends AbstractAction
  {
    public void actionPerformed( final ActionEvent e )
    {
      timerFrame.setAlwaysOnTop( SettingsManager.isAlwaysOnTopSetting() );
      SettingsManager.commit();
      dispose();
    }
  }

  private class CancelAction extends AbstractAction
  {
    public void actionPerformed( final ActionEvent e )
    {
      SettingsManager.cancel();
      dispose();
    }
  }
}

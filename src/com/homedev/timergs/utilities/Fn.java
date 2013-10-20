package com.homedev.timergs.utilities;

import javax.swing.*;
import java.awt.*;

/**
 * @author Ilya Zhuravliov, Date: 05/02/11 Time: 18:46
 */
public class Fn
{
  public static boolean isStringPopulated( final String s )
  {
    return s != null && s.length() > 0;
  }

  public static void defaultButtonSize( JButton button )
  {
    button.setPreferredSize( new Dimension( button.getPreferredSize().width, button.getPreferredSize().height - 7 ) );
    button.setMinimumSize( new Dimension( button.getPreferredSize().width, button.getPreferredSize().height ) );
  }

}

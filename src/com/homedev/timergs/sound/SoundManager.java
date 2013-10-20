package com.homedev.timergs.sound;

import com.homedev.timergs.utilities.loggers.DebugLogger;
import com.homedev.timergs.utilities.loggers.LoggerGS;
import com.homedev.timergs.utilities.SettingsManager;
import com.sun.media.sound.JavaSoundAudioClip;

import java.applet.AudioClip;
import java.io.IOException;
import java.io.InputStream;

/**
 * Simple sound manager
 *
 * @author Ilya Zhuravliov, Date: 20/10/13
 */

public class SoundManager
{
  private static final String DING_WAV = "ding.wav";
  private AudioClip warningSound = null;
  private DebugLogger soundLogger;

  public SoundManager()
  {
    initWarningSound();
  }

  private DebugLogger getSoundLogger()
  {
    if ( soundLogger == null )
    {
      soundLogger = new DebugLogger( "soundsDebug.log" );
    }
    return soundLogger;
  }

  public void doFiveBeeps()
  {
    logDebug( "doing 5 beeps" );
    if ( warningSound != null )
    {
      final Thread sound = new Thread( new Runnable()
      {
        public void run()
        {
          final long millis = 1200;
          for ( int i = 0; i < 5; i++ )
          {
            logDebug( "doing beep " + i );
            warningSound.play();
            logDebug( "sleeping for " + millis / 1000 + " seconds..." );
            sleepToAllowAudioToFinishPlaying( millis );
          }
        }

        private void sleepToAllowAudioToFinishPlaying( final long millis )
        {
          try
          {
            Thread.sleep( millis );
          }
          catch ( InterruptedException e )
          {
            e.printStackTrace();
          }
        }
      } );
      sound.start();
    }
  }

  private void logDebug( final String s )
  {
    if ( SettingsManager.isDebugEnabled() )
    {
      getSoundLogger().logDebug( s );
    }
  }

  public void doBeep()
  {
//        Toolkit.getDefaultToolkit().beep();
    if ( warningSound != null )
    {
      final Thread sound = new Thread( new Runnable()
      {
        public void run()
        {
          warningSound.play();
        }
      } );
      sound.start();
    }
  }

  private void initWarningSound()
  {
    if ( warningSound == null )
    {
      try
      {
        logDebug( "trying to load warning sound " + DING_WAV + "  ..." );
        final InputStream stream = getClass().getResourceAsStream( DING_WAV );
        logDebug( "Stream: " + stream );
        warningSound = new JavaSoundAudioClip( stream );
        logDebug( "WarningSound: " + warningSound );
      }
      catch ( IOException e )
      {
        //Failed to load sound file
      }
    }
  }
}

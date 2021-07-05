package com.lasertrac.settings;


import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Control;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Port;

public class ControlTest {

     public static void main(String[] args) throws LineUnavailableException, InterruptedException {
          Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
          for (int i = 0; i < mixerInfos.length; i++) {
               System.out.println("MIXER:\t" + mixerInfos);

               Mixer mixer = AudioSystem.getMixer(mixerInfos[i]);

               Control[] controls = mixer.getControls();

               for (int j = 0; j < controls.length; j++) {

                    System.out.println("\tmixer control:\t" + controls[j]);

               }



               try {

                    mixer.getLine(Port.Info.LINE_OUT);

                    System.out.println("got line_out");

               } catch (Exception e) {

               }



               Line[] sourceLines = mixer.getSourceLines();

               for (int j = 0; j < sourceLines.length; j++) {

                    System.out.println("\tsource line:\t" + sourceLines[j]);

                    Control[] lineControls = sourceLines[j].getControls();

                    for (int k = 0; k < lineControls.length; k++) {

                         System.out.println("\t\tline control:\t" + lineControls[k]);

                    }

               }

               Line[] targetLines = mixer.getTargetLines();

               for (int j = 0; j < targetLines.length; j++) {

                    System.out.println("\ttarget line:\t" + sourceLines[j]);

                    Control[] lineControls = targetLines[j].getControls();

                    for (int k = 0; k < lineControls.length; k++) {

                         System.out.println("\t\tline control:\t" + lineControls[k]);

                    }

               }



               // Line.Info[] sourceLineInfos = mixer.getSourceLineInfo();

               // for (int j = 0; j < sourceLineInfos.length; j++) {

               // System.out.println("\tsource line info:\t" + sourceLineInfos[j]);

               // setVolume(sourceLineInfos[j]);

               // }

               Line.Info[] targetLineInfos = mixer.getTargetLineInfo();

               for (int j = 0; j < targetLineInfos.length; j++) {

                    System.out.println("\ttarget line info:\t" + targetLineInfos[j]);

                    setVolume(targetLineInfos[j]);

               }

          }

     }



     static boolean setVolume(Line.Info lineInfo) {

          try {

               Line line = AudioSystem.getLine(lineInfo);

               line.open();

               System.out.println("\t*** line opened");

               FloatControl control = (FloatControl) line.getControl(FloatControl.Type.VOLUME);

               System.out.println("\t*** got volume control");

               System.out.println("\t\t\t*** old value = " + control.getValue());

               // set the volume to half its maximal value

               control.setValue(control.getMinimum() + (control.getMaximum() - control.getMinimum()) / 2f);

               System.out.println("\t\t\t*** new value = " + control.getValue());

               line.close();

          } catch (Exception e) {

               return false;

          }

          return true;

     }

}


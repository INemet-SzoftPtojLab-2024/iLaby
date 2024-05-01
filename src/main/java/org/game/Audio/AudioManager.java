package main.java.org.game.Audio;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A class full of static functions to manage audio<br>
 * Currently it supports WAV and OGG (Vorbis for sure, but I'm not convinced about FLAC)
 */
public final class AudioManager {
    /** helper variable to generate ids */
    private static int currentSoundId=69420;

    /** a hashmap for the pre-loaded sounds */
    private static HashMap<String, SoundPreloaded> preloadedSounds=new HashMap<>();
    /** a list for the currently active audioclips */
    private static ArrayList<SoundInternal> activeSounds=new ArrayList<>();

    /**
     * plays a sound with the specified path <br>
     * if the sound has been pre-loaded, no import occurs (to pre-load audio, use the preloadSound function) <br>
     * if something isn't quite right, <b>null</b> will be returned
     * @param path the path of the sound
     * @return a Sound object that is unique to every played sound. If a sound with the same path is played multiple times, a new Sound object will be generated every time.
     */
    public static Sound playSound(String path)
    {
        File file=new File(path);
        if(!file.exists())
        {
            System.err.println("The file "+path+" does not exist");
            return null;
        }
        if(!file.canRead())
        {
            System.err.println("The file "+path+" cannot be read");
            return null;
        }

        int id=currentSoundId++;
        final Sound sond=new Sound(id);
        Clip clip=null;
        AudioInputStream aids=null, aids2=null;
        try{
            if(preloadedSounds.containsKey(file.getAbsolutePath()))//get preloaded data
            {
                SoundPreloaded si=preloadedSounds.get(file.getAbsolutePath());
                clip = (Clip) AudioSystem.getLine(si.info);
                clip.open(si.format,si.data,0,si.data.length);
            }
            else //get audio line
            {
                //https://github.com/Trilarion/java-vorbis-support
                aids2= AudioSystem.getAudioInputStream(file);
                if(aids2==null)
                {
                    System.err.println("Something went wong while playing the file "+path);
                    return null;
                }

                AudioFormat format = aids2.getFormat();
                format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(), 16, format.getChannels(), format.getChannels() * 2, format.getSampleRate(), false);

                aids=AudioSystem.getAudioInputStream(format, aids2);
                if(aids==null)
                {
                    System.err.println("Something went wong while playing the file "+path);
                    return null;
                }

                clip=AudioSystem.getClip();
                clip.open(aids);
            }
        }
        catch(IOException | LineUnavailableException ex)
        {
            System.err.println("Something went wong while playing the file "+path);
            return null;
        }
        catch(UnsupportedAudioFileException ex)
        {
            System.err.println("The format of "+path+" is not supported");
            return null;
        }
        finally
        {
            try{
                if(aids!=null)
                    aids.close();
                if(aids2!=null)
                    aids2.close();
            }
            catch(Exception ex){}
        }

        //add listener that closes the line after the playback stopped
        clip.addLineListener(event -> {
            if (event.getType() == LineEvent.Type.STOP) {
                AudioManager.closeSound(sond);
            }
        });

        activeSounds.add(new SoundInternal(clip,id));//register the clip as an active sound
        AudioManager.setVolume(sond,1f);//normalize volume

        clip.start();

        return sond;
    }

    /**
     * stops the playing of a sound
     * @param sound the sound that has to be stopped
     */
    public static void closeSound(Sound sound)
    {
        if(sound==null)
            return;

        int index=getActiveSoundIndex_internal(sound);

        if(index==-1)
            return;

        activeSounds.get(index).clip.stop();
        activeSounds.get(index).clip.close();
        activeSounds.remove(index);
    }

    /**
     * stops all of the active sounds
     */
    public static void closeAllSounds()
    {
        /*for(SoundInternal si : activeSounds)
        {
            si.clip.stop();
            si.clip.close();
        }*/
        for (int i = 0; i < activeSounds.size(); i++) {
            activeSounds.get(i).clip.stop();
            activeSounds.get(i).clip.close();
        }

        activeSounds.clear();
    }

    /**
     * checks whether a sound is currently playing or not
     * @param sound a sound
     * @return true, if the sound is playing
     */
    public static boolean isPlaying(Sound sound)
    {
        if(sound==null)
            return false;
        for(int i=0;i<activeSounds.size();i++)
            if(activeSounds.get(i).id==sound.id)
                return activeSounds.get(i).clip.isActive();
        return false;
    }

    /**
     * returns the volume of the sound on the interval of [0;1]
     * @param sound a Sound object
     * @return the volume of the sound
     */
    public static float getSoundVolume(Sound sound) {
        int index=getActiveSoundIndex_internal(sound);
        if(index==-1)
            return 0;

        FloatControl gainControl = (FloatControl) activeSounds.get(index).clip.getControl(FloatControl.Type.MASTER_GAIN);
        return (float) Math.pow(10f, gainControl.getValue() / 20f);
    }

    /**
     * Sets the volume of the sound
     * @param sound the sound that needs to be adjusted
     * @param volume the volume of the sound on the interval of [0;1]
     */
    public static void setVolume(Sound sound, float volume) {
        if (volume < 0f )
            volume=0;
        else if(volume>1.0f)
            volume=1.0f;

        int index=getActiveSoundIndex_internal(sound);
        if(index==-1)
            return;

        FloatControl gainControl = (FloatControl) activeSounds.get(index).clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(20f * (float) Math.log10(volume));
    }

    /**
     * pre-loads a sound effect <br>
     * if the sound effect is pre-loaded, it doesn't have to be loaded on every playback, thus reducing latency <br>
     * once the pre-loaded sound effects aren't needed anymore, they can be unloaded with the function <b>unloadPreloadedSounds</b>
     * @param path the path of the sound to load
     */
    public static void preloadSound(String path)
    {
        File file=new File(path);
        if(!file.exists())
        {
            System.err.println("The file "+path+" does not exist");
            return;
        }
        if(!file.canRead())
        {
            System.err.println("The file "+path+" cannot be read");
            return;
        }
        if(preloadedSounds.containsKey(file.getAbsolutePath()))
            return;

        SoundPreloaded sp=new SoundPreloaded();
        AudioInputStream aids=null, aids2=null;
        try
        {
            //https://github.com/Trilarion/java-vorbis-support
            aids = AudioSystem.getAudioInputStream(file);
            if(aids==null)
            {
                System.err.println("Something went wong while pre-loading the file "+path);
                return;
            }

            AudioFormat format = aids.getFormat();
            format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(), 16, format.getChannels(), format.getChannels() * 2, format.getSampleRate(), false);

            aids2=AudioSystem.getAudioInputStream(format, aids);
            if(aids2==null)
            {
                System.err.println("Something went wong while pre-loading the file "+path);
                return;
            }

            sp.data = aids2.readAllBytes();
            sp.format=aids2.getFormat();
            sp.info=new DataLine.Info(Clip.class,sp.format);
        }
        catch(IOException ex)
        {
            System.err.println("Something went wong while pre-loading the file "+path);
            return;
        }
        catch(UnsupportedAudioFileException ex)
        {
            System.err.println("The format of "+path+" is not supported");
            return;
        }
        finally
        {
            try{
                if(aids!=null)
                    aids.close();
                if(aids2!=null)
                    aids2.close();
            }
            catch(Exception ex){}
        }

        preloadedSounds.put(file.getAbsolutePath(), sp);
    }

    /**
     * unloads the data that has been pre-loaded with <b>preloadSound</b>
     */
    public static void unloadPreloadedSounds()
    {
        AudioManager.closeAllSounds();
        preloadedSounds.clear();
    }

    /** internal helper function */
    private static int getActiveSoundIndex_internal(Sound sound)
    {
        if(sound==null)
            return -1;

        int index=-1;
        for(int i=0;i<activeSounds.size();i++)
        {
            if(activeSounds.get(i).id==sound.id)
            {
                index=i;
                break;
            }
        }

        return index;
    }

    /** internal helper class */
    private static class SoundInternal
    {
        public int id;
        public Clip clip;

        public SoundInternal(Clip clip, int id)
        {
            this.clip=clip;
            this.id=id;
        }
    }

    /** internal helper class */
    private static class SoundPreloaded
    {
        public byte[] data;
        public DataLine.Info info;
        public AudioFormat format;

        public SoundPreloaded()
        {
            data=null;
            info=null;
            format=null;
        }
    }
}

package main.java.org.game.Audio;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public final class AudioManager {

    private static int currentSoundId=69420;

    private static HashMap<String, SoundPreloaded> preloadedSounds=new HashMap<>();
    private static ArrayList<SoundInternal> activeSounds=new ArrayList<>();


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
        Clip clip;
        try{
            if(preloadedSounds.containsKey(file.getAbsolutePath()))//get preloaded data
            {
                SoundPreloaded si=preloadedSounds.get(file.getAbsolutePath());
                clip = (Clip) AudioSystem.getLine(si.info);
                clip.open(si.format,si.data,0,si.data.length);
            }
            else //get audio line
            {
                AudioInputStream aids2= AudioSystem.getAudioInputStream(file);
                clip=AudioSystem.getClip();
                clip.open(aids2);
            }
        }
        catch(Exception ex)
        {
            System.err.println("Something went wong while playing the file "+path);
            return null;
        }

        clip.addLineListener(event -> {
            if (event.getType() == LineEvent.Type.STOP) {
                AudioManager.closeSound(sond);
            }
        });

        clip.start();

        activeSounds.add(new SoundInternal(clip,id));

        return sond;
    }

    public static void closeSound(Sound sound)
    {
        if(sound==null)
            return;

        int index=-1;
        for(int i=0;i<activeSounds.size();i++)
        {
            if(activeSounds.get(i).id==sound.id)
            {
                index=i;
                break;
            }
        }

        if(index==-1)
            return;

        activeSounds.get(index).clip.stop();
        activeSounds.get(index).clip.close();
        activeSounds.remove(index);
    }

    public static void closeAllSounds()
    {
        for(SoundInternal si : activeSounds)
        {
            si.clip.stop();
            si.clip.close();
            activeSounds.remove(si);
        }
    }

    public static boolean isPlaying(Sound sound)
    {
        if(sound==null)
            return false;
        for(int i=0;i<activeSounds.size();i++)
            if(activeSounds.get(i).id==sound.id)
                return activeSounds.get(i).clip.isActive();
        return false;
    }

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

        SoundPreloaded sp=new SoundPreloaded();
        try(AudioInputStream aids = AudioSystem.getAudioInputStream(file))
        {
            sp.data = new byte[(int) aids.getFrameLength() * aids.getFormat().getFrameSize()];
            aids.read(sp.data);
            sp.format=aids.getFormat();
            sp.info=new DataLine.Info(Clip.class,sp.format);
        }
        catch(Exception ex)
        {
            System.err.println("Something went wong while preloading the file "+path);
            return;
        }

        preloadedSounds.put(file.getAbsolutePath(), sp);
    }

    public static void unloadPreloadedSounds()
    {
        AudioManager.closeAllSounds();
        preloadedSounds.clear();
    }

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

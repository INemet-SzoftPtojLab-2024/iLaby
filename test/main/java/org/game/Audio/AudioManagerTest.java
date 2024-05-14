package main.java.org.game.Audio;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import javax.sound.sampled.FloatControl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AudioManagerTest {
    @Mock
    Sound sound, sound1, sound2;
    @Test
    public void testPlaySound() {
        sound = AudioManager.playSound("assets/audio/click.ogg");
        assertNotNull(sound);

        Sound invalidSound = AudioManager.playSound("nonexistent/path/sound.ogg");
        assertNull(invalidSound);
    }

    @Test
    public void testCloseSound() {
        sound = AudioManager.playSound("assets/audio/click.ogg");
        assertNotNull(sound);
        AudioManager.closeSound(sound);
        assertFalse(AudioManager.isPlaying(sound));
        AudioManager.closeSound(null);
    }
    @Test
    public void testCloseAllSounds() {
        sound1 = AudioManager.playSound("assets/audio/playersound1.ogg");
        sound2 = AudioManager.playSound("assets/audio/won.ogg");

        //assertTrue(AudioManager.isPlaying(sound1));
        //assertTrue(AudioManager.isPlaying(sound2));
        assertNotNull(sound1);
        assertNotNull(sound2);

        AudioManager.closeAllSounds();

        assertFalse(AudioManager.isPlaying(sound1));
        assertFalse(AudioManager.isPlaying(sound2));
    }
    @Test
    public void testUnloadPreloadedSounds() {
        AudioManager.preloadSound("assets/audio/click.ogg");
        AudioManager.preloadSound("assets/audio/died.ogg");

        assertFalse(AudioManager.getPreloadedSounds().isEmpty());

        AudioManager.unloadPreloadedSounds();

        assertTrue(AudioManager.getPreloadedSounds().isEmpty());
    }

}
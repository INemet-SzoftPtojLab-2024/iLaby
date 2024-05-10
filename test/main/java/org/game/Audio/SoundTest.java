package main.java.org.game.Audio;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SoundTest {
    @Test
    public void testSoundId() {
        Sound sound = new Sound(1);

        assertEquals(1, sound.id);
    }

}
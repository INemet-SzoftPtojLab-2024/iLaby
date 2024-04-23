package main.java.org.game.Map;

import main.java.org.entities.villain.Villain;
import main.java.org.game.Audio.AudioManager;
import main.java.org.game.updatable.Updatable;

import java.util.Random;


public enum RoomType {
        GAS,
        BASIC,
        CURSED,
        SHADOW;

        public static RoomType getRandomRoomtype(boolean startRoom){
                RoomType[] choices = RoomType.values();
                RoomType[] startRoomChoices = {BASIC, CURSED, SHADOW};
                Random random = new Random();
                int index;
                if(startRoom)
                {
                        index = random.nextInt(startRoomChoices.length);
                        return startRoomChoices[index];
                }
                else{
                        index = random.nextInt(choices.length);
                        return choices[index];
                }
        }
}






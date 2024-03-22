package main.java.org.game.Map;

import java.util.Random;


public enum RoomType {
        GAS,
        BASIC,
        CURSED,
        SHADOW;

        public static RoomType getRandomRoomtype(){
                RoomType[] choices = RoomType.values();
                Random random = new Random();
                int index = random.nextInt(choices.length);
                return choices[index];

        }
}






package main.java.org.game.Map;

import java.util.Random;


public enum RoomType {
        GAS,
        BASIC,
        CURSED,
        SHADOW;

        public static RoomType getRandomRoomtype(boolean startRoom){
                RoomType[] choices = RoomType.values();
                Random random = new Random();
                int index;
                if(startRoom)
                {
                        return BASIC;
                }
                else{
                        index = random.nextInt(choices.length);
                        return choices[index];
                }
        }
}






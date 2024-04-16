package main.java.org.game.PlayerPrefs;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;

public class PlayerPrefs {

    private static HashMap<String, PlayerPrefsItemInternal> loadedItems=new HashMap<>();

    /**
     * load the data serialized into the playerprefs file<br>
     * it overrides the effect of every set and delete that was done after the last save() call
     */
    public static void load()
    {
        loadedItems.clear();

        File file=new File("./data/sus.amogus");
        if(!file.exists()||!file.canRead())
            return;

        byte[] bytes=null;

        try
        {
            bytes= Files.readAllBytes(file.toPath());
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        if(bytes==null)
            return;

        bytes=decode(bytes);

        //fill hash map
        String serializedText=new String(bytes);
        PlayerPrefsItemSerialized[] cuccok=new GsonBuilder().setPrettyPrinting().create().fromJson(serializedText,PlayerPrefsItemSerialized[].class);

        for(PlayerPrefsItemSerialized ppis : cuccok)
            loadedItems.put(ppis.key, ppis.data);
    }

    /**
     * save the contents of the playerprefs into the file <br>
     * this finalizes the effect of set and delete calls
     */
    public static void save()
    {
        //construct PlayerPrefsItemSerialized[]
        String[] keys=loadedItems.keySet().toArray(String[]::new);
        PlayerPrefsItemSerialized[] ppiss=new PlayerPrefsItemSerialized[loadedItems.keySet().size()];
        for(int i=0;i<keys.length;i++)
        {
            PlayerPrefsItemInternal ppii=loadedItems.get(keys[i]);
            ppiss[i]=new PlayerPrefsItemSerialized(keys[i], ppii);
        }

        String jasonString=new GsonBuilder().setPrettyPrinting().create().toJson(ppiss);
        byte[] bytes=jasonString.getBytes();
        if(bytes==null)
            bytes=new byte[0];

        bytes=encode(bytes);

        //print to file
        File fileDir=new File("./data");
        File file=new File(fileDir,"sus.amogus");
        FileOutputStream fos=null;
        try {
            if(!fileDir.exists())
                fileDir.mkdirs();
            file.createNewFile();
            fos = new FileOutputStream(file);
            fos.write(bytes);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        finally {
            if(fos!=null)
            {
                try{fos.close();}
                catch (Exception ex){}
            }
        }
    }

    /**
     * does the key already exist in the playerprefs system
     * @param key the key
     * @return true, if the key exists
     */
    public static boolean hasKey(String key)
    {
        return loadedItems.containsKey(key);
    }

    /**
     * delete an item from the playerprefs system <br>
     * if the key doesn't exist, nothing happens
     * @param key the key
     */
    public static void delete(String key)
    {
        loadedItems.remove(key);
    }

    /**
     * set an integer by key <br>
     * if the key is not yet registered, it will be done <br>
     * NOTE: a key can only be associated with one item (it doesn't matter, if they are of different types)
     * @param key the key
     * @param value the integer value
     */
    public static void setInt(String key, int value)
    {
        PlayerPrefsItemInternal ppii=loadedItems.get(key);
        if(ppii!=null&&ppii.type!=PLAYER_PREFS_INT)
        {
            System.err.println("The key "+key+" is already in use for a non-integer value bozo");
            return;
        }

        ppii=new PlayerPrefsItemInternal(value);
        loadedItems.put(key, ppii);
    }

    /**
     * set a float by key <br>
     * if the key is not yet registered, it will be done <br>
     * NOTE: a key can only be associated with one item (it doesn't matter, if they are of different types)
     * @param key the key
     * @param value the float value
     */
    public static void setFloat(String key, float value)
    {
        PlayerPrefsItemInternal ppii=loadedItems.get(key);
        if(ppii!=null&&ppii.type!=PLAYER_PREFS_FLOAT)
        {
            System.err.println("The key "+key+" is already in use for a non-float value bozo");
            return;
        }

        ppii=new PlayerPrefsItemInternal(value);
        loadedItems.put(key, ppii);
    }

    /**
     * set a string by key <br>
     * if the key is not yet registered, it will be done <br>
     * NOTE: a key can only be associated with one item (it doesn't matter, if they are of different types)
     * @param key the key
     * @param value the string value
     */
    public static void setString(String key, String value)
    {
        PlayerPrefsItemInternal ppii=loadedItems.get(key);
        if(ppii!=null&&ppii.type!=PLAYER_PREFS_STRING)
        {
            System.err.println("The key "+key+" is already in use for a non-string value bozo");
            return;
        }

        ppii=new PlayerPrefsItemInternal(value);
        loadedItems.put(key, ppii);
    }

    /**
     * acquire an integer value by key
     * @param key the key
     * @return the integer value, or <b>null</b>, if the key doesn't exist
     */
    public static Integer getInt(String key)
    {
        PlayerPrefsItemInternal ppii=loadedItems.get(key);
        if(ppii==null)
        {
            System.err.println("The key "+key+" is non-existent");
            return null;
        }
        if(ppii.type!=PLAYER_PREFS_INT)
        {
            System.err.println("The key "+key+" is used for a non-integer value bozo");
            return null;
        }

        return (Integer) ppii.intValue;
    }

    /**
     * acquire a float value by key
     * @param key the key
     * @return the float value, or <b>null</b>, if the key doesn't exist
     */
    public static Float getFloat(String key)
    {
        PlayerPrefsItemInternal ppii=loadedItems.get(key);
        if(ppii==null)
        {
            System.err.println("The key "+key+" is non-existent");
            return null;
        }
        if(ppii.type!=PLAYER_PREFS_FLOAT)
        {
            System.err.println("The key "+key+" is used for a non-float value bozo");
            return null;
        }

        return (Float) ppii.floatValue;
    }

    /**
     * acquire a string value by key
     * @param key the key
     * @return the string value, or <b>null</b>, if the key doesn't exist
     */
    public static String getString(String key)
    {
        PlayerPrefsItemInternal ppii=loadedItems.get(key);
        if(ppii==null)
        {
            System.err.println("The key "+key+" is non-existent");
            return null;
        }
        if(ppii.type!=PLAYER_PREFS_STRING)
        {
            System.err.println("The key "+key+" is used for a non-string value bozo");
            return null;
        }

        return ppii.stringValue;
    }

    private static byte[] decode(byte[] encodedBytes)
    {
        byte[] vissza=new byte[encodedBytes.length];
        for(int i=0;i<vissza.length;i++)
            vissza[i]=(byte)(encodedBytes[i] ^ (byte) 0b11010111);
        return vissza;
    }

    private static byte[] encode(byte[] bytes)
    {
        byte[] vissza=new byte[bytes.length];
        for(int i=0;i<vissza.length;i++)
            vissza[i]=(byte)(bytes[i] ^ (byte) 0b11010111);
        return vissza;
    }

    private static final int PLAYER_PREFS_INT=69;
    private static final int PLAYER_PREFS_FLOAT=420;
    private static final int PLAYER_PREFS_STRING=69420;

    private static class PlayerPrefsItemInternal{
        @SerializedName("type")
        public int type;
        @SerializedName("stringValue")
        public String stringValue="Morbonzola";
        @SerializedName("intValue")
        public int intValue=69420;
        @SerializedName("floatValue")
        public float floatValue=69.42f;

        public PlayerPrefsItemInternal(int value)
        {
            intValue=value;
            type=PLAYER_PREFS_INT;
        }

        public PlayerPrefsItemInternal(float value)
        {
            floatValue=value;
            type=PLAYER_PREFS_FLOAT;
        }

        public PlayerPrefsItemInternal(String value)
        {
            stringValue=value;
            type=PLAYER_PREFS_STRING;
        }
    }

    private static class PlayerPrefsItemSerialized
    {
        @SerializedName("key")
        public String key;
        @SerializedName("data")
        public PlayerPrefsItemInternal data;

        public PlayerPrefsItemSerialized(String key, PlayerPrefsItemInternal data)
        {
            this.key=key;
            this.data=data;
        }
    }
}

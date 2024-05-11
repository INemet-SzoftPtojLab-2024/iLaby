package main.java.org.game.UI;

import main.java.org.game.Audio.AudioManager;
import main.java.org.game.Graphics.ButtonUI;
import main.java.org.game.Graphics.ImageUI;
import main.java.org.game.Graphics.Renderable;
import main.java.org.game.Graphics.TextBox.TextBoxUI;
import main.java.org.game.Graphics.TextUI;
import main.java.org.game.Isten;
import main.java.org.game.PlayerPrefs.PlayerPrefs;
import main.java.org.game.updatable.Updatable;
import main.java.org.linalg.Vec2;
import main.java.org.manager.GameManager;

import javax.swing.*;
import java.util.ArrayList;

/**
 * This class represents the main menu.
 */
public class MainMenu extends Updatable {
    private MainMenuPanelEnum currentPanelType;
    private MainMenuPanel currentPanel=null;

    @Override
    public void onStart(Isten isten) {
        AudioManager.preloadSound("./assets/audio/click.ogg");

        currentPanelType=MainMenuPanelEnum.HOME;

        if(PlayerPrefs.hasKey("name")==false)
            PlayerPrefs.setString("name", "Logus");
        if(PlayerPrefs.hasKey("skin")==false)
            PlayerPrefs.setInt("skin",0);

        PlayerPrefs.setInt("isHost",0);//nem baj, ha ez mindig beallitodik
    }

    @Override
    public void onUpdate(Isten isten, double deltaTime) {
        if(currentPanel!=null)
            currentPanel.update(isten);

        boolean shouldChange=false;
        if(currentPanel==null&&currentPanelType!=MainMenuPanelEnum.NONE)
            shouldChange=true;
        else if(currentPanel!=null&&currentPanelType!=currentPanel.getType())
            shouldChange=true;

        if(shouldChange)
        {
            switch (currentPanelType)
            {
                case HOME:
                    if(currentPanel!=null)
                        currentPanel.unload(isten);
                    currentPanel=new HomePanel(this);
                    currentPanel.load(isten);
                    break;

                case DIFFICULTY:
                    if(currentPanel!=null)
                        currentPanel.unload(isten);
                    currentPanel=new DifficultyPanel(this);
                    currentPanel.load(isten);
                    break;

                case CHARACTER:
                    if(currentPanel!=null)
                        currentPanel.unload(isten);
                    currentPanel=new CharacterPanel(this);
                    currentPanel.load(isten);
                    break;

                case MULTI_QUESTION:
                    if(currentPanel!=null)
                        currentPanel.unload(isten);
                    currentPanel=new MultiQuestionPanel(this);
                    currentPanel.load(isten);
                    break;

                case SOLO_EASY:
                case SOLO_MEDIUM:
                case SOLO_HARD:
                    switch (currentPanelType)
                    {
                        case SOLO_EASY -> TimeCounter.setTime(901);
                        case SOLO_MEDIUM -> TimeCounter.setTime(601);
                        case SOLO_HARD -> TimeCounter.setTime(11);
                    }

                    if(currentPanel!=null)
                        currentPanel.unload(isten);
                    currentPanel=null;
                    GameManager.setStage(GameManager.GameStage.SOLO);
                    break;

                case MULTI:
                    if(currentPanel!=null)
                        currentPanel.unload(isten);
                    currentPanel=null;
                    TimeCounter.setTime(901);
                    GameManager.setStage(GameManager.GameStage.MULTI);
                    break;

                case EXIT:
                    if(currentPanel!=null)
                        currentPanel.unload(isten);
                    currentPanel=null;
                    GameManager.setStage(GameManager.GameStage.EXIT);
                    break;

                case NONE:
                    if(currentPanel!=null)
                        currentPanel.unload(isten);
                    currentPanel=null;
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {

    }

    private enum MainMenuPanelEnum{
        NONE, HOME, DIFFICULTY, CHARACTER, SOLO_EASY, SOLO_MEDIUM, SOLO_HARD, MULTI, MULTI_QUESTION, EXIT
    }

    private abstract class MainMenuPanel
    {
        protected MainMenu parent=null;
        protected ArrayList<Renderable> renderables;

        protected MainMenuPanel(MainMenu parent)
        {
            renderables=new ArrayList<>();
            this.parent=parent;
        }

        public abstract void load(Isten isten);
        public abstract void update(Isten isten);

        public void unload(Isten isten)
        {
            for(int i=0;i<renderables.size();i++)
                isten.getRenderer().deleteRenderable(renderables.get(i));
            renderables.clear();
        }

        public abstract MainMenuPanelEnum getType();
    }

    private class HomePanel extends MainMenuPanel
    {
        private ImageUI image_logo=null;
        private ImageUI image_developerLogo=null;
        private ImageUI image_background=null;

        private ButtonUI button_solo=null;
        private ButtonUI button_multi=null;
        private ButtonUI button_character=null;
        private ButtonUI button_exit=null;

        public HomePanel(MainMenu parent)
        {
            super(parent);
        }

        @Override
        public void load(Isten isten) {
            //images
            image_logo=new ImageUI(new Vec2(0, -275), new Vec2(420, 140), "./assets/ui/logo.png");
            image_developerLogo=new ImageUI(new Vec2(0, 375), new Vec2(300, 50), "./assets/ui/developer_logo.png");
            image_background=new ImageUI(new Vec2(0, 0), new Vec2(isten.getRenderer().getWidth(), isten.getRenderer().getHeight()), "./assets/ui/menu_background.jpg");

            image_logo.setSortingLayer(-69);
            image_developerLogo.setSortingLayer(-69);
            image_background.setSortingLayer(-67);

            renderables.add(image_logo);
            renderables.add(image_developerLogo);
            renderables.add(image_background);

            //buttons
            button_solo=new ButtonUI(new Vec2(0, -50), new Vec2(375, 100), "./assets/ui/button_background.png", "Single-player", "./assets/Bavarian.otf", 45);
            button_solo.setSortingLayer(-68);
            button_solo.addClickListener(() -> {
                AudioManager.playSound("./assets/audio/click.ogg");
                if(parent!=null)
                    parent.currentPanelType=MainMenuPanelEnum.DIFFICULTY;
            });

            button_multi=new ButtonUI(new Vec2(0, 50), new Vec2(375, 100), "./assets/ui/button_background.png", "Multiplayer", "./assets/Bavarian.otf", 45);
            button_multi.setSortingLayer(-68);
            button_multi.addClickListener(() -> {
                AudioManager.playSound("./assets/audio/click.ogg");
                if(parent!=null)
                    parent.currentPanelType=MainMenuPanelEnum.MULTI_QUESTION;
            });

            button_character=new ButtonUI(new Vec2(0, 150), new Vec2(375, 100), "./assets/ui/button_background.png", "Set drip", "./assets/Bavarian.otf", 45);
            button_character.setSortingLayer(-68);
            button_character.addClickListener(() -> {
                AudioManager.playSound("./assets/audio/click.ogg");
                if(parent!=null)
                    parent.currentPanelType=MainMenuPanelEnum.CHARACTER;
            });

            button_exit=new ButtonUI(new Vec2(0, 250), new Vec2(375, 100), "./assets/ui/button_background.png", "Exit", "./assets/Bavarian.otf", 45);
            button_exit.setSortingLayer(-68);
            button_exit.addClickListener(() -> {
                //AudioManager.playSound("./assets/audio/click.ogg");
                if(parent!=null)
                    parent.currentPanelType=MainMenuPanelEnum.EXIT;
            });

            renderables.add(button_solo);
            renderables.add(button_multi);
            renderables.add(button_character);
            renderables.add(button_exit);

            for(Renderable r : renderables)
            {
                r.setAlignment(Renderable.CENTER, Renderable.CENTER);
                isten.getRenderer().addRenderable(r);
            }
        }

        @Override
        public void update(Isten isten) {
            image_background.setScale(new Vec2(isten.getRenderer().getWidth(), isten.getRenderer().getHeight()));
        }

        @Override
        public MainMenuPanelEnum getType() {
            return MainMenuPanelEnum.HOME;
        }
    }

    private class DifficultyPanel extends MainMenuPanel
    {
        private ImageUI image_background=null;

        private ButtonUI button_easy=null;
        private ButtonUI button_medium=null;
        private ButtonUI button_hard=null;
        private ButtonUI button_back=null;

        private TextUI text_title=null;

        public DifficultyPanel(MainMenu parent)
        {
            super(parent);
        }

        @Override
        public void load(Isten isten) {

            //images
            image_background=new ImageUI(new Vec2(0, 0), new Vec2(isten.getRenderer().getWidth(), isten.getRenderer().getHeight()), "./assets/ui/menu_background.jpg");
            image_background.setSortingLayer(-67);
            renderables.add(image_background);


            //buttons
            button_easy=new ButtonUI(new Vec2(0, -50), new Vec2(375, 100), "./assets/ui/button_background.png", "Easy-peasy", "./assets/Bavarian.otf", 45);
            button_easy.setSortingLayer(-67);
            button_easy.addClickListener(() -> {
                //AudioManager.playSound("./assets/audio/click.ogg");
                if(parent!=null)
                    parent.currentPanelType=MainMenuPanelEnum.SOLO_EASY;
            });

            button_medium=new ButtonUI(new Vec2(0, 50), new Vec2(375, 100), "./assets/ui/button_background.png", "Casual", "./assets/Bavarian.otf", 45);
            button_medium.setSortingLayer(-67);
            button_medium.addClickListener(() -> {
                //AudioManager.playSound("./assets/audio/click.ogg");
                if(parent!=null)
                    parent.currentPanelType=MainMenuPanelEnum.SOLO_MEDIUM;
            });

            button_hard=new ButtonUI(new Vec2(0, 150), new Vec2(375, 100), "./assets/ui/button_background.png", "Gigachad", "./assets/Bavarian.otf", 45);
            button_hard.setSortingLayer(-67);
            button_hard.addClickListener(() -> {
                //AudioManager.playSound("./assets/audio/click.ogg");
                if(parent!=null)
                    parent.currentPanelType=MainMenuPanelEnum.SOLO_HARD;
            });

            button_back=new ButtonUI(new Vec2(0, 275), new Vec2(275, 100), "./assets/ui/button_background.png", "Back", "./assets/Bavarian.otf", 45);
            button_back.setSortingLayer(-67);
            button_back.addClickListener(()->{
                AudioManager.playSound("./assets/audio/click.ogg");
                if(parent!=null)
                    parent.currentPanelType=MainMenuPanelEnum.HOME;
            });

            renderables.add(button_easy);
            renderables.add(button_medium);
            renderables.add(button_hard);
            renderables.add(button_back);


            text_title=new TextUI("Choose difficulty", new Vec2(0, -300), "./assets/Bavarian.otf",50, 255, 255, 255);
            text_title.setShadowOn(false);
            text_title.setSortingLayer(-69);
            renderables.add(text_title);

            for(Renderable r : renderables)
            {
                r.setAlignment(Renderable.CENTER, Renderable.CENTER);
                isten.getRenderer().addRenderable(r);
            }
        }

        @Override
        public void update(Isten isten) {
            image_background.setScale(new Vec2(isten.getRenderer().getWidth(), isten.getRenderer().getHeight()));
        }

        @Override
        public MainMenuPanelEnum getType() {
            return MainMenuPanelEnum.DIFFICULTY;
        }
    }

    private class CharacterPanel extends MainMenuPanel
    {
        private ImageUI image_background=null;
        private ArrayList<ImageUI> image_characterImages=null;
        private int activeCharacterImage=0;

        private ButtonUI button_characterLeft=null;
        private ButtonUI button_characterRight=null;
        private ButtonUI button_back=null;

        private TextUI text_name=null;
        private TextBoxUI textBox_name=null;

        private TextUI text_title=null;

        public CharacterPanel(MainMenu parent)
        {
            super(parent);
        }

        @Override
        public void load(Isten isten) {
            //images
            image_background=new ImageUI(new Vec2(0, 0), new Vec2(isten.getRenderer().getWidth(), isten.getRenderer().getHeight()), "./assets/ui/menu_background.jpg");
            image_background.setSortingLayer(-67);
            renderables.add(image_background);

            activeCharacterImage=PlayerPrefs.getInt("skin");
            image_characterImages=new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                image_characterImages.add(new ImageUI(new Vec2(0, 75), new Vec2(200, 200), "./assets/character/character" + i + "_right1.png"));
                image_characterImages.get(i).setSortingLayer(-68);

                image_characterImages.get(i).setVisibility(false);
                if(i==activeCharacterImage)
                    image_characterImages.get(i).setVisibility(true);

                renderables.add(image_characterImages.get(i));
            }

            //buttons
            button_back=new ButtonUI(new Vec2(0, 275), new Vec2(275, 100), "./assets/ui/button_background.png", "Back", "./assets/Bavarian.otf", 45);
            button_back.setSortingLayer(-69);
            button_back.addClickListener(()->{
                AudioManager.playSound("./assets/audio/click.ogg");
                if(parent!=null)
                    parent.currentPanelType=MainMenuPanelEnum.HOME;
            });

            button_characterLeft=new ButtonUI(new Vec2(-250, 75), new Vec2(100, 100), "./assets/ui/arrow_left.png", "", "./assets/Bavarian.otf", 50);
            button_characterLeft.setSortingLayer(-69);
            button_characterLeft.addClickListener(() -> {
                AudioManager.playSound("./assets/audio/click.ogg");
                if (activeCharacterImage > 0) activeCharacterImage--;
                else activeCharacterImage = image_characterImages.size() - 1;
                PlayerPrefs.setInt("skin", activeCharacterImage);

                for (int i = 0; i < image_characterImages.size(); i++)
                    image_characterImages.get(i).setVisibility(i == activeCharacterImage);
            });

            button_characterRight=new ButtonUI(new Vec2(250, 75), new Vec2(100, 100), "./assets/ui/arrow_right.png", "", "./assets/Bavarian.otf", 50);
            button_characterRight.setSortingLayer(-69);
            button_characterRight.addClickListener(() -> {
                AudioManager.playSound("./assets/audio/click.ogg");
                if (activeCharacterImage < image_characterImages.size() - 1) activeCharacterImage++;
                else activeCharacterImage = 0;
                PlayerPrefs.setInt("skin", activeCharacterImage);
                for (int i = 0; i < image_characterImages.size(); i++)
                    image_characterImages.get(i).setVisibility(i == activeCharacterImage);
            });

            renderables.add(button_back);
            renderables.add(button_characterLeft);
            renderables.add(button_characterRight);

            //username block
            text_name=new TextUI("Nickname:", new Vec2(-175, -125), "./assets/Bavarian.otf",50, 255, 255, 255);
            text_name.setShadowOn(false);
            text_name.setSortingLayer(-69);

            textBox_name=new TextBoxUI(new Vec2(100,-110), new Vec2(300,50));
            textBox_name.setFont("./assets/Bavarian.otf",50);
            textBox_name.setText(PlayerPrefs.getString("name"));
            textBox_name.onInputEnd(textBox -> PlayerPrefs.setString("name", textBox.getText()));
            textBox_name.setSortingLayer(-69);

            renderables.add(text_name);
            renderables.add(textBox_name);

            text_title=new TextUI("Drip", new Vec2(0, -300), "./assets/Bavarian.otf",60, 255, 255, 255);
            text_title.setShadowOn(false);
            text_title.setSortingLayer(-69);
            renderables.add(text_title);


            for(Renderable r : renderables)
            {
                r.setAlignment(Renderable.CENTER, Renderable.CENTER);
                isten.getRenderer().addRenderable(r);
            }
        }

        @Override
        public void update(Isten isten) {
            image_background.setScale(new Vec2(isten.getRenderer().getWidth(), isten.getRenderer().getHeight()));
        }

        @Override
        public MainMenuPanelEnum getType() {
            return MainMenuPanelEnum.CHARACTER;
        }
    }

    private class MultiQuestionPanel extends MainMenuPanel
    {
        private ImageUI image_background=null;

        private TextUI text_title=null;

        private ButtonUI button_host=null;
        private ButtonUI button_connect=null;
        private ButtonUI button_back=null;

        public MultiQuestionPanel(MainMenu parent)
        {
            super(parent);
        }


        @Override
        public void load(Isten isten) {

            //images
            image_background=new ImageUI(new Vec2(0, 0), new Vec2(isten.getRenderer().getWidth(), isten.getRenderer().getHeight()), "./assets/ui/menu_background.jpg");
            image_background.setSortingLayer(-67);
            renderables.add(image_background);

            //buttons
            button_host=new ButtonUI(new Vec2(0, 0), new Vec2(375, 100), "./assets/ui/button_background.png", "Host game", "./assets/Bavarian.otf", 45);
            button_host.setSortingLayer(-69);
            button_host.addClickListener(()->{
                //AudioManager.playSound("./assets/audio/click.ogg");
                PlayerPrefs.setInt("isHost",69);
                if(parent!=null)
                    parent.currentPanelType=MainMenuPanelEnum.MULTI;
            });

            button_connect=new ButtonUI(new Vec2(0, 100), new Vec2(375, 100), "./assets/ui/button_background.png", "Connect...", "./assets/Bavarian.otf", 45);
            button_connect.setSortingLayer(-69);
            button_connect.addClickListener(()->{
                //AudioManager.playSound("./assets/audio/click.ogg");
                PlayerPrefs.setInt("isHost",0);
                if(parent!=null)
                    parent.currentPanelType=MainMenuPanelEnum.MULTI;
            });

            button_back=new ButtonUI(new Vec2(0, 275), new Vec2(275, 100), "./assets/ui/button_background.png", "Back", "./assets/Bavarian.otf", 45);
            button_back.setSortingLayer(-69);
            button_back.addClickListener(()->{
                AudioManager.playSound("./assets/audio/click.ogg");
                PlayerPrefs.setInt("isHost",0);
                if(parent!=null)
                    parent.currentPanelType=MainMenuPanelEnum.HOME;
            });

            renderables.add(button_host);
            renderables.add(button_connect);
            renderables.add(button_back);

            //text
            text_title=new TextUI("Multiplayer", new Vec2(0, -300), "./assets/Bavarian.otf",60, 255, 255, 255);
            text_title.setShadowOn(false);
            text_title.setSortingLayer(-69);
            renderables.add(text_title);


            for(Renderable r : renderables)
            {
                r.setAlignment(Renderable.CENTER, Renderable.CENTER);
                isten.getRenderer().addRenderable(r);
            }
        }

        @Override
        public void update(Isten isten) {
            image_background.setScale(new Vec2(isten.getRenderer().getWidth(), isten.getRenderer().getHeight()));
        }

        @Override
        public MainMenuPanelEnum getType() {
            return MainMenuPanelEnum.MULTI_QUESTION;
        }
    }
}

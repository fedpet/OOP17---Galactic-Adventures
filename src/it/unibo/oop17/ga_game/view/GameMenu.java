package it.unibo.oop17.ga_game.view;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import it.unibo.oop17.ga_game.controller.CheckConfig;
import it.unibo.oop17.ga_game.controller.CheckProgress;
import it.unibo.oop17.ga_game.controller.ConfigData;
import it.unibo.oop17.ga_game.controller.Main;
import it.unibo.oop17.ga_game.model.Difficulty;
import javafx.animation.TranslateTransition;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class GameMenu extends Parent {
    
    private final MenuButton btnNewGame;
    private final MenuButton btnContinue;
    private final MenuButton btnOptions;
    private final MenuButton btnExit;
    private final MenuButton btnBack;
    private final MenuButton btnMusic;
    private final MenuButton btnSFX;
    private final MenuButton btnLanguage;
    private final MenuButton btnDiff;
    private final MenuButton btnDefaults;
    private final MediaPlayer mediaPlayer;
    private final Map<Language, Map<Text, String>> languages;
    private final ConfigData data;
    private Map<Text, String> currLang;
    
    public GameMenu() throws IOException, ClassNotFoundException {
        
        final VBox menu0 = new VBox(8);
        final VBox menu1 = new VBox(8);
        
        this.data = CheckConfig.loadConfig();
        
        this.mediaPlayer = new MediaPlayer(new Media(Music.TRACK1.getMusic()));
        this.mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        this.mediaPlayer.setVolume(data.getMusicVol().getVolume());
        this.mediaPlayer.play();
        
        this.languages = new HashMap<Language, Map<Text, String>>();
        for (final Language l : Language.values()) {
            this.languages.put(l, loadLanguage(l));
        }
        updateLanguage();

        menu0.setTranslateX(96);
        menu0.setTranslateY(192);

        menu1.setTranslateX(96);
        menu1.setTranslateY(192);

        final int offset = 384;

        menu1.setTranslateX(offset);
        
        this.btnNewGame = new MenuButton(currLang.get(Text.NEW_GAME));
        btnNewGame.setOnMouseClicked(event -> {
            System.out.println("NG");
            // LANCIA IL TEST
            final String[] args = {};
            Main.main(args);
        });

        this.btnContinue = new MenuButton(currLang.get(Text.CONTINUE));
        this.btnContinue.setVisible(CheckProgress.exists());
        btnContinue.setOnMouseClicked(event -> {
            // LANCIA IL TEST
            final String[] args = {};
            Main.main(args);
        });

        this.btnOptions = new MenuButton(currLang.get(Text.OPTIONS));
        btnOptions.setOnMouseClicked(event -> {
            getChildren().add(menu1);

            final TranslateTransition tt = new TranslateTransition(Duration.seconds(0.25), menu0);
            tt.setToX(menu0.getTranslateX() - offset);

            final TranslateTransition tt1 = new TranslateTransition(Duration.seconds(0.5), menu1);
            tt1.setToX(menu0.getTranslateX());

            tt.play();
            tt1.play();

            tt.setOnFinished(evt -> {
                getChildren().remove(menu0);
            });
        });

        this.btnExit = new MenuButton(currLang.get(Text.EXIT));
        btnExit.setOnMouseClicked(event -> {
            System.exit(0);
        });

        this.btnBack = new MenuButton(currLang.get(Text.BACK));
        btnBack.setOnMouseClicked(event -> {
            getChildren().add(menu0);

            final TranslateTransition tt = new TranslateTransition(Duration.seconds(0.25), menu1);
            tt.setToX(menu1.getTranslateX() + offset);

            final TranslateTransition tt1 = new TranslateTransition(Duration.seconds(0.5), menu0);
            tt1.setToX(menu1.getTranslateX());

            tt.play();
            tt1.play();

            tt.setOnFinished(evt -> {
                getChildren().remove(menu1);
            });
        });
        
        this.btnMusic = new MenuButton(currLang.get(Text.VOLUME_M) + currLang.get(this.data.getMusicVol().asText()));
        btnMusic.setOnMouseClicked(event -> {
            this.data.setMusicVol((Volume.values()[(this.data.getMusicVol().ordinal() + 1) % Volume.values().length]));
            updateLanguage();
            updateMusic();
            this.btnMusic.update(currLang.get(Text.VOLUME_M) + currLang.get(this.data.getMusicVol().asText()), this.data.getSFXVol());
        });
        
        this.btnSFX = new MenuButton(currLang.get(Text.VOLUME_S) + currLang.get(this.data.getSFXVol().asText()));
        btnSFX.setOnMouseClicked(event -> {
            this.data.setSFXVol((Volume.values()[(this.data.getSFXVol().ordinal() + 1) % Volume.values().length]));
            updateLanguage();
            updateBtn();
        });
        
        this.btnLanguage = new MenuButton(currLang.get(Text.LANGUAGE) + currLang.get(this.data.getLanguage().asText()));
        btnLanguage.setOnMouseClicked(event -> {
            this.data.setLanguage(Language.values()[(this.data.getLanguage().ordinal() + 1) % Language.values().length]);
            updateLanguage();
            updateBtn();
        });
        
        this.btnDiff = new MenuButton(currLang.get(Text.DIFFICULTY) + currLang.get(this.data.getDifficulty().asText()));
        btnDiff.setOnMouseClicked(event -> {
            this.data.setDifficulty(Difficulty.values()[(this.data.getDifficulty().ordinal() + 1) % Difficulty.values().length]);
            this.btnDiff.update(currLang.get(Text.DIFFICULTY) + currLang.get(this.data.getDifficulty().asText()), this.data.getSFXVol());
        });
        
        this.btnDefaults = new MenuButton(currLang.get(Text.DEFAULT_OPT));
        btnDefaults.setOnMouseClicked(event -> {
            this.data.defaultOptions();
            updateMusic();
            updateLanguage();
            updateBtn();
        });

        menu0.getChildren().addAll(btnContinue, btnNewGame, btnOptions, btnExit);
        menu1.getChildren().addAll(btnBack, btnMusic, btnSFX, btnDiff, btnLanguage, btnDefaults);
        
        

        final Rectangle bg = new Rectangle(1024, 512);
        bg.setOpacity(0);

        getChildren().addAll(bg, menu0);
    }
    
    private void updateBtn() {
        this.btnContinue.update(currLang.get(Text.CONTINUE), this.data.getSFXVol());
        this.btnNewGame.update(currLang.get(Text.NEW_GAME), this.data.getSFXVol());
        this.btnOptions.update(currLang.get(Text.OPTIONS), this.data.getSFXVol());
        this.btnExit.update(currLang.get(Text.EXIT), this.data.getSFXVol());
        this.btnBack.update(currLang.get(Text.BACK), this.data.getSFXVol());
        this.btnMusic.update(currLang.get(Text.VOLUME_M) + currLang.get(this.data.getMusicVol().asText()), this.data.getSFXVol());
        this.btnSFX.update(currLang.get(Text.VOLUME_S) + currLang.get(this.data.getSFXVol().asText()), this.data.getSFXVol());
        this.btnLanguage.update(currLang.get(Text.LANGUAGE) + currLang.get(this.data.getLanguage().asText()), this.data.getSFXVol());
        this.btnDiff.update(currLang.get(Text.DIFFICULTY) + currLang.get(this.data.getDifficulty().asText()), this.data.getSFXVol());
        this.btnDefaults.update(currLang.get(Text.DEFAULT_OPT), this.data.getSFXVol());
    }
    
    private void updateLanguage() {
        this.currLang = this.languages.get(this.data.getLanguage());
    }
    
    private void updateMusic() {
        this.mediaPlayer.stop();
        this.mediaPlayer.setVolume(this.data.getMusicVol().getVolume());
        this.mediaPlayer.play();
    }
    
    private Map<Text, String> loadLanguage(final Language l) throws IOException {
        return  Files.lines(Paths.get("res", "languages", l.toString() + ".txt"))
                .map(line -> line.split("=", 2))
                .collect(Collectors.toMap(key -> Text.valueOf(key[0]), val -> val[1]));
    }
    
    
}
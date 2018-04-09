package it.unibo.oop17.ga_game.view.entities;

import javafx.geometry.Dimension2D;
import javafx.scene.Group;
import javafx.scene.image.Image;

public class MovingPlatformView extends AbstractEntityView implements LifelessEntityView {
    private static final int WIDTH = 70, HEIGHT = 70;

    public MovingPlatformView(final Group group) {
        super(group, new Dimension2D(WIDTH, HEIGHT));

        getView().setImage(new Image("/stone.png"));
    }
}

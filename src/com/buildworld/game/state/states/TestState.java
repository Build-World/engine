package com.buildworld.game.state.states;

import com.buildworld.game.state.State;
import com.buildworld.graphics.RenderService;
import com.buildworld.graphics.bootstrap.Renderer;
import com.buildworld.graphics.colors.RGBAColor;
import com.shawnclake.morgencore.core.component.services.Services;

public class TestState implements State {
    private int gameWidth = 640;
    private int gameHeight = 480;

    @Override
    public void input() {

    }

    @Override
    public void update(float delta) {
        //System.out.println("ticked with delta: " + delta);
    }

    @Override
    public void render(float alpha) {
        //System.out.println("drew with alpha: " + alpha);
        Renderer renderer = Services.getService(RenderService.class).getRenderer();
        String scoreText = "Score";
        int scoreTextWidth = renderer.getTextWidth(scoreText);
        int scoreTextHeight = renderer.getTextHeight(scoreText);
        float scoreTextX = (gameWidth - scoreTextWidth) / 2f;
        float scoreTextY = gameHeight - scoreTextHeight - 5;
        renderer.drawText(scoreText, scoreTextX, scoreTextY, RGBAColor.GREEN);
    }

    @Override
    public void enter() {

    }

    @Override
    public void exit() {

    }
}

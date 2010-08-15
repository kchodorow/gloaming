package com.snailinaturtleneck.gloaming;

import android.app.Activity;
import android.os.Bundle;

public class GameScreen extends Activity {
  private GamePanel gamePanel;

@Override
  protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
    gamePanel = new GamePanel(this);
	setContentView(gamePanel);
	(new Thread(gamePanel)).start();
  }

@Override
  protected void onDestroy() {
	gamePanel.destroyDrawingCache();
	super.onDestroy();
  }
}

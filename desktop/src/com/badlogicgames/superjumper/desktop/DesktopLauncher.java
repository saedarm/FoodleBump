package com.badlogicgames.superjumper.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogicgames.superjumper.SuperJumper;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("Super Jumper");
		config.setWindowedMode(480, 800);
		config.useVsync(true);
		config.setForegroundFPS(60);
		new Lwjgl3Application(new SuperJumper(), config);
	}
}
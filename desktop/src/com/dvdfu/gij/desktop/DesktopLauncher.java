package com.dvdfu.gij.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.dvdfu.gij.MainGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		TexturePacker.process("unpacked/", "/home/david/workspace/gij/gij/core/assets/img", "images");
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.fullscreen = true;
		config.width = 1366;
		config.height = 768;
		new LwjglApplication(new MainGame(), config);
	}
}

package com.dvdfu.gij.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.dvdfu.gij.Consts;
import com.dvdfu.gij.MainGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		TexturePacker.process("unpacked/", "/home/david/workspace/gij/winter-gi-jam/core/assets/img", "images");
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
//		config.fullscreen = true;
		config.width = Consts.width * 2;
		config.height = Consts.height * 2;
		config.resizable = false;
		new LwjglApplication(new MainGame(), config);
	}
}

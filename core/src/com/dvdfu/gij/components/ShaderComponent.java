package com.dvdfu.gij.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class ShaderComponent extends ShaderProgram {
	public ShaderComponent(String vertexShader, String fragmentShader) {
		super(Gdx.files.internal(vertexShader), Gdx.files.internal(fragmentShader));
		if (!isCompiled()) {
			Gdx.app.log("Shader compilation failed: ", getLog());
		}
		ShaderProgram.pedantic = false;
	}
}
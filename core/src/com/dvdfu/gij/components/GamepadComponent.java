package com.dvdfu.gij.components;

import java.util.HashMap;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;

public class GamepadComponent implements ControllerListener{
	Controller controller;
	HashMap<Controller, Device> map;
	
	public class Device {
		boolean[] keys; // true = pressed
		boolean[] preKeys;
		
		public Device() {
			keys = new boolean[Button.values().length];
			preKeys = new boolean[keys.length];
		}
	}
	
	public enum Button {
		RIGHT, LEFT, L, R, TRI, SQU, CIR, CRO
	}

	public GamepadComponent() {
		map = new HashMap<Controller, Device>();
		for (Controller c : Controllers.getControllers()) {
			map.put(c, new Device());
			Controllers.addListener(this);
		}
	}
	
	public void update() {
		for (Device d : map.values()) {
			for (int i = 0; i < Button.values().length; i++) {
				d.preKeys[i] = d.keys[i];
			}
		}
	}
	
	public boolean keyDown(int n, Button button) {
		if (n >= Controllers.getControllers().size) return false;
		Controller c = Controllers.getControllers().get(n);
		return map.get(c).keys[button.ordinal()];
	}
	
	public boolean keyPressed(int n, Button button) {
		if (n >= Controllers.getControllers().size) return false;
		Controller c = Controllers.getControllers().get(n);
		return map.get(c).keys[button.ordinal()] && !map.get(c).preKeys[button.ordinal()];
	}
	
	public boolean keyReleased(int n, Button button) {
		if (n >= Controllers.getControllers().size) return false;
		Controller c = Controllers.getControllers().get(n);
		return !map.get(c).keys[button.ordinal()] && map.get(c).preKeys[button.ordinal()];
	}

	public void connected(Controller controller) {}
	public void disconnected(Controller controller) {}

	public boolean buttonDown(Controller controller, int buttonCode) {
		Device d = map.get(controller);
		switch (buttonCode) {
			case 0: d.keys[Button.SQU.ordinal()] = true; break;
			case 1: d.keys[Button.CRO.ordinal()] = true; break;
			case 2: d.keys[Button.CIR.ordinal()] = true; break;
			case 3: d.keys[Button.TRI.ordinal()] = true; break;
			case 4: d.keys[Button.L.ordinal()] = true; break;
			case 5: d.keys[Button.R.ordinal()] = true; break;
		}
		return false;
	}

	public boolean buttonUp(Controller controller, int buttonCode) {
		Device d = map.get(controller);
		switch (buttonCode) {
			case 0: d.keys[Button.SQU.ordinal()] = false; break;
			case 1: d.keys[Button.CRO.ordinal()] = false; break;
			case 2: d.keys[Button.CIR.ordinal()] = false; break;
			case 3: d.keys[Button.TRI.ordinal()] = false; break;
			case 4: d.keys[Button.L.ordinal()] = false; break;
			case 5: d.keys[Button.R.ordinal()] = false; break;
		}
		return false;
	}

	public boolean povMoved(Controller controller, int povCode, PovDirection value) {
		Device d = map.get(controller);
		d.keys[Button.RIGHT.ordinal()] = value == PovDirection.east;
		d.keys[Button.LEFT.ordinal()] = value == PovDirection.west;
		return false;
	}

	public boolean axisMoved(Controller controller, int axisCode, float value) { return false; }
	public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) { return false; }
	public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) { return false; }
	public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) { return false; }
}

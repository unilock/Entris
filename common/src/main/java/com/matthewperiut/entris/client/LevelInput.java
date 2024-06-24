package com.matthewperiut.entris.client;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_BACKSPACE;

public class LevelInput {
    String numberStr = "";

    public void input(int scanCode) {
        if (scanCode > 47 && scanCode < 58) {
            if (numberStr.length() < 2) {
                numberStr += String.valueOf(scanCode - 48);
            }
        }
        if (scanCode == GLFW_KEY_BACKSPACE) {
            if (!numberStr.isEmpty()) {
                numberStr = numberStr.substring(0,numberStr.length()-1);
            }
        }

        if (getNumber() > 30) {
            numberStr = "30";
        }
    }

    public String getNumberStr() {
        return numberStr;
    }

    public int getNumber() {
        if (numberStr.isEmpty())
            return 0;
        else
            return Integer.parseInt(numberStr);
    }
}

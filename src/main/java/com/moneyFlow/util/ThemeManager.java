package com.moneyFlow.util;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

/**
 * Gerenciador centralizado de temas (Dark/Light) para o moneyFlow.
 * Singleton que armazena as paletas, persiste a escolha do usuário,
 * e notifica listeners quando o tema é alterado.
 */
public class ThemeManager {

    private static final ThemeManager INSTANCE = new ThemeManager();

    private static final String PREF_KEY_DARK_MODE = "moneyflow_dark_mode";

    private boolean darkMode;
    private final List<Runnable> listeners = new ArrayList<>();

    // ==================== PALETA DARK ====================
    private static final Color DARK_BG_DARK        = new Color(18, 18, 24);
    private static final Color DARK_BG_CARD        = new Color(30, 30, 42);
    private static final Color DARK_BG_NAVBAR      = new Color(22, 22, 32);
    private static final Color DARK_BG_INPUT       = new Color(40, 40, 55);
    private static final Color DARK_TEXT_PRIMARY    = new Color(235, 235, 245);
    private static final Color DARK_TEXT_SECONDARY  = new Color(160, 160, 180);
    private static final Color DARK_BORDER_COLOR   = new Color(55, 55, 75);
    private static final Color DARK_ACCENT_BLUE    = new Color(80, 140, 255);
    private static final Color DARK_ACCENT_GREEN   = new Color(72, 199, 142);
    private static final Color DARK_ACCENT_RED     = new Color(235, 87, 87);

    // ==================== PALETA LIGHT ====================
    private static final Color LIGHT_BG_DARK       = new Color(245, 245, 250);
    private static final Color LIGHT_BG_CARD       = new Color(255, 255, 255);
    private static final Color LIGHT_BG_NAVBAR     = new Color(250, 250, 255);
    private static final Color LIGHT_BG_INPUT      = new Color(235, 235, 240);
    private static final Color LIGHT_TEXT_PRIMARY   = new Color(30, 30, 40);
    private static final Color LIGHT_TEXT_SECONDARY = new Color(110, 110, 130);
    private static final Color LIGHT_BORDER_COLOR  = new Color(210, 210, 220);
    private static final Color LIGHT_ACCENT_BLUE   = new Color(60, 120, 235);
    private static final Color LIGHT_ACCENT_GREEN  = new Color(50, 180, 120);
    private static final Color LIGHT_ACCENT_RED    = new Color(220, 70, 70);

    private ThemeManager() {
        Preferences prefs = Preferences.userNodeForPackage(ThemeManager.class);
        this.darkMode = prefs.getBoolean(PREF_KEY_DARK_MODE, true); // Dark por padrão
    }

    public static ThemeManager getInstance() {
        return INSTANCE;
    }

    // ==================== CONTROLE DE TEMA ====================

    public boolean isDarkMode() {
        return darkMode;
    }

    public void setTheme(boolean isDark) {
        if (this.darkMode == isDark) return;
        this.darkMode = isDark;

        // Persistir a escolha
        Preferences prefs = Preferences.userNodeForPackage(ThemeManager.class);
        prefs.putBoolean(PREF_KEY_DARK_MODE, isDark);

        // Notificar todos os listeners
        notifyListeners();
    }

    // ==================== LISTENERS ====================

    public void addThemeChangeListener(Runnable listener) {
        listeners.add(listener);
    }

    public void removeThemeChangeListener(Runnable listener) {
        listeners.remove(listener);
    }

    private void notifyListeners() {
        for (Runnable listener : new ArrayList<>(listeners)) {
            listener.run();
        }
    }

    // ==================== GETTERS DE CORES ====================

    public Color getBgDark() {
        return darkMode ? DARK_BG_DARK : LIGHT_BG_DARK;
    }

    public Color getBgCard() {
        return darkMode ? DARK_BG_CARD : LIGHT_BG_CARD;
    }

    public Color getBgNavbar() {
        return darkMode ? DARK_BG_NAVBAR : LIGHT_BG_NAVBAR;
    }

    public Color getBgInput() {
        return darkMode ? DARK_BG_INPUT : LIGHT_BG_INPUT;
    }

    public Color getTextPrimary() {
        return darkMode ? DARK_TEXT_PRIMARY : LIGHT_TEXT_PRIMARY;
    }

    public Color getTextSecondary() {
        return darkMode ? DARK_TEXT_SECONDARY : LIGHT_TEXT_SECONDARY;
    }

    public Color getBorderColor() {
        return darkMode ? DARK_BORDER_COLOR : LIGHT_BORDER_COLOR;
    }

    public Color getAccentBlue() {
        return darkMode ? DARK_ACCENT_BLUE : LIGHT_ACCENT_BLUE;
    }

    public Color getAccentGreen() {
        return darkMode ? DARK_ACCENT_GREEN : LIGHT_ACCENT_GREEN;
    }

    public Color getAccentRed() {
        return darkMode ? DARK_ACCENT_RED : LIGHT_ACCENT_RED;
    }
}

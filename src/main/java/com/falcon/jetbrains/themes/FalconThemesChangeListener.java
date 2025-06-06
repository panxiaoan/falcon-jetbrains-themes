package com.falcon.jetbrains.themes;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import com.intellij.ide.ui.LafManager;
import com.intellij.ide.ui.LafManagerListener;
import com.intellij.openapi.editor.colors.EditorColorsManager;

/**
 * @author <a href="mailto:xiaoan.pan@qq.com">潘小安</a>
 * @since 2025-06-06 14:03
 */
public class FalconThemesChangeListener implements LafManagerListener {

    private final EditorColorsManager editorColorsManager = EditorColorsManager.getInstance();
    private String previousUI = LafManager.getInstance().getCurrentLookAndFeel().getName();

    @Override
    public void lookAndFeelChanged(@NotNull LafManager lafManager) {
        String currentUI = lafManager.getCurrentLookAndFeel().getName();
        if (!Objects.equals(previousUI, currentUI)) {
            if (FalconThemes.LIGHT_CELADON.getName().equals(currentUI)
                || FalconThemes.LIGHT_GREEN.getName().equals(currentUI)
                || FalconThemes.LIGHT_GREEN_CONTRAST.getName().equals(currentUI)
                || FalconThemes.LIGHT_GREY.getName().equals(currentUI)
                || FalconThemes.LIGHT_PINK.getName().equals(currentUI)
                || FalconThemes.LIGHT_YELLOW.getName().equals(currentUI)
                || FalconThemes.DARK.getName().equals(currentUI) || FalconThemes.DARK_BLUE.getName().equals(currentUI)
                || FalconThemes.DARK_COFFEE.getName().equals(currentUI)
                || FalconThemes.DARK_DARCULA.getName().equals(currentUI)
                || FalconThemes.DARK_GREEN.getName().equals(currentUI)
                || FalconThemes.DARK_VIOLET.getName().equals(currentUI)) {
                editorColorsManager.setGlobalScheme(editorColorsManager.getScheme("_@user_" + currentUI + " Colorful"));
            }
        }
        previousUI = currentUI;
    }
}

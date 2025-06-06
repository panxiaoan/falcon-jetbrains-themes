package com.falcon.jetbrains.themes;

/**
 * @author <a href="mailto:xiaoan.pan@qq.com">潘小安</a>
 * @since 2025-06-06 13:35
 */
public enum FalconThemes {

    LIGHT_CELADON("Relax Light Celadon"),
    LIGHT_GREEN("Relax Light Green"),
    LIGHT_GREEN_CONTRAST("Relax Light Green Contrast"),
    LIGHT_GREY("Relax Light Grey"),
    LIGHT_PINK("Relax Light Pink"),
    LIGHT_YELLOW("Relax Light Yellow"),

    DARK("Relax Dark"),
    DARK_BLUE("Relax Dark Blue"),
    DARK_COFFEE("Relax Dark Coffee"),
    DARK_DARCULA("Relax Dark Darcula"),
    DARK_GREEN("Relax Dark Green"),
    DARK_VIOLET("Relax Dark Violet");

    private final String name;

    FalconThemes(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

import java.awt.*;

/** Central design tokens for the DriveEase Swing UI. */
public final class UiTheme {

    private UiTheme() {}

    // Brand
    public static final String APP_NAME = "DriveEase";

    // Palette — warm automotive light theme
    public static final Color PRIMARY       = new Color(15, 118, 110);   // teal-700
    public static final Color PRIMARY_LIGHT = new Color(20, 184, 166);   // teal-500
    public static final Color PRIMARY_DARK  = new Color(13, 94, 88);
    public static final Color ACCENT        = new Color(245, 158, 11); // amber-500
    public static final Color ACCENT_LIGHT  = new Color(252, 211, 77);

    public static final Color BG            = new Color(241, 245, 249);  // slate-100
    public static final Color SURFACE       = Color.WHITE;
    public static final Color SURFACE_ALT   = new Color(248, 250, 252);
    public static final Color BORDER        = new Color(226, 232, 240);
    public static final Color BORDER_FOCUS  = new Color(20, 184, 166, 180);

    public static final Color TEXT_PRIMARY  = new Color(15, 23, 42);
    public static final Color TEXT_SECONDARY = new Color(100, 116, 139);
    public static final Color TEXT_ON_PRIMARY = Color.WHITE;

    public static final Color SUCCESS       = new Color(22, 163, 74);
    public static final Color SUCCESS_BG    = new Color(220, 252, 231);
    public static final Color ERROR         = new Color(220, 38, 38);
    public static final Color ERROR_BG      = new Color(254, 226, 226);
    public static final Color WARNING       = new Color(217, 119, 6);

    public static final Color SIDEBAR_BG    = new Color(15, 23, 42);
    public static final Color SIDEBAR_TEXT  = new Color(203, 213, 225);
    public static final Color SIDEBAR_ACTIVE = new Color(20, 184, 166);

    // Typography
    public static final String FONT_FAMILY = "Segoe UI";

    public static Font font(int style, int size) {
        return new Font(FONT_FAMILY, style, size);
    }

    public static Font h1()       { return font(Font.BOLD, 28); }
    public static Font h2()       { return font(Font.BOLD, 22); }
    public static Font h3()       { return font(Font.BOLD, 16); }
    public static Font body()     { return font(Font.PLAIN, 14); }
    public static Font bodyBold() { return font(Font.BOLD, 14); }
    public static Font small()    { return font(Font.PLAIN, 12); }
    public static Font caption()  { return font(Font.PLAIN, 11); }

    // Spacing
    public static final int PAD_SM  = 8;
    public static final int PAD_MD  = 16;
    public static final int PAD_LG  = 24;
    public static final int PAD_XL  = 32;
    public static final int RADIUS  = 12;
    public static final int RADIUS_SM = 8;
}

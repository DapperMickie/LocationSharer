package github.com.dappermickie.locationsharer;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.image.BufferedImage;
import javax.inject.Inject;
import javax.swing.SwingUtilities;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;

public class LocationSharerSidebar
{
    @Inject private ClientToolbar toolbar;
    private NavigationButton navigation;
    private LocationSharerPanel panel;
    private volatile long generation;

    public void open(Runnable onCopy, Runnable onPaste)
    {
        long current = ++generation;
        SwingUtilities.invokeLater(() -> {
            if (current != generation) return;
            panel = new LocationSharerPanel(onCopy, onPaste);
            navigation = NavigationButton.builder().tooltip("Location Sharer")
                .icon(createIcon()).panel(panel).priority(6).build();
            toolbar.addNavigation(navigation);
        });
    }

    public void close()
    {
        ++generation;
        SwingUtilities.invokeLater(() -> {
            if (navigation != null) toolbar.removeNavigation(navigation);
            navigation = null;
            panel = null;
        });
    }

    public void setStatus(String text)
    {
        long current = generation;
        SwingUtilities.invokeLater(() -> {
            if (current == generation && panel != null) panel.setStatus(text);
        });
    }

    public void copyToClipboard(String text)
    {
        long current = generation;
        SwingUtilities.invokeLater(() -> {
            if (current != generation || panel == null) return;
            try
            {
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(text), null);
                setStatus("Location copied. Share it with a friend.");
            }
            catch (IllegalStateException | HeadlessException ex)
            {
                setStatus("Clipboard unavailable. Please try again.");
            }
        });
    }

    public String readClipboard() throws Exception
    {
        return (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
    }

    private static BufferedImage createIcon()
    {
            BufferedImage icon = new BufferedImage(24, 24, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = icon.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(new Color(110, 205, 225));
            g.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.drawLine(5, 19, 19, 5);
            g.drawLine(9, 5, 19, 5);
            g.drawLine(19, 5, 19, 15);
            g.dispose();
        return icon;
    }
}

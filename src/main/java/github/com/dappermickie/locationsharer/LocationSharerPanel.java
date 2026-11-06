package github.com.dappermickie.locationsharer;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import net.runelite.client.ui.PluginPanel;

public class LocationSharerPanel extends PluginPanel
{
    private final JLabel status;

    public LocationSharerPanel(Runnable onCopy, Runnable onPaste)
    {
        setLayout(new BorderLayout());
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(20, 12, 20, 12));
        JLabel title = new JLabel("Location Sharer");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 19f));
        content.add(title);
        content.add(Box.createVerticalStrut(8));
        content.add(new JLabel("<html>Share a tile. Find your way there.</html>"));
        content.add(Box.createVerticalStrut(24));
        JButton copy = new JButton("Copy Current Location");
        JButton paste = new JButton("Paste Location for Shortest Path");
        for (JButton button : new JButton[]{copy, paste})
        {
            button.setAlignmentX(Component.LEFT_ALIGNMENT);
            button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
            button.setMargin(new Insets(10, 4, 10, 4));
            content.add(button);
            content.add(Box.createVerticalStrut(10));
        }
        content.add(Box.createVerticalStrut(12));
        content.add(new JLabel("<html><b>Shortest Path required</b><br>Install and enable Shortest Path from<br>the Plugin Hub to show a route.</html>"));
        content.add(Box.createVerticalStrut(18));
        status = new JLabel("<html>Copy your tile or paste a shared location.</html>");
        status.setForeground(new Color(170, 190, 200));
        content.add(status);
        copy.addActionListener(e -> onCopy.run());
        paste.addActionListener(e -> onPaste.run());
        add(content, BorderLayout.NORTH);
    }

    public void setStatus(String text)
    {
        status.setText("<html>" + text + "</html>");
    }
}

package github.com.dappermickie.locationsharer;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.Collections;
import javax.inject.Inject;

import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.events.PluginMessage;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@PluginDescriptor(name = "Location Sharer", description = "Copy and share locations. Requires Shortest Path installed and enabled to show routes.", tags = {"location", "share", "clipboard", "coordinates", "shortest path", "navigation", "route", "friends"})
public class LocationSharerPlugin extends Plugin
{
    @Inject private Client client;
    @Inject private ClientThread clientThread;
    @Inject private EventBus eventBus;
    @Inject private Gson gson;
    @Inject private LocationSharerSidebar sidebar;
    private volatile boolean active;

    @Override
    protected void startUp()
    {
        active = true;
        sidebar.open(this::copyLocation, this::pasteLocation);
    }

    private void message(String text)
    {
        sidebar.setStatus(text);
    }
    private void copyLocation()
    {
        clientThread.invoke(() -> {
            if (!active) return;
            if (client.getGameState() != GameState.LOGGED_IN || client.getLocalPlayer() == null)
            {
                message("Log in before copying a location.");
                return;
            }
            if (client.isInInstancedRegion())
            {
                message("Locations inside instances cannot be shared reliably. Copy a tile outside the instance.");
                return;
            }
            WorldPoint point = client.getLocalPlayer().getWorldLocation();
            JsonObject data = new JsonObject();
            data.addProperty("type", "location-sharer");
            data.addProperty("version", 1);
            data.addProperty("x", point.getX());
            data.addProperty("y", point.getY());
            data.addProperty("plane", point.getPlane());
            String json = gson.toJson(data);
            sidebar.copyToClipboard(json);
        });
    }

    private void pasteLocation()
    {
        try
        {
            String text = sidebar.readClipboard();
            if (text == null || text.length() > 1024) throw new IllegalArgumentException();
            JsonObject data = gson.fromJson(text, JsonObject.class);
            if (data == null || !"location-sharer".equals(data.get("type").getAsString()) || integer(data, "version", 1, 1) != 1) throw new IllegalArgumentException();
            WorldPoint target = new WorldPoint(integer(data, "x", 0, 16383), integer(data, "y", 0, 16383), integer(data, "plane", 0, 3));
            clientThread.invoke(() -> {
                if (!active) return;
                if (client.getGameState() != GameState.LOGGED_IN) { message("Log in before requesting a route."); return; }
                eventBus.post(new PluginMessage("shortestpath", "path", Collections.singletonMap("target", target)));
                message("Route requested to " + target.getX() + ", " + target.getY() + " (plane " + target.getPlane() + ").<br>If no route appears, enable Shortest Path.");
            });
        }
        catch (Exception ex) { message("Copy a valid Location Sharer location first, then try again."); }
    }

    private static int integer(JsonObject data, String key, int min, int max)
    {
        String value = data.get(key).getAsString();
        if (!value.matches("[0-9]{1,5}")) throw new IllegalArgumentException();
        int number = Integer.parseInt(value);
        if (number < min || number > max) throw new IllegalArgumentException();
        return number;
    }

    @Override
    protected void shutDown()
    {
        active = false;
        sidebar.close();
    }
}


package github.com.dappermickie.locationsharer;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class LocationSharerPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(LocationSharerPlugin.class);
		RuneLite.main(args);
	}
}

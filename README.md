# Location Sharer

A RuneLite sidebar with two buttons: copy your current tile as JSON, or paste a shared location to request a route through Shortest Path.

Install and enable **Shortest Path** from the Plugin Hub before requesting routes. Location Sharer uses its public `shortestpath` / `path` PluginMessage integration. It does not move your player or enable disabled plugins.

Copied data contains a format identifier, version, x, y, and plane. It contains no player name or account details. Locations inside instances are rejected because those tiles cannot reliably identify a shared destination. Routes use the receiving player's Shortest Path settings and unlocks. Sharing does not change worlds.

Run the development client with `./gradlew run`. For Jagex Accounts, follow https://github.com/runelite/runelite/wiki/Using-Jagex-Accounts.

Check copying and pasting between clients, all four planes, malformed clipboard text, logged-out use, instanced areas, and Shortest Path disabled. Confirm the route ends at the copied tile.

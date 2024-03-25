## Environment variables to set before launch:
<li>Discord bot token as "DISCORD_TOKEN" in SpringBot.java</li>
<li>TF2 guild (server) in discord "TF2CENTER_GUILD" in GlobalCommandRegistrar.java</li>
<li>STEAM_API_KEY in SteamApi.java</li>
<li>TF2CLOBBY_CHANNEL in LobbyPublishable.java</li>

# Server run instructions

* Download the jar from releases on github to

```
~/discord/bots/tf2c-discord-lobby-parser-1.0.jar
```

* Download bash script from releases on github to

```
~/discord/bots/discord-bot-reloader
```

* Set up cron job every 2 hours as follows `0 */2 * * * ./full path to discord-bot-reloader`. To edit cron type "crontab
  -e" and insert the given numbers
* **_chill_**


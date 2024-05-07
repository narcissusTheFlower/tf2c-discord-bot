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

Make the script executable with `cmod +x ~/discord/bots/discord-bot-reloader`

* Create SCREEN session with

```
screen
```
Configure crontab as follows "23 */1 * * * /home/discord/bots/discord-bot-reloader"

# MC Mirror

Shaded binaries can be downloaded from the [releases](https://github.com/trinitrotoluene/mcmirror/releases) page. There is also a [spigot resource page](https://www.spigotmc.org/resources/mcmirror.73259) to which download links and changelogs are also posted.

If you're reporting a bug or requesting a feature (see below for more details on that), open an [issue](https://github.com/trinitrotoluene/mcmirror/issues/new).

## Features
- Plug-and-play, just drop a webhook and bot token (the bot must be in the server you wish to mirror to) into your `config.yml` and optionally configure the channel whitelist.
- Reliable Discord <-> Minecraft message mirroring powered by [Discord4J](https://github.com/Discord4J/Discord4J)
- (Discord) Role & Channel based whitelist functionality
- (Minecraft) Robust permissions for managing access to plugin features
- Uses webhooks to ensure user interactions are conversational
  - Supports rotating through multiple webhooks for high-frequency conversations

This plugin was written for and is used privately- so while feature and pull requests are welcome, keep in mind that if you need something drastically different from the provided feature-set, another alternative or forking may be more suitable.

## Versioning
This software is semantically versioned. Any changes which would cause the public facing API to change in a backwards-incompatible way will correspond to a bump in major version.

`major.minor.patch`

### Examples
- Changes to `config.yml` or `plugin.yml` (permissions)
- Changes to command names, behaviour or syntax

Since this may cause major version numbers to increment frequently, recent but non-current versions will still receive support and bug-fixes. Should the version you're currently using not be listed, follow the update migration instructions in the associated SpigotMC resource update to move to a newer version.

### Supported Versions
- 2.x
### Current Version
- 2.0.0
### Future
- 3.0.0
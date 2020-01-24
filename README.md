# MC Mirror

Shaded binaries can be downloaded from the [releases](https://github.com/trinitrotoluene/mcmirror/releases) page. There is a [spigot resource page](https://www.spigotmc.org/resources/mcmirror.73259) to which download links and changelogs are also posted.

If you're reporting a bug or requesting a feature (see below for more details on that), open an [issue](https://github.com/trinitrotoluene/mcmirror/issues/new).

## Features
- Reliable Discord <-> Minecraft message mirroring powered by [Discord4J](https://github.com/Discord4J/Discord4J)
- Restrict mirroring to a specific channel in your guild
- Access control via permission `mcmirror.mirror` on your server, and role & user ID whitelisting on Discord
- Uses webhooks to ensure user interactions are conversational
  - Supports rotating through multiple webhooks for high-frequency conversations
- Easy to set up, but extensively customisable

## Versioning
This software is semantically versioned. Any changes which would cause the public facing API to change in a backwards-incompatible way will correspond to a bump in major version.

`major.minor.patch`

### Examples
- Changes to `config.yml` or `plugin.yml` (permissions)
- Changes to command names, behaviour or syntax

Since this may cause major version numbers to increment frequently, recent but non-current versions will still receive support and bug-fixes. Should the version you're currently using not be listed, follow the update migration instructions in the associated SpigotMC resource update to move to a newer version.

### Supported Versions
- 3.x (1.14+)
- 2.x (1.14+)
### Current Version
- 3.0.0
### Future
- TBC
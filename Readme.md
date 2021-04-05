# MagaretoYorick Discord bot

### What is MagaretoYorick ?
MagaretoYorick is a general purpose discord bot written in java, using the [D4J library](https://github.com/Discord4J/Discord4J).

### Features

- Easy to work with command system that uses reflections to register the newly created classes, no need to add them anywhere , just implement the `YorickCommand` interface and add the `@Command` annotation.
- Error handling system. All the errors are handled properly, and the bot responds accordingly. If an exception occurs that is not catched, the bot will handle it and give output to the user.
- IOC Container. It uses google's guice IOC container. Maybe in the future I will write a custom one.

### How to run

1. Clone the repo `git clone https://github.com/superuacanea/MagaretoYorick`
2. Provide [the token that you got from discord](https://discord.com/developers/applications) and set  an environment variable called `BOT_TOKEN` with the value of the discord provided token.
3. Run the bot.

### License
MIT
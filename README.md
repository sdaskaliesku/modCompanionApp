# Fallout 76 Mod Companion app

# Idea

Simple file changes listener to detect any new content in the file and execute mod specific logic.

# Running

Easy, just run `com.manson.fo76.Main` from your IDE

# Build

Run `mvn clean package` to build final bundle 

# Adding support for custom mod

Feel free to fork this repo and add support for your custom mod

0. Define your mod output structure, via extending `com.manson.fo76.processor.BaseModEntity` and specifying its type via annotation (refer to existing one's as an example).

Note, `modName` is a required field in order to make everything work.

1. Add new Gui controller to handle your mod settings by extending `com.manson.fo76.processor.gui.ModGuiController` class
2. Add custom settings object into `com.manson.fo76.settings.Settings` class
3. Add a new processor to handle changes in the file by extending `com.manson.fo76.processor.BaseProcessor` class
4. Initialize a controller and processor via `com.manson.fo76.processor.ProcessorFactory#init` method

That's it, provide pull request, and I'll take a look.
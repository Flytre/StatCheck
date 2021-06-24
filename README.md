# StatCheck


## How to make StatCheck addons / add compatibility tooltips:

### Setup:

1. Add jitpack to `build.gradle`
```
repositories {
	maven { url "https://jitpack.io" }
}
```

2. Add the latest version of `StatCheck` and `FlytreLib`:
```
dependencies {
	modImplementation 'com.github.Flytre:FlytreLib:CommitHash'
	modImplementation 'com.github.Flytre:StatCheck:CommitHash'
}
```
CommitHash can be found by going to https://github.com/Flytre/StatCheck/commits/master and https://github.com/FlytreLib/StatCheck/commits/master for the relevant mods.

3. Tell Fabric to execute relevant code if the 2 mods above are loaded: `if (FabricLoader.getInstance().isModLoaded("mod_id"))`


### Creating the Addon:

API Classes are found here: 

https://github.com/Flytre/StatCheck/tree/master/src/main/java/net/flytre/stat_check/api 

**StatEntry**: 
A StatEntry represents the value of an attribute (i.e. attack damage, durability) and tells the renderer how to render the value of that attribute. 
For example, for a String like flammability it tells it to render the String value and not to display any comparison arrows. 
For a numerical attribute like durability it tells it to render the value as a number and use comparison arrows.
To be clear, a StatEntry represents a TYPE of value, i.e. double, string, or translation, and how to render it, and is NOT unique to each attribute (See: DisplayType).

**DisplayType**:
A DisplayType represents the actual attribute and stores its data, including the icon index and icon file. Icon files should be `256x64`, where indices start at 0 in the top left and increase first by row then by column.
DislayTypes store a condition on when to display the attribute (what kind of items), and a function to provide a StatEntry from an ItemStack if the attribute should be displayed.
DisplayTypes are rendered in the order they are instantiated.

**DisplayTypeRegistry**:
You'll need to registry your display types in the registry in order for them to show up in game. Works pretty much the same as a vanilla Registry, but you must use the given register method.



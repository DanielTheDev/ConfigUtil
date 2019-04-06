# ConfigUtil

**Version** v1.0.1  
**Dependencies** [Bukkit](https://hub.spigotmc.org/javadocs/bukkit/overview-summary.html)


### Example
```java
    public void onEnable() {

        TestConfig config = new TestConfig(this);
        config.loadConfiguration();

    }
```

### How to use
**note** it is important to understand that this util is very different to the others.
The source checker doesn't actually know if the field/method exists so you got to make sure to read the console for error messages.

##### Create Config Value
To create a config value you have to create a 'public static' field in the validator class, it is also important to set the field object to the correct state. also it is required to add the value in the .yml file inside/outside the jar.

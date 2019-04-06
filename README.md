#ConfigUtil

**Version** v1.0.1
**Dependencies** [Bukkit](https://hub.spigotmc.org/javadocs/bukkit/overview-summary.html)


###Example

```java
    public void onEnable() {

        TestConfig config = new TestConfig(this);
        config.loadConfiguration();

    }
```
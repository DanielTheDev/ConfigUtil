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

#### Create Config Value
To create a config value you have to create a 'public static' field in the validator class, it is also important to set the field object to the correct state. also it is required to add the value in the .yml file inside/outside the jar.

```java
/*
    IMPORTANT: A field name will be translated to config code.
    '_' will be translated to '-' in the config.
    '$' will be translated to '.' in the config.

    '_' is used because we cannot use '-' in a field name.
    '$' is to declaire a subvalue. main$submenu -> main.submenu

    Also make sure to use the correct Object Type for every field in the list.
    Every field in the class is a value in the defined configuration file,
    */


    /*
    Using the object as primitive type
    */
    public static boolean enabled;

    /*
    Using the object as wrapper type
    */
    public static Boolean disabled;

    /*
    Making field that does not belong in the config (loader will skip these)
    Notice that "ignore_field" will be seen as "ignore-field"
    */
    @ConfigValueIndicator(type = ConfigValueType.IGNORE)
    public static String ignore_field = "this will not be checked in the config";


    /*
    This field will now be validated by the loader.
    Make sure to create a method in the validator class with the same name as the field.
    And also verify that the access values are "public static ConfigValueValidatorResult {fieldname}(parameter class) {}"
    */
    @ConfigValueIndicator(parameter = Integer.class, type = ConfigValueType.VALIDATE)
    public static int check_field;

    /*
    This field is made for subfields, if you use the '$' as keyword, it will be translated to a '.'.
    Which basically represents the path separator for YAML files.
     */
    public static String subfield$one;


    /*
    This field is made for subfields, if you use the '$' as keyword, it will be translated to a '.'.
    Which basically represents the path separator for YAML files.
    To Validate these fields, you must also copy the name and make a method
    */
    @ConfigValueIndicator(parameter = Integer.class, type = ConfigValueType.VALIDATE)
    public static int subfield$two;

```

#### Create Config Value

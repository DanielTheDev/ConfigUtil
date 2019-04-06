package test;

import com.danielthedev.config.Config;
import com.danielthedev.config.ConfigValueIndicator;
import com.danielthedev.config.ConfigValueType;
import com.danielthedev.config.ConfigValueValidatorResult;
import org.bukkit.plugin.Plugin;

public class TestConfig extends Config {

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

    /*
    Constructor is used to define the config file name, without the .yml extension at the end,
    Also you can choose from empty file, or an existing one from the jar itself.
    As last you have to define the class for the validation methods below.
     */

    public TestConfig(Plugin plugin) {
        super(plugin, "config", true, TestConfig.class);
    }



    /*
    This method has the same name as the field "check_field", and is used to validate the specific input.
    You must make the method 'private static' with 'ConfigValueValidatorResult' as return type, and the type as parameter.
    It is necessary to name the method the same as the field, and with the same type as parameters
    */
    private static ConfigValueValidatorResult check_field(int input) {
        if(input > 0) {
            return ConfigValueValidatorResult.success;
        } else {
            return ConfigValueValidatorResult.error("value must be positive");
        }
    }

    /*
    This method has the same name as the field "subfield$two", and is used to validate the specific input.
    You must make the method 'private static' with 'ConfigValueValidatorResult' as return type, and the type as parameter.
    It is necessary to name the method the same as the field, and with the same type as parameters
    This method is made for subfields, if you use the '$' as keyword, it will be translated to a '.'.
    */
    private static ConfigValueValidatorResult subfield$two(int input) {
        if(input < 0) {
            return ConfigValueValidatorResult.success;
        } else {
            return ConfigValueValidatorResult.error("value must be negative");
        }
    }
}

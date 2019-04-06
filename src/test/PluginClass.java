package test;

import com.danielthedev.config.ConfigParseException;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginClass extends JavaPlugin {

    @Override
    public void onEnable() {
        try {
            TestConfig config =  new TestConfig(this);
            config.loadConfiguration();

            System.out.println(TestConfig.check_field);
            System.out.println(TestConfig.disabled);
            System.out.println(TestConfig.ignore_field);
            System.out.println(TestConfig.enabled);
            System.out.println(TestConfig.subfield$one);
            System.out.println(TestConfig.subfield$two);

            /*
            [22:11:53 INFO]: 1
            [22:11:53 INFO]: false
            [22:11:53 INFO]: this will not be checked in the config
            [22:11:53 INFO]: true
            [22:11:53 INFO]: hey
            [22:11:53 INFO]: -28
            */
        } catch (ConfigParseException e) {
            e.printStackTrace();
        }
    }
}

package com.player.config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.ConfigFactory;

@Config.Sources("classpath:app.properties")
public interface AppConfig extends Config {

    @Key("base.url")
    @DefaultValue("http://3.68.165.45")
    String baseUrl();

    @Key("thread.count")
    @DefaultValue("3")
    int threadCount();

    @Key("default.editor")
    @DefaultValue("supervisor")
    String defaultEditor();

    @Key("admin.editor")
    @DefaultValue("admin")
    String adminEditor();

    static AppConfig getInstance() {
        return ConfigFactory.create(AppConfig.class, System.getProperties());
    }
}
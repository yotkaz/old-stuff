package yotkaz.mas.mp.emanalea.app.story;

import yotkaz.mas.mp.emanalea.app.conf.Configuration;

/**
 * Created on 12.04.16.
 */
public abstract class AbstractStory implements Story {

    private Configuration configuration;

    public AbstractStory(Configuration configuration) {
        setConfiguration(configuration);
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

}

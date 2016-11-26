package yotkaz.mas.mp.emanalea.app;

import yotkaz.mas.mp.emanalea.app.conf.Configuration;
import yotkaz.mas.mp.emanalea.app.conf.impl.BaseHardcodedConfiguration;
import yotkaz.mas.mp.emanalea.app.story.Story;
import yotkaz.mas.mp.emanalea.app.story.impl.StoryMAS1;
import yotkaz.mas.mp.emanalea.app.story.impl.StoryMAS2;
import yotkaz.mas.mp.emanalea.app.story.impl.StoryMAS3;

import java.util.HashMap;

/**
 * Created on 03.04.16.
 */
public class App {

    public static HashMap<String, Story> stories = new HashMap<>();

    public static void main(String[] args) throws Exception {

        Configuration conf = new BaseHardcodedConfiguration();
        prepareStories(conf);

        if (args.length < 1) {
            playStory(new StoryMAS3(conf)); // default story
        } else {
            for (String storyName : args) {
                Story story = stories.get(storyName);
                if (story == null) {
                    throw new IllegalArgumentException("Cannot find story [" + storyName + "]");
                }
                playStory(story);
            }
        }

    }

    private static void playStory(Story story) throws Exception {
        story.action();
    }

    private static void prepareStories(Configuration configuration) throws Exception {
        // StoryMAS1
        Story storyMAS1 = new StoryMAS1(configuration);
        stories.put("1", storyMAS1);
        stories.put("mp1", storyMAS1);

        // StoryMAS2
        Story storyMAS2 = new StoryMAS2(configuration);
        stories.put("2", storyMAS2);
        stories.put("mp2", storyMAS2);

        // StoryMAS3
        Story storyMAS3 = new StoryMAS3(configuration);
        stories.put("3", storyMAS3);
        stories.put("mp3", storyMAS3);
    }

}

package fromtwitter;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class GetConfiguration {
	public Twitter getNewInstance(){
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		.setOAuthConsumerKey("WxxytJTYUT66B1HxRESpJ4Mwe")
		.setOAuthConsumerSecret("hlUHj7fgUN6yHV27NQoVkP0s32hXNucKk6ksj1ixxTL7aFvvcl")
		.setOAuthAccessToken("2796446069-Ulq13EoBxXcrk3b8ToRbZCSxk446nBpN9CtOcMA")
		.setOAuthAccessTokenSecret("JKLxl14oQ5TfWERmrK9Y3EOtN6nbjoMP0d6GX1gatT6Px");
		TwitterFactory tf = new TwitterFactory(cb.build());
		//Twitter twitter = tf.getSingleton();
		Twitter twitter = tf.getInstance();
		return twitter;
	}
}



package fromtwitter;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import fromtwitter.GetConfiguration;
import twitter4j.IDs;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

/*
 * getUserTimeline(user)
 * get users tweets
 */
public class GetUserTimeline{
	public static void main(String[] args)throws TwitterException, IOException {
		GetConfiguration conf = new GetConfiguration();
		Twitter twitter = conf.getNewInstance();
		List<Status> statuses;
		String user = "FIFAWorldCup";
		Paging page = new Paging();
		page.count(10);
		statuses = twitter.getUserTimeline(user, page);
		User target = twitter.showUser(user);
		List<User> followers = twitter.getFollowersList(user, -1L);
		System.out.println(target.getFollowersCount());
		IDs ids = twitter.getFollowersIDs("FIFAWorldCup",-1);
//		for(long id : ids.getIDs()) {
//			System.out.println(twitter.showUser(id));
//		}
		System.out.println("Showing@"+ user + "'s user timeline.");
		for (Status status : statuses) {
			//status.getRetweetCount() 转推的数�?
			// status.getFavoriteCount() 点赞次数
			// status.getSource() 发布的客户端类型
			// status.getCreatedAt() 发布时间
			// status.getGeoLocation() 地点
			// status.getId() 获取该条tweet Id
			String content = status.getText(); //tweet内容
			String ScreenName = status.getUser().getScreenName();
			Date publishDate = status.getCreatedAt();
			System.out.println("@"+ ScreenName +"--"+ content + "--"+publishDate);
			//System.out.println(status.getUser().getName() + ":" +
			// status.getText());
			String tweetUrl = "https://twitter.com/"+ status.getUser().getScreenName() + "/status/"+ status.getId();
			System.out.println("tweetUrl:"+ tweetUrl);
			if (status.getMediaEntities() !=null && status.getMediaEntities().length>=1) {
				try {
					String type =status.getMediaEntities()[0].getType();
					if (type.equals("photo")) {
						String imgUrl =status.getMediaEntities()[0].getMediaURL();
						System.out.println("imgUrl:"+ imgUrl);
					} else if (type.equals("video")) {
						String videoUrl =status.getMediaEntities()[0].getMediaURL();
						System.out.println("videoUrl:"+ videoUrl);
					} else {
						String animatedGifUrl =status.getMediaEntities()[0].getMediaURL();
						System.out.println("animatedGifUrl:"+ animatedGifUrl);
					}

				} catch(Exception e) {
					System.out.println("Status:"+ status);
					System.out.println(e.getStackTrace());
				}
			}
			if (status.getRetweetedStatus() !=null) {
				// 如果这条消息是转发的
				//System.out.println("sourceTweet:"+status.getRetweetedStatus());
				//System.out.println(tweetUrl);
				String reScreenName = status.getRetweetedStatus().getUser().getScreenName();
				Long RetweetedId =status.getRetweetedStatus().getId();
				//System.out.println("getMediaEntities:"+status.getRetweetedStatus().getUser().getScreenName());
				//System.out.println("getMediaEntities:"+status.getRetweetedStatus().getId());
				String retweetUrl = "https://twitter.com/"+ reScreenName
						+ "/status/" + RetweetedId;
				System.out.println("retweetUrl:"+retweetUrl);
			}
		}
	}
}


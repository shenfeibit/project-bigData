package fromtwitter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import fromtwitter.GetConfiguration;
import twitter4j.IDs;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.UserMentionEntity;

/*
 * getUserTimeline(user)
 * get users tweets
 */
public class Serialisation{
	
		static void sleep(long ms) {
			try { Thread.sleep(ms); }
			catch(InterruptedException ex) { Thread.currentThread().interrupt(); }
	    }
		public static String convertir(String contenu) {
			String res = "";
			String[] listC = contenu.split("");
			for(String str: listC) {
				if (str.equals("'")) {
					str = "''";
				}
				res+=str;
			}
			return res;
		}

		static GetConfiguration conf = new GetConfiguration();
		static Twitter twitter = conf.getNewInstance();
		static String user = "ChampionsLeague";
		static Paging page = new Paging();
		static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		public static String stat() throws TwitterException {
			String stat = "";
			User target = twitter.showUser(user);
			List<Status> status;
			page.count(1);
			status = twitter.getUserTimeline(user, page);
			stat += "(screenname,name,description,location,createdDate,favorites,followers,friends,tweets,geolocation)"
					+ " values('" + target.getName() + "','" 
					+ target.getScreenName() + "','"
					+ target.getDescription() + "','"
					+ target.getLocation() + "','"
					+ sdf.format(target.getCreatedAt()) +"',"
					+ target.getFavouritesCount() + ","
					+ target.getFollowersCount() + ","
					+ target.getFriendsCount() + ","
					+ target.getStatusesCount() + ",'";
			if(status.get(0).getGeoLocation()!=null) {
					stat += status.get(0).getGeoLocation().getLatitude()+"+"
					+ status.get(0).getGeoLocation().getLongitude();
					}
					stat += "')";
			return stat;
		}
		
		public static ArrayList<String> followers() throws TwitterException{
			ArrayList<String> followers = new ArrayList<String>();
			IDs ids = twitter.getFollowersIDs("ChampionsLeague",-1);
			for(long id : ids.getIDs()) {
				List<Status> status;
				page.count(1);
				status = twitter.getUserTimeline(user, page);
				String cql1 = "";
				User user = twitter.showUser(id);
				cql1 += "(id,screenname,name,creatDate,nbTweets,nbFolowers,location,geo)";
				String cql = cql1 + " values('"
							+ id + "','"
							+ user.getScreenName() + "','"
							+ user.getName() + "','"
							+ sdf.format(user.getCreatedAt()) + "',"
							+ user.getStatusesCount() + ","
							+ user.getFollowersCount() + ",'" 
							+ user.getLocation()+"','";
					if(status.get(0).getGeoLocation()!=null) {
					cql += status.get(0).getGeoLocation().getLatitude()+"+"
					+ status.get(0).getGeoLocation().getLongitude();
					}							
							cql+="')";
					System.out.println(cql);
				followers.add(cql);
				sleep(1000);
			}
			return followers;
		}
		
		public static List<String> tweets() throws TwitterException{
			List<String> tweets = new ArrayList<String>();
			List<Status> statuses;
			page.count(200);
			statuses = twitter.getUserTimeline(user, page);
			for(Status s : statuses) {
				String cql1 = "";
				String cql2 = "";
				String cql3 = "";
				String cql4 = "";
				String cql_mentions = "";
				cql1 += "(idTweet,creatDate,nbFavoris,nbRetweet,contenu";
				cql2 += " values('"
						+ s.getId() + "','"
						+ sdf.format(s.getCreatedAt()) + "',"
						+ s.getFavoriteCount() + ","
						+ s.getRetweetCount() + ",'"
						+ convertir(s.getText()) + "'";
				if (s.getMediaEntities() !=null && s.getMediaEntities().length>=1) {
					try {
						String type =s.getMediaEntities()[0].getType();
						if (type.equals("photo")) {
							cql3 = ",'" + s.getMediaEntities()[0].getMediaURL() + "'";
							cql1 += ",imgUrl";
						} else if (type.equals("video")) {
							cql3 = ",'" + s.getMediaEntities()[0].getMediaURL() + "'";
							cql1 += ",videoUrl";
						} else {
							cql3 = ",'" + s.getMediaEntities()[0].getMediaURL() + "'";
							cql1 += ",gifUrl";
						}

					} catch(Exception e) {
						System.out.println("Status:"+ s);
						System.out.println(e.getStackTrace());
					}
				}
				if (s.getRetweetedStatus() !=null) {
					// if this tweet is retweeted
					String reScreenName = s.getRetweetedStatus().getUser().getScreenName();
					Long RetweetedId =s.getRetweetedStatus().getId();
					String retweetUrl = "https://twitter.com/"+ reScreenName
							+ "/status/" + RetweetedId;
					cql1 += ",retweetUrl";
					cql4 += ",'"+retweetUrl+"'";
				}
				
				UserMentionEntity[] userMentions;
				userMentions = s.getUserMentionEntities();
				if (userMentions.length>0) {
					cql1+=", userMention";
					cql_mentions += ", [";
					for(int i = 0; i < userMentions.length - 1; i++) {
						cql_mentions += "'"+userMentions[i].getScreenName() + "',";
					}
					cql_mentions +="'" +userMentions[userMentions.length-1].getScreenName() + "']";
					
				}
				String cql = cql1 + ")" + cql2 + cql3 + cql4 + cql_mentions +")";
				tweets.add(cql);
				
				sleep(1000);
			}
			return tweets;
		}

		public static UserMentionEntity[] userMention(Status tweet){
			UserMentionEntity[] userMentions;
			userMentions = tweet.getUserMentionEntities();
			return userMentions;
		}
}


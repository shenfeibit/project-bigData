package com.flx.test;

import java.util.ArrayList;
import java.util.List;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.SocketOptions;

import fromtwitter.Serialisation;

/** 
 * @author zxb 2014年12月29日 下午3:46:23
 */
public class CassandraTest {

    public static void main(String[] args) {
    	   		
    		Cluster cluster = null;
    		Session session = null;
    		
    		try {
    			//definir cluster
    			cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
    			//get objet session
    			session = cluster.connect();
    			//create keyspace   if not exists
    			String createKeySpaceCQL =
    					"create keyspace if not exists keyspaceUEFA with "
    					+ "replication={'class':'SimpleStrategy','replication_factor':1}";
    			session.execute(createKeySpaceCQL);
    			//create table stat
    			String createTableStat ="create table if not exists keyspaceUEFA.stat(screenname varchar primary key,"
    					+ "name varchar,description varchar,location varchar,"
    					+ "createdDate timestamp,favorites int,followers int,friends int,tweets int,geolocation varchar)";
    					//"create table if not exists keyspaceUEFA.student(name varchar primary key, age int)";
   					
    			session.execute(createTableStat);
    			//insert into stat table
    			String stat = "insert into keyspaceUEFA.stat";
    			stat += Serialisation.stat();
//    			String insertCQL = "insert into keyspaceUEFA.student(name,age) values('zhang',null)";
    			session.execute(stat);
    			//query stat
//    			String queryStat =
//    					"select * from keyspaceUEFA.stat";
//    			ResultSet rs = session.execute(queryStat);
//    			List<Row> dataList = rs.all();
//    			for (Row row : dataList) {
//    				System.out.println("------name: " + row.getString("screenname"));
//    				System.out.println("------geo: " + row.getString("geolocation"));
//    				System.out.println("------date: " + row.getTimestamp("createdDate"));
//    			}
    						
    			
    			//create table follower
    			session.execute("drop table if exists keyspaceUEFA.follower");
    			String createTableFollower ="create table if not exists keyspaceUEFA.follower(id varchar primary key,screenname varchar,"
    					+ "name varchar,creatDate timestamp,nbTweets int,nbFolowers int,location varchar,geo varchar)";
    					//"create table if not exists keyspaceUEFA.student(name varchar primary key, age int)";//    					
    			session.execute(createTableFollower);
    			//insert into follower
    			System.out.println("start insert follower");
    			ArrayList<String> listF = Serialisation.followers();
    			System.out.println("finish recuperer follower");
    			for (int i=0;i<10;i++) {
	    			String follower = "insert into keyspaceUEFA.follower";
	    			follower += listF.get(i);
	    			System.out.println(follower);
	    			session.execute(follower);
    			}
//    			query followers
//    			System.out.println("start query follower");
//	    			String queryFollower =
//	    					"select * from keyspaceUEFA.follower";
//	    			ResultSet rsf = session.execute(queryFollower);
//	    			System.out.println("start query follower");
//	    			List<Row> dataListF = rsf.all();
//	    			System.out.println("start query follower");
//	    			for (Row row : dataListF) {
//	    				System.out.println("start query follower");
//	    				System.out.println("------id: " + row.getString("id"));
//	    				System.out.println("------geo: " + row.getString("geo"));
//	    				System.out.println("------date: " + row.getTimestamp("creatDate"));
//	    			}
    			
    			//create table tweet
    			String drop="drop table if exists keyspaceUEFA.tweet";
    			session.execute(drop);
    			String createTableTweet ="create table if not exists keyspaceUEFA.tweet(idTweet varchar primary key,"
    					+ "creatDate timestamp, nbFavoris int, nbRetweet int, contenu varchar, retweet varchar,"
    					+ "imgUrl varchar, ,videoUrl varchar, gifUrl varchar,retweetUrl varchar, userMention list<varchar>)";
    			session.execute(createTableTweet);
    			String w = "what's";
    			System.out.println(Serialisation.convertir(w));
    			String test = "insert into keyspaceUEFA.tweet (idTweet) values ('"+Serialisation.convertir(w)+"')";
    			session.execute(test);
    			String queryTest =
    					"select * from keyspaceUEFA.tweet";
    			ResultSet rss = session.execute(queryTest);
    			List<Row> data = rss.all();
    			for (Row row : data) {
    				System.out.println("------name: " + row.getString("idTweet"));
    				}
    			
    			List<String> insertT = Serialisation.tweets();
    			for (String insert : insertT) {
	    			//insert into tweet table
	    			String tweet = "insert into keyspaceUEFA.tweet";
	    			tweet += insert;
	    			session.execute(tweet);
    			}
//    			//query tweet
////    			System.out.println("start query");
//	    			String queryTweet =
//	    					"select * from keyspaceUEFA.tweet";
//	    			ResultSet rst = session.execute(queryTweet);
//	    			List<Row> dataListT = rst.all();
//	    			
//	    			for (Row row : dataListT) {
////	    				System.out.println("------id: " + row.getString("idTweet"));
////	    				System.out.println("------favoris: " + row.getInt("nbFavoris"));
//	    				List<String> l = row.getList("userMention",String.class);
//	    				for(String s : l) {
////	    					System.out.println("------userMention" + s);
//	    				}	    				
////	    				System.out.println("------date: " + row.getTimestamp("creatDate"));
//	    			}
    			
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}

}
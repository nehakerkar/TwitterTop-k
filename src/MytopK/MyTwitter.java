package MytopK;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

/*
	@author: Neha Kerkar
*/
public class MyTwitter {
		private static String twitter_consumer_key;
		private static String twitter_consumer_secret;
		private static String twitter_access_token;
		private static String twitter_access_secret;
		
		private static int k=5;
		private static int time = 10;
		private static Map<String,Integer> kmap = new LinkedHashMap<String,Integer>();
		static int k_count=0;
		static int time_count=0;
	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Please enter Twitter Consumer Key: ");
		twitter_consumer_key = sc.nextLine();
		System.out.println("Please enter Twitter Consumer Secret: ");
		twitter_consumer_secret = sc.nextLine();
		System.out.println("Please enter Twitter Access Token: ");
		twitter_access_token = sc.nextLine();
		System.out.println("Please enter Twitter Access Secret: ");
		twitter_access_secret = sc.nextLine();
		System.out.println("Please enter value for K: ");
		k = sc.nextInt();
		System.out.println("Please enter value for sliding window time (# of tweets in a window): ");
		time=sc.nextInt();
		
		System.out.println("Top K hashtags:");
		System.out.println("Value of k: "+k);
		System.out.println("Value of time for sliding window: "+time);
		System.out.println();
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey(twitter_consumer_key)
		  .setOAuthConsumerSecret(twitter_consumer_secret)
		  .setOAuthAccessToken(twitter_access_token)
		  .setOAuthAccessTokenSecret(twitter_access_secret);
		
	    StatusListener listener = new StatusListener() {
//            @Override
            public void onStatus(Status status) {
            	time_count++;
            	//System.out.println(time_count);
            	//System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
            	if(time_count==time)
    	        {
    	        	time_count=0;
    	        	k_count=0;
    	        	System.out.println("----Sliding Window Starts----");
    	        	System.out.println("Hashtags and their counts in this window: "+kmap);
    	        	kmap=sortByValue(kmap);
    	        	System.out.println("Top "+k+" hashtag entries in this window: ");
    	        	Iterator<Map.Entry<String, Integer>> it = kmap.entrySet().iterator();
    	    		while(k_count++<k && it.hasNext())
    	    		{
    	    			System.out.println(it.next());
    	    		}
    	        	System.out.println("----Sliding Window Ends----");
    	        	System.out.println();
    	        	kmap = new LinkedHashMap<String,Integer>();
    	        }
            	if(status.getLang().equalsIgnoreCase("en")){
            	    for (HashtagEntity hash : status.getHashtagEntities()) {
            	       String myhash = hash.getText().toLowerCase();
            	    	if(kmap.containsKey(myhash))
            	    		kmap.put(myhash, kmap.get(myhash)+1);
            	    	else
            	    		kmap.put(myhash, 1);
            	    }
            	}
            }

//            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                //System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
            }

//            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                //System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
            }

 //           @Override
            public void onScrubGeo(long userId, long upToStatusId) {
                //System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
            }

 //           @Override
            public void onStallWarning(StallWarning warning) {
                //System.out.println("Got stall warning:" + warning);
            }

 //           @Override
            public void onException(Exception ex) {
                ex.printStackTrace();
            }
        };
        try{
		TwitterStreamFactory tsf = new TwitterStreamFactory(cb.build());
	    TwitterStream twitterStream = tsf.getInstance();        
	    twitterStream.addListener(listener);
	    twitterStream.sample();
		}catch(Exception e){}
	   
	}
	
	private static Map<String, Integer> sortByValue(Map<String, Integer> unsortMap)
    {

        List<Entry<String, Integer>> list = new LinkedList<Entry<String, Integer>>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Entry<String, Integer>>()
        {
            public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2)
            {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        // Maintaining insertion order with the help of LinkedHashMap
        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        for (Entry<String, Integer> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

}

/*
 OUTPUT:
 Top K hashtags:
Value of k: 5
Value of time for sliding window: 10
----Sliding Window Starts----
Hashtags and their counts in this window: {followtrick=2, followback=1, votadulcemaria=1, follow=3, yitikcennetimiz=1, myfirsttweet=1, ipadgames=1}
Top 5 hashtag entries in this window: 
follow=3
followtrick=2
followback=1
votadulcemaria=1
yitikcennetimiz=1
----Sliding Window Ends----
*/

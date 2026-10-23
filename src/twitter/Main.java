/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;
import java.util.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This is the main program.
 */
public class Main {

    /**
     * File containing the sample tweets to read.
     */
    public static final String SAMPLE_FILE = "tweets.txt";

    /**
     * Main method of the program. Reads sample tweets and prints facts about them.
     * 
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        final List<Tweet> tweets;
        try {
            tweets = TweetReader.readTweetsFromFile(SAMPLE_FILE);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }

        // timespan
        final Timespan span = Extract.getTimespan(tweets);
        System.out.println("read " + tweets.size() + " tweets spanning ["
                + span.getStart() + "..." + span.getEnd() + "]");

        // mentioned users
        final Set<String> mentionedUsers = Extract.getMentionedUsers(tweets);
        System.out.println("mentioned users: " + mentionedUsers);
        System.out.println();

        // tweets written by alyssa
        final List<Tweet> alyssaTweets = Filter.writtenBy(tweets, "alyssa");
        System.out.println("tweets written by alyssa:");
        for (Tweet t : alyssaTweets) {
            System.out.println("(" + t.getId() + " " + t.getTimestamp() + " "
                    + t.getAuthor() + ") " + t.getText());
        }
        System.out.println();

        // tweets containing "rivest" or "pset"
        final List<Tweet> psetTweets =
                Filter.containing(tweets, Arrays.asList("rivest", "pset"));
        System.out.println("tweets containing 'rivest' or 'pset':");
        for (Tweet t : psetTweets) {
            System.out.println("(" + t.getId() + " " + t.getTimestamp() + " "
                    + t.getAuthor() + ") " + t.getText());
        }
        System.out.println("\n socail network test");
        Map<String, Set<String>> graph = SocialNetwork.guessFollowsGraph(tweets);
        System.out.println("Influencers: " + SocialNetwork.influencers(graph));
    }
    
   
}
/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import java.util.List;
import java.util.ArrayList;

/**
 * Filter consists of methods that filter a list of tweets for those matching a
 * condition.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class Filter {

	/*
	 * Test plan for writtenBy(tweets, username):
	 * how many tweets: 0, 1, more than 1
	 * how many tweets by that username: 0, 1, more than 1
	 * case of username: same as author, different from author
	 *
	 * Test plan for inTimespan(tweets, timespan):
	 * how many tweets: 0, more than 1
	 * tweets inside the timespan: none, some, all
	 * tweet exactly on the start or end of the timespan: yes, no
	 *
	 * Test plan for containing(tweets, words):
	 * how many words: 1, more than 1
	 * matching tweets: none, some, all
	 * case of the word: same as in tweet, different
	 * word appears only as part of a bigger word: yes, no
	 *
	 * Every result is also checked for keeping the input order.
	 */
	public static List<Tweet> writtenBy(List<Tweet> tweets, String username) {
		List<Tweet> result = new ArrayList<>();
		for (Tweet tweet : tweets) {
		if (tweet.getAuthor().equalsIgnoreCase(username)) {
		result.add(tweet);
		}
		}
		return result;
		}

    /**
     * Find tweets that were sent during a particular timespan.
     * 
     * @param tweets
     *            a list of tweets with distinct ids, not modified by this method.
     * @param timespan
     *            timespan
     * @return all and only the tweets in the list that were sent during the timespan,
     *         in the same order as in the input list.
     */
	public static List<Tweet> inTimespan(List<Tweet> tweets, Timespan timespan) {
		List<Tweet> result = new ArrayList<>();
		for (Tweet tweet : tweets) {
		// a Timespan includes its endpoints
		if (!tweet.getTimestamp().isBefore(timespan.getStart())
		&& !tweet.getTimestamp().isAfter(timespan.getEnd())) {
		result.add(tweet);
		}
		}
		return result;
		}

    /**
     * Find tweets that contain certain words.
     * 
     * @param tweets
     *            a list of tweets with distinct ids, not modified by this method.
     * @param words
     *            a list of words to search for in the tweets. 
     *            A word is a nonempty sequence of nonspace characters.
     * @return all and only the tweets in the list such that the tweet text (when 
     *         represented as a sequence of nonempty words bounded by space characters 
     *         and the ends of the string) includes *at least one* of the words 
     *         found in the words list. Word comparison is not case-sensitive,
     *         so "Obama" is the same as "obama".  The returned tweets are in the
     *         same order as in the input list.
     */
	public static List<Tweet> containing(List<Tweet> tweets, List<String> words) {
		List<Tweet> result = new ArrayList<>();
		for (Tweet tweet : tweets) {
		if (hasAnyWord(tweet.getText(), words)) {
		result.add(tweet);
		}
		}
		return result;
		}
		private static boolean hasAnyWord(String text, List<String> words) {
		for (String tweetWord : text.split(" ")) {
		for (String word : words) {
		if (!tweetWord.isEmpty() && tweetWord.equalsIgnoreCase(word)) {
		return true;
		}
		}
		}
		return false;
		}

}

/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import java.util.List;

import java.util.Set;
import java.time.Instant;
import java.util.HashSet;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Extract consists of methods that extract information from a list of tweets.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class Extract {

    /**
     * Get the time period spanned by tweets.
     * 
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @return a minimum-length time interval that contains the timestamp of
     *         every tweet in the list.
     */
	public static Timespan getTimespan(List<Tweet> tweets) {
		if (tweets.isEmpty()) {
			// the spec does not say what to return here, so any timespan is fine
			return new Timespan(Instant.EPOCH, Instant.EPOCH);
		}
		Instant start = tweets.get(0).getTimestamp();
		Instant end = start;
		for (Tweet tweet : tweets) {
			Instant time = tweet.getTimestamp();
			if (time.isBefore(start)) {
				start = time;
			}
			if (time.isAfter(end)) {
				end = time;
			}
		}
		return new Timespan(start, end);
	}

    /**
     * Get usernames mentioned in a list of tweets.
     * 
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @return the set of usernames who are mentioned in the text of the tweets.
     *         A username-mention is "@" followed by a Twitter username (as
     *         defined by Tweet.getAuthor()'s spec).
     *         The username-mention cannot be immediately preceded or followed by any
     *         character valid in a Twitter username.
     *         For this reason, an email address like bitdiddle@mit.edu does NOT 
     *         contain a mention of the username mit.
     *         Twitter usernames are case-insensitive, and the returned set may
     *         include a username at most once.
     */
    /**
     * Get the usernames mentioned in a list of tweets.
     * 
     * @param tweets
     *            a list of tweets
     * @return a set of usernames that are mentioned in the tweets, without the @ symbol
     */
    public static Set<String> getMentionedUsers(List<Tweet> tweets) {
        Set<String> mentionedUsers = new HashSet<>();
        
        for (Tweet tweet : tweets) {
            String text = tweet.getText();
            
            // Regex explanation:
            // (?<=^|\\s) : Lookbehind to ensure the @ is at the start of the string OR preceded by a space.
            // @          : Matches the @ symbol
            // ([a-zA-Z0-9_-]+) : Captures the username characters
            Pattern pattern = Pattern.compile("(?<=^|\\s)@([a-zA-Z0-9_-]+)");
            Matcher matcher = pattern.matcher(text);
            
            while (matcher.find()) {
                String username = matcher.group(1);
                mentionedUsers.add(username.toLowerCase(Locale.ROOT));
            }
        }
        return mentionedUsers;
    }
	// "@" + username, not preceded by a username character, and not followed
	// by one either
	private static final Pattern MENTION = Pattern.compile("(?<![A-Za-z0-9_])@([A-Za-z0-9_]+)(?![A-Za-z0-9_])");

}
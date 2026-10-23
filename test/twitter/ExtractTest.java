/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;


import java.time.Instant;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;

import org.junit.Test;

public class ExtractTest {

	/*
	 * Testing strategy for getTimespan(tweets):
	 * number of tweets: 1, > 1
	 * order of tweets in the list: in time order, not in time order
	 * timestamps: all the same, some different
	 * (empty list is not tested: the spec does not say what to return)
	 *
	 * Testing strategy for getMentionedUsers(tweets):
	 * number of tweets: 1, > 1
	 * mentions in a tweet: 0, 1, > 1
	 * same user mentioned: once, more than once with different case
	 * position of mention: start of text, middle, end
	 * "@" preceded by a username character (email address): yes, no
	 * mention followed by punctuation: yes, no
	 *
	 * The returned set may use any case for a username, so the tests
	 * compare usernames after converting them to lowercase.
	 */
    
	private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
	private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
	private static final Instant d3 = Instant.parse("2016-02-17T12:30:00Z");
	private static final Tweet tweet1 =
	new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
	private static final Tweet tweet2 =
	new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
	private static final Tweet tweet3 =
	new Tweet(3, "alyssa", "@bbitdiddle saving you a seat, ask @Evaluator too", d3);
	private static final Tweet tweet4 =
	new Tweet(4, "evaluator", "thanks @BBitDiddle! mail me at eval@mit.edu", d2);

    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testGetTimespanTwoTweets() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2));
        
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d2, timespan.getEnd());
    }
    
    @Test
    public void testGetMentionedUsersNoMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1));
        
        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }
    
 // covers: > 1 tweet, not in time order
    @Test
    public void testGetTimespanUnorderedTweets() {
    Timespan timespan = Extract.getTimespan(Arrays.asList(tweet3, tweet1, tweet2));
    assertEquals("expected start", d1, timespan.getStart());
    assertEquals("expected end", d3, timespan.getEnd());
    }
    // covers: "@" inside an email address is not a mention
    @Test
    public void testGetMentionedUsersEmailIsNotMention() {
    Set<String> mentionedUsers =
    toLowerCase(Extract.getMentionedUsers(Arrays.asList(tweet4)));
    assertFalse("mit is not mentioned", mentionedUsers.contains("mit"));
    assertEquals("expected one user", new HashSet<>(Arrays.asList("bbitdiddle")),
    mentionedUsers);
    }
    // covers: > 1 tweets, same user mentioned with different case
    @Test
    public void testGetMentionedUsersDifferentCase() {
    Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet3,
    tweet4));
    assertEquals("expected each user once", 2, mentionedUsers.size());
    assertEquals("expected two users", new HashSet<>(Arrays.asList("bbitdiddle",
    "evaluator")),
    toLowerCase(mentionedUsers));
    }
    // usernames are case-insensitive, so compare them in lowercase
    private static Set<String> toLowerCase(Set<String> usernames) {
    Set<String> result = new HashSet<>();
    for (String username : usernames) {
    result.add(username.toLowerCase());
    }
    return result;
    }

    /*
     * Warning: all the tests you write here must be runnable against any
     * Extract class that follows the spec. It will be run against several staff
     * implementations of Extract, which will be done by overwriting
     * (temporarily) your version of Extract with the staff's version.
     * DO NOT strengthen the spec of Extract or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in Extract, because that means you're testing a
     * stronger spec than Extract says. If you need such helper methods, define
     * them in a different class. If you only need them in this test class, then
     * keep them in this test class.
     */

}

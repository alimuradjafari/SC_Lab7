/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class FilterTest {

    /*
     * TODO: your testing strategies for these methods should go here.
     * See the ic03-testing exercise for examples of what a testing strategy comment looks like.
     * Make sure you have partitions.
     */
    
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-02-17T12:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "Alyssa", "Talking about the psettonight", d3);
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
 // covers: > 1 tweets by username, author written in a different case
    @Test
    public void testWrittenByDifferentCaseMultipleResults() {
    List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2, tweet3),
    "ALYSSA");
    assertEquals("expected two tweets in order", Arrays.asList(tweet1, tweet3),
    writtenBy);
    }
    // covers: tweets exactly on start and end are included, one tweet outside
    @Test
    public void testInTimespanEndpointsIncluded() {
    List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2, tweet3),
    new Timespan(d2, d3));
    assertEquals("expected tweets on the endpoints", Arrays.asList(tweet2, tweet3),
    inTimespan);
    }
    // covers: different case, word only inside a bigger word ("Talking" is not "talk")
    @Test
    public void testContainingCaseAndWholeWords() {
    List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2, tweet3),
    Arrays.asList("TALK"));
    assertEquals("expected tweets 1 and 2 only", Arrays.asList(tweet1, tweet2),
    containing);
    }

    /*
     * Warning: all the tests you write here must be runnable against any Filter
     * class that follows the spec. It will be run against several staff
     * implementations of Filter, which will be done by overwriting
     * (temporarily) your version of Filter with the staff's version.
     * DO NOT strengthen the spec of Filter or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in Filter, because that means you're testing a stronger
     * spec than Filter says. If you need such helper methods, define them in a
     * different class. If you only need them in this test class, then keep them
     * in this test class.
     */

}

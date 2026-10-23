package twitter;

import static org.junit.Assert.*;
import java.util.*;
import java.time.Instant;
import org.junit.Test;

public class SocialNetworkTest {

    /*
     * TESTING STRATEGY (Partitions):
     * 
     * guessFollowsGraph(tweets):
     * - Number of tweets: 0, 1, > 1
     * - Mentions in a tweet: 0, 1, > 1
     * - Tweets by same author: 1, > 1
     * - Case of username: same, different
     * 
     * influencers(followsGraph):
     * - Users in graph: 0, 1, > 1
     * - Followers of top user: 0, 1, > 1
     * - Follower counts: all different, tied
     */

    // Helper method to check if A follows B, ignoring case
    private boolean follows(Map<String, Set<String>> graph, String user, String followed) {
        for (String key : graph.keySet()) {
            if (key.equalsIgnoreCase(user)) {
                for (String f : graph.get(key)) {
                    if (f.equalsIgnoreCase(followed)) return true;
                }
            }
        }
        return false;
    }

    // 1. Empty list of tweets -> graph is empty
    @Test
    public void testGuessFollowsGraphEmpty() {
        Map<String, Set<String>> graph = SocialNetwork.guessFollowsGraph(new ArrayList<>());
        assertTrue(graph.isEmpty());
    }

    // 2. Tweets without mentions -> nobody follows anybody
    @Test
    public void testGuessFollowsGraphNoMentions() {
        // Using Instant.now() instead of GregorianCalendar
        Tweet t1 = new Tweet(1L, "alyssa", "is it reasonable to talk about rivest so much?", Instant.now());
        Map<String, Set<String>> graph = SocialNetwork.guessFollowsGraph(Arrays.asList(t1));
        // Check that no set contains any usernames
        for (Set<String> follows : graph.values()) {
            assertTrue(follows.isEmpty());
        }
    }

    // 3. Single mention -> author follows that user (different case)
    @Test
    public void testGuessFollowsGraphSingleMention() {
        Tweet t1 = new Tweet(1L, "bbitdiddle", "saving a seat for @Alyssa", Instant.now());
        Map<String, Set<String>> graph = SocialNetwork.guessFollowsGraph(Arrays.asList(t1));
        assertTrue(follows(graph, "bbitdiddle", "alyssa"));
    }

    // 4. Multiple mentions -> author follows all of them
    @Test
    public void testGuessFollowsGraphMultipleMentions() {
        Tweet t1 = new Tweet(1L, "alyssa", "@bbitdiddle and @rivest are here", Instant.now());
        Map<String, Set<String>> graph = SocialNetwork.guessFollowsGraph(Arrays.asList(t1));
        assertTrue(follows(graph, "alyssa", "bbitdiddle"));
        assertTrue(follows(graph, "alyssa", "rivest"));
    }

    // 5. Multiple tweets, one user -> follows from both tweets
    @Test
    public void testGuessFollowsGraphMultipleTweetsOneUser() {
        Tweet t1 = new Tweet(1L, "alyssa", "hey @bbitdiddle", Instant.now());
        Tweet t2 = new Tweet(2L, "alyssa", "hey @rivest", Instant.now());
        Map<String, Set<String>> graph = SocialNetwork.guessFollowsGraph(Arrays.asList(t1, t2));
        assertTrue(follows(graph, "alyssa", "bbitdiddle"));
        assertTrue(follows(graph, "alyssa", "rivest"));
    }

    // 6. Empty graph -> empty list
    @Test
    public void testInfluencersEmpty() {
        Map<String, Set<String>> graph = new HashMap<>();
        List<String> influencers = SocialNetwork.influencers(graph);
        assertTrue(influencers.isEmpty());
    }

    // 7. Single user, no followers -> list = [that user]
    @Test
    public void testInfluencersSingleUserNoFollowers() {
        Map<String, Set<String>> graph = new HashMap<>();
        graph.put("alyssa", new HashSet<>());
        List<String> influencers = SocialNetwork.influencers(graph);
        assertEquals(1, influencers.size());
        assertEquals("alyssa", influencers.get(0).toLowerCase());
    }

    // 8. Single influencer -> top user first
    @Test
    public void testInfluencersSingleInfluencer() {
        Map<String, Set<String>> graph = new HashMap<>();
        graph.put("alyssa", new HashSet<>(Arrays.asList("bbitdiddle")));
        graph.put("bbitdiddle", new HashSet<>());
        List<String> influencers = SocialNetwork.influencers(graph);
        assertEquals("bbitdiddle", influencers.get(0).toLowerCase());
    }

    // 9. Multiple influencers -> order by follower count
    @Test
    public void testInfluencersMultipleInfluencers() {
        Map<String, Set<String>> graph = new HashMap<>();
        graph.put("a", new HashSet<>(Arrays.asList("top")));
        graph.put("b", new HashSet<>(Arrays.asList("top")));
        graph.put("c", new HashSet<>(Arrays.asList("mid")));
        graph.put("top", new HashSet<>());
        graph.put("mid", new HashSet<>());
        
        List<String> influencers = SocialNetwork.influencers(graph);
        assertEquals("top", influencers.get(0).toLowerCase()); // 2 followers
        assertEquals("mid", influencers.get(1).toLowerCase()); // 1 follower
        // a, b, c, top, mid have 0 followers, order doesn't matter for them
    }

    // 10. Tied influence -> either order of the tie
    @Test
    public void testInfluencersTied() {
        Map<String, Set<String>> graph = new HashMap<>();
        graph.put("a", new HashSet<>(Arrays.asList("x")));
        graph.put("b", new HashSet<>(Arrays.asList("y")));
        graph.put("x", new HashSet<>());
        graph.put("y", new HashSet<>());
        
        List<String> influencers = SocialNetwork.influencers(graph);
        // x and y both have 1 follower. Either can be first.
        String first = influencers.get(0).toLowerCase();
        String second = influencers.get(1).toLowerCase();
        assertTrue((first.equals("x") && second.equals("y")) || (first.equals("y") && second.equals("x")));
    }
    
    }

/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Set;

import org.junit.Test;

public class ExtractTest {

    /*
     * TODO: your testing strategies for these methods should go here.
     * See the ic03-testing exercise for examples of what a testing strategy comment looks like.
     * Make sure you have partitions.
     */
    
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    
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
    
    @Test
    public void testGetTimespanSingleTweet() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1));
        assertEquals("expected start and end to be the same for a single tweet", d1, timespan.getStart());
        assertEquals("expected start and end to be the same for a single tweet", d1, timespan.getEnd());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testGetTimespanEmptyTweetList() {
        Extract.getTimespan(Arrays.asList()); // should throw an exception
    }

    @Test
    public void testGetMentionedUsersSingleMention() {
        Tweet tweetWithMention = new Tweet(3, "alyssa", "Hey @bbitdiddle, check this out!", Instant.now());
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweetWithMention));
        assertTrue("expected one mention", mentionedUsers.contains("bbitdiddle"));
        assertEquals("expected 1 user mentioned", 1, mentionedUsers.size());
    }

    @Test
    public void testGetMentionedUsersMultipleMentions() {
        Tweet tweetWithMentions = new Tweet(4, "alyssa", "@bbitdiddle, @alyssa, and @charles are here!", Instant.now());
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweetWithMentions));
        assertTrue("expected mention of bbitdiddle", mentionedUsers.contains("bbitdiddle"));
        assertTrue("expected mention of alyssa", mentionedUsers.contains("alyssa"));
        assertTrue("expected mention of charles", mentionedUsers.contains("charles"));
        assertEquals("expected 3 users mentioned", 3, mentionedUsers.size());
    }

    @Test
    public void testGetMentionedUsersCaseInsensitive() {
        Tweet tweetWithCaseMention = new Tweet(5, "alyssa", "Hello @BBitDiddle!", Instant.now());
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweetWithCaseMention));
        assertTrue("expected mention to be case-insensitive", mentionedUsers.contains("bbitdiddle"));
        assertEquals("expected 1 user mentioned", 1, mentionedUsers.size());
    }

    @Test
    public void testGetMentionedUsersInvalidMention() {
        Tweet tweetWithEmail = new Tweet(6, "alyssa", "Email me at bitdiddle@mit.edu", Instant.now());
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweetWithEmail));
        assertTrue("expected no mention from email address", mentionedUsers.isEmpty());
    }


}

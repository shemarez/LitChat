package tcss450.uw.edu.hitmeupv2;

import org.junit.Before;
import org.junit.Test;

import tcss450.uw.edu.hitmeupv2.WebService.ChatMessage;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ChatMessageUnitTest {
    ChatMessage testMessage;

    @Before
    public void before() {
        testMessage = new ChatMessage();
    }

    @Test
    public void testChatMessageConstructor() throws Exception {
        assertNotNull(testMessage);
    }

    @Test
    public void  testChatMessageSetId() throws Exception {
        testMessage.setId("1");
        assertEquals("1", testMessage.getId());
    }

    @Test
    public void testSetMe() throws Exception {
        testMessage.setMe(true);
        assertTrue(testMessage.getIsme());
    }

    @Test
    public void testChatMessageSetMessage() throws Exception {
        testMessage.setMessage("this is a test");
        assertEquals("this is a test", testMessage.getMessage());
    }

    @Test
    public void testChatMessageSetSenderId() throws Exception {
        testMessage.setSenderId("3");
        assertEquals("3", testMessage.getSenderId());
    }

    @Test
    public void testSetDate() throws Exception {
        testMessage.setDate("2017-05-28 14:55:58");
        assertEquals("2017-05-28 14:55:58", testMessage.getDate());
    }

    @Test
    public void testSetMonthDay() throws Exception {
        testMessage.setMonthDay("1");
        assertEquals("1", testMessage.getMonthDay());
    }

    @Test
    public void testSetRecipientTyping() throws Exception {
        testMessage.setRecipientTyping(true);
        assertTrue(testMessage.getRecipientTyping());
    }

    @Test
    public void testSetPhotoMsg() throws Exception {
        testMessage.setPhotoMsg(true);
        assertTrue(testMessage.isPhotoMsg());
    }

    @Test
    public void testSetPhotoSrc() throws Exception {
        testMessage.setPhotoSrc("/path");
        assertEquals("/path", testMessage.getPhotoSrc());
    }

}
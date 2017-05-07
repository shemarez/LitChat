package tcss450.uw.edu.hitmeupv2.Chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class ChatContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<ChatItem> ITEMS = new ArrayList<ChatItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, ChatItem> ITEM_MAP = new HashMap<String, ChatItem>();

    private static final int COUNT = 5;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(ChatItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static ChatItem createDummyItem(int position) {
        return new ChatItem(String.valueOf(position), "FirstName LastName ", makeDetails(position));
    }

    private static String makeDetails(int position) {
//        StringBuilder builder = new StringBuilder();
//        for (int i = 0; i < position; i++) {
//            builder.append("\nConversation.");
//        }
        return "Conversation";
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class ChatItem {
        public final String id;
        public final String content;
        public final String details;

        public ChatItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}

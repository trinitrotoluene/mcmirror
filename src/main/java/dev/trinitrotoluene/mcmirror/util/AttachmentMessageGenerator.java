package dev.trinitrotoluene.mcmirror.util;

public class AttachmentMessageGenerator {
    private AttachmentMessageGenerator() {
    }

    private static final String _attachmentFormat = "tellraw @a {\"text\":\"[%s]\",\"color\":\"yellow\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"%s\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Click to view\"}}";

    public static String createTellRawCommand(String name, String url) {
        return String.format(_attachmentFormat, name, url);
    }
}

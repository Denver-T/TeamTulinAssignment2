package implementations;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * @author Jared Gutierrez
 * 
 * Handles XML tag format validation and matching logic.
 * Validates opening tags, closing tags, and ensures they match properly.
 */
public class TagValidator {
    
    // Regex patterns for different tag types
    private static final Pattern OPENING_TAG = Pattern.compile("<([a-zA-Z][a-zA-Z0-9]*)(?:\\s+[^>]*)?>");
    private static final Pattern CLOSING_TAG = Pattern.compile("</([a-zA-Z][a-zA-Z0-9]*)>");
    private static final Pattern SELF_CLOSING_TAG = Pattern.compile("<([a-zA-Z][a-zA-Z0-9]*)(?:\\s+[^>]*)?/>");
    private static final Pattern PROCESSING_INSTRUCTION = Pattern.compile("<\\?[^>]*\\?>");
    
    /**
     * Checks if a tag string has the correct opening tag format: <tagname>
     * 
     * @param tag the tag string to validate
     * @return true if it's a valid opening tag format
     */
    public static boolean isValidOpeningTag(String tag) {
        if (tag == null || tag.trim().isEmpty()) {
            return false;
        }
        
        // Check basic structure
        if (!tag.startsWith("<") || !tag.endsWith(">")) {
            return false;
        }
        
        // Make sure it's not a closing or self-closing tag
        if (tag.startsWith("</") || tag.endsWith("/>")) {
            return false;
        }
        
        // Check if it matches the opening tag pattern
        Matcher matcher = OPENING_TAG.matcher(tag);
        return matcher.matches();
    }
    
    /**
     * Checks if a tag string has the correct closing tag format: </tagname>
     * 
     * @param tag the tag string to validate
     * @return true if it's a valid closing tag format
     */
    public static boolean isValidClosingTag(String tag) {
        if (tag == null || tag.trim().isEmpty()) {
            return false;
        }
        
        // Must start with </ and end with >
        if (!tag.startsWith("</") || !tag.endsWith(">")) {
            return false;
        }
        
        // Check if it matches the closing tag pattern
        Matcher matcher = CLOSING_TAG.matcher(tag);
        return matcher.matches();
    }
    
    /**
     * Checks if a string is a self-closing tag: <tagname/>
     * 
     * @param tag the tag string to check
     * @return true if it's a self-closing tag
     */
    public static boolean isSelfClosingTag(String tag) {
        if (tag == null || tag.trim().isEmpty()) {
            return false;
        }
        
        Matcher matcher = SELF_CLOSING_TAG.matcher(tag);
        return matcher.matches();
    }
    
    /**
     * Checks if a string is a processing instruction: <?xml ...?>
     * 
     * @param tag the tag string to check
     * @return true if it's a processing instruction
     */
    public static boolean isProcessingInstruction(String tag) {
        if (tag == null || tag.trim().isEmpty()) {
            return false;
        }
        
        Matcher matcher = PROCESSING_INSTRUCTION.matcher(tag);
        return matcher.matches();
    }
    
    /**
     * Extracts the tag name from an opening tag.
     * For example: "<title>" returns "title", "<div class='test'>" returns "div"
     * 
     * @param tag the opening tag
     * @return the tag name, or null if invalid
     */
    public static String getOpeningTagName(String tag) {
        if (tag == null || !isValidOpeningTag(tag)) {
            return null;
        }
        
        Matcher matcher = OPENING_TAG.matcher(tag);
        if (matcher.matches()) {
            return matcher.group(1);
        }
        return null;
    }
    
    /**
     * Extracts the tag name from a closing tag.
     * For example: "</title>" returns "title"
     * 
     * @param tag the closing tag
     * @return the tag name, or null if invalid
     */
    public static String getClosingTagName(String tag) {
        if (tag == null || !isValidClosingTag(tag)) {
            return null;
        }
        
        Matcher matcher = CLOSING_TAG.matcher(tag);
        if (matcher.matches()) {
            return matcher.group(1);
        }
        return null;
    }
    
    /**
     * Checks if an opening tag and closing tag match.
     * Tags must have the same name (case-sensitive).
     * 
     * @param openingTag the opening tag (e.g., "<title>")
     * @param closingTag the closing tag (e.g., "</title>")
     * @return true if the tags match
     */
    public static boolean tagsMatch(String openingTag, String closingTag) {
        String openName = getOpeningTagName(openingTag);
        String closeName = getClosingTagName(closingTag);
        
        if (openName == null || closeName == null) {
            return false;
        }
        
        // Tags are case-sensitive in XML
        return openName.equals(closeName);
    }
    
    /**
     * Validates the format of any tag (opening, closing, or self-closing).
     * Returns an error message if invalid, or null if valid.
     * 
     * @param tag the tag to validate
     * @return error message if invalid, null if valid
     */
    public static String validateTagFormat(String tag) {
        if (tag == null || tag.trim().isEmpty()) {
            return "Empty or null tag";
        }
        
        String trimmed = tag.trim();
        
        // Check if it's a processing instruction (these are valid but ignored)
        if (isProcessingInstruction(trimmed)) {
            return null;
        }
        
        // Must start with <
        if (!trimmed.startsWith("<")) {
            return "Tag must start with '<'";
        }
        
        // Must end with >
        if (!trimmed.endsWith(">")) {
            return "Tag must end with '>'";
        }
        
        // Check for empty tag name
        if (trimmed.equals("<>") || trimmed.equals("</>")) {
            return "Tag name cannot be empty";
        }
        
        // Validate it's one of the recognized formats
        if (isValidOpeningTag(trimmed) || 
            isValidClosingTag(trimmed) || 
            isSelfClosingTag(trimmed)) {
            return null;
        }
        
        return "Invalid tag format: " + trimmed;
    }
    
    /**
     * Extracts all tags from a line of XML text.
     * This is a simple extraction that finds anything between < and >.
     * 
     * @param line the XML line to parse
     * @return array of tag strings found
     */
    public static String[] extractTags(String line) {
        if (line == null || line.trim().isEmpty()) {
            return new String[0];
        }
        
        // Simple approach: find all < ... > patterns
        Pattern tagPattern = Pattern.compile("<[^>]+>");
        Matcher matcher = tagPattern.matcher(line);
        
        // Count matches first
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        
        // Extract tags
        String[] tags = new String[count];
        matcher.reset();
        int index = 0;
        while (matcher.find()) {
            tags[index++] = matcher.group();
        }
        
        return tags;
    }
}

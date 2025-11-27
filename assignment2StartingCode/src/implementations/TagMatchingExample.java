package implementations;

import exceptions.EmptyQueueException;

/**
 * @author Jared Gutierrez
 * 
 * Example implementation showing how to use TagValidator and MyQueue
 * to validate XML tag matching.
 */
public class TagMatchingExample {
    
    /**
     * Demonstrates how to use MyQueue to track opening tags
     * and match them with closing tags.
     * 
     * @param xmlLine a line of XML to validate
     * @return error message if tags don't match, null if valid
     */
    public static String validateTagMatching(String xmlLine) {
        // Extract all tags from the line
        String[] tags = TagValidator.extractTags(xmlLine);
        
        // Use your queue to track opening tags
        MyQueue<String> openTags = new MyQueue<>();
        
        for (String tag : tags) {
            // Skip processing instructions
            if (TagValidator.isProcessingInstruction(tag)) {
                continue;
            }
            
            // Skip self-closing tags (they don't need a closing tag)
            if (TagValidator.isSelfClosingTag(tag)) {
                continue;
            }
            
            // Validate tag format first (YOUR RESPONSIBILITY)
            String formatError = TagValidator.validateTagFormat(tag);
            if (formatError != null) {
                return "Format error: " + formatError;
            }
            
            // If it's an opening tag, add to queue
            if (TagValidator.isValidOpeningTag(tag)) {
                openTags.enqueue(tag);
            }
            // If it's a closing tag, check if it matches the last opening tag
            else if (TagValidator.isValidClosingTag(tag)) {
                try {
                    // Get the most recent opening tag
                    String lastOpening = openTags.dequeue();
                    
                    // Check if they match (YOUR RESPONSIBILITY)
                    if (!TagValidator.tagsMatch(lastOpening, tag)) {
                        return "Tag mismatch: " + lastOpening + " does not match " + tag;
                    }
                } catch (EmptyQueueException e) {
                    // Closing tag without an opening tag
                    return "Closing tag " + tag + " has no matching opening tag";
                }
            }
        }
        
        // After processing all tags, check if any opening tags remain unclosed
        if (!openTags.isEmpty()) {
            try {
                String unclosed = openTags.dequeue();
                return "Opening tag " + unclosed + " has no matching closing tag";
            } catch (EmptyQueueException e) {
                // Should not happen
            }
        }
        
        return null; // All tags match correctly
    }
    
    /**
     * Quick test to show how it works
     */
    public static void main(String[] args) {
        // Test cases
        String[] testLines = {
            "<title>Hello World</title>",                    // Valid
            "<title>Hello World",                             // Missing closing tag
            "<title>Hello</div>",                            // Mismatched tags
            "< title>Hello</title>",                         // Invalid format (space)
            "<title>Hello</title",                           // Missing >
            "<?xml version='1.0'?><root>Test</root>",       // Valid with processing instruction
            "<item/>",                                       // Valid self-closing
            "<div><span>text</span></div>",                  // Valid nested
            "<b>bold <i>italic</b></i>"                      // Intercrossed (error)
        };
        
        System.out.println("Testing Tag Validation:\n");
        for (String line : testLines) {
            String error = validateTagMatching(line);
            if (error == null) {
                System.out.println("✓ VALID: " + line);
            } else {
                System.out.println("✗ ERROR: " + line);
                System.out.println("  Reason: " + error);
            }
            System.out.println();
        }
    }
}

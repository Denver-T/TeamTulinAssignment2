package application;

import exceptions.EmptyQueueException;
import implementations.MyStack;
import implementations.MyQueue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Denver Timlick
 * XMLParser
 * --------------------------------------------
 * This class reads an XML document, checks whether
 * the tags are properly constructed, and records
 * errors in the order that they occur.
 */
public class XMLParser {

    // A stack to track opening tags
    private MyStack<String> tagStack = new MyStack<>();

    // A queue to collect error messages in the order they occur
    private MyQueue<String> errorQueue = new MyQueue<>();

    /**
     * Reads an XML file line-by-line and checks each line
     * for violations in XML tag structure.
     */
    public void parseFile(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {

            String line;
            int lineNumber = 1;

            while ((line = reader.readLine()) != null) {
                processLine(line.trim(), lineNumber);
                lineNumber++;
            }

        } catch (IOException e) {
            System.out.println("Error reading file: " + fileName);
        }

        checkUnclosedTags();
    }

    /**
     * Examines each line for XML tags and applies the rules.
     * @param line The current line of XML text.
     * @param lineNumber The current line number.
     */
    private void processLine(String line, int lineNumber) {

        // Skip blanks
        if (line.isEmpty()) return;

        // Ignore XML processing instructions entirely
        if (line.startsWith("<?xml") && line.endsWith("?>")) {
            return;
        }

        // Extract every tag inside the line
        int start = 0;

        int lastTagEnd = -1;        // *** Added by Ayush — track last closing '>' ***

        while ((start = line.indexOf('<', start)) != -1) {
            int end = line.indexOf('>', start);

            if (end == -1) {
                errorQueue.enqueue("Line " + lineNumber + ": Missing closing '>'");
                return;
            }

            // *** Added by Ayush — check sub-phrase between tags ***
            if (lastTagEnd != -1 && start > lastTagEnd + 1) {
                String between = line.substring(lastTagEnd + 1, start).trim();
                if (between.contains("<") || between.contains(">")) {
                    errorQueue.enqueue("Line " + lineNumber +
                            ": Sub-phrase is not well constructed between matching tags.");
                }
            }

            String tag = line.substring(start + 1, end).trim();
            classifyTag(tag, lineNumber);

            lastTagEnd = end;       // *** Added by Ayush — update lastTagEnd ***
            start = end + 1;
        }

        // *** Added by Ayush — check tail text after last tag ***
        if (lastTagEnd != -1 && lastTagEnd < line.length() - 1) {
            String tail = line.substring(lastTagEnd + 1).trim();
            if (tail.contains("<") || tail.contains(">")) {
                errorQueue.enqueue("Line " + lineNumber +
                        ": Sub-phrase is not well constructed between matching tags.");
            }
        }
    }

    /**
     * Determines the type of tag and applies the rules:
     * @param tag The tag's contents (without < >)
     * @param lineNumber For error tracking
     */
    private void classifyTag(String tag, int lineNumber) {

        // *** Added by Ayush — improved self-closing tag handling ***
        if (tag.endsWith("/")) {
            String inner = tag.substring(0, tag.length() - 1).trim();
            String tagName = extractTagName(inner);

            if (tagName == null || tagName.isEmpty()) {
                errorQueue.enqueue("Line " + lineNumber + ": Self-closing tag is missing a name.");
            }
            return; // valid self-closing
        }

        // Closing tag
        if (tag.startsWith("/")) {
            String closing = tag.substring(1).trim();

            if (tagStack.isEmpty()) {
                errorQueue.enqueue("Line " + lineNumber + ": Closing tag </" + closing +
                        "> has no matching opening tag.");
                return;
            }

            String top = tagStack.pop();

            if (!top.equals(closing)) {
                errorQueue.enqueue("Line " + lineNumber +
                        ": Tag mismatch. Expected </" + top + "> but found </" + closing + ">.");
            }

            return;
        }

        // Opening tag <tag ...>
        String tagName = extractTagName(tag);
        tagStack.push(tagName);
    }

    /**
     * Given a tag that may contain attributes
     * extract the tag name only.
     */
    private String extractTagName(String raw) {
        if (raw == null) return null;
        int spaceIndex = raw.indexOf(' ');
        if (spaceIndex == -1) {
            return raw;
        }
        return raw.substring(0, spaceIndex);
    }

    /**
     * After reading the entire file, any tags left on the stack
     * are unclosed.
     */
    private void checkUnclosedTags() {
        while (!tagStack.isEmpty()) {
            String unclosed = tagStack.pop();
            errorQueue.enqueue("Unclosed tag: <" + unclosed + ">");
        }
    }

    /**
     * Prints all errors that were recorded *in the order encountered*.
     */
    public void printErrors() throws EmptyQueueException {
        if (errorQueue.isEmpty()) {
            System.out.println("XML is well-formed! No errors found.");
            return;
        }

        System.out.println("XML Errors:");
        while (!errorQueue.isEmpty()) {
            System.out.println(errorQueue.dequeue());
        }
    }

    /**
     * Main method to run the XML parser from the command line.
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java application.XMLParser <xmlfile>");
            return;
        }

        XMLParser parser = new XMLParser();
        parser.parseFile(args[0]);

        try {
            parser.printErrors();
        } catch (EmptyQueueException e) {
            System.err.println("Error printing results: " + e.getMessage());
        }
    }
}

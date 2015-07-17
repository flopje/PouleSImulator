package com.bonenkamp.PouleSimulator.data;

import android.util.Xml;

import com.bonenkamp.PouleSimulator.models.XmlTeamEntry;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Simple XML parser that creates {@link XmlTeamEntry} objects,
 * which then can be stored into the databse
 */
public class TeamsXmlParser {

    // element names we want to retrieve from the xml file.
    private static final String FEED_START_TAG = "teams";
    private static final String ITEM_START_TAG = "team";
    private static final String ITEM_NAME_TAG = "name";
    private static final String ITEM_ATT_TAG = "att";
    private static final String ITEM_MID_TAG = "mid";
    private static final String ITEM_DEF_TAG = "def";
    private static final String ITEM_AVERAGE_TAG = "average";

    private static final String namespace = null;

    /**
     * Define and init parser without the namespace setting.
     *
     * @param in as {@link InputStream}
     * @throws XmlPullParserException
     * @throws IOException
     */
    public List parse(InputStream in) throws XmlPullParserException, IOException {
        try{
            XmlPullParser parser = Xml.newPullParser();
            // Disable namespace processing
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        }finally {
            in.close();
        }
    }

    /**
     * Start reading the feed, if an element is found parse it to {@link TeamsXmlParser#readEntry}
     *
     * @param parser
     *
     * @return a {@link List} with {@link XmlTeamEntry}
     *
     * @throws XmlPullParserException
     * @throws IOException
     */
    private List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List entries = new ArrayList();

        parser.require(XmlPullParser.START_TAG, namespace, FEED_START_TAG);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals(ITEM_START_TAG)) {
                entries.add(readEntry(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }
    /**
     *
     * Read a XML entry, between the tags
     *
     * @param parser XmlPullParser
     *
     * @return a new {@link XmlTeamEntry}
     *
     * @throws XmlPullParserException
     * @throws IOException
     */
    private XmlTeamEntry readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, namespace, ITEM_START_TAG);
        String name = null;
        int att = 0;
        int mid = 0;
        int def = 0;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String elementName = parser.getName();

            switch (elementName) {
                case ITEM_NAME_TAG:
                    name = readElement(parser, ITEM_NAME_TAG);
                    break;
                case ITEM_ATT_TAG:
                    att = Integer.parseInt(readElement(parser, ITEM_ATT_TAG));
                    break;
                case ITEM_MID_TAG:
                    mid = Integer.parseInt(readElement(parser, ITEM_MID_TAG));
                    break;
                case ITEM_DEF_TAG:
                    def = Integer.parseInt(readElement(parser, ITEM_DEF_TAG));
                    break;
                default:
                    skip(parser);
                    break;
            }
        }

        return new XmlTeamEntry(name, att, mid, def);
    }

    /**
     *
     * Read a subelement in the tag, as defined above.
     * @param parser
     * @param tag The element name.
     *
     * @return element value as a String
     *
     * @throws XmlPullParserException
     * @throws IOException
     */
    private String readElement(XmlPullParser parser, String tag) throws XmlPullParserException, IOException{
        parser.require(XmlPullParser.START_TAG, namespace, tag);
        String value = readText(parser);
        parser.require(XmlPullParser.END_TAG, namespace, tag);
        return value;
    }

    /**
     *
     * Get the text value from a subelement
     *
     * @param parser
     *
     * @return result as a String.
     *
     * @throws XmlPullParserException
     * @throws IOException
     */
    private String readText(XmlPullParser parser) throws XmlPullParserException, IOException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    /**
     *
     * In case there are elements in the xml file we don't need.
     *
     * @param parser
     * @throws XmlPullParserException
     * @throws IOException
     */
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}

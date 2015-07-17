package com.bonenkamp.PouleSimulator.data;

import android.util.Xml;

import com.bonenkamp.PouleSimulator.models.XmlRefereeEntry;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Referee Xml parser. Reads the XML and creates simple {@link XmlRefereeEntry}
 * objects to put the referee data in the database.
 */
public class RefereeXmlParser {

    // element names we want to retrieve from the xml file.
    private static final String FEED_START_TAG = "referees";
    private static final String ITEM_START_TAG = "referee";
    private static final String ITEM_NAME_TAG = "name";
    private static final String ITEM_STRICT_TAG = "strictness";

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
     * Start reading the feed, if an element is found parse it to {@link RefereeXmlParser#readEntry}
     *
     * @param parser
     *
     * @return a {@link List} with {@link XmlRefereeEntry}
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
     * @return a new {@link XmlRefereeEntry}
     *
     * @throws XmlPullParserException
     * @throws IOException
     */
    private XmlRefereeEntry readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, namespace, ITEM_START_TAG);
        String name = null;
        int strictness = 0;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String elementName = parser.getName();

            switch (elementName) {
                case ITEM_NAME_TAG:
                    name = readElement(parser, ITEM_NAME_TAG);
                    break;
                case ITEM_STRICT_TAG:
                    strictness = Integer.parseInt(readElement(parser, ITEM_STRICT_TAG));
                    break;
                default:
                    skip(parser);
                    break;
            }
        }

        return new XmlRefereeEntry(name, strictness);
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

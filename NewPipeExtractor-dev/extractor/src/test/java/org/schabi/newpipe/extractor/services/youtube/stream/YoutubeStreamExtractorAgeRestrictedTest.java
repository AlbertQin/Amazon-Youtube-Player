package org.schabi.newpipe.extractor.services.youtube.stream;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.schabi.newpipe.Downloader;
import org.schabi.newpipe.extractor.MediaFormat;
import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.services.youtube.extractors.YoutubeStreamExtractor;
import org.schabi.newpipe.extractor.services.youtube.linkHandler.YoutubeStreamLinkHandlerFactory;
import org.schabi.newpipe.extractor.stream.StreamExtractor;
import org.schabi.newpipe.extractor.stream.VideoStream;
import org.schabi.newpipe.extractor.utils.Localization;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.schabi.newpipe.extractor.ExtractorAsserts.assertIsSecureUrl;
import static org.schabi.newpipe.extractor.ServiceList.YouTube;

/**
 * Test for {@link YoutubeStreamLinkHandlerFactory}
 */
public class YoutubeStreamExtractorAgeRestrictedTest {
    public static final String HTTPS = "https://";
    private static YoutubeStreamExtractor extractor;

    @BeforeClass
    public static void setUp() throws Exception {
        NewPipe.init(Downloader.getInstance(), new Localization("GB", "en"));
        extractor = (YoutubeStreamExtractor) YouTube
                .getStreamExtractor("https://www.youtube.com/watch?v=MmBeUZqv1QA");
        extractor.fetchPage();
    }

    @Test
    public void testGetInvalidTimeStamp() throws ParsingException {
        assertTrue(extractor.getTimeStamp() + "", extractor.getTimeStamp() <= 0);
    }

    @Test
    public void testGetValidTimeStamp() throws IOException, ExtractionException {
        StreamExtractor extractor = YouTube.getStreamExtractor("https://youtu.be/FmG385_uUys?t=174");
        assertEquals(extractor.getTimeStamp() + "", "174");
    }

    @Test
    public void testGetAgeLimit() throws ParsingException {
        assertEquals(18, extractor.getAgeLimit());
    }

    @Test
    public void testGetName() throws ParsingException {
        assertNotNull("name is null", extractor.getName());
        assertFalse("name is empty", extractor.getName().isEmpty());
    }

    @Test
    public void testGetDescription() throws ParsingException {
        assertNotNull(extractor.getDescription());
        assertFalse(extractor.getDescription().isEmpty());
    }

    @Test
    public void testGetUploaderName() throws ParsingException {
        assertNotNull(extractor.getUploaderName());
        assertFalse(extractor.getUploaderName().isEmpty());
    }

    @Test
    public void testGetLength() throws ParsingException {
        assertEquals(1789, extractor.getLength());
    }

    @Test
    public void testGetViews() throws ParsingException {
        assertTrue(extractor.getViewCount() > 0);
    }

    @Test
    public void testGetUploadDate() throws ParsingException {
        assertTrue(extractor.getUploadDate().length() > 0);
    }

    @Test
    public void testGetThumbnailUrl() throws ParsingException {
        assertIsSecureUrl(extractor.getThumbnailUrl());
    }

    @Test
    public void testGetUploaderAvatarUrl() throws ParsingException {
        assertIsSecureUrl(extractor.getUploaderAvatarUrl());
    }

    @Test
    public void testGetAudioStreams() throws IOException, ExtractionException {
        // audio streams are not always necessary
        assertFalse(extractor.getAudioStreams().isEmpty());
    }

    @Test
    public void testGetVideoStreams() throws IOException, ExtractionException {
        List<VideoStream> streams = new ArrayList<>();
        streams.addAll(extractor.getVideoStreams());
        streams.addAll(extractor.getVideoOnlyStreams());

        assertTrue(Integer.toString(streams.size()),streams.size() > 0);
        for (VideoStream s : streams) {
            assertTrue(s.getUrl(),
                    s.getUrl().contains(HTTPS));
            assertTrue(s.resolution.length() > 0);
            assertTrue(Integer.toString(s.getFormatId()),
                    0 <= s.getFormatId() && s.getFormatId() <= 0x100);
        }
    }


    @Test
    public void testGetSubtitlesListDefault() throws IOException, ExtractionException {
        // Video (/view?v=YQHsXMglC9A) set in the setUp() method has no captions => null
        assertTrue(extractor.getSubtitlesDefault().isEmpty());
    }

    @Test
    public void testGetSubtitlesList() throws IOException, ExtractionException {
        // Video (/view?v=YQHsXMglC9A) set in the setUp() method has no captions => null
        assertTrue(extractor.getSubtitles(MediaFormat.TTML).isEmpty());
    }
}

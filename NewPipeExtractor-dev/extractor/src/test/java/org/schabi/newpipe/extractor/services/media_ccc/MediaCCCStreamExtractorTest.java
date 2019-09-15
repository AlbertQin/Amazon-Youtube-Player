package org.schabi.newpipe.extractor.services.media_ccc;

import org.junit.BeforeClass;
import org.junit.Test;
import org.schabi.newpipe.Downloader;
import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.services.BaseExtractorTest;
import org.schabi.newpipe.extractor.stream.StreamExtractor;
import org.schabi.newpipe.extractor.services.media_ccc.extractors.MediaCCCStreamExtractor;
import org.schabi.newpipe.extractor.utils.Localization;

import static junit.framework.TestCase.assertEquals;
import static org.schabi.newpipe.extractor.ServiceList.MediaCCC;

/**
 * Test {@link MediaCCCStreamExtractor}
 */
public class MediaCCCStreamExtractorTest implements BaseExtractorTest {
    private static StreamExtractor extractor;

    @BeforeClass
    public static void setUpClass() throws Exception {
        NewPipe.init(Downloader.getInstance(), new Localization("GB", "en"));

        extractor =  MediaCCC.getStreamExtractor("https://api.media.ccc.de/public/events/8afc16c2-d76a-53f6-85e4-90494665835d");
        extractor.fetchPage();
    }

    @Override
    public void testServiceId() throws Exception {
        assertEquals(2, extractor.getServiceId());
    }

    @Override
    public void testName() throws Exception {
        assertEquals("tmux - Warum ein schwarzes Fenster am Bildschirm reicht", extractor.getName());
    }

    @Override
    public void testId() throws Exception {
        assertEquals("", extractor.getId());
    }

    @Override
    public void testUrl() throws Exception {
        assertEquals("", extractor.getUrl());
    }

    @Override
    public void testOriginalUrl() throws Exception {
        assertEquals("", extractor.getOriginalUrl());
    }

    @Test
    public void testThumbnail() throws Exception {
        assertEquals("https://static.media.ccc.de/media/events/gpn/gpn18/105-hd.jpg", extractor.getThumbnailUrl());
    }

    @Test
    public void testUploaderName() throws Exception {
        assertEquals("gpn18", extractor.getUploaderName());
    }

    @Test
    public void testUploaderUrl() throws Exception {
        assertEquals("https://api.media.ccc.de/public/conferences/gpn18", extractor.getUploaderUrl());
    }

    @Test
    public void testUploaderAvatarUrl() throws Exception {
        assertEquals("https://static.media.ccc.de/media/events/gpn/gpn18/logo.png", extractor.getUploaderAvatarUrl());
    }

    @Test
    public void testVideoStreams() throws Exception {
        assertEquals(4, extractor.getVideoStreams().size());
    }

    @Test
    public void testAudioStreams() throws Exception {
        assertEquals(2, extractor.getAudioStreams().size());
    }
}

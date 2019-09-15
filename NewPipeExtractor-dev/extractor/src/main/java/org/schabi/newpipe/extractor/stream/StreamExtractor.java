package org.schabi.newpipe.extractor.stream;

/*
 * Created by Christian Schabesberger on 10.08.18.
 *
 * Copyright (C) Christian Schabesberger 2016 <chris.schabesberger@mailbox.org>
 * StreamExtractor.java is part of NewPipe.
 *
 * NewPipe is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NewPipe is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NewPipe.  If not, see <http://www.gnu.org/licenses/>.
 */

import org.schabi.newpipe.extractor.Extractor;
import org.schabi.newpipe.extractor.MediaFormat;
import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.linkhandler.LinkHandler;
import org.schabi.newpipe.extractor.utils.Localization;
import org.schabi.newpipe.extractor.utils.Parser;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.List;

/**
 * Scrapes information from a video/audio streaming service (eg, YouTube).
 */
public abstract class StreamExtractor extends Extractor {

    public static final int NO_AGE_LIMIT = 0;

    public StreamExtractor(StreamingService service, LinkHandler linkHandler, Localization localization) {
        super(service, linkHandler, localization);
    }

    /**
     * The day on which the stream got uploaded/created. The return information should be in the format
     * dd.mm.yyyy, however it NewPipe will not crash if its not.
     * @return The day on which the stream got uploaded.
     * @throws ParsingException
     */
    @Nonnull
    public abstract String getUploadDate() throws ParsingException;

    /**
     * This will return the url to the thumbnail of the stream. Try to return the medium resolution here.
     * @return The url of the thumbnail.
     * @throws ParsingException
     */
    @Nonnull
    public abstract String getThumbnailUrl() throws ParsingException;

    /**
     * This is the stream description. On YouTube this is the video description. You can return simple HTML here.
     * @return The description of the stream/video.
     * @throws ParsingException
     */
    @Nonnull
    public abstract String getDescription() throws ParsingException;

    /**
     * Get the age limit.
     * @return The age which limits the content or {@value NO_AGE_LIMIT} if there is no limit
     * @throws ParsingException if an error occurs while parsing
     */
    public abstract int getAgeLimit() throws ParsingException;

    /**
     * This should return the length of a video in seconds.
     * @return The length of the stream in seconds.
     * @throws ParsingException
     */
    public abstract long getLength() throws ParsingException;

    /**
     * If the url you are currently handling contains a time stamp/seek, you can return the
     * position it represents here.
     * If the url has no time stamp simply return zero.
     * @return the timestamp in seconds
     * @throws ParsingException
     */
    public abstract long getTimeStamp() throws ParsingException;

    /**
     * The count of how many people have watched the video/listened to the audio stream.
     * If the current stream has no view count or its not available simply return -1
     * @return amount of views.
     * @throws ParsingException
     */
    public abstract long getViewCount() throws ParsingException;

    /**
     * The Amount of likes a video/audio stream got.
     * If the current stream has no likes or its not available simply return -1
     * @return the amount of likes the stream got
     * @throws ParsingException
     */
    public abstract long getLikeCount() throws ParsingException;

    /**
     * The Amount of dislikes a video/audio stream got.
     * If the current stream has no dislikes or its not available simply return -1
     * @return the amount of likes the stream got
     * @throws ParsingException
     */
    public abstract long getDislikeCount() throws ParsingException;

    /**
     * The Url to the page of the creator/uploader of the stream. This must not be a homepage,
     * but the page offered by the service the extractor handles. This url will be handled by the
     * <a href="https://teamnewpipe.github.io/documentation/03_Implement_a_service/#channel">ChannelExtractor</a>,
     * so be sure to implement that one before you return a value here, otherwise NewPipe will crash if one selects
     * this url.
     * @return the url to the page of the creator/uploader of the stream or an empty String
     * @throws ParsingException
     */
    @Nonnull
    public abstract String getUploaderUrl() throws ParsingException;

    /**
     * The name of the creator/uploader of the stream.
     * If the name is not available you can simply return an empty string.
     * @return the name of the creator/uploader of the stream or an empty String
     * @throws ParsingException
     */
    @Nonnull
    public abstract String getUploaderName() throws ParsingException;

    /**
     * The url to the image file/profile picture/avatar of the creator/uploader of the stream.
     * If the url is not available you can return an empty String.
     * @return The url of the image file of the uploader or an empty String
     * @throws ParsingException
     */
    @Nonnull
    public abstract String getUploaderAvatarUrl() throws ParsingException;

    /**
     * Get the dash mpd url. If you don't know what a dash MPD is you can read about it
     * <a href="https://www.brendanlong.com/the-structure-of-an-mpeg-dash-mpd.html">here</a>.
     * @return the url as a string or an empty string
     * @throws ParsingException if an error occurs while reading
     */
    @Nonnull public abstract String getDashMpdUrl() throws ParsingException;

    /**
     * I am not sure if this is in use, and how this is used. However the frontend is missing support
     * for HLS streams. Prove me if I am wrong. Please open an
     * <a href="https://github.com/teamnewpipe/newpipe/issues">issue</a>,
     * or fix this description if you know whats up with this.
     * @return The Url to the hls stream.
     * @throws ParsingException
     */
    @Nonnull public abstract String getHlsUrl() throws ParsingException;

    /**
     * This should return a list of available
     * <a href="https://teamnewpipe.github.io/NewPipeExtractor/javadoc/org/schabi/newpipe/extractor/stream/AudioStream.html">AudioStream</a>s
     * You can also return null or an empty list, however be aware that if you don't return anything
     * in getVideoStreams(), getVideoOnlyStreams() and getDashMpdUrl() either the Collector will handle this as
     * a failed extraction procedure.
     * @return a list of audio only streams in the format of AudioStream
     * @throws IOException
     * @throws ExtractionException
     */
    public abstract List<AudioStream> getAudioStreams() throws IOException, ExtractionException;

    /**
     * This should return a list of available
     * <a href="https://teamnewpipe.github.io/NewPipeExtractor/javadoc/org/schabi/newpipe/extractor/stream/VideoStream.html">VideoStream</a>s
     * Be aware this is the list of video streams which do contain an audio stream.
     * You can also return null or an empty list, however be aware that if you don't return anything
     * in getAudioStreams(), getVideoOnlyStreams() and getDashMpdUrl() either the Collector will handle this as
     * a failed extraction procedure.
     * @return a list of combined video and streams in the format of AudioStream
     * @throws IOException
     * @throws ExtractionException
     */
    public abstract List<VideoStream> getVideoStreams() throws IOException, ExtractionException;

    /**
     * This should return a list of available
     * <a href="https://teamnewpipe.github.io/NewPipeExtractor/javadoc/org/schabi/newpipe/extractor/stream/VideoStream.html">VideoStream</a>s.
     * Be aware this is the list of video streams which do NOT contain an audio stream.
     * You can also return null or an empty list, however be aware that if you don't return anything
     * in getAudioStreams(), getVideoStreams() and getDashMpdUrl() either the Collector will handle this as
     * a failed extraction procedure.
     * @return a list of video and streams in the format of AudioStream
     * @throws IOException
     * @throws ExtractionException
     */
    public abstract List<VideoStream> getVideoOnlyStreams() throws IOException, ExtractionException;

    /**
     * This will return a list of available
     * <a href="https://teamnewpipe.github.io/NewPipeExtractor/javadoc/org/schabi/newpipe/extractor/stream/Subtitles.html">Subtitles</a>s.
     * If no subtitles are available an empty list can returned.
     * @return a list of available subtitles or an empty list
     * @throws IOException
     * @throws ExtractionException
     */
    @Nonnull
    public abstract List<SubtitlesStream> getSubtitlesDefault() throws IOException, ExtractionException;

    /**
     * This will return a list of available
     * <a href="https://teamnewpipe.github.io/NewPipeExtractor/javadoc/org/schabi/newpipe/extractor/stream/Subtitles.html">Subtitles</a>s.
     * given by a specific type.
     * If no subtitles in that specific format are available an empty list can returned.
     * @param format the media format by which the subtitles should be filtered
     * @return a list of available subtitles or an empty list
     * @throws IOException
     * @throws ExtractionException
     */
    @Nonnull
    public abstract List<SubtitlesStream> getSubtitles(MediaFormat format) throws IOException, ExtractionException;

    /**
     * Get the <a href="https://teamnewpipe.github.io/NewPipeExtractor/javadoc/">StreamType</a>.
     * @return the type of the stream
     * @throws ParsingException
     */
    public abstract StreamType getStreamType() throws ParsingException;

    /**
     * should return the url of the next stream. NewPipe will automatically play
     * the next stream if the user wants that.
     * If the next stream is is not available simply return null
     * @return the InfoItem of the next stream
     * @throws IOException
     * @throws ExtractionException
     */
    public abstract StreamInfoItem getNextStream() throws IOException, ExtractionException;

    /**
     * Should return a list of streams related to the current handled. Many services show suggested
     * streams. If you don't like suggested streams you should implement them anyway since they can
     * be disabled by the user later in the frontend.
     * This list MUST NOT contain the next available video as this should be return through getNextStream()
     * If  is is not available simply return null
     * @return a list of InfoItems showing the related videos/streams
     * @throws IOException
     * @throws ExtractionException
     */
    public abstract StreamInfoItemsCollector getRelatedStreams() throws IOException, ExtractionException;

    /**
     * Should analyse the webpage's document and extracts any error message there might be. (e.g. GEMA block)
     *
     * @return Error message; null if there is no error message.
     */
    public abstract String getErrorMessage();

    //////////////////////////////////////////////////////////////////
    ///  Helper
    //////////////////////////////////////////////////////////////////

    /**
     * Override this function if the format of time stamp in the url is not the same format as that form youtube.
     * Honestly I don't even know the time stamp fromat of youtube.
     * @param regexPattern
     * @return the sime stamp/seek for the video in seconds
     * @throws ParsingException
     */
    protected long getTimestampSeconds(String regexPattern) throws ParsingException {
        String timeStamp;
        try {
            timeStamp = Parser.matchGroup1(regexPattern, getOriginalUrl());
        } catch (Parser.RegexException e) {
            // catch this instantly since an url does not necessarily have to have a time stamp

            // -2 because well the testing system will then know its the regex that failed :/
            // not good i know
            return -2;
        }

        if (!timeStamp.isEmpty()) {
            try {
                String secondsString = "";
                String minutesString = "";
                String hoursString = "";
                try {
                    secondsString = Parser.matchGroup1("(\\d{1,3})s", timeStamp);
                    minutesString = Parser.matchGroup1("(\\d{1,3})m", timeStamp);
                    hoursString = Parser.matchGroup1("(\\d{1,3})h", timeStamp);
                } catch (Exception e) {
                    //it could be that time is given in another method
                    if (secondsString.isEmpty() //if nothing was got,
                            && minutesString.isEmpty()//treat as unlabelled seconds
                            && hoursString.isEmpty()) {
                        secondsString = Parser.matchGroup1("t=(\\d+)", timeStamp);
                    }
                }

                int seconds = secondsString.isEmpty() ? 0 : Integer.parseInt(secondsString);
                int minutes = minutesString.isEmpty() ? 0 : Integer.parseInt(minutesString);
                int hours = hoursString.isEmpty() ? 0 : Integer.parseInt(hoursString);

                //don't trust BODMAS!
                return seconds + (60 * minutes) + (3600 * hours);
                //Log.d(TAG, "derived timestamp value:"+ret);
                //the ordering varies internationally
            } catch (ParsingException e) {
                throw new ParsingException("Could not get timestamp.", e);
            }
        } else {
            return 0;
        }
    }
}

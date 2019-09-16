package handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.amazon.ask.model.interfaces.audioplayer.PlayBehavior;
import com.amazon.ask.request.Predicates;
import org.schabi.newpipe.extractor.InfoItem;
import org.schabi.newpipe.extractor.ListExtractor;
import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.channel.ChannelInfoItem;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.services.youtube.extractors.YoutubeSearchExtractor;
import org.schabi.newpipe.extractor.services.youtube.extractors.YoutubeStreamExtractor;
import org.schabi.newpipe.extractor.utils.Localization;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static org.schabi.newpipe.extractor.ServiceList.YouTube;

public class YoutubePlayIntentHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(Predicates.intentName("YoutubePlayIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        //String url = "https://r5---sn-gvbxgn-t34e.googlevideo.com/videoplayback?expire=1568595134&ei=Xoh-XdifD4jqD_62urAJ&ip=99.250.71.41&id=o-AA9thMd1ciESCbCOJogBqX5dH47RpZ18ZNGVRjmPdICK&itag=140&source=youtube&requiressl=yes&mm=31%2C29&mn=sn-gvbxgn-t34e%2Csn-gvbxgn-tt1el&ms=au%2Crdu&mv=m&mvi=4&pl=18&initcwndbps=2125000&mime=audio%2Fmp4&gir=yes&clen=5938560&dur=366.898&lmt=1557008245453080&mt=1568573444&fvip=5&keepalive=yes&c=WEB&txp=5535432&sparams=expire%2Cei%2Cip%2Cid%2Citag%2Csource%2Crequiressl%2Cmime%2Cgir%2Cclen%2Cdur%2Clmt&lsparams=mm%2Cmn%2Cms%2Cmv%2Cmvi%2Cpl%2Cinitcwndbps&lsig=AHylml4wRQIgGS8500Lwsapu-WeGvozKGqyLdkCwCUNQJygIwiysyd0CIQCkBztl4geGpLN32LTkSSeeUVAHkwr6abB3gnsawrzCxQ%3D%3D&sig=ALgxI2wwRQIhANULgGB5aInGyXusNpqGR8sp3dkfXgJEsr1s0XI8p6v_AiBylNODQvZF96ZVMu3WSNi2Tec3iYUMPnFUsXMNko55rA==\n";

        String url = "";
        YoutubeStreamExtractor extractor;
        YoutubeSearchExtractor searchExtractor;
        ListExtractor.InfoItemsPage<InfoItem> itemsPage;

        IntentRequest intentRequest = (IntentRequest) input.getRequestEnvelope().getRequest();
        Map<String, Slot> slots = intentRequest.getIntent().getSlots();

        NewPipe.init(Downloader.getInstance(), new Localization("GB", "en"));
        try {
            searchExtractor = (YoutubeSearchExtractor) YouTube.getSearchExtractor(slots.get("video").getValue());
            searchExtractor.fetchPage();
            itemsPage = searchExtractor.getInitialPage();

            int skip = 0;
            if(slots.get("skip").getValue() != null){
                skip = Integer.parseInt(slots.get("skip").getValue());
            }

            InfoItem firstInfoItem = itemsPage.getItems().get(skip + 0);
            InfoItem secondInfoItem = itemsPage.getItems().get(skip + 1);

            InfoItem playerItem = (firstInfoItem instanceof ChannelInfoItem) ? secondInfoItem
                    : firstInfoItem;

            playerItem.getInfoType();
            extractor = (YoutubeStreamExtractor) YouTube
                    .getStreamExtractor(playerItem.getUrl());

            extractor.fetchPage();

            url = extractor.getAudioStreams().get(0).url;

        } catch (ExtractionException | IOException e) {
            e.printStackTrace();
        }

        return input.getResponseBuilder()
                .addAudioPlayerPlayDirective(PlayBehavior.fromValue("REPLACE_ALL"), (long) 0, "", "001", url)
                .build();
    }
}
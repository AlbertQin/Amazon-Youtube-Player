package handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.request.Predicates;

import java.util.Optional;

public class YoutubeStopIntentHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(Predicates.intentName("YoutubeStopIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {

        return input.getResponseBuilder()
                .addAudioPlayerStopDirective()
                .build();
    }
}
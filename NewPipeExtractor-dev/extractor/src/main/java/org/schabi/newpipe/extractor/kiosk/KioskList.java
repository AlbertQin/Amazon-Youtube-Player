package org.schabi.newpipe.extractor.kiosk;

import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.linkhandler.ListLinkHandlerFactory;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.utils.Localization;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public  class KioskList {
    public interface KioskExtractorFactory {
        KioskExtractor createNewKiosk(final StreamingService streamingService,
                                      final String url,
                                      final String kioskId,
                                      final Localization localization)
            throws ExtractionException, IOException;
    }

    private final int service_id;
    private final HashMap<String, KioskEntry> kioskList = new HashMap<>();
    private String defaultKiosk = null;

    private class KioskEntry {
        public KioskEntry(KioskExtractorFactory ef, ListLinkHandlerFactory h) {
            extractorFactory = ef;
            handlerFactory = h;
        }
        final KioskExtractorFactory extractorFactory;
        final ListLinkHandlerFactory handlerFactory;
    }

    public KioskList(int service_id) {
        this.service_id = service_id;
    }

    public void addKioskEntry(KioskExtractorFactory extractorFactory, ListLinkHandlerFactory handlerFactory, String id)
        throws Exception {
        if(kioskList.get(id) != null) {
            throw new Exception("Kiosk with type " + id + " already exists.");
        }
        kioskList.put(id, new KioskEntry(extractorFactory, handlerFactory));
    }

    public void setDefaultKiosk(String kioskType) {
        defaultKiosk = kioskType;
    }

    public KioskExtractor getDefaultKioskExtractor()
            throws ExtractionException, IOException {
        return getDefaultKioskExtractor("");
    }

    public KioskExtractor getDefaultKioskExtractor(String nextPageUrl)
            throws ExtractionException, IOException {
        return getDefaultKioskExtractor(nextPageUrl, NewPipe.getPreferredLocalization());
    }

    public KioskExtractor getDefaultKioskExtractor(String nextPageUrl, Localization localization)
            throws ExtractionException, IOException {
        if(defaultKiosk != null && !defaultKiosk.equals("")) {
            return getExtractorById(defaultKiosk, nextPageUrl, localization);
        } else {
            if(!kioskList.isEmpty()) {
                // if not set get any entry
                Object[] keySet = kioskList.keySet().toArray();
                return getExtractorById(keySet[0].toString(), nextPageUrl, localization);
            } else {
                return null;
            }
        }
    }

    public String getDefaultKioskId() {
        return defaultKiosk;
    }

    public KioskExtractor getExtractorById(String kioskId, String nextPageUrl)
            throws ExtractionException, IOException {
        return getExtractorById(kioskId, nextPageUrl, NewPipe.getPreferredLocalization());
    }

    public KioskExtractor getExtractorById(String kioskId, String nextPageUrl, Localization localization)
            throws ExtractionException, IOException {
        KioskEntry ke = kioskList.get(kioskId);
        if(ke == null) {
            throw new ExtractionException("No kiosk found with the type: " + kioskId);
        } else {
            return ke.extractorFactory.createNewKiosk(NewPipe.getService(service_id),
                    ke.handlerFactory.fromId(kioskId).getUrl(), kioskId, localization);
        }
    }

    public Set<String> getAvailableKiosks() {
        return kioskList.keySet();
    }

    public KioskExtractor getExtractorByUrl(String url, String nextPageUrl)
            throws ExtractionException, IOException{
        return getExtractorByUrl(url, nextPageUrl, NewPipe.getPreferredLocalization());
    }

    public KioskExtractor getExtractorByUrl(String url, String nextPageUrl, Localization localization)
            throws ExtractionException, IOException {
        for(Map.Entry<String, KioskEntry> e : kioskList.entrySet()) {
            KioskEntry ke = e.getValue();
            if(ke.handlerFactory.acceptUrl(url)) {
                return getExtractorById(e.getKey(), nextPageUrl, localization);
            }
        }
        throw new ExtractionException("Could not find a kiosk that fits to the url: " + url);
    }

    public ListLinkHandlerFactory getListLinkHandlerFactoryByType(String type) {
        return kioskList.get(type).handlerFactory;
    }
}

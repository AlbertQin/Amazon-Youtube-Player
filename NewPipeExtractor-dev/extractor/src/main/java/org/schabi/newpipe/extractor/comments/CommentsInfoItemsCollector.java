package org.schabi.newpipe.extractor.comments;

import java.util.List;
import java.util.Vector;

import org.schabi.newpipe.extractor.InfoItem;
import org.schabi.newpipe.extractor.InfoItemsCollector;
import org.schabi.newpipe.extractor.exceptions.ParsingException;

public class CommentsInfoItemsCollector extends InfoItemsCollector<CommentsInfoItem, CommentsInfoItemExtractor> {

    public CommentsInfoItemsCollector(int serviceId) {
        super(serviceId);
    }

    @Override
    public CommentsInfoItem extract(CommentsInfoItemExtractor extractor) throws ParsingException {

        // important information
        int serviceId = getServiceId();
        String url = extractor.getUrl();
        String name = extractor.getName();

        CommentsInfoItem resultItem = new CommentsInfoItem(serviceId, url, name);

        // optional information
        try {
            resultItem.setCommentId(extractor.getCommentId());
        } catch (Exception e) {
            addError(e);
        }
        try {
            resultItem.setCommentText(extractor.getCommentText());
        } catch (Exception e) {
            addError(e);
        }
        try {
            resultItem.setAuthorName(extractor.getAuthorName());
        } catch (Exception e) {
            addError(e);
        }
        try {
            resultItem.setAuthorThumbnail(extractor.getAuthorThumbnail());
        } catch (Exception e) {
            addError(e);
        }
        try {
            resultItem.setAuthorEndpoint(extractor.getAuthorEndpoint());
        } catch (Exception e) {
            addError(e);
        }
        try {
            resultItem.setPublishedTime(extractor.getPublishedTime());
        } catch (Exception e) {
            addError(e);
        }
        try {
            resultItem.setLikeCount(extractor.getLikeCount());
        } catch (Exception e) {
            addError(e);
        }
        try {
            resultItem.setThumbnailUrl(extractor.getThumbnailUrl());
        } catch (Exception e) {
            addError(e);
        }

        return resultItem;
    }

    @Override
    public void commit(CommentsInfoItemExtractor extractor) {
        try {
            addItem(extract(extractor));
        } catch (Exception e) {
            addError(e);
        }
    }

    public List<CommentsInfoItem> getCommentsInfoItemList() {
        List<CommentsInfoItem> siiList = new Vector<>();
        for (InfoItem ii : super.getItems()) {
            if (ii instanceof CommentsInfoItem) {
                siiList.add((CommentsInfoItem) ii);
            }
        }
        return siiList;
    }
}

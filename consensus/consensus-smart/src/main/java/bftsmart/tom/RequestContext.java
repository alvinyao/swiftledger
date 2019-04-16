package bftsmart.tom;

import bftsmart.communication.client.ReplyListener;
import bftsmart.tom.core.messages.TOMMessageType;

/**
 * This class contains information related to a client request.
 */
public final class RequestContext {

    private final int reqId;
    private final int operationId;
    private final TOMMessageType requestType;
    private final int[] targets;
    private final long sendingTime;
    private final ReplyListener replyListener;
    private final byte[] request;

    /**
     * Instantiates a new Request context.
     *
     * @param reqId         the req id
     * @param operationId   the operation id
     * @param requestType   the request type
     * @param targets       the targets
     * @param sendingTime   the sending time
     * @param replyListener the reply listener
     * @param request       the request
     */
    public RequestContext(int reqId, int operationId, TOMMessageType requestType, int[] targets, long sendingTime,
        ReplyListener replyListener, byte[] request) {
        this.reqId = reqId;
        this.operationId = operationId;
        this.requestType = requestType;
        this.targets = targets;
        this.sendingTime = sendingTime;
        this.replyListener = replyListener;
        this.request = request;
    }

    /**
     * Gets req id.
     *
     * @return the req id
     */
    public final int getReqId() {
        return reqId;
    }

    /**
     * Gets operation id.
     *
     * @return the operation id
     */
    public final int getOperationId() {
        return operationId;
    }

    /**
     * Gets request type.
     *
     * @return the request type
     */
    public final TOMMessageType getRequestType() {
        return requestType;
    }

    /**
     * Gets sending time.
     *
     * @return the sending time
     */
    public final long getSendingTime() {
        return sendingTime;
    }

    /**
     * Gets reply listener.
     *
     * @return the reply listener
     */
    public ReplyListener getReplyListener() {
        return replyListener;
    }

    /**
     * Get targets int [ ].
     *
     * @return the int [ ]
     */
    public int[] getTargets() {
        return targets;
    }

    /**
     * Get request byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getRequest() {
        return request;
    }
}


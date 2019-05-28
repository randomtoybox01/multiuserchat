package com.muc;
/**
 * @author Cole McGuire
 */

/**
 * interface that initializes onMessage
 */
public interface MessageListener {
    public void onMessage(String fromLogin, String msgBody);
}

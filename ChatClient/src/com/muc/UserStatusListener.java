package com.muc;
/**
 * @author Cole McGuire
 */

/**
 * Interface that initializes the online/offline
 * methods
 */
public interface UserStatusListener {
    public void online(String login);
    public void offline(String login);
}

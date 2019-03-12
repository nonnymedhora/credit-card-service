package org.bawaweb.core.model;

/**
 * Created by medhoran on 12/11/13.
 * Holds the 'Session' level information which is passed around by 'In' interceptors
 * to the manager
 */
public class SessionInfo {

    private static String sessionId;
    private static Integer serverRequestNumber;

    private static Integer customerId;
    private static Integer clientId;
    private static Integer conciergeId;
    private static Integer userId;
    private static String userName;

//    public SessionInfo() {}

    public static String getSessionId() {
        System.out.println("Session is id " + sessionId);
        return sessionId != null ? sessionId : "999999";
    }

    public static Integer getServerRequestNumber() {
        return serverRequestNumber;
    }

    public static void setSessionId(String id) {
        sessionId = id;
    }


    public static void setServerRequestNumber(Integer num) {
        serverRequestNumber = num;
    }

    public static Integer getCustomerId() {
        return customerId != null ? customerId : 9999999;
    }

    public static void setCustomerId(Integer id) {
        customerId = id;
    }

    public static Integer getClientId() {
        return clientId != null ? clientId : 88888;
    }

    public static void setClientId(Integer id) {
        clientId = id;
    }

    public static Integer getConciergeId() {
        return conciergeId != null ? conciergeId : 77777;
    }

    public static void setConciergeId(Integer id) {
        conciergeId = id;
    }

    public static Integer getUserId() {
        return userId != null ? userId : 66666;
    }

    public static void setUserId(Integer id) {
        userId = id;
    }

    public static String getUserName() {
        return userName != null ? userName : "AMEEE";
    }

    public static void setUserName(String name) {
        userName = name;
    }
}

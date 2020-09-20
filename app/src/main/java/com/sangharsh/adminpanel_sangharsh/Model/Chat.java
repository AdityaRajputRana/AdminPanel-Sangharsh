package com.sangharsh.adminpanel_sangharsh.Model;

import java.util.Map;

public class Chat {

    private String chatId;
    private String chaterName;
    private String chaterPic;
    private String chaterUid;
    private String lastMessage;
    private int status;  /*
    0 - no message
    1 - unread
    2 - read
    3 - deleted
    */

    private long unreadCount;


    private Map<String, Object> time;

    public int getStatus() {
        return status;
    }

    public Map<String, Object> getTime() {
        return time;
    }

    public void setTime(Map<String, Object> time) {
        this.time = time;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(long unreadCount) {
        this.unreadCount = unreadCount;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public void setChaterName(String chaterName) {
        this.chaterName = chaterName;
    }

    public void setChaterPic(String chaterPic) {
        this.chaterPic = chaterPic;
    }

    public void setChaterUid(String chaterUid) {
        this.chaterUid = chaterUid;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }





//    public Chat(String chatId, String chaterName, String chaterPic, String chaterUid, String lastMessage, Map<String, String> timestamp) {
//        this.chatId = chatId;
//        this.chaterName = chaterName;
//        this.chaterPic = chaterPic;
//        this.chaterUid = chaterUid;
//        this.lastMessage = lastMessage;
//        this.timestamp = timestamp;
//    }

    public Chat(){}

    public String getChatId() {
        return chatId;
    }

    public String getChaterName() {
        return chaterName;
    }

    public String getChaterPic() {
        return chaterPic;
    }

    public String getChaterUid() {
        return chaterUid;
    }

    public String getLastMessage() {
        return lastMessage;
    }


}
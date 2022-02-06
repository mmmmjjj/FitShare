package com.fitshare.backend.api.service;

import com.fitshare.backend.api.request.FriendReq;
import com.fitshare.backend.api.response.FriendRes;

import java.util.List;

public interface FriendService {

    void addFriend(FriendReq friendReq);

    List<FriendRes> getFriendList();

    List<FriendRes> getFriendListByEmail(String friendEmail);

    void deleteFriend(Long friendId);

    void addFriendRequest(FriendReq friendReq);

    List<FriendRes> getFriendRequestList();

    void deleteFriendRequest(Long requesterId);

}

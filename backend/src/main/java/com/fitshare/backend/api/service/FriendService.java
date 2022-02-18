package com.fitshare.backend.api.service;

import com.fitshare.backend.api.request.FriendReq;
import com.fitshare.backend.api.response.FriendRes;

import java.util.List;

public interface FriendService {

    void addFriend(Long memberId, FriendReq friendReq);

    List<FriendRes> getFriendList(Long memberId);

    List<FriendRes> getFriendListByName(Long memberId, String friendName);

    void deleteFriend(Long memberId, Long friendId);

    void addFriendRequest(Long memberId, FriendReq friendReq);

    List<FriendRes> getFriendRequestList(Long memberId);

    void deleteFriendRequest(Long memberId, Long requesterId);

}

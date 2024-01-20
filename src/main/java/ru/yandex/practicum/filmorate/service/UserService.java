package ru.yandex.practicum.filmorate.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserService {

    private final InMemoryUserStorage inMemoryUserStorage;

    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public User addFriend(Integer id, Integer friendId) {
        User currentUser = inMemoryUserStorage.findUserById(id);
        User friend = inMemoryUserStorage.findUserById(friendId);
        if (!currentUser.getFriends().contains(friendId)) {
            currentUser.addIdUserFriend(friendId);
            friend.addIdUserFriend(id);
        }
        inMemoryUserStorage.updateUser(currentUser);
        inMemoryUserStorage.updateUser(friend);
        log.info("Попытка добавить в друзья пользователя с id - {}. Во френдлисте пользователя - {}",
                friendId, inMemoryUserStorage.findUserById(id).getFriends());
        return friend;
    }

    public User deleteFriend(Integer id, Integer friendId) {
        User currentUser = inMemoryUserStorage.findUserById(id);
        User friend = inMemoryUserStorage.findUserById(id);
        if (currentUser.getFriends().contains(friendId)) {
            currentUser.removeIdUserFriend(friendId);
            friend.removeIdUserFriend(id);
        }
        log.info("Попытка удалить пользователя id - {} из друзей. Пользователь id - {} друзья: {}",
                friendId, id, inMemoryUserStorage.findUserById(id).getFriends());
        log.info("Пользователь id - {} друзья: {}",
                friendId, inMemoryUserStorage.findUserById(id).getFriends());
        return friend;
    }

    public List<User> showCommonFriendList(Integer id, Integer otherId) {
        User currentUser = inMemoryUserStorage.findUserById(id);
        User anitherUser = inMemoryUserStorage.findUserById(otherId);
        List<Integer> idList = new ArrayList<>();
        for (Integer i : currentUser.getFriends()) {
            if (anitherUser.getFriends().contains(i)) {
                idList.add(i);
            }
        }
        List<User> friendList = new ArrayList<>();
        for (Integer i : idList) {
            friendList.add(inMemoryUserStorage.findUserById(i));
        }
        return friendList;
    }

    public List<User> showFriendList(Integer id) {
        User currentUser = inMemoryUserStorage.findUserById(id);
        List<Integer> idList = new ArrayList<>();
        idList.addAll(currentUser.getFriends());
        List<User> allFriendList = new ArrayList<>();
        for (Integer i : idList) {
            allFriendList.add(inMemoryUserStorage.findUserById(i));
        }
        return allFriendList;
    }


}

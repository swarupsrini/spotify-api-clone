# assignment 3

**total grade**: 49.5/54.0

---

## automarker test results

Running auto-marker
('playlist', 'user1-favorites')
('playlist', 'user1-favorites')
('playlist', 'user1-favorites')
OK
{
    "Add Song with additional unexpected param, unexpected param: unexepectedParam: 12345": "Test Passed!",
    "Add Song with all invalid param keys, param keys: inValidSongName:songName1 invalidSongArtistFullName:songArtistFullName1 invalidSongAlbum:songAlbum": "Test Passed!",
    "Add Song with all valid paramaters, params: songName:songName1 songArtistFullName:songArtistFullName1 songAlbum:songAlbum": "Test Passed!",
    "Add Song with one mandatory missing paramater, missing param: songArtistFullName": "Test Passed!",
    "Calling /followFriend as non-existing userName 'user1' to follow user with userName 'tahmid'": "Test Passed!",
    "Calling /followFriend as userName 'user1' to follow a non-existing user with userName 'non-existing-user'": "Test Passed!",
    "Calling /followFriend as userName 'user1' to follow friend with userName 'ilir'": "Test Passed!",
    "Calling /followFriend as userName 'user1' to follow friend with userName 'shabaz'": "Test Passed!",
    "Calling /followFriend as userName 'user1' to follow friend with userName 'tahmid'": "Test Passed!",
    "Calling /followFriend with missing param 'friendUserName'": "Test Passed!",
    "Calling /getAllFriendFavouriteSongTitles to get songs of user with userName 'user1' friends likes": "Test Passed!",
    "Calling /unfollowFriend as userName 'user1' to unfollow friend with userName 'ilir'": "Test Passed!",
    "Calling rount /profile with missing params 'fullName'": "Test Passed!",
    "Calling route /profile to add user with userName 'user1'": "Test Passed!",
    "Checking DB to check if all nodes remains as expected after creating user": "Test failed! Some node(s) did not match the expected result.",
    "Checking DB to check if all nodes remains as expected after follow": "Test failed! Some node(s) did not match the expected result.",
    "Checking DB to check if all nodes remains as expected after unfollow": "Test failed! Some node(s) did not match the expected result.",
    "Checking DB to see if user 'user1' was correctly followed users with userNames 'ilir', 'tahmid' and 'shabaz'": "Test Passed!",
    "Checking DB to see if user 'user1' was correctly unfollowed users with userNames 'shabaz'": "Test Passed!",
    "Checking DB to see if user1 actually liked songId 5d620f54d78b833e34e65b46 and 5d620f54d78b833e34e65b47": "Test Failed! DB data does not match expected data",
    "Checking DB to see if user1 actually unliked songId 5d620f54d78b833e34e65b46 and 5d620f54d78b833e34e65b47": "Test Passed!",
    "Checking if the follow was one directional. Only 'user1' followed 'ilir' and not the other way around": "Test Passed!",
    "Checking if the unfollow was one directional. Only 'user1' unfollowed 'shabaz' and not the other way around": "Test Passed!",
    "Checking returned data after calling /getAllFriendFavouriteSongTitles": "Test Failed! Returned data does not match expected data",
    "Decrementing favourites count below 0 for a valid songId, id=5def22a23326da261f746c72": "Test Failed! Response status did not match expected response status",
    "Decrementing favourites count for a songId that does not exist, id=000000000000000000000000": "Test Passed!",
    "Decrementing favourites count for a songId that is invalid, id=?efef!#3434": "Test Failed! Response status did not match expected response status",
    "Decrementing favourites count for a valid songId, id=5d61728193528481fe5a3122": "Test Passed!",
    "Deleting song by id that does not exist in the DB, id=000000000000000000000000": "Test Passed!",
    "Deleting song by id that exists in the DB, id=5d61728193528481fe5a3122": "Test Passed!",
    "Deleting song via an invalid songId, id=?efef!#3434": "Test Failed! Response status did not match expected response status",
    "Getting song by id that does not exist in the DB, id=000000000000000000000000": "Test Passed!",
    "Getting song by id that exists in the DB, id=5def22a13326da261f746c71": "Test Passed!",
    "Getting song title by id that does not exist in the DB, id=000000000000000000000000": "Test Passed!",
    "Getting song title by id that exists in the DB, id=5d61728193528481fe5a3122": "Test Passed!",
    "Getting song title with an invalid songId, id=?efef!#3434": "Test Failed! Response status did not match expected response status",
    "Getting song with an invalid songId, id=?efef!#3434": "Test Failed! Response status did not match expected response status",
    "Incrementing favourites count for a songId that does not exist, id=000000000000000000000000": "Test Passed!",
    "Incrementing favourites count for a songId that is invalid, id=?efef!#3434": "Test Failed! Response status did not match expected response status",
    "Incrementing favourites count for a songId which exists, but providing invalid ?shouldDecrement param, ?shouldDecrement=gibberish!, songId=5d61728193528481fe5a3122": "Test Passed!",
    "Incrementing favourites count for a valid songId id=5d61728193528481fe5a3122": "Test Passed!",
    "calling /getAllFriendFavouriteSongTitles to get songs user with userName 'ilir' likes'": "Test Passed!",
    "calling /likeSong with user1 to unlike the same songId 5d620f54d78b833e34e65b47": "Test Failed! Expected response OK",
    "calling /likesong with user1 to like songId 5d620f54d78b833e34e65b46": "Test Passed!",
    "calling /likesong with user1 to like songId 5d620f54d78b833e34e65b47": "Test Passed!",
    "calling /likesong with user1 to like the same songId 5d620f54d78b833e34e65b47": "Test Passed!",
    "calling /unlikeSong with user1 to unlike songId 5d620f54d78b833e34e65b46": "Test Passed!",
    "calling /unlikeSong with user1 to unlike songId 5d620f54d78b833e34e65b47": "Test Passed!",
    "checking song DB to confirm that the favorite counter remained the same for songId 5d620f54d78b833e34e65b47": "Test Failed! Expected favorite counter to be 57",
    "checking song DB to see if the favorite counter is decremented": "Test Passed!",
    "checking song DB to see if the favorite counter is decremented for songId 5d620f54d78b833e34e65b47": "Test Passed!",
    "checking song DB to see if the favorite counter is incremented": "Test Passed!",
    "checking song DB to see if the favorite counter is incremented for songId 5d620f54d78b833e34e65b47": "Test Passed!"
}

**automarker grade**: 48.0/48.0

---

## code style

git usage: 1.0/2.0

- commit messages are not capitalized
- git flow usage is good

---

code style: 0.5/4.0

- Song microservice has some inappropriate variable names
    - some single letter variable names
- some comments in Song microservice but generally lacking
- Profile microservice has some inappropriate variable names
    - some mixed snake_case and two letter variable names
- lacking comments in Profile microservice

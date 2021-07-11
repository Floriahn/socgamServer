package com.socgam.server;


import com.socgam.server.ControllerArrays.UserList;
import com.socgam.server.model.*;
import com.socgam.server.model.CombatSystem.CharacterClasses;
import com.socgam.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class UserController {

    final double squaredRadius = 1.0;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user/check/{id}")
    public User checkForUser(@PathVariable String id){
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty())
            return new User("-1","NOUSER");

        return user.get();
    }

    @PostMapping("/user/new/{id}/{username}")
    public void addNewUser(@PathVariable String id, @PathVariable String username){
        userRepository.save(new User(id, username));
    }

    @PostMapping("user/test")
    public String test(){
        userRepository.findAll();
        System.out.println("test");
        return "abc";
    }

    @PostMapping("/user/updateHealth/{id}/{CharType}/{newHealth}")
    public void updateHealth(@PathVariable String id, @PathVariable String CharType, @PathVariable String newHealth){
        User u = userRepository.findById(id).get();
        for(CharacterModel c : u.getCharacters()){
            if(c.getCharClass().equals(CharacterClasses.valueOf(CharType))){
                c.setVitality(Integer.parseInt(newHealth));
                if(c.getCharClass().equals(u.getActiveCharacter().getCharClass())){
                    u.setActiveCharacter(c);
                }
            }
        }
        userRepository.save(u);
    }

    @GetMapping("/user/getNearbyActiveUsers/{stringlat}/{stringlng}")
    public User[] getNearbyActiveUsers(@PathVariable String stringlat, @PathVariable String stringlng){
        double lat = Double.parseDouble(stringlat);
        double lng = Double.parseDouble(stringlng);

        List<User> users = userRepository.findAll().stream().filter(u ->  (u.getLoc()!=null && distance(lat, u.getLoc().getLatitude(),lng,u.getLoc().getLongitude()) < 0.750 ) && u.isOnline()).collect(Collectors.toList());

        User[] uarr = new User[users.size()];
        for (int i = 0; i < users.size(); i++){
            uarr[i] = users.get(i);
        }

        return uarr;
    }

    @PostMapping("/user/updateLocation/{id}/{stringlat}/{stringlng}")
    public void updateLocation(@PathVariable String id, @PathVariable String stringlat, @PathVariable String stringlng){
        double lat = Double.parseDouble(stringlat);
        double lng = Double.parseDouble(stringlng);
        Optional<User> user = userRepository.findById(id);
        User u = user.get();
        u.setLoc(new Location(lng, lat));
        u.setLastTimeUpdated(System.currentTimeMillis());

        userRepository.save(u);
    }

    @PostMapping("/user/updateName/{id}/{newName}")
    public void updateName(@PathVariable String id, @PathVariable String newName){
        User user = userRepository.findById(id).get();
        user.setUsername(newName);
        userRepository.save(user);
    }

    @GetMapping("/user/getPendingFriendRequests/{id}")
    public List<User> getPendingFriendRequests(@PathVariable String id){
        User user = userRepository.findById(id).get();

        List<User> friendRequests = new LinkedList<>();
        userRepository.findAllById(user.getFriendRequests()).forEach(friendRequests::add);
        return friendRequests;
    }

    @GetMapping("/user/getFriends/{id}")
    public List<User> getFriends(@PathVariable String id){
        User user = userRepository.findById(id).get();

        List<User> friends = new LinkedList<>();
        userRepository.findAllById(user.getFriendRelations().stream().map(FriendRelation::getFriendId).collect(Collectors.toList())).forEach(friends::add);
        return friends;
    }

    @PostMapping("/user/sendFriendRequest/{id}/{friendId}")
    public void sendFriendRequest(@PathVariable String id, @PathVariable String friendId){
        User friend = userRepository.findById(friendId).get();

        if(!friend.getFriendRequests().contains(id)){
            friend.getFriendRequests().add(id);
        }

        userRepository.save(friend);
    }

    @PostMapping("/user/acceptFriendRequest/{id}/{friendId}")
    public void acceptFriendRequest(@PathVariable String id, @PathVariable String friendId){
        User user = userRepository.findById(id).get();
        User friend = userRepository.findById(friendId).get();
        if(user.getFriendRelations().stream().noneMatch(fr -> fr.getFriendId().equals(friendId))){
            user.getFriendRelations().add(new FriendRelation(friendId));
        }
        if(friend.getFriendRelations().stream().noneMatch(fr -> fr.getFriendId().equals(id))){
            friend.getFriendRelations().add(new FriendRelation(id));
        }
        user.getFriendRequests().remove(friendId);
        userRepository.save(user);
        userRepository.save(friend);
    }

    @PostMapping("/user/declineFriendRequest/{id}/{friendId}")
    public void declineFriendRequest(@PathVariable String id, @PathVariable String friendId){
        User user = userRepository.findById(id).get();
        user.getFriendRequests().remove(friendId);
        userRepository.save(user);
    }

    @PostMapping("/user/removeFriend/{id}/{friendId}")
    public void removeFriend(@PathVariable String id, @PathVariable String friendId) {
        User user = userRepository.findById(id).get();
        User friend = userRepository.findById(friendId).get();
        user.getFriendRelations().remove(new FriendRelation(friendId));
        friend.getFriendRelations().remove(new FriendRelation(id));

        userRepository.save(user);
        userRepository.save(friend);
    }

    @PostMapping("/user/addCharacter/{id}/{name}/{type}/{health}/{charIndex}")
    public void addCharacter(@PathVariable String name, @PathVariable String type, @PathVariable String id, @PathVariable String health, @PathVariable String charIndex){
        User u = userRepository.findById(id).get();
        CharacterModel c = new CharacterModel(name, CharacterClasses.valueOf(type), Integer.parseInt(health),Integer.parseInt(charIndex));
        u.getCharacters().add(c);
        u.getCardCollection().addAll(c.getCardDeck());
        if(u.getActiveCharacter() == null){
            u.setActiveCharacter(c);
        }
        userRepository.save(u);
    }

    @PostMapping("/user/changeCharName/{id}/{name}/{type}")
    public void changeCharName(@PathVariable String id, @PathVariable String name, @PathVariable String type){
        User u = userRepository.findById(id).get();
        for(CharacterModel c : u.getCharacters()){
            if(c.getCharClass().equals(CharacterClasses.valueOf(type))){
                c.setName(name);
                break;
            }
        }
        userRepository.save(u);
    }

    @PostMapping("user/setScrollCount/{id}/{amount}")
    public void setScrollCount(@PathVariable String amount, @PathVariable String id){
        User u = userRepository.findById(id).get();
        u.setScrollCount(Integer.parseInt(amount));
        userRepository.save(u);
    }

    @PostMapping("user/changeActiveCharacter/{id}/{type}")
    public void changeActiveCharacter(@PathVariable String id, @PathVariable String type){
        User u = userRepository.findById(id).get();
        for(CharacterModel c : u.getCharacters()){
            if(c.getCharClass().equals(CharacterClasses.valueOf(type))){
                u.setActiveCharacter(c);
                break;
            }
        }
        userRepository.save(u);
    }

    @PostMapping("user/addCardtoDeck/{id}/{type}/{cardID}")
    public void addCardtoDeck(@PathVariable String id, @PathVariable String type, @PathVariable String cardID){
        User u = userRepository.findById(id).get();
        for(CharacterModel c : u.getCharacters()){
            if(c.getCharClass().equals(CharacterClasses.valueOf(type))){
                c.getCardDeck().add(cardID);
                if(c.getCharClass().equals(u.getActiveCharacter().getCharClass())){
                    u.setActiveCharacter(c);
                }
                break;
            }
        }
        userRepository.save(u);
    }

    @PostMapping("user/removeCardfromDeck/{id}/{type}/{cardID}")
    public void removeCardfromDeck(@PathVariable String id, @PathVariable String type, @PathVariable String cardID){
        User u = userRepository.findById(id).get();
        for(CharacterModel c : u.getCharacters()){
            if(c.getCharClass().equals(CharacterClasses.valueOf(type))){
                c.getCardDeck().remove(cardID);
                if(c.getCharClass().equals(u.getActiveCharacter().getCharClass())){
                    u.setActiveCharacter(c);
                }
                break;
            }
        }
        userRepository.save(u);
    }

    @GetMapping("user/getAllUsers")
    public UserList getAllUsers(){
        return new UserList(userRepository.findAll());
    }

    private double distance(double lat1, double lat2, double lon1, double lon2) {
        //https://www.geeksforgeeks.org/program-distance-two-points-earth/
        // The math module contains a function
        // named toRadians which converts from
        // degrees to radians.
        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        // Haversine formula
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2),2);
        double c = 2 * Math.asin(Math.sqrt(a));
        // Radius of earth in kilometers. Use 3956
        // for miles
        double r = 6371;
        // calculate the result
        return(c * r);
    }
}

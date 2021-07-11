package com.socgam.server;

import com.socgam.server.model.Team;
import com.socgam.server.model.TeamRequest;
import com.socgam.server.model.User;
import com.socgam.server.repository.TeamRepository;
import com.socgam.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class TeamController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;


    @PostMapping("/team/create/{teamName}/{playerId}")
    public void create(@PathVariable String teamName, @PathVariable String playerId){

        String id = null;
        while (id == null){
            id = UUID.randomUUID().toString();
            if(teamRepository.existsById(id))
                id = null;
        }

        List<String> play = new LinkedList<>();
        play.add(playerId);

        Team t = new Team(id,teamName,play);

        teamRepository.save(t);

        User user = userRepository.findById(playerId).get();
        user.setTeamId(id);
        userRepository.save(user);
    }

    @GetMapping("/team/getPendingTeamRequests/{id}")
    public TeamRequest[] getPendingTeamRequests(@PathVariable String id){
        User user = userRepository.findById(id).get();

        TeamRequest[] req = new TeamRequest[user.getTeamRequests().size()];
        for(int i = 0; i < user.getTeamRequests().size(); i++){
            req[i] = user.getTeamRequests().get(i);
        }
        return req;
    }

    @GetMapping("/team/getTeam/{id}")
    public Team getTeam(@PathVariable String id){
        User user = userRepository.findById(id).get();
        String teamId = user.getTeamId();
        if(teamId == null) return null;
        return teamRepository.findById(teamId).get();
    }

    @GetMapping("/team/getTeammates/{id}")
    public User[] getTeammates(@PathVariable String id){
        User u = userRepository.findById(id).get();
        String teamId = u.getTeamId();
        List<User> mates = new LinkedList<>();
        if(teamId == null) return new User[0];
        Team t = teamRepository.findById(teamId).get();
        List<String> playerids = t.getPlayerIds();
        for(String s : playerids){
            mates.add(userRepository.findById(s).get());
        }
        User[] ma = new User[mates.size()];
        for(int i = 0; i<mates.size(); i++){
            ma[i] = mates.get(i);
        }
        return ma;
    }

    @PostMapping("/team/sendTeamRequest/{fromId}/{toid}/{teamId}")
    public void sendTeamRequest(@PathVariable String toid, @PathVariable String teamId, @PathVariable String fromId){
        User user = userRepository.findById(toid).get();

        if(!user.getTeamRequests().contains(teamId)){
            user.getTeamRequests().add(new TeamRequest(teamId, fromId));
        }

        userRepository.save(user);
    }

    @GetMapping("/team/tryAcceptTeamRequest/{id}/{teamId}")
    public boolean tryAcceptTeamRequest(@PathVariable String id, @PathVariable String teamId){
        User user = userRepository.findById(id).get();
        if(user.getTeamId() != null && user.getTeamId().equals(teamId)){
            user.getTeamRequests().removeIf(req -> req.getTeamId().equals(teamId));
            userRepository.save(user);
            return true;
        }

        Team t = teamRepository.findById(teamId).get();

        if(t.tryJoinTeam(id)){
            //altes team ggf verlassen
            if(user.getTeamId() != null) {
                Optional<Team> oldTeamOptional = teamRepository.findById(user.getTeamId());
                if(oldTeamOptional.isPresent()){
                    oldTeamOptional.get().leaveTeam(id);
                    teamRepository.save(oldTeamOptional.get());
                }
                user.setTeamId(null);
            }

            user.setTeamId(teamId);
            user.getTeamRequests().removeIf(req -> req.getTeamId().equals(teamId));
            userRepository.save(user);
            teamRepository.save(t);
            return true;
        }
        return false;
    }

    @PostMapping("/team/declineTeamRequest/{id}/{teamId}")
    public void declineTeamRequest(@PathVariable String id, @PathVariable String teamId){
        User user = userRepository.findById(id).get();
        user.getTeamRequests().removeIf(req -> req.getTeamId().equals(teamId));
        userRepository.save(user);
    }

    @PostMapping("/team/leaveTeam/{id}/{teamId}")
    public void leaveTeam(@PathVariable String id, @PathVariable String teamId) {
        User user = userRepository.findById(id).get();
        Team team = teamRepository.findById(teamId).get();
        team.leaveTeam(id);
        teamRepository.save(team);
        if(team.getPlayerIds().size() == 0){
            teamRepository.delete(team);
        }

        user.setTeamId(null);
        userRepository.save(user);
    }
}
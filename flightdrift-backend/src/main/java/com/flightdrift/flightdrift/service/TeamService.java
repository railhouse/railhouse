package com.flightdrift.flightdrift.service;

import com.flightdrift.flightdrift.dto.team.CreateTeamRequest;
import com.flightdrift.flightdrift.dto.team.EditTeamRequest;
import com.flightdrift.flightdrift.dto.team.TeamResponse;
import com.flightdrift.flightdrift.entity.Account;
import com.flightdrift.flightdrift.entity.AccountTeam;
import com.flightdrift.flightdrift.entity.Team;
import com.flightdrift.flightdrift.entity.TeamRole;
import com.flightdrift.flightdrift.repository.AccountRepository;
import com.flightdrift.flightdrift.repository.AccountTeamRepository;
import com.flightdrift.flightdrift.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    private final AccountTeamRepository accountTeamRepository;

    private final AccountRepository accountRepository;

    @Transactional
    public TeamResponse createTeam(String username, CreateTeamRequest request) {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Team team = Team.builder()
                .name(request.name())
                .website(request.website())
                .description(request.description())
                .pictureLink(request.pictureLink())
                .build();
        team = teamRepository.save(team);

        AccountTeam accountTeam = AccountTeam.builder()
                .account(account)
                .team(team)
                .role(TeamRole.ADMIN)
                .build();
        accountTeamRepository.save(accountTeam);

        return TeamResponse.fromEntity(team);
    }

    @Transactional(readOnly = true)
    public List<TeamResponse> getTeams(String username) {
        List<AccountTeam> accountTeams = accountTeamRepository.findByAccountUsername(username);
        return accountTeams.stream()
                .map(AccountTeam::getTeam)
                .map(TeamResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public TeamResponse editTeam(String username, Long teamId, EditTeamRequest request) {
        AccountTeam accountTeam = accountTeamRepository.findByAccountUsernameAndTeamId(username, teamId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found or access denied"));

        if (accountTeam.getRole() != TeamRole.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only admins can edit team details");
        }

        Team team = accountTeam.getTeam();
        team.setName(request.name());
        team.setWebsite(request.website());
        team.setDescription(request.description());
        team.setPictureLink(request.pictureLink());
        team = teamRepository.save(team);

        return TeamResponse.fromEntity(team);
    }

    @Transactional
    public void deleteTeam(String username, Long teamId) {
        AccountTeam accountTeam = accountTeamRepository.findByAccountUsernameAndTeamId(username, teamId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found or access denied"));

        if (accountTeam.getRole() != TeamRole.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only admins can delete the team");
        }

        teamRepository.delete(accountTeam.getTeam());
    }


    @Transactional
    public void addAccountToTeam(String username, Long teamId, TeamRole role) {
        Account targetAccount = accountRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Target user not found"));

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found"));

        if (accountTeamRepository.findByAccountUsernameAndTeamId(username, teamId).isPresent()) {
            return; // Already in team
        }

        AccountTeam newAccountTeam = AccountTeam.builder()
                .account(targetAccount)
                .team(team)
                .role(role)
                .build();
        
        accountTeamRepository.save(newAccountTeam);
    }

    @Transactional
    public void leaveTeam(String requesterUsername, Long teamId) {
        AccountTeam accountTeam = accountTeamRepository.findByAccountUsernameAndTeamId(requesterUsername, teamId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "You are not a member of this team"));

        if (accountTeam.getRole() == TeamRole.ADMIN) {
            long adminCount = accountTeamRepository.countByTeamIdAndRole(teamId, TeamRole.ADMIN);
            if (adminCount <= 1) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot leave team: you are the last admin.");
            }
        }

        accountTeamRepository.delete(accountTeam);
    }

    @Transactional
    public void removeAccountFromTeam(String requesterUsername, Long teamId, String accountId) {
        AccountTeam requesterTeam = accountTeamRepository.findByAccountUsernameAndTeamId(requesterUsername, teamId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found or access denied"));

        if (requesterTeam.getRole() != TeamRole.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only admins can remove members");
        }

        AccountTeam targetTeam = accountTeamRepository.findByAccountUsernameAndTeamId(accountId, teamId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Target account is not a member of this team"));

        if (targetTeam.getRole() == TeamRole.ADMIN) {
            long adminCount = accountTeamRepository.countByTeamIdAndRole(teamId, TeamRole.ADMIN);
            if (adminCount <= 1) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot remove the last admin from the team.");
            }
        }

        accountTeamRepository.delete(targetTeam);
    }
}

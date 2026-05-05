package dev.jamius.weaver.service;

import dev.jamius.weaver.dto.team.CreateTeamRequest;
import dev.jamius.weaver.dto.team.EditTeamRequest;
import dev.jamius.weaver.dto.team.TeamResponse;
import dev.jamius.weaver.entity.Account;
import dev.jamius.weaver.entity.AccountTeam;
import dev.jamius.weaver.entity.Team;
import dev.jamius.weaver.entity.TeamRole;
import dev.jamius.weaver.repository.AccountRepository;
import dev.jamius.weaver.repository.AccountTeamRepository;
import dev.jamius.weaver.repository.TeamRepository;
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
}

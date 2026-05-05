package dev.jamius.weaver.controller;

import dev.jamius.weaver.dto.ApiResponse;
import dev.jamius.weaver.dto.team.CreateTeamRequest;
import dev.jamius.weaver.dto.team.EditTeamRequest;
import dev.jamius.weaver.dto.team.TeamResponse;
import dev.jamius.weaver.service.TeamService;
import dev.jamius.weaver.dto.team.TeamListResponse;
import dev.jamius.weaver.dto.team.TeamWrapperResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
@Tag(name = "Teams")
@SecurityRequirement(name = "bearerAuth")
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    @Operation(summary = "Create a team")
    public ResponseEntity<ApiResponse<?>> createTeam(
            Authentication authentication,
            @Valid @RequestBody CreateTeamRequest request) {

        String username = authentication.getName();
        TeamResponse team = teamService.createTeam(username, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Team created successfully", new TeamWrapperResponse(team)));
    }

    @GetMapping
    @Operation(summary = "Get all teams for the user")
    public ResponseEntity<ApiResponse<?>> getTeams(Authentication authentication) {
        String username = authentication.getName();
        List<TeamResponse> teams = teamService.getTeams(username);

        return ResponseEntity.ok(new ApiResponse<>(true, "Teams retrieved successfully", new TeamListResponse(teams)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Edit a team")
    public ResponseEntity<ApiResponse<?>> editTeam(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody EditTeamRequest request) {

        String username = authentication.getName();
        TeamResponse team = teamService.editTeam(username, id, request);

        return ResponseEntity.ok(new ApiResponse<>(true, "Team updated successfully", new TeamWrapperResponse(team)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a team")
    public ResponseEntity<ApiResponse<?>> deleteTeam(
            Authentication authentication,
            @PathVariable Long id) {

        String username = authentication.getName();
        teamService.deleteTeam(username, id);

        return ResponseEntity.ok(new ApiResponse<>(true, "Team deleted successfully", null));
    }

    @PostMapping("/{id}/leave")
    @Operation(summary = "Leave a team")
    public ResponseEntity<ApiResponse<?>> leaveTeam(
            Authentication authentication,
            @PathVariable Long id) {

        String requesterUsername = authentication.getName();
        teamService.leaveTeam(requesterUsername, id);

        return ResponseEntity.ok(new ApiResponse<>(true, "Left team successfully", null));
    }

    @DeleteMapping("/{id}/members/{usernameToRemove}")
    @Operation(summary = "Remove a member from a team")
    public ResponseEntity<ApiResponse<?>> removeAccountFromTeam(
            Authentication authentication,
            @PathVariable Long id,
            @PathVariable String usernameToRemove) {

        String requesterUsername = authentication.getName();
        teamService.removeAccountFromTeam(requesterUsername, id, usernameToRemove);

        return ResponseEntity.ok(new ApiResponse<>(true, "Member removed successfully", null));
    }
}

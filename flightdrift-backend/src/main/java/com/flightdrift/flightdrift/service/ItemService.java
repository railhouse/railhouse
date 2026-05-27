package com.flightdrift.flightdrift.service;

import com.flightdrift.flightdrift.dto.item.CreateItemRequest;
import com.flightdrift.flightdrift.dto.item.EditItemRequest;
import com.flightdrift.flightdrift.dto.item.ItemResponse;
import com.flightdrift.flightdrift.entity.AccountOrganizationMapping;
import com.flightdrift.flightdrift.entity.Item;
import com.flightdrift.flightdrift.entity.OrganizationRole;
import com.flightdrift.flightdrift.entity.Project;
import com.flightdrift.flightdrift.repository.AccountOrganizationMappingRepository;
import com.flightdrift.flightdrift.repository.ItemRepository;
import com.flightdrift.flightdrift.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.flightdrift.flightdrift.entity.OrganizationRole.ADMIN_USER;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/*
 * Author: Jamius Siam
 * Since: 27/05/2026
 */
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    private final ProjectRepository projectRepository;

    private final AccountOrganizationMappingRepository accountOrganizationMappingRepository;

    @Transactional
    public ItemResponse createItem(String username, UUID projectId, CreateItemRequest request) {
        Project project = getAccessibleProject(username, projectId);
        AccountOrganizationMapping accountOrganizationMapping = getMembership(username, project);

        if (accountOrganizationMapping.getRole() != ADMIN_USER) {
            throw new ResponseStatusException(FORBIDDEN, "Only admins can create items");
        }

        Item item = Item.builder()
                .name(request.name())
                .description(request.description())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .project(project)
                .build();

        item = itemRepository.save(item);

        return ItemResponse.fromEntity(item);
    }

    @Transactional(readOnly = true)
    public List<ItemResponse> getItems(String username, UUID projectId) {
        getAccessibleProject(username, projectId);

        List<Item> items = itemRepository.findByProjectId(projectId);

        return items.stream()
                .map(ItemResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public ItemResponse editItem(String username, UUID itemId, EditItemRequest request) {
        Item item = itemRepository
                .findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Item not found"));

        AccountOrganizationMapping accountOrganizationMapping = getMembership(username, item.getProject());

        if (accountOrganizationMapping.getRole() != ADMIN_USER) {
            throw new ResponseStatusException(FORBIDDEN, "Only admins can edit items");
        }

        item.setName(request.name());
        item.setDescription(request.description());
        item.setStartDate(request.startDate());
        item.setEndDate(request.endDate());

        item = itemRepository.save(item);

        return ItemResponse.fromEntity(item);
    }

    @Transactional
    public void deleteItem(String username, UUID itemId) {
        Item item = itemRepository
                .findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Item not found"));

        AccountOrganizationMapping accountOrganizationMapping = getMembership(username, item.getProject());

        if (accountOrganizationMapping.getRole() != ADMIN_USER) {
            throw new ResponseStatusException(FORBIDDEN, "Only admins can delete items");
        }

        itemRepository.delete(item);
    }

    private Project getAccessibleProject(String username, UUID projectId) {
        Project project = projectRepository
                .findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Project not found"));

        getMembership(username, project);

        return project;
    }

    private AccountOrganizationMapping getMembership(String username, Project project) {
        return accountOrganizationMappingRepository
                .findByAccountUsernameAndOrganizationId(username, project.getOrganization().getId())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Project not found or access denied"));
    }
}

package com.jcs.javacommunitysite.service;

import com.jcs.javacommunitysite.dto.community.CommunityDTO;
import com.jcs.javacommunitysite.dto.community.CreateCommunityRequest;
import com.jcs.javacommunitysite.dto.community.UpdateCommunityRequest;
import com.jcs.javacommunitysite.dto.post.PostDTO;
import com.jcs.javacommunitysite.model.Community;
import com.jcs.javacommunitysite.repository.CommunityRepository;
import com.jcs.javacommunitysite.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CommunityService {
    private final CommunityRepository communityRepo;
    private final PostRepository postRepo;

    public CommunityService(CommunityRepository communityRepo, PostRepository postRepo) {
        this.communityRepo = communityRepo;
        this.postRepo = postRepo;
    }

    public CommunityDTO createCommunity(CreateCommunityRequest request) {
        if(communityRepo.existsByName(request.getName())) {
            throw new IllegalArgumentException("Community name already in use");
        }

        Community community = Community.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();

        Community saved = communityRepo.save(community);

        return CommunityDTO.builder()
                .id(saved.getId())
                .name(saved.getName())
                .description(saved.getDescription())
                .build();
    }

    public CommunityDTO updateCommunity(UpdateCommunityRequest request, UUID communityId) {
        Community community = communityRepo.findById(communityId)
                .orElseThrow(() -> new IllegalArgumentException("Community not found"));

        if (request.getName() != null) {
            if (communityRepo.existsByName(request.getName())) {
                throw new IllegalArgumentException("Community name already in use");
            }
            community.setName(request.getName());
        }

        if (request.getDescription() != null) {
            community.setDescription(request.getDescription());
        }

        Community updated = communityRepo.save(community);

        return CommunityDTO.builder()
                .name(updated.getName())
                .description(updated.getDescription())
                .build();
    }

    public void deleteCommunity(UUID communityId) {

        Community community = communityRepo.findById((communityId))
                .orElseThrow(() -> new IllegalArgumentException("Community not found"));

        if(community != null){
            communityRepo.delete(community);
        }
    }

    public CommunityDTO getCommunityDetailsById(UUID communityId) {

        Community community = communityRepo.findById(communityId)
                .orElseThrow(() -> new IllegalArgumentException("Community not found"));

        return CommunityDTO.builder()
                .name(community.getName())
                .description(community.getDescription())
                .build();
    }

    public List<PostDTO> getAllCommunityPosts(UUID communityId){
        Community community = communityRepo.findById(communityId)
                .orElseThrow(() -> new IllegalArgumentException("Community not found"));

        return postRepo.findAllByCommunity(community).stream()
                .map(post -> PostDTO.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .createdAt(post.getCreatedAt())
                        .updatedAt(post.getUpdatedAt())
                        .build())
                .toList();
    }

}



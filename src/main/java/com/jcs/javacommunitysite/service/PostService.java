package com.jcs.javacommunitysite.service;

import com.jcs.javacommunitysite.dto.comment.CommentDTO;
import com.jcs.javacommunitysite.dto.post.CreatePostRequest;
import com.jcs.javacommunitysite.dto.post.PostDTO;
import com.jcs.javacommunitysite.dto.post.UpdatePostRequest;
import com.jcs.javacommunitysite.model.Community;
import com.jcs.javacommunitysite.model.Post;
import com.jcs.javacommunitysite.model.User;
import com.jcs.javacommunitysite.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PostService {

    private PostRepository postRepo;
    private UserRepository userRepo;
    private CommunityRepository communityRepo;
    private CommentRepository commentRepo;
    private VoteRepository voteRepo;

    public PostService(PostRepository postRepo, UserRepository userRepo, CommunityRepository communityRepo, CommentRepository commentRepo, VoteRepository voteRepo) {
        this.postRepo = postRepo;
        this.userRepo = userRepo;
        this.communityRepo = communityRepo;
        this.commentRepo = commentRepo;
        this.voteRepo = voteRepo;
    }

    public PostDTO createPost(CreatePostRequest request){

        User userPosting = userRepo.findById(UUID.fromString(request.getUserId()))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Community communityPosting = communityRepo.findById(UUID.fromString(request.getCommunityId()))
                .orElseThrow(() -> new IllegalArgumentException("Community not found"));

        Post post = Post.builder()
                .user(userPosting)
                .community(communityPosting)
                .title(request.getTitle())
                .content(request.getContent())
                .build();

        postRepo.save(post);

        return PostDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    public PostDTO updatePost(UpdatePostRequest request, UUID postId){

        Post postToUpdate = postRepo.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        if(request.getTitle() != null){
            postToUpdate.setTitle(request.getTitle());
        }

        if(request.getContent() != null){
            postToUpdate.setContent(request.getContent());
        }

        if(request.getContent() != null || request.getTitle() != null) {
            postRepo.save(postToUpdate);
        }

        return PostDTO.builder()
                .title(postToUpdate.getTitle())
                .content(postToUpdate.getContent())
                .createdAt(postToUpdate.getCreatedAt())
                .updatedAt(postToUpdate.getUpdatedAt())
                .build();
    }

    public void deletePost(UUID postId) {

        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        if(post != null){
            postRepo.delete(post);
        }
    }

    public PostDTO getPostDetailsById(UUID postId){

        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        return PostDTO.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    public List<CommentDTO> getAllPostComments(UUID postId){

        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        return commentRepo.findAllByPost(post).stream()
                .map(comment -> CommentDTO.builder()
                        .id(comment.getId())
                        .content(comment.getContent())
                        .createdAt(comment.getCreatedAt())
                        .updatedAt(comment.getUpdatedAt())
                        .build())
                .toList();
    }

    public Map<String, Object> getAllPostVotes(UUID postId){
        String upvote = "upvote";
        String downvote = "downvote";
        String postIdStr = "postId";

        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        long countUpvote = voteRepo.countByPostAndVoteType(post, (short) 1);
        long countDownvote = voteRepo.countByPostAndVoteType(post, (short) -1);

        Map<String, Object> allPostVotes = new HashMap<>();
        allPostVotes.put(upvote, countUpvote);
        allPostVotes.put(downvote, countDownvote);
        allPostVotes.put(postIdStr, postId);

        return allPostVotes;
    }
}

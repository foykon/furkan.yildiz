package com.furkan.project.comment.helper;

import com.furkan.project.comment.repository.CommentRepository;
import com.furkan.project.common.security.CurrentUserHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("commentAuth")
@RequiredArgsConstructor
public class CommentAuth {
    private final CommentRepository commentRepository;
    private final CurrentUserHelper current;

    public boolean canModify(Long commentId) {
        Long uid = current.getIdOrNull();
        if (uid == null) return false;
        if (current.hasRole("ROLE_ADMIN")) return true;
        return commentRepository.findById(commentId)
                .map(c -> uid.equals(c.getUserId()))
                .orElse(false);
    }
}

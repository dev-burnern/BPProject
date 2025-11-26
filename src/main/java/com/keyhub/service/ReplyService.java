package com.keyhub.service;

import java.util.List;
import com.keyhub.dao.ReplyDAO;
import com.keyhub.dto.ReplyDTO;

public class ReplyService {
    
    private ReplyDAO replyDAO = new ReplyDAO();

    public boolean addReply(ReplyDTO reply) {
        return replyDAO.insertReply(reply) > 0;
    }

    public List<ReplyDTO> getReplyList(int bNo) {
        return replyDAO.selectAllReplies(bNo);
    }

    public boolean removeReply(int rNo) {
        return replyDAO.deleteReply(rNo) > 0;
    }
}
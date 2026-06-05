package kr.or.bit.dto;

public class CommentDTO {
    private int commentId;
    private int boardId;
    private String writer;
    private String password;
    private String content;
    private String createdAt;
    private String updatedAt;

    public CommentDTO() {}

    public CommentDTO(int commentId, int boardId, String writer, String password, String content, String createdAt, String updatedAt) {
        this.commentId = commentId;
        this.boardId = boardId;
        this.writer = writer;
        this.password = password;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getBoardId() {
        return boardId;
    }

    public void setBoardId(int boardId) {
        this.boardId = boardId;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "CommentDTO [commentId=" + commentId + ", boardId=" + boardId + ", writer=" + writer + ", password="
                + password + ", content=" + content + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
    }
}

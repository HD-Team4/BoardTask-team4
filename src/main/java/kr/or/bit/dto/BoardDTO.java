package kr.or.bit.dto;

public class BoardDTO {
    private int boardId;
    private String writer;
    private String password;
    private String title;
    private String content;
    private int readCount;
    private int ref;
    private int reStep;
    private int reLevel;
    private String createdAt;
    private String updatedAt;

    public BoardDTO() {}

    public BoardDTO(int boardId, String writer, String password, String title, String content, int readCount,
                    int ref, int reStep, int reLevel, String createdAt, String updatedAt) {
        this.boardId = boardId;
        this.writer = writer;
        this.password = password;
        this.title = title;
        this.content = content;
        this.readCount = readCount;
        this.ref = ref;
        this.reStep = reStep;
        this.reLevel = reLevel;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }

    public int getRef() {
        return ref;
    }

    public void setRef(int ref) {
        this.ref = ref;
    }

    public int getReStep() {
        return reStep;
    }

    public void setReStep(int reStep) {
        this.reStep = reStep;
    }

    public int getReLevel() {
        return reLevel;
    }

    public void setReLevel(int reLevel) {
        this.reLevel = reLevel;
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
        return "BoardDTO [boardId=" + boardId + ", writer=" + writer + ", password=" + password + ", title=" + title
                + ", content=" + content + ", readCount=" + readCount + ", ref=" + ref + ", reStep=" + reStep
                + ", reLevel=" + reLevel + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
    }
}

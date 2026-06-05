-- 오라클 데이터베이스 계층형 게시판 및 댓글 테이블 설계

-- 1. 기존 테이블 및 시퀀스 제거 (기존에 있을 경우)
DROP TABLE reply CASCADE CONSTRAINTS;
DROP TABLE jspboard CASCADE CONSTRAINTS;
DROP SEQUENCE seq_jspboard_id;
DROP SEQUENCE seq_reply_id;

-- 2. 게시글(답글) 테이블 생성
CREATE TABLE jspboard (
    board_id NUMBER PRIMARY KEY,
    writer VARCHAR2(50) NOT NULL,
    password VARCHAR2(50) NOT NULL,
    title VARCHAR2(200) NOT NULL,
    content VARCHAR2(4000) NOT NULL,
    read_count NUMBER DEFAULT 0,
    ref NUMBER DEFAULT 0,
    re_step NUMBER DEFAULT 0,
    re_level NUMBER DEFAULT 0,
    created_at DATE DEFAULT SYSDATE,
    updated_at DATE DEFAULT SYSDATE
);

-- 게시글 일련번호 시퀀스 생성
CREATE SEQUENCE seq_jspboard_id
START WITH 1
INCREMENT BY 1
NOCACHE;

-- 3. 댓글 테이블 생성 (게시글에 종속)
CREATE TABLE reply (
    comment_id NUMBER PRIMARY KEY,
    board_id NUMBER NOT NULL,
    writer VARCHAR2(50) NOT NULL,
    password VARCHAR2(50) NOT NULL,
    content VARCHAR2(1000) NOT NULL,
    created_at DATE DEFAULT SYSDATE,
    updated_at DATE DEFAULT SYSDATE,
    CONSTRAINT fk_jspboard_board_id FOREIGN KEY (board_id) REFERENCES jspboard(board_id) ON DELETE CASCADE
);

-- 댓글 일련번호 시퀀스 생성
CREATE SEQUENCE seq_reply_id
START WITH 1
INCREMENT BY 1
NOCACHE;

// 헬퍼 함수: HTML escaping
function escapeHtml(str) {
    if (!str) return "";
    return str.replace(/&/g, "&amp;")
              .replace(/</g, "&lt;")
              .replace(/>/g, "&gt;")
              .replace(/"/g, "&quot;")
              .replace(/'/g, "&#039;");
}

// 헬퍼 함수: JS 문자열 escaping
function escapeJs(str) {
    if (!str) return "";
    return str.replace(/\\/g, "\\\\")
              .replace(/'/g, "\\'")
              .replace(/"/g, "\\\"")
              .replace(/\n/g, "\\n")
              .replace(/\r/g, "\\r");
}

let editModal;
let deleteModal;

// 1. 댓글 목록 불러오기
function loadComments() {
    const boardId = document.getElementById("commentBoardId").value;
    const listContainer = document.getElementById("commentList");

    fetch(`${contextPath}/comment/list.ajax?boardId=${boardId}`)
        .then(response => {
            if (!response.ok) throw new Error("네트워크 응답 오류");
            return response.json();
        })
        .then(data => {
            listContainer.innerHTML = "";
            if (data.length === 0) {
                listContainer.innerHTML = '<div class="text-center text-muted py-4">아직 등록된 댓글이 없습니다.</div>';
                return;
            }
            data.forEach(comment => {
                const item = document.createElement("div");
                item.className = "comment-item d-flex flex-column flex-md-row justify-content-between gap-3";
                
                // HTML을 동적으로 삽입하면서 XSS 방지를 위해 escapeHtml 처리
                item.innerHTML = `
                    <div>
                        <strong>${escapeHtml(comment.writer)}</strong>
                        <time class="ms-2">${comment.createdAt}</time>
                        <p class="comment-content mb-0">${escapeHtml(comment.content)}</p>
                    </div>
                    <div class="comment-actions align-self-start">
                        <button type="button" class="btn btn-outline-primary btn-sm rounded-pill px-3" 
                            onclick="openEditModal(${comment.commentId}, '${escapeJs(comment.writer)}', '${escapeJs(comment.content)}')">수정</button>
                        <button type="button" class="btn btn-outline-danger btn-sm rounded-pill px-3" 
                            onclick="openDeleteModal(${comment.commentId})">삭제</button>
                    </div>
                `;
                listContainer.appendChild(item);
            });
        })
        .catch(err => {
            console.error(err);
            listContainer.innerHTML = '<div class="text-center text-danger py-4">댓글을 불러오는 중 오류가 발생했습니다.</div>';
        });
}

// 2. 댓글 등록 처리
function submitComment() {
    const boardId = document.getElementById("commentBoardId").value;
    const writer = document.getElementById("commentWriter").value;
    const password = document.getElementById("commentPassword").value;
    const content = document.getElementById("commentContent").value;

    const params = new URLSearchParams();
    params.append("boardId", boardId);
    params.append("writer", writer);
    params.append("password", password);
    params.append("content", content);

    fetch(`${contextPath}/comment/write.ajax`, {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: params.toString()
    })
    .then(response => response.json())
    .then(data => {
        if (data.result === "success") {
            // 등록 성공 시 폼 초기화 및 목록 갱신
            document.getElementById("commentWriter").value = "";
            document.getElementById("commentPassword").value = "";
            document.getElementById("commentContent").value = "";
            loadComments();
        } else {
            alert("댓글 등록에 실패했습니다.");
        }
    })
    .catch(err => {
        console.error(err);
        alert("서버 통신 중 오류가 발생했습니다.");
    });
}

// 3. 댓글 수정 모달 열기
function openEditModal(commentId, writer, content) {
    document.getElementById("editCommentId").value = commentId;
    document.getElementById("editCommentWriter").value = writer;
    document.getElementById("editCommentContent").value = content;
    document.getElementById("editCommentPassword").value = "";
    editModal.show();
}

// 4. 댓글 수정 요청
function submitEditComment() {
    const commentId = document.getElementById("editCommentId").value;
    const writer = document.getElementById("editCommentWriter").value;
    const content = document.getElementById("editCommentContent").value;
    const password = document.getElementById("editCommentPassword").value;

    const params = new URLSearchParams();
    params.append("commentId", commentId);
    params.append("writer", writer);
    params.append("content", content);
    params.append("password", password);

    fetch(`${contextPath}/comment/modify.ajax`, {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: params.toString()
    })
    .then(response => response.json())
    .then(data => {
        if (data.result === "success") {
            editModal.hide();
            loadComments();
        } else {
            alert("비밀번호가 일치하지 않거나 수정에 실패했습니다.");
        }
    })
    .catch(err => {
        console.error(err);
        alert("수정 처리 중 오류가 발생했습니다.");
    });
}

// 5. 댓글 삭제 모달 열기
function openDeleteModal(commentId) {
    document.getElementById("deleteCommentId").value = commentId;
    document.getElementById("deleteCommentPassword").value = "";
    deleteModal.show();
}

// 6. 댓글 삭제 요청
function submitDeleteComment() {
    const commentId = document.getElementById("deleteCommentId").value;
    const password = document.getElementById("deleteCommentPassword").value;

    const params = new URLSearchParams();
    params.append("commentId", commentId);
    params.append("password", password);

    fetch(`${contextPath}/comment/delete.ajax`, {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: params.toString()
    })
    .then(response => response.json())
    .then(data => {
        if (data.result === "success") {
            deleteModal.hide();
            loadComments();
        } else {
            alert("비밀번호가 일치하지 않거나 삭제에 실패했습니다.");
        }
    })
    .catch(err => {
        console.error(err);
        alert("삭제 처리 중 오류가 발생했습니다.");
    });
}

// 초기 로딩
document.addEventListener("DOMContentLoaded", () => {
    editModal = new bootstrap.Modal(document.getElementById('commentEditModal'));
    deleteModal = new bootstrap.Modal(document.getElementById('commentDeleteModal'));

    // 댓글 작성 이벤트 바인딩
    const commentForm = document.getElementById("commentForm");
    if (commentForm) {
        commentForm.addEventListener("submit", (e) => {
            e.preventDefault();
            submitComment();
        });
    }

    // 댓글 수정 이벤트 바인딩
    const editForm = document.getElementById("commentEditForm");
    if (editForm) {
        editForm.addEventListener("submit", (e) => {
            e.preventDefault();
            submitEditComment();
        });
    }

    // 댓글 삭제 이벤트 바인딩
    const deleteForm = document.getElementById("commentDeleteForm");
    if (deleteForm) {
        deleteForm.addEventListener("submit", (e) => {
            e.preventDefault();
            submitDeleteComment();
        });
    }

    loadComments();
});

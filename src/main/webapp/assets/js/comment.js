// HTML escaping helper
function escapeHtml(str) {
    if (!str) return "";
    return String(str).replace(/&/g, "&amp;")
                      .replace(/</g, "&lt;")
                      .replace(/>/g, "&gt;")
                      .replace(/"/g, "&quot;")
                      .replace(/'/g, "&#039;");
}

// JavaScript string escaping helper
function escapeJs(str) {
    if (!str) return "";
    return String(str).replace(/\\/g, "\\\\")
                      .replace(/'/g, "\\'")
                      .replace(/"/g, "\\\"")
                      .replace(/\n/g, "\\n")
                      .replace(/\r/g, "\\r");
}

let editModal;
let deleteModal;

function loadComments() {
    const boardId = $("#commentBoardId").val();
    const listContainer = document.getElementById("commentList");

    $.ajax({
        url: `${contextPath}/comment/list.ajax`,
        type: "GET",
        dataType: "json",
        data: { boardId: boardId },
        success: function(data) {
            listContainer.innerHTML = "";
            if (data.length === 0) {
                listContainer.innerHTML = '<div class="text-center text-muted py-4">?? ??? ??? ????.</div>';
                return;
            }

            data.forEach(comment => {
                const item = document.createElement("div");
                item.className = "comment-item d-flex flex-column flex-md-row justify-content-between gap-3";
                item.innerHTML = `
                    <div>
                        <strong>${escapeHtml(comment.writer)}</strong>
                        <time class="ms-2">${comment.createdAt}</time>
                        <p class="comment-content mb-0">${escapeHtml(comment.content)}</p>
                    </div>
                    <div class="comment-actions align-self-start">
                        <button type="button" class="btn btn-outline-primary btn-sm rounded-pill px-3"
                            onclick="openEditModal(${comment.commentId}, '${escapeJs(comment.writer)}', '${escapeJs(comment.content)}')">??</button>
                        <button type="button" class="btn btn-outline-danger btn-sm rounded-pill px-3"
                            onclick="openDeleteModal(${comment.commentId})">??</button>
                    </div>
                `;
                listContainer.appendChild(item);
            });
        },
        error: function(xhr, status, error) {
            console.error(error || status);
            listContainer.innerHTML = '<div class="text-center text-danger py-4">??? ???? ? ??? ??????.</div>';
        }
    });
}

function submitComment() {
    $.ajax({
        url: `${contextPath}/comment/write.ajax`,
        type: "POST",
        dataType: "json",
        data: {
            boardId: $("#commentBoardId").val(),
            writer: $("#commentWriter").val(),
            password: $("#commentPassword").val(),
            content: $("#commentContent").val()
        },
        success: function(data) {
            if (data.result === "success") {
                $("#commentWriter").val("");
                $("#commentPassword").val("");
                $("#commentContent").val("");
                loadComments();
            } else {
                alert("?? ??? ??????.");
            }
        },
        error: function(xhr, status, error) {
            console.error(error || status);
            alert("?? ?? ? ??? ??????.");
        }
    });
}

function openEditModal(commentId, writer, content) {
    $("#editCommentId").val(commentId);
    $("#editCommentWriter").val(writer);
    $("#editCommentContent").val(content);
    $("#editCommentPassword").val("");
    editModal.show();
}

function submitEditComment() {
    $.ajax({
        url: `${contextPath}/comment/modify.ajax`,
        type: "POST",
        dataType: "json",
        data: {
            commentId: $("#editCommentId").val(),
            writer: $("#editCommentWriter").val(),
            content: $("#editCommentContent").val(),
            password: $("#editCommentPassword").val()
        },
        success: function(data) {
            if (data.result === "success") {
                editModal.hide();
                loadComments();
            } else {
                alert("????? ???? ??? ??? ??????.");
            }
        },
        error: function(xhr, status, error) {
            console.error(error || status);
            alert("?? ?? ? ??? ??????.");
        }
    });
}

function openDeleteModal(commentId) {
    $("#deleteCommentId").val(commentId);
    $("#deleteCommentPassword").val("");
    deleteModal.show();
}

function submitDeleteComment() {
    $.ajax({
        url: `${contextPath}/comment/delete.ajax`,
        type: "POST",
        dataType: "json",
        data: {
            commentId: $("#deleteCommentId").val(),
            password: $("#deleteCommentPassword").val()
        },
        success: function(data) {
            if (data.result === "success") {
                deleteModal.hide();
                loadComments();
            } else {
                alert("????? ???? ??? ??? ??????.");
            }
        },
        error: function(xhr, status, error) {
            console.error(error || status);
            alert("?? ?? ? ??? ??????.");
        }
    });
}

$(function() {
    editModal = new bootstrap.Modal(document.getElementById("commentEditModal"));
    deleteModal = new bootstrap.Modal(document.getElementById("commentDeleteModal"));

    $("#commentForm").on("submit", function(e) {
        e.preventDefault();
        submitComment();
    });

    $("#commentEditForm").on("submit", function(e) {
        e.preventDefault();
        submitEditComment();
    });

    $("#commentDeleteForm").on("submit", function(e) {
        e.preventDefault();
        submitDeleteComment();
    });

    loadComments();
});

$(function () {
    init()

    $("#deleteAttatchment").on("click", delAttatchment)
    $("#replySubmitBtn").on("click", addReply)
})

function init() {
    const boardSeqNo = $("#boardSeqNo").val()
    const replyContainer = $(".reply-controller")

    $.ajax({
        url: "/reply/list",
        method: "get",
        data: {boardSeqNo: boardSeqNo},
        success: function (resp) {
            const loginId = $("#loginId").val()
            let replyTable = `
            <table class="reply-table">
                <thead>
                    <tr>
                        <th class="reply-writer">글쓴이</th>
                        <th class="reply-content">댓글</th>
                        <th class="reply-createdAt">날짜</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody id="reply-tbody">`
            $.each(resp, (idx, el) => {
                replyTable += `
                    <tr>
                        <td class="reply-writer">${el.writer}</td>
                        <td class="reply-content">${el.content}</td>
                        <td class="reply-createdAt">${el.createdAt.replace("T", " ")}</td>
                        <td class="reply-btns">
                            <input type="button" value="수정하기" data-seq="${el.seqNo}" class="btn btn-info replyUpdateProcBtn" style="display: none">
                            <input type="button" value="수정취소" data-seq="${el.seqNo}" class="btn btn-warning cancelBtn" style="display: none">
                            <input type="button" value="수정" data-seq="${el.seqNo}" class="btn btn-secondary replyUpdateBtn" ${loginId == el.writer ? "" : "disabled"}>
                            <input type="button" value="삭제" data-seq="${el.seqNo}" class="btn btn-danger delReplyBtn" ${loginId == el.writer ? "" : "disabled"}>
                        </td>
                    </tr>
                `
            })
            replyTable += "</tbody></table>"
            replyContainer.html(replyTable)
            $(".replyUpdateProcBtn").on("click", replyUpdateProc)
            $(".cancelBtn").on("click", replyUpdateCancel)
            $(".replyUpdateBtn").on("click", replyUpdate)
            $(".delReplyBtn").on("click", delReply)
        }
    })
}

function delAttatchment() {
    const answer = confirm("정말로 삭제하시겠습니까?")
    
    if(answer) {
        const seqNo = $(this).attr("data-seq")
        const searchTheme = $(this).attr("data-searchTheme")
        const searchWord = $(this).attr("data-searchWord")

        location.href = `/board/file/delete?seqNo=${seqNo}&searchTheme=${searchTheme}&searchWord=${searchWord}`
        alert("삭제되었습니다.")
    } else {
        alert("취소했습니다.")
    }
}

function addReply() {
    const userId = $(this).attr("data-user")
    if (userId=="anonymousUser") {
        const conf = confirm("로그인이 필요합니다.")

        if (conf)
            location.href = "/user/login"

        return 
    }
    const content = $("#replyContent").val()
    if (content.trim().length == 0) {
        alert("댓글을 입력해 주세요.")

        return
    }
    const boardSeqNo = $("#boardSeqNo").val()

    $.ajax({
        url: "/reply/write",
        method: "post",
        data: {boardSeqNo: boardSeqNo, content: content},
        success: (resp) => {
            let replyTable = $("#reply-tbody")
            const createdReply = `
            <tr>
                <td class="reply-writer">${resp.writer}</td>
                <td class="reply-content">${resp.content}</td>
                <td class="reply-createdAt">${resp.createdAt}</td>
                <td class="reply-btns">
                    <input type="button" value="수정하기" data-seq="${resp.seqNo}" class="btn btn-info replyUpdateProcBtn" style="display: none">
                    <input type="button" value="수정취소" data-seq="${resp.seqNo}" class="btn btn-warning cancelBtn" style="display: none">
                    <input type="button" value="수정" data-seq="${resp.seqNo}" class="btn btn-secondary replyUpdateBtn" ${userId == resp.writer ? "" : "disabled"}>
                    <input type="button" value="삭제" data-seq="${resp.seqNo}" class="btn btn-danger delReplyBtn" ${userId == resp.writer ? "" : "disabled"}>
                </td>
            </tr>`

            replyTable.append(createdReply)
            $(".replyUpdateProcBtn").on("click", replyUpdateProc)
            $(".cancelBtn").on("click", replyUpdateCancel)
            $(".replyUpdateBtn").on("click", replyUpdate)
            $(".delReplyBtn").on("click", delReply)
            $("#replyContent").val("")
        }
    })
}

function delReply() {
    const seqNo = $(this).attr("data-seq")
    const answer = confirm("삭제하시겠습니까?")
    if (answer) {
        $.ajax({
            url: `/reply/${seqNo}/delete`,
            method: "get",
            success: init
        })
    }
}

function replyUpdate() {
    const seqNo = $(this).attr("data-seq")
    let oldContentTag = $(this).parent().parent().children("td.reply-content")
    const oldContentText = oldContentTag.text()
    oldContentTag.attr("data-prevContent", oldContentText)
    console.log(oldContentText);
    oldContentTag.html(`<input type="text" value="${oldContentText}" style="width: 500px" id="updateReplyContent">`)

    $(".replyUpdateProcBtn").filter(`[data-seq=${seqNo}]`).css({"display": ""})
    $(this).css({"display": "none"})

    $(".cancelBtn").filter(`[data-seq=${seqNo}]`).css({"display": ""})
    $(".delReplyBtn").filter(`[data-seq=${seqNo}]`).css({"display": "none"})
}

function replyUpdateCancel() {
    const seqNo = $(this).attr("data-seq")
    let contentTag = $(this).parent().parent().children("td.reply-content")
    let prevContent = contentTag.attr("data-prevContent")
    contentTag.html(prevContent)

    $(".replyUpdateProcBtn").filter(`[data-seq=${seqNo}]`).css({"display": "none"})
    $(".replyUpdateBtn").filter(`[data-seq=${seqNo}]`).css({"display": ""})

    $(this).css({"display": "none"})
    $(".delReplyBtn").filter(`[data-seq=${seqNo}]`).css({"display": ""})
}

function replyUpdateProc() {
    const seqNo = $(this).attr("data-seq")
    const newContent = $("#updateReplyContent").val()
    $.ajax({
        url: `/reply/${seqNo}/update`,
        method: "post",
        data: {content: newContent},
        success: init
    })
}
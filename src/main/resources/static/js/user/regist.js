$(function () {
    let isDuplicated = true
    let isAvailableId = false
    let isAvailablePwd = false

    $("#userId").on("keyup", availableIdCheck)
    $("#dupCheckBtn").on("click", idDupCheck)
    $("#userPwd").on("focus", () => {
        $("#userPwdCheck").val("");
    })
    $("#userPwdCheck").on("keyup", confirmPwd)
    $("#submitBun").on("click", registId)
})

function availableIdCheck() {
    let userId = $("#userId").val()
    let resSpanTag = $("#idDupCheckResult")

    if (6 <= userId.length && userId.length <= 12) {
        resSpanTag.html("사용할 수 있는 아이디입니다.")
        resSpanTag.css("font-size", "0.8em")
        resSpanTag.css("color", "blue")

        isAvailableId = true
    } else {
        resSpanTag.html("아이디는 6자 이상 12자 이하 입니다.")
        resSpanTag.css("font-size", "0.8em")
        resSpanTag.css("color", "red")

        isAvailableId = false
    }
    isDuplicated = true

    return
}

function idDupCheck() {
    const userId = $("#userId").val();
    let resSpanTag = $("#idDupCheckResult")

    if (6 <= userId.length && userId.length <= 12) {
        $.ajax({
            url: "/user/idDupCheck",
            method: "post",
            data: {"userId": userId},
            success: (resp) => {
                isDuplicated = resp

                if(isDuplicated) {
                    resSpanTag.html("사용할 수 없는 아이디입니다.")
                    resSpanTag.css("font-size", "0.8em")
                    resSpanTag.css("color", "red")
                } else {
                    resSpanTag.html("사용할 수 있는 아이디입니다.")
                    resSpanTag.css("font-size", "0.8em")
                    resSpanTag.css("color", "blue")
                }
            }
        })
    7}
    resSpanTag.html("사용할 수 없는 아이디입니다.")
    resSpanTag.css("font-size", "0.8em")
    resSpanTag.css("color", "red")

    return
}

function confirmPwd() {
    const userPwd = $("#userPwd").val()
    const userPwdCheck = $("#userPwdCheck").val()
    let pwdCheckResSpan = $("#pwdCheckResult")
    
    if (userPwd == userPwdCheck) {
        isAvailablePwd = true
        pwdCheckResSpan.html("동일한 비밀번호 입니다.")
        pwdCheckResSpan.css("font-size", "0.8em")
        pwdCheckResSpan.css("color", "blue")
    } else {
        isAvailablePwd = false
        pwdCheckResSpan.html("동일하지 않은 비밀번호 입니다.")
        pwdCheckResSpan.css("font-size", "0.8em")
        pwdCheckResSpan.css("color", "red")
    }
}

function registId() {
    if (isDuplicated) {
        alert("중복 체크를 해주세요.")

        return false
    }
    if (!isAvailableId) {
        alert("유효한 아이디가 아닙니다.")

        return false
    }
    if (!isAvailablePwd) {
        alert("비밀번호가 다릅니다.")

        return false
    }
    if ($("userName").val().trim() < 1) {
        alert("유효하지 않은 이름입니다.")

        return false
    }
    let email = $("email").val()
    if (email.trim() < 1 || !email.includs("@")) {
        alert("유효하지 않은 이메일입니다.")

        return false
    }

    return true
}
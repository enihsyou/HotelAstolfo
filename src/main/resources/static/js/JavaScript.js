//延时函数
function sleep(time) {
    return new Promise((resolve, reject) => {
        setTimeout(resolve, time);
    });
}

//标题滚动
async function titleScroller() {
    let index = 0;
    do {
        await sleep(250);
        document.title = document.title.substr(1) + document.title.substr(0, 1);
    } while (++index < document.title.length);
    setTimeout(titleScroller, 2000);
}

//右上角登录入口按钮
$('.login_button').click((e) => {
    let login = $('.login');
    let signup = $('.signup');
    if (login.css('display') === 'none' && signup.css('display') === 'none') {
        login.fadeIn(800);
        $('.main').css('filter', 'blur(20px)');
    }
    else {
        login.fadeOut(500);
        signup.fadeOut(500);
        $('.main').css('filter', 'none');
    }
});

//登录界面注册按钮
$('.login .window .loginFooter .signup_button').click(function () {
    let signup = $('.signup');
    if (signup.css('display') === 'none') {
        $('.login').fadeOut(500);
        signup.fadeIn(500);
    }
    else {
        signup.fadeOut(500);
    }
});

//登录界面确认登录按钮
$('.login .window .confirm').click(function () {
    if (isLogin) return;
    let _this = this;
    let username = $('#inputUserName');
    let password = $('#inputPassword');
    if (username.val().length > 0 && password.val().length > 0) {
        if (!isLogin) {
            isLogin = true;
            $(_this).append('<span class="loading line"></span>');
        }
        $.ajax({
            url: '/api/users/login',
            type: 'POST',
            contentType: "application/json; charset=UTF-8",
            data: JSON.stringify({
                phone_number: username.val(),
                password: sha256(password.val())
            }),
            success: function () {
                isLogin = false;
                //TODO dateStructure of callback
                $(_this).children('.loading').remove();
            },
            error: function (err) {
                showMsg('网络错误！');
                isLogin = false;
                $(_this).children('.loading').remove();
            }
        });
    } else {
        showMsg('用户名和密码不能为空');
    }
});

//注册界面确认注册按钮
$('.signup .window .confirm').click(function () {
    if (isSignup) return;
    let _this = this;
    let username = $('#signupUsername');
    let nickname = $('#signupNickname');
    let password = $('#signupPassword');
    let passwordAgain = $('#signupPasswordAgain');
    if (username.val().length > 0 && password.val().length > 0 && passwordAgain.val().length > 0) {
        if (password.val() !== passwordAgain.val()) {
            showMsg('两次输入密码不相符');
            return;
        }
        if (!isSignup) {
            isSignup = true;
            $(_this).append('<span class="loading line"></span>');
        }
        $.ajax({
            url: '/api/users/signup',
            type: 'POST',
            contentType: "application/json; charset=UTF-8",
            data: JSON.stringify({
                phone_number: username.val(),
                nickname: nickname.val(),
                password: sha256(password.val())
            }),
            success: function () {
                isSignup = false;
                //TODO dateStructure of callback
                $(_this).children('.loading').remove();
            },
            error: function (err) {
                showMsg('网络错误！');
                isSignup = false;
                $(_this).children('.loading').remove();
            }
        });
    } else {
        showMsg('用户名和密码不能为空');
    }
});

//登录、注册界面关闭图标动作
$('.window .close').click(function () {
    $(this).parent().parent().fadeOut();
    isLogin = false;
    isSignup = false;
    $('.main').css('filter', 'none');
});

//首页搜索框确认动作
$('.searchBox .confirm').click(function () {
    if (isSearch) return;
    let _this = this;
    let searchStart = $('#bookingStart');
    let searchEnd = $('#bookingEnd');
    if (searchStart.val().length > 0 && searchEnd.val().length > 0) {
        if (new Date(searchStart.val()) >= new Date(searchEnd.val())) {
            showMsg('离店时间必须晚于预定时间');
            return;
        }
        if (!isSearch) {
            isSearch = true;
            $(_this).append('<span class="loading line"></span>');
        }
        $.ajax({
            url: '/api/search',
            type: 'POST',
            contentType: "application/json; charset=UTF-8",
            data: JSON.stringify({
                searchStart: searchStart.val(),
                searchEnd: searchEnd.val(),
                searchType: $('#bookingType').val()
            }),
            success: function () {
                isSignup = false;
                //TODO dateStructure of callback
                $(_this).children('.loading').remove();
            },
            error: function (err) {
                showMsg('网络错误！');
                isSearch = false;
                $(_this).children('.loading').remove();
            }
        });
    } else {
        showMsg('请输入完整的入住日期和离店日期');
    }
});

//封装消息提示
async function showMsg(msg) {
    let newMsg = $(`<div class="msg untouchable"><span>${msg}</span></div>`);
    $('.main').after(newMsg);
    newMsg.slideDown(400);
    await sleep(Math.max(1000, msg.length * 100));
    newMsg.slideUp(300);
    await sleep(400);
    newMsg.remove();
}

//初始化
(function init() {
    titleScroller();
    $('#bookingStart').val(new Date().toLocaleDateString().replace(/\//g, '-'));
    $('#bookingEnd').val(new Date(new Date().getTime() + 24 * 60 * 60 * 1000).toLocaleDateString().replace(/\//g, '-'));
})();

let isLogin = false;//登录确认按钮状态
let isSignup = false;//注册确认按钮状态
let isSearch = false;//搜索确认按钮状态
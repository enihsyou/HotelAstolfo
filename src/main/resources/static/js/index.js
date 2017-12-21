//右上角登录入口按钮
$('.login-btn').click((e) => {
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

//登出按钮动作
$('.user-btn').click((e) => {
    location.href = 'management.html'
});

//登出按钮动作
$('.logout-btn').click((e) => {
    localStorage.clear();
    sessionStorage.clear();
    location.href = '/'
});

//登录界面注册按钮
$('.login .window .loginFooter .signup-btn').click(function () {
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
    if ($(this).children('span').hasClass('loading')) {
        return;
    }
    let _this = this;
    let username = $('#inputUserName');
    let password = $('#inputPassword');
    if (username.val().length > 0 && password.val().length > 0) {
        $(_this).append('<span class="loading line"></span>');
        $.ajax({
            url: '/api/users/login',
            type: 'POST',
            contentType: "application/json; charset=UTF-8",
            data: JSON.stringify({
                phone_number: username.val(),
                password: sha256(password.val())
            }),
            success: function (data, textStatus, jqXHR) {
                //TODO dateStructure of callback
                $(_this).children('.loading').remove();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                showMsg('网络错误！');
                $(_this).children('.loading').remove();
            }
        });
    } else {
        showMsg('用户名和密码不能为空');
    }
});

//注册界面确认注册按钮
$('.signup .window .confirm').click(function () {
    if ($(this).children('span').hasClass('loading')) {
        return;
    }
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
        $(_this).append('<span class="loading line"></span>');
        $.ajax({
            url: '/api/users/signup',
            type: 'POST',
            contentType: "application/json; charset=UTF-8",
            data: JSON.stringify({
                phone_number: username.val(),
                nickname: nickname.val(),
                password: sha256(password.val())
            }),
            success: function (data, textStatus, jqXHR) {
                //TODO dateStructure of callback
                $(_this).children('.loading').remove();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                showMsg('网络错误！');
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
    $('.loading').remove();
    $('.main').css('filter', 'none');
});

//首页搜索框确认动作
$('.searchBox .confirm').click(function () {
    if ($(this).children('span').hasClass('loading')) {
        return;
    }
    let _this = this;
    let searchStart = $('#bookingStart');
    let searchEnd = $('#bookingEnd');
    if (searchStart.val().length > 0 && searchEnd.val().length > 0) {
        if (new Date(searchStart.val()) >= new Date(searchEnd.val())) {
            showMsg('离店时间必须晚于预定时间');
            return;
        }
        $(_this).append('<span class="loading line"></span>');
        $.ajax({
            url: '/api/search',
            type: 'POST',
            contentType: "application/json; charset=UTF-8",
            data: JSON.stringify({
                searchStart: searchStart.val(),
                searchEnd: searchEnd.val(),
                searchType: $('#bookingType').val()
            }),
            success: function (data, textStatus, jqXHR) {
                //TODO dateStructure of callback
                $(_this).children('.loading').remove();
                //显示右侧搜索列表
                $('.slider_container').css('filter', 'blur(10px)');
                $('.searchBox').css('left', '5%');
                $('.searchList').slideDown(333);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                showMsg('网络错误！');
                $(_this).children('.loading').remove();
            }
        });
    } else {
        showMsg('请输入完整的入住日期和离店日期');
    }
});

//初始化
$(function init() {
        //开始标题滚动
        titleScroller();
        //初始化首页搜索
        let time = new Date();
        let year = time.getFullYear();
        let month = time.getMonth();
        let day = time.getDay() > 9 ? time.getDay() : '0' + time.getDay();
        $('#bookingStart').val(`${year}-${month}-${day}`)
            .attr('min', `${year}-${month}-${day}`)
            .attr('max', `${year + 1}-${month}-${day}`);
        let nday = (time.getDay() + 1) > 9 ? (time.getDay() + 1) : '0' + (time.getDay() + 1);
        $('#bookingEnd').val(`${year}-${month}-${nday}`)
            .attr('min', `${year}-${month}-${nday}`)
            .attr('max', `${year + 1}-${month}-${nday}`);
        //初始化顶栏
        let nickname = sessionStorage.nickname || localStorage.nickname;
        if (nickname != null && nickname.length > 0) {
            showMsg('欢迎回来~');
            $('.user-info .user-btn').html();
            $('.user-info ').show();
        }
        else {
            $('.login-btn').show();
        }
    }
);


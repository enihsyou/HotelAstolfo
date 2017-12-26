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
            url: `${serverHost}/api/users/login`,
            type: 'POST',
            contentType: "application/json; charset=UTF-8",
            data: JSON.stringify({
                phoneNumber: username.val(),
                password: sha256(password.val())
            }),
            success: function (data, textStatus, jqXHR) {
                /*{
                    "id": 1,
                    "phoneNumber": "18834321240",
                    "nickname": "temp",
                    "password": "ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad",
                    "register_date": "2017-12-21T22:40:24.289",
                    "role": "注册用户",
                    "guests": []
                }*/
                showMsg(`亲爱的${data.nickname},欢迎你回到阿福旅店！`).then(() => {
                    if ($(".checkbox input").prop('checked')) {
                        localStorage.username = data.phoneNumber;
                        localStorage.password = data.password;
                        localStorage.nickname = data.nickname;
                    } else {
                        sessionStorage.username = data.phoneNumber;
                        sessionStorage.password = data.password;
                        sessionStorage.nickname = data.nickname;
                    }
                    sessionStorage.role = data.role;
                    location.href = '/';
                });
            },
            error: function (jqXHR, textStatus, errorThrown) {
                let msg;
                switch (jqXHR.status) {
                    case 404:
                        msg = '当前用户不存在';
                        break;
                    case 400:
                        msg = '密码错误';
                        break;
                    default:
                        msg = '网络错误'
                }
                showMsg(msg);
            },
            complete: function () {
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
            url: `${serverHost}/api/users/make`,
            type: 'POST',
            contentType: "application/json; charset=UTF-8",
            data: JSON.stringify({
                phoneNumber: username.val(),
                password: password.val(),
                nickname: nickname.val()
            }),
            success: function (data, textStatus, jqXHR) {
                /*{
                    "id": 1,
                    "phoneNumber": "18834321240",
                    "nickname": "temp",
                    "password": "ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad",
                    "register_date": "2017-12-21T22:40:24.289",
                    "role": "注册用户",
                    "guests": []
                }*/
                showMsg(`亲爱的${data.nickname},欢迎你来到阿福旅店！`).then(() => {
                    sessionStorage.username = data.phoneNumber;
                    sessionStorage.password = data.password;
                    sessionStorage.nickname = data.nickname;
                    sessionStorage.role = data.role;
                    location.href = '/';
                });
            },
            error: function (jqXHR, textStatus, errorThrown) {
                let msg;
                switch (jqXHR.status) {
                    case 409:
                        msg = '用户已存在';
                        break;
                    default:
                        msg = '网络错误'
                }
                showMsg(msg);
            },
            complete: function () {
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

//搜索框关闭图标动作
$('.searchList .close').click();

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
    //获取首页搜索选项
    $.ajax({
        url: `${serverHost}/api/rooms/load`,
        type: 'GET',
        dataType: 'json',
        contentType: "application/json; charset=UTF-8",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Authorization", "Basic " + btoa(sessionStorage.username || localStorage.username + ":" + sessionStorage.password || localStorage.password));
        },
        success: function (data, textStatus, jqXHR) {
            searchBoxVue.update(data);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            showMsg('连接服务器失败')
        },
        complete: function () {
            searchBoxVue.isLoaded = true;
        }
    });
    //初始化顶栏
    reqLogin(sessionStorage.username || localStorage.username, sessionStorage.password || localStorage.password)
        .then((data) => {
            //将接受到的数据解析
            sessionStorage.nickname = data.nickname;
            $('.user-info .user-btn').html(sessionStorage.nickname || localStorage.nickname);
            $('.user-info ').show();
        }, (error) => {
            if (error !== 'EMPTY_USERNAME_OR_PASSWORD') {
                console.error('autoLogin:', error);
                showMsg(error)
            }
            $('.login-btn').show();
        });
});

let searchBoxVue = new Vue({
    el: '.searchBox',
    data: {
        type: [],
        direction: [],
        floorAndNumber: {},
        number: [],
        isLoaded: false
    },
    methods: {
        update: function (data) {
            this.type = data.types;
            this.direction = data.directions;
            this.floorAndNumber = data.rooms;
        },
        updateNumber: function (e) {
            let sfloor = $('#bookingFloor').val();
            if (sfloor.length != null) {
                this.number = this.floorAndNumber[sfloor]
            }
        },
        search: function (e) {
            let _this = e.target;
            if ($(_this).children('span').hasClass('loading')) {
                return;
            }
            let from = $('#bookingStart').val();
            let to = $('#bookingEnd').val();
            let type = $('#bookingType').val();
            let priceFrom = $('#priceFrom').val();
            let priceTo = $('#priceTo').val();
            let direction = $('#bookingDirection').val();
            let floor = $('#bookingFloor').val();
            let number = $('#bookingNumber').val();
            if (new Date(from) >= new Date(to)) {
                showMsg('离店时间必须晚于预定时间');
                return;
            }
            if (priceFrom > priceTo) {
                showMsg('最高价格必须高于最低价格');
                return;
            }
            $(_this).append('<span class="loading line"></span>');
            $.ajax({
                url: `${serverHost}/api/rooms/list`,
                type: 'GET',
                contentType: "application/json; charset=UTF-8",
                data: {
                    from: from.length > 0 && to.length > 0 ? new Date(from).toISOString().replace('Z', '') : undefined,
                    to: from.length > 0 && to.length > 0 ? new Date(to).toISOString().replace('Z', '') : undefined,
                    type: type.length > 0 ? type : undefined,
                    priceFrom: priceFrom > 0 && priceTo > 0 ? priceFrom : undefined,
                    priceTo: priceFrom > 0 && priceTo > 0 ? priceTo : undefined,
                    direction: direction.length > 0 ? direction : undefined,
                    floor: floor > 0 ? floor : undefined,
                    number: number > 0 ? number : undefined
                },
                success: function (data, textStatus, jqXHR) {
                    //显示右侧搜索列表
                    $('.slider_container').css('filter', 'blur(10px)');
                    $('.searchBox').css('left', '5%');
                    $('.searchList').slideDown(333);
                    //激活Vue
                    searchListVue.rooms = data;
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    showMsg('网络错误！');
                },
                complete: function () {
                    $(_this).children('.loading').remove();
                }
            });
        }
    },
    computed: {
        floor: function () {
            let floors = [];
            for (let floor in this.floorAndNumber) {
                floors.push(floor);
            }
            return floors;
        }
    }
});

let searchListVue = new Vue({
    el: '.searchList',
    data: {
        rooms: []
    },
    methods: {
        book: function (e) {
            /*TODO
            * 预定动作
            */
            console.log(e);
        },
        close: function (e) {
            $(e.target).parent().parent().slideUp();
            $('.slider_container').css('filter', 'none');
            $('.searchBox').css('left', '10%');
        }
    }
});

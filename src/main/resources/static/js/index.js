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
    sessionStorage.isLogin = false;
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
    let username = $('#inputUserName').val();
    let password = $('#inputPassword').val();
    if (username.length > 0 && password.length > 0) {
        $(_this).append('<span class="loading line"></span>');
        $.ajax({
            url: `${serverHost}/api/users/login`,
            type: 'POST',
            contentType: "application/json; charset=UTF-8",
            data: JSON.stringify({
                phoneNumber: username,
                password: sha256(password)
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
                if ($(".checkbox input").prop('checked')) {
                    localStorage.username = username;
                    localStorage.password = sha256(password);
                    localStorage.nickname = data.nickname;
                    localStorage.role = data.role;
                }
                sessionStorage.username = username;
                sessionStorage.password = sha256(password);
                sessionStorage.nickname = data.nickname;
                sessionStorage.role = data.role;
                sessionStorage.isLogin = true;
                $('.window .close').trigger('click');
                $('.user-info .user-btn').html(sessionStorage.nickname);
                $('.user-info ').show();
                $('.login-btn').hide();
                showMsg(`亲爱的${data.nickname},欢迎你回到阿福旅店！`);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                let msg='登录失败：';
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
    let username = $('#signupUsername').val();
    let nickname = $('#signupNickname').val();
    let password = $('#signupPassword').val();
    let passwordAgain = $('#signupPasswordAgain').val();
    if (isEmpty(username, passwordAgain, password, passwordAgain)) {
        showMsg('请完整填写注册信息');
        return;
    }
    if (password !== passwordAgain) {
        showMsg('两次输入密码不相符');
        return;
    }
    $(_this).append('<span class="loading line"></span>');
    $.ajax({
        url: `${serverHost}/api/users/make`,
        type: 'POST',
        contentType: "application/json; charset=UTF-8",
        data: JSON.stringify({
            phoneNumber: username,
            password: sha256(password),
            nickname: nickname
        }),
        success: function (data, textStatus, jqXHR) {
            $('.window .close').trigger('click');
            sessionStorage.username = username;
            sessionStorage.password = sha256(password);
            sessionStorage.nickname = data.nickname;
            sessionStorage.role = data.role;
            sessionStorage.isLogin = true;
            $('.user-info .user-btn').html(sessionStorage.nickname);
            $('.user-info ').show();
            $('.login-btn').hide();
            showMsg(`亲爱的${data.nickname},欢迎你来到阿福旅店！`);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            let msg='注册失败：';
            switch (jqXHR.status) {
                case 409:
                    msg = '用户已存在';
                    break;
                default:
                    msg += jqXHR.responseJSON && jqXHR.responseJSON.message || '网络错误';
            }
            showMsg(msg);
        },
        complete: function () {
            $(_this).children('.loading').remove();
        }
    });
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
    $('#bookingStart').val(dateTimeISOFormat(new Date()).slice(0, 10));
    $('#bookingEnd').val(dateTimeISOFormat(new Date(new Date().getTime() + 24 * 3600 * 1000)).slice(0, 10));
    //获取首页搜索选项
    $.ajax({
        url: `${serverHost}/api/rooms/load`,
        type: 'GET',
        dataType: 'json',
        contentType: "application/json; charset=UTF-8",
        success: function (data, textStatus, jqXHR) {
            searchBoxVue.update(data);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            let msg = '初始化搜索栏失败：';
            switch (jqXHR.status) {
                default:
                    msg += jqXHR.responseJSON && jqXHR.responseJSON.message || '网络错误';
            }
            showMsg(msg);
        },
        complete: function () {
        }
    });
    //初始化顶栏
    startCatLoading();
    reqLogin(sessionStorage.username || localStorage.username, sessionStorage.password || localStorage.password)
        .then((data) => {
            //将接受到的数据解析
            sessionStorage.nickname = data.nickname;
            sessionStorage.isLogin = true;
            $('.user-info .user-btn').html(sessionStorage.nickname || localStorage.nickname);
            $('.user-info ').show();
        }, (error) => {
            if (error !== 'EMPTY_USERNAME_OR_PASSWORD') {
                showMsg(`自动登录失败:${error}`);
            }
            $('.login-btn').show();
        })
        .then(stopCatLoading);
    //初始化登录与注册格式
    constraintTel($("#inputUserName"), $("#signupUsername"));
    //初始化键盘快捷键
    $('.form-horizontal').keypress(function (e) {
        if (e.keyCode === 13) {
            $(this).find('.confirm').trigger('click');
        }
    })
});

let searchBoxVue = new Vue({
    el: '.searchBox',
    data: {
        type: [],
        direction: [],
        floorAndNumber: {},
        number: []
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
            let priceFrom = +$('#priceFrom').val();
            let priceTo = +$('#priceTo').val();
            let direction = $('#bookingDirection').val();
            let floor = $('#bookingFloor').val();
            let number = $('#bookingNumber').val();
            if (isEmpty(from, to)) {
                showMsg('请正确填写预定时间和离店时间');
                return;
            }
            if (new Date(from) >= new Date(to)) {
                showMsg('离店时间必须晚于预定时间');
                return;
            }
            if (priceFrom > priceTo || isNegative(priceFrom, priceTo)) {
                showMsg('最高价格必须高于最低价格，且不为负数');
                return;
            }
            $(_this).append('<span class="loading line"></span>');
            $.ajax({
                url: `${serverHost}/api/rooms`,
                type: 'GET',
                contentType: "application/json; charset=UTF-8",
                data: {
                    from: dateTimeISOFormat(new Date(from)),
                    to: dateTimeISOFormat(new Date(to)),
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
                    let msg='搜索失败：';
                    switch (jqXHR.status) {
                        default:
                            msg += jqXHR.responseJSON && jqXHR.responseJSON.message || '网络错误';
                    }
                    showMsg(msg);
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
        rooms: [],
        ids: [],
        selectedIndex: -1,
        comments:[]
    },
    methods: {
        //查看房间评价
        showComments: function (e) {
            startCatLoading();
            this.comments=this.rooms[+$(e.target).attr('index')].comments;
            $('.searchList .commentList').slideDown(333);
            stopCatLoading();
        },
        showIDs: function (e) {
            if (sessionStorage.isLogin !== 'true') {
                showMsg('请先登录！').then(
                    () => {
                        $('.login-btn').trigger('click');
                    }
                );
                return;
            }
            if (sessionStorage.role !== '注册用户') {
                showMsg(`尊敬的${sessionStorage.role}:非注册用户禁止由此登记预订`);
                return;
            }
            startCatLoading();
            $.ajax({
                url: `${serverHost}/api/users/guests?phone=${sessionStorage.username || localStorage.username}`,
                type: 'GET',
                dataType: 'json',
                contentType: "application/json; charset=UTF-8",
                beforeSend: function (xhr) {
                    xhr.setRequestHeader("Authorization", "Basic " + btoa((sessionStorage.username || localStorage.username) + ":" + (sessionStorage.password || localStorage.password)));
                },
                success: function (data, textStatus, jqXHR) {
                    searchListVue.ids = data;
                    searchListVue.selectedIndex = +$(e.target).attr('index');
                    $('.searchList .selectID').fadeIn();
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    let msg='获取用户信息失败：';
                    switch (jqXHR.status) {
                        default:
                            msg += jqXHR.responseJSON && jqXHR.responseJSON.message || '网络错误';
                    }
                    showMsg(msg);
                },
                complete: function () {
                    stopCatLoading();
                }
            })
        },
        submitBook: function () {
            let selectedIDs = [];
            $(".selectID input:checked").each(function () {
                selectedIDs.push($(this).attr('identification'));
            });
            if (isEmpty(selectedIDs)) {
                showMsg('请至少选择一位旅客');
                return;
            }
            startCatLoading();
            $('.selectID .close span').trigger('click');
            $.ajax({
                url: `${serverHost}/api/transactions`,
                type: 'POST',
                data: JSON.stringify({
                    dateFrom: dateTimeISOFormat(new Date($('#bookingStart').val())),
                    dateTo: dateTimeISOFormat(new Date($('#bookingEnd').val())),
                    phone: sessionStorage.username,
                    guests: selectedIDs,
                    room: {
                        floor: searchListVue.rooms[searchListVue.selectedIndex].roomNumber.floor,
                        number: searchListVue.rooms[searchListVue.selectedIndex].roomNumber.number
                    }
                }),
                contentType: "application/json; charset=UTF-8",
                beforeSend: function (xhr) {
                    xhr.setRequestHeader("Authorization", "Basic " + btoa((sessionStorage.username || localStorage.username) + ":" + (sessionStorage.password || localStorage.password)));
                },
                success: function (data, textStatus, jqXHR) {
                    showMsg('预定成功')
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    let msg = '预订失败：';
                    switch (jqXHR.status) {
                        case 409:
                            msg+=':该时段已有用户入住';
                            break;
                        case 423:
                            msg += '：该房间已被占用';
                            break;
                        default:
                            msg += jqXHR.responseJSON && jqXHR.responseJSON.message || '网络错误';
                    }
                    showMsg(msg);
                },
                complete: function () {
                    stopCatLoading();
                }
            })
        },
        timeFormat: function (time) {
            return new Date(time).toLocaleString();
        },
        close: function (e) {
            let button = $(e.target);
            let thisWindow = $(e.target).parent().parent();
            switch (button.attr('id')) {
                case 'closeSearchList':
                    $('.slider_container').css('filter', 'none');
                    $('.searchBox').css('left', '10%');
                    break;
            }
            thisWindow.slideUp();
            thisWindow.find('.close span').trigger('click');
        }
    }
});

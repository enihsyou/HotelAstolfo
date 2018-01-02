const left_nav = new Vue({
    el: '.left-nav',
    data: {
        userType: '',//登录用户类型
        items: []//登录用户可操作条目
    },
    methods: {
        displayView: function (event) {
            startCatLoading();
            let item = event.target.innerText;
            switch (item) {
                case '我的订单':
                    check_my_order();
                    break;
                case '个人信息':
                    modify_my_info();
                    break;
                case '客房管理':
                    rooms_all_info();
                    break;
                case '订单管理':
                    check_all_booking();
                    break;
                case '客房类型设置':
                    modify_rooms_type();
                    break;
                case '账户管理':
                    modify_user_info();
                    break;
                // case '销售月表':
                //     sales_per_month();
                //     break;
                // case '客户分析':
                //     client_analyze();
                //     break;
                case '返回主页':
                    backHome();
                    break;
                case '登出':
                    logout();
                    break;
                default:
                    showMsg('该功能还只是一堆BUG(ಡωಡ)');
                    stopCatLoading();
            }
        }
    }
});

let ordinary_user_items = ['我的订单', '个人信息', '返回主页', '登出'];
let receptionist_user_items = ['客房管理', '订单管理', '返回主页', '登出'];
let manager_user_items = ['客房管理', '订单管理', '客房类型设置', '账户管理', '销售月表', '客户分析', '返回主页', '登出'];

//初始化
$(function init() {
    //获取用户信息
    let username = sessionStorage.username || localStorage.username;
    let password = sessionStorage.password || localStorage.password;
    let nickname = sessionStorage.nickname || localStorage.nickname;
    if (sessionStorage.isLogin !== 'true') {
        //未登录则返回主页
        location.href = '/';
    }
    else {
        startCatLoading(100);
        reqLogin(username, password).then(
            (data) => {
                sessionStorage.nickname = data.nickname;
                sessionStorage.role = data.role;
                switch (sessionStorage.role) {
                    case '经理':
                        left_nav.userType = '经理：' + nickname;
                        left_nav.items = manager_user_items;
                        break;
                    case '前台':
                        left_nav.userType = '前台：' + nickname;
                        left_nav.items = receptionist_user_items;
                        break;
                    case '注册用户':
                        left_nav.userType = '欢迎您！' + nickname;
                        left_nav.items = ordinary_user_items;
                        break;
                }
            },
            (errorThrown) => {
                showMsg(errorThrown).then(
                    () => {
                        location.href = '/';
                    }
                )
            }
        ).then(stopCatLoading);
    }
});
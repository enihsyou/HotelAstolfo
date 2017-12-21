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
                case '查看我的订单':
                    check_my_order();
                    break;
                case '查看我的预订':
                    check_my_booking();
                    break;
                case '修改个人信息':
                    modify_my_info();
                    break;
                case '预订客房':
                    book_a_room();
                    break;
                case '当前所有客房状态':
                    rooms_all_info();
                    break;
                case '查询预定客户信息':
                    check_all_booking();
                    break;
                case '客房维修登记':
                    fix_a_room();
                    break;
                case '客房类型设置':
                    modify_rooms_type();
                    break;
                case '可用客房设置':
                    set_rooms_avail();
                    break;
                case '预订查询与修改':
                    check_all_booking();
                    break;
                case '销售月表':
                    sales_per_month();
                    break;
                case '客户分析':
                    client_analyze();
                    break;
                case '登出':
                    logout();
                    break;
            }
        }
    }
});

let ordinary_user_items = ['查看我的订单', '查看我的预订', '修改个人信息', '登出'];
let receptionist_user_items = ['当前所有客房状态', '查询预定客户信息', '预订客房', '客房维修登记', '登出'];
let manager_user_items = ['客房类型设置', '可用客房设置', '当前所有客房状态', '预订客房', '预订查询与修改', '销售月表', '客户分析', '登出'];

//初始化
$(async function init() {
    //获取用户信息
    let username = sessionStorage.username || localStorage.username;
    let password = sessionStorage.password || localStorage.password;
    let nickname = sessionStorage.nickname || localStorage.nickname;
    if (username == null || password == null) {
        location.href = '/';
    }
    else {
        let data = await reqLogin(username, password);

    }
    left_nav.userType = '普通用户';
    left_nav.items = ordinary_user_items;
});
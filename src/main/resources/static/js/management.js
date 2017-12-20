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
                case '预订客房':
                case '当前所有客房状态':
                case '查询预定客户信息':
                case '客房维修登记':
                case '客房类型设置':
                case '可用客房设置':
                case '预订查询与修改':
                case '销售月表':
                case '客户分析':
                case '登出':
            }
        }
    }
});

let ordinary_user_items = ['查看我的订单', '查看我的预订', '修改个人信息', '登出'];
let receptionist_user_items = ['当前所有客房状态', '查询预定客户信息', '预订客房', '客房维修登记', '登出'];
let manager_user_items = ['客房类型设置', '可用客房设置', '当前所有客房状态', '预订客房', '预订查询与修改', '销售月表', '客户分析', '登出'];

left_nav.userType = '普通用户';
left_nav.items = ordinary_user_items;
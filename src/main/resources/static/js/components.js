const NOTE = ['查看我的订单', '查看我的预订', '修改个人信息',
    '预订客房', '当前所有客房状态', '查询预定客户信息',
    '客房维修登记', '客房类型设置', '可用客房设置',
    '预订查询与修改', '销售月表', '客户分析', '登出'];

function render_Container(template) {
    return new Promise((resolve) => {
        let JQcontainer = $('.container');
        JQcontainer.empty();
        JQcontainer.append(template);
        resolve();
    })
}

async function check_my_order() {
    //身份验证
    //获取订单
    let resStrBuilder = [];
    resStrBuilder.push(`
        <h1>假装打印出所有订单</h1>
        `);
    //生成html
    await render_Container(resStrBuilder.toString());
    //addListeners
    $('.container h1').click(function () {
        showMsg('测试一下')
    });
    //关闭动画？
    stopCatLoading();
}

async function check_my_booking() {
    //身份验证
    //获取订单
    let resStrBuilder = [];
    resStrBuilder.push(`
        <h1>假装打印出所有预订</h1>
        `);
    //生成html
    await render_Container(resStrBuilder.toString());
    //addListeners
}

async function modify_my_info() {
    //身份验证
    //获取订单
    let resStrBuilder = [];
    resStrBuilder.push();
    //生成html
    await render_Container(resStrBuilder.toString());
    //addListeners
}

async function book_a_room() {
    //身份验证
    //获取订单
    let resStrBuilder = [];
    resStrBuilder.push();
    //生成html
    await render_Container(resStrBuilder.toString());
    //addListeners
}

async function rooms_all_info() {
    //身份验证
    //获取订单
    let resStrBuilder = [];
    resStrBuilder.push();
    //生成html
    await render_Container(resStrBuilder.toString());
    //addListeners
}

async function check_all_booking() {
    //身份验证
    //获取订单
    let resStrBuilder = [];
    resStrBuilder.push();
    //生成html
    await render_Container(resStrBuilder.toString());
    //addListeners
}

async function fix_a_room() {
    //身份验证
    //获取订单
    let resStrBuilder = [];
    resStrBuilder.push();
    //生成html
    await render_Container(resStrBuilder.toString());
    //addListeners
}

async function modify_rooms_type() {
    //身份验证
    //获取订单
    let resStrBuilder = [];
    resStrBuilder.push();
    //生成html
    await render_Container(resStrBuilder.toString());
    //addListeners
}

async function set_rooms_avail() {
    //身份验证
    //获取订单
    let resStrBuilder = [];
    resStrBuilder.push();
    //生成html
    await render_Container(resStrBuilder.toString());
    //addListeners
}

async function sales_per_month() {
    //身份验证
    //获取订单
    let resStrBuilder = [];
    resStrBuilder.push();
    //生成html
    await render_Container(resStrBuilder.toString());
    //addListeners
}

async function client_analyze() {
    //身份验证
    //获取订单
    let resStrBuilder = [];
    resStrBuilder.push();
    //生成html
    await render_Container(resStrBuilder.toString());
    //addListeners
}

async function logout() {
    localStorage.clear();
    sessionStorage.clear();
    location.href = '/';
}
const NOTE = ['查看我的订单', '修改个人信息',
    '预订客房', '当前所有客房状态', '查询预定客户信息',
    '客房维修登记', '客房类型设置', '可用客房设置',
    '预订查询与修改', '所有账户管理', '销售月表',
    '客户分析', '登出'];

function render_Container(template) {
    return new Promise((resolve) => {
        let JQcontainer = $('.container');
        JQcontainer.empty();
        JQcontainer.append(template);
        resolve();
    })
}

//done
async function check_my_order() {
    //身份验证&获取数据
    $.ajax({
        url: `http://47.100.117.174:8899/api/users/transactions?phone=${sessionStorage.username || localStorage.username}`,
        type: 'GET',
        dataType: 'json',
        contentType: "application/json; charset=UTF-8",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Authorization", "Basic " + btoa(sessionStorage.username || localStorage.username + ":" + sessionStorage.password || localStorage.password));
        },
        success: function (data, textStatus, jqXHR) {
            //获取订单
            let resStrBuilder = [];
            resStrBuilder.push(`
            <div class="check_my_order">
                <table>
                    <tr>
                        <td>订单编号</td>
                        <td>身份证号</td>
                        <td>姓名</td>
                        <td><!--操作--></td>
                    </tr>
                    <tr class="oderList" v-for="item in orders" v-cloak>
                        <td>
                            {{item.id}}
                        </td>
                        <td>
                            {{item.identification}}
                        </td>
                        <td>
                            {{item.name}}
                        </td>
                        <td>
                            <div class="comm-btn btn-xs btn-default" v-if="isOver">评价</div>
                        </td>
                    </tr>
                </table>
            </div>
            `);
            //生成html
            render_Container(resStrBuilder.toString());
            //script
            let app = new Vue({
                el: '.check_my_order',
                data: {
                    orders: data
                }
            })
        },
        error: function (jqXHR, textStatus, errorThrown) {
            showMsg(errorThrown)
        },
        complete: function () {
            //关闭动画？
            stopCatLoading();
        }
    });
}


async function modify_my_info() {
    //身份验证&获取数据
    $.ajax({
        url: `http://47.100.117.174:8899/api/`,
        type: 'GET',
        dataType: 'json',
        contentType: "application/json; charset=UTF-8",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Authorization", "Basic " + btoa(sessionStorage.username || localStorage.username + ":" + sessionStorage.password || localStorage.password));
        },
        success: function (data, textStatus, jqXHR) {
            //获取订单
            let resStrBuilder = [];
            resStrBuilder.push(`
            <div class="modify_my_info">
                <dl>
                    <dt>修改昵称</dt>
                    <dd>
                        <input type="text" title="nickname" :value="nickname">
                    </dd>
                </dl>
                <dl>
                    <dt>修改密码</dt>
                    <dd>
                        </label>
                        <label for="newPWD">新密码：
                            <input type="password" id="newPWD">
                        </label>
                        <label for="newPWDR">确认新密码：
                            <input type="password" id="newPWDR">
                        </label>
                        <div class="btn btn-default">确认修改</div>
                    </dd>
                </dl>
                <dl>
                    <dt>已有身份证信息</dt>
                    <dd>
                        <table>
                            <tr>
                                <td>姓名</td>
                                <td>身份证</td>
                                <td></td>
                            </tr>
                            <tr v-for="guest in guests" v-cloak>
                                <td>{{guest.name}}</td>
                                <td>{{guest.id}}</td>
                                <td>
                                    <div class="comfirm btn btn-default" :name="guest.name">删除</div>
                                </td>
                            </tr>
                            <tr>
                                <td><input type="text" title="name"></td>
                                <td><input type="text" title="ID"></td>
                                <td>
                                    <div class="btn btn-default">添加</div>
                                </td>
                            </tr>
                        </table>
                    </dd>
                </dl>
            </div>
            `);
            //生成html
            render_Container(resStrBuilder.toString());
            //script
            $('.container h1').click(function () {
                showMsg('测试一下')
            });
        },
        error: function (jqXHR, textStatus, errorThrown) {
            showMsg(errorThrown)
        },
        complete: function () {
            //关闭动画？
            stopCatLoading();
        }
    });

}

async function book_a_room() {
    //身份验证&获取数据
    $.ajax({
        url: `http://47.100.117.174:8899/api/`,
        type: 'GET',
        dataType: 'json',
        contentType: "application/json; charset=UTF-8",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Authorization", "Basic " + btoa(sessionStorage.username || localStorage.username + ":" + sessionStorage.password || localStorage.password));
        },
        success: function (data, textStatus, jqXHR) {
            //获取订单
            let resStrBuilder = [];
            //类首页搜搜（简化）
            resStrBuilder.push(`
            <div class="book_a_room">
                <div class="search">
                    <label class="col-xs-6" for="bookingStart">入住日期：
                        <input type="date" id="bookingStart">
                        ~
                        <input type="date" id="bookingEnd">
                    </label>
                    <label class="col-xs-6" for="bookingPrice">价格区间：
                        <span id="bookingPrice">
                        <input type="number" min="0" max="9999" placeholder="最低">
                        ~
                        <input min="0" max="9999" type="number" placeholder="最高">
                    </span>
                    </label>
                    <label class="col-xs-12" for="bookingType">客房类型：
                        <select class="form-control" id="bookingType">
                            <option value="std">高端双床间</option>
                            <option value="big">至尊大床房</option>
                            <option value="thr">激情三人房</option>
                            <option value="suit">奢华总统套房</option>
                            <option value="" selected>任意</option>
                        </select>
                    </label>
                    <label class="col-xs-12" for="bookingDirection">房间朝向：
                        <select class="form-control" id="bookingDirection">
                            <option value="east">东</option>
                            <option value="south">南</option>
                            <option value="west">西</option>
                            <option value="north">北</option>
                            <option value="" selected>任意</option>
                        </select>
                    </label>
                    <label class="col-xs-12" for="bookingFloor">楼层：
                        <select class="form-control" id="bookingFloor">
                            <option value="1">1</option>
                            <option value="2">2</option>
                            <option value="4">3</option>
                            <option value="5">4</option>
                            <option value="5">5</option>
                            <option value="" selected>任意</option>
                        </select>
                    </label>
                    <label class="col-xs-12" for="bookingNumber">房号：
                        <select class="form-control" id="bookingNumber" disabled>
                            <option v-for="number in numbers" :value="value" v-cloak="">{{number}}</option>
                            <option value="" selected>任意</option>
                        </select>
                    </label>
                    <div class=" text-center">
                        <div type="submit" class="btn btn-default confirm untouchable">搜索</div>
                    </div>
                </div>
                <div class="resList">
                    <table>
                        <tr>
                            <td>房型</td>
                            <td>床型</td>
                            <td>特殊服务</td>
                            <td>房价</td>
                            <td><!--预定--></td>
                        </tr>
                        <!--<tr>-->
                        <!--<td>高端双床间</td>-->
                        <!--<td>2</td>-->
                        <!--<td>大保健</td>-->
                        <!--<td>699</td>-->
                        <!--<td>-->
                        <!--<div class="book-btn btn-default btn-md">立即预定</div>-->
                        <!--</td>-->
                        <!--</tr>-->
                        <tr v-cloak>
                            <td class="searchListItem" v-for="item in searchResults">
                                {{item}}
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
            `);
            //生成html
            render_Container(resStrBuilder.toString());
            //script
            $('.container h1').click(function () {
                showMsg('测试一下')
            });
        },
        error: function (jqXHR, textStatus, errorThrown) {
            showMsg(errorThrown)
        },
        complete: function () {
            //关闭动画？
            stopCatLoading();
        }
    });
}

async function rooms_all_info() {
    //身份验证&获取数据
    $.ajax({
        url: `http://47.100.117.174:8899/api/`,
        type: 'GET',
        dataType: 'json',
        contentType: "application/json; charset=UTF-8",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Authorization", "Basic " + btoa(sessionStorage.username || localStorage.username + ":" + sessionStorage.password || localStorage.password));
        },
        success: function (data, textStatus, jqXHR) {
            //获取订单
            let resStrBuilder = [];
            resStrBuilder.push(`
            <h1>假装打印出所有订单</h1>
            `);
            //生成html
            render_Container(resStrBuilder.toString());
            //script
            $('.container h1').click(function () {
                showMsg('测试一下')
            });
        },
        error: function (jqXHR, textStatus, errorThrown) {
            showMsg(errorThrown)
        },
        complete: function () {
            //关闭动画？
            stopCatLoading();
        }
    });

}

async function check_all_booking() {
    //身份验证&获取数据
    $.ajax({
        url: `http://47.100.117.174:8899/api/`,
        type: 'GET',
        dataType: 'json',
        contentType: "application/json; charset=UTF-8",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Authorization", "Basic " + btoa(sessionStorage.username || localStorage.username + ":" + sessionStorage.password || localStorage.password));
        },
        success: function (data, textStatus, jqXHR) {
            //获取订单
            let resStrBuilder = [];
            resStrBuilder.push(`
            <h1>假装打印出所有订单</h1>
            `);
            //生成html
            render_Container(resStrBuilder.toString());
            //script
            $('.container h1').click(function () {
                showMsg('测试一下')
            });
        },
        error: function (jqXHR, textStatus, errorThrown) {
            showMsg(errorThrown)
        },
        complete: function () {
            //关闭动画？
            stopCatLoading();
        }
    });

}

async function fix_a_room() {
    //身份验证&获取数据
    $.ajax({
        url: `http://47.100.117.174:8899/api/`,
        type: 'GET',
        dataType: 'json',
        contentType: "application/json; charset=UTF-8",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Authorization", "Basic " + btoa(sessionStorage.username || localStorage.username + ":" + sessionStorage.password || localStorage.password));
        },
        success: function (data, textStatus, jqXHR) {
            //获取订单
            let resStrBuilder = [];
            resStrBuilder.push(`
            <h1>假装打印出所有订单</h1>
            `);
            //生成html
            render_Container(resStrBuilder.toString());
            //script
            $('.container h1').click(function () {
                showMsg('测试一下')
            });
        },
        error: function (jqXHR, textStatus, errorThrown) {
            showMsg(errorThrown)
        },
        complete: function () {
            //关闭动画？
            stopCatLoading();
        }
    });

}

async function modify_rooms_type() {
    //身份验证&获取数据
    $.ajax({
        url: `http://47.100.117.174:8899/api/`,
        type: 'GET',
        dataType: 'json',
        contentType: "application/json; charset=UTF-8",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Authorization", "Basic " + btoa(sessionStorage.username || localStorage.username + ":" + sessionStorage.password || localStorage.password));
        },
        success: function (data, textStatus, jqXHR) {
            //获取订单
            let resStrBuilder = [];
            resStrBuilder.push(`
            <h1>假装打印出所有订单</h1>
            `);
            //生成html
            render_Container(resStrBuilder.toString());
            //script
            $('.container h1').click(function () {
                showMsg('测试一下')
            });
        },
        error: function (jqXHR, textStatus, errorThrown) {
            showMsg(errorThrown)
        },
        complete: function () {
            //关闭动画？
            stopCatLoading();
        }
    });

}

async function set_rooms_avail() {
    //身份验证&获取数据
    $.ajax({
        url: `http://47.100.117.174:8899/api/`,
        type: 'GET',
        dataType: 'json',
        contentType: "application/json; charset=UTF-8",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Authorization", "Basic " + btoa(sessionStorage.username || localStorage.username + ":" + sessionStorage.password || localStorage.password));
        },
        success: function (data, textStatus, jqXHR) {
            //获取订单
            let resStrBuilder = [];
            resStrBuilder.push(`
            <h1>假装打印出所有订单</h1>
            `);
            //生成html
            render_Container(resStrBuilder.toString());
            //script
            $('.container h1').click(function () {
                showMsg('测试一下')
            });
        },
        error: function (jqXHR, textStatus, errorThrown) {
            showMsg(errorThrown)
        },
        complete: function () {
            //关闭动画？
            stopCatLoading();
        }
    });

}

async function modify_user_info() {
    //身份验证&获取数据
    $.ajax({
        url: `http://47.100.117.174:8899/api/`,
        type: 'GET',
        dataType: 'json',
        contentType: "application/json; charset=UTF-8",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Authorization", "Basic " + btoa(sessionStorage.username || localStorage.username + ":" + sessionStorage.password || localStorage.password));
        },
        success: function (data, textStatus, jqXHR) {
            //获取订单
            let resStrBuilder = [];
            resStrBuilder.push(`
            <h1>假装打印出所有订单</h1>
            `);
            //生成html
            render_Container(resStrBuilder.toString());
            //script
            $('.container h1').click(function () {
                showMsg('测试一下')
            });
        },
        error: function (jqXHR, textStatus, errorThrown) {
            showMsg(errorThrown)
        },
        complete: function () {
            //关闭动画？
            stopCatLoading();
        }
    });

}

async function sales_per_month() {
    //身份验证&获取数据
    $.ajax({
        url: `http://47.100.117.174:8899/api/`,
        type: 'GET',
        dataType: 'json',
        contentType: "application/json; charset=UTF-8",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Authorization", "Basic " + btoa(sessionStorage.username || localStorage.username + ":" + sessionStorage.password || localStorage.password));
        },
        success: function (data, textStatus, jqXHR) {
            //获取订单
            let resStrBuilder = [];
            resStrBuilder.push(`
            <h1>假装打印出所有订单</h1>
            `);
            //生成html
            render_Container(resStrBuilder.toString());
            //script
            $('.container h1').click(function () {
                showMsg('测试一下')
            });
        },
        error: function (jqXHR, textStatus, errorThrown) {
            showMsg(errorThrown)
        },
        complete: function () {
            //关闭动画？
            stopCatLoading();
        }
    });

}

async function client_analyze() {
    //身份验证&获取数据
    $.ajax({
        url: `http://47.100.117.174:8899/api/`,
        type: 'GET',
        dataType: 'json',
        contentType: "application/json; charset=UTF-8",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Authorization", "Basic " + btoa(sessionStorage.username || localStorage.username + ":" + sessionStorage.password || localStorage.password));
        },
        success: function (data, textStatus, jqXHR) {
            //获取订单
            let resStrBuilder = [];
            resStrBuilder.push(`
            <h1>假装打印出所有订单</h1>
            `);
            //生成html
            render_Container(resStrBuilder.toString());
            //script
            $('.container h1').click(function () {
                showMsg('测试一下')
            });
        },
        error: function (jqXHR, textStatus, errorThrown) {
            showMsg(errorThrown)
        },
        complete: function () {
            //关闭动画？
            stopCatLoading();
        }
    });
}

async function logout() {
    localStorage.clear();
    sessionStorage.clear();
    location.href = '/';
}
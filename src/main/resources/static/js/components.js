const NOTE = ['查看我的订单', '修改个人信息',
    '预订客房', '当前所有客房状态', '查询预定客户信息',
    '客房维修登记', '客房类型设置', '可用客房设置',
    '预订查询与修改', '所有账户管理', '销售月表',
    '客户分析', '返回主页', '登出'];

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
        url: `${serverHost}/api/users/transactions?phone=${sessionStorage.username || localStorage.username}`,
        type: 'GET',
        dataType: 'json',
        contentType: "application/json; charset=UTF-8",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Authorization", "Basic " + btoa(sessionStorage.username || localStorage.username + ":" + sessionStorage.password || localStorage.password));
        },
        success: function (data, textStatus, jqXHR) {
            //获取订单
            let resStr = `
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
            `;
            //生成html
            render_Container(resStr);
            //script
            let app = new Vue({
                el: '.check_my_order',
                data: {
                    orders: data
                }
            })
        },
        error: function (jqXHR, textStatus, errorThrown) {
            showMsg(jqXHR.status)
        },
        complete: function () {
            //关闭动画？
            stopCatLoading();
        }
    });
}

//done
async function modify_my_info() {
    //身份验证&获取数据
    $.ajax({
        url: `${serverHost}/api/users/guests?phone=${sessionStorage.username || localStorage.username}`,
        type: 'GET',
        dataType: 'json',
        contentType: "application/json; charset=UTF-8",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Authorization", "Basic " + btoa(sessionStorage.username || localStorage.username + ":" + sessionStorage.password || localStorage.password));
        },
        success: function (data, textStatus, jqXHR) {
            let resStr = `
            <div class="modify_my_info">
            <dl>
                <dt>修改个人资料</dt>
                <dd>
                    <table class="info">
                        <tr>
                            <td>昵称：</td>
                            <td>
                                <input type="text" id="nickname" title="nickname" placeholder="请输入你要更改的昵称" :value="nickname" >
                            </td>
                            <td></td>
                        </tr>
                        <tr>
                            <td>新密码：</td>
                            <td>
                                <input type="password" id="newPWD" title="password" placeholder="********">
                            </td>
                            <td><p>*不填写则不修改</p>
                            </td>
                        </tr>
                        <tr>
                            <td>确认新密码：</td>
                            <td>
                                <input type="password" id="newPWDR" title="passwordR" placeholder="********">
                            </td>
                            <td></td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <div class="btn btn-default">确认修改</div>
                            </td>
                            <td>
                        </tr>
                    </table>
                </dd>
            </dl>
            <dl>
                <dt>常用旅客</dt>
                <dd>
                    <table class="id">
                        <tr>
                            <td>姓名</td>
                            <td>身份证</td>
                            <td></td>
                        </tr>
                        <tr v-for="guest in guests" v-cloak>
                            <td>{{guest.name}}</td>
                            <td>{{guest.identification}}</td>
                            <td>
                                <div class="comfirm btn btn-default" :name="guest.name">删除</div>
                            </td>
                        </tr>
                        <tr>
                            <td><input type="text" id="newG" title="name" placeholder="新旅客姓名"></td>
                            <td><input type="text" id="newID" title="ID" placeholder="新旅客身份证"></td>
                            <td>
                                <div class="btn btn-default confirm">添加</div>
                            </td>
                        </tr>
                    </table>
                </dd>
            </dl>
        </div>
            `;
            //生成html
            render_Container(resStr);
            //script
            //初始化界面
            const app = new Vue({
                el: '.modify_my_info',
                data: {
                    nickname: sessionStorage.nickname || localStorage.nickname,
                    guests: data
                }
            });
            //修改个人资料
            $('.container .info .btn').click(function () {
                let newPassword = $('#newPWD').val();
                let oldNickname = sessionStorage.nickname || localStorage.nickname;
                let newNickname = $('#nickname').val();
                if (newPassword !== $('#newPWDR').val()) {
                    showMsg('两次输入的密码不相符！');
                    return;
                }
                startCatLoading();
                $.ajax({
                    url: `${serverHost}/api/users?phone=${sessionStorage.username || localStorage.username}`,
                    type: 'PUT',
                    data: JSON.stringify({
                        nickname: newNickname,
                        password: newPassword.length > 0 ? newPassword : undefined
                    }),
                    dataType: 'json',
                    contentType: "application/json; charset=UTF-8",
                    beforeSend: function (xhr) {
                        xhr.setRequestHeader("Authorization", "Basic " + btoa(sessionStorage.username || localStorage.username + ":" + sessionStorage.password || localStorage.password));
                    },
                    success: function (data, textStatus, jqXHR) {
                        sessionStorage.nickname = newNickname;
                        left_nav.userType = left_nav.userType.replace(oldNickname, newNickname);
                        showMsg('修改成功！');
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        showMsg('修改失败！');
                    },
                    complete: function () {
                        stopCatLoading();
                    }
                })
            });
            //添加身份证信息
            $('.container .id .confirm').click(function () {
                let newG = $('#newG').val();
                let newID = $('#newID').val();
                if (newG == null || newID == null) {
                    showMsg('请完整填写新旅客信息');
                    return;
                }
                startCatLoading();
                $.ajax({
                    url: `${serverHost}/api/users/guests?phone=${sessionStorage.username || localStorage.username}`,
                    type: 'POST',
                    data: JSON.stringify({
                        name: newG,
                        identification: newID,
                    }),
                    contentType: "application/json; charset=UTF-8",
                    beforeSend: function (xhr) {
                        xhr.setRequestHeader("Authorization", "Basic " + btoa(sessionStorage.username || localStorage.username + ":" + sessionStorage.password || localStorage.password));
                    },
                    success: function (data, textStatus, jqXHR) {
                        showMsg(`添加新旅客${newG}成功！`);
                        app.guests.push({
                            id: -1,
                            identification: newID,
                            name: newG
                        })
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        let msg = '添加新旅客失败';
                        switch (jqXHR.status) {
                            case 409:
                                msg += ',已存在该旅客';
                                break;
                        }
                        console.log(jqXHR);
                        showMsg(msg);
                    },
                    complete: function () {
                        stopCatLoading();
                    }
                })
            })
        },
        error: function (jqXHR, textStatus, errorThrown) {
            showMsg(jqXHR.status)
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
        url: `${serverHost}/api/`,
        type: 'GET',
        dataType: 'json',
        contentType: "application/json; charset=UTF-8",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Authorization", "Basic " + btoa(sessionStorage.username || localStorage.username + ":" + sessionStorage.password || localStorage.password));
        },
        success: function (data, textStatus, jqXHR) {
            //获取订单
            let resStr = `
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
            `;
            //生成html
            render_Container(resStr);
            //script
            $('.container h1').click(function () {
                showMsg('测试一下')
            });
        },
        error: function (jqXHR, textStatus, errorThrown) {
            showMsg(jqXHR.status)
        },
        complete: function () {
            //关闭动画？
            stopCatLoading();
        }
    });
}

//done
async function rooms_all_info() {
    //身份验证&获取数据
    $.ajax({
        url: `${serverHost}/api/rooms/list`,
        type: 'GET',
        dataType: 'json',
        contentType: "application/json; charset=UTF-8",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Authorization", "Basic " + btoa(sessionStorage.username || localStorage.username + ":" + sessionStorage.password || localStorage.password));
        },
        success: function (data, textStatus, jqXHR) {
            //获取订单
            let resStr = `
            <div class="rooms_all_info">
                <dl>
                    <dt>所有房间信息：</dt>
                    <dd>
                        <table>
                            <tr>
                                <td>楼层</td>
                                <td>房号</td>
                                <td>朝向</td>
                                <td>房间类型</td>
                                <td>特色</td>
                                <td>价格</td>
                                <td><!--操作--></td>
                            </tr>
                            <tr v-for="room in rooms" v-cloak>
                                <td>{{room.roomNumber.floor}}</td>
                                <td>{{room.roomNumber.number}}</td>
                                <td :title="room.direction.description">{{room.direction.type}}</td>
                                <td :title="room.type.description">{{room.type.type}}</td>
                                <td>{{room.specialty}}</td>
                                <td>{{room.price}}</td>
                                <td></td>
                            </tr>
                        </table>
                    </dd>
                </dl>
            </div>
            `;
            //生成html
            render_Container(resStr);
            //script
            const app = new Vue({
                el: '.rooms_all_info',
                data: {
                    rooms: data
                }
            });
        },
        error: function (jqXHR, textStatus, errorThrown) {
            showMsg(jqXHR.status)
        },
        complete: function () {
            //关闭动画？
            stopCatLoading();
        }
    });
}

//done
async function check_all_booking() {
    //身份验证&获取数据
    $.ajax({
        url: `${serverHost}/api/transactions/list`,
        type: 'GET',
        dataType: 'json',
        contentType: "application/json; charset=UTF-8",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Authorization", "Basic " + btoa(sessionStorage.username || localStorage.username + ":" + sessionStorage.password || localStorage.password));
        },
        success: function (data, textStatus, jqXHR) {
            //获取订单
            let resStr = `
            <div class="check_all_booking">
            <dl>
                <dt>所有订单：</dt>
                <dd>
                    <table>
                        <tr>
                            <td>订单编号</td>
                            <td>创建时间</td>
                            <td>房间楼层与房间号</td>
                            <td>入住人</td>
                            <td>入住时间</td>
                            <td>退房时间</td>
                            <td>订单有效性</td>
                            <td>是否入住</td>
                            <td><!--操作--></td>
                        </tr>
                        <tr v-for="order in orders" v-cloak>
                            <td>{{order.id}}</td>
                            <td>{{timeFormat(order.createDate)}}</td>
                            <td :title="order.room.type.type">
                                {{order.room.roomNumber.floor}}-{{order.room.roomNumber.number}}
                            </td>
                            <td><span v-for="guest in order.guests">{{guest.name}}:{{guest.identification}}</span>
                            </td>
                            <td>{{timeFormat(order.dateFrom)}}</td>
                            <td>{{timeFormat(order.dateTo)}}</td>
                            <td>{{orderActivated}}</td>
                            <td>{{orderUsed}}</td>
                            <td></td>
                        </tr>
                    </table>
                </dd>
            </dl>
        </div>
            `;
            //生成html
            render_Container(resStr);
            //script
            const app = new Vue({
                el: '.check_all_booking',
                data: {
                    orders: data
                },
                methods: {
                    timeFormat: function (time) {
                        return new Date(time).toLocaleString();
                    }
                },
                computed: {
                    orderActivated: function () {
                        return data.activated ? '有效' : '已失效'
                    },
                    orderUsed: function () {
                        return data.used ? '已入住' : '未入住'
                    }

                }
            });
        },
        error: function (jqXHR, textStatus, errorThrown) {
            showMsg(jqXHR.status)
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
        url: `${serverHost}/api/`,
        type: 'GET',
        dataType: 'json',
        contentType: "application/json; charset=UTF-8",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Authorization", "Basic " + btoa(sessionStorage.username || localStorage.username + ":" + sessionStorage.password || localStorage.password));
        },
        success: function (data, textStatus, jqXHR) {
            //获取订单
            let resStr = `
            <h1>假装打印出所有订单</h1>
            `;
            //生成html
            render_Container(resStr);
            //script
            $('.container h1').click(function () {
                showMsg('测试一下')
            });
        },
        error: function (jqXHR, textStatus, errorThrown) {
            showMsg(jqXHR.status)
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
        url: `${serverHost}/api/`,
        type: 'GET',
        dataType: 'json',
        contentType: "application/json; charset=UTF-8",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Authorization", "Basic " + btoa(sessionStorage.username || localStorage.username + ":" + sessionStorage.password || localStorage.password));
        },
        success: function (data, textStatus, jqXHR) {
            //获取订单
            let resStr = `
            <h1>假装打印出所有订单</h1>
            `;
            //生成html
            render_Container(resStr);
            //script
            $('.container h1').click(function () {
                showMsg('测试一下')
            });
        },
        error: function (jqXHR, textStatus, errorThrown) {
            showMsg(jqXHR.status)
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
        url: `${serverHost}/api/`,
        type: 'GET',
        dataType: 'json',
        contentType: "application/json; charset=UTF-8",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Authorization", "Basic " + btoa(sessionStorage.username || localStorage.username + ":" + sessionStorage.password || localStorage.password));
        },
        success: function (data, textStatus, jqXHR) {
            //获取订单
            let resStr = `
            <h1>假装打印出所有订单</h1>
            `;
            //生成html
            render_Container(resStr);
            //script
            $('.container h1').click(function () {
                showMsg('测试一下')
            });
        },
        error: function (jqXHR, textStatus, errorThrown) {
            showMsg(jqXHR.status)
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
        url: `${serverHost}/api/`,
        type: 'GET',
        dataType: 'json',
        contentType: "application/json; charset=UTF-8",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Authorization", "Basic " + btoa(sessionStorage.username || localStorage.username + ":" + sessionStorage.password || localStorage.password));
        },
        success: function (data, textStatus, jqXHR) {
            //获取订单
            let resStr = `
            <h1>假装打印出所有订单</h1>
            `;
            //生成html
            render_Container(resStr);
            //script
            $('.container h1').click(function () {
                showMsg('测试一下')
            });
        },
        error: function (jqXHR, textStatus, errorThrown) {
            showMsg(jqXHR.status)
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
        url: `${serverHost}/api/`,
        type: 'GET',
        dataType: 'json',
        contentType: "application/json; charset=UTF-8",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Authorization", "Basic " + btoa(sessionStorage.username || localStorage.username + ":" + sessionStorage.password || localStorage.password));
        },
        success: function (data, textStatus, jqXHR) {
            //获取订单
            let resStr = `
            <h1>假装打印出所有订单</h1>
            `;
            //生成html
            render_Container(resStr);
            //script
            $('.container h1').click(function () {
                showMsg('测试一下')
            });
        },
        error: function (jqXHR, textStatus, errorThrown) {
            showMsg(jqXHR.status)
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
        url: `${serverHost}/api/`,
        type: 'GET',
        dataType: 'json',
        contentType: "application/json; charset=UTF-8",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Authorization", "Basic " + btoa(sessionStorage.username || localStorage.username + ":" + sessionStorage.password || localStorage.password));
        },
        success: function (data, textStatus, jqXHR) {
            //获取订单
            let resStr = `
            <h1>假装打印出所有订单</h1>
            `;
            //生成html
            render_Container(resStr);
            //script
            $('.container h1').click(function () {
                showMsg('测试一下')
            });
        },
        error: function (jqXHR, textStatus, errorThrown) {
            showMsg(jqXHR.status)
        },
        complete: function () {
            //关闭动画？
            stopCatLoading();
        }
    });
}

async function backHome() {
    location.href = '/';
}

async function logout() {
    localStorage.clear();
    sessionStorage.clear();
    location.href = '/';
}
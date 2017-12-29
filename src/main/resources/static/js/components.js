//渲染网页
function render_Container(template) {
    return new Promise((resolve) => {
        let JQcontainer = $('.container');
        JQcontainer.empty();
        JQcontainer.append(template);
        resolve();
    })
}

/*
* 用户端：我的订单
* 获取当前所有订单
* 可取消订单
* 评价已完成订单 TODO
*/
async function check_my_order() {
    $.ajax({
        url: `${serverHost}/api/users/transactions?phone=${sessionStorage.username }`,
        type: 'GET',
        dataType: 'json',
        contentType: "application/json; charset=UTF-8",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Authorization", "Basic " + btoa(sessionStorage.username + ":" + sessionStorage.password));
        },
        success: function (data, textStatus, jqXHR) {
            //获取订单
            let resStr = `
                <div id="check_my_order">
            <table class="orders">
                <tr>
                    <td>订单编号</td>
                    <td>房间楼层号与类型</td>
                    <td>入住人</td>
                    <td>入住时间</td>
                    <td>退房时间</td>
                    <td>订单状态</td>
                    <td><!--操作--></td>
                </tr>
                <tr v-for="(order,index) in orders" v-cloak>
                    <td :title="'订单创建时间：'+timeFormat(order.createDate)">{{order.id}}</td>
                    <td :title="order.room.type.type">
                        {{order.room.roomNumber.floor}}-{{order.room.roomNumber.number}}-{{order.room.type.type}}
                    </td>
                    <td>
                        <span v-for="guest in order.guests">{{guest.name}}:{{guest.identification}}</span>
                    </td>
                    <td>{{timeFormat(order.dateFrom)}}</td>
                    <td>{{timeFormat(order.dateTo)}}</td>
                    <td class="status">
                        <!--10等待入住，00已取消，11已入住，01已完成-->
                        {{status(order.activated,order.used)}}
                    </td>
                    <td>
                        <!--等待入住显示-->
                        <div class="cancelOrder btn btn-default" v-if="order.activated && !order.used" :index="index"
                             @click="cancelBooking">取消订单
                        </div>
                        <!--已完成后显示-->
                        <div class="commentlOrder btn btn-default" v-if="!order.activated && order.used" :index="index"
                             @click="addComment">评价
                        </div>
                    </td>
                </tr>
            </table>
        </div>
            `;
            //生成html
            render_Container(resStr);
            //script
            let app = new Vue({
                el: '#check_my_order',
                data: {
                    orders: data
                },
                methods: {
                    timeFormat: function (time) {
                        return new Date(time).toLocaleString();
                    },
                    orderActivated: function (isActivated) {
                        return isActivated ? '有效' : '已失效'
                    },
                    orderUsed: function (isUsed) {
                        return isUsed ? '已入住' : '未入住'
                    },
                    status: function (activated, used) {
                        if (activated) {
                            if (used) return '已入住';
                            else return '等待入住';
                        } else {
                            if (used) return '已完成';
                            else return '已取消'
                        }
                    },
                    cancelBooking: function (e) {
                        let index = +$(e.target).attr('index');
                        let bookID = app.orders[index].id;
                        let line = $(`.orders table tr:nth-child(${index + 2})`);
                        if (!confirm(`确定取消订单${bookID}？`)) return;
                        startCatLoading();
                        $.ajax({
                            url: `${serverHost}/api/transactions?bookId=${bookID}`,
                            type: 'PATCH',
                            data: JSON.stringify({
                                activated: false
                            }),
                            contentType: "application/json; charset=UTF-8",
                            beforeSend: function (xhr) {
                                xhr.setRequestHeader("Authorization", "Basic " + btoa(sessionStorage.username + ":" + sessionStorage.password));
                            },
                            success: function (data, textStatus, jqXHR) {
                                line.find('.status').text('已取消');
                                line.find('.cancelOrder').hide();
                                showMsg('取消订单成功');
                            },
                            error: function (jqXHR, textStatus, errorThrown) {
                                let msg = '取消订单失败';
                                switch (jqXHR.status) {
                                    default:
                                        msg += ':网络错误';
                                }
                                showMsg(msg);
                            },
                            complete: function () {
                                stopCatLoading();
                            }
                        });
                    },
                    addComment: function () {
                        //TODO
                        //等待接口
                    }

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

/*
* 用户端：个人信息
* 修改自己的昵称、密码
* 添加旅客身份证
*/
async function modify_my_info() {
    //身份验证&获取数据
    $.ajax({
        url: `${serverHost}/api/users/guests?phone=${sessionStorage.username }`,
        type: 'GET',
        dataType: 'json',
        contentType: "application/json; charset=UTF-8",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Authorization", "Basic " + btoa(sessionStorage.username + ":" + sessionStorage.password));
        },
        success: function (data, textStatus, jqXHR) {
            let resStr = `
            <div id="modify_my_info">
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
                                <div class="btn btn-default" @click="modifyPro">确认修改</div>
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
                        <tr v-for="(guest,index) in guests" v-cloak>
                            <td>{{guest.name}}</td>
                            <td>{{guest.identification}}</td>
                            <td>
                                <div class="comfirm btn btn-default" :index="index" @click="delID">删除</div>
                            </td>
                        </tr>
                        <tr>
                            <td><input type="text" id="newG" title="name" placeholder="新旅客姓名"></td>
                            <td><input type="text" id="newID" title="ID" placeholder="新旅客身份证"></td>
                            <td>
                                <div class="btn btn-default confirm" @click="addID">添加</div>
                            </td>
                        </tr>
                    </table>
                </dd>
            </dl>
        </div>
            `;
            //生成html
            render_Container(resStr);
            //初始化界面
            $.mask.definitions['X']='[0-9Xx]';
            $('#newID').mask("999999-9999-99-99-999X");
            const app = new Vue({
                el: '#modify_my_info',
                data: {
                    nickname: sessionStorage.nickname || localStorage.nickname,
                    guests: data
                },
                methods: {
                    //修改个人资料
                    modifyPro: function () {
                        let newPassword = $('#newPWD').val();
                        let oldNickname = sessionStorage.nickname || localStorage.nickname;
                        let newNickname = $('#nickname').val();
                        if (newPassword !== $('#newPWDR').val()) {
                            showMsg('两次输入的密码不相符！');
                            return;
                        }
                        startCatLoading();
                        $.ajax({
                            url: `${serverHost}/api/users?phone=${sessionStorage.username }`,
                            type: 'PATCH',
                            data: JSON.stringify({
                                nickname: newNickname,
                                password: newPassword.length > 0 ? newPassword : undefined
                            }),
                            dataType: 'json',
                            contentType: "application/json; charset=UTF-8",
                            beforeSend: function (xhr) {
                                xhr.setRequestHeader("Authorization", "Basic " + btoa(sessionStorage.username + ":" + sessionStorage.password));
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
                    },
                    //添加身份证信息
                    addID: function () {
                        let newG = $('#newG').val();
                        let newID = $('#newID').val().replace(/-/g,'').toLocaleUpperCase();
                        if (isEmpty(newG, newID)) {
                            showMsg('请完整填写新旅客信息');
                            return;
                        }
                        startCatLoading();
                        $.ajax({
                            url: `${serverHost}/api/users/guests?phone=${sessionStorage.username }`,
                            type: 'POST',
                            data: JSON.stringify({
                                name: newG,
                                identification: newID,
                            }),
                            contentType: "application/json; charset=UTF-8",
                            beforeSend: function (xhr) {
                                xhr.setRequestHeader("Authorization", "Basic " + btoa(sessionStorage.username + ":" + sessionStorage.password));
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
                                showMsg(msg);
                            },
                            complete: function () {
                                stopCatLoading();
                            }
                        })
                    },
                    //删除身份证信息
                    delID: function (e) {
                        let index = +$(e.target).attr('index');
                        let name = app.guests[index].name;
                        let identification = app.guests[index].identification;
                        if (!confirm(`确定删除旅客"${name}:${identification}"？`)) return;
                        startCatLoading();
                        $.ajax({
                            url: `${serverHost}/api/users/guests?identification=${identification}`,
                            type: 'DELETE',
                            contentType: "application/json; charset=UTF-8",
                            beforeSend: function (xhr) {
                                xhr.setRequestHeader("Authorization", "Basic " + btoa(sessionStorage.username + ":" + sessionStorage.password));
                            },
                            success: function (data, textStatus, jqXHR) {
                                app.guests.splice(index, 1);
                                showMsg('删除旅客成功！')
                            },
                            error: function (jqXHR, textStatus, errorThrown) {
                                let msg = '删除旅客失败';
                                switch (jqXHR.status) {

                                }
                                showMsg(msg);
                            },
                            complete: function () {
                                stopCatLoading();
                            }
                        });
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

/*
* 前台端/经理端：客房管理
* 列出所有房间信息
* 可筛选（多条件筛选） TODO
* 可报修
* 可登记客户预定（管理层下单） TODO
* 可直接登记身份证散客入住（管理层下单） TODO
*/
async function rooms_all_info() {
    $.ajax({
        url: `${serverHost}/api/rooms`,
        type: 'GET',
        dataType: 'json',
        contentType: "application/json; charset=UTF-8",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Authorization", "Basic " + btoa(sessionStorage.username + ":" + sessionStorage.password));
        },
        success: function (data, textStatus, jqXHR) {
            //获取订单
            let resStr = `
            
            `;
            //生成html
            render_Container(resStr);
            //script

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

/*
× 前台端/经理端：订单管理
* 获取所有订单信息，可查看订单状态
* 可筛选（四态订单） TODO
* 可根据身份证筛选 TODO
* 可登记入住
* 可设置取消订单
* 可以修改订单 TODO //有较大逻辑问题
* 可退房
* 可续住 TODO
* 可换房 TODO //未解决逻辑问题
*/
async function check_all_booking() {
    $.ajax({
        url: `${serverHost}/api/transactions/list`,
        type: 'GET',
        dataType: 'json',
        contentType: "application/json; charset=UTF-8",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Authorization", "Basic " + btoa(sessionStorage.username + ":" + sessionStorage.password));
        },
        success: function (data, textStatus, jqXHR) {
            //获取订单
            let resStr = `
                <div id="check_all_booking">
            <dl>
                <dt>所有订单：</dt>
                <dd>
                    <table class="orders">
                        <tr>
                            <td>订单编号</td>
                            <td>房间楼层号与类型</td>
                            <td>入住人</td>
                            <td>入住时间</td>
                            <td>退房时间</td>
                            <td>订单状态</td>
                            <td><!--登记入住or退房--></td>
                            <td>
                                <!--取消-->
                            </td>
                        </tr>
                        <tr v-for="(order,index) in orders" v-cloak>
                            <td :title="'订单创建时间：'+timeFormat(order.createDate)">{{order.id}}</td>
                            <td :title="order.room.type.type">
                                {{order.room.roomNumber.floor}}-{{order.room.roomNumber.number}}-{{order.room.type.type}}
                            </td>
                            <td>
                                <span v-for="guest in order.guests">{{guest.name}}:{{guest.identification}}</span>
                            </td>
                            <td>{{timeFormat(order.dateFrom)}}</td>
                            <td>{{timeFormat(order.dateTo)}}</td>
                            <td class="status">
                                <!--10等待入住，00已取消，11已入住，01已完成-->
                                {{status(order.activated,order.used)}}
                            </td>
                            <td>
                                <!--等待入住显示-->
                                <div class="checkIn btn btn-default" v-if="order.activated && !order.used"
                                     :index="index"
                                     @click="checkIn">到店入住
                                </div>
                                <!--已入住后显示-->
                                <div class="checkOut btn btn-default" v-if="order.activated && order.used"
                                     :index="index"
                                     @click="checkOut">退房
                                </div>
                            </td>
                            <td><!--等待入住显示-->
                                <div class="cancelOrder btn btn-default" v-if="order.activated && !order.used"
                                     :index="index"
                                     @click="cancelBooking">取消订单
                                </div>
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
            // let data = [
            //     {
            //         "id": 29,
            //         "createDate": "2017-12-28T21:48:00",
            //         "room": {
            //             "id": 15,
            //             "roomNumber": {"floor": 1, "number": 1},
            //             "type": {"id": 1, "type": "大床房", "description": "足够两个人唑在一起的大小"},
            //             "direction": {"id": 11, "type": "南边", "description": "222"},
            //             "specialty": "",
            //             "price": 199,
            //             "broken": false
            //         },
            //         "guests": [{"id": 27, "identification": "123321123321", "name": "hs"}],
            //         "dateFrom": "2017-12-28T21:48:00",
            //         "dateTo": "2017-12-28T21:48:00",
            //         "activated": true,
            //         "used": false
            //     }, {
            //         "id": 30,
            //         "createDate": "2017-12-28T21:48:20",
            //         "room": {
            //             "id": 20,
            //             "roomNumber": {"floor": 1, "number": 2},
            //             "type": {"id": 19, "type": "超级大床房", "description": "足够二十个人唑在一起的大小"},
            //             "direction": {"id": 18, "type": "北方", "description": "为所欲为"},
            //             "specialty": "可以挤很多人",
            //             "price": 1999,
            //             "broken": false
            //         },
            //         "guests": [{"id": 27, "identification": "123321123321", "name": "hs"}, {
            //             "id": 28,
            //             "identification": "1233211233211231",
            //             "name": "adasd"
            //         }],
            //         "dateFrom": "2017-12-28T21:48:20",
            //         "dateTo": "2017-12-28T21:48:20",
            //         "activated": true,
            //         "used": false
            //     }]
            let app = new Vue({
                el: '#check_all_booking',
                data: {
                    orders: data
                },
                methods: {
                    timeFormat: function (time) {
                        return new Date(time).toLocaleString();
                    },
                    orderActivated: function (isActivated) {
                        return isActivated ? '有效' : '已失效'
                    },
                    orderUsed: function (isUsed) {
                        return isUsed ? '已入住' : '未入住'
                    },
                    status: function (activated, used) {
                        if (activated) {
                            if (used) return '已入住';
                            else return '等待入住';
                        } else {
                            if (used) return '已完成';
                            else return '已取消'
                        }
                    },
                    cancelBooking: function (e) {
                        let index = +$(e.target).attr('index');
                        let bookID = app.orders[index].id;
                        let line = $(`.orders table tr:nth-child(${index + 2})`);
                        if (!confirm(`确定取消订单${bookID}？`)) return;
                        startCatLoading();
                        $.ajax({
                            url: `${serverHost}/api/transactions?bookId=${bookID}`,
                            type: 'PATCH',
                            data: JSON.stringify({
                                activated: false
                            }),
                            contentType: "application/json; charset=UTF-8",
                            beforeSend: function (xhr) {
                                xhr.setRequestHeader("Authorization", "Basic " + btoa(sessionStorage.username + ":" + sessionStorage.password));
                            },
                            success: function (data, textStatus, jqXHR) {
                                line.find('.status').text('已取消');
                                line.find('.cancelOrder').hide();
                                showMsg('取消订单成功');
                            },
                            error: function (jqXHR, textStatus, errorThrown) {
                                let msg = '取消订单失败';
                                switch (jqXHR.status) {
                                    default:
                                        msg += ':网络错误';
                                }
                                showMsg(msg);
                            },
                            complete: function () {
                                stopCatLoading();
                            }
                        });
                    },
                    checkIn: function (e) {
                        let index = +$(e.target).attr('index');
                        let bookID = app.orders[index].id;
                        let line = $(`.orders table tr:nth-child(${index + 2})`);
                        if (!confirm(`订单${bookID}确定入住？`)) return;
                        startCatLoading();
                        $.ajax({
                            url: `${serverHost}/api/transactions?bookId=${bookID}`,
                            type: 'PATCH',
                            data: JSON.stringify({
                                used: true
                            }),
                            contentType: "application/json; charset=UTF-8",
                            beforeSend: function (xhr) {
                                xhr.setRequestHeader("Authorization", "Basic " + btoa(sessionStorage.username + ":" + sessionStorage.password));
                            },
                            success: function (data, textStatus, jqXHR) {
                                line.find('.status').text('已入住');
                                line.find('.checkIn').hide();
                                showMsg('客户入住成功');
                            },
                            error: function (jqXHR, textStatus, errorThrown) {
                                let msg = '客户入住失败';
                                switch (jqXHR.status) {
                                    default:
                                        msg += ':网络错误';
                                }
                                showMsg(msg);
                            },
                            complete: function () {
                                stopCatLoading();
                            }
                        });
                    },
                    checkOut: function (e) {
                        let index = +$(e.target).attr('index');
                        let bookID = app.orders[index].id;
                        let line = $(`.orders table tr:nth-child(${index + 2})`);
                        if (!confirm(`订单${bookID}确定退房？`)) return;
                        startCatLoading();
                        $.ajax({
                            url: `${serverHost}/api/transactions?bookId=${bookID}`,
                            type: 'PATCH',
                            data: JSON.stringify({
                                activated: false
                            }),
                            contentType: "application/json; charset=UTF-8",
                            beforeSend: function (xhr) {
                                xhr.setRequestHeader("Authorization", "Basic " + btoa(sessionStorage.username + ":" + sessionStorage.password));
                            },
                            success: function (data, textStatus, jqXHR) {
                                line.find('.status').text('已完成');
                                line.find('.checkOut').hide();
                                showMsg('客户退房成功');
                                //TODO
                                //结账界面
                            },
                            error: function (jqXHR, textStatus, errorThrown) {
                                let msg = '客户退房失败';
                                switch (jqXHR.status) {
                                    default:
                                        msg += ':网络错误';
                                }
                                showMsg(msg);
                            },
                            complete: function () {
                                stopCatLoading();
                            }
                        });
                    }

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

/*
* 前台端/经理端：房态图
* 所有客房状态 TODO
* 已入住客房信息 TODO
*/
async function room_graph() {
    //身份验证&获取数据
    $.ajax({
        url: `${serverHost}/api`,
        type: 'GET',
        dataType: 'json',
        contentType: "application/json; charset=UTF-8",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Authorization", "Basic " + btoa(sessionStorage.username + ":" + sessionStorage.password));
        },
        success: function (data, textStatus, jqXHR) {
            //获取订单
            let resStr = `
            
            `;
            //生成html
            render_Container(resStr);
            //script
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

/*
* 经理端：客房类型设置
* （获取/修改/删除）当前所有房间（类型/方向/房间）
*/
async function modify_rooms_type() {
    //身份验证&获取数据
    $.ajax({
        url: `${serverHost}/api/rooms/load2`,
        type: 'GET',
        dataType: 'json',
        contentType: "application/json; charset=UTF-8",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Authorization", "Basic " + btoa(sessionStorage.username + ":" + sessionStorage.password));
        },
        success: function (data, textStatus, jqXHR) {
            //获取订单
            let resStr = `
                <div id="modify_rooms_type">
            <div class="type">
                <h4>房间类型管理</h4>
                <table>
                    <tr>
                        <td>类型名称</td>
                        <td>房间描述</td>
                        <td><!--修改--></td>
                        <td><!--删除--></td>
                    </tr>
                    <tr v-for="(room,index) in types" v-cloak>
                        <td>{{room.type}}</td>
                        <td><input type="text" class="modTypeDes" :value="room.description"></td>
                        <td>
                            <div class="confirm btn btn-default" :index="index" @click="modType">修改</div>
                        </td>
                        <td>
                            <div class="confirm btn btn-default" :index="index" @click="delType">删除</div>
                        </td>
                    </tr>
                    <tr>
                        <td><input type="text" id="newType" title="新客房类型" placeholder="新客房类型"></td>
                        <td><input type="text" id="newTypeDes" title="新客房类型描述" placeholder="新客房类型描述"></td>
                        <td>
                            <div class="btn btn-default confirm" @click="addType">添加</div>
                        </td>
                        <td></td>
                    </tr>
                </table>
            </div>
            <div class="direction"><h4>房间朝向管理</h4>
                <table>
                    <tr>
                        <td>朝向名称</td>
                        <td>朝向描述</td>
                        <td><!--修改--></td>
                        <td><!--删除--></td>
                    </tr>
                    <tr v-for="(direction,index) in directions" v-cloak>
                        <td>{{direction.type}}</td>
                        <td><input type="text" class="modDirDes" :value="direction.description"></td>
                        <td>
                            <div class="confirm btn btn-default" :index="index" @click="modDir">修改</div>
                        </td>
                        <td>
                            <div class="confirm btn btn-default" :index="index" @click="delDir">删除</div>
                        </td>
                    </tr>
                    <tr>
                        <td><input type="text" id="newDir" title="新朝向名称" placeholder="新朝向名称"></td>
                        <td><input type="text" id="newDirDes" title="新朝向描述" placeholder="新朝向描述"></td>
                        <td>
                            <div class="btn btn-default confirm" @click="addDir">添加</div>
                        </td>
                        <td></td>
                    </tr>
                </table>
            </div>
            <div class="room">
                <h4>所有客房管理</h4>
                <table>
                    <tr>
                        <td>楼层</td>
                        <td>房号</td>
                        <td>朝向</td>
                        <td>房间类型</td>
                        <td>特色</td>
                        <td>价格</td>
                        <td>是否有损坏</td>
                        <td><!--修改--></td>
                        <td><!--删除--></td>
                    </tr>
                    <tr v-for="(room,index) in rooms" v-cloak>
                        <td>{{room.roomNumber.floor}}</td>
                        <td>{{room.roomNumber.number}}</td>
                        <td>
                            <select class="modRDir">
                                <option v-for="edirection in directions" :title="room.direction.description"
                                        :selected="edirection.type === room.direction.type" v-cloak>{{edirection.type}}
                                </option>
                            </select>
                        </td>
                        <td>
                            <select class="modRType">
                                <option v-for="etype in types" :title="room.type.description"
                                        :selected="etype.type === room.type.type" v-cloak>{{etype.type}}
                                </option>
                            </select>
                        </td>
                        <td><input class="modRSpe" type="text" :value="room.specialty"></td>
                        <td><input class="modRpri" type="number" min="0" :value="room.price"></td>
                        <td><select class="modRBro">
                            <option value="true" :selected="room.broken === true">需维修</option>
                            <option value="false" :selected="room.broken === false">无需维修</option>
                        </select>
                        </td>
                        <td>
                            <div class="confirm btn btn-default" :index="index" @click="modRoom">修改</div>
                        </td>
                        <td>
                            <div class="confirm btn btn-default" :index="index" @click="delRoom">删除</div>
                        </td>
                    </tr>
                    <tr>
                        <td><input type="number" min="0" id="newRFloor" title="开放新房间楼层" placeholder="开放新房间楼层"></td>
                        <td><input type="number" min="0" id="newRNum" title="开放新房间房号" placeholder="开放新房间房号"></td>
                        <td>
                            <select id="newRDir" title="开放新房间朝向">
                                <option v-for="edirection in directions" v-cloak>{{edirection.type}}</option>
                            </select>
                        </td>
                        <td>
                            <select id="newRType" title="开放新房间类型">
                                <option v-for="etype in types" v-cloak>{{etype.type}}</option>
                            </select>
                        </td>
                        <td><input type="text" id="newRSpecial" title="开放新房间特色" placeholder="开放新房间特色"></td>
                        <td><input type="number" min="0" id="newRPrice" title="开放新房间价格"
                                   placeholder="新房间价格"></td>
                        <td><select id="newRBroken" title="开放新房间是否损坏">
                            <option value="true">需维修</option>
                            <option value="false" selected>无需维修</option>
                        </select>
                        </td>
                        <td>
                            <div class="btn btn-default confirm" @click="addRoom">添加</div>
                        </td>
                        <td></td>
                    </tr>
                </table>
            </div>
        </div>
            `;
            //生成html
            render_Container(resStr);
            //script
            let app = new Vue({
                el: '#modify_rooms_type',
                data: {
                    types: data.types,
                    directions: data.directions,
                    rooms: data.rooms
                },
                methods: {
                    addType: function () {
                        let newType = $('#newType').val();
                        let newTypeDes = $('#newTypeDes').val();
                        if (newType.length === 0) {
                            showMsg('新类型名称不能为空');
                            return;
                        }
                        startCatLoading();
                        $.ajax({
                            url: `${serverHost}/api/rooms/types`,
                            type: 'POST',
                            contentType: "application/json; charset=UTF-8",
                            data: JSON.stringify({
                                type: newType,
                                description: newTypeDes
                            }),
                            beforeSend: function (xhr) {
                                xhr.setRequestHeader("Authorization", "Basic " + btoa(sessionStorage.username + ":" + sessionStorage.password));
                            },
                            success: function (data, textStatus, jqXHR) {
                                app.types.push({
                                    id: -1,
                                    type: newType,
                                    description: newTypeDes
                                });
                                $('#newType').val('');
                                $('#newTypeDes').val('');
                                showMsg('添加成功！')
                            },
                            error: function (jqXHR, textStatus, errorThrown) {
                                let msg = '添加失败';
                                switch (jqXHR.status) {
                                    case 400:
                                        msg += '格式错误';
                                        break;
                                    case 409:
                                        msg += '已存在该类型';
                                        break;
                                }
                                showMsg(msg);
                            },
                            complete: function () {
                                stopCatLoading();
                            }
                        });
                    },
                    modType: function (e) {
                        let index = +$(e.target).attr('index');
                        let line = $(`.type table tr:nth-child(${index + 2})`);
                        let modType = app.types[index].type;
                        let modTypeDes = line.find('.modTypeDes').val();
                        if (!confirm(`确定修改房间类型“${modType}”的描述为“${modTypeDes}”？`)) return;
                        startCatLoading();
                        $.ajax({
                            url: `${serverHost}/api/rooms/types?type=${modType}`,
                            type: 'PATCH',
                            data: JSON.stringify({
                                description: modTypeDes
                            }),
                            contentType: "application/json; charset=UTF-8",
                            beforeSend: function (xhr) {
                                xhr.setRequestHeader("Authorization", "Basic " + btoa(sessionStorage.username + ":" + sessionStorage.password));
                            },
                            success: function (data, textStatus, jqXHR) {
                                app.types.splice(index, 1, {
                                    type: modType,
                                    description: modTypeDes
                                });
                                showMsg('修改成功！')
                            },
                            error: function (jqXHR, textStatus, errorThrown) {
                                let msg = '修改失败';
                                switch (jqXHR.status) {

                                }
                                showMsg(msg);
                            },
                            complete: function () {
                                stopCatLoading();
                            }
                        });
                    },
                    delType: function (e) {
                        let index = +$(e.target).attr('index');
                        let delType = app.types[index].type;
                        if (!confirm(`确定删除房间类型"${delType}"？`)) return;
                        startCatLoading();
                        $.ajax({
                            url: `${serverHost}/api/rooms/types?type=${delType}`,
                            type: 'DELETE',
                            contentType: "application/json; charset=UTF-8",
                            beforeSend: function (xhr) {
                                xhr.setRequestHeader("Authorization", "Basic " + btoa(sessionStorage.username + ":" + sessionStorage.password));
                            },
                            success: function (data, textStatus, jqXHR) {
                                app.types.splice(index, 1);
                                showMsg('删除成功！')
                            },
                            error: function (jqXHR, textStatus, errorThrown) {
                                let msg = '删除失败';
                                switch (jqXHR.status) {

                                }
                                showMsg(msg);
                            },
                            complete: function () {
                                stopCatLoading();
                            }
                        });
                    },
                    addDir: function () {
                        let newDir = $('#newDir').val();
                        let newDirDes = $('#newDirDes').val();
                        if (newDir.length === 0) {
                            showMsg('新朝向名称不能为空');
                            return;
                        }
                        startCatLoading();
                        $.ajax({
                            url: `${serverHost}/api/rooms/directions`,
                            type: 'POST',
                            contentType: "application/json; charset=UTF-8",
                            data: JSON.stringify({
                                type: newDir,
                                description: newDirDes
                            }),
                            beforeSend: function (xhr) {
                                xhr.setRequestHeader("Authorization", "Basic " + btoa(sessionStorage.username + ":" + sessionStorage.password));
                            },
                            success: function (data, textStatus, jqXHR) {
                                app.directions.push({
                                    id: -1,
                                    type: newDir,
                                    description: newDirDes
                                });
                                $('#newDir').val('');
                                $('#newDirDes').val('');
                                showMsg('添加成功！');
                            },
                            error: function (jqXHR, textStatus, errorThrown) {
                                let msg = '添加失败';
                                switch (jqXHR.status) {
                                    case 409:
                                        msg += '已存在该方位';
                                }
                                showMsg(msg);
                            },
                            complete: function () {
                                stopCatLoading();
                            }
                        });
                    },
                    modDir: function (e) {
                        let index = +$(e.target).attr('index');
                        let line = $(`.direction table tr:nth-child(${index + 2})`);
                        let modDir = app.directions[index].type;
                        let modDirDes = line.find('.modDirDes').val();
                        if (!confirm(`确定修改房间朝向“${modDir}”的描述为“${modDirDes}”？`)) return;
                        startCatLoading();
                        $.ajax({
                            url: `${serverHost}/api/rooms/directions?direction=${modDir}`,
                            type: 'PATCH',
                            data: JSON.stringify({
                                description: modDirDes
                            }),
                            contentType: "application/json; charset=UTF-8",
                            beforeSend: function (xhr) {
                                xhr.setRequestHeader("Authorization", "Basic " + btoa(sessionStorage.username + ":" + sessionStorage.password));
                            },
                            success: function (data, textStatus, jqXHR) {
                                app.directions.splice(index, 1, {
                                    type: modDir,
                                    description: modDirDes
                                });
                                showMsg('修改成功！')
                            },
                            error: function (jqXHR, textStatus, errorThrown) {
                                let msg = '修改失败';
                                switch (jqXHR.status) {

                                }
                                showMsg(msg);
                            },
                            complete: function () {
                                stopCatLoading();
                            }
                        });
                    },
                    delDir: function (e) {
                        let index = +$(e.target).attr('index');
                        let delDir = app.directions[index].type;
                        if (!confirm(`确定删除方向"${delDir}"?`)) return;
                        startCatLoading();
                        $.ajax({
                            url: `${serverHost}/api/rooms/directions?direction=${delDir}`,
                            type: 'DELETE',
                            contentType: "application/json; charset=UTF-8",
                            beforeSend: function (xhr) {
                                xhr.setRequestHeader("Authorization", "Basic " + btoa(sessionStorage.username + ":" + sessionStorage.password));
                            },
                            success: function (data, textStatus, jqXHR) {
                                app.directions.splice(index, 1);

                                showMsg('删除成功！')
                            },
                            error: function (jqXHR, textStatus, errorThrown) {
                                let msg = '删除失败';
                                switch (jqXHR.status) {

                                }
                                showMsg(msg);
                            },
                            complete: function () {
                                stopCatLoading();
                            }
                        });
                    },
                    addRoom: function () {
                        let newRFloor = $('#newRFloor').val();
                        let newRNum = $('#newRNum').val();
                        let newRDir = $('#newRDir').children('option:selected').text();
                        let newRType = $('#newRType').children('option:selected').text();
                        let newRSpecial = $('#newRSpecial').val();
                        let newRPrice = $('#newRPrice').val();
                        let newRBroken = $('#newRBroken').val();
                        if (isEmpty(newRFloor, newRNum, newRDir, newRType, newRPrice, newRBroken)) {
                            showMsg('请完整填写新房间信息');
                            return;
                        }
                        startCatLoading();
                        $.ajax({
                            url: `${serverHost}/api/rooms`,
                            type: 'POST',
                            contentType: "application/json; charset=UTF-8",
                            data: JSON.stringify({
                                type: newRType,
                                direction: newRDir,
                                specialty: newRSpecial,
                                price: newRPrice,
                                roomNumber: {
                                    floor: newRFloor,
                                    number: newRNum
                                }
                            }),
                            beforeSend: function (xhr) {
                                xhr.setRequestHeader("Authorization", "Basic " + btoa(sessionStorage.username + ":" + sessionStorage.password));
                            },
                            success: function (data, textStatus, jqXHR) {
                                app.rooms.push({
                                    id: -1,
                                    roomNumber: {
                                        floor: newRFloor,
                                        number: newRNum
                                    },
                                    type: {
                                        id: -2,
                                        type: newRType,
                                        description: ''
                                    },
                                    direction: {
                                        id: -2,
                                        type: newRDir,
                                        description: '',
                                        room: []
                                    },
                                    specialty: newRSpecial,
                                    price: newRPrice
                                });
                                showMsg('添加成功！')
                            },
                            error: function (jqXHR, textStatus, errorThrown) {
                                let msg = '添加失败';
                                switch (jqXHR.status) {
                                    case 409:
                                        msg += '已存在该房间';
                                }
                                showMsg(msg);
                            },
                            complete: function () {
                                stopCatLoading();
                            }
                        });
                    },
                    modRoom: function (e) {
                        let index = +$(e.target).attr('index');
                        let line = $(`.room table tr:nth-child(${index + 2})`);
                        let modRFloor = app.rooms[index].roomNumber.floor;
                        let modRNumber = app.rooms[index].roomNumber.number;
                        let modRDir = line.find('.modRDir option:selected').text();
                        let modRType = line.find('.modRType option:selected').text();
                        let modRSpe = line.find('.modRSpe').val();
                        let modRpri = line.find('.modRpri').val();
                        let modRBro = line.find('.modRBro option:selected').val() === 'true';
                        if (!confirm(`确定修改${modRFloor}层${modRNumber}号房间的信息？`)) return;
                        startCatLoading();
                        $.ajax({
                            url: `${serverHost}/api/rooms?floor=${modRFloor}&number=${modRNumber}`,
                            type: 'PATCH',
                            data: JSON.stringify({
                                type: modRType,
                                direction: modRDir,
                                specialty: modRSpe,
                                price: modRpri,
                                broken: modRBro
                            }),
                            contentType: "application/json; charset=UTF-8",
                            beforeSend: function (xhr) {
                                xhr.setRequestHeader("Authorization", "Basic " + btoa(sessionStorage.username + ":" + sessionStorage.password));
                            },
                            success: function (data, textStatus, jqXHR) {
                                app.directions.splice(index, 1, {
                                    type: modRFloor,
                                    description: modRSpe
                                });
                                showMsg('修改成功！')
                            },
                            error: function (jqXHR, textStatus, errorThrown) {
                                let msg = '修改失败';
                                switch (jqXHR.status) {

                                }
                                showMsg(msg);
                            },
                            complete: function () {
                                stopCatLoading();
                            }
                        });
                    },
                    delRoom: function (e) {
                        let index = +$(e.target).attr('index');
                        if (!confirm(`确定删除${app.rooms[index].roomNumber.floor}层${app.rooms[index].roomNumber.number}号房间?`)) return;
                        startCatLoading();
                        $.ajax({
                            url: `${serverHost}/api/rooms?floor=${app.rooms[index].roomNumber.floor}&number=${app.rooms[index].roomNumber.number}`,
                            type: 'DELETE',
                            contentType: "application/json; charset=UTF-8",
                            beforeSend: function (xhr) {
                                xhr.setRequestHeader("Authorization", "Basic " + btoa(sessionStorage.username + ":" + sessionStorage.password));
                            },
                            success: function (data, textStatus, jqXHR) {
                                app.rooms.splice(index, 1);
                                showMsg('删除成功！')
                            },
                            error: function (jqXHR, textStatus, errorThrown) {
                                let msg = '删除失败';
                                switch (jqXHR.status) {

                                }
                                showMsg(msg);
                            },
                            complete: function () {
                                stopCatLoading();
                            }
                        });
                    },
                    broken: function (isBroken) {
                        return isBroken ? '需维修' : '无需维修';
                    }
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

/*
* 经理端：账户管理
* 添加任何类型账户
* 修改/删除现有所有账户
*/
async function modify_user_info() {
    //身份验证&获取数据
    $.ajax({
        url: `${serverHost}/api/users`,
        type: 'GET',
        dataType: 'json',
        contentType: "application/json; charset=UTF-8",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Authorization", "Basic " + btoa(sessionStorage.username + ":" + sessionStorage.password));
        },
        success: function (data, textStatus, jqXHR) {
            //获取订单
            let resStr = `
            <div id="modify_user_info">
                <dl class="addAccount">
                    <dt>添加账户</dt>
                    <dd>
                        <table>
                            <tr>
                                <td>账户名：</td>
                                <td><input type="text" id="newAccName" title="新建账户"></td>
                            </tr>
                            <tr>
                                <td>账户昵称：</td>
                                <td><input type="text" id="newAccNick" title="新建账户昵称"></td>
                            </tr>
                            <tr>
                                <td>密码：</td>
                                <td><input type="password" id="newAccPWD" title="新建账户密码"></td>
                            </tr>
                            <tr>
                                <td>确认密码：</td>
                                <td><input type="password" id="newAccPWDR" title="新建账户确认密码"></td>
                            </tr>
                            <tr>
                                <td>账户类型：</td>
                                <td>
                                    <select id="newAccRole" title="新建账户类型">
                                        <option value="/admin">经理</option>
                                        <option value="/employee" selected>前台</option>
                                        <option value="">注册用户</option>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2">
                                    <div class="btn btn-default confirm" @click="addAcc">添加</div>
                                </td>
                            </tr>
                        </table>
                    </dd>
                </dl>
                <dl class="modifyAccount">
                    <dt>管理当前所有账户</dt>
                    <dd>
                        <table>
                            <tr>
                                <td>账户名（手机号）</td>
                                <td>账户昵称</td>
                                <td>密码<span>*不填写则不修改</span></td>
                                <td>账户类型</td>
                                <td>创建时间</td>
                                <td>绑定旅客</td>
                                <td><!--修改--></td>
                                <td><!--删除--></td>
                            </tr>
                            <tr v-for="(user,index) in users" v-cloak>
                                <td>{{user.phoneNumber}}</td>
                                <td><input type="text" class="mNickname" title="修改该用户昵称" :value="user.nickname"></td>
                                <td><input type="password" class="mPWD" title="修改该用户密码" placeholder="********"></td>
                                <td>
                                    <select title="修改该用户类型" class="mRole">
                                        <option value="经理" :selected="user.role === '经理'">经理</option>
                                        <option value="前台" :selected="user.role === '前台'">前台</option>
                                        <option value="注册用户" :selected="user.role === '注册用户'">注册用户</option>
                                    </select>
                                </td>
                                <td>{{user.register_date}}</td>
                                <td>
                                    <span v-for="guest in user.guests">{{guest.name}}:{{guest.identification}}</span>
                                </td>
                                <td>
                                    <div class="btn btn-default confirm" :index="index" @click="modAcc">修改</div>
                                </td>
                                <td>
                                    <div class="btn btn-default confirm" :index="index" @click="delAcc">删除</div>
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
            $("#newAccName").mask("999-9999-9999");
            let app = new Vue({
                el: '#modify_user_info',
                data: {
                    users: data
                },
                methods: {
                    addAcc: function () {
                        let username = $('#newAccName').val().replace(/-/g, '');
                        let nickname = $('#newAccNick').val();
                        let password = $('#newAccPWD').val();
                        let passwordAgain = $('#newAccPWDR').val();
                        let role = $('#newAccRole').children('option:selected');
                        if (isEmpty(username, passwordAgain, password, passwordAgain)) {
                            showMsg('请完整填写注册信息');
                        }
                        if (password !== passwordAgain) {
                            showMsg('两次输入密码不相符');
                            return;
                        }
                        startCatLoading();
                        $.ajax({
                            url: `${serverHost}/api/users/make${role}.val()`,
                            type: 'POST',
                            contentType: "application/json; charset=UTF-8",
                            data: JSON.stringify({
                                phoneNumber: username,
                                password: password,
                                nickname: nickname
                            }),
                            success: function (data, textStatus, jqXHR) {
                                app.users.push({
                                    id: -1,
                                    phoneNumber: username,
                                    nickname: nickname,
                                    password: password,
                                    register_date: new Date().toISOString(),
                                    role: role.text(),
                                    guests: []
                                })
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
                                stopCatLoading();
                            }
                        });
                    },
                    modAcc: function (e) {
                        let index = +$(e.target).attr('index');
                        let username = app.users[index].phoneNumber;
                        if (!confirm(`确定修改用户"${username}"的信息？`)) return;
                        let line = $(`.modifyAccount table tr:nth-child(${index + 2})`);
                        let nickname = line.find('.mNickname').val();
                        let password = line.find('.mPWD').val();
                        let role = line.find('.mRole option:selected').val();
                        startCatLoading();
                        $.ajax({
                            url: `${serverHost}/api/users?phone=${username}`,
                            type: 'PATCH',
                            data: {
                                password: password.length > 0 ? password : undefined,
                                nickname: nickname,
                                role: role
                            },
                            contentType: "application/json; charset=UTF-8",
                            beforeSend: function (xhr) {
                                xhr.setRequestHeader("Authorization", "Basic " + btoa(sessionStorage.username + ":" + sessionStorage.password));
                            },
                            success: function (data, textStatus, jqXHR) {

                            },
                            error: function (jqXHR, textStatus, errorThrown) {

                            },
                            complete: function () {
                                stopCatLoading();
                            }
                        });
                    },
                    delAcc: function (e) {
                        let index = +$(e.target).attr('index');
                        let username = app.users[index].phoneNumber;
                        if (!confirm(`确定删除用户"${username}"？`)) return;
                        startCatLoading();
                        $.ajax({
                            url: `${serverHost}/api/users?phone=${username}`,
                            type: 'DELETE',
                            contentType: "application/json; charset=UTF-8",
                            beforeSend: function (xhr) {
                                xhr.setRequestHeader("Authorization", "Basic " + btoa(sessionStorage.username + ":" + sessionStorage.password));
                            },
                            success: function (data, textStatus, jqXHR) {
                                app.users.splice(index, 1);
                                showMsg('删除成功！')
                            },
                            error: function (jqXHR, textStatus, errorThrown) {
                                let msg = '删除失败';
                                switch (jqXHR.status) {

                                }
                                showMsg(msg);
                            },
                            complete: function () {
                                stopCatLoading();
                            }
                        });
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

/*
* 经理端：销售月表
* 显示当月每天的销售情况 TODO
*/
async function sales_per_month() {
    //身份验证&获取数据
    $.ajax({
        url: `${serverHost}/api`,
        type: 'GET',
        dataType: 'json',
        contentType: "application/json; charset=UTF-8",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Authorization", "Basic " + btoa(sessionStorage.username + ":" + sessionStorage.password));
        },
        success: function (data, textStatus, jqXHR) {
            //获取订单
            let resStr = `
            
            `;
            //生成html
            render_Container(resStr);
            //script
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

/*
* 经理端：客户分析
* 在住房客信息 TODO
*/
async function client_analyze() {
    //身份验证&获取数据
    $.ajax({
        url: `${serverHost}/api`,
        type: 'GET',
        dataType: 'json',
        contentType: "application/json; charset=UTF-8",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Authorization", "Basic " + btoa(sessionStorage.username + ":" + sessionStorage.password));
        },
        success: function (data, textStatus, jqXHR) {
            //获取订单
            let resStr = `
            
            `;
            //生成html
            render_Container(resStr);
            //script
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

/*
* 所有端：返回主页
*/
async function backHome() {
    location.href = '/';
}

/*
* 所有端：登出
* 退出登录并返回主页
*/
async function logout() {
    localStorage.clear();
    sessionStorage.clear();
    sessionStorage.isLogin = false;
    location.href = '/';
}
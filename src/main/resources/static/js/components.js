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
* 评价已完成订单
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
                    <td :title="'订单创建时间：'+timeFormat(order.createdDate)">{{order.id}}</td>
                    <td :title="'朝向：' + order.room.direction.type">
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
                        <div class="cancelOrder btn btn-default" v-if="order.activated && !order.used"
                             :index="index"
                             @click="cancelBooking">取消订单
                        </div>
                        <div class="cancelOrder btn btn-default disabled" v-if="order.activated && order.used">取消订单
                        </div>
                        <!--已完成后显示-->
                        <div class="commentlOrder btn btn-default" v-if="!order.activated && order.used"
                        :index="index"
                        @click="addComment">评价
                        </div>
                        <div class="commentlOrder btn btn-default disabled" alt="退房后可评价" v-if="order.activated && order.used">评价
                        </div>
                    </td>
                </tr>
            </table>
            <div class="commentList">
                <button type="button" class="close" @click="close" aria-label="Close">
                    <span id="closeCommentList" aria-hidden="true">&times;</span>
                </button>
                <div v-if="comments.length === 0" class="noCom">该房间还没有评论！</div>
                <div v-else class="content">
                    <dl v-for="comment in comments">
                        <dt>{{comment.user.nickname}}:</dt>
                        <dd>{{comment.body}}</dd>
                        <dd class="date">{{timeFormat(comment.createdDate)}}</dd>
                    </dl>
                </div>
                <div class="addCom">
                    <dl>
                        <dd><input id="addCom" type="text" placeholder="客官感受如何？">
                            <div class="btn btn-default" @click="addComment">添加评论
                            </div>
                        </dd>
                    </dl>
                </div>
            </div>
        </div>
            `;
            //生成html
            render_Container(resStr);
            //script
            let app = new Vue({
                el: '#check_my_order',
                data: {
                    orders: data,
                    comments: [],
                    selectedIndex: -1,
                },
                methods: {
                    timeFormat: function (time) {
                        return new Date(time).toLocaleString();
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
                                let tmp = app.orders[index];
                                tmp.activated = false;
                                app.orders.splice(index, 1, tmp);
                                showMsg('取消订单成功');
                            },
                            error: function (jqXHR, textStatus, errorThrown) {
                                let msg = '取消订单失败：';
                                switch (jqXHR.status) {
                                    default:
                                        msg += jqXHR.responseJSON && jqXHR.responseJSON.message || '网络错误';
                                }
                                showMsg(msg);

                            },
                            complete: function () {
                                stopCatLoading();
                            }
                        });
                    },
                    showCommentList: function (e) {
                        this.selectedIndex = +$(e.target).attr('index');
                        this.comments = this.orders[this.selectedIndex].room.comments;
                        $('#check_my_order').children('.commentList').slideDown();
                        if (this.orders[this.selectedIndex].commentId === null) {
                            $('#addCom').attr('placeholder', '哎呀已经评论过了呢')
                                .prop('disabled', true)
                                .css('width', '100%')
                                .css('text-align', 'center');
                            $('.commentList .addCom .btn').hide();
                        }
                    },
                    addComment: function () {
                        let text = $('#addCom').val();
                        if (isEmpty(text)) {
                            showMsg('评论内容不能为空');
                            return;
                        }
                        startCatLoading();
                        $.ajax({
                            url: `${serverHost}/api/comments/?transactionId=${app.orders[app.selectedIndex].id}`,
                            type: 'POST',
                            data: JSON.stringify({
                                body: text
                            }),
                            contentType: "application/json; charset=UTF-8",
                            beforeSend: function (xhr) {
                                xhr.setRequestHeader("Authorization", "Basic " + btoa(sessionStorage.username || localStorage.username + ":" + sessionStorage.password || localStorage.password));
                            },
                            success: function (data, textStatus, jqXHR) {
                                app.orders[app.selectedIndex].room.comments.push({
                                    body: text,
                                    user: {
                                        nickname: sessionStorage.nickname
                                    }
                                })
                                ;
                                showMsg('评论成功');
                            },
                            error: function (jqXHR, textStatus, errorThrown) {
                                let msg = '评论失败：';
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
                    close: function (e) {
                        let button = $(e.target);
                        let thisWindow = button.parent().parent();
                        thisWindow.slideUp();
                        thisWindow.find('.close span').trigger('click');
                    }
                }
            })
        },
        error: function (jqXHR, textStatus, errorThrown) {
            let msg = '获取失败：';
            switch (jqXHR.status) {
                default:
                    msg += jqXHR.responseJSON && jqXHR.responseJSON.message || '网络错误';
            }
            showMsg(msg);
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
                                password: newPassword.length > 0 ? sha256(newPassword) : undefined
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
                                let msg = '修改失败：';
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
                    //添加身份证信息
                    addID: function () {
                        let newG = $('#newG').val();
                        let newID = $('#newID').val().toLocaleUpperCase();
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
                                clearVal($('#newG'), $('#newID'));
                                showMsg(`添加新旅客${newG}成功！`);
                                app.guests.push({
                                    id: -1,
                                    identification: newID,
                                    name: newG
                                })
                            },
                            error: function (jqXHR, textStatus, errorThrown) {
                                let msg = '添加新旅客失败：';
                                switch (jqXHR.status) {
                                    case 409:
                                        msg += '已存在该旅客';
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
                                let msg = '删除旅客失败：';
                                switch (jqXHR.status) {
                                    default:
                                        msg += jqXHR.responseJSON && jqXHR.responseJSON.message || '网络错误';
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
            constraintID($('#newID'));
        },
        error: function (jqXHR, textStatus, errorThrown) {
            let msg = '获取列表详情失败：';
            switch (jqXHR.status) {
                case 401:
                    msg += '密码已失效，请重新登陆';
                    logout();
                    break;
                default:
                    msg += jqXHR.responseJSON && jqXHR.responseJSON.message || '网络错误';
            }
            showMsg(msg);
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
* 可筛选（多条件筛选）
* 可报修
* 可登记客户预定（管理层下单）
* 可直接登记身份证散客入住（管理层下单）
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
                <div id="rooms_all_info">
            <dl>
                <dt>
                    <label>
                        房间状态：
                        <select id="sStatus" @change="switchTable">
                            <option value="">全部</option>
                            <option value="empty">当前空余</option>
                            <option value="busy">已经入住</option>
                            <option value="fixing">正在维修</option>
                        </select>
                    </label>
                    <label>
                        楼层号：
                        <select id="sFloor" @change="switchTable">
                            <option value="">任意</option>
                            <option v-for="(room,index) in info.rooms" :value="index" v-cloak>{{index}}
                            </option>
                        </select>
                    </label>
                    <label>
                        房间类型：
                        <select id="sType" @change="switchTable">
                            <option value="">任意</option>
                            <option v-for="type in info.types" :value="type" v-cloak>{{type}}</option>
                        </select>
                    </label>
                    <label>
                        房间朝向：
                        <select id="sDir" @change="switchTable">
                            <option value="">任意</option>
                            <option v-for="direction in info.directions" :value="direction" v-cloak>{{direction}}
                            </option>
                        </select>
                    </label>
                </dt>
                <dd>
                    <table>
                        <tr>
                            <td >楼层-房号</td>
                            <td>朝向</td>
                            <td>房间类型</td>
                            <td>特色</td>
                            <td>价格</td>
                            <td>房间状态</td>
                            <td><!--预定--></td>
                            <td><!--报修--></td>
                        </tr>
                        <tr v-for="(room,index) in rooms" v-cloak>
                            <td>{{room.roomNumber.floor}}-{{room.roomNumber.number}}</td>
                            <td :title="room.direction.description">{{room.direction.type}}</td>
                            <td :title="room.type.description">{{room.type.type}}</td>
                            <td>{{room.specialty}}</td>
                            <td>{{room.price}}</td>
                            <td>{{status(room.broken,room.occupied)}}</td>
                            <td>
                                <div v-if="!room.broken && !room.occupied" :index="index" class="btn btn-default"
                                     @click="askBook">下单
                                </div>
                                <div v-else :index="index" class="btn btn-default disabled">下单</div>
                            </td>
                            <td>
                                <div v-if="!room.broken" :index="index" class="btn btn-default" @click="askFix">报修</div>
                                <div v-else class="btn btn-default disabled">维修中</div>
                            </td>
                        </tr>
                    </table>
                </dd>
            </dl>
            <div class="bookWindow untouchable">
                <div class="content">
                    <button type="button" class="close" @click="close" aria-label="Close">
                        <span id="closeContent" aria-hidden="true">&times;</span>
                    </button>
                    <div>请选择预定类型</div>
                    <div class="btn btn-default" @click="userBook">登记客户预定</div>
                    <div class="btn btn-default" @click="tourBook">散客入住</div>
                </div>
                <div class="bookList userBookList">
                    <button type="button" class="close" @click="close" aria-label="Close">
                        <span id="closeUserBookList" aria-hidden="true">&times;</span>
                    </button>
                    <dl>
                        <dt>入住时间：</dt>
                        <dd>
                            <input id="orderStart" type="date">
                            ~
                            <input id="orderEnd" type="date">
                        </dd>
                    </dl>
                    <dl>
                        <dt>入住人：</dt>
                        <dd>
                            <table>
                                <tr>
                                    <td>姓名</td>
                                    <td>身份证号</td>
                                    <td></td>
                                </tr>
                                <tr v-for="(tour,index) in tours" v-cloak="">
                                    <td>{{tour.name}}</td>
                                    <td>{{tour.id}}</td>
                                    <td>
                                        <div class="btn btn-default" :index="index" @click="delTour">删除</div>
                                    </td>
                                </tr>
                                <tr>
                                    <td><input id="newTourName" type="text"></td>
                                    <td><input id="newTourID" type="text"></td>
                                    <td>
                                        <div class="btn btn-default" @click="addTour">添加</div>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="3">
                                        <div class="btn btn-default" @click="submitBook">提交订单</div>
                                    </td>
                                </tr>
                            </table>
                        </dd>
                    </dl>
                </div>
            </div>
        </div>
            `;
            //生成html
            render_Container(resStr);
            //script
            const app = new Vue({
                el: '#rooms_all_info',
                data: {
                    allRooms: data.sort((a, b) => {
                        return a.roomNumber.floor - b.roomNumber.floor
                    }),
                    rooms: data.sort((a, b) => {
                        return a.roomNumber.floor - b.roomNumber.floor
                    }),
                    info: {},
                    tours: [],
                    curFloor: -1,
                    curNumber: -1
                },
                methods: {
                    switchTable: function () {
                        startCatLoading();
                        let sStatus = $('#sStatus').val();
                        let sFloor = $('#sFloor').val();
                        let sDir = $('#sDir').val();
                        let sType = $('#sType').val();
                        let res = this.allRooms;
                        if (!isEmpty(sStatus)) {
                            res = res.filter((room) => {
                                switch (sStatus) {
                                    case 'fixing':
                                        return room.broken;
                                    case 'empty':
                                        return !room.occupied;
                                    case 'busy':
                                        return room.occupied;
                                }
                            })
                        }
                        if (!isEmpty(sFloor)) {
                            res = res.filter((room) => {
                                return room.roomNumber.floor === +sFloor;
                            })
                        }
                        if (!isEmpty(sType)) {
                            res = res.filter((room) => {
                                return room.type.type === sType;
                            })
                        }
                        if (!isEmpty(sDir)) {
                            res = res.filter((room) => {
                                return room.direction.type === sDir;
                            })
                        }
                        this.rooms = res;
                        stopCatLoading();
                    },
                    status: function (broken, occupied) {
                        if (broken) {
                            return '正在维修';
                        } else if (occupied) {
                            return '用户在住';
                        } else {
                            return '空闲';
                        }
                    },
                    askFix: function (e) {
                        let index = +$(e.target).attr('index');
                        let fixFloor = app.rooms[index].roomNumber.floor;
                        let fixNumber = app.rooms[index].roomNumber.number;
                        // let line = $(`.orders table tr:nth-child(${index + 2})`);
                        if (!confirm(`确定报修${fixFloor}楼${fixNumber}号房间？`)) return;
                        startCatLoading();
                        $.ajax({
                            url: `${serverHost}/api/rooms?floor=${fixFloor}&number=${fixNumber}`,
                            type: 'PATCH',
                            data: JSON.stringify({
                                broken: true
                            }),
                            contentType: "application/json; charset=UTF-8",
                            beforeSend: function (xhr) {
                                xhr.setRequestHeader("Authorization", "Basic " + btoa(sessionStorage.username + ":" + sessionStorage.password));
                            },
                            success: function (data, textStatus, jqXHR) {
                                let tmp = app.rooms[index];
                                tmp.broken = true;
                                app.rooms.splice(index, 1, tmp);
                            },
                            error: function (jqXHR, textStatus, errorThrown) {
                                let msg = '报修失败：';
                                switch (jqXHR.status) {
                                    default:
                                        msg += jqXHR.responseJSON && jqXHR.responseJSON.message || '网络错误';
                                }
                                showMsg(msg);
                            },
                            complete: function () {
                                //关闭动画？
                                stopCatLoading();
                            }
                        });
                    },
                    askBook: function (e) {
                        $('.bookWindow').fadeIn(333);
                        let index = +$(e.target).attr('index');
                        this.curFloor = app.rooms[index].roomNumber.floor;
                        this.curNumber = app.rooms[index].roomNumber.number;
                    },
                    userBook: function () {
                        $('.bookWindow .bookList').slideDown(333);
                        $('#orderStart').val(dateTimeISOFormat(new Date()).slice(0, 10))
                            .attr('disabled', false);
                    },
                    tourBook: function () {
                        $('.bookWindow .bookList').slideDown(333);
                        $('#orderStart').val(dateTimeISOFormat(new Date()).slice(0, 10))
                            .attr('disabled', true);
                    },
                    addTour: function () {
                        let newTourName = $('#newTourName');
                        let newTourID = $('#newTourID');
                        if (isEmpty(newTourName.val(), newTourID.val())) {
                            showMsg('请完整填写信息');
                            return;
                        }
                        this.tours.push({
                            name: newTourName.val(),
                            id: newTourID.val(),
                        });
                        clearVal(newTourName, newTourID);
                    },
                    delTour: function (e) {
                        let index = +$(e.target).attr('index');
                        this.tours.splice(index, 1);
                    },
                    submitBook: function () {
                        let orderStart = $('#orderStart').val();
                        let orderEnd = $('#orderEnd').val();
                        if (isEmpty(orderStart, orderEnd)) {
                            showMsg('请正确填写预订时间');
                            return;
                        }
                        if (new Date(orderStart) >= new Date(orderEnd)) {
                            showMsg('离店时间必须晚于预定时间');
                            return;
                        }
                        if (isEmpty(this.tours)) {
                            showMsg('请至少添加一名旅客信息');
                            return;
                        }
                        if (isNegative(this.curFloor, this.curNumber)) {
                            showMsg('系统错误，请刷新页面重试');
                            return;
                        }
                        startCatLoading();
                        $.ajax({
                            url: `${serverHost}/api/transactions`,
                            type: 'POST',
                            data: JSON.stringify({
                                dateFrom: dateTimeISOFormat(new Date(orderStart)),
                                dateTo: dateTimeISOFormat(new Date(orderEnd)),
                                phone: sessionStorage.username,
                                guests: app.tours.map((tour) => {
                                    return tour.id;
                                }),
                                room: {
                                    floor: app.curFloor,
                                    number: app.curNumber
                                }
                            }),
                            contentType: "application/json; charset=UTF-8",
                            success: function (data, textStatus, jqXHR) {
                                $('#closeUserBookList').trigger('click');
                                showMsg('下单成功！')
                            },
                            error: function (jqXHR, textStatus, errorThrown) {
                                let msg = '下单失败：';
                                switch (jqXHR.status) {
                                    case 409:
                                        msg += '订单时间冲突';
                                        break;
                                    default:
                                        msg += jqXHR.responseJSON && jqXHR.responseJSON.message || '网络错误';
                                }
                                showMsg(msg);
                            },
                            complete: function () {
                                stopCatLoading()
                            }
                        });
                    },
                    close: function (e) {
                        let button = $(e.target);
                        let thisWindow = button.parent().parent();
                        switch (button.attr('id')) {
                            case 'closeContent':
                                thisWindow.parent().fadeOut(333);
                                break;
                            case 'closeUserBookList':
                            case 'closeTourBookList':
                                thisWindow.slideUp(333);
                                thisWindow.parent().slideUp(333);
                                this.tours = [];
                                this.curFloor = -1;
                                this.curNumber = -1;
                                break;
                        }
                        thisWindow.find('.close span').trigger('click');
                    }
                }
            });
            constraintID($('#newTourID'));
            //更新筛选信息
            startCatLoading();
            $.ajax({
                url: `${serverHost}/api/rooms/load`,
                type: 'GET',
                dataType: 'json',
                contentType: "application/json; charset=UTF-8",
                success: function (data, textStatus, jqXHR) {
                    app.info = data;
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    let msg = '初始化筛选项失败：';
                    switch (jqXHR.status) {
                        default:
                            msg += jqXHR.responseJSON && jqXHR.responseJSON.message || '网络错误';
                    }
                    showMsg(msg);
                },
                complete: function () {
                    stopCatLoading();
                }
            });
        },
        error: function (jqXHR, textStatus, errorThrown) {
            let msg = '获取列表详情失败：';
            switch (jqXHR.status) {
                case 401:
                    msg += '密码已失效，请重新登陆';
                    logout();
                    break;
                default:
                    msg += jqXHR.responseJSON && jqXHR.responseJSON.message || '网络错误';
            }
            showMsg(msg);
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
* 可筛选（四态订单）
* 可根据身份证筛选
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
                <dt>
                    <label>
                        订单状态：
                        <select id="oStatus" @change="switchTable">
                            <option value="">全部</option>
                            <option value="10">等待入住</option>
                            <option value="00">已取消</option>
                            <option value="11">已入住</option>
                            <option value="01">已完成</option>
                        </select>
                    </label>
                    <label>
                        身份证号：
                        <input type="text" id="searchID" @keyup="switchTable" placeholder="支持正则搜索">
                    </label>
                </dt>
                <dd class="orders">
                    <table>
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
                            <td :title="'订单创建时间：'+timeFormat(order.createdDate)">{{order.id}}</td>
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
                                     @click="checkOut">客户退房
                                </div>
                                <div class="checkOut2 btn btn-default disabled"
                                     v-if="!order.activated && order.used">客户退房
                                </div>
                            </td>
                            <td><!--等待入住显示-->
                                <div class="cancelOrder btn btn-default" v-if="order.activated && !order.used"
                                     :index="index"
                                     @click="cancelBooking">取消订单
                                </div>
                                <div class="cancelOrder2 btn btn-default disabled" v-else :index="index">取消订单</div>
                            </td>
                        </tr>
                    </table>
                </dd>
                <div class="checkOutList">
                    <button type="button" class="close" @click="close" aria-label="Close">
                        <span id="closeCommentList" aria-hidden="true">&times;</span>
                    </button>
                    <h1>结账金额</h1>
                    <p><span>NaN</span>元</p>
                </div>
            </dl>
        </div>
            `;
            //生成html
            render_Container(resStr);
            //script
            let app = new Vue({
                el: '#check_all_booking',
                data: {
                    allOrders: data,
                    orders: data
                },
                methods: {
                    timeFormat: function (time) {
                        return new Date(time).toLocaleString();
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
                    switchTable: function () {
                        let oStatus = $('#oStatus').val();
                        let res = this.allOrders;
                        if (!isEmpty(oStatus)) {
                            res = res.filter((order) => {
                                switch (oStatus) {
                                    //activated used
                                    case '00':
                                        return !order.activated && !order.used;
                                    case '01':
                                        return !order.activated && order.used;
                                    case '10':
                                        return order.activated && !order.used;
                                    case '11':
                                        return order.activated && order.used;
                                }
                            });
                        }
                        let text = $('#searchID').val().toUpperCase();
                        res = res.filter((order) => {
                            for (let guest of order.guests) {
                                if (new RegExp(text).test(guest.identification)) {
                                    return true;
                                }
                            }
                            return false;
                        });
                        this.orders = res;
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
                                let tmp = app.orders[index];
                                tmp.activated = false;
                                app.orders.splice(index, 1, tmp);
                                showMsg('取消订单成功');
                            },
                            error: function (jqXHR, textStatus, errorThrown) {
                                let msg = '取消订单失败：';
                                switch (jqXHR.status) {
                                    default:
                                        msg += jqXHR.responseJSON && jqXHR.responseJSON.message || '网络错误';
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
                                let tmp = app.orders[index];
                                tmp.used = true;
                                app.orders.splice(index, 1, tmp);
                                showMsg('客户入住成功');
                            },
                            error: function (jqXHR, textStatus, errorThrown) {
                                let msg = '客户入住失败：';
                                switch (jqXHR.status) {
                                    default:
                                        msg += jqXHR.responseJSON && jqXHR.responseJSON.message || '网络错误';
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
                            url: `${serverHost}/api/transactions/checkout?bookId=${bookID}`,
                            type: 'POST',
                            dataType: 'json',
                            contentType: "application/json; charset=UTF-8",
                            beforeSend: function (xhr) {
                                xhr.setRequestHeader("Authorization", "Basic " + btoa(sessionStorage.username + ":" + sessionStorage.password));
                            },
                            success: function (data, textStatus, jqXHR) {
                                let tmp = app.orders[index];
                                tmp.activated = false;
                                app.orders.splice(index, 1, tmp);
                                $('.checkOutList').slideDown();
                                $('.checkOutList p span').text(data);
                                showMsg('客户退房成功');
                            },
                            error: function (jqXHR, textStatus, errorThrown) {
                                let msg = '客户退房失败：';
                                switch (jqXHR.status) {
                                    default:
                                        msg += jqXHR.responseJSON && jqXHR.responseJSON.message || '网络错误';
                                }
                                showMsg(msg);
                            },
                            complete: function () {
                                stopCatLoading();
                            }
                        });
                    },
                    close: function (e) {
                        let button = $(e.target);
                        let thisWindow = button.parent().parent();
                        thisWindow.slideUp();
                    }
                }
            })
        },
        error: function (jqXHR, textStatus, errorThrown) {
            let msg = '获取列表详情失败：';
            switch (jqXHR.status) {
                case 401:
                    msg += '密码已失效，请重新登陆';
                    logout();
                    break;
                default:
                    msg += jqXHR.responseJSON && jqXHR.responseJSON.message || '网络错误';
            }
            showMsg(msg);
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
                            <option value="true" :selected="room.broken == true">正在维修</option>
                            <option value="false" :selected="room.broken == false">无需维修</option>
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
                        <td><input type="number" min="0" max="99" id="newRFloor" title="开放新房间的楼层" placeholder="新楼层"></td>
                        <td><input type="number" min="0" max="99" id="newRNum" title="开放新房间的房号" placeholder="新房号"></td>
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
                            <option value="true">正在维修</option>
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
                    rooms: data.rooms.sort((a, b) => {
                        return a.roomNumber.floor - b.roomNumber.floor
                    })
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
                                clearVal($('#newType'), $('#newTypeDes'));
                                showMsg('添加成功！')
                            },
                            error: function (jqXHR, textStatus, errorThrown) {
                                let msg = '添加失败：';
                                switch (jqXHR.status) {
                                    case 400:
                                        msg += '格式错误';
                                        break;
                                    case 409:
                                        msg += '已存在该类型';
                                        break;
                                    default:
                                        msg += jqXHR.responseJSON && jqXHR.responseJSON.message || '网络错误';
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
                                let msg = '修改失败：';
                                switch (jqXHR.status) {
                                    case 500:
                                        msg += '有房间使用了该类型参数，请先删除对应房间';
                                        break;
                                    default:
                                        msg += jqXHR.responseJSON && jqXHR.responseJSON.message || '网络错误';
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
                                let msg = '删除失败：';
                                switch (jqXHR.status) {
                                    default:
                                        msg += jqXHR.responseJSON && jqXHR.responseJSON.message || '网络错误';
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
                                clearVal($('#newDir'), $('#newDirDes'));
                                showMsg('添加成功！');
                            },
                            error: function (jqXHR, textStatus, errorThrown) {
                                let msg = '添加失败：';
                                switch (jqXHR.status) {
                                    case 409:
                                        msg += '已存在该方位';
                                        break;
                                    default:
                                        msg += jqXHR.responseJSON && jqXHR.responseJSON.message || '网络错误';
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
                                let msg = '修改失败：';
                                switch (jqXHR.status) {
                                    case 500:
                                        msg += '有房间使用了该方位参数，请先删除对应房间';
                                        break;
                                    default:
                                        msg += jqXHR.responseJSON && jqXHR.responseJSON.message || '网络错误';
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
                                let msg = '删除失败：';
                                switch (jqXHR.status) {
                                    default:
                                        msg += jqXHR.responseJSON && jqXHR.responseJSON.message || '网络错误';
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
                        let newRDir = $('#newRDir').val();
                        let newRType = $('#newRType').val();
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
                                },
                                broken: false
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
                                clearVal($('#newRFloor'), $('#newRNum'), $('#newRSpecial'), $('#newRPrice'));
                                showMsg('添加成功！')
                            },
                            error: function (jqXHR, textStatus, errorThrown) {
                                let msg = '添加失败：';
                                switch (jqXHR.status) {
                                    case 409:
                                        msg += '已存在该房间';
                                        break;
                                    default:
                                        msg += jqXHR.responseJSON && jqXHR.responseJSON.message || '网络错误';
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
                        let modRDir = line.find('.modRDir option:selected').text().trim();
                        let modRType = line.find('.modRType option:selected').text().trim();
                        let modRSpe = line.find('.modRSpe').val();
                        let modRpri = line.find('.modRpri').val();
                        let modRBro = line.find('.modRBro option:selected').val() === 'true';
                        if (isEmpty(modRpri)) {
                            showMsg('请正确填写信息');
                            return;
                        }
                        if (isNegative(modRpri)) {
                            showMsg('价格不能为负数');
                            return;
                        }
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
                                showMsg('修改成功！')
                            },
                            error: function (jqXHR, textStatus, errorThrown) {
                                let msg = '修改失败：';
                                switch (jqXHR.status) {
                                    case 500:
                                        msg += '该房间存在订单，禁止删除';
                                        break;
                                    default:
                                        msg += jqXHR.responseJSON && jqXHR.responseJSON.message || '网络错误';
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
                                let msg = '删除失败：';
                                switch (jqXHR.status) {
                                    default:
                                        msg += jqXHR.responseJSON && jqXHR.responseJSON.message || '网络错误';
                                }
                                showMsg(msg);
                            },
                            complete: function () {
                                stopCatLoading();
                            }
                        });
                    },
                    broken: function (isBroken) {
                        return isBroken ? '正在维修' : '无需维修';
                    }
                }
            })
        },
        error: function (jqXHR, textStatus, errorThrown) {
            let msg = '获取列表详情失败：';
            switch (jqXHR.status) {
                case 401:
                    msg += '密码已失效，请重新登陆';
                    logout();
                    break;
                default:
                    msg += jqXHR.responseJSON && jqXHR.responseJSON.message || '网络错误';
            }
            showMsg(msg);
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
                                <td>密码</td>
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
            let app = new Vue({
                el: '#modify_user_info',
                data: {
                    users: data
                },
                methods: {
                    addAcc: function () {
                        let username = $('#newAccName').val();
                        let nickname = $('#newAccNick').val();
                        let password = $('#newAccPWD').val();
                        let passwordAgain = $('#newAccPWDR').val();
                        let role = $('#newAccRole');
                        if (isEmpty(username, passwordAgain, password, passwordAgain)) {
                            showMsg('请完整填写注册信息');
                        }
                        if (password !== passwordAgain) {
                            showMsg('两次输入密码不相符');
                            return;
                        }
                        startCatLoading();
                        $.ajax({
                            url: `${serverHost}/api/users/make${role.val()}`,
                            type: 'POST',
                            contentType: "application/json; charset=UTF-8",
                            data: JSON.stringify({
                                phoneNumber: username,
                                password: sha256(password),
                                nickname: nickname
                            }),
                            success: function (data, textStatus, jqXHR) {
                                app.users.push({
                                    id: -1,
                                    phoneNumber: username,
                                    nickname: nickname,
                                    password: sha256(password),
                                    register_date: new Date().toISOString().slice(0, 22),
                                    role: role.children('option:selected').text(),
                                    guests: []
                                });
                                clearVal($('#newAccName'), $('#newAccNick'), $('#newAccPWD'), $('#newAccPWDR'));
                                showMsg(`添加 [${role.val()}]${nickname} 成功`)
                            },
                            error: function (jqXHR, textStatus, errorThrown) {
                                let msg = '添加失败：';
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
                            data: JSON.stringify({
                                password: password.length > 0 ? sha256(password) : undefined,
                                nickname: nickname,
                                role: role
                            }),
                            contentType: "application/json; charset=UTF-8",
                            beforeSend: function (xhr) {
                                xhr.setRequestHeader("Authorization", "Basic " + btoa(sessionStorage.username + ":" + sessionStorage.password));
                            },
                            success: function (data, textStatus, jqXHR) {
                                showMsg('修改成功');
                            },
                            error: function (jqXHR, textStatus, errorThrown) {
                                let msg = '修改失败：';
                                switch (jqXHR.status) {
                                    default:
                                        msg += jqXHR.responseJSON && jqXHR.responseJSON.message || '网络错误';
                                }
                                showMsg(msg);
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
                                let msg = '删除失败：';
                                switch (jqXHR.status) {
                                    default:
                                        msg += jqXHR.responseJSON && jqXHR.responseJSON.message || '网络错误';
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
            constraintTel($("#newAccName"));
        },
        error: function (jqXHR, textStatus, errorThrown) {
            let msg = '获取列表详情失败：';
            switch (jqXHR.status) {
                case 401:
                    msg += '密码已失效，请重新登陆';
                    logout();
                    break;
                default:
                    msg += jqXHR.responseJSON && jqXHR.responseJSON.message || '网络错误';
            }
            showMsg(msg);
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
            let msg = '获取列表详情失败：';
            switch (jqXHR.status) {
                case 401:
                    msg += '密码已失效，请重新登陆';
                    logout();
                    break;
                default:
                    msg += jqXHR.responseJSON && jqXHR.responseJSON.message || '网络错误';
            }
            showMsg(msg);
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
            let msg = '获取列表详情失败：';
            switch (jqXHR.status) {
                case 401:
                    msg += '密码已失效，请重新登陆';
                    logout();
                    break;
                default:
                    msg += jqXHR.responseJSON && jqXHR.responseJSON.message || '网络错误';
            }
            showMsg(msg);
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
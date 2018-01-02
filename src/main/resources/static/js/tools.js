//服务器地址
/*
* Ali-2G:       'http://47.100.117.174:8899'
* enihsyou-PC   'https://enihsyou.synology.me:8899'
* Production    'https://astolfo.20001.me'
* Default       'http://localhost:10080'
*
* */
let serverHost = 'https://astolfo.20001.me';

//封装消息提示
function showMsg(msg) {
    return new Promise(async (resolve, reject) => {
        let newMsg = $(`<div class="msg untouchable"><span>${msg}</span></div>`);
        $('.main').after(newMsg);
        newMsg.slideDown(333);
        newMsg.css('z-index', 9999 - msg.length);
        await sleep(Math.max(1000, msg.length * 150));
        newMsg.slideUp(333);
        await sleep(400);
        newMsg.remove();
        resolve();
    })
}

//判空
function isEmpty(a1, a2, ...an) {
    for (let i of arguments) {
        if (i == null || i.length === 0) return true;
    }
    return false;
}

//判负
function isNegative(a1, a2, ...an) {
    for (let i of arguments) {
        if (+i < 0) return true;
    }
    return false;
}

//传入时间转化为日期形式的ISO不带Z格式
function dateTimeISOFormat(time) {
    if (!time instanceof Date) {
        console.error(`dateTimeISOFormat:传入值(${time})类型不正确，已更正为当前时间！`);
        time = new Date();
    }
    let year = time.getFullYear();
    let month = time.getMonth() > 8 ? time.getMonth() + 1 : '0' + (time.getMonth() + 1);
    let day = time.getDate() > 9 ? time.getDate() : '0' + time.getDate();
    return `${year}-${month}-${day}T00:00:00.000`
}

//清空jqObj.val()
function clearVal(jqObj1, jqObj2, ...jqObjN) {
    for (let jqObj of arguments) {
        if (jqObj instanceof $) jqObj.val('');
        else {
            console.error(`clearVal:${jqObj}不是jqObj类型`);
        }
    }
}

//mask约束规则
$.mask.definitions['X'] = '[0-9Xx]';

//身份证约束
function constraintID(jqObj1, jqObj2, ...jqObjN) {
    for (let jqObj of arguments) {
        if (jqObj instanceof $) jqObj.mask("99999999999999999X",{placeholder:""});
        else {
            console.error(`constraintID:${jqObj}不是jqObj类型`);
        }
    }
}

//手机号约束
function constraintTel(jqObj1, jqObj2, ...jqObjN) {
    for (let jqObj of arguments) {
        if (jqObj instanceof $) jqObj.mask("99999999999",{placeholder:""});
        else {
            console.error(`constraintTel:${jqObj}不是jqObj类型`);
        }
    }
}

//延时函数
function sleep(time) {
    return new Promise((resolve, reject) => {
        setTimeout(resolve, time);
    });
}

//标题滚动
async function titleScroller() {
    let index = 0;
    do {
        await sleep(250);
        document.title = document.title.substr(1) + document.title.substr(0, 1);
    } while (++index < document.title.length);
    setTimeout(titleScroller, 2000);
}

//请求登录的数据(promise)
function reqLogin(username, password) {
    return new Promise((resolve, reject) => {
        if (username == null || password == null) {
            reject('EMPTY_USERNAME_OR_PASSWORD');
            return
        }
        $.ajax({
            url: `${serverHost}/api/users/login`,
            type: 'POST',
            data: JSON.stringify({
                phoneNumber: username,
                password: password
            }),
            dataType: 'json',
            contentType: "application/json; charset=UTF-8",
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Authorization", "Basic " + btoa(username + ":" + password));
            },
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
                resolve(data)
            },
            error: function (jqXHR, textStatus, errorThrown) {
                sessionStorage.isLogin = false;
                switch (jqXHR.status) {
                    //密码错误则清除本地缓存
                    case 400:
                        sessionStorage.clear();
                        localStorage.clear();
                        reject('WRONG_PASSWORD');
                        break;
                    //用户不存在则清除本地缓存
                    case 404:
                        sessionStorage.clear();
                        localStorage.clear();
                        reject('USERNAME_NOT_EXIST');
                        break;
                    default:
                        reject('BAD_NETWORK');
                }
            },
            complete: function () {
            }
        });
    });
}

//开始猫滚
function startCatLoading(time) {
    let loading = $('.cat-loading');
    loading.fadeIn(time || 200);
}

//停止猫滚
function stopCatLoading(time) {
    let loading = $('.cat-loading');
    loading.fadeOut(time || 500);
}

//啪嗒猫滚
function toggleCatLoading() {
    if ($('.cat-loading').length > 0) stopCatLoading();
    else startCatLoading();
}


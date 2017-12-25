//服务器地址
let serverHost = 'http://47.100.117.174:8899';

//封装消息提示
async function showMsg(msg) {
    let newMsg = $(`<div class="msg untouchable"><span>${msg}</span></div>`);
    $('.main').after(newMsg);
    newMsg.slideDown(400);
    await sleep(Math.max(1000, msg.length * 100));
    newMsg.slideUp(300);
    await sleep(400);
    newMsg.remove();
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
                reject(jqXHR.status)
            },
            complete: function () {
            }
        });
    });
}

//用于处理Cookie
let Cookies = {
    getItem: function (sKey) {
        return decodeURIComponent(document.cookie.replace(new RegExp("(?:(?:^|.*;)\\s*" + encodeURIComponent(sKey).replace(/[\-.+*]/g, "\\$&") + "\\s*\\=\\s*([^;]*).*$)|^.*$"), "$1")) || null;
    },
    setItem: function (sKey, sValue, vEnd, sPath, sDomain, bSecure) {
        if (!sKey || /^(?:expires|max-age|path|domain|secure)$/i.test(sKey)) {
            return false;
        }
        let sExpires = "";
        if (vEnd) {
            switch (vEnd.constructor) {
                case Number:
                    sExpires = vEnd === Infinity ? "; expires=Fri, 31 Dec 9999 23:59:59 GMT" : "; max-age=" + vEnd;
                    break;
                case String:
                    sExpires = "; expires=" + vEnd;
                    break;
                case Date:
                    sExpires = "; expires=" + vEnd.toUTCString();
                    break;
            }
        }
        document.cookie = encodeURIComponent(sKey) + "=" + encodeURIComponent(sValue) + sExpires + (sDomain ? "; domain=" + sDomain : "") + (sPath ? "; path=" + sPath : "") + (bSecure ? "; secure" : "");
        return true;
    },
    removeItem: function (sKey, sPath, sDomain) {
        if (!sKey || !this.hasItem(sKey)) {
            return false;
        }
        document.cookie = encodeURIComponent(sKey) + "=; expires=Thu, 01 Jan 1970 00:00:00 GMT" + (sDomain ? "; domain=" + sDomain : "") + (sPath ? "; path=" + sPath : "");
        return true;
    },
    hasItem: function (sKey) {
        return (new RegExp("(?:^|;\\s*)" + encodeURIComponent(sKey).replace(/[\-.+*]/g, "\\$&") + "\\s*\\=")).test(document.cookie);
    },
    keys: /* optional method: you can safely remove it! */ function () {
        let aKeys = document.cookie.replace(/((?:^|\s*;)[^=]+)(?=;|$)|^\s*|\s*(?:=[^;]*)?(?:\1|$)/g, "").split(/\s*(?:=[^;]*)?;\s*/);
        for (let nIdx = 0; nIdx < aKeys.length; nIdx++) {
            aKeys[nIdx] = decodeURIComponent(aKeys[nIdx]);
        }
        return aKeys;
    }
};

//开始猫滚
async function startCatLoading() {
    if ($('.cat-loading').length > 0) return;
    let loading = $(`<div class="cat-loading untouchable"></div>`);
    $('.main').after(loading);
    loading.fadeIn(500);
}

//停止猫滚
async function stopCatLoading() {
    let loading = $('.cat-loading');
    loading.fadeOut(500);
    await sleep(800);
    loading.remove();
}

//啪嗒猫滚
function toggleCatLoading() {
    if ($('.cat-loading').length > 0) stopCatLoading();
    else startCatLoading();
}
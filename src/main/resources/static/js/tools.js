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
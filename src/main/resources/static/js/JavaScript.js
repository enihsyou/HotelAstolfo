function sleep(time) {
    return new Promise((resolve, reject) => {
        setTimeout(resolve, time);
    });
}

async function titleScroller() {
    let index = 0;
    do {
        await sleep(250);
        document.title = document.title.substr(1)+document.title.substr(0,1);
    } while (++index < document.title.length);
    setTimeout(titleScroller, 2000);
}

titleScroller();

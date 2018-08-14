/**
 * 初始化通知详情对话框
 */
var GoodsPriceInfoDlg = {
    noticeInfoData: {},
    editor: null,
    validateFields: {
        
    }
};

/**
 * 清除数据
 */
GoodsPriceInfoDlg.clearData = function () {
    this.noticeInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
GoodsPriceInfoDlg.set = function (key, val) {
    this.noticeInfoData[key] = (typeof value == "undefined") ? $("#" + key).val() : value;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
GoodsPriceInfoDlg.get = function (key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
GoodsPriceInfoDlg.close = function () {
    parent.layer.close(window.parent.Goods.layerIndex);
}

/**
 * 收集数据
 */
GoodsPriceInfoDlg.collectData = function () {
    this.set('minPrice').set('maxPrice').set('goodsId').set('shopId');
}


/**
 * 提交修改
 */
GoodsPriceInfoDlg.editSubmit = function () {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/goods/saveShowPrice", function (data) {
        Feng.success("配置成功!");
        GoodsPriceInfoDlg.close();
    }, function (data) {
        Feng.error("配置失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.noticeInfoData);
    ajax.set("shopIds",$("#shopIds").val());
    ajax.start();
}

GoodsPriceInfoDlg.selectShop = function () {
    var index = layer.open({
        type: 2,
        title: '选择门店',
        area: ['300px', '450px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/goods/shop_select'
    });
    this.layerIndex = index;
};

$(function () {

});

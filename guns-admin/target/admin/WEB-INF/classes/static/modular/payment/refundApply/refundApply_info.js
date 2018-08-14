/**
 * 初始化部门详情对话框
 */
var RefundApplyInfoDlg = {
	refundApplyInfoData : {},
    zTreeInstance : null,
    validateFields: {
        
    }
};

/**
 * 清除数据
 */
RefundApplyInfoDlg.clearData = function() {
    this.refundApplyInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
RefundApplyInfoDlg.set = function(key, val) {
    this.refundApplyInfoData[key] = (typeof value == "undefined") ? $("#" + key).val() : value;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
RefundApplyInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
RefundApplyInfoDlg.close = function() {
    parent.layer.close(window.parent.RefundApply.layerIndex);
}



/**
 * 收集数据
 */
RefundApplyInfoDlg.collectData = function() {
    this.set('refundApplyId').set('money');
}


RefundApplyInfoDlg.refundSubmit = function (){
    var ajax = new $ax(Feng.ctxPath + "/refundApply/refund", function (data) {
        Feng.success("添加成功!");
        window.parent.RefundApply.table.refresh();
        RefundApplyInfoDlg.close();
    }, function (data) {
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set("refundApplyId",this.refundApplyInfoData.refundApplyId);
    ajax.set("money",this.refundApplyInfoData.money);
    ajax.start();
} 

$(function() {
    
});

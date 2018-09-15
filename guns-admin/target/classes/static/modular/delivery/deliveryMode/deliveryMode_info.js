/**
 * 初始化配送方式详情对话框
 */
var DeliveryModeInfoDlg = {
    adsPositionInfoData : {},
    validateFields: {
        price: {
            validators: {
                notEmpty: {
                    message: '价格不能为空'
                }
            }
        }
    }
};

/**
 * 验证数据是否为空
 */
DeliveryModeInfoDlg.validate = function () {
    $('#deliveryModeInfoForm').data("bootstrapValidator").resetForm();
    $('#deliveryModeInfoForm').bootstrapValidator('validate');
    return $("#deliveryModeInfoForm").data('bootstrapValidator').isValid();
}

/**
 * 清除数据
 */
DeliveryModeInfoDlg.clearData = function() {
    this.adsPositionInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
DeliveryModeInfoDlg.set = function(key, val) {
    this.adsPositionInfoData[key] = (typeof value == "undefined") ? $("#" + key).val() : value;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
DeliveryModeInfoDlg.get = function(key) {
    return $("#" + key).val();
}


/**
 * 收集数据
 */
DeliveryModeInfoDlg.collectData = function() {
    this.set('id').set('name').set('remark').set('sequence').set('price');
}

/**
 * 添加提交
 */
DeliveryModeInfoDlg.addSubmit = function () {
	this.clearData();
	this.collectData();
    if (!this.validate()) {
        return;
    }
    var ajax = new $ax(Feng.ctxPath + "/deliveryMode/add", function (data) {
        Feng.success("添加成功!");
        window.parent.DeliveryMode.table.refresh();
        DeliveryModeInfoDlg.close();
    }, function (data) {
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.adsPositionInfoData);
    ajax.start();
}

/**
 * 修改提交
 */
DeliveryModeInfoDlg.editSubmit = function () {
	this.clearData();
	this.collectData();
    if (!this.validate()) {
        return;
    }
    var ajax = new $ax(Feng.ctxPath + "/deliveryMode/edit", function (data) {
        Feng.success("修改成功!");
        window.parent.DeliveryMode.table.refresh();
        DeliveryModeInfoDlg.close();
    }, function (data) {
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.adsPositionInfoData);
    ajax.start();
}



/**
 * 关闭此对话框
 */
DeliveryModeInfoDlg.close = function () {
	parent.layer.close(window.parent.DeliveryMode.layerIndex);
}

$(function() {
    Feng.initValidator("deliveryModeInfoForm", DeliveryModeInfoDlg.validateFields);
});


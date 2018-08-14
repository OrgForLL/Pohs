/**
 * 初始化
 */
var ShareGoodsInfoDlg = {
    shareGoodsInfoData : {},
    validateFields: {
        
    }
};

/**
 * 清除数据
 */
ShareGoodsInfoDlg.clearData = function() {
    this.shareGoodsInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ShareGoodsInfoDlg.set = function(key, val) {
    this.shareGoodsInfoData[key] = (typeof value == "undefined") ? $("#" + key).val() : value;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ShareGoodsInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
ShareGoodsInfoDlg.close = function() {
    parent.layer.close(window.parent.ShareGoods.layerIndex);
}


/**
 * 收集数据
 */
ShareGoodsInfoDlg.collectData = function() {
    this.set('title').set('content').set('profit').set('imgurl').set('associatedEntityId').set('isDefault').set('type');
}



/**
 * 提交
 */
ShareGoodsInfoDlg.addSubmit = function() {
	ShareGoodsInfoDlg.radioCheck();
    this.clearData();
    this.collectData();
    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/share/addShareConfigure", function(data){
        Feng.success("修改成功!");
        ShareGoodsInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.shareGoodsInfoData);
    ajax.start();
}

/**
 * 提交
 */
ShareGoodsInfoDlg.addSubmitDefault = function() {
	ShareGoodsInfoDlg.radioCheck();
	this.clearData();
	this.collectData();
	//提交信息
	var ajax = new $ax(Feng.ctxPath + "/share/addDefaultShareDefaultConfigure", function(data){
		Feng.success("修改成功!");
		ShareGoodsInfoDlg.close();
	},function(data){
		Feng.error("修改失败!" + data.responseJSON.message + "!");
	});
	ajax.set(this.shareGoodsInfoData);
	ajax.start();
}

/**
 * radio点击事件
 */
ShareGoodsInfoDlg.radioCheck = function () {
	$("input[name='radio1']").each(
		function(){
			if($(this).prop('checked')){
				$('#isDefault').val($(this).val());
			}
		}
	);
}



$(function() {
    //分享图片上传
    var avatarUp = new $WebUpload("imgurl","/share/upload");
    avatarUp.init();
    
});

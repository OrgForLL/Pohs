/**
 * 客户初始化
 */
var SharePayApply = {
	id: "SharePayApplyTable",	//表格id
	seItem: null,		//选中的条目
	table: null,
	layerIndex: -1
};

/**
 * 初始化表格的列
 */
SharePayApply.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: '昵称', field: 'name', align: 'center', valign: 'middle', sortable: true},
        {title: '账户号', field: 'phone', align: 'center', valign: 'middle', sortable: true},
        {title: '提现金额', field: 'amount', align: 'center', valign: 'middle', sortable: true},
        {title: '提现账户信息', field: 'remark', align: 'center', valign: 'middle', sortable: true},
        {title: '申请日期', field: 'createTime', align: 'center', valign: 'middle', sortable: true},
        {title: '操作', field: 'num', align: 'center', valign: 'middle', sortable: true,formatter : operateFormatter}]
};

function operateFormatter(value, row, index) {
	if(row.status == 0){
		return '<button id="refundBtn" onClick="SharePayApply.agreeClick(\''+row.id+'\')" icon="fa-check" type="button" class="btn btn-primary btn-xs">通过</button>'
		+'&nbsp;&nbsp;&nbsp;&nbsp;<button id="refundBtn" onClick="SharePayApply.unAgreeClick(\''+row.id+'\')" icon="fa-check" type="button" class="btn btn-primary btn-xs">不通过</button>';
	}
};

SharePayApply.agreeClick = function (id){
    var ajax = new $ax(Feng.ctxPath + "/share/agreeWithPay", function () {
        Feng.success("操作成功!");
        SharePayApply.table.refresh();
    }, function (data) {
        Feng.error("操作失败!" + data.responseJSON.message + "!");
    });
    ajax.set("payApplyId", id);
    ajax.set("status", 1);
    ajax.start();
}

SharePayApply.unAgreeClick = function (id){
	var ajax = new $ax(Feng.ctxPath + "/share/agreeWithPay", function () {
		Feng.success("操作成功!");
		SharePayApply.table.refresh();
	}, function (data) {
		Feng.error("操作失败!" + data.responseJSON.message + "!");
	});
	ajax.set("payApplyId", id);
    ajax.set("status", 2);
	ajax.start();
}


/**
 * 是否选中
 */
SharePayApply.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
    	SharePayApply.seItem = selected[0];
        return true;
    }
};

/**
 * 查询客户列表
 */
SharePayApply.search = function () {
    var queryData = {};
    queryData['status'] = $("#status").val();
    SharePayApply.table.refresh({query: queryData});
};
$(function () {
    var defaultColunms = SharePayApply.initColumn();
    var table = new BSTable(SharePayApply.id, "/share/payApplyList", defaultColunms);
    table.setPaginationType("client");
    table.init();
    SharePayApply.table = table;
});

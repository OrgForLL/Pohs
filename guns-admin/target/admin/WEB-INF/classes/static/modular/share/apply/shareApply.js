/**
 * 客户初始化
 */
var ShareApply = {
	id: "ShareApplyTable",	//表格id
	seItem: null,		//选中的条目
	table: null,
	layerIndex: -1
};

/**
 * 初始化表格的列
 */
ShareApply.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: '用户名', field: 'realName', align: 'center', valign: 'middle', sortable: true},
        {title: '申请门店', field: 'shopNames', align: 'center', valign: 'middle', sortable: true},
        {title: '状态', field: 'statusName', align: 'center', valign: 'middle', sortable: true},
        {title: '操作', field: 'num', align: 'center', valign: 'middle', sortable: true,formatter : operateFormatter}]
};

function operateFormatter(value, row, index) {
	if(row.status == 3){
		return '<button id="refundBtn" onClick="ShareApply.agreeClick(\''+row.id+'\')" icon="fa-check" type="button" class="btn btn-primary btn-xs">通过</button>'
		+'&nbsp;&nbsp;&nbsp;&nbsp;<button id="refundBtn" onClick="ShareApply.unAgreeClick(\''+row.id+'\')" icon="fa-check" type="button" class="btn btn-primary btn-xs">不通过</button>';
	}
};

ShareApply.agreeClick = function (id){
    var ajax = new $ax(Feng.ctxPath + "/share/agreeWithShareUser", function () {
        Feng.success("操作成功!");
        ShareApply.table.refresh();
    }, function (data) {
        Feng.error("操作失败!" + data.responseJSON.message + "!");
    });
    ajax.set("shareUserId", id);
    ajax.set("status", 4);
    ajax.start();
}

ShareApply.unAgreeClick = function (id){
	var ajax = new $ax(Feng.ctxPath + "/share/agreeWithShareUser", function () {
		Feng.success("操作成功!");
		ShareApply.table.refresh();
	}, function (data) {
		Feng.error("操作失败!" + data.responseJSON.message + "!");
	});
	ajax.set("shareUserId", id);
    ajax.set("status", 2);
	ajax.start();
}

/**
 * 查询客户列表
 */
ShareApply.search = function () {
    var queryData = {};
    queryData['status'] = $("#status").val();
    ShareApply.table.refresh({query: queryData});
};

/**
 * 是否选中
 */
ShareApply.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
    	ShareApply.seItem = selected[0];
        return true;
    }
};

$(function () {
    var defaultColunms = ShareApply.initColumn();
    var table = new BSTable(ShareApply.id, "/share/applyList", defaultColunms);
    table.setPaginationType("client");
    table.init();
    ShareApply.table = table;
});

/**
 * 客户初始化
 */
var RefundApply = {
	id: "RefundApplyTable",	//表格id
	seItem: null,		//选中的条目
	table: null,
	layerIndex: -1
};

/**
 * 初始化表格的列
 */
RefundApply.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: '订单编号', field: 'orderSn', align: 'center', valign: 'middle', sortable: true},
        {title: '客户帐号', field: 'memberPhone', align: 'center', valign: 'middle', sortable: true},
        {title: '退款原因', field: 'applyWhy', align: 'center', valign: 'middle', sortable: true},
        {title: '退货物流单号', field: 'logisticsMsg', align: 'center', valign: 'middle', sortable: true},
        {title: '申请时间', field: 'createTime', align: 'center', valign: 'middle', sortable: true},
        {title: '退款类型', field: 'refundTypeMsg', align: 'center', valign: 'middle', sortable: true},
        {title: '订单状态', field: 'statusName', align: 'center', valign: 'middle', sortable: true},
        {title: '操作', field: 'num', align: 'center', valign: 'middle', sortable: true,formatter : operateFormatter}];
};

function operateFormatter(value, row, index) {
	return '<button id="refundBtn" onClick="RefundApply.openOrderDetail(\''+row.id+'\')" icon="fa-check" type="button" class="btn btn-primary btn-xs">订单详情</button>';
};

RefundApply.openOrderDetail = function (id) {
	var index = layer.open({
        type: 2,
        title: '订单详情',
        area: ['800px', '450px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/refundApply/detail/' + id
    });
    layer.full(index);
    this.layerIndex = index;
}

/**
 * 查询客户列表
 */
RefundApply.search = function () {
    var queryData = {};
    queryData['name'] = $("#name").val();
    queryData['phoneNum'] = $("#phoneNum").val();
    RefundApply.table.refresh({query: queryData});
};

/**
 * 是否选中
 */
RefundApply.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
    	RefundApply.seItem = selected[0];
        return true;
    }
};

$(function () {
    var defaultColunms = RefundApply.initColumn();
    var table = new BSTable(RefundApply.id, "/refundApply/list", defaultColunms);
    table.setPaginationType("client");
    table.init();
    RefundApply.table = table;
});

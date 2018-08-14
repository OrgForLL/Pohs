/**
 * 商品管理初始化
 */
var ShareActivity = {
    id: "ShareActivityTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
ShareActivity.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: '广告名称', field: 'name', align: 'center', valign: 'middle', sortable: true},
        {title: '链接', field: 'link', align: 'center', valign: 'middle', sortable: true},
        {title: '广告位', field: 'positionName', align: 'center', valign: 'middle', sortable: true},
        {title: '排序', field: 'sequence', align: 'center', valign: 'middle', sortable: true},
        {title: '创建时间', field: 'createTime', align: 'center', valign: 'middle', sortable: true}];
};

/**
 * 检查是否选中
 */
ShareActivity.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        ShareActivity.seItem = selected[0];
        return true;
    }
};

/**
 * 打开分享配置页
 */
ShareActivity.openShareView = function () {
	if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '分享配置',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/share/shareActivity/'+ ShareActivity.seItem.id
        });
        layer.full(index);
        this.layerIndex = index;
    }
};
/**
 * 打开商品分享配置页
 */
ShareActivity.openShareActivityView = function () {
	var index = layer.open({
		type: 2,
		title: '默认配置',
		area: ['800px', '420px'], //宽高
		fix: false, //不固定
		maxmin: true,
		content: Feng.ctxPath + '/share/shareActivityView/'
	});
	layer.full(index);
	this.layerIndex = index;
};


/**
 *根据名称，编号查询商品
 */
ShareActivity.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    ShareActivity.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = ShareActivity.initColumn();
    var table = new BSTable(ShareActivity.id, "/advertising/list", defaultColunms);
    table.setPaginationType("client");
    table.init();
    ShareActivity.table = table;
});

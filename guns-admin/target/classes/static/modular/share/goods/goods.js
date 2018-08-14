/**
 * 商品管理初始化
 */
var ShareGoods = {
    id: "ShareGoodsTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
ShareGoods.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: '流水号', field: 'sn', align: 'center', valign: 'middle', sortable: true},
        {title: '名称', field: 'name', align: 'center', valign: 'middle', sortable: true},
        {title: '品牌', field: 'brandName', align: 'center', valign: 'middle', sortable: true},
        {title: '分类', field: 'categoryName', align: 'center', valign: 'middle', sortable: true},
        {title: '创建时间', field: 'createTime', align: 'center', valign: 'middle', sortable: true}];
};

/**
 * 检查是否选中
 */
ShareGoods.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        ShareGoods.seItem = selected[0];
        return true;
    }
};

/**
 * 打开分享配置页
 */
ShareGoods.openShareView = function () {
	if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '分享配置',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/share/shareView/'+ ShareGoods.seItem.id
        });
        layer.full(index);
        this.layerIndex = index;
    }
};
/**
 * 打开商品分享配置页
 */
ShareGoods.openShareGoodsView = function () {
	var index = layer.open({
		type: 2,
		title: '默认配置',
		area: ['800px', '420px'], //宽高
		fix: false, //不固定
		maxmin: true,
		content: Feng.ctxPath + '/share/shareGoodsView/'
	});
	layer.full(index);
	this.layerIndex = index;
};


/**
 *根据名称，编号查询商品
 */
ShareGoods.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    queryData['barcode']=$("#barcode").val();
    ShareGoods.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = ShareGoods.initColumn();
    var table = new BSTable(ShareGoods.id, "/goods/list", defaultColunms);
    table.setPaginationType("client");
    table.init();
    ShareGoods.table = table;
});

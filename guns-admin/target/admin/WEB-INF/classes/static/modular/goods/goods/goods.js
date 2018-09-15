/**
 * 商品管理初始化
 */
var Goods = {
    id: "GoodsTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Goods.initColumn = function () {
    return [
        {field: 'rows', radio: true},
        {title: '流水号', field: 'sn', align: 'center', valign: 'middle', sortable: true},
        {title: '名称', field: 'name', align: 'center', valign: 'middle', sortable: true},
        {title: '品牌', field: 'brandName', align: 'center', valign: 'middle', sortable: true},
        {title: '分类', field: 'categoryName', align: 'center', valign: 'middle', sortable: true},
        {title: '创建时间', field: 'createTime', align: 'center', valign: 'middle', sortable: true}];
};

/**
 * 检查是否选中
 */
Goods.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Goods.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加商品
 */
Goods.openAddGoods = function () {
    var index = layer.open({
        type: 2,
        title: '添加商品',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/goods/toAdd'
    });
    localStorage.setItem("paramGroups", "[]");
    localStorage.setItem("specGroups", "[]");
    localStorage.setItem("products", "[]");
    layer.full(index);
    this.layerIndex = index;
};

/**
 * 点击查看品牌详情（修改）
 */
Goods.openGoodsDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '商品详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/goods/toEdit/' + Goods.seItem.id
        });
        localStorage.setItem("paramGroups", "[]");
        localStorage.setItem("specGroups", "[]");
        localStorage.setItem("products", "[]");
        layer.full(index);
        this.layerIndex = index;
    }
};

/**
 * 删除商品
 */
Goods.delete = function () {
    if (this.check()) {
        var operation = function(){
            var ajax = new $ax(Feng.ctxPath + "/goods/delete", function () {
                //Feng.success("删除成功!");
                Goods.table.refresh();
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("goodsId",Goods.seItem.id);
            ajax.start();
        };
        Feng.confirm("是否刪除该商品?", operation);
    }
};


/**
 * 查询品牌列表
 */
Goods.search = function () {
	Goods.table.refresh({query: Goods.formParams()});
};

/**
 * 查询表单提交参数对象
 * @returns {{}}
 */
Goods.formParams = function() {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    queryData['barcode']=$("#barcode").val();
    return queryData;
}
/**
 * 商品价格配置
 */
Goods.assign = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '商品价格配置',
            area: ['300px', '450px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/priceTag/priceTag_assign/' + this.seItem.id
        });
        layer.full(index);
        this.layerIndex = index;
    }
};
/**
 * 展示价格配置
 */
Goods.setShowPrice = function () {
	if (this.check()) {
		var index = layer.open({
			type: 2,
			title: '展示价格配置',
			area: ['300px', '450px'], //宽高
			fix: false, //不固定
			maxmin: true,
			content: Feng.ctxPath + '/goods/showPrice/' + this.seItem.id
		});
		layer.full(index);
		this.layerIndex = index;
	}
};
$(function () {
    var defaultColunms = Goods.initColumn();
    var table = new BSTable(Goods.id, "/goods/list", defaultColunms);
    table.setPaginationType("server");
    table.setQueryParams(Goods.formParams());
    table.init();
    Goods.table = table;
});

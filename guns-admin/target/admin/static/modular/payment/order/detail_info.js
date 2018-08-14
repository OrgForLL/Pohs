/**
 * 初始化品牌详情对话框
 */
var OrderInfoDlg = {
	orderInfoData : {},
	orderItemsId : "OrderItemsTable",
	orderItems : null,
	refundApply : null,
	table : null,
	shippings : null
};

/**
 * 初始化订单明细表格的列
 */
OrderInfoDlg.initItemColumn = function() {
	return [ {
		field : 'selectItem',
		radio : true,
		visible : false
	}, {
		title : '商品规格名称',
		field : 'goodsName',
		align : 'center',
		valign : 'middle',
		sortable : false
	}, {
		title : '单价',
		field : 'actualPrice',
		align : 'center',
		valign : 'middle',
		sortable : true
	}, {
		title : '数量',
		field : 'quantity',
		align : 'center',
		valign : 'middle',
		sortable : true
	}, {
		title : '小计',
		field : 'amount',
		align : 'center',
		valign : 'middle',
		sortable : true
	}, {
		title : '操作',
		field : 'm',
		align : 'center',
		valign : 'middle',
		sortable : true,
		formatter : operateFormatter
	} ];
};

function operateFormatter(value, row, index) {
	console.log(row.id);
	console.log($("#status").val());
	console.log($("#refundOrderItemId").val());
	if (row.id == $("#refundOrderItemId").val()) {
		if ( $("#status").val() == 9) {
			return '<button id="refundBtn" onClick="RefundApply.agreeToRefund(\''
					+ $("#refundApplyId").val()
					+ '\')" icon="fa-check" type="button" class="btn btn-primary btn-xs">同意退货</button>';
		} else if ($("#status").val() == 7) {
			return '<button id="refundBtn" onClick="RefundApply.refundClick(\''
					+ $("#refundApplyId").val()
					+ '\')" icon="fa-check" type="button" class="btn btn-primary btn-xs">退款</button>';
		} else if ($("#status").val() == 10) {
			return '<button id="refundBtn" onClick="RefundApply.returnSuccess(\''
					+ $("#refundApplyId").val()
					+ '\')" icon="fa-check" type="button" class="btn btn-primary btn-xs">确认退货</button>';
		}
	}
	return null;
};

OrderInfoDlg.refundClick = function(id) {
	var index = layer.open({
		type : 2,
		title : '退款操作',
		area : [ '800px', '450px' ], // 宽高
		fix : false, // 不固定
		maxmin : true,
		content : Feng.ctxPath + '/refundApply/refund_view/' + id
	});
	this.layerIndex = index;
}

// 同意退货
OrderInfoDlg.agreeToRefund = function(id) {
	var operation = function() {
		var ajax = new $ax(Feng.ctxPath + "/refundApply/agreeToReturn",
				function() {
					Feng.success("操作成功!");
					OrderInfoDlg.table.refresh();
				}, function(data) {
					Feng.error("操作失败!" + data.responseJSON.message + "!");
				});
		ajax.set("refundApplyId", id);
		ajax.start();
	};
	Feng.confirm("是否同意退货?", operation);
}
// 已退货
OrderInfoDlg.returnSuccess = function(id) {
	var operation = function() {
		var ajax = new $ax(Feng.ctxPath + "/refundApply/returnSuccess",
				function() {
					Feng.success("操作成功!");
					OrderInfoDlg.table.refresh();
				}, function(data) {
					Feng.error("操作失败!" + data.responseJSON.message + "!");
				});
		ajax.set("refundApplyId", id);
		ajax.start();
	};

	Feng.confirm("是否确认该订单已收到退货？", operation);
}

/**
 * 获取对话框的数据
 */
OrderInfoDlg.get = function(key) {
	return $("#" + key).val();
}

/**
 * 设置对话框中的数据
 */
OrderInfoDlg.set = function(key, val) {
	this.orderInfoData[key] = (typeof value == "undefined") ? $("#" + key)
			.val() : value;
	return this;
}

$(function() {
	var defaultColunms = OrderInfoDlg.initItemColumn();
    var table = new BSTable(OrderInfoDlg.orderItemsId, "/order/orderItemList/" + $("#id").val(), defaultColunms);
    table.setPaginationType("client");
    table.init();
    OrderInfoDlg.table = table;
});

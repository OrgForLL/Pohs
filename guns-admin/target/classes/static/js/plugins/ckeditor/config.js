/**
 * @license Copyright (c) 2003-2017, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.md or http://ckeditor.com/license
 */

CKEDITOR.editorConfig = function( config ) {
	// Define changes to default configuration here. For example:
	// config.language = 'fr';
	// config.uiColor = '#AADC6E';
	//config.filebrowserUploadUrl= Feng.ctxPath + "/brand/ckedit";
	config.filebrowserUploadUrl= "http://app.wmggcl.com/admin/brand/ckedit";
	//console.log(config.filebrowserUploadUrl+"===============")
	config.language='zh-cn';
	config.height=300;
	config.font_names='宋体/宋体;黑体/黑体;仿宋/仿宋GB_2312;楷体/楷体_GB2312;隶书/隶书;幼圆/幼圆;微软雅黑/微软雅黑;';
	config.toolbarCanCollapse = true;
	config.toolbar = 'Basic';
};

@/*
    select标签中各个参数的说明:
    name : radio的名称
    id : radio的id
    radioname : radio的name
    underline : 是否带分割线
    value : 值
@*/
<div class="form-group">
    <label class="col-sm-3 control-label">${name}</label>
    <div class="col-sm-9">
    	@if(value == 1){
    		<input type="radio" name="${radioname}" value="1" checked>
    	@}else{
    		<input type="radio" name="${radioname}" value="1">
    	@}
    	<label>是</label>
    	@if(value == 0){
    		<input type="radio" name="${radioname}" value="0" checked>
    	@}else{
    		<input type="radio" name="${radioname}" value="0">
    	@}
		<label>否</label>
		<input type="hidden" id="${id}">
    </div>
</div>
@if(isNotEmpty(underline) && underline == 'true'){
    <div class="hr-line-dashed"></div>
@}
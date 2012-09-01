/**
 * 数据处理工具类，将xml字符串转换为html等
 */
define(["dojox/xml/parser",
        "dojo/_base/array"], function(
        		xmlParser,
        		array){
	
	var dataUtil = {};
	
	dataUtil.xmlDocToHtml = function(xmlDoc){
		var xmlString = "";
		var root = xmlDoc.documentElement;
		var lines = root.childNodes;
		array.forEach(lines, function(line, index){
			var lineString = "<div>"+line.textContent+"</div>";
			xmlString += lineString;
		});
		return xmlString;
	},
	
	dataUtil.xmlStringToHtml = function(xmlString){
		var doc = xmlParser.parse(xmlString);
		return this.xmlDocToHtml(doc);
	}
	
	return dataUtil;
	
});
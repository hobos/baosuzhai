define(["dojox/xml/parser",
        "dojo/_base/array"], function(
        		xmlParser,
        		array){
	// summary:
	//		数据处理工具类，将xml字符串转换为html等。
	var dataUtil = {};
	
	// summary:
	//		xml的格式为：
	//		<root><line><text></text><math></math></line></root>
	dataUtil.xmlDocToHtml = function(xmlDoc){
		var xmlString = "";
		var root = xmlDoc.documentElement;
		var lines = root.childNodes;
		array.forEach(lines, function(line, index){
			var lineString = "<div class='drip_line'>";
			var spans = line.childNodes;
			array.forEach(spans, function(span, index){
				if(span.nodeName == "text"){
					lineString += "<span>"+span.textContent+"</span>";
				}else if(span.nodeName == "math"){
					var tmp = xmlParser.innerXML(span);
					lineString += tmp.replace(/&amp;/g, "&");
				}
			});
			lineString += "</div>";
			
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
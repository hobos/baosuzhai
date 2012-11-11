// 显示活动列表
define(["dojo/_base/declare",
        "dojo/_base/array",
        "dojo/_base/lang",
        "dojo/_base/event",
        "dojo/dom-construct",
        "dojo/dom-prop",
        "dojo/query",
        "dojo/on",
        "dijit/_WidgetBase",
        "dijit/_TemplatedMixin",
        "dijit/form/Button",
        "dojo/store/JsonRest",
        "classCode",
        "drip/Editor",
        "dojo/text!/templates/ActivityNode.html",
        "dojo/text!/templates/ActivityList.html"], function(
        		declare,
        		array,
        		lang,
        		event,
        		domConstruct,
        		domProp,
        		query,
        		on,
        		_WidgetBase,
        		_TemplatedMixin,
        		Button,
        		JsonRest,
        		classCode,
        		Editor,
        		nodeTemplate,
        		listTemplate){
	
	// TODO：用户答题前，确保没有显示答案
	// TODO：“不做了”，恢复到答题前的状态
	
	// TODO:重复存在，重构
	var optionLabel = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	var ActivityNode = declare("ActivityNode",[_WidgetBase, _TemplatedMixin],{
		templateString: nodeTemplate,
		data:{},
		postCreate : function(){
			this.inherited(arguments);
			this._showActionLabel();
			
			// 为模板赋值
			var exerciseInfo = this.data.exercise;
			this._createExercise(exerciseInfo);
			// 针对不同的题型，有不同的渲染方式
			
			var exerType = exerciseInfo.exerType;
			var answerInfo = this.data.answer;
			if(answerInfo){
				if(this._isOptionExercise(exerType)){
					this._optionLabels = [];
					array.forEach(answerInfo.detail, lang.hitch(this,this._setOptionAnswer));
					// 即使是选择题，也要在答案面板中回答
					var answerDiv = domConstruct.create("div",{"class":"answer"}, this.exerciseNode,"after");
					answerDiv.innerHTML = "答案是："+"<span>"+ this._optionLabels.join(",") +"</span>";
				}
			}
			
			// 解答按钮
			on(this.btnAnswer,"click", lang.hitch(this, function(e){
				event.stop(e);
				
				// 如果是选择题
				if(this._isOptionExercise(exerType)){
					// 把所有option设置为有效
					this._getOptionEls().forEach(function(optEl,index){
						domProp.set(optEl,"disabled",false);
					});
				}
				
				// 需要 习题解析框， 习题答案框
				
				// 如果回答显示答案的习题时，应先把别人隐藏的答案删除掉
				
				var doAnswerPane = this.doAnswerPane = domConstruct.create("div",null, this.exerciseNode, "after");
				var guideDiv = domConstruct.create("div",{"class":"guide"}, doAnswerPane);
				var guideLabel = domConstruct.create("div",{innerHTML:"习题解析"},guideDiv);
				var editor = new Editor({style:"height:50px;width:98%"});
				editor.placeAt(guideDiv);
				// FIXME：当div中有float元素时，怎么让div的高度根据其中元素的高度自适应
				var btnContainer = domConstruct.create("div",{style:"height:18px"},doAnswerPane);
				
				var btnSave = new Button({"label":"保存", style:"float:right"});
				btnSave.placeAt(btnContainer);
				
				var btnCancel = new Button({"label":"不做了",style:"float:right"});
				btnCancel.placeAt(btnContainer);
				btnSave.on("click", function(e){
					alert("save");
				});
				btnCancel.on("click", lang.hitch(this,function(e){
					// 删除答题面板
					this._destroyAnswerPane();
				}));
				
				// 回答问题相关的dijit部件 
				var answerWidget = this._answerWidget = [];
				answerWidget.push(editor);
				answerWidget.push(btnSave);
				answerWidget.push(btnCancel);
			}));
		},
		
		_destroyAnswerPane: function(){
			// summary
			//		删除答题面板
			
			if(this._answerWidget){
				array.forEach(this._answerWidget, function(widget,index){
					widget.destroyRecursive();
				});
				delete this._answerWidget;
			}
			domConstruct.destroy(this.doAnswerPane);
		},
		
		_isOptionExercise: function(exerType){
			return exerType == classCode.ExerciseType.SINGLE_OPTION || exerType == classCode.ExerciseType.MULTI_OPTION;
		},
		
		_setOptionAnswer: function(answer, index){
			var optionId = answer.optionId;
			// TODO:从性能角度上将，可先缓存这些radio
			this._getOptionEls().some(lang.hitch(this,function(node,index){
				if(domProp.get(node,"optionId") == optionId){
					domProp.set(node,"checked", true);
					this._optionLabels.push(node.parentNode.nextSibling.firstChild.firstChild.innerHTML);
					return true;
				}
				return false;
			}));
		},
		
		_getOptionEls: function(){
			if(!this._optionEls){
				this._optionEls = query("input[type=radio]",this.table);
			}
			return this._optionEls;
		},
		
		_showActionLabel: function(){
			var data = this.data;
			
			var userName = data.displayUserName;
			this.userInfo.innerHTML = userName;
			this.action.innerHTML = classCode.ActionTypeMap[data.actionType];
			this.time.innerHTML = this._prettyTime(data.createTime);
			this.time.title = data.createTime;
		},
		
		_prettyTime: function(date){
			return date;
		},

		_createExercise: function(exerciseInfo){
			var _contentDiv = domConstruct.create("div", {innerHTML:exerciseInfo.content,"class":"content"},this.exerciseNode);
			
			var options = exerciseInfo.options;
			if(options && options.length > 0){
				var inputType = "radio";
				if(exerciseInfo.exerType == classCode.ExerciseType.SINGLE_OPTION){
					inputType = "radio";
				}else if(exerciseInfo.exerType == classCode.ExerciseType.MULTI_OPTION){
					inputType = "checkbox";
				}
					
				var _optionsDiv = domConstruct.create("div",{"class":"option"},this.exerciseNode);
				var table = this.table = domConstruct.create("table", null, _optionsDiv);
				// 循环填写options节点
				array.forEach(options,lang.hitch(this, this._createOption, table, inputType));
			}
		},
		
		_createOption: function(parentNode,inputType, data, index){
			console.log(parentNode, data, index);
			// 因为选项的name必须要与其他习题的区分开，所以应该使用部件id
			var inputId = this.id+"_"+data.id;
			var inputGroupName = "opt_"+this.id;
			
			var tr = domConstruct.place('<tr></tr>', parentNode);
			var td1 = domConstruct.place('<td></td>', tr);
			
			var input = domConstruct.create("input",{type:inputType,name:inputGroupName, id:inputId}, td1);
			domProp.set(input,{"disabled":true,optionId:data.id});
			var td2 = domConstruct.place('<td></td>', tr);
			var label = domConstruct.place('<label></label>',td2);
			domProp.set(label,"for",inputId);
			
			domConstruct.place('<span style="padding-right:5px">'+optionLabel.charAt(index)+'</span>', label);
			var divOptionContent = domConstruct.place("<span></span>",label);
			divOptionContent.innerHTML = data.content;
		}
		
	});
	
	var Activity = declare("Activity",[_WidgetBase, _TemplatedMixin],{
		templateString: listTemplate,
		 
		 store:new JsonRest({
			 target:"/activities/"
		 }),
		 
		 // 如果没有习题，则显示没有习题，
		 // 可扩展提示用户录入习题。
		 postCreate : function(){
			 this.inherited(arguments);
			 this.store.query(/*TODO:加入分页信息*/).then(lang.hitch(this, this._load));
		 },
		 
		 _load : function(items){
			 if(items.length == 0){
				 this.domNode.innerHTML = "没有活动";
			 }else{
				 console.log(items);
				 array.forEach(items, lang.hitch(this,function(item, index){
					 var node = new ActivityNode({
						 data : item
					 });
					 this.domNode.appendChild(node.domNode);
				 }));
				 // 使用mathjax进行呈现
				 MathJax.Hub.Queue(["Typeset",MathJax.Hub, this.domNode]);
			 }
		 }
		 
	});
	
	return Activity;
	
});
// 显示活动列表
define(["dojo/_base/declare",
        "dojo/_base/array",
        "dojo/_base/lang",
        "dojo/dom-construct",
        "dojo/dom-prop",
        "dojo/query",
        "dijit/_WidgetBase",
        "dijit/_TemplatedMixin",
        "dojo/store/JsonRest",
        "classCode",
        "dojo/text!/templates/ActivityNode.html",
        "dojo/text!/templates/ActivityList.html"], function(
        		declare,
        		array,
        		lang,
        		domConstruct,
        		domProp,
        		query,
        		_WidgetBase,
        		_TemplatedMixin,
        		JsonRest,
        		classCode,
        		nodeTemplate,
        		listTemplate){
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
				if(exerType == classCode.ExerciseType.SINGLE_OPTION || exerType == classCode.ExerciseType.MULTI_OPTION){
					this._optionLabels = [];
					array.forEach(answerInfo.detail, lang.hitch(this,this._setOptionAnswer));
					// 即使是选择题，也要在答案面板中回答
					var answerDiv = domConstruct.create("div",{"class":"answer"}, this.exerciseNode,"after");
					answerDiv.innerHTML = "答案是："+"<span>"+ this._optionLabels.join(",") +"</span>";
				}
				
			}
		},
		
		_setOptionAnswer: function(answer, index){
			var optionId = answer.optionId;
			// TODO:从性能角度上将，可先缓存这些radio
			this._getOptionEls().some(lang.hitch(this,function(node,index){
				if(domProp.get(node,"optionId") == optionId){
					domProp.set(node,"checked", true);
					this._optionLabels.push(node.parentNode.nextSibling.firstChild.innerHTML);
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
			var tr = domConstruct.place('<tr></tr>', parentNode);
			var td1 = domConstruct.place('<td></td>', tr);
			var input = domConstruct.create("input",{type:inputType}, td1);
			domProp.set(input,{"disabled":true,optionId:data.id});
			var td2 = domConstruct.place('<td></td>', tr);
			var label = domConstruct.place('<label>'+optionLabel.charAt(index)+'</label>', td2);
			var td3 = domConstruct.place('<td></td>', tr);
			td3.innerHTML = data.content;
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
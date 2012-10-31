define(["dojo/_base/declare",
        "dojo/_base/lang",
        "dojo/_base/array",
        "dojo/_base/event",
        "dojo/request/xhr",
        "dijit/_WidgetBase",
        "dijit/registry",
        "dojo/dom-construct",
        "dojo/dom-style",
        "dojo/dom-attr",
        "dojo/on",
        "dojo/query",
        "dojo/json",
        "drip/Editor",
        "classCode"], function(
        		declare,
        		lang,
        		array,
        		event,
        		xhr,
        		_WidgetBase,
        		registry,
        		domConstruct,
        		domStyle,
        		domAttr,
        		on,
        		query,
        		JSON,
        		Editor,
        		classCode){
	
	var optionLabel = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	return declare("drip.exercises.new",[_WidgetBase],{
		// summary:
		//		数据
		//		exerType:
		//		content:
		//		guide:
		//		options:[]
		//		answers:[]
		data:{},
		
		optionLength: 4,
		
		_optionName:"answer-option",
		
		postCreate: function(){
			this.inherited(arguments);
			
			
			this._createExerciseTypeBar();
			// 默认选中
			this._createSingleSelectForm();
			
			// 为保存按钮绑定事件
			var btnNewExercise = registry.byId("btnNewExercise");
			btnNewExercise.on("click",lang.hitch(this, this.doSave));
		},
		
		doSave: function(){
			// 保存之前要先校验
			var data = this.data;
			data.content = this.editorExerContent.get("value");
			data.guide = this.editorGuide.get("value");
			data.options = [];
			data.answers = [];
			query("[name="+this._optionName+"]:checked", this.tblOption).forEach(function(inputEl, index){
				console.log(inputEl, index);
				data.answers.push(inputEl.value);
			});
			registry.findWidgets(this.tblOption).forEach(function(widget, index){
				console.log(widget, index, widget.get("value"));
				data.options.push(widget.get("value"));
			});
			
			console.log("将要保存的习题数据为：",data);
			
			xhr("/exercises/",{method:"POST", data:JSON.stringify(data)}).then(lang.hitch(this,function(response){
				// 保存成功，在界面上清除用户输入数据，使处于新增状态。在页面给出保存成功的提示，在按钮旁边显示。
				this._reset();
			}),lang.hitch(this, function(error){
				// 保存失败，不清除用户输入数据，并给出详尽的错误提示
			}));
		},
		
		_reset: function(){
			this.data = {};
			this.editorExerContent.set("value","");
			this.editorGuide.set("value","");
			query("[name="+this._optionName+"]:checked", this.tblOption).forEach(function(inputEl, index){
				domAttr.set(inputEl,"checked", false);
			});
			registry.findWidgets(this.tblOption).forEach(function(widget, index){
				widget.set("value","");
			});
		},
		
		empty: function(){
			
		},
		
		/*
						<!-- optionId
						
						暂时决定不在这里录入习题解析。网站上的一切习题，只有大家回答的答案，没有绝对的标准答案。
						把录入习题和解答习题分为两个活动。
						可提供一个按钮，叫确定并解答习题。在解答习题的页面会给“习题解析”录入框。
						
						现在设计的是把录入习题和回答习题放在一个页面了。
						 -->
					</div>
					
			
		 */
		
		_createExerciseTypeBar: function(){
			
		},
		
		_createSingleSelectForm: function(){
			// summary:
			//		创建单选题form
			this.data.exerType = classCode.ExerciseType.SINGLE_OPTION;
			
			this._createContentInput();
			this._createImageInput();
			this._createOptionPane();
			this._createGuideInput();
		},
		
		_createContentInput: function(){
			// summary:
			//		创建习题内容输入框
			
			domConstruct.place('<div class="drip-title">内容</div>', this.domNode);
			this.editorExerContent = this._createEditor(this.domNode,150);
		},
		
		_createEditor: function(parentNode, height, width){
			var outer = domConstruct.place('<div style="padding: 3px; margin-bottom: 10px; border-radius: 3px; background: #eee"></div>', parentNode);
			if(width){
				domStyle.set(outer,{"width":width+"px"});
			}
			var inner = domConstruct.place('<div style="border: 1px solid #cacaca; background: #fbfbfb; padding-left: 10px; padding-right: 10px">',outer);
			var focusNode = domConstruct.place('<div style="padding-top: 5px; padding-bottom: 10px;"></div>',inner);
			var parms = {};
			parms.style = "height:"+height+"px;";
			var editor = new Editor(parms);
			editor.placeAt(focusNode);
			return editor;
		},
		
		_createImageInput: function(){
			// summary:
			//		创建图片上传输入框与图片编辑器。
			
			var title = domConstruct.place('<div class="drip-title" style="margin-bottom: 5px;"></div>', this.domNode);
			var aTitle = domConstruct.place('<a href="#">附图？</a>', title);
			var imageContainer = domConstruct.place('<div style="display: none">提供上传图片，或者直接在本地画图.当需要添加的时候，再撑开。</div>', this.domNode);
		},
		
		_createOptionPane: function(){
			// summary:
			//		创建习题选项面板
			
			domConstruct.place('<div class="drip-title">选项和答案</div>', this.domNode);
			var container = domConstruct.place('<div></div>', this.domNode);
			
			// 创建选项
			var table = this.tblOption = domConstruct.place('<table class="drip-exercise-option"></table', container);
			var defaultOptionLength = this.optionLength;
			for(var i = 0; i < defaultOptionLength; i++){
				this._createOption(table,i, "radio");
			}
			this._refreshOption();
			
			// 创建新建行按钮
			var addContainer = domConstruct.place('<div></div>', container);
			
			var aAdd = domConstruct.place('<a href="#"><img src="/resources/images/add_20.png"/></a>', addContainer);
			on(aAdd, "click", lang.hitch(this, function(e){
				this._createOption(table, this.optionLength++, "radio");
				event.stop(e);
			}));
		},
		
		_getOptionId: function(){
			
			if(!this.optionId){
				this.optionId = 0;
			}
			return this.optionId++;
		},
		
		_createOption: function(parentNode,index, inputType){
			var tr = domConstruct.place('<tr></tr>', parentNode);
			var td1 = domConstruct.place('<td></td>', tr);
			var optId = this._getOptionId();
			var input = domConstruct.place('<input type="'+inputType+'\" name="'+this._optionName+'"/>', td1);
			var td2 = domConstruct.place('<td></td>', tr);
			var label = domConstruct.place('<label>'+optionLabel.charAt(index)+'</label>', td2);
			var td3 = domConstruct.place('<td></td>', tr);
			var editor = this._createEditor(td3,15,550);
			var td4 = domConstruct.place('<td></td>', tr);
			var aDel = domConstruct.place('<a href="#"><img alt="删除" src="/resources/images/delete.png"></a>', td4);
			var aDown = domConstruct.place('<a href="#"><img alt="下移" src="/resources/images/arrow_down.gif"></a>', td4);
			var aUp = domConstruct.place('<a href="#"><img alt="上移" src="/resources/images/arrow_up.gif"></a>', td4);
			
			on(aDel, "click", lang.hitch(this, function(e){
				this.optionLength--;
				// 先删除tr中的所有dijit部件
				
				registry.findWidgets(tr).forEach(function(w,index){
					debugger;
					w.destroyRecursive();
				});
				domConstruct.destroy(tr);
				this._refreshOption();
				event.stop(e);
			}));
			
			on(aDown, "click", lang.hitch(this, function(e){
				if(tr.nextSibling){
					domConstruct.place(tr,tr.nextSibling, "after");
				}else{
					domConstruct.place(tr,parentNode,"first");
				}
				
				this._refreshOption();
				event.stop(e);
			}));
			
			on(aUp, "click", lang.hitch(this, function(e){
				if(tr.previousSibling){
					domConstruct.place(tr,tr.previousSibling, "before");
				}else{
					domConstruct.place(tr,parentNode,"last");
				}
				
				this._refreshOption();
				event.stop(e);
			}));
		},
		
		_refreshOption: function(){
			var trs = this.tblOption.childNodes;
			array.forEach(trs, function(tr, index){
				var label = optionLabel.charAt(index);
				var inputEl = tr.cells[0].firstChild;
				var id = "option"+(index+1);
				domAttr.set(inputEl,"id",id);
				inputEl.value = index;
				
				var labelEl = tr.cells[1].firstChild;
				domAttr.set(labelEl,"for",id);
				labelEl.innerHTML = label;
			});
		},
		
		_createGuideInput: function(){
			// summary:
			//		创建习题解析输入框
			
			domConstruct.place('<div class="drip-title">习题解析</div>', this.domNode);
			this.editorGuide = this._createEditor(this.domNode, 100);
		}
		
	});
	
});
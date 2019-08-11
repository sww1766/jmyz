$(function () {
    $("#jqGrid").jqGrid({
        url: '../sys/appversion/list',
        datatype: "json",
        colModel: [			
			{ label: 'ID', name: 'id', index: "id", width: 45, key: true },
			{ label: 'app版本等级', name: 'level', width: 75 },
			{ label: '更新级别编码', name: 'code', width: 80, formatter: function(value, options, row){
				if(value == 1){
					return '<span class="label label-success">需要更新</span>';
				}else if (value == 2){
					return '<span class="label label-success">强制更新</span>';
				}else if (value == 3){
					return '<span class="label label-success">不用更新</span>';
				}else{
					return '<span class="label label-success">暂无</span>';
				}
			}},
			{ label: '名称', name: 'name', width: 100 },
			{ label: '更新url', name: 'url', width: 100 },
			{ label: '创建时间', name: 'createTime', index: "create_time", width: 80}
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader : {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames : {
            page:"page", 
            rows:"limit", 
            order: "order"
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
    
    new AjaxUpload('#upload', {
        action: "../sys/appversion/upload",
        name: 'file',
        autoSubmit:true,
        responseType:"json",
        onSubmit:function(file, extension){
        },
        onComplete : function(file, r){
            if(r.code == 0){
                $("#pathId").val(r.path);
            }else{
                alert(r.msg);
            }
        }
    });
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title:null,
		appversion:{
		}
	},
	methods: {
		add: function(){
			vm.showList = false;
			vm.title = "新增";
		},
		update: function () {
            var id = getSelectedRow();
			if(id == null){
				return ;
			}
			
			vm.showList = false;
            vm.title = "修改";
			
			vm.getAppversion(id);
		},
		getAppversion: function(id){
			$.get("../sys/appversion/info/"+id, function(r){
				vm.appversion = r.appversion;
			});
		},
		del: function () {
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../sys/appversion/delete",
				    data: JSON.stringify(ids),
				    success: function(r){
						if(r.code == 0){
							alert('操作成功', function(index){
                                vm.reload();
							});
						}else{
							alert(r.msg);
						}
					}
				});
			});
		},
		saveOrUpdate: function (event) {
			var url = vm.appversion.id == null ? "../sys/appversion/save" : "../sys/appversion/update";
			vm.appversion.url = $("#pathId").val();
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.appversion),
			    success: function(r){
			    	if(r.code === 0){
						alert('操作成功', function(index){
							vm.reload();
						});
					}else{
						alert(r.msg);
					}
				}
			});
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:page
            }).trigger("reloadGrid");
		}
	}
});

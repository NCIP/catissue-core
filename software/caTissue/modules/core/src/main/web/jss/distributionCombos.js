/*
 * Ext JS Library 2.0.2
 * Copyright(c) 2006-2008, Ext JS, LLC.
 * licensing@extjs.com
 *
 * http://extjs.com/license
 */

Ext.onReady(function(){
   Ext.QuickTips.init();

  var myUrl= 'DistributeComboDataAction.do';
  var ds = new Ext.data.Store({proxy: new Ext.data.HttpProxy({url: myUrl}),
        reader: new Ext.data.JsonReader({root: 'row',totalProperty: 'totalCount',id: 'id'
        }, [{name: 'excerpt', mapping: 'field'}])
    });
      var combo = new Ext.form.ComboBox({
        store: ds,displayField:'excerpt',typeAhead: false,width: 570, pageSize:15,forceSelection: true ,
		queryParam : 'query',mode: 'remote',triggerAction: 'all',minChars : 3,queryDelay : 300,
		typeAheadDelay : 900,selectOnFocus:true,tpl: '<tpl for="."><div ext:qtip="{excerpt}" class="x-combo-list-item">{excerpt}</div></tpl>',
        applyTo: 'distributionProtocol',fields: ['id_cp'],id: 'idjs_cp'
   });
 combo.on("expand", function(){if(Ext.isIE || Ext.isIE7){combo.list.setStyle("width", "550");combo.innerList.setStyle("width", "550");}else{combo.list.setStyle("width", "550");combo.innerList.setStyle("width", "550");}}, {single: true});

  combo.on('select', function(){
				//this will change rel cpe combo's url to updated colprotoid
				var appli = Ext.getCmp('idjs_cp').getValue();
				if("-- Select --" == appli)
				{
					var form = Ext.getCmp('idjs_cp');
					form.store.proxy.conn.url = 'DistributeComboDataAction.do?';
					form.store.reload({params:{start:0, limit:15,query:''}});
				}
 	});
});